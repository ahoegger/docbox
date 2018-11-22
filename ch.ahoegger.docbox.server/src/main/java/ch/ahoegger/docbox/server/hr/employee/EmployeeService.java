package ch.ahoegger.docbox.server.hr.employee;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.ch.ahoegger.docbox.server.or.app.tables.Address;
import org.ch.ahoegger.docbox.server.or.app.tables.Employee;
import org.ch.ahoegger.docbox.server.or.app.tables.Partner;
import org.ch.ahoegger.docbox.server.or.app.tables.records.EmployeeRecord;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.server.jdbc.SQL;
import org.eclipse.scout.rt.shared.servicetunnel.RemoteServiceAccessDenied;
import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.SelectConditionStep;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.shared.backup.IBackupService;
import ch.ahoegger.docbox.shared.hr.IAddressService;
import ch.ahoegger.docbox.shared.hr.employee.EmployeeFormData;
import ch.ahoegger.docbox.shared.hr.employee.EmployeeSearchFormData;
import ch.ahoegger.docbox.shared.hr.employee.EmployeeTableData;
import ch.ahoegger.docbox.shared.hr.employee.EmployeeTableData.EmployeeTableRowData;
import ch.ahoegger.docbox.shared.hr.employee.IEmployeeService;
import ch.ahoegger.docbox.shared.partner.IPartnerService;
import ch.ahoegger.docbox.shared.partner.PartnerFormData;

/**
 * <h3>{@link EmployeeService}</h3>
 *
 * @author Andreas Hoegger
 */
public class EmployeeService implements IEmployeeService {
  private static final Logger LOG = LoggerFactory.getLogger(EmployeeService.class);

  @Override
  public EmployeeTableData getTableData(EmployeeSearchFormData formData) {

    Employee emp = Employee.EMPLOYEE.as("emp");
    Partner ptr = Partner.PARTNER.as("ptr");
    Address employeeAddress = Address.ADDRESS.as("employeeAddress");

    Condition condition = DSL.trueCondition();

    // search criteria firstname
    if (StringUtility.hasText(formData.getFirstName().getValue())) {
      condition = condition.and(emp.FIRST_NAME.upper().contains(formData.getFirstName().getValue().toUpperCase()));
    }
    // lastname
    if (StringUtility.hasText(formData.getLastName().getValue())) {
      condition = condition.and(emp.LAST_NAME.contains(formData.getLastName().getValue()));
    }
    // seach criteria active

    if (formData.getActiveBox().getValue() != null) {
      switch (formData.getActiveBox().getValue()) {
        case TRUE:

          condition = condition.and(ptr.END_DATE.isNull().or(ptr.END_DATE.ge(new Date())));

          break;
        case FALSE:
          condition = condition.and(ptr.END_DATE.lessThan(new Date()));
          break;
      }
    }

    // birthday from
    if (formData.getBirtheayFrom().getValue() != null) {
      condition = condition.and(emp.BIRTHDAY.ge(formData.getBirtheayFrom().getValue()));
    }
    // birthday to
    if (formData.getBirthdayTo().getValue() != null) {
      condition = condition.and(emp.BIRTHDAY.le(formData.getBirthdayTo().getValue()));
    }

    SelectConditionStep<Record> statement = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .select(emp.PARTNER_NR, emp.FIRST_NAME, emp.LAST_NAME, employeeAddress.LINE_1, employeeAddress.LINE_2, employeeAddress.PLZ, employeeAddress.CITY, emp.AHV_NUMBER, emp.ACCOUNT_NUMBER, emp.BIRTHDAY, emp.HOURLY_WAGE)
        .select(ptr.NAME, ptr.START_DATE, ptr.END_DATE)
        .from(emp)
        .leftOuterJoin(ptr)
        .on(emp.PARTNER_NR.eq(ptr.PARTNER_NR))
        .leftOuterJoin(employeeAddress).on(employeeAddress.ADDRESS_NR.eq(emp.ADDRESS_NR))
        .where(condition);

    List<EmployeeTableRowData> rows = statement.fetch()
        .stream()
        .map(rec -> {
          EmployeeTableRowData row = new EmployeeTableRowData();
          row.setPartnerId(rec.get(emp.PARTNER_NR));
          row.setFirstName(emp.FIRST_NAME.get(rec));
          row.setLastName(emp.LAST_NAME.get(rec));
          row.setAddressLine1(employeeAddress.LINE_1.get(rec));
          row.setAddressLine2(employeeAddress.LINE_2.get(rec));
          row.setPlz(employeeAddress.PLZ.get(rec));
          row.setCity(employeeAddress.CITY.get(rec));
          row.setAHVNumber(emp.AHV_NUMBER.get(rec));
          row.setAccountNumber(emp.ACCOUNT_NUMBER.get(rec));
          row.setBirthday(emp.BIRTHDAY.get(rec));
          row.setHourlyWage(emp.HOURLY_WAGE.get(rec));
          row.setDisplayName(ptr.NAME.get(rec));
          row.setStartDate(ptr.START_DATE.get(rec));
          row.setEndDate(ptr.END_DATE.get(rec));
          return row;
        }).collect(Collectors.toList());

    EmployeeTableData tableData = new EmployeeTableData();
    tableData.setRows(rows.toArray(new EmployeeTableRowData[]{}));
    return tableData;
  }

  @Override
  public EmployeeFormData prepareCreate(EmployeeFormData formData) {

    return formData;
  }

  @Override
  public EmployeeFormData create(EmployeeFormData formData) {
    IPartnerService partnerService = BEANS.get(IPartnerService.class);
    // partner
    BigDecimal partnerId = formData.getPartnerId();
    if (partnerId == null) {
      // create partner
      PartnerFormData partnerData = new PartnerFormData();
      partnerService.prepareCreate(partnerData);
      formData.getPartnerGroupBox();
      partnerData.getPartnerBox().getName().setValue(formData.getPartnerGroupBox().getName().getValue());
      partnerData.getPartnerBox().getDescription().setValue(formData.getPartnerGroupBox().getDescription().getValue());
      partnerData.getPartnerBox().getStartDate().setValue(formData.getPartnerGroupBox().getStartDate().getValue());
      partnerData.getPartnerBox().getEndDate().setValue(formData.getPartnerGroupBox().getEndDate().getValue());
      partnerData = partnerService.create(partnerData);
      if (partnerData == null) {
        return null;
      }
      partnerId = partnerData.getPartnerId();
    }
    formData.setPartnerId(partnerId);

    // address
    BEANS.get(IAddressService.class).create(formData.getEmployeeBox().getAddressBox());

    // employee
    int rowCount = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .executeInsert(toRecord(formData));

    if (rowCount == 1) {
      // notify backup needed
      BEANS.get(IBackupService.class).notifyModification();

      return formData;
    }

    return null;
  }

  @Override
  public EmployeeFormData load(EmployeeFormData formData) {

    Employee emp = Employee.EMPLOYEE.as("EMP");

    EmployeeRecord empRec = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .fetchOne(emp, emp.PARTNER_NR.eq(formData.getPartnerId()));
    if (empRec == null) {
      return null;
    }
    fillFormData(empRec, formData);

    // partner
    PartnerFormData partnerData = new PartnerFormData();
    partnerData.setPartnerId(formData.getPartnerId());
    partnerData = BEANS.get(IPartnerService.class).load(partnerData);
    formData.getPartnerGroupBox().getName().setValue(partnerData.getPartnerBox().getName().getValue());
    formData.getPartnerGroupBox().getDescription().setValue(partnerData.getPartnerBox().getDescription().getValue());
    formData.getPartnerGroupBox().getStartDate().setValue(partnerData.getPartnerBox().getStartDate().getValue());
    formData.getPartnerGroupBox().getEndDate().setValue(partnerData.getPartnerBox().getEndDate().getValue());

    // address
    BEANS.get(IAddressService.class).load(formData.getEmployeeBox().getAddressBox());

    return formData;

  }

  @Override
  public EmployeeFormData store(EmployeeFormData formData) {
    // partner
    PartnerFormData partnerData = new PartnerFormData();
    partnerData.setPartnerId(formData.getPartnerId());
    partnerData.getPartnerBox().getName().setValue(formData.getPartnerGroupBox().getName().getValue());
    partnerData.getPartnerBox().getDescription().setValue(formData.getPartnerGroupBox().getDescription().getValue());
    partnerData.getPartnerBox().getStartDate().setValue(formData.getPartnerGroupBox().getStartDate().getValue());
    partnerData.getPartnerBox().getEndDate().setValue(formData.getPartnerGroupBox().getEndDate().getValue());
    if (BEANS.get(IPartnerService.class).store(partnerData) == null) {
      return null;
    }
    // address
    BEANS.get(IAddressService.class).store(formData.getEmployeeBox().getAddressBox());

    int rowCount = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .executeUpdate(toRecord(formData));

    if (rowCount == 1) {
      // notify backup needed
      BEANS.get(IBackupService.class).notifyModification();

      return formData;
    }
    return null;
  }

  @RemoteServiceAccessDenied
  public int insert(Connection connection, BigDecimal partnerId, String firstName, String lastName, BigDecimal addressId, String ahvNumber, String accountNumber, Date birthday, BigDecimal hourlyWage,
      BigDecimal socialInsuranceRate, BigDecimal sourceTaxRate, BigDecimal vacationExtraRate,
      BigDecimal employerId) {
    return DSL.using(connection, SQLDialect.DERBY)
        .executeInsert(toRecord(partnerId, firstName, lastName, addressId, ahvNumber, accountNumber, birthday, hourlyWage, socialInsuranceRate, sourceTaxRate, vacationExtraRate, employerId));

  }

  protected EmployeeFormData fillFormData(EmployeeRecord rec, EmployeeFormData fd) {
    fd.setPartnerId(rec.getPartnerNr());
    fd.getEmployer().setValue(rec.getEmployerNr());
    fd.getEmployeeBox().getFirstName().setValue(rec.getFirstName());
    fd.getEmployeeBox().getLastName().setValue(rec.getLastName());
    fd.getEmployeeBox().getAddressBox().setAddressId(rec.getAddressNr());
    fd.getEmployeeBox().getAhvNumber().setValue(rec.getAhvNumber());
    fd.getEmployeeBox().getAccountNumber().setValue(rec.getAccountNumber());
    fd.getEmployeeBox().getBirthday().setValue(rec.getBirthday());
    fd.getEmploymentBox().getHourlyWage().setValue(rec.getHourlyWage());
    fd.getEmploymentBox().getSocialInsuranceRate().setValue(rec.getSocialInsuranceRate());
    fd.getEmploymentBox().getSourceTaxRate().setValue(rec.getSourceTaxRate());
    fd.getEmploymentBox().getVacationExtraRate().setValue(rec.getVacationExtraRate());
    return fd;
  }

  protected EmployeeRecord toRecord(EmployeeFormData fd) {
    return toRecord(fd.getPartnerId(), fd.getEmployeeBox().getFirstName().getValue(), fd.getEmployeeBox().getLastName().getValue(), fd.getEmployeeBox().getAddressBox().getAddressId(),
        fd.getEmployeeBox().getAhvNumber().getValue(), fd.getEmployeeBox().getAccountNumber().getValue(), fd.getEmployeeBox().getBirthday().getValue(), fd.getEmploymentBox().getHourlyWage().getValue(),
        fd.getEmploymentBox().getSocialInsuranceRate().getValue(), fd.getEmploymentBox().getSourceTaxRate().getValue(), fd.getEmploymentBox().getVacationExtraRate().getValue(),
        fd.getEmployer().getValue());
  }

  protected EmployeeRecord toRecord(BigDecimal partnerId, String firstName, String lastName, BigDecimal addressId, String ahvNumber, String accountNumber, Date birthday, BigDecimal hourlyWage,
      BigDecimal socialInsuranceRate, BigDecimal sourceTaxRate, BigDecimal vacationExtraRate,
      BigDecimal employerId) {
    return mapToRecord(new EmployeeRecord(), partnerId, firstName, lastName, addressId, ahvNumber, accountNumber, birthday, hourlyWage, socialInsuranceRate, sourceTaxRate, vacationExtraRate, employerId);
  }

  protected EmployeeRecord mapToRecord(EmployeeRecord rec, BigDecimal partnerId, String firstName, String lastName, BigDecimal addressId, String ahvNumber, String accountNumber, Date birthday, BigDecimal hourlyWage,
      BigDecimal socialInsuranceRate, BigDecimal sourceTaxRate, BigDecimal vacationExtraRate,
      BigDecimal employerId) {
    Employee t = Employee.EMPLOYEE;
    return rec
        .with(t.EMPLOYER_NR, employerId)
        .with(t.ACCOUNT_NUMBER, accountNumber)
        .with(t.ADDRESS_NR, addressId)
        .with(t.AHV_NUMBER, ahvNumber)
        .with(t.BIRTHDAY, birthday)
        .with(t.FIRST_NAME, firstName)
        .with(t.HOURLY_WAGE, hourlyWage)
        .with(t.SOCIAL_INSURANCE_RATE, socialInsuranceRate)
        .with(t.SOURCE_TAX_RATE, sourceTaxRate)
        .with(t.VACATION_EXTRA_RATE, vacationExtraRate)
        .with(t.LAST_NAME, lastName)
        .with(t.PARTNER_NR, partnerId);
  }

}

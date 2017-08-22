package ch.ahoegger.docbox.server.hr.employee;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
import ch.ahoegger.docbox.shared.hr.employee.EmployeeFormData;
import ch.ahoegger.docbox.shared.hr.employee.IEmployeeService;
import ch.ahoegger.docbox.shared.hr.employer.EmployeeSearchFormData;
import ch.ahoegger.docbox.shared.hr.employer.EmployeeTableData;
import ch.ahoegger.docbox.shared.hr.employer.EmployeeTableData.EmployeeTableRowData;
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

    SelectConditionStep<Record> statement = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .select(emp.PARTNER_NR, emp.FIRST_NAME, emp.LAST_NAME, emp.ADDRESS_LINE1, emp.ADDRESS_LINE2, emp.AHV_NUMBER, emp.ACCOUNT_NUMBER, emp.HOURLY_WAGE)
        .select(ptr.NAME, ptr.START_DATE, ptr.END_DATE)
        .from(emp)
        .leftOuterJoin(ptr)
        .on(emp.PARTNER_NR.eq(ptr.PARTNER_NR))
        .where(condition);

    List<EmployeeTableRowData> rows = statement.fetch()
        .stream()
        .map(rec -> {
          EmployeeTableRowData row = new EmployeeTableRowData();
          row.setPartnerId(rec.get(emp.PARTNER_NR));
          row.setFirstName(emp.FIRST_NAME.get(rec));
          row.setLastName(emp.LAST_NAME.get(rec));
          row.setAddressLine1(emp.ADDRESS_LINE1.get(rec));
          row.setAddressLine2(emp.ADDRESS_LINE2.get(rec));
          row.setAHVNumber(emp.AHV_NUMBER.get(rec));
          row.setAccountNumber(emp.ACCOUNT_NUMBER.get(rec));
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
    Partner p = Partner.PARTNER.as("P");

    Record empRec = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .select(emp.PARTNER_NR, emp.FIRST_NAME, emp.LAST_NAME, emp.ADDRESS_LINE1, emp.ADDRESS_LINE2, emp.AHV_NUMBER, emp.ACCOUNT_NUMBER, emp.HOURLY_WAGE, emp.SOCIAL_INSURANCE_RATE, emp.SOURCE_TAX_RATE, emp.VACATION_EXTRA_RATE,
            emp.EMPLOYER_ADDRESS_LINE1, emp.EMPLOYER_ADDRESS_LINE2, emp.EMPLOYER_ADDRESS_LINE3, emp.EMPLOYER_EMAIL, emp.EMPLOYER_PHONE)
        .select(p.NAME, p.DESCRIPTION, p.START_DATE, p.END_DATE)
        .from(emp)
        .leftOuterJoin(p)
        .on(p.PARTNER_NR.eq(emp.PARTNER_NR))
        .where(emp.PARTNER_NR.eq(formData.getPartnerId()))
        .fetchOne();
    if (empRec == null) {
      return null;
    }
    EmployeeFormData result = empRec
        .map(rec -> {
          EmployeeFormData fd = new EmployeeFormData();
          fd.setPartnerId(rec.get(emp.PARTNER_NR));
          fd.getEmployeeBox().getFirstName().setValue(rec.get(emp.FIRST_NAME));
          fd.getEmployeeBox().getLastName().setValue(rec.get(emp.LAST_NAME));
          fd.getEmployeeBox().getAddressLine1().setValue(rec.get(emp.ADDRESS_LINE1));
          fd.getEmployeeBox().getAddressLine2().setValue(rec.get(emp.ADDRESS_LINE2));
          fd.getEmployeeBox().getAhvNumber().setValue(rec.get(emp.AHV_NUMBER));
          fd.getEmployeeBox().getAccountNumber().setValue(rec.get(emp.ACCOUNT_NUMBER));
          fd.getEmploymentBox().getHourlyWage().setValue(rec.get(emp.HOURLY_WAGE));
          fd.getEmploymentBox().getSocialInsuranceRate().setValue(rec.get(emp.SOCIAL_INSURANCE_RATE));
          fd.getEmploymentBox().getSourceTaxRate().setValue(rec.get(emp.SOURCE_TAX_RATE));
          fd.getEmploymentBox().getVacationExtraRate().setValue(rec.get(emp.VACATION_EXTRA_RATE));

          fd.getEmployerBox().getAddressLine1().setValue(rec.get(emp.EMPLOYER_ADDRESS_LINE1));
          fd.getEmployerBox().getAddressLine2().setValue(rec.get(emp.EMPLOYER_ADDRESS_LINE2));
          fd.getEmployerBox().getAddressLine3().setValue(rec.get(emp.EMPLOYER_ADDRESS_LINE3));
          fd.getEmployerBox().getEmail().setValue(rec.get(emp.EMPLOYER_EMAIL));
          fd.getEmployerBox().getPhone().setValue(rec.get(emp.EMPLOYER_PHONE));
          fd.getPartnerGroupBox().getName().setValue(rec.get(p.NAME));
          fd.getPartnerGroupBox().getDescription().setValue(rec.get(p.DESCRIPTION));
          fd.getPartnerGroupBox().getStartDate().setValue(rec.get(p.START_DATE));
          fd.getPartnerGroupBox().getEndDate().setValue(rec.get(p.END_DATE));
          return fd;
        });

    return result;

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
  public int insert(Connection connection, BigDecimal partnerId, String firstName, String lastName, String addressLine1, String addressLine2, String ahvNumber, String accountNumber, BigDecimal hourlyWage,
      BigDecimal socialInsuranceRate, BigDecimal sourceTaxRate, BigDecimal vacationExtraRate,
      String employerAddressLine1, String employerAddressLine2, String employerAddressLine3, String employerEmail, String employerPhone) {
    return DSL.using(connection, SQLDialect.DERBY)
        .executeInsert(toRecord(partnerId, firstName, lastName, addressLine1, addressLine2, ahvNumber, accountNumber, hourlyWage, socialInsuranceRate, sourceTaxRate, vacationExtraRate, employerAddressLine1, employerAddressLine2,
            employerAddressLine3, employerEmail, employerPhone));

  }

  protected EmployeeRecord toRecord(EmployeeFormData fd) {
    return toRecord(fd.getPartnerId(), fd.getEmployeeBox().getFirstName().getValue(), fd.getEmployeeBox().getLastName().getValue(), fd.getEmployeeBox().getAddressLine1().getValue(), fd.getEmployeeBox().getAddressLine2().getValue(),
        fd.getEmployeeBox().getAhvNumber().getValue(), fd.getEmployeeBox().getAccountNumber().getValue(), fd.getEmploymentBox().getHourlyWage().getValue(),
        fd.getEmploymentBox().getSocialInsuranceRate().getValue(), fd.getEmploymentBox().getSourceTaxRate().getValue(), fd.getEmploymentBox().getVacationExtraRate().getValue(),
        fd.getEmployerBox().getAddressLine1().getValue(),
        fd.getEmployerBox().getAddressLine2().getValue(), fd.getEmployerBox().getAddressLine3().getValue(), fd.getEmployerBox().getEmail().getValue(),
        fd.getEmployerBox().getPhone().getValue());
  }

  protected EmployeeRecord toRecord(BigDecimal partnerId, String firstName, String lastName, String addressLine1, String addressLine2, String ahvNumber, String accountNumber, BigDecimal hourlyWage,
      BigDecimal socialInsuranceRate, BigDecimal sourceTaxRate, BigDecimal vacationExtraRate,
      String employerAddressLine1, String employerAddressLine2, String employerAddressLine3, String employerEmail, String employerPhone) {
    return mapToRecord(new EmployeeRecord(), partnerId, firstName, lastName, addressLine1, addressLine2, ahvNumber, accountNumber, hourlyWage, socialInsuranceRate, sourceTaxRate, vacationExtraRate, employerAddressLine1,
        employerAddressLine2, employerAddressLine3, employerEmail, employerPhone);
  }

  protected EmployeeRecord mapToRecord(EmployeeRecord rec, BigDecimal partnerId, String firstName, String lastName, String addressLine1, String addressLine2, String ahvNumber, String accountNumber, BigDecimal hourlyWage,
      BigDecimal socialInsuranceRate, BigDecimal sourceTaxRate, BigDecimal vacationExtraRate,
      String employerAddressLine1, String employerAddressLine2, String employerAddressLine3, String employerEmail, String employerPhone) {
    Employee t = Employee.EMPLOYEE;
    return rec
        .with(t.ACCOUNT_NUMBER, accountNumber)
        .with(t.ADDRESS_LINE1, addressLine1)
        .with(t.ADDRESS_LINE2, addressLine2)
        .with(t.AHV_NUMBER, ahvNumber)
        .with(t.EMPLOYER_ADDRESS_LINE1, employerAddressLine1)
        .with(t.EMPLOYER_ADDRESS_LINE2, employerAddressLine2)
        .with(t.EMPLOYER_ADDRESS_LINE3, employerAddressLine3)
        .with(t.EMPLOYER_EMAIL, employerEmail)
        .with(t.EMPLOYER_PHONE, employerPhone)
        .with(t.FIRST_NAME, firstName)
        .with(t.HOURLY_WAGE, hourlyWage)
        .with(t.SOCIAL_INSURANCE_RATE, socialInsuranceRate)
        .with(t.SOURCE_TAX_RATE, sourceTaxRate)
        .with(t.VACATION_EXTRA_RATE, vacationExtraRate)
        .with(t.LAST_NAME, lastName)
        .with(t.PARTNER_NR, partnerId);
  }

}

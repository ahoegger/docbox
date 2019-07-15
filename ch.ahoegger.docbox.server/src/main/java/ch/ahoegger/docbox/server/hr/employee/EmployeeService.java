package ch.ahoegger.docbox.server.hr.employee;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.ch.ahoegger.docbox.server.or.app.tables.Address;
import org.ch.ahoegger.docbox.server.or.app.tables.Employee;
import org.ch.ahoegger.docbox.server.or.app.tables.EmployeeTaxGroup;
import org.ch.ahoegger.docbox.server.or.app.tables.Partner;
import org.ch.ahoegger.docbox.server.or.app.tables.records.EmployeeRecord;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.server.jdbc.SQL;
import org.eclipse.scout.rt.shared.servicetunnel.RemoteServiceAccessDenied;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.SelectConditionStep;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.server.hr.statement.StatementBean;
import ch.ahoegger.docbox.server.jooq.FieldValidators;
import ch.ahoegger.docbox.shared.backup.IBackupService;
import ch.ahoegger.docbox.shared.hr.IAddressService;
import ch.ahoegger.docbox.shared.hr.employee.EmployeeFormData;
import ch.ahoegger.docbox.shared.hr.employee.EmployeeSearchFormData;
import ch.ahoegger.docbox.shared.hr.employee.EmployeeTableData;
import ch.ahoegger.docbox.shared.hr.employee.EmployeeTableData.EmployeeTableRowData;
import ch.ahoegger.docbox.shared.hr.employee.IEmployeeService;
import ch.ahoegger.docbox.shared.partner.IPartnerService;
import ch.ahoegger.docbox.shared.partner.PartnerFormData;
import ch.ahoegger.docbox.shared.template.AbstractAddressBoxData;

/**
 * <h3>{@link EmployeeService}</h3>
 *
 * @author Andreas Hoegger
 */
public class EmployeeService implements IEmployeeService {
  @SuppressWarnings("unused")
  private static final Logger LOG = LoggerFactory.getLogger(EmployeeService.class);

  public static Field<String> createDisplayNameForAlias(Employee t) {
    return DSL.concat(t.FIRST_NAME, DSL.val(" "), t.LAST_NAME).as("DISPLAY_NAME");
  }

  @Override
  public EmployeeTableData getTableData(EmployeeSearchFormData formData) {

    Employee emp = Employee.EMPLOYEE.as("emp");
    Partner ptr = Partner.PARTNER.as("ptr");
    Address employeeAddress = Address.ADDRESS.as("employeeAddress");

    Condition condition = DSL.trueCondition();

    // search criteria employer
    if (formData.getEmployer().getValue() != null) {
      condition = condition.and(emp.EMPLOYER_NR.eq(formData.getEmployer().getValue()));
    }
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
        .select(emp.EMPLOYEE_NR, emp.FIRST_NAME, emp.LAST_NAME, employeeAddress.LINE_1, employeeAddress.LINE_2, employeeAddress.PLZ, employeeAddress.CITY, emp.AHV_NUMBER, emp.ACCOUNT_NUMBER, emp.TAX_TYPE, emp.BIRTHDAY, emp.HOURLY_WAGE)
        .select(ptr.NAME, ptr.START_DATE, ptr.END_DATE)
        .from(emp)
        .leftOuterJoin(ptr)
        .on(emp.EMPLOYEE_NR.eq(ptr.PARTNER_NR))
        .leftOuterJoin(employeeAddress).on(employeeAddress.ADDRESS_NR.eq(emp.ADDRESS_NR))
        .where(condition);

    List<EmployeeTableRowData> rows = statement.fetch()
        .stream()
        .map(rec -> {
          EmployeeTableRowData row = new EmployeeTableRowData();
          row.setPartnerId(rec.get(emp.EMPLOYEE_NR));
          row.setFirstName(emp.FIRST_NAME.get(rec));
          row.setLastName(emp.LAST_NAME.get(rec));
          row.setAddressLine1(employeeAddress.LINE_1.get(rec));
          row.setAddressLine2(employeeAddress.LINE_2.get(rec));
          row.setPlz(employeeAddress.PLZ.get(rec));
          row.setCity(employeeAddress.CITY.get(rec));
          row.setAHVNumber(emp.AHV_NUMBER.get(rec));
          row.setAccountNumber(emp.ACCOUNT_NUMBER.get(rec));
          row.setTaxType(emp.TAX_TYPE.get(rec));
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

    Employee employee = Employee.EMPLOYEE;
    FieldValidators.notNullValidator()
        .with(employee.EMPLOYER_NR, formData.getEmployer().getValue())
        .with(employee.FIRST_NAME, formData.getEmployeeBox().getFirstName().getValue())
        .with(employee.LAST_NAME, formData.getEmployeeBox().getLastName().getValue())
        .with(employee.TAX_TYPE, formData.getEmploymentBox().getTaxType().getValue())
        .with(employee.REDUCED_LUNCH, formData.getEmploymentBox().getReducedLunch().getValue())
        .validateAndThrow();

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
    AbstractAddressBoxData addressData = BEANS.get(IAddressService.class).create(formData.getEmployeeBox().getAddressBox());
    formData.getEmployeeBox().getAddressBox().setAddressId(addressData.getAddressId());

    // employee
    int rowCount = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .executeInsert(mapToRecord(employee.newRecord(), formData));

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
        .fetchOne(emp, emp.EMPLOYEE_NR.eq(formData.getPartnerId()));
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

    Employee employee = Employee.EMPLOYEE;
    EmployeeRecord rec = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .fetchOne(employee, employee.EMPLOYEE_NR.eq(formData.getPartnerId()));

    int rowCount = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .executeUpdate(mapToRecord(rec, formData));

    if (rowCount == 1) {
      // notify backup needed
      BEANS.get(IBackupService.class).notifyModification();

      return formData;
    }
    return null;
  }

  @RemoteServiceAccessDenied
  public int insert(Connection connection, BigDecimal partnerId, String firstName, String lastName, BigDecimal addressId, String ahvNumber, String accountNumber, BigDecimal taxType, boolean reducedLunch, Date birthday,
      BigDecimal hourlyWage,
      BigDecimal socialInsuranceRate, BigDecimal sourceTaxRate, BigDecimal vacationExtraRate,
      BigDecimal employerId) {
    Employee employee = Employee.EMPLOYEE;
    EmployeeRecord rec = employee.newRecord()
        .with(employee.EMPLOYER_NR, employerId)
        .with(employee.ACCOUNT_NUMBER, accountNumber)
        .with(employee.ADDRESS_NR, addressId)
        .with(employee.AHV_NUMBER, ahvNumber)
        .with(employee.BIRTHDAY, birthday)
        .with(employee.FIRST_NAME, firstName)
        .with(employee.TAX_TYPE, taxType)
        .with(employee.REDUCED_LUNCH, reducedLunch)
        .with(employee.HOURLY_WAGE, hourlyWage)
        .with(employee.SOCIAL_INSURANCE_RATE, socialInsuranceRate)
        .with(employee.SOURCE_TAX_RATE, sourceTaxRate)
        .with(employee.VACATION_EXTRA_RATE, vacationExtraRate)
        .with(employee.LAST_NAME, lastName)
        .with(employee.EMPLOYEE_NR, partnerId);

    return DSL.using(connection, SQLDialect.DERBY)
        .executeInsert(rec);
  }

  @RemoteServiceAccessDenied
  public EmployeeFormData getEmployeeByEmployeeTaxGroupId(BigDecimal employeeTaxGroupId) {
    EmployeeFormData formData = new EmployeeFormData();
    Employee emp = Employee.EMPLOYEE.as("EMP");
    EmployeeTaxGroup etg = EmployeeTaxGroup.EMPLOYEE_TAX_GROUP;
    fillFormData(DSL.using(SQL.getConnection(), SQLDialect.DERBY).select(emp.fields())
        .from(emp)
        .innerJoin(etg).on(emp.EMPLOYEE_NR.eq(etg.EMPLOYEE_NR))
        .where(etg.EMPLOYEE_TAX_GROUP_NR.eq(employeeTaxGroupId)).fetchOne(),
        formData);
    return formData;
  }

  @RemoteServiceAccessDenied
  public StatementBean mapToStatementBean(EmployeeFormData employee, StatementBean result) {
    return result.withAccountNumber(employee.getEmployeeBox().getAccountNumber().getValue())
        .withHourlyWage(employee.getEmploymentBox().getHourlyWage().getValue())
        .withHourlyWage(employee.getEmploymentBox().getHourlyWage().getValue())
        .withSocialInsuranceRate(employee.getEmploymentBox().getSocialInsuranceRate().getValue())
        .withSourceTaxRate(employee.getEmploymentBox().getSourceTaxRate().getValue())
        .withTaxType(employee.getEmploymentBox().getTaxType().getValue())
        .withVacationExtraRate(employee.getEmploymentBox().getVacationExtraRate().getValue());
  }

  protected EmployeeFormData fillFormData(Record rec, EmployeeFormData fd) {
    if (rec == null) {
      return null;
    }
    Employee emp = Employee.EMPLOYEE;
    fd.setPartnerId(emp.EMPLOYEE_NR.get(rec));
    fd.getEmployer().setValue(emp.EMPLOYER_NR.get(rec));
    fd.getEmployeeBox().getFirstName().setValue(emp.FIRST_NAME.get(rec));
    fd.getEmployeeBox().getLastName().setValue(emp.LAST_NAME.get(rec));
    fd.getEmployeeBox().getAddressBox().setAddressId(emp.ADDRESS_NR.get(rec));
    fd.getEmployeeBox().getAhvNumber().setValue(emp.AHV_NUMBER.get(rec));
    fd.getEmployeeBox().getAccountNumber().setValue(emp.ACCOUNT_NUMBER.get(rec));
    fd.getEmployeeBox().getBirthday().setValue(emp.BIRTHDAY.get(rec));
    fd.getEmploymentBox().getTaxType().setValue(emp.TAX_TYPE.get(rec));
    fd.getEmploymentBox().getReducedLunch().setValue(emp.REDUCED_LUNCH.get(rec));
    fd.getEmploymentBox().getHourlyWage().setValue(emp.HOURLY_WAGE.get(rec));
    fd.getEmploymentBox().getSocialInsuranceRate().setValue(emp.SOCIAL_INSURANCE_RATE.get(rec));
    fd.getEmploymentBox().getSourceTaxRate().setValue(emp.SOURCE_TAX_RATE.get(rec));
    fd.getEmploymentBox().getPensionsFund().setValue(emp.PENSIONS_FUND_MONTHLY.get(rec));
    fd.getEmploymentBox().getVacationExtraRate().setValue(emp.VACATION_EXTRA_RATE.get(rec));
    return fd;
  }

  protected EmployeeRecord mapToRecord(EmployeeRecord rec, EmployeeFormData formData) {
    if (formData == null) {
      return null;
    }
    Employee employee = Employee.EMPLOYEE;
    return rec
        .with(employee.EMPLOYEE_NR, formData.getPartnerId())
        .with(employee.EMPLOYER_NR, formData.getEmployer().getValue())
        .with(employee.ADDRESS_NR, formData.getEmployeeBox().getAddressBox().getAddressId())
        .with(employee.FIRST_NAME, formData.getEmployeeBox().getFirstName().getValue())
        .with(employee.LAST_NAME, formData.getEmployeeBox().getLastName().getValue())
        .with(employee.AHV_NUMBER, formData.getEmployeeBox().getAhvNumber().getValue())
        .with(employee.BIRTHDAY, formData.getEmployeeBox().getBirthday().getValue())
        .with(employee.ACCOUNT_NUMBER, formData.getEmployeeBox().getAccountNumber().getValue())
        .with(employee.TAX_TYPE, formData.getEmploymentBox().getTaxType().getValue())
        .with(employee.REDUCED_LUNCH, formData.getEmploymentBox().getReducedLunch().getValue())
        .with(employee.HOURLY_WAGE, formData.getEmploymentBox().getHourlyWage().getValue())
        .with(employee.SOCIAL_INSURANCE_RATE, formData.getEmploymentBox().getSocialInsuranceRate().getValue())
        .with(employee.SOURCE_TAX_RATE, formData.getEmploymentBox().getSourceTaxRate().getValue())
        .with(employee.PENSIONS_FUND_MONTHLY, formData.getEmploymentBox().getPensionsFund().getValue())
        .with(employee.VACATION_EXTRA_RATE, formData.getEmploymentBox().getVacationExtraRate().getValue());

  }

}

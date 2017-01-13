package ch.ahoegger.docbox.server.hr.employee;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.ch.ahoegger.docbox.server.or.app.tables.Employee;
import org.ch.ahoegger.docbox.server.or.app.tables.Partner;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.server.jdbc.SQL;
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

    System.out.println(statement);
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
      partnerService.create(partnerData);
      partnerId = partnerData.getPartnerId();
    }

    formData.setPartnerId(partnerId);

    Employee emp = Employee.EMPLOYEE.as("EMP");

    int rowCount = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .newRecord(emp)
        .with(emp.PARTNER_NR, formData.getPartnerId())
        .with(emp.FIRST_NAME, formData.getEmployeeBox().getFirstName().getValue())
        .with(emp.LAST_NAME, formData.getEmployeeBox().getLastName().getValue())
        .with(emp.ADDRESS_LINE1, formData.getEmployeeBox().getAddressLine1().getValue())
        .with(emp.ADDRESS_LINE2, formData.getEmployeeBox().getAddressLine2().getValue())
        .with(emp.AHV_NUMBER, formData.getEmployeeBox().getAhvNumber().getValue())
        .with(emp.ACCOUNT_NUMBER, formData.getEmployeeBox().getAccountNumber().getValue())
        .with(emp.HOURLY_WAGE, formData.getEmployeeBox().getHourlyWage().getValue())
        .with(emp.EMPLOYER_ADDRESS_LINE1, formData.getEmployerBox().getAddressLine1().getValue())
        .with(emp.EMPLOYER_ADDRESS_LINE2, formData.getEmployerBox().getAddressLine2().getValue())
        .with(emp.EMPLOYER_ADDRESS_LINE3, formData.getEmployerBox().getAddressLine3().getValue())
        .with(emp.EMPLOYER_EMAIL, formData.getEmployerBox().getEmail().getValue())
        .with(emp.EMPLOYER_PHONE, formData.getEmployerBox().getPhone().getValue())
        .insert();

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

    EmployeeFormData result = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .select(emp.PARTNER_NR, emp.FIRST_NAME, emp.LAST_NAME, emp.ADDRESS_LINE1, emp.ADDRESS_LINE2, emp.AHV_NUMBER, emp.ACCOUNT_NUMBER, emp.HOURLY_WAGE, emp.EMPLOYER_ADDRESS_LINE1,
            emp.EMPLOYER_ADDRESS_LINE2, emp.EMPLOYER_ADDRESS_LINE3, emp.EMPLOYER_EMAIL, emp.EMPLOYER_PHONE)
        .select(p.NAME, p.DESCRIPTION, p.START_DATE, p.END_DATE)
        .from(emp)
        .leftOuterJoin(p)
        .on(p.PARTNER_NR.eq(emp.PARTNER_NR))
        .where(emp.PARTNER_NR.eq(formData.getPartnerId()))
        .fetchOne()
        .map(rec -> {
          EmployeeFormData fd = new EmployeeFormData();
          fd.setPartnerId(rec.get(emp.PARTNER_NR));
          fd.getEmployeeBox().getFirstName().setValue(rec.get(emp.FIRST_NAME));
          fd.getEmployeeBox().getLastName().setValue(rec.get(emp.LAST_NAME));
          fd.getEmployeeBox().getAddressLine1().setValue(rec.get(emp.ADDRESS_LINE1));
          fd.getEmployeeBox().getAddressLine2().setValue(rec.get(emp.ADDRESS_LINE2));
          fd.getEmployeeBox().getAhvNumber().setValue(rec.get(emp.AHV_NUMBER));
          fd.getEmployeeBox().getAccountNumber().setValue(rec.get(emp.ACCOUNT_NUMBER));
          fd.getEmployeeBox().getHourlyWage().setValue(rec.get(emp.HOURLY_WAGE));
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
    BEANS.get(IPartnerService.class).store(partnerData);

    Employee emp = Employee.EMPLOYEE.as("EMP");
    int rowCount = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .update(emp)
        .set(emp.FIRST_NAME, formData.getEmployeeBox().getFirstName().getValue())
        .set(emp.LAST_NAME, formData.getEmployeeBox().getLastName().getValue())
        .set(emp.ADDRESS_LINE1, formData.getEmployeeBox().getAddressLine1().getValue())
        .set(emp.ADDRESS_LINE2, formData.getEmployeeBox().getAddressLine2().getValue())
        .set(emp.AHV_NUMBER, formData.getEmployeeBox().getAhvNumber().getValue())
        .set(emp.ACCOUNT_NUMBER, formData.getEmployeeBox().getAccountNumber().getValue())
        .set(emp.HOURLY_WAGE, formData.getEmployeeBox().getHourlyWage().getValue())
        .set(emp.EMPLOYER_ADDRESS_LINE1, formData.getEmployerBox().getAddressLine1().getValue())
        .set(emp.EMPLOYER_ADDRESS_LINE2, formData.getEmployerBox().getAddressLine2().getValue())
        .set(emp.EMPLOYER_ADDRESS_LINE3, formData.getEmployerBox().getAddressLine3().getValue())
        .set(emp.EMPLOYER_EMAIL, formData.getEmployerBox().getEmail().getValue())
        .set(emp.EMPLOYER_PHONE, formData.getEmployerBox().getPhone().getValue())
        .where(emp.PARTNER_NR.eq(formData.getPartnerId()))
        .execute();

    if (rowCount == 1) {
      // notify backup needed
      BEANS.get(IBackupService.class).notifyModification();

      return formData;
    }
    return null;
  }
}

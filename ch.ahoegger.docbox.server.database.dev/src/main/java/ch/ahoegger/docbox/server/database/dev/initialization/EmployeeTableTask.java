package ch.ahoegger.docbox.server.database.dev.initialization;

import java.math.BigDecimal;

import org.ch.ahoegger.docbox.server.or.app.tables.Employee;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.jooq.Query;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.server.or.generator.table.EmployeeTableStatement;

/**
 * <h3>{@link EmployeeTableTask}</h3>
 *
 * @author Andreas Hoegger
 */
public class EmployeeTableTask extends EmployeeTableStatement implements ITableTask {
  private static final Logger LOG = LoggerFactory.getLogger(EmployeeTableTask.class);

  @Override
  public void createTable(ISqlService sqlService) {
    LOG.info("SQL-DEV create Table: {}", TABLE_NAME);
    sqlService.insert(getCreateTable());
  }

  @Override
  public void deleteTable(ISqlService sqlService) {
    LOG.info("SQL-DEV delete table: {}", TABLE_NAME);
    Employee t = Employee.EMPLOYEE;
    Query query = DSL.using(sqlService.getConnection(), SQLDialect.DERBY).delete(t);
    query.execute();
  }

  @Override
  public void dropTable(ISqlService sqlService) {
    LOG.info("SQL-DEV drop table: {}", TABLE_NAME);
    Employee t = Employee.EMPLOYEE;
    Query query = DSL.using(sqlService.getConnection(), SQLDialect.DERBY).dropTable(t);
    query.execute();
  }

  public void insert(ISqlService sqlService, BigDecimal partnerId, String firstName, String lastName, String addressLine1, String addressLine2, String ahvNumber, String accountNumber, BigDecimal hourlyWage,
      String employerAddressLine1, String employerAddressLine2, String employerAddressLine3, String employerEmail, String employerPhone) {
    Employee t = Employee.EMPLOYEE;
    DSL.using(sqlService.getConnection(), SQLDialect.DERBY)
        .newRecord(t)
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
        .with(t.LAST_NAME, lastName)
        .with(t.PARTNER_NR, partnerId)
        .insert();

  }

}

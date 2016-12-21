package ch.ahoegger.docbox.server.database.dev.initialization;

import org.eclipse.scout.rt.platform.holders.NVPair;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.shared.hr.employee.IEmployeeTable;
import ch.ahoegger.docbox.shared.util.SqlFramentBuilder;

/**
 * <h3>{@link EmployeeTableTask}</h3>
 *
 * @author Andreas Hoegger
 */
public class EmployeeTableTask implements ITableTask, IEmployeeTable {
  private static final Logger LOG = LoggerFactory.getLogger(EmployeeTableTask.class);

  @Override
  public String getCreateStatement() {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("CREATE TABLE ").append(TABLE_NAME).append(" (");
    statementBuilder.append(PARTNER_NR).append(" DECIMAL NOT NULL, ");
    statementBuilder.append(FIRST_NAME).append(" VARCHAR(").append(FIRST_NAME_LENGTH).append(") NOT NULL, ");
    statementBuilder.append(LAST_NAME).append(" VARCHAR(").append(LAST_NAME_LENGTH).append(") NOT NULL, ");
    statementBuilder.append(ADDRESS_LINE1).append(" VARCHAR(").append(ADDRESS_LINE1_LENGTH).append("), ");
    statementBuilder.append(ADDRESS_LINE2).append(" VARCHAR(").append(ADDRESS_LINE2_LENGTH).append("), ");
    statementBuilder.append(AHV_NUMBER).append(" VARCHAR(").append(AHV_NUMBER_LENGTH).append("), ");
    statementBuilder.append(ACCOUNT_NUMBER).append(" VARCHAR(").append(ACCOUNT_NUMBER_LENGTH).append("), ");
    statementBuilder.append(HOURLY_WAGE).append(" DECIMAL(5, 2), ");

    statementBuilder.append("PRIMARY KEY (").append(PARTNER_NR).append(")");
    statementBuilder.append(")");
    return statementBuilder.toString();
  }

  @Override
  public void createTable(ISqlService sqlService) {
    LOG.info("SQL-DEV create Table: {}", TABLE_NAME);
    sqlService.insert(getCreateStatement());
  }

  @Override
  public void deleteTable(ISqlService sqlService) {
    LOG.info("SQL-DEV delete table: {}", TABLE_NAME);
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("DELETE FROM ").append(TABLE_NAME);
    sqlService.insert(statementBuilder.toString());
  }

  @Override
  public void dropTable(ISqlService sqlService) {
    LOG.info("SQL-DEV drop table: {}", TABLE_NAME);
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("DROP TABLE ").append(TABLE_NAME);
    sqlService.insert(statementBuilder.toString());
  }

  public void createEmployerRow(ISqlService sqlService, long partnerId, String firstName, String lastName, String addressLine1, String addressLine2, String ahvNumber, String accountNumber, double hourlyWage) {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("INSERT INTO ").append(TABLE_NAME).append(" (");
    statementBuilder.append(SqlFramentBuilder.columns(PARTNER_NR, FIRST_NAME, LAST_NAME, ADDRESS_LINE1, ADDRESS_LINE2, AHV_NUMBER, ACCOUNT_NUMBER, HOURLY_WAGE));
    statementBuilder.append(") VALUES (");
    statementBuilder.append(" :partnerId, :firstName, :lastName, :addressLine1, :addressLine2, :ahvNumber, :accountNumber, :hourlyWage");
    statementBuilder.append(")");
    sqlService.insert(statementBuilder.toString(),
        new NVPair("partnerId", partnerId),
        new NVPair("firstName", firstName),
        new NVPair("lastName", lastName),
        new NVPair("addressLine1", addressLine1),
        new NVPair("addressLine2", addressLine2),
        new NVPair("ahvNumber", ahvNumber),
        new NVPair("accountNumber", accountNumber),
        new NVPair("hourlyWage", hourlyWage));
  }

}
package ch.ahoegger.docbox.server.or.generator.table;

import ch.ahoegger.docbox.or.definition.table.IEmployeeTable;

/**
 * <h3>{@link EmployeeTableStatement}</h3>
 *
 * @author Andreas Hoegger
 */
public class EmployeeTableStatement implements ITableStatement, IEmployeeTable {
  @Override
  public String getCreateTable() {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("CREATE TABLE ").append(TABLE_NAME).append(" (");
    statementBuilder.append(PARTNER_NR).append(" BIGINT NOT NULL, ");
    statementBuilder.append(FIRST_NAME).append(" VARCHAR(").append(FIRST_NAME_LENGTH).append(") NOT NULL, ");
    statementBuilder.append(LAST_NAME).append(" VARCHAR(").append(LAST_NAME_LENGTH).append(") NOT NULL, ");
    statementBuilder.append(ADDRESS_LINE1).append(" VARCHAR(").append(ADDRESS_LINE_LENGTH).append("), ");
    statementBuilder.append(ADDRESS_LINE2).append(" VARCHAR(").append(ADDRESS_LINE_LENGTH).append("), ");
    statementBuilder.append(AHV_NUMBER).append(" VARCHAR(").append(AHV_NUMBER_LENGTH).append("), ");
    statementBuilder.append(ACCOUNT_NUMBER).append(" VARCHAR(").append(ACCOUNT_NUMBER_LENGTH).append("), ");
    statementBuilder.append(EMPLOYER_ADDRESS_LINE1).append(" VARCHAR(").append(ADDRESS_LINE_LENGTH).append("), ");
    statementBuilder.append(EMPLOYER_ADDRESS_LINE2).append(" VARCHAR(").append(ADDRESS_LINE_LENGTH).append("), ");
    statementBuilder.append(EMPLOYER_ADDRESS_LINE3).append(" VARCHAR(").append(ADDRESS_LINE_LENGTH).append("), ");
    statementBuilder.append(EMPLOYER_EMAIL).append(" VARCHAR(").append(EMPLOYER_EMAIL_LENGTH).append("), ");
    statementBuilder.append(EMPLOYER_PHONE).append(" VARCHAR(").append(EMPLOYER_PHONE_LENGTH).append("), ");
    statementBuilder.append(HOURLY_WAGE).append(" DECIMAL(5, 2), ");

    statementBuilder.append("PRIMARY KEY (").append(PARTNER_NR).append(")");
    statementBuilder.append(")");
    return statementBuilder.toString();
  }

}

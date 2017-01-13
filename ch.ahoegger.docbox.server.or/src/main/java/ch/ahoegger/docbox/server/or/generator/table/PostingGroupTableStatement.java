package ch.ahoegger.docbox.server.or.generator.table;

import ch.ahoegger.docbox.or.definition.table.IPostingGroupTable;

/**
 * <h3>{@link PostingGroupTableStatement}</h3>
 *
 * @author Andreas Hoegger
 */
public class PostingGroupTableStatement implements ITableStatement, IPostingGroupTable {
  @Override
  public String getCreateTable() {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("CREATE TABLE ").append(TABLE_NAME).append(" (");
    statementBuilder.append(POSTING_GROUP_NR).append(" BIGINT NOT NULL, ");
    statementBuilder.append(PARTNER_NR).append(" BIGINT NOT NULL, ");
    statementBuilder.append(TAX_GROUP_NR).append(" BIGINT, ");
    statementBuilder.append(DOCUMENT_NR).append(" BIGINT NOT NULL, ");
    statementBuilder.append(NAME).append(" VARCHAR(").append(NAME_LENGTH).append("), ");
    statementBuilder.append(STATEMENT_DATE).append(" DATE, ");
    statementBuilder.append(WORKING_HOURS).append(" DECIMAL(6, 2), ");
    statementBuilder.append(BRUTTO_WAGE).append(" DECIMAL(6, 2), ");
    statementBuilder.append(NETTO_WAGE).append(" DECIMAL(6, 2), ");
    statementBuilder.append(SOURCE_TAX).append(" DECIMAL(6, 2), ");
    statementBuilder.append(SOCIAL_SECURITY_TAX).append(" DECIMAL(6, 2), ");
    statementBuilder.append(VACATION_EXTRA).append(" DECIMAL(6, 2), ");
    statementBuilder.append("PRIMARY KEY (").append(POSTING_GROUP_NR).append(")");
    statementBuilder.append(")");
    return statementBuilder.toString();
  }

}

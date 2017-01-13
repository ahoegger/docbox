package ch.ahoegger.docbox.server.or.generator.table;

import ch.ahoegger.docbox.or.definition.table.IEntityTable;

/**
 * <h3>{@link EntityTableStatement}</h3>
 *
 * @author Andreas Hoegger
 */
public class EntityTableStatement implements ITableStatement, IEntityTable {

  @Override
  public String getCreateTable() {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("CREATE TABLE ").append(TABLE_NAME).append(" (");
    statementBuilder.append(ENTITY_NR).append(" BIGINT NOT NULL, ");
    statementBuilder.append(PARTNER_NR).append(" BIGINT NOT NULL, ");
    statementBuilder.append(POSTING_GROUP_NR).append(" BIGINT, ");
    statementBuilder.append(ENTITY_TYPE).append(" BIGINT NOT NULL, ");
    statementBuilder.append(ENTITY_DATE).append(" DATE, ");
    statementBuilder.append(WORKING_HOURS).append(" DECIMAL(4, 2), ");
    statementBuilder.append(EXPENSE_AMOUNT).append(" DECIMAL(8, 2), ");
    statementBuilder.append(DESCRIPTION).append(" VARCHAR(").append(DESCRIPTION_LENGTH).append("), ");
    statementBuilder.append("PRIMARY KEY (").append(ENTITY_NR).append(")");
    statementBuilder.append(")");
    return statementBuilder.toString();
  }
}

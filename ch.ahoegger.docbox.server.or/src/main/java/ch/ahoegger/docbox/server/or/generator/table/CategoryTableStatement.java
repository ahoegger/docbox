package ch.ahoegger.docbox.server.or.generator.table;

import ch.ahoegger.docbox.or.definition.table.ICategoryTable;

/**
 * <h3>{@link CategoryTableStatement}</h3>
 *
 * @author Andreas Hoegger
 */
public class CategoryTableStatement implements ITableStatement, ICategoryTable {

  @Override
  public String getCreateTable() {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("CREATE TABLE ").append(TABLE_NAME).append(" (");
    statementBuilder.append(CATEGORY_NR).append(" BIGINT NOT NULL, ");
    statementBuilder.append(NAME).append(" VARCHAR(").append(NAME_LENGTH).append(") NOT NULL, ");
    statementBuilder.append(DESCRIPTION).append(" VARCHAR(").append(DESCRIPTION_LENGTH).append("), ");
    statementBuilder.append(START_DATE).append(" DATE, ");
    statementBuilder.append(END_DATE).append(" DATE, ");
    statementBuilder.append("PRIMARY KEY (").append(CATEGORY_NR).append(")");
    statementBuilder.append(")");
    return statementBuilder.toString();
  }

}

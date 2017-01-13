package ch.ahoegger.docbox.server.or.generator.table;

import ch.ahoegger.docbox.or.definition.table.ITaxGroupTable;

/**
 * <h3>{@link TaxGroupTableStatement}</h3>
 *
 * @author Andreas Hoegger
 */
public class TaxGroupTableStatement implements ITableStatement, ITaxGroupTable {

  @Override
  public String getCreateTable() {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("CREATE TABLE ").append(TABLE_NAME).append(" (");
    statementBuilder.append(TAX_GROUP_NR).append(" BIGINT NOT NULL, ");
    statementBuilder.append(NAME).append(" VARCHAR(").append(NAME_LENGTH).append(") NOT NULL, ");
    statementBuilder.append(START_DATE).append(" DATE NOT NULL, ");
    statementBuilder.append(END_DATE).append(" DATE, ");
    statementBuilder.append("PRIMARY KEY (").append(TAX_GROUP_NR).append(")");
    statementBuilder.append(")");
    return statementBuilder.toString();
  }

}

package ch.ahoegger.docbox.server.or.generator.table;

import org.eclipse.scout.rt.platform.Order;

import ch.ahoegger.docbox.or.definition.table.ISequenceTable;

/**
 * <h3>{@link SequenceStatement}</h3>
 *
 * @author Andreas Hoegger
 */
@Order(-20)
public class SequenceStatement implements ITableStatement, ISequenceTable {

  @Override
  public String getCreateTable() {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("CREATE TABLE ").append(TABLE_NAME).append(" (");
    statementBuilder.append(LAST_VAL).append(" BIGINT");
    statementBuilder.append(")");
    return statementBuilder.toString();
  }

}

package ch.ahoegger.docbox.server.database.initialization;

import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.holders.NVPair;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.shared.ISequenceTable;

/**
 * <h3>{@link SequenceTask}</h3>
 *
 * @author aho
 */
@Order(-20)
public class SequenceTask implements ITableTask, ISequenceTable {
  private static final Logger LOG = LoggerFactory.getLogger(SequenceTask.class);

  @Override
  public String getCreateStatement() {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("CREATE TABLE ").append(TABLE_NAME).append(" (");
    statementBuilder.append(LAST_VAL).append(" BIGINT");
    statementBuilder.append(")");
    return statementBuilder.toString();
  }

  @Override
  public void createTable(ISqlService sqlService) {
    LOG.info("SQL-DEV create Table: {0}", TABLE_NAME);
    sqlService.insert(getCreateStatement());
  }

  @Override
  public void createRows(ISqlService sqlService) {
    LOG.info("SQL-DEV create rows for: {0}", TABLE_NAME);
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("INSERT INTO ").append(TABLE_NAME).append(" (").append(LAST_VAL).append(") VALUES (:lastVal)");
    sqlService.insert(statementBuilder.toString(),
        new NVPair("lastVal", 1000));
  }

}

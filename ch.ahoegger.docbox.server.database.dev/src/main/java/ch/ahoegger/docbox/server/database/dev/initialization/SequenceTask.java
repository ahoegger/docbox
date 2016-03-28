package ch.ahoegger.docbox.server.database.dev.initialization;

import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.holders.NVPair;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.shared.ISequenceTable;

/**
 * <h3>{@link SequenceTask}</h3>
 *
 * @author Andreas Hoegger
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

  public void insertInitialValue(ISqlService sqlService, long initialValue) {
    LOG.info("SQL-DEV create rows for: {}", TABLE_NAME);
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("INSERT INTO ").append(TABLE_NAME).append(" (").append(LAST_VAL).append(") VALUES (:lastVal)");
    sqlService.insert(statementBuilder.toString(),
        new NVPair("lastVal", initialValue));
  }

}

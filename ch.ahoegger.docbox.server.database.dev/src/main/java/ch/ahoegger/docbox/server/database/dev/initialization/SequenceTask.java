package ch.ahoegger.docbox.server.database.dev.initialization;

import org.ch.ahoegger.docbox.server.or.app.tables.PrimaryKeySeq;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.holders.NVPair;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.jooq.Query;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.server.or.generator.table.SequenceStatement;

/**
 * <h3>{@link SequenceTask}</h3>
 *
 * @author Andreas Hoegger
 */
@Order(-20)
public class SequenceTask extends SequenceStatement implements ITableTask {
  private static final Logger LOG = LoggerFactory.getLogger(SequenceTask.class);

  @Override
  public void createTable(ISqlService sqlService) {
    LOG.info("SQL-DEV create Table: {}", TABLE_NAME);
    sqlService.insert(getCreateTable());
  }

  @Override
  public void deleteTable(ISqlService sqlService) {
    LOG.info("SQL-DEV delete table: {}", TABLE_NAME);
    PrimaryKeySeq t = PrimaryKeySeq.PRIMARY_KEY_SEQ;
    Query query = DSL.using(sqlService.getConnection(), SQLDialect.DERBY).delete(t);
    query.execute();
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

package ch.ahoegger.docbox.server.database.initialization;

import org.eclipse.scout.rt.platform.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.server.database.IDocboxSqlService;

/**
 * <h3>{@link SequenceTask}</h3>
 *
 * @author aho
 */
@Order(-20)
public class SequenceTask implements ITableTask {
  private static final Logger LOG = LoggerFactory.getLogger(SequenceTask.class);

  @Override
  public String getCreateStatement() {
    return "CREATE TABLE PRIMARY_KEY_SEQ (LAST_VAL BIGINT)";
  }

  @Override
  public void createTable(IDocboxSqlService sqlService) {
    LOG.info("SQL-DEV create Table: PRIMARY_KEY_SEQ");
    sqlService.insert(getCreateStatement());
  }

  @Override
  public void createRows(IDocboxSqlService sqlService) {
    sqlService.insert("INSERT INTO PRIMARY_KEY_SEQ (LAST_VAL) VALUES (1000)");
  }

}

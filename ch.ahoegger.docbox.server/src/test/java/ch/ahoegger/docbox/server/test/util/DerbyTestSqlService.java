package ch.ahoegger.docbox.server.test.util;

import javax.annotation.PostConstruct;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.CreateImmediately;
import org.eclipse.scout.rt.platform.Replace;
import org.eclipse.scout.rt.platform.exception.ExceptionHandler;
import org.eclipse.scout.rt.platform.transaction.TransactionScope;
import org.eclipse.scout.rt.platform.util.concurrent.IRunnable;

import ch.ahoegger.docbox.server.SuperUserRunContextProducer;
import ch.ahoegger.docbox.server.database.DerbySqlService;
import ch.ahoegger.docbox.server.database.dev.initialization.ITableTask;

/**
 * <h3>{@link DerbyTestSqlService}</h3>
 *
 * @author Andreas Hoegger
 */
@CreateImmediately
@Replace
public class DerbyTestSqlService extends DerbySqlService {

  @PostConstruct
  protected void init() {
    // init in memory db
    try {
      BEANS.get(SuperUserRunContextProducer.class).produce().withTransactionScope(TransactionScope.REQUIRES_NEW).run(new IRunnable() {

        @Override
        public void run() throws Exception {
          initializeDatabase();
        }

      });
    }
    catch (RuntimeException e) {
      BEANS.get(ExceptionHandler.class).handle(e);
    }
  }

  @Override
  protected String getConfiguredJdbcMappingName() {
    // in memory
    return "jdbc:derby:memory:contacts-database;create=true";
  }

  private void initializeDatabase() {

    for (ITableTask task : BEANS.all(ITableTask.class)) {
      task.createTable(this);

    }
  }
}

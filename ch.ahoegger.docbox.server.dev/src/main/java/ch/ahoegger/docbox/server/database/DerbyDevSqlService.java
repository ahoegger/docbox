package ch.ahoegger.docbox.server.database;

import java.util.List;

import javax.annotation.PostConstruct;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.CreateImmediately;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ExceptionHandler;
import org.eclipse.scout.rt.platform.util.concurrent.IRunnable;

import ch.ahoegger.docbox.server.SuperUserRunContextProducer;
import ch.ahoegger.docbox.server.database.initialization.ITableTask;

/**
 * <h3>{@link DerbyDevSqlService}</h3>
 *
 * @author aho
 */
@Order(-20)
@CreateImmediately
public class DerbyDevSqlService extends AbstractDocboxSqlService {

  @PostConstruct
  protected void init() {
    try {
      BEANS.get(SuperUserRunContextProducer.class).produce().run(new IRunnable() {

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

  private void initializeDatabase() {
    List<ITableTask> tableTasks = BEANS.all(ITableTask.class);
    // create tables
    for (ITableTask task : tableTasks) {
      task.createTable(this);
    }
    // create test data
    for (ITableTask task : tableTasks) {
      task.createRows(this);
    }
  }

  @Override
  protected String getConfiguredJdbcMappingName() {
    return "jdbc:derby:memory:contacts-database;create=true";
  }
}

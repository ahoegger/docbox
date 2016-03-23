package ch.ahoegger.docbox.server.database;

import java.util.List;

import javax.annotation.PostConstruct;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.CreateImmediately;
import org.eclipse.scout.rt.platform.Replace;
import org.eclipse.scout.rt.platform.config.CONFIG;
import org.eclipse.scout.rt.platform.exception.ExceptionHandler;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.platform.util.concurrent.IRunnable;

import ch.ahoegger.docbox.server.SuperUserRunContextProducer;
import ch.ahoegger.docbox.server.database.initialization.ITableTask;

/**
 * <h3>{@link DerbyDevSqlService}</h3>
 *
 * @author Andreas Hoegger
 */

@CreateImmediately
@Replace
public class DerbyDevSqlService extends DerbySqlService {

  @PostConstruct
  protected void init() {
    if (StringUtility.isNullOrEmpty(CONFIG.getPropertyValue(DatabaseLocationProperty.class))) {
      // init in memory db
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
  }

  protected void initializeDatabase() {
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
    if (StringUtility.isNullOrEmpty(CONFIG.getPropertyValue(DatabaseLocationProperty.class))) {
      // in memory
      return "jdbc:derby:memory:contacts-database;create=true";
    }
    return super.getConfiguredJdbcMappingName();
  }

}

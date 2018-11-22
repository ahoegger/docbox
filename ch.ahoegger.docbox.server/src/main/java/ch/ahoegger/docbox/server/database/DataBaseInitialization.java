package ch.ahoegger.docbox.server.database;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.IPlatform;
import org.eclipse.scout.rt.platform.IPlatformListener;
import org.eclipse.scout.rt.platform.PlatformEvent;
import org.eclipse.scout.rt.platform.exception.ExceptionHandler;
import org.eclipse.scout.rt.platform.util.concurrent.IRunnable;

import ch.ahoegger.docbox.server.SuperUserRunContextProducer;
import ch.ahoegger.docbox.server.database.migration.IMigrationTask;

/**
 * <h3>{@link DataBaseInitialization}</h3>
 *
 * @author aho
 */
public class DataBaseInitialization implements IPlatformListener {

  @Override
  public final void stateChanged(PlatformEvent event) {
    if (event.getState() == IPlatform.State.PlatformStarted) {
      try {
        BEANS.get(SuperUserRunContextProducer.class).produce().run(new IRunnable() {

          @Override
          public void run() throws Exception {
            runMigration();
            initDb();
          }
        });
      }
      catch (RuntimeException e) {
        BEANS.get(ExceptionHandler.class).handle(e);
      }
    }
  }

  protected void runMigration() {
    // run migrations
    BEANS.all(IMigrationTask.class).forEach(task -> task.run());
  }

  protected void initDb() {
    // void here
  }

}

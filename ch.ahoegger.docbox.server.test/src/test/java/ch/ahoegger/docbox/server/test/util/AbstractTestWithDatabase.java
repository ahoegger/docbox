package ch.ahoegger.docbox.server.test.util;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import ch.ahoegger.docbox.server.ServerSession;
import ch.ahoegger.docbox.server.database.initialization.ITableTask;
import ch.ahoegger.docbox.server.database.initialization.SequenceTask;
import ch.ahoegger.docbox.server.test.adminstration.user.UserServiceTest;

/**
 * <h3>{@link AbstractTestWithDatabase}</h3>
 *
 * @author aho
 */
@RunWith(ServerTestRunner.class)
@RunWithSubject(UserServiceTest.SUBJECT_NAME)
@RunWithServerSession(ServerSession.class)
public abstract class AbstractTestWithDatabase {

  @BeforeClass
  public static void setupDb() {
    ISqlService sqlService = BEANS.get(ISqlService.class);
    for (ITableTask task : BEANS.all(ITableTask.class)) {
      task.dropTable(sqlService);
      task.createTable(sqlService);

    }
    // init index
    BEANS.get(SequenceTask.class).createRows(sqlService);
  }
}

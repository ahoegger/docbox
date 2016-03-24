package ch.ahoegger.docbox.server.test.util;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.Before;
import org.junit.runner.RunWith;

import ch.ahoegger.docbox.server.ServerSession;
import ch.ahoegger.docbox.server.database.initialization.ITableTask;
import ch.ahoegger.docbox.server.database.initialization.SequenceTask;

/**
 * <h3>{@link AbstractTestWithDatabase}</h3>
 *
 * @author aho
 */
@RunWith(ServerTestRunner.class)
@RunWithSubject(AbstractTestWithDatabase.SUBJECT_NAME)
@RunWithServerSession(ServerSession.class)
public abstract class AbstractTestWithDatabase {

  public static final String SUBJECT_NAME = "admin";

  @Before
  public void setupDb() throws Exception {
    // delete document store
    BEANS.get(TestDocumentStoreService.class).clearStore();
    ISqlService sqlService = BEANS.get(ISqlService.class);
    for (ITableTask task : BEANS.all(ITableTask.class)) {
      task.dropTable(sqlService);
      task.createTable(sqlService);

    }
    // init index
    BEANS.get(SequenceTask.class).createRows(sqlService);
  }
}

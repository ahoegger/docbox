package ch.ahoegger.docbox.server.test.util;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.transaction.TransactionScope;
import org.eclipse.scout.rt.platform.util.concurrent.IRunnable;
import org.eclipse.scout.rt.server.context.ServerRunContexts;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.Before;
import org.junit.runner.RunWith;

import ch.ahoegger.docbox.server.ServerSession;
import ch.ahoegger.docbox.server.database.dev.initialization.ITableTask;
import ch.ahoegger.docbox.server.database.dev.initialization.SequenceTask;
import ch.ahoegger.docbox.server.security.SecurityService;

/**
 * <h3>{@link AbstractTestWithDatabase}</h3>
 *
 * @author Andreas Hoegger
 */
@RunWith(ServerTestRunner.class)
@RunWithSubject(AbstractTestWithDatabase.SUBJECT_NAME)
@RunWithServerSession(ServerSession.class)
public abstract class AbstractTestWithDatabase {

  public static final String SUBJECT_NAME = "admin";

  @Before
  public void beforeTransactional() {
    ServerRunContexts.copyCurrent().withTransactionScope(TransactionScope.REQUIRES_NEW).run(new IRunnable() {
      @Override
      public void run() throws Exception {
        setupDb();
      }
    });

  }

  public void setupDb() throws Exception {
    // delete document store
    BEANS.get(TestDocumentStoreService.class).clearStore();
    ISqlService sqlService = BEANS.get(ISqlService.class);
    for (ITableTask task : BEANS.all(ITableTask.class)) {
      task.deleteTable(sqlService);

    }
    // init index
    BEANS.get(SequenceTask.class).insertInitialValue(sqlService, 1000);

  }

  protected String passwordHash(String password) {
    return new String(BEANS.get(SecurityService.class).createPasswordHash(password.toCharArray()));
  }
}

package ch.ahoegger.docbox.server.test.util;

import java.math.BigDecimal;

import org.ch.ahoegger.docbox.server.or.app.tables.PrimaryKeySeq;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.transaction.TransactionScope;
import org.eclipse.scout.rt.platform.util.concurrent.IRunnable;
import org.eclipse.scout.rt.server.context.ServerRunContexts;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.eclipse.scout.rt.server.jdbc.SQL;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.Before;
import org.junit.runner.RunWith;

import ch.ahoegger.docbox.server.ServerSession;
import ch.ahoegger.docbox.server.or.generator.table.ITableStatement;
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
    for (ITableStatement task : BEANS.all(ITableStatement.class)) {
      task.deleteTable(sqlService.getConnection());

    }
    // init index
    DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .insertInto(PrimaryKeySeq.PRIMARY_KEY_SEQ)
        .set(PrimaryKeySeq.PRIMARY_KEY_SEQ.LAST_VAL, BigDecimal.valueOf(1000))
        .execute();

  }

  protected String passwordHash(String password) {
    return new String(BEANS.get(SecurityService.class).createPasswordHash(password.toCharArray()));
  }
}

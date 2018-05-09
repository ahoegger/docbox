package ch.ahoegger.docbox.server.test.util;

import java.math.BigDecimal;
import java.sql.Connection;

import org.ch.ahoegger.docbox.server.or.app.tables.PrimaryKeySeq;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.transaction.TransactionScope;
import org.eclipse.scout.rt.server.context.ServerRunContexts;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.Before;
import org.junit.runner.RunWith;

import ch.ahoegger.docbox.server.ServerSession;
import ch.ahoegger.docbox.server.administration.user.UserService;
import ch.ahoegger.docbox.server.or.generator.table.ITableStatement;
import ch.ahoegger.docbox.server.security.SecurityService;

/**
 * <h3>{@link AbstractTestWithDatabase}</h3>
 *
 * @author Andreas Hoegger
 */
@RunWith(ServerTestRunner.class)
@RunWithSubject("admin")
@RunWithServerSession(ServerSession.class)
public abstract class AbstractTestWithDatabase {

  public static final String SUBJECT_NAME = "admin";

  public static final String ADMIN = "admin";
  public static final String USER = "user";
  public static final String USER_INACTIVE = "user_inactive";

  @Before
  public void beforeTransactional() {
    ServerRunContexts.copyCurrent().withTransactionScope(TransactionScope.REQUIRES_NEW).run(() -> setupDb());
  }

  private final void setupDb() throws Exception {
    // delete document store
    BEANS.get(TestDocumentStoreService.class).clearStore();
    ISqlService sqlService = BEANS.get(ISqlService.class);
    Connection connection = sqlService.getConnection();
    for (ITableStatement task : BEANS.all(ITableStatement.class)) {
      task.deleteTable(connection);

    }
    // init index
    DSL.using(connection, SQLDialect.DERBY)
        .insertInto(PrimaryKeySeq.PRIMARY_KEY_SEQ)
        .set(PrimaryKeySeq.PRIMARY_KEY_SEQ.LAST_VAL, BigDecimal.valueOf(1000))
        .execute();

    // setup user table
    UserService userService = BEANS.get(UserService.class);
    userService.insert(connection, "admin", "manager", ADMIN, "secret", true, true);
    userService.insert(connection, "name01", "firstname01", USER, "secret", true, false);
    userService.insert(connection, "name02", "firstname02", USER_INACTIVE, "secret", false, false);

    execSetupDb(connection);

  }

  /**
   * override to add test relevant db entities.
   *
   * @param connection
   * @throws Exception
   */
  protected void execSetupDb(Connection connection) throws Exception {

  }

  protected String passwordHash(String password) {
    return new String(BEANS.get(SecurityService.class).createPasswordHash(password.toCharArray()));
  }
}

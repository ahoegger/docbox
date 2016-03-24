package ch.ahoegger.docbox.server.test.util;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.ahoegger.docbox.server.ServerSession;
import ch.ahoegger.docbox.server.database.initialization.ITableTask;

/**
 * <h3>{@link DumpDbStatementsTest}</h3>
 *
 * @author aho
 */
@RunWith(ServerTestRunner.class)
@RunWithSubject("admin")
@RunWithServerSession(ServerSession.class)
@Ignore
public class DumpDbStatementsTest {

  @Test
  public void dumpCreateStatements() {
    for (ITableTask task : BEANS.all(ITableTask.class)) {
      System.out.println(task.getCreateStatement() + ";");
      System.out.println();
    }
  }
}

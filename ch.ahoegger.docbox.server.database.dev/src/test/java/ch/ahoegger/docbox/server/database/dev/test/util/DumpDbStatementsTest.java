package ch.ahoegger.docbox.server.database.dev.test.util;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.testing.platform.runner.PlatformTestRunner;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.ahoegger.docbox.server.database.dev.initialization.ITableTask;

/**
 * <h3>{@link DumpDbStatementsTest}</h3>
 *
 * @author Andreas Hoegger
 */
@RunWith(PlatformTestRunner.class)
@RunWithSubject("admin")
//@Ignore
public class DumpDbStatementsTest {

  @Test
  public void dumpCreateStatements() {
    for (ITableTask task : BEANS.all(ITableTask.class)) {
      System.out.println(task.getCreateStatement() + ";");
      System.out.println();
    }
  }
}

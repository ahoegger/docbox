package ch.ahoegger.docbox.server.or.table;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.testing.platform.runner.PlatformTestRunner;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.ahoegger.docbox.server.or.generator.table.ITableStatement;

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
    for (ITableStatement task : BEANS.all(ITableStatement.class)) {
      System.out.println(task.getCreateTable() + ";");
      System.out.println();
    }
  }
}

package ch.ahoegger.docbox.server.database.migration;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.IgnoreBean;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.junit.Assert;
import org.junit.Test;

import ch.ahoegger.docbox.server.database.DataBaseInitialization;
import ch.ahoegger.docbox.server.or.generator.Version;
import ch.ahoegger.docbox.server.test.util.AbstractTestWithDatabase;
import ch.ahoegger.docbox.server.test.util.MethodInvocationConsumer;

/**
 * <h3>{@link DatabaseInitializationTest}</h3>
 *
 * @author aho
 */
public class DatabaseInitializationTest extends AbstractTestWithDatabase {

  @Test
  public void testMigrationWithSameVersion() {
    // current version
    BEANS.get(MigrationService.class).insert(new Version(1, 5, 0));

    List<Version> callLog = new ArrayList<>();
    MethodInvocationConsumer<Void> validateTables = new MethodInvocationConsumer<>();
    Consumer<Version> vConsumer = callLog::add;
    new TestDataBaseInitialization(CollectionUtility.arrayList(
        new DBMigTask(new Version(1, 5, 0), vConsumer)),
        validateTables)
            .runMigration();
    Assert.assertEquals(CollectionUtility.arrayList(), callLog);
    Assert.assertEquals(new Version(1, 5, 0), BEANS.get(MigrationService.class).getLastMigrationVersion());
    Assert.assertEquals(0, validateTables.getInvocationCount());
  }

  @Test
  public void testMigrationWithSomeTasks() {
    // current version
    BEANS.get(MigrationService.class).insert(new Version(1, 5, 0));

    List<Version> callLog = new ArrayList<>();
    MethodInvocationConsumer<Void> validateTables = new MethodInvocationConsumer<>();
    Consumer<Version> vConsumer = callLog::add;
    new TestDataBaseInitialization(CollectionUtility.arrayList(
        new DBMigTask(new Version(1, 0, 0), vConsumer),
        new DBMigTask(new Version(1, 5, 0), vConsumer),
        new DBMigTask(new Version(2, 0, 0), vConsumer),
        new DBMigTask(new Version(1, 2, 0), vConsumer),
        new DBMigTask(new Version(1, 8, 0), vConsumer)),
        validateTables)
            .runMigration();
    Assert.assertEquals(CollectionUtility.arrayList(new Version(1, 8, 0), new Version(2, 0, 0)), callLog);
    Assert.assertEquals(new Version(2, 0, 0), BEANS.get(MigrationService.class).getLastMigrationVersion());
    Assert.assertEquals(1, validateTables.getInvocationCount());
  }

  @IgnoreBean
  private class TestDataBaseInitialization extends DataBaseInitialization {

    private List<IMigrationTask> m_migTasks;
    private MethodInvocationConsumer<Void> m_validateTable;

    public TestDataBaseInitialization(List<IMigrationTask> migTasks, MethodInvocationConsumer<Void> validateTable) {
      m_migTasks = migTasks;
      m_validateTable = validateTable;
    }

    @Override
    protected List<IMigrationTask> getAllMigrationTasks() {
      return m_migTasks;
    }

    @Override
    protected void runMigration() {
      super.runMigration();
    }

    @Override
    protected void validateTables() {
      m_validateTable.increment();
    }
  }

  @IgnoreBean
  private class DBMigTask implements IMigrationTask {
    private final Version m_version;
    private Consumer<Version> m_consumer;

    public DBMigTask(Version version, Consumer<Version> consumer) {
      m_version = version;
      m_consumer = consumer;
    }

    @Override
    public Version getVersion() {
      return m_version;
    }

    @Override
    public void run() {
      m_consumer.accept(getVersion());
    }
  }
}

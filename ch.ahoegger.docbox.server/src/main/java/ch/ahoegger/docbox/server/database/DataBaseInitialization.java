package ch.ahoegger.docbox.server.database;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.ch.ahoegger.docbox.server.or.sys.tables.Systables;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.IPlatform;
import org.eclipse.scout.rt.platform.IPlatformListener;
import org.eclipse.scout.rt.platform.PlatformEvent;
import org.eclipse.scout.rt.platform.exception.ExceptionHandler;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.ProcessingStatus;
import org.eclipse.scout.rt.platform.status.IStatus;
import org.eclipse.scout.rt.platform.status.MultiStatus;
import org.eclipse.scout.rt.platform.status.Status;
import org.eclipse.scout.rt.platform.util.concurrent.IRunnable;
import org.eclipse.scout.rt.server.jdbc.SQL;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.server.SuperUserRunContextProducer;
import ch.ahoegger.docbox.server.database.migration.IMigrationTask;
import ch.ahoegger.docbox.server.database.migration.MigrationService;
import ch.ahoegger.docbox.server.database.migration.MigrationUtility;
import ch.ahoegger.docbox.server.or.generator.IJooqTable;

/**
 * <h3>{@link DataBaseInitialization}</h3>
 *
 * @author aho
 */
public class DataBaseInitialization implements IPlatformListener {
  private static final Logger LOG = LoggerFactory.getLogger(DataBaseInitialization.class);

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

    final MigrationService migService = BEANS.get(MigrationService.class);
    // run migrations
    if (BEANS.all(IMigrationTask.class).stream()
        .filter(task -> migService.isUpdateRequired(task.getVersion()))
        .map(task -> {
          task.run();
          return true;
        }).reduce(false, (acc, val) -> acc || val).booleanValue()) {
      validateTables();
    }
  }

  /**
   *
   */
  private void validateTables() {

    Set<Table<?>> logicalTables = BEANS.all(IJooqTable.class)
        .stream().map(t -> t.getJooqTable())
        .collect(Collectors.toSet());

    Set<String> logicalTableNames = logicalTables
        .stream()
        .map(t -> t.getName())
        .collect(Collectors.toSet());

    // physical
    Systables systables = Systables.SYSTABLES;
    Set<String> physicalTableNames = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .select(systables.TABLENAME)
        .from(systables)
        .where(systables.TABLETYPE.eq("T"))
        .fetch()
        .stream()
        .map(rec -> rec.get(systables.TABLENAME))
        .collect(Collectors.toSet());

    MultiStatus status = new MultiStatus();

    Set<String> missingLogicalTableNames = physicalTableNames.stream()
        .filter(n -> !logicalTableNames.contains(n))
        .collect(Collectors.toSet());

    Set<String> missingPhysicalTableNames = logicalTableNames.stream()
        .filter(n -> !physicalTableNames.contains(n))
        .collect(Collectors.toSet());

    if (missingLogicalTableNames.size() > 0 || missingPhysicalTableNames.size() > 0) {
      StringBuilder message = new StringBuilder();
      if (missingLogicalTableNames.size() > 0) {
        message.append("missing logical tables = [")
            .append(missingLogicalTableNames.stream().collect(Collectors.joining(", ")))
            .append("] ");
      }
      if (missingPhysicalTableNames.size() > 0) {
        message.append("missing physical tables = [")
            .append(missingPhysicalTableNames.stream().collect(Collectors.joining(", ")))
            .append("] ");
      }
      status.add(new Status(message.toString(), IStatus.ERROR));
    }

    status.addAll(logicalTables.stream()
        .filter(t -> physicalTableNames.contains(t.getName()))
        .map(t -> validateTable(t))
        .filter(s -> s.getSeverity() > IStatus.OK)
        .collect(Collectors.toList()));

    if (status.getSeverity() >= IStatus.WARNING) {
      throw new ProcessingException(new ProcessingStatus(status));
    }
  }

  /**
   * @param string
   * @param string2
   * @return
   */
  private IStatus validateTable(Table<?> table) {
    LOG.info("Validate table ''");

    Set<String> logicalColumns = Arrays.asList(table.fields())
        .stream()
        .map(field -> field.getName())
        .sorted()
        .collect(Collectors.toSet());

    Set<String> physicalColumns = MigrationUtility.getTableDesription(table.getName()).getColumns();

    Set<String> missingPhysicalColumns = logicalColumns.stream()
        .filter(col -> !physicalColumns.contains(col))
        .collect(Collectors.toSet());

    Set<String> missingLogicalColumns = physicalColumns.stream()
        .filter(col -> !logicalColumns.contains(col))
        .collect(Collectors.toSet());

    if (missingLogicalColumns.size() == 0 && missingPhysicalColumns.size() == 0) {
      return Status.OK_STATUS;
    }
    StringBuilder message = new StringBuilder("Validate table '").append(table.getName()).append("' ");
    if (missingLogicalColumns.size() > 0) {
      message.append("missing logical=[")
          .append(missingLogicalColumns.stream().collect(Collectors.joining(", ")))
          .append("] ");
    }
    if (missingPhysicalColumns.size() > 0) {
      message.append("missing physical=[")
          .append(missingPhysicalColumns.stream().collect(Collectors.joining(", ")))
          .append("] ");
    }
    return new Status(message.toString(), IStatus.ERROR);
  }

  protected void initDb() {
    // void here
  }

}

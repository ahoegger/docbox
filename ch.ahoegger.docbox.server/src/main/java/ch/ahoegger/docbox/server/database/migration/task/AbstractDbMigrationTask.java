package ch.ahoegger.docbox.server.database.migration.task;

import java.math.BigDecimal;

import org.eclipse.scout.rt.platform.BEANS;

import ch.ahoegger.docbox.or.definition.table.IMigrationTable;
import ch.ahoegger.docbox.server.database.migration.IMigrationTask;
import ch.ahoegger.docbox.server.database.migration.MigrationService;
import ch.ahoegger.docbox.server.database.migration.MigrationUtility;

/**
 * <h3>{@link AbstractDbMigrationTask}</h3>
 *
 * @author aho
 */
public abstract class AbstractDbMigrationTask implements IMigrationTask {

  abstract BigDecimal getVersion();

  @Override
  public final void run() {
    if (isRequired()) {
      runMigration();
    }
    // check version
  }

  abstract void runMigration();

  protected boolean isRequired() {
    if (MigrationUtility.getTableDesription(IMigrationTable.TABLE_NAME) == null) {
      return true;
    }
    return BEANS.get(MigrationService.class).isUpdateRequired(getVersion());
  }

}

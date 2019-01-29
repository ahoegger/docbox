package ch.ahoegger.docbox.server.database.migration.task;

import org.eclipse.scout.rt.server.jdbc.SQL;

import ch.ahoegger.docbox.or.definition.table.IEmployeeTable;
import ch.ahoegger.docbox.or.definition.table.IStatementTable;
import ch.ahoegger.docbox.server.database.migration.IMigrationTask;
import ch.ahoegger.docbox.server.database.migration.ITableDescription;
import ch.ahoegger.docbox.server.database.migration.MigrationUtility;
import ch.ahoegger.docbox.server.or.generator.Version;

/**
 * <h3>{@link DbMigrationTask_1_2_1}</h3><br>
 * <note>Ignored since it is already executed - safety first!</note>
 *
 * @author aho
 */
public class DbMigrationTask_1_2_1 implements IMigrationTask {

  public static final Version TARGET_VERSION = new Version(1, 2, 1);

  @Override
  public Version getVersion() {
    return TARGET_VERSION;
  }

  @Override
  public void run() {
    updateEmployee();
    updateStatement();
  }

  /**
  *
  */
  private void updateEmployee() {
    ITableDescription tableDesc = MigrationUtility.getTableDesription(IEmployeeTable.TABLE_NAME);
    if (!tableDesc.hasColumn(IEmployeeTable.PENSIONS_FUND_MONTHLY)) {
      SQL.update("ALTER TABLE " + IEmployeeTable.TABLE_NAME + " ADD " + IEmployeeTable.PENSIONS_FUND_MONTHLY + " DECIMAL(9, 2)");
    }

  }

  private void updateStatement() {
    ITableDescription tableDesc = MigrationUtility.getTableDesription(IStatementTable.TABLE_NAME);
    if (!tableDesc.hasColumn(IStatementTable.PENSIONS_FUND)) {
      SQL.update("ALTER TABLE " + IStatementTable.TABLE_NAME + " ADD " + IStatementTable.PENSIONS_FUND + " DECIMAL(9, 2)");
    }
    if (!tableDesc.hasColumn(IStatementTable.MANUAL_CORRECTION_REASON)) {
      SQL.update("ALTER TABLE " + IStatementTable.TABLE_NAME + " ADD " + IStatementTable.MANUAL_CORRECTION_REASON + " VARCHAR(" + IStatementTable.MANUAL_CORRECTION_REASON_LENGTH + ")");
    }
    if (!tableDesc.hasColumn(IStatementTable.MANUAL_CORRECTION_AMOUNT)) {
      SQL.update("ALTER TABLE " + IStatementTable.TABLE_NAME + " ADD " + IStatementTable.MANUAL_CORRECTION_AMOUNT + " DECIMAL(9, 2)");
    }
  }
}

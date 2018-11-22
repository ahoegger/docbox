package ch.ahoegger.docbox.server.database.migration;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.Date;

import org.ch.ahoegger.docbox.server.or.app.tables.Migration;
import org.ch.ahoegger.docbox.server.or.app.tables.records.MigrationRecord;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.server.jdbc.SQL;
import org.jooq.Field;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import ch.ahoegger.docbox.or.definition.table.ISequenceTable;
import ch.ahoegger.docbox.shared.backup.IBackupService;
import ch.ahoegger.docbox.shared.util.LocalDateUtility;

/**
 * <h3>{@link MigrationService}</h3>
 *
 * @author aho
 */
public class MigrationService implements IService {

  public void insert(BigDecimal version) {
    insert(SQL.getConnection(), version);
  }

  public void insert(Connection connection, BigDecimal version) {
    BigDecimal migrationNr = new BigDecimal(SQL.getSequenceNextval(ISequenceTable.TABLE_NAME));

    Migration table = Migration.MIGRATION.as("MIG");
    int rowCount = mapToRecord(DSL.using(connection, SQLDialect.DERBY)
        .newRecord(table), migrationNr, version, LocalDateUtility.toDate(LocalDate.now())).insert();
    if (rowCount == 1) {
      // notify backup needed
      BEANS.get(IBackupService.class).notifyModification();
    }
  }

  protected MigrationRecord mapToRecord(MigrationRecord rec, BigDecimal migrationId, BigDecimal version, Date migrationDate) {
    Migration table = Migration.MIGRATION;
    return rec
        .with(table.MIGRATION_NR, migrationId)
        .with(table.VERSION, version)
        .with(table.EXECUTED_DATE, migrationDate);
  }

  /**
   * @param version
   * @return
   */
  public boolean isUpdateRequired(BigDecimal version) {
    Migration table = Migration.MIGRATION.as("MIG");
    Field<BigDecimal> maxVersionField = DSL.max(table.VERSION).as("MAX_VERSION");
    BigDecimal maxVersion = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .select(maxVersionField)
        .from(table)
        .fetch()
        .stream()
        .findFirst()
        .map(rec -> rec.get(maxVersionField))
        .orElse(BigDecimal.valueOf(-1));
    return version.compareTo(maxVersion) > 0;
  }
}

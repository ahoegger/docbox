package ch.ahoegger.docbox.or.definition.table;

/**
 * <h3>{@link IMigrationTable}</h3>
 *
 * @author Andreas Hoegger
 */
public interface IMigrationTable {
  public static String TABLE_NAME = "MIGRATION";

  // columns
  public static String MIGRATION_NR = "MIGRATION_NR";

  public static String DOCBOX_VERSION = "DOCBOX_VERSION";

  public static String EXECUTED_DATE = "EXECUTED_DATE";

}

package ch.ahoegger.docbox.or.definition.table;

/**
 * <h3>{@link IAddressTable}</h3>
 *
 * @author Andreas Hoegger
 */
public interface IAddressTable {
  public static String TABLE_NAME = "ADDRESS";

  // columns
  public static String ADDRESS_NR = "ADDRESS_NR";

  public static String LINE_1 = "LINE_1";
  public static String LINE_2 = "LINE_2";
  public static int ADDRESS_LINE_LENGTH = 1200;

  public static String PLZ = "PLZ";
  public static int PLZ_LENGTH = 16;
  public static String CITY = "CITY";
  public static int CITY_LENGTH = 240;

}

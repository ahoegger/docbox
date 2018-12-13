package ch.ahoegger.docbox.or.definition.table;

/**
 * <h3>{@link ITaxGroupTable}</h3>
 *
 * @author Andreas Hoegger
 */
public interface ITaxGroupTable {

  public static String TABLE_NAME = "TAX_GROUP";

  // columns
  public static String TAX_GROUP_NR = "TAX_GROUP_NR"; // PK

  public static String NAME = "NAME";
  public static int NAME_LENGTH = 240;

  String START_DATE = "START_DATE";

  String END_DATE = "END_DATE";

}

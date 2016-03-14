package ch.ahoegger.docbox.shared.category;

/**
 * <h3>{@link ICategoryTable}</h3>
 *
 * @author aho
 */
public interface ICategoryTable {
  public static String TABLE_ALIAS = "CAT";
  public static String TABLE_NAME = "CATEGORY";

  // columns
  public static String CATEGORY_NR = "DOCUMENT_NR";
  public static String NAME = "NAME";
  public static int NAME_LENGTH = 1200;
  public static String DESCRIPTION = "DESCRIPTION";
  public static int DESCRIPTION_LENGTH = 2400;
  public static String START_DATE = "START_DATE";
  public static String END_DATE = "END_DATE";
}

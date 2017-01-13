package ch.ahoegger.docbox.or.definition.table;

/**
 * <h3>{@link ICategoryTable}</h3>
 *
 * @author aho
 */
public interface ICategoryTable {

  static String TABLE_NAME = "CATEGORY";

  // columns
  static String CATEGORY_NR = "CATEGORY_NR";
  static String NAME = "NAME";
  static int NAME_LENGTH = 1200;
  static String DESCRIPTION = "DESCRIPTION";
  static int DESCRIPTION_LENGTH = 2400;
  static String START_DATE = "START_DATE";
  static String END_DATE = "END_DATE";

}

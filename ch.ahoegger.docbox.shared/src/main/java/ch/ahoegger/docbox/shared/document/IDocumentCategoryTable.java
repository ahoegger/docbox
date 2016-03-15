package ch.ahoegger.docbox.shared.document;

import ch.ahoegger.docbox.shared.category.ICategoryTable;

/**
 * <h3>{@link IDocumentCategoryTable}</h3>
 *
 * @author aho
 */
public interface IDocumentCategoryTable {
  public static String TABLE_NAME = "DOCUMENT_CATEGORY";
  public static String TABLE_ALIAS = "DCAT";

  public static String DOCUMENT_NR = IDocumentTable.DOCUMENT_NR;
  public static String CATEGORY_NR = ICategoryTable.CATEGORY_NR;
}

package ch.ahoegger.docbox.or.definition.table;

/**
 * <h3>{@link IDocumentOcrTable}</h3>
 *
 * @author Andreas Hoegger
 */
public interface IDocumentOcrTable {

  public static String TABLE_NAME = "DOCUMENT_OCR";

  public static String DOCUMENT_NR = IDocumentTable.DOCUMENT_NR;
  public static String TEXT = "TEXT";
  public static int TEXT_LENGHT = Integer.MAX_VALUE;
  public static String OCR_SCANNED = "OCR_SCANNED";
  public static String PARSE_FAILED = "PARSE_FAILED";

}

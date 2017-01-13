package ch.ahoegger.docbox.or.definition.table;

/**
 * <h3>{@link IDocumentTable}</h3>
 *
 * @author Andreas Hoegger
 */
public interface IDocumentTable {

  static String TABLE_NAME = "DOCUMENT";

  // columns
  static String DOCUMENT_NR = "DOCUMENT_NR";
  static String ABSTRACT = "ABSTRACT";
  static int ABSTRACT_LENGTH = 4800;
  static String DOCUMENT_DATE = "DOCUMENT_DATE";
  static String INSERT_DATE = "INSERT_DATE";
  static String VALID_DATE = "VALID_DATE";
  static String DOCUMENT_URL = "DOCUMENT_URL";
  static int DOCUMENT_URL_LENGTH = 1200;
  static String ORIGINAL_STORAGE = "ORIGINAL_STORAGE";
  static int ORIGINAL_STORAGE_LENGTH = 1200;
  static String CONVERSATION_NR = IConversationTable.CONVERSATION_NR;
  static String PARSE_OCR = "PARSE_OCR";
}

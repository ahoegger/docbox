package ch.ahoegger.docbox.shared.document;

import ch.ahoegger.docbox.shared.conversation.IConversationTable;

/**
 * <h3>{@link IDocumentTable}</h3>
 *
 * @author Andreas Hoegger
 */
public interface IDocumentTable {

  public static String TABLE_ALIAS = "DOC";
  public static String TABLE_NAME = "DOCUMENT";

  // columns
  public static String DOCUMENT_NR = "DOCUMENT_NR";
  public static String ABSTRACT = "ABSTRACT";
  public static int ABSTRACT_LENGTH = 4800;
  public static String DOCUMENT_DATE = "DOCUMENT_DATE";
  public static String INSERT_DATE = "INSERT_DATE";
  public static String VALID_DATE = "VALID_DATE";
  public static String DOCUMENT_URL = "DOCUMENT_URL";
  public static int DOCUMENT_URL_LENGTH = 1200;
  public static String ORIGINAL_STORAGE = "ORIGINAL_STORAGE";
  public static int ORIGINAL_STORAGE_LENGTH = 1200;
  public static String CONVERSATION_NR = IConversationTable.CONVERSATION_NR;
  public static String PARSE_OCR = "PARSE_OCR";
}

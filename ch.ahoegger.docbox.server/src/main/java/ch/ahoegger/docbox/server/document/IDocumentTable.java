package ch.ahoegger.docbox.server.document;

/**
 * <h3>{@link IDocumentTable}</h3>
 *
 * @author aho
 */
public interface IDocumentTable {

  public static String TABLE_NAME = "DOCUMENT";

  // columns
  public static String DOCUMENT_NR = "DOCUMENT_NR";
  public static String ABSTRACT = "ABSTRACT";
  public static String PARTNER_NR = "PARTNER_NR";
  public static String DOCUMENT_DATE = "DOCUMENT_DATE";
  public static String INSERT_DATE = "INSERT_DATE";
  public static String VALID_DATE = "VALID_DATE";
  public static String DOCUMENT_URL = "DOCUMENT_URL";
  public static String ORIGINAL_STORAGE = "ORIGINAL_STORAGE";
}

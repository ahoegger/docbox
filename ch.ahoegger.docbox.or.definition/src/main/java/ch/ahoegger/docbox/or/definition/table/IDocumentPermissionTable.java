package ch.ahoegger.docbox.or.definition.table;

/**
 * <h3>{@link IDocumentPermissionTable}</h3>
 *
 * @author Andreas Hoegger
 */
public interface IDocumentPermissionTable {

  public static final String TABLE_NAME = "DOCUMENT_PERMISSION";

  public static final String USERNAME = IUserTable.USERNAME;
  public static int USERNAME_LENGTH = IUserTable.USERNAME_LENGTH;
  public static final String DOCUMENT_NR = IDocumentTable.DOCUMENT_NR;
  public static final String PERMISSION = "PERMISSION";

}

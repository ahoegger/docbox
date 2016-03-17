package ch.ahoegger.docbox.shared.document;

import ch.ahoegger.docbox.shared.administration.user.IUserTable;

/**
 * <h3>{@link IDocumentPermissionTable}</h3>
 *
 * @author Andreas Hoegger
 */
public interface IDocumentPermissionTable {

  public static final String TABLE_ALIAS = "D_P";
  public static final String TABLE_NAME = "DOCUMENT_PERMISSION";

  public static final String USERNAME = IUserTable.USERNAME;
  public static final String DOCUMENT_NR = IDocumentTable.DOCUMENT_NR;
  public static final String PERMISSION = "PERMISSION";

  public static final int PERMISSION_READ = 1;
  public static final int PERMISSION_WRITE = 2;
  public static final int PERMISSION_OWNER = 3;

}

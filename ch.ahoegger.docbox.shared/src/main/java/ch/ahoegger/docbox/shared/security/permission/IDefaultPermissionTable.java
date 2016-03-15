package ch.ahoegger.docbox.shared.security.permission;

import ch.ahoegger.docbox.shared.administration.user.IUserTable;
import ch.ahoegger.docbox.shared.document.IDocumentPermissionTable;

/**
 * <h3>{@link IDefaultPermissionTable}</h3>
 *
 * @author aho
 */
public interface IDefaultPermissionTable {
  public static String TABLE_NAME = "DEFAULT_PERMISSION_TABLE";
  public static String TABLE_ALIAS = "DPS";

  public static String USERNAME = IUserTable.USERNAME;
  public static String PERMISSION = IDocumentPermissionTable.PERMISSION;
}

package ch.ahoegger.docbox.shared.security.permission;

import ch.ahoegger.docbox.shared.administration.user.IUserTable;

/**
 * <h3>{@link IPermissionTable}</h3>
 *
 * @author aho
 */
public interface IPermissionTable {

  public static final String TABLE_ALIAS = "PER";
  public static final String TABLE_NAME = "PERMISSION";

  public static final String USERNAME = IUserTable.USERNAME;
  public static final String ENTITY_NR = "ENTITY_NR";
  public static final String PERMISSION = "PERMISSION";

  public static final int PERMISSION_READ = 1;
  public static final int PERMISSION_WRITE = 2;

}

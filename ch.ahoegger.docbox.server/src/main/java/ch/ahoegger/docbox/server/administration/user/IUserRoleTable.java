package ch.ahoegger.docbox.server.administration.user;

import ch.ahoegger.docbox.server.security.role.IRoleTable;
import ch.ahoegger.docbox.shared.administration.user.IUserTable;

/**
 * <h3>{@link IUserRoleTable}</h3>
 *
 * @author Andreas Hoegger
 */
public interface IUserRoleTable {
  public static final String TABLE_NAME = "USER_ROLE";

  public static final String ROLE_NR = IRoleTable.ROLE_NR;
  public static final String USERNAME = IUserTable.USERNAME;
}

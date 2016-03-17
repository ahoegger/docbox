package ch.ahoegger.docbox.shared.permission;

import java.security.BasicPermission;
import java.security.Permission;

/**
 * <h3>{@link UsernamePermission}</h3>
 *
 * @author Andreas Hoegger
 */
public class UsernamePermission extends BasicPermission {
  private static final long serialVersionUID = 1L;
  private String m_username;

  /**
   * @param name
   */
  public UsernamePermission(String username) {
    super("User Permission username: " + username);
    m_username = username;
  }

  public String getUsername() {
    return m_username;
  }

  @Override
  public boolean implies(Permission p) {
    return super.implies(p);
  }

  @Override
  public boolean equals(Object obj) {
    return super.equals(obj);
  }

}

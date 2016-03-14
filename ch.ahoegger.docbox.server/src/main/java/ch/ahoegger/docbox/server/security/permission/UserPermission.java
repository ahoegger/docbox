package ch.ahoegger.docbox.server.security.permission;

/**
 * <h3>{@link UserPermission}</h3>
 *
 * @author aho
 */
public class UserPermission {

  private final String m_username;
  private final int m_permission;

  public UserPermission(String username, int permission) {
    m_username = username;
    m_permission = permission;

  }

  public String getUsername() {
    return m_username;
  }

  public int getPermission() {
    return m_permission;
  }
}

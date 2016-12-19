package ch.ahoegger.docbox.shared.security.permission;

import java.security.BasicPermission;

/**
 * <h3>{@link AdministratorPermission}</h3>
 *
 * @author Andreas Hoegger
 */
public class AdministratorPermission extends BasicPermission {
  private static final long serialVersionUID = 1L;

  /**
   * @param name
   */
  public AdministratorPermission() {
    super("Administrator");
  }

}

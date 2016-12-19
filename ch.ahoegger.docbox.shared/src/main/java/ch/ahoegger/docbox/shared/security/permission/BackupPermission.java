package ch.ahoegger.docbox.shared.security.permission;

import java.security.BasicPermission;

/**
 * <h3>{@link BackupPermission}</h3>
 *
 * @author Andreas Hoegger
 */
public class BackupPermission extends BasicPermission {
  private static final long serialVersionUID = 1L;

  /**
   * @param name
   */
  public BackupPermission() {
    super("Backup");
  }

}

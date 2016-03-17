package ch.ahoegger.docbox.shared.permission;

import java.security.BasicPermission;
import java.security.Permission;

/**
 * <h3>{@link DocumentReadPermission}</h3>
 *
 * @author Andreas Hoegger
 */
public class DocumentReadPermission extends BasicPermission {
  private static final long serialVersionUID = 1L;

  /**
   * @param name
   * @param actions
   */
  public DocumentReadPermission() {
    super("DocumentReadPermission");
  }

  @Override
  public boolean implies(Permission p) {
    return super.implies(p);
  }

}

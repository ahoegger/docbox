package ch.ahoegger.docbox.shared.permission;

import java.security.BasicPermission;
import java.security.Permission;

/**
 * <h3>{@link EntityReadPermission}</h3>
 *
 * @author Andreas Hoegger
 */
public class EntityReadPermission extends BasicPermission {

  private static final long serialVersionUID = 1L;
  private Long m_entityId;

  /**
   * @param name
   */
  public EntityReadPermission(Long entityId) {
    super("Read Entity: " + entityId);
    m_entityId = entityId;
  }

  public Long getEntityId() {
    return m_entityId;
  }

  @Override
  public boolean implies(Permission permission) {
    return false;
  }

  @Override
  public boolean equals(Object obj) {
    return super.equals(obj);
  }

}

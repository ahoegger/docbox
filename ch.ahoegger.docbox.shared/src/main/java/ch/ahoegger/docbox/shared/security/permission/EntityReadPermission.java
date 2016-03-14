package ch.ahoegger.docbox.shared.security.permission;

import java.security.BasicPermission;
import java.security.Permission;

import org.eclipse.scout.rt.platform.BEANS;

/**
 * <h3>{@link EntityReadPermission}</h3>
 *
 * @author aho
 */
public class EntityReadPermission extends BasicPermission {
  private static final long serialVersionUID = 1L;
  private final Long m_entityId;
  private final String m_userId;

  public EntityReadPermission(String userId) {
    this(userId, null);
  }

  public EntityReadPermission(Long entityId) {
    this(null, entityId);
  }

  public EntityReadPermission(String userId, Long entityId) {
    super("Entity read permission");
    m_userId = userId;
    m_entityId = entityId;
  }

  @Override
  public boolean implies(Permission p) {
    if (p.getClass() == EntityReadPermission.class) {
      Long entityId = ((EntityReadPermission) p).getEntityId();
      return BEANS.get(IPermissionService.class).hasReadAccess(getUserId(), entityId);
    }
    return false;
  }

  public Long getEntityId() {
    return m_entityId;
  }

  public String getUserId() {
    return m_userId;
  }
}

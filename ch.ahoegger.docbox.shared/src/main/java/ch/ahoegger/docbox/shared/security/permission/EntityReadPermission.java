package ch.ahoegger.docbox.shared.security.permission;

import java.security.BasicPermission;
import java.security.Permission;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;
import org.eclipse.scout.rt.shared.services.common.security.IAccessControlService;

/**
 * <h3>{@link EntityReadPermission}</h3>
 *
 * @author Andreas Hoegger
 */
public class EntityReadPermission extends BasicPermission {
  private static final long serialVersionUID = 1L;
  private final Long m_entityId;

  public EntityReadPermission() {
    this(null);
  }

  public EntityReadPermission(Long entityId) {
    super("Entity read permission");
    m_entityId = entityId;
  }

  @Override
  public boolean implies(Permission p) {
    if (ACCESS.check(new AdministratorPermission())) {
      return true;
    }
    if (p.getClass() == EntityReadPermission.class) {
      Long entityId = ((EntityReadPermission) p).getEntityId();
      return BEANS.get(IPermissionService.class).hasReadAccess(BEANS.get(IAccessControlService.class).getUserIdOfCurrentSubject(), entityId);
    }
    return false;
  }

  public Long getEntityId() {
    return m_entityId;
  }

}

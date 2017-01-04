package ch.ahoegger.docbox.server.security;

import java.security.PermissionCollection;

import org.eclipse.scout.rt.platform.Replace;

import ch.ahoegger.docbox.shared.security.permission.AccessControlService;
import ch.ahoegger.docbox.shared.security.permission.AdministratorPermission;

/**
 * <h3>{@link DevAccessControlService}</h3>
 *
 * @author Andreas Hoegger
 */
@Replace
public class DevAccessControlService extends AccessControlService {

  @Override
  protected PermissionCollection execLoadPermissions(String userId) {
    PermissionCollection permissions = super.execLoadPermissions(userId);
    permissions.add(new AdministratorPermission());
    return permissions;
  }
}

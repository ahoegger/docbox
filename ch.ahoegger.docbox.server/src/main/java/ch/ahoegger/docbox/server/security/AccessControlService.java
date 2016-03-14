package ch.ahoegger.docbox.server.security;

import java.security.Permissions;

import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.shared.security.RemoteServiceAccessPermission;
import org.eclipse.scout.rt.shared.services.common.security.UserIdAccessControlService;

import ch.ahoegger.docbox.shared.security.permissions.EntityReadPermission;

/**
 * <h3>{@link AccessControlService}</h3>
 *
 * @author aho
 */
@Order(-1)
public class AccessControlService extends UserIdAccessControlService {
  @Override
  protected Permissions execLoadPermissions(String userId) {
    Permissions permissions = new Permissions();
    permissions.add(new RemoteServiceAccessPermission("*.shared.*", "*"));

    // TODO [aho]: Fill access control service

//    permissions.add(new AllPermission());

    permissions.add(new EntityReadPermission(getUserIdOfCurrentSubject()));
//    permissions.add(new DocumentReadPermission());
//    permissions.add(new UsernamePermission(getUserIdOfCurrentUser()));
    return permissions;
  }
}

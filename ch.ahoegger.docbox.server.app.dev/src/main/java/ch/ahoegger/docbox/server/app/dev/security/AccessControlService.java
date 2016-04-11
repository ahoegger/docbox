package ch.ahoegger.docbox.server.app.dev.security;

import java.security.Permissions;

import org.eclipse.scout.rt.platform.Replace;

import ch.ahoegger.docbox.shared.security.permission.AdministratorPermission;

/**
 * <h3>{@link AccessControlService}</h3>
 *
 * @author aho
 */
@Replace
public class AccessControlService extends ch.ahoegger.docbox.server.security.AccessControlService {

  @Override
  protected Permissions execLoadPermissions(String userId) {
    Permissions permissions = super.execLoadPermissions(userId);
    permissions.add(new AdministratorPermission());
    return permissions;
  }
}

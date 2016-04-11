package ch.ahoegger.docbox.server.security;

import java.security.Permissions;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.shared.security.RemoteServiceAccessPermission;
import org.eclipse.scout.rt.shared.services.common.security.UserIdAccessControlService;

import ch.ahoegger.docbox.server.administration.user.UserService;
import ch.ahoegger.docbox.server.backup.FtpBackupService;
import ch.ahoegger.docbox.shared.administration.user.UserFormData;
import ch.ahoegger.docbox.shared.security.permission.AdministratorPermission;
import ch.ahoegger.docbox.shared.security.permission.BackupPermission;
import ch.ahoegger.docbox.shared.security.permission.EntityReadPermission;

/**
 * <h3>{@link AccessControlService}</h3>
 *
 * @author Andreas Hoegger
 */
@Order(-1)
public class AccessControlService extends UserIdAccessControlService {
  @Override
  protected Permissions execLoadPermissions(String userId) {

    Permissions permissions = new Permissions();
    permissions.add(new RemoteServiceAccessPermission("*.shared.*", "*"));

    // TODO [aho]: Fill access control service

//    permissions.add(new AllPermission());

    permissions.add(new EntityReadPermission());
    // user admin check
    UserFormData userFd = new UserFormData();
    userFd.getUsername().setValue(getUserIdOfCurrentUser());
    userFd = BEANS.get(UserService.class).load(userFd);
    if (userFd != null && userFd.getAdministrator().getValue()) {
      permissions.add(new AdministratorPermission());
    }

    // backup user
    if (StringUtility.equalsIgnoreCase(FtpBackupService.BACKUP_USER_NAME, userId)) {
      permissions.add(new BackupPermission());
    }
//    permissions.add(new DocumentReadPermission());
//    permissions.add(new UsernamePermission(getUserIdOfCurrentUser()));
    return permissions;
  }
}

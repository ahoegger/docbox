package ch.ahoegger.docbox.shared.security.permission;

import java.security.PermissionCollection;
import java.security.Permissions;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.shared.security.RemoteServiceAccessPermission;
import org.eclipse.scout.rt.shared.services.common.security.AbstractAccessControlService;

import ch.ahoegger.docbox.shared.administration.user.IUserService;
import ch.ahoegger.docbox.shared.administration.user.UserFormData;
import ch.ahoegger.docbox.shared.backup.IBackupService;
import ch.ahoegger.docbox.shared.hr.entity.CreatePayslipPermission;

/**
 * <h3>{@link AccessControlService}</h3>
 *
 * @author Andreas Hoegger
 */
@Order(-1)
public class AccessControlService extends AbstractAccessControlService<String> {

  @Override
  protected String getCurrentUserCacheKey() {
    return getUserIdOfCurrentUser();
  }

  @Override
  protected PermissionCollection execLoadPermissions(String userId) {
    Permissions permissions = new Permissions();
    permissions.add(new RemoteServiceAccessPermission("*.shared.*", "*"));

    permissions.add(new CreatePayslipPermission());
    // TODO [aho]: Fill access control service

//    permissions.add(new AllPermission());

    permissions.add(new EntityReadPermission());
    // user admin check
    UserFormData userFd = new UserFormData();
    userFd.getUsername().setValue(getUserIdOfCurrentUser());
    userFd = BEANS.get(IUserService.class).load(userFd);
    if (userFd != null && userFd.getAdministrator().getValue()) {
      permissions.add(new AdministratorPermission());
    }

    // backup user
    if (StringUtility.equalsIgnoreCase(IBackupService.BACKUP_USER_NAME, userId)) {
      permissions.add(new BackupPermission());
    }
//    permissions.add(new DocumentReadPermission());
//    permissions.add(new UsernamePermission(getUserIdOfCurrentUser()));
    return permissions;
  }
}

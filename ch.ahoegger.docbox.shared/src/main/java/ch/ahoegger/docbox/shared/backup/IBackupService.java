package ch.ahoegger.docbox.shared.backup;

import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

/**
 * <h3>{@link IBackupService}</h3>
 *
 * @author Andreas Hoegger
 */
@TunnelToServer
public interface IBackupService extends IService {
  public static final String BACKUP_USER_NAME = "docadmin-backup-user";

  /**
   *
   */
  void backup();

  /**
   *
   */
  void notifyModification();

}

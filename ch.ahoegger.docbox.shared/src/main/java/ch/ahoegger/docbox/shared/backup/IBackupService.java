package ch.ahoegger.docbox.shared.backup;

import org.eclipse.scout.rt.platform.ApplicationScoped;
import org.eclipse.scout.rt.shared.TunnelToServer;

/**
 * <h3>{@link IBackupService}</h3>
 *
 * @author aho
 */
@ApplicationScoped
@TunnelToServer
public interface IBackupService {
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

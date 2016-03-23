package ch.ahoegger.docbox.shared.backup;

import org.eclipse.scout.rt.shared.TunnelToServer;

/**
 * <h3>{@link IBackupService}</h3>
 *
 * @author aho
 */
@TunnelToServer
public interface IBackupService {

  /**
   *
   */
  void backup();

  /**
   *
   */
  void notifyModification();

}

package ch.ahoegger.docbox.server.app.dev;

import org.eclipse.scout.rt.platform.Replace;

import ch.ahoegger.docbox.server.backup.FtpBackupService;

/**
 * <h3>{@link DevBackupService}</h3>
 *
 * @author aho
 */
@Replace
public class DevBackupService extends FtpBackupService {
  @Override
  public void setupChronJob() {
    // void
  }

  @Override
  public void notifyModification() {
    // void
  }

  @Override
  public void backup() {
    // void
  }
}

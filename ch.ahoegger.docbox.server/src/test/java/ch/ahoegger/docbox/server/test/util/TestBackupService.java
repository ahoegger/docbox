package ch.ahoegger.docbox.server.test.util;

import org.eclipse.scout.rt.platform.Replace;

import ch.ahoegger.docbox.server.backup.FtpBackupService;

/**
 * <h3>{@link TestBackupService}</h3>
 *
 * @author aho
 */
@Replace
public class TestBackupService extends FtpBackupService {
  private boolean m_backupNeeded = true;

  @Override
  public void setupChronJob() {
    // void
  }

  @Override
  public void backup() {
    m_backupNeeded = false;
  }

  @Override
  public void notifyModification() {
    m_backupNeeded = true;
  }

  public boolean isBackupNeeded() {
    return m_backupNeeded;
  }

}

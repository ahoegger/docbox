package ch.ahoegger.docbox.server.backup;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.SocketException;
import java.nio.file.Paths;
import java.util.Date;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.eclipse.scout.rt.platform.ApplicationScoped;
import org.eclipse.scout.rt.platform.config.AbstractPortConfigProperty;
import org.eclipse.scout.rt.platform.config.AbstractStringConfigProperty;
import org.eclipse.scout.rt.platform.config.CONFIG;
import org.eclipse.scout.rt.platform.exception.PlatformException;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.ProcessingStatus;
import org.eclipse.scout.rt.platform.status.IStatus;
import org.eclipse.scout.rt.platform.util.FileUtility;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.server.backup.internal.FtpBackupRunnable;

/**
 * <h3>{@link FtpBackupService}</h3>
 *
 * @author aho
 */
@ApplicationScoped
public class FtpBackupService {
  private static final Logger LOG = LoggerFactory.getLogger(FtpBackupService.class);

  private static enum Status {
    Idle,
    Backup
  }

  private String m_backupFileName;

  // service members
  private Date m_lastBackupDate;
  private Date m_lastModificationDate;
  private Status m_status = Status.Idle;

  public void notifyModification() {
    m_lastModificationDate = new Date();
  }

  public void backup() {
    if (m_lastBackupDate == null) {
      backupInternal();
    }
    else if (m_lastModificationDate != null) {
      if (m_lastBackupDate.before(m_lastModificationDate)) {
        backupInternal();
      }
    }
  }

  protected void backupInternal() throws ProcessingException {
    try {
      if (Status.Backup.equals(m_status)) {
        throw new ProcessingException(new ProcessingStatus("It is a backup running - try later...", IStatus.ERROR));
      }
      m_status = Status.Backup;
      checkParameters();
      // create zip file
      File zipFile = new FtpBackupRunnable().call();
      store(zipFile);
      m_lastBackupDate = new Date();
    }
    catch (Exception e) {
      // TODO EMAIL notification
      throw new ProcessingException(new ProcessingStatus("Could not create backup file.", e, 0, IStatus.ERROR));
    }
    finally {
      m_status = Status.Idle;
    }
  }

  /**
   *
   */
  private void checkParameters() {
  }

  /**
   * @param zipFile
   * @throws IOException
   * @throws PlatformException
   * @throws SocketException
   */
  private void store(File zipFile) {
    FTPClient client = new FTPClient();
    client.setConnectTimeout(20000);

    try {
      client.connect(CONFIG.getPropertyValue(FtpServerUrlProperty.class), CONFIG.getPropertyValue(FtpServerPortProperty.class));

      if (!StringUtility.isNullOrEmpty(CONFIG.getPropertyValue(FtpUsernameProperty.class))) {
        if (client.login(CONFIG.getPropertyValue(FtpUsernameProperty.class), CONFIG.getPropertyValue(FtpPasswordProperty.class))) {
          LOG.debug("FTP connection to '" + CONFIG.getPropertyValue(FtpServerUrlProperty.class) + "' login successful.");
        }
        else {
          LOG.error("FTP connection to '" + CONFIG.getPropertyValue(FtpServerUrlProperty.class) + "' login failed.");
          return;
        }
      }

      moveAndCreateDirectory(Paths.get(CONFIG.getPropertyValue(FtpRemoteBackupPathProperty.class)), client);
      LOG.debug("moved to working directory '" + CONFIG.getPropertyValue(FtpRemoteBackupPathProperty.class) + "'");
      String backupFileName = getBackupFileName();
      client.rename(backupFileName, String.format("%s.bak", backupFileName));

      // put
      FileInputStream stream = new FileInputStream(zipFile);
      try {
        client.setFileType(FTP.BINARY_FILE_TYPE, FTP.BINARY_FILE_TYPE);
        client.setFileTransferMode(FTP.BINARY_FILE_TYPE);
        BufferedInputStream bis = new BufferedInputStream(stream);
        client.storeFile(backupFileName, bis);
        bis.close();
      }
      catch (IOException e) {
        stream.close();
        throw e;
      }
    }
    catch (Exception e) {
      throw new ProcessingException(new ProcessingStatus(String.format("FTP backup: could not upload file '%s'.", zipFile.getAbsolutePath()), e, 0, IStatus.ERROR));
    }
    finally {
      try {
        client.logout();
      }
      catch (IOException e) {
        LOG.warn("unable to logout from ftp connection.", e);
      }
      try {
        client.disconnect();
      }
      catch (IOException e) {
        LOG.warn("unable to close ftp connection.", e);
      }
    }
  }

  protected void moveAndCreateDirectory(java.nio.file.Path remotePath, final FTPClient client) {
    remotePath.forEach(segment -> {
      try {
        if (!client.changeWorkingDirectory(segment.toString())) {
          if (client.makeDirectory(segment.toString())) {
            if (!client.changeWorkingDirectory(segment.toString())) {
              throw new ProcessingException("could not move working directory to '" + segment.toString() + "' in '" + client.printWorkingDirectory() + "'.");
            }
          }
          else {
            throw new ProcessingException(new ProcessingStatus("FTP backup: could not create remote directory '" + segment.toString() + "' in '" + client.printWorkingDirectory() + "'.", IStatus.ERROR));
          }
        }
      }
      catch (IOException ex) {
        throw new ProcessingException(new ProcessingStatus("FTP backup: could not create remote directory '" + segment.toString() + ".", ex, 0, IStatus.ERROR));
      }
    });
  }

  public String getBackupFileName() {
    if (m_backupFileName == null) {
      String backupFileName = CONFIG.getPropertyValue(FtpBackupFilenameProperty.class);
      if (StringUtility.isNullOrEmpty(backupFileName)) {
        m_backupFileName = "";
        throw new ProcessingException(new ProcessingStatus(String.format("Ftp backup: Property '%s' is not set.", FtpBackupFilenameProperty.KEY), IStatus.ERROR));
      }
      if (!"zip".equalsIgnoreCase(FileUtility.getFileExtension(backupFileName))) {
        backupFileName = String.format("%s.zip", backupFileName);
      }
      m_backupFileName = backupFileName;
    }
    return m_backupFileName;
  }

  public Date getLastBackupDate() {
    return m_lastBackupDate;
  }

  public Date getLastModificationDate() {
    return m_lastModificationDate;
  }

  public static class FtpServerUrlProperty extends AbstractStringConfigProperty {
    public static final String KEY = "docbox.ftp.serverUrl";

    @Override
    public String getKey() {
      return KEY;
    }
  }

  public static class FtpServerPortProperty extends AbstractPortConfigProperty {
    public static final String KEY = "docbox.ftp.serverPort";

    @Override
    public String getKey() {
      return KEY;
    }

    @Override
    protected Integer getDefaultValue() {
      return 21;
    }
  }

  public static class FtpUsernameProperty extends AbstractStringConfigProperty {
    public static final String KEY = "docbox.ftp.username";

    @Override
    public String getKey() {
      return KEY;
    }
  }

  public static class FtpPasswordProperty extends AbstractStringConfigProperty {
    public static final String KEY = "docbox.ftp.password";

    @Override
    public String getKey() {
      return KEY;
    }
  }

  public static class FtpRemoteBackupPathProperty extends AbstractStringConfigProperty {
    public static final String KEY = "docbox.ftp.remoteBackupPath";

    @Override
    public String getKey() {
      return KEY;
    }
  }

  public static class FtpBackupFilenameProperty extends AbstractStringConfigProperty {
    public static final String DEFAULT_FILE_NAME = "docboxBackup";
    public static final String KEY = "docbox.ftp.backupFileName";

    @Override
    public String getKey() {
      return KEY;
    }

    @Override
    protected String getDefaultValue() {
      return DEFAULT_FILE_NAME;
    }
  }

}

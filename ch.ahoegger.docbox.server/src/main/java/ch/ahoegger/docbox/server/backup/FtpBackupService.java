package ch.ahoegger.docbox.server.backup;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.security.auth.Subject;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.CreateImmediately;
import org.eclipse.scout.rt.platform.config.AbstractBooleanConfigProperty;
import org.eclipse.scout.rt.platform.config.AbstractPortConfigProperty;
import org.eclipse.scout.rt.platform.config.AbstractStringConfigProperty;
import org.eclipse.scout.rt.platform.config.CONFIG;
import org.eclipse.scout.rt.platform.exception.PlatformException;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.ProcessingStatus;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.job.Jobs;
import org.eclipse.scout.rt.platform.security.SimplePrincipal;
import org.eclipse.scout.rt.platform.status.IStatus;
import org.eclipse.scout.rt.platform.transaction.TransactionScope;
import org.eclipse.scout.rt.platform.util.FileUtility;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.platform.util.concurrent.IRunnable;
import org.eclipse.scout.rt.server.context.ServerRunContexts;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;
import org.eclipse.scout.rt.shared.servicetunnel.RemoteServiceAccessDenied;
import org.quartz.CronScheduleBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.server.backup.internal.ZipFile;
import ch.ahoegger.docbox.server.database.DerbySqlService;
import ch.ahoegger.docbox.server.database.DerbySqlService.DatabaseLocationProperty;
import ch.ahoegger.docbox.server.document.store.DocumentStoreService;
import ch.ahoegger.docbox.server.document.store.DocumentStoreService.DocumentStoreLocationProperty;
import ch.ahoegger.docbox.shared.administration.DbDumpFormData;
import ch.ahoegger.docbox.shared.backup.IBackupService;
import ch.ahoegger.docbox.shared.backup.IDbDumpService;
import ch.ahoegger.docbox.shared.security.permission.AdministratorPermission;
import ch.ahoegger.docbox.shared.security.permission.BackupPermission;

/**
 * <h3>{@link FtpBackupService}</h3>
 *
 * @author Andreas Hoegger
 */

@CreateImmediately
public class FtpBackupService implements IBackupService {
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

  @PostConstruct
  public void setupChronJob() {
    Subject subject = new Subject();
    subject.getPrincipals().add(new SimplePrincipal(BACKUP_USER_NAME));
    subject.setReadOnly();

    if (CONFIG.getPropertyValue(FtpBackupEnabledProperty.class)) {
      Jobs.schedule(new IRunnable() {

        @Override
        public void run() throws Exception {
          LOG.info("Start chron backup.");
          backup();
        }
      },
          Jobs.newInput()
              .withRunContext(ServerRunContexts.empty()
                  .withSubject(subject).withTransactionScope(TransactionScope.REQUIRES_NEW))
              .withName("backup job")
              .withExecutionTrigger(Jobs.newExecutionTrigger()
                  .withSchedule(CronScheduleBuilder.cronSchedule("0 10 2 * * ?"))));
    }
  }

  @Override
  public void notifyModification() {
    m_lastModificationDate = new Date();
  }

  @Override
  public void backup() {
    if (!(ACCESS.check(new AdministratorPermission())
        || ACCESS.check(new BackupPermission()))) {
      throw new VetoException("Access denied.");
    }
    if (m_lastBackupDate == null) {
      backupInternal();
    }
    else if (m_lastModificationDate != null) {
      if (m_lastBackupDate.before(m_lastModificationDate)) {
        backupInternal();
      }
    }
  }

  @RemoteServiceAccessDenied
  protected void backupInternal() throws ProcessingException {
    File zipFile = null;
    try {
      if (Status.Backup.equals(m_status)) {
        throw new ProcessingException(new ProcessingStatus("It is a backup running - try later...", IStatus.ERROR));
      }
      m_status = Status.Backup;
      // create zip file

      zipFile = createBackupFile();
      store(zipFile);
      m_lastBackupDate = new Date();
    }
    catch (Exception e) {
      // TODO EMAIL notification
      throw new ProcessingException(new ProcessingStatus("Could not create backup file.", e, 0, IStatus.ERROR));
    }
    finally {
      if (zipFile != null) {
        try {
          zipFile.delete();
        }
        catch (Exception e) {
          LOG.warn("Could not delete zipFile '" + zipFile + "'.", e);
        }
      }
      m_status = Status.Idle;

    }
  }

  protected File createBackupFile() throws ProcessingException {
    String databaseLocation = CONFIG.getPropertyValue(DatabaseLocationProperty.class);
    if (StringUtility.isNullOrEmpty(databaseLocation)) {
      throw new ProcessingException(new ProcessingStatus(String.format("Document store: Could not create backup file: database location is not set! [Property:'%s']", DatabaseLocationProperty.KEY), IStatus.ERROR));
    }
    String documentStoreLocation = CONFIG.getPropertyValue(DocumentStoreLocationProperty.class);
    if (StringUtility.isNullOrEmpty(documentStoreLocation)) {
      throw new ProcessingException(new ProcessingStatus(String.format("Document store: Could not create backup file: document store location is not set! [Property:'%s']", DocumentStoreLocationProperty.KEY), IStatus.ERROR));
    }

    // create zip file
    ZipFile zipFile = null;
    try {
      zipFile = new ZipFile(Files.createTempFile("backup-tmp", ".zip").toFile());
      zipFile.getArchiveFile().deleteOnExit();

      // db dump
      BEANS.get(DocumentStoreService.class).backup(zipFile);
      BEANS.get(DerbySqlService.class).backup(zipFile);
      IDbDumpService dbDumpService = BEANS.get(IDbDumpService.class);
      DbDumpFormData formData = new DbDumpFormData();
      formData = dbDumpService.load(formData);
      zipFile.addFile("docadminDbDump.sql", formData.getDBScript().getValue().getBytes());

      return zipFile.getArchiveFile();
    }
    catch (IOException e) {
      throw new ProcessingException(new ProcessingStatus(String.format("Document store: Could not create backup file!"), e, 0, IStatus.ERROR));
    }
    finally {
      if (zipFile != null) {
        try {
          zipFile.close();
        }
        catch (IOException e) {
          // void
        }
      }
    }
  }

  /**
   * @param zipFile
   * @throws IOException
   * @throws PlatformException
   * @throws SocketException
   */
  @RemoteServiceAccessDenied
  private void store(File zipFile) {
    FTPClient client = new FTPClient();
    client.setConnectTimeout(20000);

    try {
      client.connect(CONFIG.getPropertyValue(FtpServerUrlProperty.class), CONFIG.getPropertyValue(FtpServerPortProperty.class));

      if (!StringUtility.isNullOrEmpty(CONFIG.getPropertyValue(FtpUsernameProperty.class))) {
        if (client.login(CONFIG.getPropertyValue(FtpUsernameProperty.class), CONFIG.getPropertyValue(FtpPasswordProperty.class))) {
          LOG.info("FTP connection to '" + CONFIG.getPropertyValue(FtpServerUrlProperty.class) + "' login successful.");
        }
        else {
          LOG.error("FTP connection to '" + CONFIG.getPropertyValue(FtpServerUrlProperty.class) + "' login failed.");
          return;
        }
      }

      moveAndCreateDirectory(Paths.get(CONFIG.getPropertyValue(FtpRemoteBackupPathProperty.class)), client);
      LOG.info("moved to working directory '" + CONFIG.getPropertyValue(FtpRemoteBackupPathProperty.class) + "'");
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

  @RemoteServiceAccessDenied
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

  @RemoteServiceAccessDenied
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

  @RemoteServiceAccessDenied
  public Date getLastBackupDate() {
    return m_lastBackupDate;
  }

  @RemoteServiceAccessDenied
  public Date getLastModificationDate() {
    return m_lastModificationDate;
  }

  public static class FtpBackupEnabledProperty extends AbstractBooleanConfigProperty {
    public static final String KEY = "docbox.ftp.backupEnabled";

    @Override
    public String getKey() {
      return KEY;
    }

    @Override
    public Boolean getDefaultValue() {
      return false;
    }

    @Override
    public String description() {
      // TODO
      return null;
    }
  }

  public static class FtpServerUrlProperty extends AbstractStringConfigProperty {
    public static final String KEY = "docbox.ftp.serverUrl";

    @Override
    public String getKey() {
      return KEY;
    }

    @Override
    public String description() {
      // TODO
      return null;
    }
  }

  public static class FtpServerPortProperty extends AbstractPortConfigProperty {
    public static final String KEY = "docbox.ftp.serverPort";

    @Override
    public String getKey() {
      return KEY;
    }

    @Override
    public Integer getDefaultValue() {
      return 21;
    }

    @Override
    public String description() {
      // TODO
      return null;
    }
  }

  public static class FtpUsernameProperty extends AbstractStringConfigProperty {
    public static final String KEY = "docbox.ftp.username";

    @Override
    public String getKey() {
      return KEY;
    }

    @Override
    public String description() {
      // TODO
      return null;
    }
  }

  public static class FtpPasswordProperty extends AbstractStringConfigProperty {
    public static final String KEY = "docbox.ftp.password";

    @Override
    public String getKey() {
      return KEY;
    }

    @Override
    public String description() {
      // TODO
      return null;
    }
  }

  public static class FtpRemoteBackupPathProperty extends AbstractStringConfigProperty {
    public static final String KEY = "docbox.ftp.remoteBackupPath";

    @Override
    public String getKey() {
      return KEY;
    }

    @Override
    public String description() {
      // TODO
      return "TODO";
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
    public String getDefaultValue() {
      return DEFAULT_FILE_NAME;
    }

    @Override
    public String description() {
      // TODO
      return null;
    }
  }

}

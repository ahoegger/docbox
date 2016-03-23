package ch.ahoegger.docbox.server.backup.internal;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

import org.eclipse.scout.rt.platform.config.AbstractPortConfigProperty;
import org.eclipse.scout.rt.platform.config.AbstractStringConfigProperty;
import org.eclipse.scout.rt.platform.config.CONFIG;
import org.eclipse.scout.rt.platform.exception.PlatformException;
import org.eclipse.scout.rt.platform.job.Jobs;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.platform.util.concurrent.IRunnable;
import org.eclipse.scout.rt.server.context.ServerRunContexts;

import ch.ahoegger.docbox.server.database.DerbySqlService.DatabaseLocationProperty;
import ch.ahoegger.docbox.server.document.store.DocumentStoreService;
import ch.ahoegger.docbox.server.document.store.DocumentStoreService.DocumentStoreLocationProperty;

/**
 * <h3>{@link FtpBackupRunnable}</h3>
 *
 * @author aho
 */
public class FtpBackupRunnable implements Callable<File> {

  @Override
  public File call() throws Exception {
    String databaseLocation = CONFIG.getPropertyValue(DatabaseLocationProperty.class);
    if (StringUtility.isNullOrEmpty(databaseLocation)) {
      throw new IllegalAccessException(String.format("Could not create backup file: database location is not set! [Property:'%s']", DatabaseLocationProperty.KEY));
    }
    String documentStoreLocation = CONFIG.getPropertyValue(DocumentStoreLocationProperty.class);
    if (StringUtility.isNullOrEmpty(documentStoreLocation)) {
      throw new IllegalAccessException(String.format("Could not create backup file: document store location is not set! [Property:'%s']", DocumentStoreLocationProperty.KEY));
    }

    // create zip file
    ZipFile zipFile = null;
    try {
      zipFile = new ZipFile(Files.createTempFile("backup-tmp", ".zip").toFile());
      zipFile.getArchiveFile().deleteOnExit();
//      zipFile.addFile("docadminDbDump.sql", getDBDump());
      appendDocumentStore(zipFile);
      appendDatabase(zipFile);
      zipFile.addFile(Paths.get(databaseLocation).toFile());

      return zipFile.getArchiveFile();
    }
    finally {
      if (zipFile != null) {
        zipFile.close();
      }
    }
  }

  private void appendDocumentStore(final ZipFile zipFile) {
    Jobs.schedule(new IRunnable() {

      @Override
      public void run() throws Exception {
        // access files
        zipFile.addFile(Paths.get(CONFIG.getPropertyValue(DocumentStoreLocationProperty.class)).toFile());
      }
    }, Jobs.newInput().withRunContext(ServerRunContexts.copyCurrent()).withExecutionSemaphore(DocumentStoreService.DOCUMENT_ACCESS_SEMAPHORE)).awaitDone();

  }

  private void appendDatabase(final ZipFile zipFile) throws PlatformException, IOException {
    // TODO lock db
    zipFile.addFile(Paths.get(CONFIG.getPropertyValue(DatabaseLocationProperty.class)).toFile());
  }

  public static class FtpServerUrlProperty extends AbstractStringConfigProperty {
    public static final String KEY = "ftp.serverUrl";

    @Override
    public String getKey() {
      return KEY;
    }
  }

  public static class FtpServerPortProperty extends AbstractPortConfigProperty {
    public static final String KEY = "ftp.serverPort";

    @Override
    public String getKey() {
      return KEY;
    }
  }

  public static class FtpUsernameProperty extends AbstractStringConfigProperty {
    public static final String KEY = "ftp.username";

    @Override
    public String getKey() {
      return KEY;
    }
  }

  public static class FtpPasswordProperty extends AbstractStringConfigProperty {
    public static final String KEY = "ftp.password";

    @Override
    public String getKey() {
      return KEY;
    }
  }

  public static class FtpRemoteBackupPathProperty extends AbstractStringConfigProperty {
    public static final String KEY = "ftp.remoteBackupPath";

    @Override
    public String getKey() {
      return KEY;
    }
  }

  public static class BackupFilenameProperty extends AbstractStringConfigProperty {
    public static final String KEY = "ftp.backupFileName";

    @Override
    public String getKey() {
      return KEY;
    }
  }
}

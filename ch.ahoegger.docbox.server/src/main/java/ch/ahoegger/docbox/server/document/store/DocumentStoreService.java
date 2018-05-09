package ch.ahoegger.docbox.server.document.store;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.config.AbstractStringConfigProperty;
import org.eclipse.scout.rt.platform.config.CONFIG;
import org.eclipse.scout.rt.platform.exception.PlatformException;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.ProcessingStatus;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.resource.BinaryResource;
import org.eclipse.scout.rt.platform.resource.BinaryResources;
import org.eclipse.scout.rt.platform.status.Status;
import org.eclipse.scout.rt.platform.util.FileUtility;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;
import org.eclipse.scout.rt.shared.servicetunnel.RemoteServiceAccessDenied;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.server.backup.internal.ZipFile;
import ch.ahoegger.docbox.server.document.DocumentService;
import ch.ahoegger.docbox.shared.document.DocumentFormData;
import ch.ahoegger.docbox.shared.document.store.IDocumentStoreService;
import ch.ahoegger.docbox.shared.security.permission.AdministratorPermission;
import ch.ahoegger.docbox.shared.security.permission.EntityReadPermission;

/**
 * <h3>{@link DocumentStoreService}</h3>
 *
 * @author Andreas Hoegger
 */
public class DocumentStoreService implements IDocumentStoreService {

  private static final Logger LOG = LoggerFactory.getLogger(DocumentStoreService.class);

  private Path m_documentStore;
  protected static final Object IO_LOCK = new Object();

  public DocumentStoreService() {
    m_documentStore = Paths.get(getConfiguredDocumentStoreLocation());
  }

  protected String getConfiguredDocumentStoreLocation() {
    String docStoreLocation = CONFIG.getPropertyValue(DocumentStoreLocationProperty.class);
    if (StringUtility.isNullOrEmpty(docStoreLocation)) {
      throw new ProcessingException("Document store location is not set. See property '{}'", new Object[]{DocumentStoreLocationProperty.KEY});
    }
    return docStoreLocation;
  }

  protected Path getDocumentStore() {
    return m_documentStore;
  }

  @Override
  public BinaryResource getDocument(BigDecimal documentId) {
    // permissioncheck
    if (!ACCESS.check(new EntityReadPermission(documentId))) {
      throw new VetoException("Access denied");
    }

    DocumentFormData formData = new DocumentFormData();
    formData.setDocumentId(documentId);
    formData = BEANS.get(DocumentService.class).loadTrusted(formData);
    BinaryResource resource = get(formData.getDocumentPath());
    return resource;
  }

  @RemoteServiceAccessDenied
  public String store(BinaryResource resource, Date capturedDate, BigDecimal documentId) {
    DateFormat dfYear = new SimpleDateFormat("yyyy");
    DateFormat df = new SimpleDateFormat("yyyy_MM_dd");
    StringBuilder pathBuilder = new StringBuilder();
    pathBuilder.append(dfYear.format(capturedDate)).append("/").append(df.format(capturedDate)).append("_").append(documentId).append(".").append(FileUtility.getFileExtension(resource.getFilename()));
    synchronized (IO_LOCK) {
      Path file = toAbsolutePath(pathBuilder.toString());
      if (Files.exists(file)) {
        LOG.warn(String.format("document['%s'] already exists serverside!", file));
        return pathBuilder.toString();
      }
      else {
        java.io.OutputStream out = null;
        try {
          Files.createDirectories(file.getParent());
          file = Files.createFile(file);
          out = new BufferedOutputStream(Files.newOutputStream(file));
          out.write(resource.getContent());
          out.flush();
          return pathBuilder.toString();
        }
        catch (IOException ex) {
          throw new ProcessingException(new ProcessingStatus("could not write document ['" + file + "']!", ex, 0, Status.ERROR));
        }
        finally {
          if (out != null) {
            try {
              out.close();
            }
            catch (IOException e) {
              LOG.error("Could not close output stream for file '{}'.", file);
            }
          }
        }
      }

    }
  }

  @RemoteServiceAccessDenied
  public boolean delete(String path) {
    // permissioncheck
    if (!ACCESS.check(new AdministratorPermission())) {
      throw new VetoException("Access denied");
    }

    synchronized (IO_LOCK) {
      Path file = toAbsolutePath(path);
      if (Files.exists(file)) {
        try {
          Files.delete(file);
          return true;
        }
        catch (IOException e) {
          LOG.error("Could not delete file '" + file + "'.", e);
        }
      }
      return false;
    }
  }

  @RemoteServiceAccessDenied
  public void backup(ZipFile zipFile) throws PlatformException, IOException {
    synchronized (IO_LOCK) {
      zipFile.addFile(Paths.get(CONFIG.getPropertyValue(DocumentStoreLocationProperty.class)).toFile());
    }
  }

  @RemoteServiceAccessDenied
  public boolean exists(String path) {
    synchronized (IO_LOCK) {
      Path file = toAbsolutePath(path);
      return Files.exists(file);
    }
  }

  @RemoteServiceAccessDenied
  public BinaryResource get(String path) {

    synchronized (IO_LOCK) {
      Path file = toAbsolutePath(path);
      if (Files.exists(file) && Files.isReadable(file)) {
        try {
          return BinaryResources.create().withFilename(file.getFileName().toString())
              .withContentType(FileUtility.getMimeType(file))
              .withContent(Files.readAllBytes(file))
              .withLastModified(Files.getLastModifiedTime(file).toMillis())
              .withCachingAllowed(false).build();
        }
        catch (IOException ex) {
          throw new ProcessingException(new ProcessingStatus("could not read document ['" + file + "']!", ex, 0, Status.ERROR));
        }
      }
      else {
        throw new ProcessingException(new ProcessingStatus("document '" + file + "' does not exist serverside!", Status.ERROR));
      }
    }
  }

  protected Path toAbsolutePath(String documentPath) {
    if (StringUtility.hasText(documentPath)) {
      documentPath = documentPath.replaceAll("\\+", "/");
      documentPath = documentPath.replaceAll("^/", "");
    }
    return getDocumentStore().resolve(Paths.get(documentPath));

  }

  public static class DocumentStoreLocationProperty extends AbstractStringConfigProperty {

    public static final String KEY = "docbox.server.documentStoreLocation";

    @Override
    public String getKey() {
      return KEY;
    }

    @Override
    public String description() {
      return "The location of the document store. All documents are expected under the specified location and will be stored there.";
    }
  }

}

package ch.ahoegger.docbox.server.document.store;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
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
import org.eclipse.scout.rt.platform.util.IOUtility;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;
import org.eclipse.scout.rt.shared.servicetunnel.RemoteServiceAccessDenied;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.server.backup.internal.ZipFile;
import ch.ahoegger.docbox.server.document.DocumentService;
import ch.ahoegger.docbox.shared.document.DocumentFormData;
import ch.ahoegger.docbox.shared.document.store.IDocumentStoreService;
import ch.ahoegger.docbox.shared.security.permission.EntityReadPermission;

/**
 * <h3>{@link DocumentStoreService}</h3>
 *
 * @author Andreas Hoegger
 */
public class DocumentStoreService implements IDocumentStoreService {
  @SuppressWarnings("unused")
  private static final Logger LOG = LoggerFactory.getLogger(DocumentStoreService.class);

  private File m_documentStoreDirectory;
  protected static final Object IO_LOCK = new Object();

  public DocumentStoreService() {
    m_documentStoreDirectory = new File(getConfiguredDocumentStoreLocation());
  }

  protected String getConfiguredDocumentStoreLocation() {
    String docStoreLocation = CONFIG.getPropertyValue(DocumentStoreLocationProperty.class);
    if (StringUtility.isNullOrEmpty(docStoreLocation)) {
      throw new ProcessingException("Document store location is not set. See property '{}'", new Object[]{DocumentStoreLocationProperty.KEY});
    }
    return docStoreLocation;
  }

  protected File getDocumentStoreDirectory() {
    return m_documentStoreDirectory;
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
    pathBuilder.append("/").append(dfYear.format(capturedDate)).append("/").append(df.format(capturedDate)).append("_").append(documentId).append(".").append(FileUtility.getFileExtension(resource.getFilename()));
    synchronized (IO_LOCK) {
      File file = new File(getDocumentStoreDirectory(), pathBuilder.toString());
      if (file.exists()) {
        throw new ProcessingException(new ProcessingStatus("document['" + file.getAbsolutePath() + "'] already exists serverside!", Status.ERROR));
      }
      else {
        try {
          file.getParentFile().mkdirs();
          file.createNewFile();
          IOUtility.writeBytes(new FileOutputStream(file), resource.getContent());
          return pathBuilder.toString();
        }
        catch (IOException e) {
          throw new ProcessingException(new ProcessingStatus("could not write document serverside!", e, 0, Status.ERROR));
        }
      }
    }
  }

  @RemoteServiceAccessDenied
  public void backup(ZipFile zipFile) throws PlatformException, IOException {
    synchronized (IO_LOCK) {
      zipFile.addFile(Paths.get(CONFIG.getPropertyValue(DocumentStoreLocationProperty.class)).toFile());
    }
  }

  @RemoteServiceAccessDenied
  public BinaryResource get(String path) {
    synchronized (IO_LOCK) {
      File f = new File(getDocumentStoreDirectory(), path);
      if (f.exists() && f.canRead()) {
        return BinaryResources.create().withFilename(f.getName()).withContentType(FileUtility.getContentType(f)).withContent(IOUtility.getContent(f)).withLastModified(f.lastModified()).withCachingAllowed(false).build();
      }
      else {
        throw new ProcessingException(new ProcessingStatus("document '" + f.getAbsolutePath() + "' does not exist serverside!", Status.ERROR));
      }
    }
  }

  public static class DocumentStoreLocationProperty extends AbstractStringConfigProperty {

    public static final String KEY = "docbox.server.documentStoreLocation";

    @Override
    public String getKey() {
      return KEY;
    }
  }

}

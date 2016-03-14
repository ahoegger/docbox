package ch.ahoegger.docbox.server.document.store;

import java.io.File;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.resource.BinaryResource;
import org.eclipse.scout.rt.shared.services.common.file.RemoteFile;
import org.eclipse.scout.rt.shared.servicetunnel.RemoteServiceAccessDenied;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.server.document.DocumentService;
import ch.ahoegger.docbox.shared.document.DocumentFormData;
import ch.ahoegger.docbox.shared.document.store.IDocumentStoreService;

/**
 * <h3>{@link DocumentStoreService}</h3>
 *
 * @author aho
 */
public class DocumentStoreService implements IDocumentStoreService {
  private static final Logger LOG = LoggerFactory.getLogger(DocumentStoreService.class);

  @Override
  public BinaryResource getDocument(Long documentId) {
    DocumentFormData formData = new DocumentFormData();
    formData.setDocumentId(documentId);
    formData = BEANS.get(DocumentService.class).loadTrusted(formData);

    BinaryResource resource = getDocument(formData.getDocumentPath());
    return resource;
  }

  @Override
  public File store(RemoteFile remoteFile) {
    return null;
  }

  @RemoteServiceAccessDenied
  public BinaryResource getDocument(String path) {
    //TODO
    return null;
  }
}

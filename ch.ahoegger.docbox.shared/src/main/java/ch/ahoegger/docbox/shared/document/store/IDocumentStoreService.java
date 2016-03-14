package ch.ahoegger.docbox.shared.document.store;

import java.io.File;

import org.eclipse.scout.rt.platform.ApplicationScoped;
import org.eclipse.scout.rt.platform.resource.BinaryResource;
import org.eclipse.scout.rt.shared.TunnelToServer;
import org.eclipse.scout.rt.shared.services.common.file.RemoteFile;

/**
 * <h3>{@link IFileService}</h3>
 *
 * @author aho
 */
@ApplicationScoped
@TunnelToServer
public interface IDocumentStoreService {

  File store(RemoteFile remoteFile);

  /**
   * @param path
   * @return
   */
  BinaryResource getDocument(Long docuemntId);
}

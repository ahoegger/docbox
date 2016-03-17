package ch.ahoegger.docbox.shared.document.store;

import org.eclipse.scout.rt.platform.ApplicationScoped;
import org.eclipse.scout.rt.platform.resource.BinaryResource;
import org.eclipse.scout.rt.shared.TunnelToServer;

/**
 * <h3>{@link IFileService}</h3>
 *
 * @author Andreas Hoegger
 */
@ApplicationScoped
@TunnelToServer
public interface IDocumentStoreService {

  /**
   * @param path
   * @return
   */
  BinaryResource getDocument(Long docuemntId);
}

package ch.ahoegger.docbox.shared.document.store;

import java.math.BigDecimal;

import org.eclipse.scout.rt.platform.resource.BinaryResource;
import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

/**
 * <h3>{@link IFileService}</h3>
 *
 * @author Andreas Hoegger
 */
@TunnelToServer
public interface IDocumentStoreService extends IService {

  /**
   * @param path
   * @return
   */
  BinaryResource getDocument(BigDecimal docuemntId);
}

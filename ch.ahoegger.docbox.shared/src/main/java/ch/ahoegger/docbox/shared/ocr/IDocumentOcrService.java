package ch.ahoegger.docbox.shared.ocr;

import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

import ch.ahoegger.docbox.shared.document.ocr.DocumentOcrFormData;

/**
 * <h3>{@link IDocumentOcrService}</h3>
 *
 * @author Andreas Hoegger
 */
@TunnelToServer
public interface IDocumentOcrService extends IService {

  /**
   * @param formData
   * @return
   */
  DocumentOcrFormData load(DocumentOcrFormData formData);

  /**
   * @param formData
   * @return
   */
  DocumentOcrFormData store(DocumentOcrFormData formData);

}

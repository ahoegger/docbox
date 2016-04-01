package ch.ahoegger.docbox.shared.ocr;

import org.eclipse.scout.rt.platform.Bean;
import org.eclipse.scout.rt.shared.TunnelToServer;

import ch.ahoegger.docbox.shared.document.ocr.DocumentOcrFormData;

/**
 * <h3>{@link IDocumentOcrService}</h3>
 *
 * @author aho
 */
@Bean
@TunnelToServer
public interface IDocumentOcrService {

  /**
   * @param formData
   * @return
   */
  DocumentOcrFormData load(DocumentOcrFormData formData);

}

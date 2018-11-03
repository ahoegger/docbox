package ch.ahoegger.docbox.shared.ocr;

import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

import ch.ahoegger.docbox.shared.document.OcrResultGroupBoxData;

/**
 * <h3>{@link IDocumentOcrService}</h3>
 *
 * @author Andreas Hoegger
 */
@TunnelToServer
public interface IDocumentOcrService extends IService {

  OcrResultGroupBoxData load(OcrResultGroupBoxData formData);

  OcrResultGroupBoxData store(OcrResultGroupBoxData formData);

//  /**
//   * @param formData
//   * @return
//   */
//  @Deprecated
//  DocumentOcrFormData load(DocumentOcrFormData formData);
//
//  /**
//   * @param formData
//   * @return
//   */
//  @Deprecated
//  DocumentOcrFormData store(DocumentOcrFormData formData);

}

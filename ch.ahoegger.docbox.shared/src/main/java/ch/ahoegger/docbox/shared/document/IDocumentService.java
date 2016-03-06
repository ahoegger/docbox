package ch.ahoegger.docbox.shared.document;

import org.eclipse.scout.rt.platform.Bean;
import org.eclipse.scout.rt.shared.TunnelToServer;

/**
 * <h3>{@link IDocumentService}</h3>
 *
 * @author aho
 */
@Bean
@TunnelToServer
public interface IDocumentService {

  /**
   * @param formData
   */
  DocumentTableData getTableData(DocumentSearchFormData formData);

  /**
   * @param formData
   */
  void store(DocumentFormData formData);

}

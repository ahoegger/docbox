package ch.ahoegger.docbox.shared.document;

import org.eclipse.scout.rt.platform.Bean;
import org.eclipse.scout.rt.shared.TunnelToServer;

/**
 * <h3>{@link IDocumentService}</h3>
 *
 * @author Andreas Hoegger
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

  /**
   * @param formData
   * @return
   */
  DocumentFormData load(DocumentFormData formData);

  /**
   * @param formData
   */
  DocumentFormData prepareCreate(DocumentFormData formData);

  /**
   * @param formData
   */
  void create(DocumentFormData formData);

}

package ch.ahoegger.docbox.shared.document;

import java.math.BigDecimal;
import java.util.List;

import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

/**
 * <h3>{@link IDocumentService}</h3>
 *
 * @author Andreas Hoegger
 */
@TunnelToServer
public interface IDocumentService extends IService {

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
   * @return
   */
  DocumentFormData create(DocumentFormData formData);

  /**
   * @param selectedValue
   */
  boolean delete(BigDecimal selectedValue);

  /**
   *
   */
  void buildOcrOfMissingDocuments();

  /**
   * @param selectedValues
   */
  void buildOcrOfMissingDocuments(List<BigDecimal> documentIds);

  /**
   * @param selectedValues
   */
  void deletePasedConent(List<BigDecimal> documentIds);

  /**
   * @param fd
   */
  void replaceDocument(ReplaceDocumentFormData formData);

}

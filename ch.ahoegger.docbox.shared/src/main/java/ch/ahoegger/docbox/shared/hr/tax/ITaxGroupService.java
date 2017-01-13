package ch.ahoegger.docbox.shared.hr.tax;

import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

/**
 * <h3>{@link ITaxGroupService}</h3>
 *
 * @author aho
 */
@TunnelToServer
public interface ITaxGroupService extends IService {

  /**
   * @param filter
   * @return
   */
  TaxGroupTablePageData getTaxGroupTableData(TaxGroupSearchFormData searchFormData);

  /**
   * @param formData
   * @return
   */
  TaxGroupFormData prepareCreate(TaxGroupFormData formData);

  /**
   * @param formData
   */
  TaxGroupFormData create(TaxGroupFormData formData);

  /**
   * @param formData
   * @return
   */
  TaxGroupFormData load(TaxGroupFormData formData);

  /**
   * @param formData
   */
  TaxGroupFormData store(TaxGroupFormData formData);
}

package ch.ahoegger.docbox.shared.administration.taxgroup;

import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

import ch.ahoegger.docbox.shared.hr.tax.TaxGroupFormData;
import ch.ahoegger.docbox.shared.hr.tax.TaxGroupSearchFormData;
import ch.ahoegger.docbox.shared.hr.tax.TaxGroupTablePageData;

/**
 * <h3>{@link ITaxGroupService}</h3>
 *
 * @author Andreas Hoegger
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

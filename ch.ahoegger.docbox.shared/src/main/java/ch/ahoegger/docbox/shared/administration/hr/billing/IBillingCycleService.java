package ch.ahoegger.docbox.shared.administration.hr.billing;

import java.math.BigDecimal;

import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.platform.status.IStatus;
import org.eclipse.scout.rt.shared.TunnelToServer;

import ch.ahoegger.docbox.shared.administration.hr.billingcycle.BillingCycleFormData;
import ch.ahoegger.docbox.shared.administration.hr.billingcycle.BillingCycleSearchFormData;
import ch.ahoegger.docbox.shared.administration.hr.billingcycle.BillingCycleTablePageData;

/**
 * <h3>{@link IBillingCycleService}</h3>
 *
 * @author aho
 */
@TunnelToServer
public interface IBillingCycleService extends IService {

  /**
   * @param formData
   * @return
   */
  BillingCycleTablePageData getTableData(BillingCycleSearchFormData formData);

  /**
   * @param formData
   * @return
   */
  IStatus validate(BillingCycleFormData formData);

  /**
   * @param formData
   * @return
   */
  IStatus validateNew(BillingCycleFormData formData);

  /**
   * @param formData
   * @return
   */
  BillingCycleFormData prepareCreate(BillingCycleFormData formData);

  /**
   * @param formData
   * @return
   */
  BillingCycleFormData create(BillingCycleFormData formData);

  /**
   * @param formData
   * @return
   */
  BillingCycleFormData load(BillingCycleFormData formData);

  /**
   * @param formData
   * @return
   */
  BillingCycleFormData store(BillingCycleFormData formData);

  /**
   * @param selectedValue
   * @return
   */
  boolean delete(BigDecimal selectedValue);

}

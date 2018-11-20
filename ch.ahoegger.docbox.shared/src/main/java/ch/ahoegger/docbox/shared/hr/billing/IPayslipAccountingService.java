package ch.ahoegger.docbox.shared.hr.billing;

import java.math.BigDecimal;

import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

/**
 * <h3>{@link IPayslipAccountingService}</h3>
 *
 * @author Andreas Hoegger
 */
@TunnelToServer
public interface IPayslipAccountingService extends IService {

  /**
   * @param formData
   * @return
   */
  PayslipAccountingTableData getTableData(PayslipAccountingSearchFormData formData);

  /**
   * @param formData
   * @return
   */
  PayslipAccountingFormData load(PayslipAccountingFormData formData);

  /**
   * @param formData
   * @return
   */
  PayslipAccountingFormData prepareCreate(PayslipAccountingFormData formData);

  /**
   * @param formData
   */
  PayslipAccountingFormData create(PayslipAccountingFormData formData);

  /**
   * @param formData
   * @return
   */
  PayslipAccountingFormData store(PayslipAccountingFormData formData);

  /**
   * @param selectedValue
   */
  boolean delete(BigDecimal selectedValue);

  /**
   * @param fd
   * @return
   */
  PayslipAccountingFormData calculateWage(PayslipAccountingFormData fd);

}

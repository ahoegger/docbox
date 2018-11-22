package ch.ahoegger.docbox.shared.hr.billing;

import java.math.BigDecimal;

import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

/**
 * <h3>{@link IPayslipService}</h3>
 *
 * @author Andreas Hoegger
 */
@TunnelToServer
public interface IPayslipService extends IService {

  /**
   * @param formData
   * @return
   */
  PayslipTableData getTableData(PayslipSearchFormData formData);

  /**
   * @param formData
   * @return
   */
  PayslipFormData load(PayslipFormData formData);

  /**
   * @param formData
   * @return
   */
  PayslipFormData prepareCreate(PayslipFormData formData);

  /**
   * @param formData
   */
  PayslipFormData create(PayslipFormData formData);

  /**
   * @param formData
   * @return
   */
  PayslipFormData store(PayslipFormData formData);

  /**
   * @param selectedValue
   */
  boolean delete(BigDecimal selectedValue);

  /**
   * @param fd
   * @return
   */
  PayslipFormData calculateWage(PayslipFormData fd);

}

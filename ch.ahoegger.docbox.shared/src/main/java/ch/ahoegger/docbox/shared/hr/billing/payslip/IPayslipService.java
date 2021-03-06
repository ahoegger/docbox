package ch.ahoegger.docbox.shared.hr.billing.payslip;

import java.math.BigDecimal;

import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

import ch.ahoegger.docbox.shared.util.FormDataResult;

/**
 * <h3>{@link IPayslipService}</h3>
 *
 * @author aho
 */
@TunnelToServer
public interface IPayslipService extends IService {

  /**
   * @param searchData
   * @return
   */
  PayslipSearchFormData loadSearch(PayslipSearchFormData searchData);

  /**
   * @param formData
   * @return
   */
  PayslipTableData getTableData(PayslipSearchFormData formData);

  /**
   * @param searchData
   * @return
   */
  boolean hasTableData(PayslipSearchFormData searchData);

  /**
   * @param formData
   * @return
   */
  PayslipFormData prepareCreate(PayslipFormData formData);

  /**
   * @param formData
   * @return
   */
  PayslipFormData create(PayslipFormData formData);

  /**
   * @param formData
   * @return
   */
  PayslipFormData load(PayslipFormData formData);

  /**
   * @param formData
   * @return
   */
  PayslipFormData finalize(PayslipFormData formData);

  /**
   * @param formData
   * @return
   */
  PayslipFormData unfinalize(BigDecimal payslipId);

  boolean delete(BigDecimal payslipId);

  /**
   * @param payslipSearchFormData
   * @return
   */
  boolean finalized(PayslipSearchFormData payslipSearchFormData);

  /**
   * @param payslipId
   * @return
   */
  FormDataResult<PayslipFormData, Boolean> isFinalized(BigDecimal payslipId);

}

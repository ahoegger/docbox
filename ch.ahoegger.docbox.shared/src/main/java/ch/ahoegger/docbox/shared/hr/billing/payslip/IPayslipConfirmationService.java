package ch.ahoegger.docbox.shared.hr.billing.payslip;

import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

/**
 * <h3>{@link IPayslipConfirmationService}</h3>
 *
 * @author aho
 */
@TunnelToServer
public interface IPayslipConfirmationService extends IService {

  /**
   * @param formData
   * @return
   */
  PayslipConfirmationFormData load(PayslipConfirmationFormData formData);

}

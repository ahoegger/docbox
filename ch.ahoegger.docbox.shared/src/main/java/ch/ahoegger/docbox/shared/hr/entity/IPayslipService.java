package ch.ahoegger.docbox.shared.hr.entity;

import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

import ch.ahoegger.docbox.shared.document.DocumentFormData;

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
  DocumentFormData create(PayslipFormData formData);
}

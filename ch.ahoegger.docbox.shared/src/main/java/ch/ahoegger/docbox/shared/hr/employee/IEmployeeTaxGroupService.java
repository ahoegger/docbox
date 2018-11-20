package ch.ahoegger.docbox.shared.hr.employee;

import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

import ch.ahoegger.docbox.shared.hr.tax.EmployeeTaxGroupFormData;

/**
 * <h3>{@link IEmployeeTaxGroupService}</h3>
 *
 * @author aho
 */
@TunnelToServer
public interface IEmployeeTaxGroupService extends IService {

  /**
   * @param formData
   * @return
   */
  EmployeeTaxGroupFormData create(EmployeeTaxGroupFormData formData);

}

package ch.ahoegger.docbox.shared.hr.billing.taxgroup;

import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.platform.status.IStatus;
import org.eclipse.scout.rt.shared.TunnelToServer;

import ch.ahoegger.docbox.shared.hr.employee.EmployeeTaxGroupFormData;
import ch.ahoegger.docbox.shared.hr.employee.EmployeeTaxGroupSearchFormData;
import ch.ahoegger.docbox.shared.hr.employee.EmployeeTaxGroupTableData;

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
  EmployeeTaxGroupTableData getTableData(EmployeeTaxGroupSearchFormData formData);

  /**
   * @param searchData
   * @return
   */
  boolean hasTableData(EmployeeTaxGroupSearchFormData searchData);

  /**
   * @param formData
   * @return
   */
  IStatus validate(EmployeeTaxGroupFormData formData);

  /**
   * @param formData
   * @return
   */
  EmployeeTaxGroupFormData prepareCreate(EmployeeTaxGroupFormData formData);

  /**
   * @param formData
   * @return
   */
  EmployeeTaxGroupFormData create(EmployeeTaxGroupFormData formData);

  /**
   * @param formData
   * @return
   */
  EmployeeTaxGroupFormData load(EmployeeTaxGroupFormData formData);

  /**
   * @param formData
   * @return
   */
  EmployeeTaxGroupFormData store(EmployeeTaxGroupFormData formData);

  /**
   * @param formData
   * @return
   */
  EmployeeTaxGroupFormData finalize(EmployeeTaxGroupFormData formData);

}

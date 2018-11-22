package ch.ahoegger.docbox.shared.hr.employee;

import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

/**
 * <h3>{@link IEmployeeService}</h3>
 *
 * @author Andreas Hoegger
 */
@TunnelToServer
public interface IEmployeeService extends IService {

  /**
   * @param formData
   * @return
   */
  EmployeeTableData getTableData(EmployeeSearchFormData formData);

  /**
   * @param formData
   * @return
   */
  EmployeeFormData prepareCreate(EmployeeFormData formData);

  /**
   * @param formData
   * @return
   */
  EmployeeFormData create(EmployeeFormData formData);

  /**
   * @param formData
   * @return
   */
  EmployeeFormData load(EmployeeFormData formData);

  /**
   * @param formData
   * @return
   */
  EmployeeFormData store(EmployeeFormData formData);
}

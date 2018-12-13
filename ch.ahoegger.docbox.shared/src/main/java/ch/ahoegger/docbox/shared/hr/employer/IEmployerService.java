package ch.ahoegger.docbox.shared.hr.employer;

import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

/**
 * <h3>{@link IEmployerService}</h3>
 *
 * @author aho
 */
@TunnelToServer
public interface IEmployerService extends IService {

  /**
   * @param formData
   * @return
   */
  EmployerTablePageData getTableData(EmployerSearchFormData formData);

  /**
   * @param formData
   * @return
   */
  EmployerFormData prepareCreate(EmployerFormData formData);

  /**
   * @param formData
   * @return
   */
  EmployerFormData create(EmployerFormData formData);

  /**
   * @param formData
   * @return
   */
  EmployerFormData load(EmployerFormData formData);

  /**
   * @param formData
   * @return
   */
  EmployerFormData store(EmployerFormData formData);

}

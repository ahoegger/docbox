package ch.ahoegger.docbox.shared.administration.user;

import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

/**
 * <h3>{@link IUserService}</h3>
 *
 * @author Andreas Hoegger
 */
@TunnelToServer
public interface IUserService extends IService {

  /**
   * @return
   */
  UserTablePageData getUserTableData();

  /**
   * @param formData
   * @return
   */
  UserFormData load(UserFormData formData);

  /**
   * @param formData
   * @return
   */
  UserFormData prepareCreate(UserFormData formData);

  /**
   * @param formData
   * @return
   */
  UserFormData create(UserFormData formData);

  /**
   * @param string
   * @return
   */
  boolean delete(String string);

  /**
   * @param formData
   * @return
   */
  UserFormData store(UserFormData formData);

}

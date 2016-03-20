package ch.ahoegger.docbox.shared.administration.user;

import org.eclipse.scout.rt.platform.ApplicationScoped;
import org.eclipse.scout.rt.shared.TunnelToServer;

/**
 * <h3>{@link IUserService}</h3>
 *
 * @author Andreas Hoegger
 */
@ApplicationScoped
@TunnelToServer
public interface IUserService {

  /**
   * @return
   */
  UserTablePageData getUserTableData();

  /**
   * @param username
   * @param passwordPlainText
   * @return
   */
  boolean authenticate(String username, char[] passwordPlainText);

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
   */
  void delete(String string);

}

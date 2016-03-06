package ch.ahoegger.docbox.shared.administration.user;

import org.eclipse.scout.rt.platform.ApplicationScoped;
import org.eclipse.scout.rt.shared.TunnelToServer;

/**
 * <h3>{@link IUserService}</h3>
 *
 * @author aho
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

}

package ch.ahoegger.docbox.shared.administration.user;

import org.eclipse.scout.rt.platform.util.TriState;
import org.eclipse.scout.rt.shared.services.lookup.ILookupService;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;

/**
 * <h3>{@link UserLookupCall}</h3>
 *
 * @author Andreas Hoegger
 */
public class UserLookupCall extends LookupCall<String> {

  private static final long serialVersionUID = 1L;

  @Override
  public void setActive(TriState s) {
    super.setActive(s);
  }

  @Override
  protected Class<? extends ILookupService<String>> getConfiguredService() {
    return IUserLookupService.class;
  }
}

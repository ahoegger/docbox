package ch.ahoegger.docbox.shared.administration.user;

import org.eclipse.scout.rt.shared.services.lookup.ILookupService;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;

/**
 * <h3>{@link UserLookupCall}</h3>
 *
 * @author aho
 */
public class UserLookupCall extends LookupCall<String> {

  @Override
  protected Class<? extends ILookupService<String>> getConfiguredService() {
    return IUserLookupService.class;
  }
}

package ch.ahoegger.docbox.shared.administration.user;

import org.eclipse.scout.rt.shared.services.lookup.ILookupService;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;

/**
 * <h3>{@link AdministratorLookupCall}</h3>
 *
 * @author Andreas Hoegger
 */
public class AdministratorLookupCall extends LookupCall<String> {

  private static final long serialVersionUID = 1L;

  @Override
  protected Class<? extends ILookupService<String>> getConfiguredService() {
    return IAdministratorLookupService.class;
  }
}

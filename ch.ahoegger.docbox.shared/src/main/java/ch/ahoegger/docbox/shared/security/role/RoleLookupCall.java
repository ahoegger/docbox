package ch.ahoegger.docbox.shared.security.role;

import java.math.BigDecimal;

import org.eclipse.scout.rt.shared.services.lookup.ILookupService;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;

/**
 * <h3>{@link RoleLookupCall}</h3>
 *
 * @author Andreas Hoegger
 */
public class RoleLookupCall extends LookupCall<BigDecimal> {

  private static final long serialVersionUID = 1L;

  @Override
  protected Class<? extends ILookupService<BigDecimal>> getConfiguredService() {
    return IRoleLookupService.class;
  }
}

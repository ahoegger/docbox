package ch.ahoegger.docbox.shared.partner;

import java.math.BigDecimal;

import org.eclipse.scout.rt.shared.services.lookup.ILookupService;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;

/**
 * <h3>{@link PatnerLookupCall}</h3>
 *
 * @author aho
 */
public class PatnerLookupCall extends LookupCall<BigDecimal> {

  private static final long serialVersionUID = 1L;

  @Override
  protected Class<? extends ILookupService<BigDecimal>> getConfiguredService() {
    return IParterLookupService.class;
  }
}

package ch.ahoegger.docbox.shared.hr.employer;

import java.math.BigDecimal;

import org.eclipse.scout.rt.shared.services.lookup.ILookupService;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;

/**
 * <h3>{@link EmployerLookupCall}</h3>
 *
 * @author Andreas Hoegger
 */
public class EmployerLookupCall extends LookupCall<BigDecimal> {

  private static final long serialVersionUID = 1L;

  @Override
  protected Class<? extends ILookupService<BigDecimal>> getConfiguredService() {
    return IEmployerLookupService.class;
  }
}

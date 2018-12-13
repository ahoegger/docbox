package ch.ahoegger.docbox.shared.administration.hr.billing;

import java.math.BigDecimal;

import org.eclipse.scout.rt.shared.TunnelToServer;
import org.eclipse.scout.rt.shared.services.lookup.ILookupService;

/**
 * <h3>{@link IBillingCycleLookupService}</h3>
 *
 * @author Andreas Hoegger
 */
@TunnelToServer
public interface IBillingCycleLookupService extends ILookupService<BigDecimal> {

}

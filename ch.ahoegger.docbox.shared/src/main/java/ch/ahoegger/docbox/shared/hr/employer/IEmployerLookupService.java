package ch.ahoegger.docbox.shared.hr.employer;

import java.math.BigDecimal;

import org.eclipse.scout.rt.shared.TunnelToServer;
import org.eclipse.scout.rt.shared.services.lookup.ILookupService;

/**
 * <h3>{@link IEmployerLookupService}</h3>
 *
 * @author Andreas Hoegger
 */
@TunnelToServer
public interface IEmployerLookupService extends ILookupService<BigDecimal> {

}

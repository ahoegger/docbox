package ch.ahoegger.docbox.shared.hr.employee;

import java.math.BigDecimal;

import org.eclipse.scout.rt.shared.TunnelToServer;
import org.eclipse.scout.rt.shared.services.lookup.ILookupService;

/**
 * <h3>{@link IEmployeeLookupService}</h3>
 *
 * @author Andreas Hoegger
 */
@TunnelToServer
public interface IEmployeeLookupService extends ILookupService<BigDecimal> {

}

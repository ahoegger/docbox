package ch.ahoegger.docbox.shared.hr.tax;

import java.math.BigDecimal;

import org.eclipse.scout.rt.shared.TunnelToServer;
import org.eclipse.scout.rt.shared.services.lookup.ILookupService;

/**
 * <h3>{@link ITaxGroupLookupService}</h3>
 *
 * @author Andreas Hoegger
 */
@TunnelToServer
public interface ITaxGroupLookupService extends ILookupService<BigDecimal> {

}

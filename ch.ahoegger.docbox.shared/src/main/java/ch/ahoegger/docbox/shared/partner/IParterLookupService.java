package ch.ahoegger.docbox.shared.partner;

import java.math.BigDecimal;

import org.eclipse.scout.rt.shared.TunnelToServer;
import org.eclipse.scout.rt.shared.services.lookup.ILookupService;

/**
 * <h3>{@link IParterLookupService}</h3>
 *
 * @author aho
 */
@TunnelToServer
public interface IParterLookupService extends ILookupService<BigDecimal> {

}

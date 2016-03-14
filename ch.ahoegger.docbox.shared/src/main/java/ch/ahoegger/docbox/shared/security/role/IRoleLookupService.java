package ch.ahoegger.docbox.shared.security.role;

import java.math.BigDecimal;

import org.eclipse.scout.rt.shared.TunnelToServer;
import org.eclipse.scout.rt.shared.services.lookup.ILookupService;

/**
 * <h3>{@link IRoleLookupService}</h3>
 *
 * @author aho
 */
@TunnelToServer
public interface IRoleLookupService extends ILookupService<BigDecimal> {

}

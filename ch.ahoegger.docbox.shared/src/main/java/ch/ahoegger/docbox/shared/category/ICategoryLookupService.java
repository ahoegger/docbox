package ch.ahoegger.docbox.shared.category;

import java.math.BigDecimal;

import org.eclipse.scout.rt.shared.TunnelToServer;
import org.eclipse.scout.rt.shared.services.lookup.ILookupService;

/**
 * <h3>{@link ICategoryLookupService}</h3>
 *
 * @author aho
 */
@TunnelToServer
public interface ICategoryLookupService extends ILookupService<BigDecimal> {

}

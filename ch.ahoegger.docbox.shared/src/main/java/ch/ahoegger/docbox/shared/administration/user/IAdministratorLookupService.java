package ch.ahoegger.docbox.shared.administration.user;

import org.eclipse.scout.rt.shared.TunnelToServer;
import org.eclipse.scout.rt.shared.services.lookup.ILookupService;

/**
 * <h3>{@link IAdministratorLookupService}</h3>
 *
 * @author Andreas Hoegger
 */
@TunnelToServer
public interface IAdministratorLookupService extends ILookupService<String> {

}

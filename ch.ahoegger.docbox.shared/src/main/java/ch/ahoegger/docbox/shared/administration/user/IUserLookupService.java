package ch.ahoegger.docbox.shared.administration.user;

import org.eclipse.scout.rt.shared.TunnelToServer;
import org.eclipse.scout.rt.shared.services.lookup.ILookupService;

/**
 * <h3>{@link IUserLookupService}</h3>
 *
 * @author aho
 */
@TunnelToServer
public interface IUserLookupService extends ILookupService<String> {

}

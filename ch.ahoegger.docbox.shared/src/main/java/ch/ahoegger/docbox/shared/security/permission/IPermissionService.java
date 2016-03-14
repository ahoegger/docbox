package ch.ahoegger.docbox.shared.security.permission;

import org.eclipse.scout.rt.platform.ApplicationScoped;
import org.eclipse.scout.rt.shared.TunnelToServer;

/**
 * <h3>{@link IPermissionService}</h3>
 *
 * @author aho
 */
@TunnelToServer
@ApplicationScoped
public interface IPermissionService {

  boolean hasReadAccess(String userId, Long entityId);

  boolean hasWriteAccess(Long entityId);
}

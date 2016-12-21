package ch.ahoegger.docbox.shared.security.permission;

import java.math.BigDecimal;

import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

/**
 * <h3>{@link IPermissionService}</h3>
 *
 * @author Andreas Hoegger
 */
@TunnelToServer
public interface IPermissionService extends IService {

  boolean hasReadAccess(String userId, BigDecimal entityId);

  boolean hasWriteAccess(BigDecimal entityId);
}

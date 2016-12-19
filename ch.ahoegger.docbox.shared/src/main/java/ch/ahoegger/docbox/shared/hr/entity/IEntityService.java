package ch.ahoegger.docbox.shared.hr.entity;

import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

/**
 * <h3>{@link IEntityService}</h3>
 *
 * @author Andreas Hoegger
 */
@TunnelToServer
public interface IEntityService extends IService {

  /**
   * @param filter
   * @return
   */
  EntityTablePageData getEntityTableData(EntitySearchFormData filter);
}

package ch.ahoegger.docbox.shared.hr.entity;

import java.math.BigDecimal;

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

  /**
   * @param formData
   * @return
   */
  EntityFormData prepareCreate(EntityFormData formData);

  /**
   * @param formData
   * @return
   */
  EntityFormData create(EntityFormData formData);

  /**
   * @param formData
   * @return
   */
  EntityFormData load(EntityFormData formData);

  /**
   * @param formData
   * @return
   */
  EntityFormData store(EntityFormData formData);

  boolean delete(BigDecimal entityId);
}

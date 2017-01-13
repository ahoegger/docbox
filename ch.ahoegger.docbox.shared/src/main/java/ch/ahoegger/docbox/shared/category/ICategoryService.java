package ch.ahoegger.docbox.shared.category;

import java.math.BigDecimal;

import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

/**
 * <h3>{@link ICategoryService}</h3>
 *
 * @author Andreas Hoegger
 */
@TunnelToServer
public interface ICategoryService extends IService {

  /**
   * @param formData
   * @return
   */
  CategoryTableData getTableData(CategorySearchFormData formData);

  /**
   * @param formData
   * @return
   */
  CategoryFormData prepareCreate(CategoryFormData formData);

  /**
   * @param formData
   * @return
   */
  CategoryFormData create(CategoryFormData formData);

  /**
   * @param categoryId
   */
  boolean delete(BigDecimal categoryId);

  /**
   * @param formData
   * @return
   */
  CategoryFormData load(CategoryFormData formData);

  /**
   * @param formData
   * @return
   */
  CategoryFormData store(CategoryFormData formData);

}

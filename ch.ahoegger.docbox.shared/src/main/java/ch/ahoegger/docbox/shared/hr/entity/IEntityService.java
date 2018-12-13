package ch.ahoegger.docbox.shared.hr.entity;

import java.math.BigDecimal;
import java.util.function.Predicate;

import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.platform.status.IStatus;
import org.eclipse.scout.rt.shared.TunnelToServer;

import ch.ahoegger.docbox.shared.hr.entity.EntityTablePageData.EntityTableRowData;
import ch.ahoegger.docbox.shared.hr.entity.EntityTypeCodeType.ExpenseCode;
import ch.ahoegger.docbox.shared.hr.entity.EntityTypeCodeType.WorkCode;

/**
 * <h3>{@link IEntityService}</h3>
 *
 * @author Andreas Hoegger
 */
@TunnelToServer
public interface IEntityService extends IService {

  Predicate<? super EntityTableRowData> EXPENSE_FILTER = e -> ExpenseCode.isEqual(e.getEntityType());
  Predicate<? super EntityTableRowData> WORK_FILTER = e -> WorkCode.isEqual(e.getEntityType());

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
  IStatus validate(EntityFormData formData);

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

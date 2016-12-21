package ch.ahoegger.docbox.shared.hr.billing;

import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

/**
 * <h3>{@link IPostingGroupService}</h3>
 *
 * @author Andreas Hoegger
 */
@TunnelToServer
public interface IPostingGroupService extends IService {

  /**
   * @param formData
   * @return
   */
  PostingGroupTableData getTableData(PostingGroupSearchFormData formData);

  /**
   * @param formData
   * @return
   */
  PostingGroupFormData prepareCreate(PostingGroupFormData formData);

  /**
   * @param formData
   */
  PostingGroupFormData create(PostingGroupFormData formData);

  /**
   * @param formData
   * @return
   */
  PostingCalculationBoxData calculateWage(PostingGroupFormData formData);

}

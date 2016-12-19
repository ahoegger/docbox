package ch.ahoegger.docbox.shared.conversation;

import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

/**
 * <h3>{@link IConversationService}</h3>
 *
 * @author Andreas Hoegger
 */
@TunnelToServer
public interface IConversationService extends IService {

  /**
   * @param formData
   */
  ConversationTableData getTableData(ConversationSearchFormData formData);

  /**
   * @param formData
   * @return
   */
  ConversationFormData prepareCreate(ConversationFormData formData);

  /**
   * @param formData
   * @return
   */
  ConversationFormData create(ConversationFormData formData);

  /**
   * @param selectedValue
   */
  void delete(Long selectedValue);

  /**
   * @param formData
   * @return
   */
  ConversationFormData load(ConversationFormData formData);

  /**
   * @param formData
   * @return
   */
  ConversationFormData store(ConversationFormData formData);

}

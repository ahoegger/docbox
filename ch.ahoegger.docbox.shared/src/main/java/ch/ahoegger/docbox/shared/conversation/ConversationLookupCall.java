package ch.ahoegger.docbox.shared.conversation;

import java.math.BigDecimal;

import org.eclipse.scout.rt.shared.services.lookup.ILookupService;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;

/**
 * <h3>{@link ConversationLookupCall}</h3>
 *
 * @author Andreas Hoegger
 */
public class ConversationLookupCall extends LookupCall<BigDecimal> {

  private static final long serialVersionUID = 1L;
  private boolean m_noMasterShowAll = false;

  @Override
  protected Class<? extends ILookupService<BigDecimal>> getConfiguredService() {
    return IConversationLookupService.class;
  }

  public void setNoMasterShowAll(boolean noMasterShowAll) {
    m_noMasterShowAll = noMasterShowAll;
  }

  public boolean isNoMasterShowAll() {
    return m_noMasterShowAll;
  }
}

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

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    if (obj.getClass() != this.getClass()) {
      return false;
    }
    ConversationLookupCall other = (ConversationLookupCall) obj;
    if (m_noMasterShowAll != other.m_noMasterShowAll) {
      return false;
    }
    return super.equals(obj);
  }

}

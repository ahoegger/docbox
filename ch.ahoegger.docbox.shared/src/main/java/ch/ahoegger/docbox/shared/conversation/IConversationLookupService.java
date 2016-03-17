package ch.ahoegger.docbox.shared.conversation;

import java.math.BigDecimal;

import org.eclipse.scout.rt.shared.TunnelToServer;
import org.eclipse.scout.rt.shared.services.lookup.ILookupService;

/**
 * <h3>{@link IConversationLookupService}</h3>
 *
 * @author Andreas Hoegger
 */
@TunnelToServer
public interface IConversationLookupService extends ILookupService<BigDecimal> {

}

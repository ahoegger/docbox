package ch.ahoegger.docbox.server.partner;

import java.math.BigDecimal;
import java.util.Optional;

import org.eclipse.scout.rt.platform.ApplicationScoped;
import org.eclipse.scout.rt.platform.holders.NVPair;
import org.eclipse.scout.rt.server.jdbc.SQL;
import org.eclipse.scout.rt.shared.servicetunnel.RemoteServiceAccessDenied;

import ch.ahoegger.docbox.server.database.SqlFramentBuilder;
import ch.ahoegger.docbox.shared.partner.IPartnerConversationTable;

/**
 * <h3>{@link PartnerConversationService}</h3>
 *
 * @author Andreas Hoegger
 */
@ApplicationScoped
public class PartnerConversationService implements IPartnerConversationTable {

  public void delete(Long conversationId) {
    if (conversationId != null) {
      StringBuilder statementBuilder = new StringBuilder();
      statementBuilder.append("DELETE FROM ").append(TABLE_NAME).append(" WHERE ").append(CONVERSATION_NR).append(" = :conversationId");
      SQL.delete(statementBuilder.toString(), new NVPair("conversationId", conversationId));
    }
  }

  /**
   * @param partnerId
   * @param conversationId
   */
  @RemoteServiceAccessDenied
  public void create(BigDecimal partnerId, BigDecimal conversationId) {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("INSERT INTO ").append(TABLE_NAME).append(" (");
    statementBuilder.append(SqlFramentBuilder.columns(PARTNER_NR, CONVERSATION_NR)).append(" ) VALUES (");
    statementBuilder.append(":partnerId, conversationId");
    statementBuilder.append(")");
    SQL.insert(statementBuilder.toString(),
        new NVPair("partnerId", partnerId),
        new NVPair("conversationId", conversationId));
  }

  /**
   * @param value
   * @param conversationId
   */
  public void update(BigDecimal partnerId, BigDecimal conversationId) {
    if (partnerId == null) {
      delete(Optional.of(conversationId).map(id -> id.longValue()).orElse(null));
    }
    else {
      StringBuilder statementBuilder = new StringBuilder();
      statementBuilder.append("UPDATE ").append(TABLE_NAME).append(" SET ");
      statementBuilder.append(PARTNER_NR).append("= :partnerId");
      statementBuilder.append(" WHERE ").append(CONVERSATION_NR).append(" = :conversationId");
      SQL.update(statementBuilder.toString(),
          new NVPair("partnerId", partnerId),
          new NVPair("conversationId", conversationId));
    }
  }
}

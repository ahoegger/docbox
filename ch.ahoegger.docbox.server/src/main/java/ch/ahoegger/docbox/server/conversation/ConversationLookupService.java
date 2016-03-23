package ch.ahoegger.docbox.server.conversation;

import java.math.BigDecimal;

import org.eclipse.scout.rt.server.jdbc.lookup.AbstractSqlLookupService;

import ch.ahoegger.docbox.server.database.SqlFramentBuilder;
import ch.ahoegger.docbox.shared.conversation.IConversationLookupService;
import ch.ahoegger.docbox.shared.conversation.IConversationTable;

/**
 * <h3>{@link ConversationLookupService}</h3>
 *
 * @author Andreas Hoegger
 */
public class ConversationLookupService extends AbstractSqlLookupService<BigDecimal> implements IConversationLookupService, IConversationTable {

  @Override
  protected String getConfiguredSqlSelect() {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("SELECT ").append(SqlFramentBuilder.columns(CONVERSATION_NR, NAME));
    statementBuilder.append(" FROM ").append(TABLE_NAME);
    statementBuilder.append(" ").append(SqlFramentBuilder.WHERE_DEFAULT).append(" ");
    statementBuilder.append("<key>").append("AND ").append(CONVERSATION_NR).append(" = :key").append("</key>");
    statementBuilder.append("<text>").append("AND UPPER(").append(NAME).append(") LIKE UPPER(:text||'%')").append("</text>");
    return statementBuilder.toString();
  }

}

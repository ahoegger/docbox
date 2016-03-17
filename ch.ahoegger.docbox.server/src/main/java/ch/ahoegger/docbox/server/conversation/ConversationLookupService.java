package ch.ahoegger.docbox.server.conversation;

import java.math.BigDecimal;
import java.util.List;

import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.server.jdbc.lookup.AbstractSqlLookupService;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;
import org.eclipse.scout.rt.shared.services.lookup.ILookupRow;

import ch.ahoegger.docbox.server.database.SqlFramentBuilder;
import ch.ahoegger.docbox.shared.conversation.IConversationLookupService;
import ch.ahoegger.docbox.shared.conversation.IConversationTable;
import ch.ahoegger.docbox.shared.partner.IPartnerConversationTable;

/**
 * <h3>{@link ConversationLookupService}</h3>
 *
 * @author Andreas Hoegger
 */
public class ConversationLookupService extends AbstractSqlLookupService<BigDecimal> implements IConversationLookupService, IConversationTable {

  @Override
  protected String getConfiguredSqlSelect() {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("SELECT ").append(SqlFramentBuilder.columnsAliased(TABLE_ALIAS, CONVERSATION_NR, NAME));
    statementBuilder.append(" FROM ").append(TABLE_NAME).append(" AS ").append(TABLE_ALIAS);
    statementBuilder.append(" ").append(SqlFramentBuilder.WHERE_DEFAULT).append(" ");
    // all categories with no partner link

    statementBuilder.append("<key>").append("AND ").append(SqlFramentBuilder.columnsAliased(TABLE_ALIAS, CONVERSATION_NR)).append(" = :key").append("</key>");
    statementBuilder.append("<text>").append("AND UPPER(").append(SqlFramentBuilder.columnsAliased(TABLE_ALIAS, NAME)).append(") LIKE UPPER(:text||'%')").append("</text>");
    statementBuilder.append(" AND NOT EXISTS (SELECT 1 FROM ").append(IPartnerConversationTable.TABLE_NAME).append(" AS ").append(IPartnerConversationTable.TABLE_ALIAS);
    statementBuilder.append(" WHERE ").append(SqlFramentBuilder.columnsAliased(TABLE_ALIAS, CONVERSATION_NR)).append(" = ")
        .append(SqlFramentBuilder.columnsAliased(IPartnerConversationTable.TABLE_ALIAS, IPartnerConversationTable.CONVERSATION_NR)).append(") ");
    return statementBuilder.toString();
  }

  @Override
  protected List<ILookupRow<BigDecimal>> execLoadLookupRows(String originalSql, String preprocessedSql, ILookupCall<BigDecimal> call) {
    StringBuilder statementBuilder = new StringBuilder(preprocessedSql);
    List<BigDecimal> partnerIds = (List<BigDecimal>) call.getMaster();
    if (CollectionUtility.hasElements(partnerIds)) {
      statementBuilder.append(" OR ").append(SqlFramentBuilder.columnsAliased(TABLE_ALIAS, CONVERSATION_NR)).append(" IN (")
          .append("SELECT ").append(SqlFramentBuilder.columnsAliased(IPartnerConversationTable.TABLE_ALIAS, IPartnerConversationTable.CONVERSATION_NR))
          .append(" FROM ").append(IPartnerConversationTable.TABLE_NAME).append(" AS ").append(IPartnerConversationTable.TABLE_ALIAS)
          .append(" WHERE ").append(SqlFramentBuilder.columnsAliased(TABLE_ALIAS, CONVERSATION_NR)).append(" = ")
          .append(SqlFramentBuilder.columnsAliased(IPartnerConversationTable.TABLE_ALIAS, IPartnerConversationTable.CONVERSATION_NR))
          .append(" AND ").append(SqlFramentBuilder.columnsAliased(IPartnerConversationTable.TABLE_ALIAS, IPartnerConversationTable.PARTNER_NR)).append(" IN ( ")
          .append(partnerIds.stream().map(id -> id.toPlainString()).reduce((id1, id2) -> id1 + ", " + id2).get())
          .append(") ) ");
    }

    return super.execLoadLookupRows(originalSql, statementBuilder.toString(), call);

  }
}

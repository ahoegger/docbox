package ch.ahoegger.docbox.server.conversation;

import java.math.BigDecimal;
import java.util.Date;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.holders.NVPair;
import org.eclipse.scout.rt.server.jdbc.SQL;

import ch.ahoegger.docbox.server.database.SqlFramentBuilder;
import ch.ahoegger.docbox.server.partner.PartnerConversationService;
import ch.ahoegger.docbox.shared.ISequenceTable;
import ch.ahoegger.docbox.shared.conversation.ConversationFormData;
import ch.ahoegger.docbox.shared.conversation.IConversationService;
import ch.ahoegger.docbox.shared.conversation.IConversationTable;
import ch.ahoegger.docbox.shared.partner.IPartnerConversationTable;
import ch.ahoegger.docbox.shared.partner.IPartnerTable;

/**
 * <h3>{@link ConversationService}</h3>
 *
 * @author Andreas Hoegger
 */
public class ConversationService implements IConversationService, IConversationTable {

  @Override
  public ConversationFormData prepareCreate(ConversationFormData formData) {
    formData.getStartDate().setValue(new Date());
    return formData;
  }

  @Override
  public ConversationFormData create(ConversationFormData formData) {
    formData.setConversationId(new BigDecimal(SQL.getSequenceNextval(ISequenceTable.TABLE_NAME)));
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("INSERT INTO ").append(TABLE_NAME);
    statementBuilder.append(" (").append(SqlFramentBuilder.columns(CONVERSATION_NR, NAME, DESCRIPTION, START_DATE, END_DATE)).append(")");
    statementBuilder.append(" VALUES (:conversationId, :name, :description, :startDate, :endDate )");
    SQL.insert(statementBuilder.toString(), formData);

    // partner conversation link
    BigDecimal partnerId = formData.getPartner().getValue();
    if (partnerId != null) {
      BEANS.get(PartnerConversationService.class).create(partnerId, formData.getConversationId());
    }
    return formData;
  }

  @Override
  public ConversationFormData load(ConversationFormData formData) {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("SELECT ").append(SqlFramentBuilder.columns(SqlFramentBuilder.columnsAliased(TABLE_ALIAS, NAME, DESCRIPTION, START_DATE, END_DATE)));
    statementBuilder.append(", ").append(SqlFramentBuilder.columnsAliased(IPartnerTable.TABLE_ALIAS, IPartnerTable.NAME));
    statementBuilder.append(" FROM ").append(TABLE_NAME).append(" AS ").append(TABLE_ALIAS);
    statementBuilder.append(", ").append(IPartnerConversationTable.TABLE_NAME).append(" AS ").append(IPartnerConversationTable.TABLE_ALIAS);
    statementBuilder.append(", ").append(IPartnerTable.TABLE_NAME).append(" AS ").append(IPartnerTable.TABLE_ALIAS);
    // joins
    statementBuilder.append(" WHERE ").append(SqlFramentBuilder.columnsAliased(TABLE_ALIAS, CONVERSATION_NR)).append(" = ")
        .append(SqlFramentBuilder.columnsAliased(IPartnerConversationTable.TABLE_ALIAS, IPartnerConversationTable.CONVERSATION_NR));
    statementBuilder.append(" AND ").append(SqlFramentBuilder.columnsAliased(IPartnerConversationTable.TABLE_ALIAS, IPartnerConversationTable.PARTNER_NR)).append(" = ")
        .append(SqlFramentBuilder.columnsAliased(IPartnerTable.TABLE_ALIAS, IPartnerTable.PARTNER_NR));
    // criteria
    statementBuilder.append(" AND ").append(SqlFramentBuilder.columns(SqlFramentBuilder.columnsAliased(TABLE_ALIAS, CONVERSATION_NR))).append(" = :conversationId");
    statementBuilder.append(" INTO :name, :description, :partner, :startDate, :endDate");
    SQL.selectInto(statementBuilder.toString(), formData);
    return formData;
  }

  @Override
  public ConversationFormData store(ConversationFormData formData) {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("UPDATE ").append(TABLE_NAME).append(" SET ");
    statementBuilder.append(NAME).append("= :name, ");
    statementBuilder.append(DESCRIPTION).append("= :description, ");
    statementBuilder.append(START_DATE).append("= :startDate, ");
    statementBuilder.append(END_DATE).append("= :endDate ");
    statementBuilder.append(" WHERE ").append(CONVERSATION_NR).append(" = :conversationId");
    SQL.update(statementBuilder.toString(), formData);

    // partner conversation link
    BEANS.get(PartnerConversationService.class).update(formData.getPartner().getValue(), formData.getConversationId());

    return formData;
  }

  @Override
  public void delete(Long conversationId) {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("DELETE FROM ").append(TABLE_NAME).append(" WHERE ").append(CONVERSATION_NR).append(" = :conversationId");
    SQL.delete(statementBuilder.toString(), new NVPair("conversationId", conversationId));

    // delete partner conversation connection
    BEANS.get(PartnerConversationService.class).delete(conversationId);
  }
}

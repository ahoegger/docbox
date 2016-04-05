package ch.ahoegger.docbox.server.conversation;

import java.math.BigDecimal;
import java.util.Date;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.holders.BooleanHolder;
import org.eclipse.scout.rt.platform.holders.NVPair;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.server.jdbc.SQL;

import ch.ahoegger.docbox.shared.ISequenceTable;
import ch.ahoegger.docbox.shared.backup.IBackupService;
import ch.ahoegger.docbox.shared.conversation.ConversationFormData;
import ch.ahoegger.docbox.shared.conversation.ConversationSearchFormData;
import ch.ahoegger.docbox.shared.conversation.ConversationTableData;
import ch.ahoegger.docbox.shared.conversation.IConversationService;
import ch.ahoegger.docbox.shared.conversation.IConversationTable;
import ch.ahoegger.docbox.shared.util.SqlFramentBuilder;

/**
 * <h3>{@link ConversationService}</h3>
 *
 * @author Andreas Hoegger
 */
public class ConversationService implements IConversationService, IConversationTable {

  @Override
  public ConversationTableData getTableData(ConversationSearchFormData formData) {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("SELECT ").append(SqlFramentBuilder.columnsAliased(TABLE_ALIAS, CONVERSATION_NR, NAME, START_DATE, END_DATE))
        .append(" FROM ").append(TABLE_NAME).append(" AS ").append(TABLE_ALIAS).append(" ")
        .append(SqlFramentBuilder.WHERE_DEFAULT);

    // search criteria name
    if (StringUtility.hasText(formData.getName().getValue())) {
      statementBuilder.append(" AND ").append(SqlFramentBuilder.whereStringContains(NAME, formData.getName().getValue()));
    }
    // seach criteria active
    if (formData.getActiveBox().getValue() != null) {
      switch (formData.getActiveBox().getValue()) {
        case TRUE:
          statementBuilder.append(" AND (").append(SqlFramentBuilder.columnsAliased(TABLE_ALIAS, END_DATE)).append(" >= ").append("CURRENT_DATE")
              .append(" OR ").append(SqlFramentBuilder.columnsAliased(TABLE_ALIAS, END_DATE)).append(" IS NULL)");
          break;
        case FALSE:
          statementBuilder.append(" AND ").append(SqlFramentBuilder.columnsAliased(TABLE_ALIAS, END_DATE)).append(" < ").append("CURRENT_DATE");
          break;
      }
    }

    statementBuilder.append(" INTO ")
        .append(":{td.conversationId}, ")
        .append(":{td.name}, ")
        .append(":{td.startDate}, ")
        .append(":{td.endDate} ");

    ConversationTableData tableData = new ConversationTableData();
    SQL.selectInto(statementBuilder.toString(),
        new NVPair("td", tableData),
        formData);
    return tableData;
  }

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
    statementBuilder.append(" (").append(SqlFramentBuilder.columns(CONVERSATION_NR, NAME, NOTES, START_DATE, END_DATE)).append(")");
    statementBuilder.append(" VALUES ( :conversationId, :name, :notes, :startDate, :endDate )");
    SQL.insert(statementBuilder.toString(),
        formData);

    // notify backup needed
    BEANS.get(IBackupService.class).notifyModification();

    return formData;
  }

  @Override
  public ConversationFormData load(ConversationFormData formData) {
    BooleanHolder exists = new BooleanHolder();
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("SELECT TRUE, ").append(SqlFramentBuilder.columns(SqlFramentBuilder.columnsAliased(TABLE_ALIAS, NAME, NOTES, START_DATE, END_DATE)))
        .append(" FROM ").append(TABLE_NAME).append(" AS ").append(TABLE_ALIAS)
        // criteria
        .append(" ").append(SqlFramentBuilder.WHERE_DEFAULT)
        .append(" AND ").append(SqlFramentBuilder.columns(SqlFramentBuilder.columnsAliased(TABLE_ALIAS, CONVERSATION_NR))).append(" = :conversationId")
        .append(" INTO :exists, :name, :notes,  :startDate, :endDate");

    SQL.selectInto(statementBuilder.toString(),
        new NVPair("exists", exists),
        formData);
    if (exists.getValue() == null) {
      return null;
    }

    return formData;
  }

  @Override
  public ConversationFormData store(ConversationFormData formData) {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("UPDATE ").append(TABLE_NAME).append(" SET ");
    statementBuilder.append(NAME).append("= :name, ");
    statementBuilder.append(NOTES).append("= :notes, ");
    statementBuilder.append(START_DATE).append("= :startDate, ");
    statementBuilder.append(END_DATE).append("= :endDate ");
    statementBuilder.append(" WHERE ").append(CONVERSATION_NR).append(" = :conversationId");
    SQL.update(statementBuilder.toString(), formData);

    // notify backup needed
    BEANS.get(IBackupService.class).notifyModification();

    return formData;
  }

  @Override
  public void delete(Long conversationId) {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("DELETE FROM ").append(TABLE_NAME).append(" WHERE ").append(CONVERSATION_NR).append(" = :conversationId");

    SQL.delete(statementBuilder.toString(),
        new NVPair("conversationId", conversationId));

    // notify backup needed
    BEANS.get(IBackupService.class).notifyModification();
  }
}

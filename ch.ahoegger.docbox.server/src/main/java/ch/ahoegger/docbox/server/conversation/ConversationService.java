package ch.ahoegger.docbox.server.conversation;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.ch.ahoegger.docbox.server.or.app.tables.Conversation;
import org.ch.ahoegger.docbox.server.or.app.tables.records.ConversationRecord;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.eclipse.scout.rt.server.jdbc.SQL;
import org.eclipse.scout.rt.shared.servicetunnel.RemoteServiceAccessDenied;
import org.jooq.Condition;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.or.definition.table.ISequenceTable;
import ch.ahoegger.docbox.shared.backup.IBackupService;
import ch.ahoegger.docbox.shared.conversation.ConversationFormData;
import ch.ahoegger.docbox.shared.conversation.ConversationSearchFormData;
import ch.ahoegger.docbox.shared.conversation.ConversationTableData;
import ch.ahoegger.docbox.shared.conversation.ConversationTableData.ConversationTableRowData;
import ch.ahoegger.docbox.shared.conversation.IConversationService;
import ch.ahoegger.docbox.shared.util.LocalDateUtility;

/**
 * <h3>{@link ConversationService}</h3>
 *
 * @author Andreas Hoegger
 */
public class ConversationService implements IConversationService {
  private static final Logger LOG = LoggerFactory.getLogger(ConversationService.class);

  @Override
  public ConversationTableData getTableData(ConversationSearchFormData formData) {

    Conversation c = Conversation.CONVERSATION.as("con");

    Condition condition = DSL.trueCondition();
    // name
    if (StringUtility.hasText(formData.getName().getValue())) {
      condition = condition.and(c.NAME.lower().contains(formData.getName().getValue().toLowerCase()));
    }
    // active
    if (formData.getActiveBox().getValue() != null) {
      switch (formData.getActiveBox().getValue()) {
        case TRUE:
          condition = condition.and(
              c.END_DATE.ge(new Date())
                  .or(c.END_DATE.isNull()));
          break;
        case FALSE:
          condition = condition.and(
              c.END_DATE.lessThan(new Date()));
          break;
      }
    }

    List<ConversationTableRowData> rows = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .select(c.CONVERSATION_NR, c.NAME, c.START_DATE, c.END_DATE)
        .from(c)
        .where(condition)
        .fetch()
        .stream()
        .map(rec -> {
          ConversationTableRowData row = new ConversationTableRowData();
          row.setConversationId(rec.get(c.CONVERSATION_NR));
          row.setName(rec.get(c.NAME));
          row.setStartDate(rec.get(c.START_DATE));
          row.setEndDate(rec.get(c.END_DATE));
          return row;
        })
        .collect(Collectors.toList());

    ConversationTableData tableData = new ConversationTableData();
    tableData.setRows(rows.toArray(new ConversationTableRowData[0]));
    return tableData;
  }

  @Override
  public ConversationFormData prepareCreate(ConversationFormData formData) {
    formData.getStartDate().setValue(LocalDateUtility.today());
    return formData;
  }

  @Override
  public ConversationFormData create(ConversationFormData formData) {
    formData.setConversationId(new BigDecimal(SQL.getSequenceNextval(ISequenceTable.TABLE_NAME)));

    if (DSL.using(SQL.getConnection(), SQLDialect.DERBY).executeInsert(toRecord(formData)) == 1) {
      // notify backup needed
      BEANS.get(IBackupService.class).notifyModification();

      return formData;
    }
    return null;

  }

  @Override
  public ConversationFormData load(ConversationFormData formData) {

    Conversation c = Conversation.CONVERSATION.as("con");
    ConversationRecord rec = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .fetchOne(c, c.CONVERSATION_NR.eq(formData.getConversationId()));
    return toFormData(rec);

  }

  @Override
  public ConversationFormData store(ConversationFormData formData) {

    if (DSL.using(SQL.getConnection(), SQLDialect.DERBY).executeUpdate(toRecord(formData)) == 1) {
      // notify backup needed
      BEANS.get(IBackupService.class).notifyModification();
      return formData;
    }
    return null;
  }

  @Override
  public boolean delete(BigDecimal conversationId) {
    Conversation c = Conversation.CONVERSATION.as("con");
    ConversationRecord rec = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .fetchOne(c, c.CONVERSATION_NR.eq(conversationId));
    if (rec == null) {
      LOG.warn("Try to delete not existing record with id '{}'!", conversationId);
      return false;
    }
    if (rec.delete() != 1) {
      LOG.error("Could not delete record with id '{}'!", conversationId);
      return false;
    }
    BEANS.get(IBackupService.class).notifyModification();
    return true;
  }

  @RemoteServiceAccessDenied
  public int insert(ISqlService sqlService, BigDecimal conversationId, String name, String description,
      Date startDate, Date endDate) {
    return DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .executeInsert(toRecord(conversationId, name, description, startDate, endDate));
  }

  private ConversationRecord toRecord(ConversationFormData formData) {
    return toRecord(formData.getConversationId(), formData.getName().getValue(), formData.getNotes().getValue(), formData.getStartDate().getValue(), formData.getEndDate().getValue());
  }

  private ConversationRecord toRecord(BigDecimal conversationId, String name, String description,
      Date startDate, Date endDate) {
    Conversation t = Conversation.CONVERSATION;
    return new ConversationRecord()
        .with(t.CONVERSATION_NR, conversationId)
        .with(t.END_DATE, endDate)
        .with(t.NAME, name)
        .with(t.NOTES, description)
        .with(t.START_DATE, startDate);

  }

  /**
   * @param rec
   * @return
   */
  private ConversationFormData toFormData(ConversationRecord rec) {
    if (rec == null) {
      return null;
    }
    ConversationFormData fd = new ConversationFormData();
    fd.setConversationId(rec.getConversationNr());
    fd.getName().setValue(rec.getName());
    fd.getNotes().setValue(rec.getNotes());
    fd.getStartDate().setValue(rec.getStartDate());
    fd.getEndDate().setValue(rec.getEndDate());
    return fd;
  }
}

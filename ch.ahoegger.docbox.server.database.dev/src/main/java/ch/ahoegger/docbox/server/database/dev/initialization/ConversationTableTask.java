package ch.ahoegger.docbox.server.database.dev.initialization;

import java.math.BigDecimal;
import java.util.Date;

import org.ch.ahoegger.docbox.server.or.app.tables.Conversation;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.jooq.Query;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.server.or.generator.table.ConversationTableStatement;

/**
 * <h3>{@link ConversationTableTask}</h3>
 *
 * @author Andreas Hoegger
 */
public class ConversationTableTask extends ConversationTableStatement implements ITableTask {
  private static final Logger LOG = LoggerFactory.getLogger(ConversationTableTask.class);

  @Override
  public void createTable(ISqlService sqlService) {
    LOG.info("SQL-DEV create Table: {}", TABLE_NAME);
    sqlService.insert(getCreateTable());
  }

  @Override
  public void deleteTable(ISqlService sqlService) {
    LOG.info("SQL-DEV delete table: {}", TABLE_NAME);
    Conversation t = Conversation.CONVERSATION;
    Query query = DSL.using(sqlService.getConnection(), SQLDialect.DERBY).delete(t);
    query.execute();
  }

  @Override
  public void dropTable(ISqlService sqlService) {
    LOG.info("SQL-DEV drop table: {}", TABLE_NAME);
    Conversation t = Conversation.CONVERSATION;
    Query query = DSL.using(sqlService.getConnection(), SQLDialect.DERBY).dropTable(t);
    query.execute();
  }

  public void insert(ISqlService sqlService, BigDecimal conversationId, String name, String description,
      Date startDate, Date endDate) {
    Conversation t = Conversation.CONVERSATION;
    DSL.using(sqlService.getConnection(), SQLDialect.DERBY)
        .newRecord(t)
        .with(t.CONVERSATION_NR, conversationId)
        .with(t.END_DATE, endDate)
        .with(t.NAME, name)
        .with(t.NOTES, description)
        .with(t.START_DATE, startDate)
        .insert();
  }

}

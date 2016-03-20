package ch.ahoegger.docbox.server.database.initialization;

import java.util.Date;

import org.eclipse.scout.rt.platform.holders.NVPair;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.server.database.SqlFramentBuilder;
import ch.ahoegger.docbox.shared.conversation.IConversationTable;

/**
 * <h3>{@link ConversationTableTask}</h3>
 *
 * @author Andreas Hoegger
 */
public class ConversationTableTask implements ITableTask, IConversationTable {
  private static final Logger LOG = LoggerFactory.getLogger(ConversationTableTask.class);

  @Override
  public String getCreateStatement() {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("CREATE TABLE ").append(TABLE_NAME).append(" (");
    statementBuilder.append(CONVERSATION_NR).append(" DECIMAL NOT NULL, ");
    statementBuilder.append(NAME).append(" VARCHAR(").append(NAME_LENGTH).append(") NOT NULL, ");
    statementBuilder.append(DESCRIPTION).append(" VARCHAR(").append(DESCRIPTION_LENGTH).append("), ");
    statementBuilder.append(START_DATE).append(" DATE, ");
    statementBuilder.append(END_DATE).append(" DATE, ");
    statementBuilder.append("PRIMARY KEY (").append(CONVERSATION_NR).append(")");
    statementBuilder.append(")");
    return statementBuilder.toString();
  }

  @Override
  public void createTable(ISqlService sqlService) {
    LOG.info("SQL-DEV create Table: {}", TABLE_NAME);
    sqlService.insert(getCreateStatement());
  }

  @Override
  public void createRows(ISqlService sqlService) {
    LOG.info("SQL-DEV create rows for: {}", TABLE_NAME);
    createConversationRow(sqlService, IDevSequenceNumbers.SEQ_START_CONVERSATION, "House selling", "everything related with house selling.", new Date(), null);
    createConversationRow(sqlService, IDevSequenceNumbers.SEQ_START_CONVERSATION + 1, "Ponny order", "all documents to get a ponny.", new Date(), null);
    createConversationRow(sqlService, IDevSequenceNumbers.SEQ_START_CONVERSATION + 2, "Without partner rel", "all documents to get a ponny.", new Date(), null);
  }

  @Override
  public void dropTable(ISqlService sqlService) {
    LOG.info("SQL-DEV drop table: {}", TABLE_NAME);
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("DROP TABLE ").append(TABLE_NAME);
    sqlService.insert(statementBuilder.toString());
  }

  public void createConversationRow(ISqlService sqlService, long conversationId, String name, String description, Date startDate, Date endDate) {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("INSERT INTO ").append(TABLE_NAME).append(" (");
    statementBuilder.append(SqlFramentBuilder.columns(CONVERSATION_NR, NAME, DESCRIPTION, START_DATE, END_DATE));
    statementBuilder.append(") VALUES (");
    statementBuilder.append(":conversationId, :name, :description, :startDate, :endDate");
    statementBuilder.append(")");
    sqlService.insert(statementBuilder.toString(),
        new NVPair("conversationId", conversationId),
        new NVPair("name", name),
        new NVPair("description", description),
        new NVPair("startDate", startDate),
        new NVPair("endDate", endDate));
  }

}

package ch.ahoegger.docbox.server.database.initialization;

import org.eclipse.scout.rt.platform.holders.NVPair;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.server.database.SqlFramentBuilder;
import ch.ahoegger.docbox.shared.partner.IPartnerConversationTable;

/**
 * <h3>{@link PartnerConversationTableTask}</h3>
 *
 * @author Andreas Hoegger
 */
public class PartnerConversationTableTask implements ITableTask, IPartnerConversationTable {

  private static final Logger LOG = LoggerFactory.getLogger(PartnerConversationTableTask.class);

  @Override
  public String getCreateStatement() {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("CREATE TABLE ").append(TABLE_NAME).append(" (");
    statementBuilder.append(PARTNER_NR).append(" DECIMAL NOT NULL, ");
    statementBuilder.append(CONVERSATION_NR).append(" DECIMAL NOT NULL, ");
    statementBuilder.append("PRIMARY KEY (").append(SqlFramentBuilder.columns(PARTNER_NR, CONVERSATION_NR)).append(")");
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
    createPartnerConversationRow(sqlService, IDevSequenceNumbers.SEQ_START_PARTNER, IDevSequenceNumbers.SEQ_START_CONVERSATION);
    createPartnerConversationRow(sqlService, IDevSequenceNumbers.SEQ_START_PARTNER, IDevSequenceNumbers.SEQ_START_CONVERSATION + 1);
  }

  @Override
  public void dropTable(ISqlService sqlService) {
    LOG.info("SQL-DEV drop table: {}", TABLE_NAME);
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("DROP TABLE ").append(TABLE_NAME);
    sqlService.insert(statementBuilder.toString());
  }

  private void createPartnerConversationRow(ISqlService sqlService, Long partnerId, Long conversationId) {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("INSERT INTO ").append(TABLE_NAME).append(" (");
    statementBuilder.append(SqlFramentBuilder.columns(PARTNER_NR, CONVERSATION_NR));
    statementBuilder.append(") VALUES (");
    statementBuilder.append(":partnerId, :conversationId");
    statementBuilder.append(")");
    sqlService.insert(statementBuilder.toString(),
        new NVPair("partnerId", partnerId),
        new NVPair("conversationId", conversationId));
  }
}

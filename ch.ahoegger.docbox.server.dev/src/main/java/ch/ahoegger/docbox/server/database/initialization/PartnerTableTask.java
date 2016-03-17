package ch.ahoegger.docbox.server.database.initialization;

import java.util.Date;

import org.eclipse.scout.rt.platform.holders.NVPair;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.server.database.SqlFramentBuilder;
import ch.ahoegger.docbox.shared.partner.IPartnerTable;

/**
 * <h3>{@link PartnerTableTask}</h3>
 *
 * @author Andreas Hoegger
 */
public class PartnerTableTask implements ITableTask, IPartnerTable {
  private static final Logger LOG = LoggerFactory.getLogger(PartnerTableTask.class);

  @Override
  public String getCreateStatement() {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("CREATE TABLE ").append(TABLE_NAME).append(" (");
    statementBuilder.append(PARTNER_NR).append(" DECIMAL NOT NULL, ");
    statementBuilder.append(NAME).append(" VARCHAR(").append(NAME_LENGTH).append(") NOT NULL, ");
    statementBuilder.append(DESCRIPTION).append(" VARCHAR(").append(DESCRIPTION_LENGTH).append("), ");
    statementBuilder.append(START_DATE).append(" DATE, ");
    statementBuilder.append(END_DATE).append(" DATE, ");
    statementBuilder.append("PRIMARY KEY (").append(PARTNER_NR).append(")");
    statementBuilder.append(")");
    return statementBuilder.toString();
  }

  @Override
  public void createTable(ISqlService sqlService) {
    LOG.info("SQL-DEV create Table: {0}", TABLE_NAME);
    sqlService.insert(getCreateStatement());
  }

  @Override
  public void createRows(ISqlService sqlService) {
    LOG.info("SQL-DEV create rows for: {0}", TABLE_NAME);
    createPartnerRow(sqlService, IDevSequenceNumbers.SEQ_START_PARTNER, "Gorak Inc", "A special company", new Date(), null);
    createPartnerRow(sqlService, IDevSequenceNumbers.SEQ_START_PARTNER + 1, "Solan Org", "Some other comapny", new Date(), null);
  }

  public void createPartnerRow(ISqlService sqlService, long patnerId, String name, String description, Date startDate, Date endDate) {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("INSERT INTO ").append(TABLE_NAME).append(" (");
    statementBuilder.append(SqlFramentBuilder.columns(PARTNER_NR, NAME, DESCRIPTION, START_DATE, END_DATE));
    statementBuilder.append(") VALUES (");
    statementBuilder.append(":partnerId, :name, :description, :startDate, :endDate");
    statementBuilder.append(")");
    sqlService.insert(statementBuilder.toString(),
        new NVPair("partnerId", patnerId),
        new NVPair("name", name),
        new NVPair("description", description),
        new NVPair("startDate", startDate),
        new NVPair("endDate", endDate));
  }

}

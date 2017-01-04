package ch.ahoegger.docbox.server.database.dev.initialization;

import java.math.BigDecimal;
import java.util.Date;

import org.eclipse.scout.rt.platform.holders.NVPair;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.shared.hr.entity.IEntityTable;
import ch.ahoegger.docbox.shared.util.SqlFramentBuilder;

/**
 * <h3>{@link EntityTableTask}</h3>
 *
 * @author Andreas Hoegger
 */
public class EntityTableTask implements ITableTask, IEntityTable {
  private static final Logger LOG = LoggerFactory.getLogger(EntityTableTask.class);

  @Override
  public String getCreateStatement() {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("CREATE TABLE ").append(TABLE_NAME).append(" (");
    statementBuilder.append(ENTITY_NR).append(" DECIMAL NOT NULL, ");
    statementBuilder.append(PARTNER_NR).append(" DECIMAL NOT NULL, ");
    statementBuilder.append(POSTING_GROUP_NR).append(" DECIMAL, ");
    statementBuilder.append(ENTITY_TYPE).append(" DECIMAL NOT NULL, ");
    statementBuilder.append(ENTITY_DATE).append(" DATE, ");
    statementBuilder.append(HOURS).append(" DECIMAL(4, 2), ");
    statementBuilder.append(AMOUNT).append(" DECIMAL(8, 2), ");
    statementBuilder.append(DESCRIPTION).append(" VARCHAR(").append(DESCRIPTION_LENGTH).append("), ");
    statementBuilder.append("PRIMARY KEY (").append(ENTITY_NR).append(")");
    statementBuilder.append(")");
    return statementBuilder.toString();
  }

  @Override
  public void createTable(ISqlService sqlService) {
    LOG.info("SQL-DEV create Table: {}", TABLE_NAME);
    sqlService.insert(getCreateStatement());
  }

  @Override
  public void deleteTable(ISqlService sqlService) {
    LOG.info("SQL-DEV delete table: {}", TABLE_NAME);
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("DELETE FROM ").append(TABLE_NAME);
    sqlService.insert(statementBuilder.toString());
  }

  @Override
  public void dropTable(ISqlService sqlService) {
    LOG.info("SQL-DEV drop table: {}", TABLE_NAME);
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("DROP TABLE ").append(TABLE_NAME);
    sqlService.insert(statementBuilder.toString());
  }

  public void createEntityRow(ISqlService sqlService, BigDecimal entityId, BigDecimal partnerId, BigDecimal postingGroupId, BigDecimal entityType, Date entityDate, BigDecimal hours, BigDecimal amount, String desc) {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("INSERT INTO ").append(TABLE_NAME).append(" (");
    statementBuilder.append(SqlFramentBuilder.columns(ENTITY_NR, PARTNER_NR, POSTING_GROUP_NR, ENTITY_TYPE, ENTITY_DATE, HOURS, AMOUNT, DESCRIPTION));
    statementBuilder.append(") VALUES (");
    statementBuilder.append(":entityId, :partnerId, :postingGroupId, :entityType, :entityDate, :hours, :amount, :desc,  :billed");
    statementBuilder.append(")");
    sqlService.insert(statementBuilder.toString(), new NVPair("entityId", entityId), new NVPair("partnerId", partnerId),
        new NVPair("postingGroupId", postingGroupId),
        new NVPair("entityType", entityType),
        new NVPair("entityDate", entityDate),
        new NVPair("hours", hours),
        new NVPair("amount", amount),
        new NVPair("desc", desc));
  }

}

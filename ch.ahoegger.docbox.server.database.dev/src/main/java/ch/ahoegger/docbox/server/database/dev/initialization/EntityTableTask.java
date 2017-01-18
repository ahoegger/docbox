package ch.ahoegger.docbox.server.database.dev.initialization;

import java.math.BigDecimal;
import java.util.Date;

import org.ch.ahoegger.docbox.server.or.app.tables.Entity;
import org.eclipse.scout.rt.platform.holders.NVPair;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.jooq.Query;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.server.or.generator.table.EntityTableStatement;
import ch.ahoegger.docbox.shared.util.SqlFramentBuilder;

/**
 * <h3>{@link EntityTableTask}</h3>
 *
 * @author Andreas Hoegger
 */
public class EntityTableTask extends EntityTableStatement implements ITableTask {
  private static final Logger LOG = LoggerFactory.getLogger(EntityTableTask.class);

  @Override
  public void createTable(ISqlService sqlService) {
    LOG.info("SQL-DEV create Table: {}", TABLE_NAME);
    sqlService.insert(getCreateTable());
  }

  @Override
  public void deleteTable(ISqlService sqlService) {
    LOG.info("SQL-DEV delete table: {}", TABLE_NAME);
    Entity t = Entity.ENTITY;
    Query query = DSL.using(sqlService.getConnection(), SQLDialect.DERBY).delete(t);
    query.execute();
  }

  @Override
  public void dropTable(ISqlService sqlService) {
    LOG.info("SQL-DEV drop table: {}", TABLE_NAME);
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("DROP TABLE ").append(TABLE_NAME);
    sqlService.insert(statementBuilder.toString());
  }

  public void insert(ISqlService sqlService, BigDecimal entityId, BigDecimal partnerId, BigDecimal postingGroupId, BigDecimal entityType, Date entityDate, BigDecimal hours, BigDecimal amount, String desc) {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("INSERT INTO ").append(TABLE_NAME).append(" (");
    statementBuilder.append(SqlFramentBuilder.columns(ENTITY_NR, PARTNER_NR, POSTING_GROUP_NR, ENTITY_TYPE, ENTITY_DATE, WORKING_HOURS, EXPENSE_AMOUNT, DESCRIPTION));
    statementBuilder.append(") VALUES (");
    statementBuilder.append(":entityId, :partnerId, :postingGroupId, :entityType, :entityDate, :hours, :amount, :desc");
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

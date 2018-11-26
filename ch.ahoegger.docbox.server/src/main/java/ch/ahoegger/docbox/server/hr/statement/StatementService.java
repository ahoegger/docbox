package ch.ahoegger.docbox.server.hr.statement;

import java.math.BigDecimal;

import org.ch.ahoegger.docbox.server.or.app.tables.Statement;
import org.ch.ahoegger.docbox.server.or.app.tables.records.StatementRecord;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.platform.util.Assertions;
import org.eclipse.scout.rt.server.jdbc.SQL;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import ch.ahoegger.docbox.or.definition.table.ISequenceTable;
import ch.ahoegger.docbox.shared.backup.IBackupService;
import ch.ahoegger.docbox.shared.util.LocalDateUtility;

/**
 * <h3>{@link StatementService}</h3>
 *
 * @author aho
 */
public class StatementService implements IService {

  public StatementBean prepareCreate(StatementBean bean) {
    return bean;
  }

  public StatementBean create(StatementBean bean) {
    Assertions.assertNull(bean.getStatementId());
    bean.withStatementId(new BigDecimal(SQL.getSequenceNextval(ISequenceTable.TABLE_NAME)));
    bean.withStatementDate(LocalDateUtility.today());
    int rowCount = insert(bean);
    if (rowCount == 1) {
      // notify backup needed
      BEANS.get(IBackupService.class).notifyModification();
      return bean;
    }
    return null;
  }

  public StatementBean load(StatementBean bean) {
    Assertions.assertNotNull(bean.getStatementId());
    Statement table = Statement.STATEMENT;
    return mapToBean(bean, DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .fetchOne(table, table.STATEMENT_NR.eq(bean.getStatementId())));
  }

  public StatementBean store(StatementBean bean) {
    Assertions.assertNotNull(bean.getStatementId());
    Statement table = Statement.STATEMENT;
    StatementRecord record = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .fetchOne(table, table.STATEMENT_NR.eq(bean.getStatementId()));

    if (record == null) {
      return null;
    }

    int rowCount = mapToRecord(record, bean).update();
    if (rowCount == 1) {
      // notify backup needed
      BEANS.get(IBackupService.class).notifyModification();
      return bean;
    }
    return null;
  }

  public int insert(StatementBean bean) {
    Statement table = Statement.STATEMENT;
    return mapToRecord(DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .newRecord(table), bean).insert();

  }

  protected StatementBean mapToBean(StatementBean bean, StatementRecord rec) {
    if (rec == null) {
      return null;
    }
    return bean.withAccountNumber(rec.getAccountNumber())
        .withBruttoWage(rec.getBruttoWage())
        .withExpenses(rec.getExpenses())
        .withHourlyWage(rec.getHourlyWage())
        .withNettoWage(rec.getNettoWage())
        .withNettoWageRounded(rec.getNettoWagePayout())
        .withPartnerId(rec.getPartnerNr())
        .withSocialInsuranceRate(rec.getSocialInsuranceRate())
        .withSocialInsuranceTax(rec.getSocialSecurityTax())
        .withSourceTax(rec.getSourceTax())
        .withSourceTaxRate(rec.getSourceTaxRate())
        .withStatementDate(rec.getStatementDate())
        .withStatementId(rec.getStatementNr())
        .withTaxType(rec.getTaxType())
        .withVacationExtra(rec.getVacationExtra())
        .withVacationExtraRate(rec.getVacationExtraRate())
        .withWage(rec.getWage())
        .withWorkingHours(rec.getWorkingHours());

  }

  protected StatementRecord mapToRecord(StatementRecord rec, StatementBean bean) {
    Statement table = Statement.STATEMENT;
    return rec
        .with(table.ACCOUNT_NUMBER, bean.getAccountNumber())
        .with(table.BRUTTO_WAGE, bean.getBruttoWage())
        .with(table.EXPENSES, bean.getExpenses())
        .with(table.HOURLY_WAGE, bean.getHourlyWage())
        .with(table.NETTO_WAGE, bean.getNettoWage())
        .with(table.NETTO_WAGE_PAYOUT, bean.getNettoWageRounded())
        .with(table.PARTNER_NR, bean.getPartnerId())
        .with(table.SOCIAL_INSURANCE_RATE, bean.getSocialInsuranceRate())
        .with(table.SOCIAL_SECURITY_TAX, bean.getSocialInsuranceTax())
        .with(table.SOURCE_TAX, bean.getSourceTax())
        .with(table.SOURCE_TAX_RATE, bean.getSourceTaxRate())
        .with(table.STATEMENT_DATE, bean.getStatementDate())
        .with(table.STATEMENT_NR, bean.getStatementId())
        .with(table.TAX_TYPE, bean.getTaxType())
        .with(table.VACATION_EXTRA, bean.getVacationExtra())
        .with(table.VACATION_EXTRA_RATE, bean.getVacationExtraRate())
        .with(table.WAGE, bean.getWage())
        .with(table.WORKING_HOURS, bean.getWorkingHours());
  }
}

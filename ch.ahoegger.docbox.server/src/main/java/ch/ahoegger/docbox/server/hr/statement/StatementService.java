package ch.ahoegger.docbox.server.hr.statement;

import java.math.BigDecimal;

import org.ch.ahoegger.docbox.server.or.app.tables.Statement;
import org.ch.ahoegger.docbox.server.or.app.tables.records.StatementRecord;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.platform.util.Assertions;
import org.eclipse.scout.rt.server.jdbc.SQL;
import org.eclipse.scout.rt.shared.servicetunnel.RemoteServiceAccessDenied;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.or.definition.table.ISequenceTable;
import ch.ahoegger.docbox.server.document.DocumentService;
import ch.ahoegger.docbox.shared.backup.IBackupService;
import ch.ahoegger.docbox.shared.hr.billing.AbstractStatementBoxData;
import ch.ahoegger.docbox.shared.hr.billing.payslip.PayslipTableData.PayslipTableRowData;
import ch.ahoegger.docbox.shared.hr.employee.EmployeeTaxGroupTableData.EmployeeTaxGroupTableRowData;
import ch.ahoegger.docbox.shared.hr.employer.EmployerTaxGroupTableData.EmployerTaxGroupTableRowData;
import ch.ahoegger.docbox.shared.util.LocalDateUtility;

/**
 * <h3>{@link StatementService}</h3>
 *
 * @author aho
 */
public class StatementService implements IService {
  private static final Logger LOG = LoggerFactory.getLogger(StatementService.class);

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
    throw new VetoException("Modification of statements is not allowed");
  }

  public boolean delete(BigDecimal statementId) {
    Assertions.assertNotNull(statementId);
    Statement statement = Statement.STATEMENT;
    StatementRecord rec = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .fetchOne(statement, statement.STATEMENT_NR.eq(statementId));
    if (rec == null) {
      LOG.warn("Try to delete not existing record with id '{}'!", statementId);
      return false;
    }

    // document if available
    BigDecimal documentId = rec.get(statement.DOCUMENT_NR);
    if (documentId != null) {
      BEANS.get(DocumentService.class).delete(documentId);
    }

    if (rec.delete() != 1) {
      LOG.error("Could not delete record with id '{}'!", statementId);
      return false;
    }

    // notify backup needed
    BEANS.get(IBackupService.class).notifyModification();
    return true;
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
        .withDocumentId(rec.getDocumentNr())
        .withExpenses(rec.getExpenses())
        .withHourlyWage(rec.getHourlyWage())
        .withNettoWage(rec.getNettoWage())
        .withNettoWagePayout(rec.getNettoWagePayout())
        .withSocialInsuranceRate(rec.getSocialInsuranceRate())
        .withSocialInsuranceTax(rec.getSocialInsuranceTax())
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

  @RemoteServiceAccessDenied
  protected StatementRecord mapToRecord(StatementRecord rec, StatementBean bean) {
    Statement table = Statement.STATEMENT;
    return rec
        .with(table.ACCOUNT_NUMBER, bean.getAccountNumber())
        .with(table.BRUTTO_WAGE, bean.getBruttoWage())
        .with(table.DOCUMENT_NR, bean.getDocumentId())
        .with(table.EXPENSES, bean.getExpenses())
        .with(table.HOURLY_WAGE, bean.getHourlyWage())
        .with(table.NETTO_WAGE, bean.getNettoWage())
        .with(table.NETTO_WAGE_PAYOUT, bean.getNettoWagePayout())
        .with(table.SOCIAL_INSURANCE_RATE, bean.getSocialInsuranceRate())
        .with(table.SOCIAL_INSURANCE_TAX, bean.getSocialInsuranceTax())
        .with(table.SOURCE_TAX, bean.getSourceTax())
        .with(table.SOURCE_TAX_RATE, bean.getSourceTaxRate())
        .with(table.PENSIONS_FUND, bean.getPensionsFund())
        .with(table.STATEMENT_DATE, bean.getStatementDate())
        .with(table.STATEMENT_NR, bean.getStatementId())
        .with(table.TAX_TYPE, bean.getTaxType())
        .with(table.VACATION_EXTRA, bean.getVacationExtra())
        .with(table.VACATION_EXTRA_RATE, bean.getVacationExtraRate())
        .with(table.WAGE, bean.getWage())
        .with(table.WORKING_HOURS, bean.getWorkingHours());
  }

  @RemoteServiceAccessDenied
  public AbstractStatementBoxData mapToStatementBoxData(AbstractStatementBoxData fd, StatementBean bean) {
    fd.getBruttoWage().setValue(bean.getBruttoWage());
    fd.getNettoWage().setValue(bean.getNettoWage());
    fd.getSocialSecurityTax().setValue(bean.getSocialInsuranceTax());
    fd.getSourceTax().setValue(bean.getSourceTax());
    fd.getPensionsFund().setValue(bean.getPensionsFund());
    fd.getVacationExtra().setValue(bean.getVacationExtra());
    fd.getWorkingHours().setValue(bean.getWorkingHours());
    return fd;
  }

  /**
   * @param rd
   * @param calculateWage
   */
  @RemoteServiceAccessDenied
  public PayslipTableRowData mapToPayslipRowData(PayslipTableRowData rd, StatementBean calculateWage) {
    rd.setTaxType(calculateWage.getTaxType());
    rd.setAccountNumber(calculateWage.getAccountNumber());
    rd.setHourlyWage(calculateWage.getHourlyWage());
    rd.setSocialInsuranceRate(calculateWage.getSocialInsuranceRate());
    rd.setSourceTaxRate(calculateWage.getSourceTaxRate());
    rd.setVacationExtraRate(calculateWage.getVacationExtraRate());
    rd.setWorkingHours(calculateWage.getWorkingHours());
    rd.setWage(calculateWage.getWage());
    rd.setBrutto(calculateWage.getBruttoWage());
    rd.setNetto(calculateWage.getNettoWage());
    rd.setPayout(calculateWage.getNettoWagePayout());
    rd.setSourceTax(calculateWage.getSourceTax());
    rd.setSocialInsuranceTax(calculateWage.getSocialInsuranceTax());
    rd.setVacationExtra(calculateWage.getVacationExtra());
    rd.setExpenses(calculateWage.getExpenses());
    return rd;
  }

  /**
   * @param rd
   * @param calculateWage
   */
  public EmployeeTaxGroupTableRowData mapToEmployeeTaxGruopRowData(EmployeeTaxGroupTableRowData rd, StatementBean calculateWage) {
    rd.setTaxType(calculateWage.getTaxType());
    rd.setAccountNumber(calculateWage.getAccountNumber());
    rd.setHourlyWage(calculateWage.getHourlyWage());
    rd.setSocialInsuranceRate(calculateWage.getSocialInsuranceRate());
    rd.setSourceTaxRate(calculateWage.getSourceTaxRate());
    rd.setVacationExtraRate(calculateWage.getVacationExtraRate());
    rd.setWorkingHours(calculateWage.getWorkingHours());
    rd.setWage(calculateWage.getWage());
    rd.setBrutto(calculateWage.getBruttoWage());
    rd.setNetto(calculateWage.getNettoWage());
    rd.setPayout(calculateWage.getNettoWagePayout());
    rd.setSourceTax(calculateWage.getSourceTax());
    rd.setSocialInsuranceTax(calculateWage.getSocialInsuranceTax());
    rd.setVacationExtra(calculateWage.getVacationExtra());
    rd.setExpenses(calculateWage.getExpenses());
    return rd;
  }

  /**
   * @param rd
   * @param calculateWage
   */
  public EmployerTaxGroupTableRowData mapToEmployerTaxGruopRowData(EmployerTaxGroupTableRowData rd, StatementBean calculateWage) {
    rd.setTaxType(calculateWage.getTaxType());
    rd.setAccountNumber(calculateWage.getAccountNumber());
    rd.setHourlyWage(calculateWage.getHourlyWage());
    rd.setSocialInsuranceRate(calculateWage.getSocialInsuranceRate());
    rd.setSourceTaxRate(calculateWage.getSourceTaxRate());
    rd.setVacationExtraRate(calculateWage.getVacationExtraRate());
    rd.setWorkingHours(calculateWage.getWorkingHours());
    rd.setWage(calculateWage.getWage());
    rd.setBrutto(calculateWage.getBruttoWage());
    rd.setNetto(calculateWage.getNettoWage());
    rd.setPayout(calculateWage.getNettoWagePayout());
    rd.setSourceTax(calculateWage.getSourceTax());
    rd.setSocialInsuranceTax(calculateWage.getSocialInsuranceTax());
    rd.setVacationExtra(calculateWage.getVacationExtra());
    rd.setExpenses(calculateWage.getExpenses());
    return rd;
  }

}

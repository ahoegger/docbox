package ch.ahoegger.docbox.server.hr.employer;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.ch.ahoegger.docbox.server.or.app.tables.EmployeeTaxGroup;
import org.ch.ahoegger.docbox.server.or.app.tables.EmployerTaxGroup;
import org.ch.ahoegger.docbox.server.or.app.tables.Statement;
import org.ch.ahoegger.docbox.server.or.app.tables.records.EmployerTaxGroupRecord;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.util.Assertions;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.platform.util.TriState;
import org.eclipse.scout.rt.server.jdbc.SQL;
import org.eclipse.scout.rt.shared.services.lookup.ILookupRow;
import org.eclipse.scout.rt.shared.servicetunnel.RemoteServiceAccessDenied;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.or.definition.table.ISequenceTable;
import ch.ahoegger.docbox.server.administration.hr.billing.BillingCycleService;
import ch.ahoegger.docbox.server.administration.taxgroup.TaxGroupLookupService;
import ch.ahoegger.docbox.server.administration.taxgroup.TaxGroupService;
import ch.ahoegger.docbox.server.hr.employee.EmployeeTaxGroupService;
import ch.ahoegger.docbox.server.hr.statement.StatementBean;
import ch.ahoegger.docbox.server.hr.statement.StatementService;
import ch.ahoegger.docbox.server.util.BigDecimalUtilitiy;
import ch.ahoegger.docbox.shared.administration.hr.taxgroup.TaxGroupFormData;
import ch.ahoegger.docbox.shared.administration.taxgroup.TaxGroupLookupCall;
import ch.ahoegger.docbox.shared.backup.IBackupService;
import ch.ahoegger.docbox.shared.hr.employee.EmployeeTaxGroupSearchFormData;
import ch.ahoegger.docbox.shared.hr.employee.EmployeeTaxGroupTableData.EmployeeTaxGroupTableRowData;
import ch.ahoegger.docbox.shared.hr.employer.EmployerTaxGroupFormData;
import ch.ahoegger.docbox.shared.hr.employer.EmployerTaxGroupSearchFormData;
import ch.ahoegger.docbox.shared.hr.employer.EmployerTaxGroupTableData;
import ch.ahoegger.docbox.shared.hr.employer.EmployerTaxGroupTableData.EmployerTaxGroupTableRowData;
import ch.ahoegger.docbox.shared.hr.employer.IEmployerTaxGroupService;
import ch.ahoegger.docbox.shared.util.FormDataResult;
import ch.ahoegger.docbox.shared.util.LocalDateUtility;

/**
 * <h3>{@link EmployerTaxGroupService}</h3>
 *
 * @author aho
 */
public class EmployerTaxGroupService implements IEmployerTaxGroupService {
  private static final Logger LOG = LoggerFactory.getLogger(BillingCycleService.class);

  private Field<Integer> eeTaxGroupCountField = DSL.count(EmployeeTaxGroup.EMPLOYEE_TAX_GROUP.EMPLOYEE_TAX_GROUP_NR).as("EMPLOYEE_TAX_GROUP_COUNT");

  @Override
  public EmployerTaxGroupTableData getTableData(EmployerTaxGroupSearchFormData formData) {
    EmployerTaxGroup erTaxGroup = EmployerTaxGroup.EMPLOYER_TAX_GROUP;
    Statement statement = Statement.STATEMENT;
    Condition condition = DSL.trueCondition();

    // TaxGroup
    if (formData.getTaxGroup().getValue() != null) {
      condition = condition.and(erTaxGroup.TAX_GROUP_NR.eq(formData.getTaxGroup().getValue()));
    }
    // employer
    if (formData.getEmployer().getValue() != null) {
      condition = condition.and(erTaxGroup.EMPLOYER_NR.eq(formData.getEmployer().getValue()));
    }

    // active document (valid date)
    if (formData.getFinalizedRadioGroup().getValue() != null) {
      switch (formData.getFinalizedRadioGroup().getValue()) {
        case TRUE:
          condition = condition.and(erTaxGroup.STATEMENT_NR.isNotNull());
          break;
        case FALSE:
          condition = condition.and(erTaxGroup.STATEMENT_NR.isNull());
          break;
      }
    }

    List<EmployerTaxGroupTableRowData> rows = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .select(erTaxGroup.EMPLOYER_TAX_GROUP_NR, erTaxGroup.EMPLOYER_NR, erTaxGroup.TAX_GROUP_NR, erTaxGroup.STATEMENT_NR,
            statement.STATEMENT_NR, statement.DOCUMENT_NR, statement.TAX_TYPE, statement.STATEMENT_DATE, statement.ACCOUNT_NUMBER, statement.HOURLY_WAGE,
            statement.SOCIAL_INSURANCE_RATE, statement.SOURCE_TAX_RATE, statement.VACATION_EXTRA_RATE, statement.WORKING_HOURS, statement.WAGE, statement.BRUTTO_WAGE, statement.NETTO_WAGE,
            statement.NETTO_WAGE_PAYOUT, statement.SOURCE_TAX, statement.SOCIAL_INSURANCE_TAX, statement.VACATION_EXTRA, statement.EXPENSES)
        .from(erTaxGroup)
        .leftOuterJoin(statement).on(erTaxGroup.STATEMENT_NR.eq(statement.STATEMENT_NR))
        .where(condition)
        .fetch()
        .stream()
        .map(rec -> {
          EmployerTaxGroupTableRowData rd = new EmployerTaxGroupTableRowData();
          rd.setEmployerTaxGroupId(rec.get(erTaxGroup.EMPLOYER_TAX_GROUP_NR));
          rd.setEmployer(rec.get(erTaxGroup.EMPLOYER_NR));
          rd.setTaxGroup(rec.get(erTaxGroup.TAX_GROUP_NR));
          rd.setStatementId(rec.get(erTaxGroup.STATEMENT_NR));
          rd.setDocumentId(rec.get(statement.DOCUMENT_NR));
          rd.setTaxType(rec.get(statement.TAX_TYPE));
          rd.setStatementDate(rec.get(statement.STATEMENT_DATE));
          rd.setAccountNumber(rec.get(statement.ACCOUNT_NUMBER));
          rd.setHourlyWage(rec.get(statement.HOURLY_WAGE));
          rd.setSocialInsuranceRate(rec.get(statement.SOCIAL_INSURANCE_RATE));
          rd.setSourceTaxRate(rec.get(statement.SOURCE_TAX_RATE));
          rd.setVacationExtraRate(rec.get(statement.VACATION_EXTRA_RATE));
          rd.setWorkingHours(rec.get(statement.WORKING_HOURS));
          rd.setWage(rec.get(statement.WAGE));
          rd.setBrutto(rec.get(statement.BRUTTO_WAGE));
          rd.setNetto(rec.get(statement.NETTO_WAGE));
          rd.setPayout(rec.get(statement.NETTO_WAGE_PAYOUT));
          rd.setSourceTax(rec.get(statement.SOURCE_TAX));
          rd.setSocialInsuranceTax(rec.get(statement.SOCIAL_INSURANCE_TAX));
          rd.setVacationExtra(rec.get(statement.VACATION_EXTRA));
          rd.setExpenses(rec.get(statement.EXPENSES));
          if (rec.get(erTaxGroup.STATEMENT_NR) == null) {
            // calculate wage
            BEANS.get(StatementService.class).mapToEmployerTaxGruopRowData(rd, calculateWage(rd.getEmployerTaxGroupId()));
          }

          return rd;
        })
        .collect(Collectors.toList());

    EmployerTaxGroupTableData pageData = new EmployerTaxGroupTableData();
    pageData.setRows(rows.toArray(new EmployerTaxGroupTableRowData[rows.size()]));
    return pageData;
  }

  @Override
  public boolean hasTableData(EmployerTaxGroupSearchFormData searchData) {
    return Arrays.asList(getTableData(searchData).getRows()).size() > 0;
  }

  @Override
  public EmployerTaxGroupFormData prepareCreate(EmployerTaxGroupFormData formData) {
    Assertions.assertNotNull(formData.getEmployerId());
    TaxGroupLookupCall call = new TaxGroupLookupCall();
    call.setNotEmployerId(formData.getEmployerId());
    List<? extends ILookupRow<BigDecimal>> taxGroups = BEANS.get(TaxGroupLookupService.class).getDataByAll(call);
    if (taxGroups.size() > 0) {
      formData.getTaxGroup().setValue(taxGroups.get(0).getKey());
    }
    return formData;
  }

  @Override
  public EmployerTaxGroupFormData create(EmployerTaxGroupFormData formData) {
    Assertions.assertNotNull(formData.getEmployerId());
    Assertions.assertNotNull(formData.getTaxGroup().getValue());

    formData.setEmployerTaxGroupId(new BigDecimal(SQL.getSequenceNextval(ISequenceTable.TABLE_NAME)));
    int rowCount = insert(SQL.getConnection(), formData);
    if (rowCount == 1) {
      // notify backup needed
      BEANS.get(IBackupService.class).notifyModification();
      return formData;
    }
    return null;
  }

  @Override
  public EmployerTaxGroupFormData load(EmployerTaxGroupFormData formData) {
    EmployerTaxGroup erTaxGroup = EmployerTaxGroup.EMPLOYER_TAX_GROUP;
    EmployeeTaxGroup eeTaxGroup = EmployeeTaxGroup.EMPLOYEE_TAX_GROUP;
    Condition condition = DSL.trueCondition();

    if (formData.getEmployerTaxGroupId() != null) {
      condition = condition.and(erTaxGroup.EMPLOYER_TAX_GROUP_NR.eq(formData.getEmployerTaxGroupId()));
    }
    else if (formData.getEmployerId() != null && formData.getTaxGroup().getValue() != null) {
      condition = condition.and(
          erTaxGroup.EMPLOYER_NR.eq(formData.getEmployerId())
              .and(erTaxGroup.TAX_GROUP_NR.eq(formData.getTaxGroup().getValue())));
    }
    else {
      throw new VetoException("Could not load EmployerTaxGroup with the given arguments:");
    }

    Record empRec = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .select(erTaxGroup.fields())
        .select(eeTaxGroupCountField)
        .from(erTaxGroup)
        .leftOuterJoin(eeTaxGroup).on(erTaxGroup.EMPLOYER_TAX_GROUP_NR.eq(eeTaxGroup.EMPLOYER_TAX_GROUP_NR))
        .where(condition)
        .groupBy(erTaxGroup.fields())
//        .orderBy(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10)
        .fetchOne();
    if (empRec == null) {
      return null;
    }
    fillFormData(empRec, formData);

    StatementBean statement = new StatementBean();
    if (formData.getStatementId() != null) {
      statement.withStatementId(formData.getStatementId());
      statement = BEANS.get(StatementService.class).load(statement);
    }
    else {
      statement = calculateWage(formData.getEmployerTaxGroupId());
    }
    BEANS.get(StatementService.class).mapToStatementBoxData(formData.getCalculationBox(), statement);

    return formData;
  }

  @Override
  public EmployerTaxGroupFormData finalize(EmployerTaxGroupFormData formData) {
    EmployeeTaxGroupSearchFormData searchData = new EmployeeTaxGroupSearchFormData();
    searchData.setEmployerTaxGroupId(formData.getEmployerTaxGroupId());
    searchData.getFinalizedRadioGroup().setValue(TriState.FALSE);
    if (BEANS.get(EmployeeTaxGroupService.class).hasTableData(searchData)) {
      throw new VetoException("Can only finalize when all subentries are finalized.");
    }
    Date statementDate = LocalDateUtility.today();

    StatementBean statementBean = calculateWage(formData.getEmployerTaxGroupId());
    // create statement
    statementBean.withStatementDate(statementDate);
    statementBean = BEANS.get(StatementService.class).create(statementBean);
    formData.setStatementId(statementBean.getStatementId());

    // update erTaxGroup
    EmployerTaxGroup table = EmployerTaxGroup.EMPLOYER_TAX_GROUP;
    EmployerTaxGroupRecord record = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .fetchOne(table, table.EMPLOYER_TAX_GROUP_NR.eq(formData.getEmployerTaxGroupId()));
    if (record == null) {
      return null;
    }
    int rowCount = mapToRecord(record, formData).update();
    if (rowCount == 1) {
      // notify backup needed
      BEANS.get(IBackupService.class).notifyModification();

      return formData;
    }
    return formData;
  }

  @Override
  public boolean delete(BigDecimal employerTaxGroupId) {
    EmployeeTaxGroupSearchFormData searchData = new EmployeeTaxGroupSearchFormData();
    searchData.setEmployerTaxGroupId(employerTaxGroupId);
    if (BEANS.get(EmployeeTaxGroupService.class).hasTableData(searchData)) {
      throw new VetoException("Can only delete empty tax groups");
    }

    EmployerTaxGroup erTaxGroup = EmployerTaxGroup.EMPLOYER_TAX_GROUP;
    EmployerTaxGroupRecord rec = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .fetchOne(erTaxGroup, erTaxGroup.EMPLOYER_TAX_GROUP_NR.eq(employerTaxGroupId));
    if (rec == null) {
      LOG.warn("Try to delete not existing record with id '{}'!", employerTaxGroupId);
      return false;
    }
    if (rec.delete() != 1) {
      LOG.error("Could not delete record with id '{}'!", employerTaxGroupId);
      return false;
    }
    return true;
  }

  @Override
  public TaxGroupFormData getTaxGroup(BigDecimal employerTaxGroupId) {
    EmployerTaxGroupFormData erTaxGroupData = new EmployerTaxGroupFormData();
    erTaxGroupData.setEmployerTaxGroupId(employerTaxGroupId);
    erTaxGroupData = load(erTaxGroupData);
    if (erTaxGroupData == null) {
      return null;
    }

    TaxGroupFormData taxGroupData = new TaxGroupFormData();
    taxGroupData.setTaxGroupId(erTaxGroupData.getTaxGroup().getValue());
    return BEANS.get(TaxGroupService.class).load(taxGroupData);
  }

  public EmployerTaxGroupFormData getOrCreate(BigDecimal employerId, BigDecimal taxGroupId) {
    // try get
    EmployerTaxGroup erTaxGroup = EmployerTaxGroup.EMPLOYER_TAX_GROUP;
    return Optional.ofNullable(
        DSL.using(SQL.getConnection(), SQLDialect.DERBY)
            .select(erTaxGroup.EMPLOYER_TAX_GROUP_NR)
            .from(erTaxGroup)
            .where(erTaxGroup.EMPLOYER_NR.eq(employerId)
                .and(erTaxGroup.TAX_GROUP_NR.eq(taxGroupId)))
            .fetchOne())
        .map(rec -> rec.get(erTaxGroup.EMPLOYER_TAX_GROUP_NR))
        .map(erTaxGroupId -> {
          EmployerTaxGroupFormData fd = new EmployerTaxGroupFormData();
          fd.setEmployerTaxGroupId(erTaxGroupId);
          return load(fd);
        }).orElseGet(() -> {
          EmployerTaxGroupFormData fd = new EmployerTaxGroupFormData();
          fd.setEmployerId(employerId);
          fd.getTaxGroup().setValue(taxGroupId);
          fd = create(fd);
          return fd;
        });
  }

  @Override
  public FormDataResult<EmployerTaxGroupFormData, Boolean> isFinalized(BigDecimal employerTaxgroupId) {
    Assertions.assertNotNull(employerTaxgroupId);
    EmployerTaxGroupFormData formData = new EmployerTaxGroupFormData();
    formData.setEmployerTaxGroupId(employerTaxgroupId);
    formData = load(formData);
    return new FormDataResult<>(formData, formData.getStatementId() != null);
  }
//  /**
//   * @param employerTaxGroupNr
//   * @return
//   */
//  @RemoteServiceAccessDenied
//  public IStatus isFinalized(BigDecimal employerTaxGroupNr) {
//    EmployerTaxGroup erTaxGroup = EmployerTaxGroup.EMPLOYER_TAX_GROUP;
//    TaxGroup taxGroup = TaxGroup.TAX_GROUP;
//    Employer employer = Employer.EMPLOYER;
//
//    Record rec = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
//        .select(taxGroup.NAME, taxGroup.START_DATE, taxGroup.END_DATE, employer.NAME, erTaxGroup.STATEMENT_NR)
//        .from(erTaxGroup)
//        .leftOuterJoin(employer).on(erTaxGroup.EMPLOYER_NR.eq(employer.EMPLOYER_NR))
//        .leftOuterJoin(taxGroup).on(erTaxGroup.TAX_GROUP_NR.eq(taxGroup.TAX_GROUP_NR))
//        .where(erTaxGroup.EMPLOYER_TAX_GROUP_NR.eq(employerTaxGroupNr))
//        .fetchOne();
//    if (rec == null) {
//      throw new ProcessingException("EmployeeTaxGroup with id:'{}' not found in DB.", employerTaxGroupNr);
//    }
//    if (rec.get(erTaxGroup.STATEMENT_NR) != null) {
//      return new Status(
//          String.format("Employer tax group '%s (%s-%s)' of employer '%s' is already finalized!",
//              rec.get(taxGroup.NAME),
//              LocalDateUtility.format(rec.get(taxGroup.START_DATE), LocalDateUtility.DATE_FORMATTER_ddMMyyyy),
//              LocalDateUtility.format(rec.get(taxGroup.END_DATE), LocalDateUtility.DATE_FORMATTER_ddMMyyyy),
//              rec.get(employer.NAME)),
//          IStatus.ERROR);
//    }
//    return Status.OK_STATUS;
//  }

  @RemoteServiceAccessDenied
  public int insert(Connection connection, BigDecimal employerTaxGroupId, BigDecimal employerId, BigDecimal taxGroupId, BigDecimal statementId) {
    EmployerTaxGroupFormData fd = new EmployerTaxGroupFormData();
    fd.setEmployerTaxGroupId(employerTaxGroupId);
    fd.setEmployerId(employerId);
    fd.getTaxGroup().setValue(taxGroupId);
    fd.setStatementId(statementId);
    return insert(connection, fd);
  }

  @RemoteServiceAccessDenied
  public int insert(Connection connection, EmployerTaxGroupFormData formData) {
    EmployerTaxGroup table = EmployerTaxGroup.EMPLOYER_TAX_GROUP;
    return mapToRecord(DSL.using(connection, SQLDialect.DERBY)
        .newRecord(table), formData).insert();

  }

  protected EmployerTaxGroupRecord mapToRecord(EmployerTaxGroupRecord rec, EmployerTaxGroupFormData formData) {
    EmployerTaxGroup table = EmployerTaxGroup.EMPLOYER_TAX_GROUP;
    return rec
        .with(table.EMPLOYER_TAX_GROUP_NR, formData.getEmployerTaxGroupId())
        .with(table.EMPLOYER_NR, formData.getEmployerId())
        .with(table.TAX_GROUP_NR, formData.getTaxGroup().getValue())
        .with(table.STATEMENT_NR, formData.getStatementId());
  }

  protected EmployerTaxGroupFormData fillFormData(Record rec, EmployerTaxGroupFormData fd) {
    if (rec == null) {
      return null;
    }
    EmployerTaxGroup erTaxGroup = EmployerTaxGroup.EMPLOYER_TAX_GROUP;
    fd.setEmployerTaxGroupId(erTaxGroup.EMPLOYER_TAX_GROUP_NR.get(rec));
    fd.setEmployerId(erTaxGroup.EMPLOYER_NR.get(rec));
    fd.getTaxGroup().setValue(erTaxGroup.TAX_GROUP_NR.get(rec));
    fd.setStatementId(erTaxGroup.STATEMENT_NR.get(rec));
    fd.getEmployeeTaxGroupCount().setValue(eeTaxGroupCountField.get(rec));
    return fd;
  }

  /**
   * @param employerTaxGroupId
   * @return
   */
  private StatementBean calculateWage(BigDecimal employerTaxGroupId) {
    EmployeeTaxGroupSearchFormData eeTaxGroupSearchData = new EmployeeTaxGroupSearchFormData();
    eeTaxGroupSearchData.setEmployerTaxGroupId(employerTaxGroupId);
    EmployeeTaxGroupTableRowData result = new EmployeeTaxGroupTableRowData();

    result = CollectionUtility.arrayList(BEANS.get(EmployeeTaxGroupService.class).getTableData(eeTaxGroupSearchData).getRows())
        .stream()
        .reduce(result, (acc, row2) -> {
          acc.setBrutto(BigDecimalUtilitiy.nullSafeAdd(acc.getBrutto(), row2.getBrutto()));
          acc.setExpenses(BigDecimalUtilitiy.nullSafeAdd(acc.getExpenses(), row2.getExpenses()));
          acc.setNetto(BigDecimalUtilitiy.nullSafeAdd(acc.getNetto(), row2.getNetto()));
          acc.setPayout(BigDecimalUtilitiy.nullSafeAdd(acc.getPayout(), row2.getPayout()));
          acc.setSocialInsuranceTax(BigDecimalUtilitiy.nullSafeAdd(acc.getSocialInsuranceTax(), row2.getSocialInsuranceTax()));
          acc.setSourceTax(BigDecimalUtilitiy.nullSafeAdd(acc.getSourceTax(), row2.getSourceTax()));
          acc.setVacationExtra(BigDecimalUtilitiy.nullSafeAdd(acc.getVacationExtra(), row2.getVacationExtra()));
          acc.setWage(BigDecimalUtilitiy.nullSafeAdd(acc.getWage(), row2.getWage()));
          acc.setWorkingHours(BigDecimalUtilitiy.nullSafeAdd(acc.getWorkingHours(), row2.getWorkingHours()));
          return acc;
        });

    return new StatementBean()
        .withBruttoWage(result.getBrutto())
        .withExpenses(result.getExpenses())
        .withNettoWage(result.getNetto())
        .withNettoWagePayout(result.getPayout())
        .withSocialInsuranceTax(result.getSocialInsuranceTax())
        .withSourceTax(result.getSourceTax())
        .withVacationExtra(result.getVacationExtra())
        .withWage(result.getWage())
        .withWorkingHours(result.getWorkingHours());
  }

}

package ch.ahoegger.docbox.server.hr.employee;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.ch.ahoegger.docbox.server.or.app.tables.EmployeeTaxGroup;
import org.ch.ahoegger.docbox.server.or.app.tables.EmployerTaxGroup;
import org.ch.ahoegger.docbox.server.or.app.tables.Statement;
import org.ch.ahoegger.docbox.server.or.app.tables.records.EmployeeTaxGroupRecord;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingStatus;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.resource.BinaryResource;
import org.eclipse.scout.rt.platform.status.IStatus;
import org.eclipse.scout.rt.platform.status.Status;
import org.eclipse.scout.rt.platform.util.Assertions;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.platform.util.TriState;
import org.eclipse.scout.rt.server.jdbc.SQL;
import org.eclipse.scout.rt.shared.servicetunnel.RemoteServiceAccessDenied;
import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import ch.ahoegger.docbox.or.definition.table.ISequenceTable;
import ch.ahoegger.docbox.server.document.DocumentService;
import ch.ahoegger.docbox.server.hr.SalaryStatementService;
import ch.ahoegger.docbox.server.hr.billing.payslip.PayslipService;
import ch.ahoegger.docbox.server.hr.employer.EmployerTaxGroupService;
import ch.ahoegger.docbox.server.hr.statement.StatementBean;
import ch.ahoegger.docbox.server.hr.statement.StatementService;
import ch.ahoegger.docbox.server.util.BigDecimalUtilitiy;
import ch.ahoegger.docbox.shared.administration.hr.taxgroup.TaxGroupFormData;
import ch.ahoegger.docbox.shared.backup.IBackupService;
import ch.ahoegger.docbox.shared.document.DocumentFormData;
import ch.ahoegger.docbox.shared.document.DocumentFormData.Partners.PartnersRowData;
import ch.ahoegger.docbox.shared.hr.billing.payslip.IPayslipService;
import ch.ahoegger.docbox.shared.hr.billing.payslip.PayslipSearchFormData;
import ch.ahoegger.docbox.shared.hr.billing.payslip.PayslipTableData;
import ch.ahoegger.docbox.shared.hr.billing.payslip.PayslipTableData.PayslipTableRowData;
import ch.ahoegger.docbox.shared.hr.billing.taxgroup.IEmployeeTaxGroupService;
import ch.ahoegger.docbox.shared.hr.employee.EmployeeFormData;
import ch.ahoegger.docbox.shared.hr.employee.EmployeeTaxGroupFormData;
import ch.ahoegger.docbox.shared.hr.employee.EmployeeTaxGroupSearchFormData;
import ch.ahoegger.docbox.shared.hr.employee.EmployeeTaxGroupTableData;
import ch.ahoegger.docbox.shared.hr.employee.EmployeeTaxGroupTableData.EmployeeTaxGroupTableRowData;
import ch.ahoegger.docbox.shared.hr.employer.EmployerTaxGroupFormData;
import ch.ahoegger.docbox.shared.hr.employer.EmployerTaxGroupSearchFormData;
import ch.ahoegger.docbox.shared.util.FormDataResult;
import ch.ahoegger.docbox.shared.util.LocalDateUtility;
import ch.ahoegger.docbox.shared.util.LocalDateUtility.LocalDateRange;

/**
 * <h3>{@link EmployeeTaxGroupService}</h3>
 *
 * @author aho
 */
public class EmployeeTaxGroupService implements IEmployeeTaxGroupService {
  public static Function<? super BigDecimal, ? extends BigDecimal> NULL_AS_ZERO_MAPPER = in -> Optional.ofNullable(in).orElse(BigDecimal.ZERO);

  @Override
  public EmployeeTaxGroupTableData getTableData(EmployeeTaxGroupSearchFormData formData) {
    EmployeeTaxGroup eeTaxGroup = EmployeeTaxGroup.EMPLOYEE_TAX_GROUP;
    EmployerTaxGroup erTaxGroup = EmployerTaxGroup.EMPLOYER_TAX_GROUP;
    Statement statement = Statement.STATEMENT;
    Condition condition = DSL.trueCondition();

    if (formData.getEmployerTaxGroupId() != null) {
      condition = condition.and(eeTaxGroup.EMPLOYER_TAX_GROUP_NR.eq(formData.getEmployerTaxGroupId()));
    }
    if (formData.getTaxGroup().getValue() != null) {
      condition = condition.and(erTaxGroup.TAX_GROUP_NR.eq(formData.getTaxGroup().getValue()));
    }
    // employee
    if (formData.getEmployee().getValue() != null) {
      condition = condition.and(eeTaxGroup.EMPLOYEE_NR.eq(formData.getEmployee().getValue()));
    }
    // employees
    if (formData.getEmployeeIds() != null) {
      condition = condition.and(eeTaxGroup.EMPLOYEE_NR.in(formData.getEmployeeIds()));
    }
    // active document (valid date)
    if (formData.getFinalizedRadioGroup().getValue() != null) {
      switch (formData.getFinalizedRadioGroup().getValue()) {
        case TRUE:
          condition = condition.and(eeTaxGroup.STATEMENT_NR.isNotNull());
          break;
        case FALSE:
          condition = condition.and(eeTaxGroup.STATEMENT_NR.isNull());
          break;
      }
    }

    List<EmployeeTaxGroupTableRowData> rows = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .select(eeTaxGroup.EMPLOYEE_TAX_GROUP_NR, eeTaxGroup.EMPLOYEE_NR, erTaxGroup.TAX_GROUP_NR, eeTaxGroup.STATEMENT_NR, eeTaxGroup.START_DATE, eeTaxGroup.END_DATE,
            statement.STATEMENT_NR, statement.DOCUMENT_NR, statement.TAX_TYPE, statement.STATEMENT_DATE, statement.ACCOUNT_NUMBER, statement.HOURLY_WAGE,
            statement.SOCIAL_INSURANCE_RATE, statement.SOURCE_TAX_RATE, statement.VACATION_EXTRA_RATE, statement.WORKING_HOURS, statement.WAGE, statement.BRUTTO_WAGE, statement.NETTO_WAGE,
            statement.NETTO_WAGE_PAYOUT, statement.SOURCE_TAX, statement.SOCIAL_INSURANCE_TAX, statement.VACATION_EXTRA, statement.EXPENSES)
        .from(eeTaxGroup)
        .leftOuterJoin(erTaxGroup).on(eeTaxGroup.EMPLOYER_TAX_GROUP_NR.eq(erTaxGroup.EMPLOYER_TAX_GROUP_NR))
        .leftOuterJoin(statement).on(eeTaxGroup.STATEMENT_NR.eq(statement.STATEMENT_NR))
        .where(condition)
        .fetch()
        .stream()
        .map(rec -> {
          EmployeeTaxGroupTableRowData rd = new EmployeeTaxGroupTableRowData();
          rd.setId(rec.get(eeTaxGroup.EMPLOYEE_TAX_GROUP_NR));
          rd.setEmployee(rec.get(eeTaxGroup.EMPLOYEE_NR));
          rd.setTaxGroup(rec.get(erTaxGroup.TAX_GROUP_NR));
          rd.setStatementId(rec.get(eeTaxGroup.STATEMENT_NR));
          rd.setStart(rec.get(eeTaxGroup.START_DATE));
          rd.setEnd(rec.get(eeTaxGroup.END_DATE));
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
          if (rec.get(eeTaxGroup.STATEMENT_NR) == null) {
            // calculate wage
            BEANS.get(StatementService.class).mapToEmployeeTaxGruopRowData(rd, calculateWage(rd.getId()));
          }

          return rd;
        })
        .collect(Collectors.toList());

    EmployeeTaxGroupTableData pageData = new EmployeeTaxGroupTableData();
    pageData.setRows(rows.toArray(new EmployeeTaxGroupTableRowData[rows.size()]));
    return pageData;
  }

  @Override
  public boolean hasTableData(EmployeeTaxGroupSearchFormData searchData) {
    return getTableData(searchData).getRowCount() > 0;
  }

  @Override
  public IStatus validate(EmployeeTaxGroupFormData formData) {
    return validate(formData, false);
  }

  public IStatus validate(EmployeeTaxGroupFormData formData, boolean throwVetoException) {
    IStatus status = null;
    if (formData.getTaxGroup().getValue() == null) {
      status = new Status("Tax group is not set.", IStatus.ERROR);
      if (throwVetoException) {
        throw new VetoException(new ProcessingStatus(status));
      }
      return status;
    }
    ensureEmployerTaxGroup(formData);

    LocalDateRange payslipRange = null;
    if (formData.getEmployeeTaxGroupId() != null) {
      PayslipSearchFormData payslipSearchData = new PayslipSearchFormData();
      payslipSearchData.setEmployeeTaxGroupId(formData.getEmployeeTaxGroupId());
      payslipRange = BEANS.get(PayslipService.class).getDateRange(payslipSearchData);
    }
    TaxGroupFormData taxGroupData = BEANS.get(EmployerTaxGroupService.class).getTaxGroup(formData.getEmployerTaxGroupId());
    if (formData.getPeriodBox().getFrom().getValue() != null) {
      if (!LocalDateUtility.isBetweenOrEqual(taxGroupData.getPeriodBox().getFrom().getValue(), taxGroupData.getPeriodBox().getTo().getValue(), formData.getPeriodBox().getFrom().getValue())) {
        status = new Status("Date period not in range of tax group date range.", IStatus.ERROR);
        if (throwVetoException) {
          throw new VetoException(new ProcessingStatus(status));
        }
        return status;
      }
      if (payslipRange != null) {
        if (LocalDateUtility.toLocalDate(formData.getPeriodBox().getFrom().getValue()).isAfter(payslipRange.getFrom())) {

          status = new Status("Date period dos not cover the payslips.", IStatus.ERROR);
          if (throwVetoException) {
            throw new VetoException(new ProcessingStatus(status));
          }
          return status;
        }
      }
    }
    if (formData.getPeriodBox().getTo().getValue() != null) {
      if (!LocalDateUtility.isBetweenOrEqual(taxGroupData.getPeriodBox().getFrom().getValue(), taxGroupData.getPeriodBox().getTo().getValue(), formData.getPeriodBox().getTo().getValue())) {
        status = new Status("Date period not in range of tax group date range.", IStatus.ERROR);
        if (throwVetoException) {
          throw new VetoException(new ProcessingStatus(status));
        }
        return status;
      }
      if (payslipRange != null) {
        if (LocalDateUtility.toLocalDate(formData.getPeriodBox().getTo().getValue()).isBefore(payslipRange.getTo())) {

          status = new Status("Date period dos not cover the payslips.", IStatus.ERROR);
          if (throwVetoException) {
            throw new VetoException(new ProcessingStatus(status));
          }
          return status;
        }
      }
    }
    return Status.OK_STATUS;
  }

  protected void ensureEmployerTaxGroup(EmployeeTaxGroupFormData formData) {
    if (formData.getEmployerTaxGroupId() == null) {
      if (formData.getEmployeeId() != null && formData.getTaxGroup().getValue() != null) {
        EmployeeFormData eeData = new EmployeeFormData();
        eeData.setPartnerId(formData.getEmployeeId());
        BEANS.get(EmployeeService.class).load(eeData);
        EmployerTaxGroupSearchFormData data = new EmployerTaxGroupSearchFormData();
        data.getEmployer().setValue(eeData.getEmployer().getValue());
        data.getTaxGroup().setValue(formData.getTaxGroup().getValue());

        EmployerTaxGroupFormData a = new EmployerTaxGroupFormData();
        a.setEmployerId(eeData.getEmployer().getValue());
        a.getTaxGroup().setValue(formData.getTaxGroup().getValue());
        BEANS.get(EmployerTaxGroupService.class).load(a);
        formData.setEmployerTaxGroupId(a.getEmployerTaxGroupId());
      }
    }
  }

  @Override
  public EmployeeTaxGroupFormData prepareCreate(EmployeeTaxGroupFormData formData) {
    if (formData.getEmployerTaxGroupId() != null) {
      EmployerTaxGroup erTaxGroup = EmployerTaxGroup.EMPLOYER_TAX_GROUP;
      Record1<BigDecimal> rec = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
          .select(erTaxGroup.TAX_GROUP_NR)
          .from(erTaxGroup)
          .where(erTaxGroup.EMPLOYER_TAX_GROUP_NR.eq(formData.getEmployerTaxGroupId()))
          .fetchOne();
      formData.getTaxGroup().setValue(rec.get(erTaxGroup.TAX_GROUP_NR));
    }
    return formData;
  }

  @Override
  public EmployeeTaxGroupFormData create(EmployeeTaxGroupFormData formData) {
    ensureEmployerTaxGroup(formData);
    Assertions.assertNotNull(formData.getEmployerTaxGroupId());

    FormDataResult<EmployerTaxGroupFormData, Boolean> finalizedResult = BEANS.get(EmployerTaxGroupService.class).isFinalized(formData.getEmployerTaxGroupId());
    if (finalizedResult.getValue()) {
      throw new VetoException(new ProcessingStatus(
          String.format("EmployerTaxGroup is finalized!"),
          IStatus.ERROR));
    }
    formData.setEmployeeTaxGroupId(new BigDecimal(SQL.getSequenceNextval(ISequenceTable.TABLE_NAME)));
    int rowCount = insert(SQL.getConnection(), formData);
    if (rowCount == 1) {
      // notify backup needed
      BEANS.get(IBackupService.class).notifyModification();
      return formData;
    }
    return null;
  }

  @Override
  public EmployeeTaxGroupFormData load(EmployeeTaxGroupFormData formData) {

    EmployeeTaxGroup eeTaxGroup = EmployeeTaxGroup.EMPLOYEE_TAX_GROUP;
    EmployerTaxGroup erTaxGroup = EmployerTaxGroup.EMPLOYER_TAX_GROUP;
    Condition conditions = DSL.trueCondition();
    if (formData.getEmployeeTaxGroupId() != null) {
      conditions = conditions.and(eeTaxGroup.EMPLOYEE_TAX_GROUP_NR.eq(formData.getEmployeeTaxGroupId()));
    }
    else if (formData.getEmployeeId() != null && formData.getTaxGroup().getValue() != null) {
      conditions = conditions.and(
          eeTaxGroup.EMPLOYEE_NR.eq(formData.getEmployeeId())
              .and(erTaxGroup.TAX_GROUP_NR.eq(formData.getTaxGroup().getValue())));
    }
    else {
      throw new VetoException("employeeTaxGroupId or (employeeId + taxGroupId) must be set!");
    }
    Record empRec = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .select(eeTaxGroup.fields()).select(erTaxGroup.TAX_GROUP_NR)
        .from(eeTaxGroup)
        .leftOuterJoin(erTaxGroup).on(eeTaxGroup.EMPLOYER_TAX_GROUP_NR.eq(erTaxGroup.EMPLOYER_TAX_GROUP_NR))
        .where(conditions)
        .fetchOne();

    mapToFormData(empRec, formData);

    // min date range
    PayslipSearchFormData payslipSearchData = new PayslipSearchFormData();
    payslipSearchData.setEmployeeTaxGroupId(formData.getEmployeeTaxGroupId());
    LocalDateRange payslipRange = BEANS.get(PayslipService.class).getDateRange(payslipSearchData);
    if (payslipRange != null) {
      formData.getMinPeriodBox().getFrom().setValue(LocalDateUtility.toDate(payslipRange.getFrom()));
      formData.getMinPeriodBox().getTo().setValue(LocalDateUtility.toDate(payslipRange.getTo()));
    }

    StatementBean statement = new StatementBean();
    if (formData.getStatementId() != null) {
      statement.withStatementId(formData.getStatementId());
      statement = BEANS.get(StatementService.class).load(statement);
    }
    else {
      statement = calculateWage(formData.getEmployeeTaxGroupId());
    }
    BEANS.get(StatementService.class).mapToStatementBoxData(formData.getWageBox(), statement);
    return formData;
  }

  @Override
  public EmployeeTaxGroupFormData store(EmployeeTaxGroupFormData formData) {
    Assertions.assertNotNull(formData.getEmployeeTaxGroupId());
    validate(formData, true);

    FormDataResult<EmployerTaxGroupFormData, Boolean> finalizedResult = BEANS.get(EmployerTaxGroupService.class).isFinalized(formData.getEmployerTaxGroupId());
    if (finalizedResult.getValue()) {
      throw new VetoException(new ProcessingStatus(
          String.format("EmployerTaxGroup is finalized!"),
          IStatus.ERROR));
    }
    EmployeeTaxGroup eeTaxGroup = EmployeeTaxGroup.EMPLOYEE_TAX_GROUP;
    EmployeeTaxGroupRecord record = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .fetchOne(eeTaxGroup, eeTaxGroup.EMPLOYEE_TAX_GROUP_NR.eq(formData.getEmployeeTaxGroupId()));
    if (record == null) {
      return null;
    }
    int rowCount = mapToRecord(record, formData).update();
    if (rowCount == 1) {
      // notify backup needed
      BEANS.get(IBackupService.class).notifyModification();
      return formData;
    }
    return null;
  }

  @Override
  public EmployeeTaxGroupFormData finalize(EmployeeTaxGroupFormData formData) {
    Assertions.assertNotNull(formData.getEmployeeTaxGroupId());

    // if already finalized
    if (formData.getStatementId() != null) {
      throw new VetoException(new ProcessingStatus("EmployeeTaxGroup is already finalized.", IStatus.ERROR));
    }

    // check all payslips are finalized
    PayslipSearchFormData payslipSearchData = new PayslipSearchFormData();
    payslipSearchData.setEmployeeTaxGroupId(formData.getEmployeeTaxGroupId());
    payslipSearchData.getFinalzedRadioGroup().setValue(TriState.FALSE);
    if (BEANS.get(IPayslipService.class).hasTableData(payslipSearchData)) {
      throw new VetoException(new ProcessingStatus("Payslips of EmployeeTaxGroup are not finalized.", IStatus.ERROR));
    }

    EmployeeFormData employeeData = new EmployeeFormData();
    employeeData.setPartnerId(formData.getEmployeeId());
    employeeData = BEANS.get(EmployeeService.class).load(employeeData);

    Date statementDate = LocalDateUtility.today();

    StatementBean statementBean = calculateWage(formData.getEmployeeTaxGroupId());
    BinaryResource statementContent = BEANS.get(SalaryStatementService.class).createSalaryStatement(formData, statementBean);
    // create document
    DocumentFormData documentData = new DocumentFormData();
    DocumentService documentService = BEANS.get(DocumentService.class);
    documentData = documentService.prepareCreate(documentData);
    documentData.getDocumentDate().setValue(statementDate);
    documentData.getDocument().setValue(statementContent);
    PartnersRowData partnerRow = documentData.getPartners().addRow();
    partnerRow.setPartner(formData.getEmployeeId());
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", new Locale("de", "CH"));
    documentData.getAbstract().setValue(
        new StringBuilder()
            .append("Lohnabrechnung ")
            .append(DateTimeFormatter.ofPattern("yyyy", new Locale("de", "CH")).format(LocalDateUtility.toLocalDate(formData.getPeriodBox().getFrom().getValue())))
            .append(" (")
            .append(dateFormatter.format(LocalDateUtility.toLocalDate(formData.getPeriodBox().getFrom().getValue())))
            .append(" - ")
            .append(dateFormatter.format(LocalDateUtility.toLocalDate(formData.getPeriodBox().getTo().getValue())))
            .toString());
    documentData.getCategoriesBox().setValue(CollectionUtility.emptyTreeSet());
    documentData = documentService.create(documentData);

    // create statement
    statementBean.withStatementDate(statementDate);
    statementBean = BEANS.get(EmployeeService.class).mapToStatementBean(employeeData, statementBean);
    statementBean.withDocumentId(documentData.getDocumentId());

    statementBean = BEANS.get(StatementService.class).create(statementBean);
    formData.setStatementId(statementBean.getStatementId());

    // update empTaxGroup
    EmployeeTaxGroup table = EmployeeTaxGroup.EMPLOYEE_TAX_GROUP;
    EmployeeTaxGroupRecord record = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .fetchOne(table, table.EMPLOYEE_TAX_GROUP_NR.eq(formData.getEmployeeTaxGroupId()));
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
  public EmployeeTaxGroupFormData unfinalize(BigDecimal employeeTaxGroupId) {
    Assertions.assertNotNull(employeeTaxGroupId);
    EmployeeTaxGroupFormData formData = new EmployeeTaxGroupFormData();
    formData.setEmployeeTaxGroupId(employeeTaxGroupId);
    formData = load(formData);

    // delete statement
    BEANS.get(StatementService.class).delete(formData.getStatementId());

    // update employeeTaxGroup
    EmployeeTaxGroup eeTaxGroup = EmployeeTaxGroup.EMPLOYEE_TAX_GROUP;
    formData.setStatementId(null);
    EmployeeTaxGroupRecord record = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .fetchOne(eeTaxGroup, eeTaxGroup.EMPLOYEE_TAX_GROUP_NR.eq(formData.getEmployeeTaxGroupId()));
    if (record == null) {
      return null;
    }
    int rowCount = mapToRecord(record, formData).update();
    if (rowCount == 1) {
      // notify backup needed
      BEANS.get(IBackupService.class).notifyModification();
    }
    return formData;
  }

  /**
   * @param employeeId
   * @param employerTaxGroupId
   */
  public EmployeeTaxGroupFormData getOrCreate(BigDecimal employeeId, BigDecimal employerTaxGroupId) {
    EmployeeTaxGroup eeTaxGroup = EmployeeTaxGroup.EMPLOYEE_TAX_GROUP;
    return Optional.ofNullable(DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .fetchOne(eeTaxGroup, eeTaxGroup.EMPLOYEE_NR.eq(employeeId).and(eeTaxGroup.EMPLOYER_TAX_GROUP_NR.eq(employerTaxGroupId))))
        .map(rec -> {
          EmployeeTaxGroupFormData fd = new EmployeeTaxGroupFormData();
          fd.setEmployeeTaxGroupId(rec.get(eeTaxGroup.EMPLOYEE_TAX_GROUP_NR));
          fd = load(fd);
          return fd;
        })
        .orElseGet(() -> {
          EmployeeTaxGroupFormData fd = new EmployeeTaxGroupFormData();
          fd.setEmployeeId(employeeId);
          fd.setEmployerTaxGroupId(employerTaxGroupId);
          fd = prepareCreate(fd);
          fd = create(fd);
          return fd;
        });
  }

  @Override
  public FormDataResult<EmployeeTaxGroupFormData, Boolean> isFinalized(BigDecimal employeeTaxgroupId) {
    Assertions.assertNotNull(employeeTaxgroupId);
    EmployeeTaxGroupFormData formData = new EmployeeTaxGroupFormData();
    formData.setEmployeeTaxGroupId(employeeTaxgroupId);
    formData = load(formData);
    return new FormDataResult<>(formData, formData.getStatementId() != null);
  }

  @RemoteServiceAccessDenied
  public int insert(Connection connection, BigDecimal employeeTaxGroupId, BigDecimal employerTaxGroupId, BigDecimal employeeId, BigDecimal statementId, Date start, Date end) {
    EmployeeTaxGroupFormData fd = new EmployeeTaxGroupFormData();
    fd.setEmployeeTaxGroupId(employeeTaxGroupId);
    fd.setEmployerTaxGroupId(employerTaxGroupId);
    fd.setEmployeeId(employeeId);
    fd.getPeriodBox().getFrom().setValue(start);
    fd.getPeriodBox().getTo().setValue(end);
    fd.setStatementId(statementId);
    return insert(connection, fd);
  }

  @RemoteServiceAccessDenied
  public int insert(Connection connection, EmployeeTaxGroupFormData formData) {
    return DSL.using(connection, SQLDialect.DERBY)
        .executeInsert(mapToRecord(new EmployeeTaxGroupRecord(), formData));
  }

  protected EmployeeTaxGroupRecord mapToRecord(EmployeeTaxGroupRecord rec, EmployeeTaxGroupFormData formData) {
    EmployeeTaxGroup table = EmployeeTaxGroup.EMPLOYEE_TAX_GROUP;
    return rec.with(table.EMPLOYEE_NR, formData.getEmployeeId())
        .with(table.EMPLOYEE_TAX_GROUP_NR, formData.getEmployeeTaxGroupId())
        .with(table.EMPLOYER_TAX_GROUP_NR, formData.getEmployerTaxGroupId())
        .with(table.START_DATE, formData.getPeriodBox().getFrom().getValue())
        .with(table.END_DATE, formData.getPeriodBox().getTo().getValue())
        .with(table.STATEMENT_NR, formData.getStatementId());
  }

  protected EmployeeTaxGroupFormData mapToFormData(Record rec, EmployeeTaxGroupFormData fd) {
    if (rec == null) {
      return null;
    }
    EmployeeTaxGroup eeTaxGroup = EmployeeTaxGroup.EMPLOYEE_TAX_GROUP;
    EmployerTaxGroup erTaxGroup = EmployerTaxGroup.EMPLOYER_TAX_GROUP;
    fd.setEmployeeTaxGroupId(rec.get(eeTaxGroup.EMPLOYEE_TAX_GROUP_NR));
    fd.setEmployeeId(rec.get(eeTaxGroup.EMPLOYEE_NR));
    fd.getTaxGroup().setValue(rec.get(erTaxGroup.TAX_GROUP_NR));
    fd.setEmployerTaxGroupId(rec.get(eeTaxGroup.EMPLOYER_TAX_GROUP_NR));
    fd.getPeriodBox().getFrom().setValue(rec.get(eeTaxGroup.START_DATE));
    fd.getPeriodBox().getTo().setValue(rec.get(eeTaxGroup.END_DATE));
    fd.setStatementId(rec.get(eeTaxGroup.STATEMENT_NR));
    return fd;
  }

  protected StatementBean calculateWage(BigDecimal employeeTaxGroupId) {

    PayslipSearchFormData payslipSearchData = new PayslipSearchFormData();
    payslipSearchData.setEmployeeTaxGroupId(employeeTaxGroupId);
    PayslipTableData tableData = BEANS.get(PayslipService.class).getTableData(payslipSearchData);
    ArrayList<PayslipTableRowData> rowDataList = CollectionUtility.arrayList(tableData.getRows());
    PayslipTableRowData result = new PayslipTableRowData();

    result = rowDataList.stream()
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

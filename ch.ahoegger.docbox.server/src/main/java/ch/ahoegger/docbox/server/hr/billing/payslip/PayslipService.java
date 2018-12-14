package ch.ahoegger.docbox.server.hr.billing.payslip;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.ch.ahoegger.docbox.jasper.WageReportService;
import org.ch.ahoegger.docbox.jasper.bean.ReportMonthPayslip;
import org.ch.ahoegger.docbox.server.or.app.tables.BillingCycle;
import org.ch.ahoegger.docbox.server.or.app.tables.Employee;
import org.ch.ahoegger.docbox.server.or.app.tables.EmployeeTaxGroup;
import org.ch.ahoegger.docbox.server.or.app.tables.EmployerTaxGroup;
import org.ch.ahoegger.docbox.server.or.app.tables.Payslip;
import org.ch.ahoegger.docbox.server.or.app.tables.Statement;
import org.ch.ahoegger.docbox.server.or.app.tables.records.PayslipRecord;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingStatus;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.resource.BinaryResource;
import org.eclipse.scout.rt.platform.status.IStatus;
import org.eclipse.scout.rt.platform.util.Assertions;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.server.jdbc.SQL;
import org.eclipse.scout.rt.shared.servicetunnel.RemoteServiceAccessDenied;
import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.or.definition.table.ISequenceTable;
import ch.ahoegger.docbox.server.administration.hr.billing.BillingCycleService;
import ch.ahoegger.docbox.server.document.DocumentService;
import ch.ahoegger.docbox.server.hr.billing.MonthlyReportBeanMapper;
import ch.ahoegger.docbox.server.hr.billing.WageCalculationInput;
import ch.ahoegger.docbox.server.hr.billing.WageCalculationResult;
import ch.ahoegger.docbox.server.hr.billing.WageCalculationService;
import ch.ahoegger.docbox.server.hr.employee.EmployeeService;
import ch.ahoegger.docbox.server.hr.employee.EmployeeTaxGroupService;
import ch.ahoegger.docbox.server.hr.employer.EmployerService;
import ch.ahoegger.docbox.server.hr.entity.EntityService;
import ch.ahoegger.docbox.server.hr.statement.StatementBean;
import ch.ahoegger.docbox.server.hr.statement.StatementService;
import ch.ahoegger.docbox.server.jooq.FieldValidators;
import ch.ahoegger.docbox.shared.administration.hr.billingcycle.BillingCycleFormData;
import ch.ahoegger.docbox.shared.backup.IBackupService;
import ch.ahoegger.docbox.shared.document.DocumentFormData;
import ch.ahoegger.docbox.shared.document.DocumentFormData.Partners.PartnersRowData;
import ch.ahoegger.docbox.shared.hr.billing.payslip.IPayslipService;
import ch.ahoegger.docbox.shared.hr.billing.payslip.PayslipFormData;
import ch.ahoegger.docbox.shared.hr.billing.payslip.PayslipFormData.WageBox.Entities.EntitiesRowData;
import ch.ahoegger.docbox.shared.hr.billing.payslip.PayslipSearchFormData;
import ch.ahoegger.docbox.shared.hr.billing.payslip.PayslipTableData;
import ch.ahoegger.docbox.shared.hr.billing.payslip.PayslipTableData.PayslipTableRowData;
import ch.ahoegger.docbox.shared.hr.employee.EmployeeFormData;
import ch.ahoegger.docbox.shared.hr.employee.EmployeeTaxGroupFormData;
import ch.ahoegger.docbox.shared.hr.employer.EmployerFormData;
import ch.ahoegger.docbox.shared.hr.entity.EntitySearchFormData;
import ch.ahoegger.docbox.shared.hr.entity.EntityTablePageData.EntityTableRowData;
import ch.ahoegger.docbox.shared.hr.entity.IEntityService;
import ch.ahoegger.docbox.shared.util.FormDataResult;
import ch.ahoegger.docbox.shared.util.LocalDateUtility;
import ch.ahoegger.docbox.shared.util.LocalDateUtility.LocalDateRange;

/**
 * <h3>{@link PayslipService}</h3>
 *
 * @author aho
 */
public class PayslipService implements IPayslipService {

  private static final Logger LOG = LoggerFactory.getLogger(PayslipService.class);

  @Override
  public PayslipSearchFormData loadSearch(PayslipSearchFormData searchData) {
    if (searchData.getEmployeeTaxGroupId() != null) {
      EmployeeTaxGroup eeTaxGroup = EmployeeTaxGroup.EMPLOYEE_TAX_GROUP;
      EmployerTaxGroup erTaxGroup = EmployerTaxGroup.EMPLOYER_TAX_GROUP;

      return DSL.using(SQL.getConnection(), SQLDialect.DERBY)
          .select(eeTaxGroup.EMPLOYEE_NR, erTaxGroup.TAX_GROUP_NR)
          .from(eeTaxGroup)
          .leftOuterJoin(erTaxGroup).on(eeTaxGroup.EMPLOYER_TAX_GROUP_NR.eq(erTaxGroup.EMPLOYER_TAX_GROUP_NR))
          .where(eeTaxGroup.EMPLOYEE_TAX_GROUP_NR.eq(searchData.getEmployeeTaxGroupId()))
          .fetchOne()
          .map(rec -> {
            searchData.getEmployee().setValue(rec.get(eeTaxGroup.EMPLOYEE_NR));
            searchData.getTaxGroup().setValue(rec.get(erTaxGroup.TAX_GROUP_NR));
            return searchData;
          });

    }
    return searchData;
  }

  @Override
  public PayslipTableData getTableData(PayslipSearchFormData formData) {
    Payslip payslip = Payslip.PAYSLIP;
    BillingCycle billingCycle = BillingCycle.BILLING_CYCLE;
    EmployeeTaxGroup eeTaxGroup = EmployeeTaxGroup.EMPLOYEE_TAX_GROUP;
    EmployerTaxGroup erTaxGroup = EmployerTaxGroup.EMPLOYER_TAX_GROUP;
    Statement statement = Statement.STATEMENT;

    Condition condition = DSL.trueCondition();

    // employer
    if (formData.getEmployer().getValue() != null) {
      condition = condition.and(erTaxGroup.EMPLOYER_NR.eq(formData.getEmployer().getValue()));
    }

    // employeeTaxGroup
    if (formData.getEmployeeTaxGroupId() != null) {
      condition = condition.and(eeTaxGroup.EMPLOYEE_TAX_GROUP_NR.eq(formData.getEmployeeTaxGroupId()));
    }

    // employee
    if (formData.getEmployee().getValue() != null) {
      condition = condition.and(eeTaxGroup.EMPLOYEE_NR.eq(formData.getEmployee().getValue()));
    }

    // tax group
    if (formData.getTaxGroup().getValue() != null) {
      condition = condition.and(erTaxGroup.TAX_GROUP_NR.eq(formData.getTaxGroup().getValue()));
    }
    // billing cycle
    if (formData.getBillingCycle().getValue() != null) {
      condition = condition.and(billingCycle.BILLING_CYCLE_NR.eq(formData.getBillingCycle().getValue()));
    }

    // active document (valid date)
    if (formData.getFinalzedRadioGroup().getValue() != null) {
      switch (formData.getFinalzedRadioGroup().getValue()) {
        case TRUE:
          condition = condition.and(payslip.STATEMENT_NR.isNotNull());
          break;
        case FALSE:
          condition = condition.and(payslip.STATEMENT_NR.isNull());
          break;
      }
    }

    List<PayslipTableRowData> rows = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .select(payslip.PAYSLIP_NR, payslip.BILLING_CYCLE_NR, billingCycle.START_DATE, billingCycle.END_DATE, payslip.STATEMENT_NR, payslip.EMPLOYEE_TAX_GROUP_NR,
            eeTaxGroup.EMPLOYEE_TAX_GROUP_NR, eeTaxGroup.EMPLOYEE_NR,
            statement.DOCUMENT_NR, statement.TAX_TYPE, statement.STATEMENT_DATE, statement.ACCOUNT_NUMBER, statement.HOURLY_WAGE,
            statement.SOCIAL_INSURANCE_RATE, statement.SOURCE_TAX_RATE, statement.VACATION_EXTRA_RATE, statement.WORKING_HOURS, statement.WAGE,
            statement.BRUTTO_WAGE, statement.NETTO_WAGE, statement.NETTO_WAGE_PAYOUT, statement.SOURCE_TAX, statement.SOCIAL_INSURANCE_TAX,
            statement.VACATION_EXTRA, statement.EXPENSES)
        .from(payslip)
        .leftOuterJoin(billingCycle).on(payslip.BILLING_CYCLE_NR.eq(billingCycle.BILLING_CYCLE_NR))
        .leftOuterJoin(eeTaxGroup).on(payslip.EMPLOYEE_TAX_GROUP_NR.eq(eeTaxGroup.EMPLOYEE_TAX_GROUP_NR))
        .leftOuterJoin(erTaxGroup).on(eeTaxGroup.EMPLOYER_TAX_GROUP_NR.eq(erTaxGroup.EMPLOYER_TAX_GROUP_NR))
        .leftOuterJoin(statement).on(payslip.STATEMENT_NR.eq(statement.STATEMENT_NR))
        .where(condition)
        .orderBy(billingCycle.START_DATE)
        .fetch()
        .stream()
        .map(rec -> {
          PayslipTableRowData rd = new PayslipTableRowData();
          rd.setPayslipId(rec.get(payslip.PAYSLIP_NR));
          rd.setEmployeeTaxGroup(rec.get(eeTaxGroup.EMPLOYEE_TAX_GROUP_NR));
          rd.setEmployee(rec.get(eeTaxGroup.EMPLOYEE_NR));
          rd.setBillingCycle(rec.get(payslip.BILLING_CYCLE_NR));
          rd.setPeriodFrom(rec.get(billingCycle.START_DATE));
          rd.setPeriodTo(rec.get(billingCycle.END_DATE));
          rd.setStatementId(rec.get(payslip.STATEMENT_NR));
          if (rd.getStatementId() != null) {
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
          }
          else {
            // calculate wage
            BEANS.get(StatementService.class).mapToPayslipRowData(rd,
                calculateWage(rd.getPayslipId(), rec.get(eeTaxGroup.EMPLOYEE_NR), rec.get(payslip.EMPLOYEE_TAX_GROUP_NR)));
          }

          return rd;
        })
        .collect(Collectors.toList());

    PayslipTableData pageData = new PayslipTableData();
    pageData.setRows(rows.toArray(new PayslipTableRowData[rows.size()]));
    return pageData;
  }

  @Override
  public boolean hasTableData(PayslipSearchFormData searchData) {
    return getTableData(searchData).getRowCount() > 0;
  }

  public LocalDateRange getDateRange(PayslipSearchFormData searchData) {
    return Optional.ofNullable(Arrays.asList(BEANS.get(PayslipService.class).getTableData(searchData).getRows())
        .stream()
        .map(rd -> new LocalDateUtility.LocalDateRange(LocalDateUtility.toLocalDate(rd.getPeriodFrom()), LocalDateUtility.toLocalDate(rd.getPeriodTo())))
        .reduce(new LocalDateUtility.LocalDateRange(), LocalDateUtility.DATE_RANGE_ACCUMULATOR))
        .filter(range -> range.isSet()).orElse(null);
  }

  @Override
  public PayslipFormData prepareCreate(PayslipFormData formData) {
    if (formData.getEmployeeTaxGroupId() != null) {
      EmployeeTaxGroup eeTaxGroup = EmployeeTaxGroup.EMPLOYEE_TAX_GROUP;
      EmployerTaxGroup erTaxGroup = EmployerTaxGroup.EMPLOYER_TAX_GROUP;

      return DSL.using(SQL.getConnection(), SQLDialect.DERBY)
          .select(eeTaxGroup.EMPLOYEE_NR, erTaxGroup.TAX_GROUP_NR)
          .from(eeTaxGroup)
          .leftOuterJoin(erTaxGroup).on(eeTaxGroup.EMPLOYER_TAX_GROUP_NR.eq(erTaxGroup.EMPLOYER_TAX_GROUP_NR))
          .where(eeTaxGroup.EMPLOYEE_TAX_GROUP_NR.eq(formData.getEmployeeTaxGroupId()))
          .fetchOne()
          .map(rec -> {
            formData.getEmployee().setValue(rec.get(eeTaxGroup.EMPLOYEE_NR));
            formData.getTaxGroup().setValue(rec.get(erTaxGroup.TAX_GROUP_NR));
            return formData;
          });
    }

    return formData;
  }

  @Override
  public PayslipFormData create(PayslipFormData formData) {
    if (formData.getEmployeeTaxGroupId() == null) {
      // try to resolve
      FieldValidators.notNullValidator()
          .with("employeeId", formData.getEmployee().getValue())
          .with("taxGroupId", formData.getTaxGroup().getValue())
          .validateAndThrow();
      EmployeeTaxGroup eeTaxGroup = EmployeeTaxGroup.EMPLOYEE_TAX_GROUP;
      EmployerTaxGroup erTaxGroup = EmployerTaxGroup.EMPLOYER_TAX_GROUP;

      Record1<BigDecimal> rec = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
          .select(eeTaxGroup.EMPLOYEE_TAX_GROUP_NR)
          .from(eeTaxGroup)
          .innerJoin(erTaxGroup).on(eeTaxGroup.EMPLOYER_TAX_GROUP_NR.eq(erTaxGroup.EMPLOYER_TAX_GROUP_NR))
          .where(eeTaxGroup.EMPLOYEE_NR.eq(formData.getEmployee().getValue()))
          .and(erTaxGroup.TAX_GROUP_NR.eq(formData.getTaxGroup().getValue()))
          .fetchOne();
      formData.setEmployeeTaxGroupId(rec.get(eeTaxGroup.EMPLOYEE_TAX_GROUP_NR));
    }

    Payslip payslip = Payslip.PAYSLIP;
    FieldValidators.notNullValidator()
        .with(payslip.BILLING_CYCLE_NR, formData.getBillingCycle().getValue())
        .with(payslip.EMPLOYEE_TAX_GROUP_NR, formData.getEmployeeTaxGroupId())
        .validateAndThrow();

    FormDataResult<EmployeeTaxGroupFormData, Boolean> finalizedResult = BEANS.get(EmployeeTaxGroupService.class).isFinalized(formData.getEmployeeTaxGroupId());
    if (finalizedResult.getValue()) {
      throw new VetoException(new ProcessingStatus(
          String.format("EmployeeTaxGroup is finalized!"),
          IStatus.ERROR));
    }

    formData.setPayslipId(new BigDecimal(SQL.getSequenceNextval(ISequenceTable.TABLE_NAME)));
    int rowCount = insert(formData);
    if (rowCount == 1) {
      // notify backup needed
      BEANS.get(IBackupService.class).notifyModification();
      return formData;
    }
    return null;
  }

  @Override
  public PayslipFormData load(PayslipFormData formData) {
    Payslip table = Payslip.PAYSLIP;
    EmployeeTaxGroup etg = EmployeeTaxGroup.EMPLOYEE_TAX_GROUP;
    EmployerTaxGroup erTaxGroup = EmployerTaxGroup.EMPLOYER_TAX_GROUP;
    Condition conditions = DSL.trueCondition();
    if (formData.getPayslipId() != null) {
      conditions = conditions.and(table.PAYSLIP_NR.eq(formData.getPayslipId()));
    }
    else if (formData.getBillingCycle().getValue() != null && formData.getEmployee().getValue() != null) {
      conditions = conditions.and(
          table.BILLING_CYCLE_NR.eq(formData.getBillingCycle().getValue())
              .and(etg.EMPLOYEE_NR.eq(formData.getEmployee().getValue())));
    }

    Record empRec = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .select(table.fields()).select(etg.EMPLOYEE_NR, erTaxGroup.TAX_GROUP_NR)
        .from(table)
        .leftOuterJoin(etg).on(table.EMPLOYEE_TAX_GROUP_NR.eq(etg.EMPLOYEE_TAX_GROUP_NR))
        .leftOuterJoin(erTaxGroup).on(etg.EMPLOYER_TAX_GROUP_NR.eq(erTaxGroup.EMPLOYER_TAX_GROUP_NR))
        .where(conditions).fetchOne();
    if (empRec == null) {
      return null;
    }
    fillFormData(empRec, formData);

    EntitySearchFormData entitySearchData = new EntitySearchFormData();
    entitySearchData.setPayslipId(formData.getPayslipId());
    List<EntityTableRowData> entityRows = CollectionUtility.arrayList(BEANS.get(EntityService.class).getEntityTableData(entitySearchData).getRows());
    List<EntityTableRowData> expenseEntities = entityRows.stream().filter(IEntityService.EXPENSE_FILTER).collect(Collectors.toList());
    List<EntityTableRowData> workEntities = entityRows.stream().filter(IEntityService.WORK_FILTER).collect(Collectors.toList());
    formData.getWageBox().getEntities().setRows(entityRows.stream()
        .map(row -> {
          EntitiesRowData res = new EntitiesRowData();
          res.setAmount(row.getAmount());
          res.setDate(row.getDate());
          res.setEnityId(row.getEnityId());
          res.setEntityType(row.getEntityType());
          res.setHours(row.getHours());
          res.setPayslipId(row.getPayslipId());
          res.setText(row.getText());
          return res;
        })
        .collect(Collectors.toList()).toArray(new EntitiesRowData[0]));
    StatementBean statement = new StatementBean();
    if (formData.getStatementId() != null) {
      statement.withStatementId(formData.getStatementId());
      statement = BEANS.get(StatementService.class).load(statement);
    }
    else {
      statement = calculateWage(formData.getEmployee().getValue(), workEntities, expenseEntities);
    }
    BEANS.get(StatementService.class).mapToStatementBoxData(formData.getWageBox(), statement);
    return formData;
  }

  @Override
  public PayslipFormData finalize(PayslipFormData formData) {
    Assertions.assertNotNull(formData.getPayslipId());
    if (formData.getStatementId() != null) {
      throw new VetoException(new ProcessingStatus("Payslip already finalized.", IStatus.ERROR));
    }
    Payslip payslip = Payslip.PAYSLIP;

    FieldValidators.notNullValidator()
        .with(payslip.BILLING_CYCLE_NR, formData.getBillingCycle().getValue())
        .with(payslip.EMPLOYEE_TAX_GROUP_NR, formData.getEmployeeTaxGroupId())
        .validateAndThrow();

    LocalDate statementDate = LocalDate.now();
    BillingCycleFormData billingCycleData = new BillingCycleFormData();
    billingCycleData.setBillingCycleId(formData.getBillingCycle().getValue());
    billingCycleData = BEANS.get(BillingCycleService.class).load(billingCycleData);

    EmployeeTaxGroupFormData employeeTaxGroupData = new EmployeeTaxGroupFormData();
    employeeTaxGroupData.setEmployeeTaxGroupId(formData.getEmployeeTaxGroupId());
    BEANS.get(EmployeeTaxGroupService.class).load(employeeTaxGroupData);

    // calculate wage
    EmployeeFormData employeeData = new EmployeeFormData();
    employeeData.setPartnerId(employeeTaxGroupData.getEmployeeId());
    employeeData = BEANS.get(EmployeeService.class).load(employeeData);
    Employee employee = Employee.EMPLOYEE;
    FieldValidators.notNullValidator()
        .with(employee.EMPLOYEE_NR, employeeData.getPartnerId())
        .with(employee.HOURLY_WAGE, employeeData.getEmploymentBox().getHourlyWage().getValue())
        .with(employee.REDUCED_LUNCH, employeeData.getEmploymentBox().getReducedLunch().getValue())
        .with(employee.SOCIAL_INSURANCE_RATE, employeeData.getEmploymentBox().getSocialInsuranceRate().getValue())
        .with(employee.SOURCE_TAX_RATE, employeeData.getEmploymentBox().getSourceTaxRate().getValue())
        .with(employee.TAX_TYPE, employeeData.getEmploymentBox().getTaxType().getValue())
        .with(employee.VACATION_EXTRA_RATE, employeeData.getEmploymentBox().getVacationExtraRate().getValue())
        .validateAndThrow();

    EmployerFormData employerFormData = new EmployerFormData();
    employerFormData.setEmployerId(employeeData.getEmployer().getValue());
    employerFormData = BEANS.get(EmployerService.class).load(employerFormData);

    EntitySearchFormData entitySearchData = new EntitySearchFormData();
    entitySearchData.setPayslipId(formData.getPayslipId());
    List<EntityTableRowData> entityRows = CollectionUtility.arrayList(BEANS.get(EntityService.class).getEntityTableData(entitySearchData).getRows());
    List<EntityTableRowData> expenseEntities = entityRows.stream().filter(IEntityService.EXPENSE_FILTER).collect(Collectors.toList());
    List<EntityTableRowData> workEntities = entityRows.stream().filter(IEntityService.WORK_FILTER).collect(Collectors.toList());
    WageCalculationInput calculationInput = new WageCalculationInput()
        .withExpenseEntities(expenseEntities)
        .withHourlyWage(employeeData.getEmploymentBox().getHourlyWage().getValue())
        .withSocialInsuranceRate(employeeData.getEmploymentBox().getSocialInsuranceRate().getValue())
        .withSourceTaxRate(employeeData.getEmploymentBox().getSourceTaxRate().getValue())
        .withTaxType(employeeData.getEmploymentBox().getTaxType().getValue())
        .withVacationExtraRate(employeeData.getEmploymentBox().getVacationExtraRate().getValue())
        .withWorkEntities(workEntities);
    WageCalculationResult calculationResult = BEANS.get(WageCalculationService.class).calculateWage(calculationInput);

    // create document
    ReportMonthPayslip reportBean = BEANS.get(MonthlyReportBeanMapper.class).map(statementDate, employerFormData, employeeData, formData, calculationInput, calculationResult, billingCycleData, workEntities, expenseEntities);
    byte[] docContent = BEANS.get(WageReportService.class).createMonthlyReport(reportBean);

    DocumentFormData documentData = new DocumentFormData();
    DocumentService documentService = BEANS.get(DocumentService.class);
    documentData = documentService.prepareCreate(documentData);
    documentData.getDocumentDate().setValue(LocalDateUtility.today());
    documentData.getDocument().setValue(new BinaryResource("payslip.pdf", docContent));
    PartnersRowData partnerRow = documentData.getPartners().addRow();
    partnerRow.setPartner(employeeTaxGroupData.getEmployeeId());
    documentData.getAbstract().setValue(formData.getPayslipDocumentAbstract().getValue());
    documentData.getCategoriesBox().setValue(CollectionUtility.emptyTreeSet());
    documentData = documentService.create(documentData);

    // create statement
    StatementBean statementBean = new StatementBean();
    statementBean.withStatementDate(LocalDateUtility.toDate(statementDate));
    statementBean = BEANS.get(EmployeeService.class).mapToStatementBean(employeeData, statementBean);
    statementBean = BEANS.get(WageCalculationService.class).mapToStatementBean(calculationResult, statementBean);
    statementBean.withDocumentId(documentData.getDocumentId());

    statementBean = BEANS.get(StatementService.class).create(statementBean);
    formData.setStatementId(statementBean.getStatementId());

    // update payslip
    PayslipRecord record = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .fetchOne(payslip, payslip.PAYSLIP_NR.eq(formData.getPayslipId()));
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
  public PayslipFormData unfinalize(BigDecimal payslipId) {
    Assertions.assertNotNull(payslipId);
    PayslipFormData formData = new PayslipFormData();
    formData.setPayslipId(payslipId);
    formData = load(formData);

    // delete statement
    BEANS.get(StatementService.class).delete(formData.getStatementId());

    // update payslip
    Payslip payslip = Payslip.PAYSLIP;
    formData.setStatementId(null);
    PayslipRecord record = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .fetchOne(payslip, payslip.PAYSLIP_NR.eq(formData.getPayslipId()));
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

  @Override
  public boolean delete(BigDecimal payslipId) {
    Assertions.assertNotNull(payslipId);
    PayslipFormData formData = new PayslipFormData();
    formData.setPayslipId(payslipId);
    formData = load(formData);
    if (formData.getStatementId() != null) {
      throw new VetoException(new ProcessingStatus(
          String.format("Finalized payslips can not be deleted!"),
          IStatus.ERROR));
    }
    EntitySearchFormData entitySearchData = new EntitySearchFormData();
    entitySearchData.setPayslipId(payslipId);
    if (BEANS.get(EntityService.class).getEntityTableData(entitySearchData).getRowCount() > 0) {
      throw new VetoException(new ProcessingStatus(
          String.format("Payslips with entities can not be deleted!"),
          IStatus.ERROR));
    }

    Payslip e = Payslip.PAYSLIP;
    PayslipRecord rec = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .fetchOne(e, e.PAYSLIP_NR.eq(payslipId));
    if (rec == null) {
      LOG.warn("Try to delete not existing record with id '{}'!", payslipId);
      return false;
    }
    if (rec.delete() != 1) {
      LOG.error("Could not delete record with id '{}'!", payslipId);
      return false;
    }

    // notify backup needed
    BEANS.get(IBackupService.class).notifyModification();
    return true;
  }

  @Override
  public boolean finalized(PayslipSearchFormData payslipSearchFormData) {
    List<PayslipTableRowData> rowList = Arrays.asList(getTableData(payslipSearchFormData).getRows());
    if (rowList.size() == 0) {
      return false;
    }
    return rowList
        .stream()
        .filter(row -> row.getStatementId() == null)
        .count() == 0;
  }

  @Override
  public FormDataResult<PayslipFormData, Boolean> isFinalized(BigDecimal payslipId) {
    Assertions.assertNotNull(payslipId);
    PayslipFormData formData = new PayslipFormData();
    formData.setPayslipId(payslipId);
    formData = load(formData);
    return new FormDataResult<>(formData, formData.getStatementId() != null);
  }

  @RemoteServiceAccessDenied
  public int insert(Connection connection, BigDecimal payslipId, BigDecimal billingCycleId, BigDecimal employeeTaxGroupId, BigDecimal statementId) {
    PayslipFormData fd = new PayslipFormData();
    fd.setPayslipId(payslipId);
    fd.getBillingCycle().setValue(billingCycleId);
    fd.setEmployeeTaxGroupId(employeeTaxGroupId);
    fd.setStatementId(statementId);
    return insert(fd);
  }

  @RemoteServiceAccessDenied
  public int insert(PayslipFormData formData) {
    Payslip table = Payslip.PAYSLIP;
    return mapToRecord(DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .newRecord(table), formData).insert();

  }

  protected PayslipRecord mapToRecord(PayslipRecord rec, PayslipFormData formData) {
    EmployeeTaxGroupFormData eeTaxGroupData = new EmployeeTaxGroupFormData();
    eeTaxGroupData.setEmployeeTaxGroupId(formData.getEmployeeTaxGroupId());
    eeTaxGroupData = BEANS.get(EmployeeTaxGroupService.class).load(eeTaxGroupData);

    Payslip table = Payslip.PAYSLIP;
    return rec
        .with(table.PAYSLIP_NR, formData.getPayslipId())
        .with(table.BILLING_CYCLE_NR, formData.getBillingCycle().getValue())
        .with(table.EMPLOYEE_TAX_GROUP_NR, eeTaxGroupData.getEmployeeTaxGroupId())
        .with(table.STATEMENT_NR, formData.getStatementId());
  }

  protected PayslipFormData fillFormData(Record rec, PayslipFormData fd) {
    if (rec == null) {
      return null;
    }
    Payslip payslip = Payslip.PAYSLIP;
    EmployeeTaxGroup eeTaxGroup = EmployeeTaxGroup.EMPLOYEE_TAX_GROUP;
    EmployerTaxGroup erTaxGroup = EmployerTaxGroup.EMPLOYER_TAX_GROUP;
    fd.setEmployeeTaxGroupId(rec.get(payslip.EMPLOYEE_TAX_GROUP_NR));
    fd.getTaxGroup().setValue(rec.get(erTaxGroup.TAX_GROUP_NR));
    fd.getBillingCycle().setValue(payslip.BILLING_CYCLE_NR.get(rec));
    fd.getEmployee().setValue(eeTaxGroup.EMPLOYEE_NR.get(rec));
    fd.setPayslipId(payslip.PAYSLIP_NR.get(rec));
    fd.setStatementId(payslip.STATEMENT_NR.get(rec));
    return fd;
  }

  protected StatementBean calculateWage(BigDecimal payslipId, BigDecimal employeeId, BigDecimal employeeTaxGroupId) {
    EntitySearchFormData entitySearchData = new EntitySearchFormData();
    entitySearchData.setPayslipId(payslipId);
    List<EntityTableRowData> entityRows = CollectionUtility.arrayList(BEANS.get(EntityService.class).getEntityTableData(entitySearchData).getRows());
    List<EntityTableRowData> expenseEntities = entityRows.stream().filter(IEntityService.EXPENSE_FILTER).collect(Collectors.toList());
    List<EntityTableRowData> workEntities = entityRows.stream().filter(IEntityService.WORK_FILTER).collect(Collectors.toList());
    return calculateWage(employeeId, workEntities, expenseEntities);
  }

  protected StatementBean calculateWage(BigDecimal employeeId, List<EntityTableRowData> workEntities, List<EntityTableRowData> expenseEntities) {
    EmployeeFormData employeeData = new EmployeeFormData();
    employeeData.setPartnerId(employeeId);
    employeeData = BEANS.get(EmployeeService.class).load(employeeData);
    WageCalculationResult wage = BEANS.get(WageCalculationService.class).calculateWage(
        new WageCalculationInput()
            .withExpenseEntities(expenseEntities)
            .withHourlyWage(employeeData.getEmploymentBox().getHourlyWage().getValue())
            .withSocialInsuranceRate(employeeData.getEmploymentBox().getSocialInsuranceRate().getValue())
            .withSourceTaxRate(employeeData.getEmploymentBox().getSourceTaxRate().getValue())
            .withTaxType(employeeData.getEmploymentBox().getTaxType().getValue())
            .withVacationExtraRate(employeeData.getEmploymentBox().getVacationExtraRate().getValue())
            .withWorkEntities(workEntities));

    StatementBean result = new StatementBean();
    result = BEANS.get(WageCalculationService.class).mapToStatementBean(wage, result);
    result = BEANS.get(EmployeeService.class).mapToStatementBean(employeeData, result);
    return result;
  }
}

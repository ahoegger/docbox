package ch.ahoegger.docbox.server.administration.hr.billing;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.ch.ahoegger.docbox.server.or.app.tables.BillingCycle;
import org.ch.ahoegger.docbox.server.or.app.tables.Payslip;
import org.ch.ahoegger.docbox.server.or.app.tables.records.BillingCycleRecord;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingStatus;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.status.IStatus;
import org.eclipse.scout.rt.platform.status.MultiStatus;
import org.eclipse.scout.rt.platform.status.Status;
import org.eclipse.scout.rt.platform.util.Assertions;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.platform.util.TriState;
import org.eclipse.scout.rt.server.jdbc.SQL;
import org.eclipse.scout.rt.shared.servicetunnel.RemoteServiceAccessDenied;
import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.or.definition.table.ISequenceTable;
import ch.ahoegger.docbox.server.administration.taxgroup.TaxGroupLookupService;
import ch.ahoegger.docbox.server.administration.taxgroup.TaxGroupService;
import ch.ahoegger.docbox.server.hr.billing.payslip.PayslipService;
import ch.ahoegger.docbox.server.hr.employee.EmployeeService;
import ch.ahoegger.docbox.server.hr.employee.EmployeeTaxGroupService;
import ch.ahoegger.docbox.server.hr.employer.EmployerTaxGroupService;
import ch.ahoegger.docbox.server.jooq.FieldValidators;
import ch.ahoegger.docbox.server.partner.PartnerLookupService;
import ch.ahoegger.docbox.shared.administration.hr.billing.IBillingCycleService;
import ch.ahoegger.docbox.shared.administration.hr.billingcycle.BillingCycleFormData;
import ch.ahoegger.docbox.shared.administration.hr.billingcycle.BillingCycleSearchFormData;
import ch.ahoegger.docbox.shared.administration.hr.billingcycle.BillingCycleTablePageData;
import ch.ahoegger.docbox.shared.administration.hr.billingcycle.BillingCycleTablePageData.BillingCycleTableRowData;
import ch.ahoegger.docbox.shared.administration.hr.taxgroup.TaxGroupFormData;
import ch.ahoegger.docbox.shared.backup.IBackupService;
import ch.ahoegger.docbox.shared.hr.billing.payslip.PayslipFormData;
import ch.ahoegger.docbox.shared.hr.billing.payslip.PayslipSearchFormData;
import ch.ahoegger.docbox.shared.hr.employee.EmployeeFormData;
import ch.ahoegger.docbox.shared.hr.employee.EmployeeTaxGroupFormData;
import ch.ahoegger.docbox.shared.hr.employee.EmployeeTaxGroupSearchFormData;
import ch.ahoegger.docbox.shared.hr.employer.EmployerTaxGroupFormData;
import ch.ahoegger.docbox.shared.util.LocalDateUtility;

/**
 * <h3>{@link BillingCycleService}</h3>
 *
 * @author aho
 */
public class BillingCycleService implements IBillingCycleService {
  private static final Logger LOG = LoggerFactory.getLogger(BillingCycleService.class);

  @Override
  public BillingCycleTablePageData getTableData(BillingCycleSearchFormData formData) {
    BillingCycle billingCycle = BillingCycle.BILLING_CYCLE;

    Condition condition = DSL.trueCondition();

    // search tax group
    if (formData.getTaxGroup().getValue() != null) {
      condition = condition.and(billingCycle.TAX_GROUP_NR.eq(formData.getTaxGroup().getValue()));
    }

    List<BillingCycleTableRowData> rows = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .select(billingCycle.BILLING_CYCLE_NR, billingCycle.NAME, billingCycle.START_DATE, billingCycle.END_DATE)
        .from(billingCycle)
        .where(condition)
        .orderBy(billingCycle.START_DATE)
        .fetch()
        .stream()
        .map(rec -> {
          BillingCycleTableRowData rd = new BillingCycleTableRowData();
          rd.setId(rec.get(billingCycle.BILLING_CYCLE_NR));
          rd.setName(rec.get(billingCycle.NAME));
          rd.setPeriodFrom(rec.get(billingCycle.START_DATE));
          rd.setPeriodTo(rec.get(billingCycle.END_DATE));
          return rd;
        })
        .collect(Collectors.toList());

    BillingCycleTablePageData pageData = new BillingCycleTablePageData();
    pageData.setRows(rows.toArray(new BillingCycleTableRowData[0]));
    return pageData;
  }

  @Override
  public IStatus validate(BillingCycleFormData formData) {
    MultiStatus status = new MultiStatus();
    validatePeriodBox(formData, status, false);
    return status;
  }

  @Override
  public IStatus validateNew(BillingCycleFormData formData) {
    MultiStatus status = new MultiStatus();
    validatePeriodBox(formData, status, false);
    validateLinkings(formData, status, false);
    return status;
  }

  @Override
  public BillingCycleFormData prepareCreate(BillingCycleFormData formData) {
    if (formData.getTaxGroup().getValue() != null) {
      TaxGroupFormData taxGroupData = new TaxGroupFormData();
      taxGroupData.setTaxGroupId(formData.getTaxGroup().getValue());
      taxGroupData = BEANS.get(TaxGroupService.class).load(taxGroupData);

      BillingCycleSearchFormData billingCycleSearchData = new BillingCycleSearchFormData();
      billingCycleSearchData.getTaxGroup().setValue(taxGroupData.getTaxGroupId());
      LocalDate billingCycleStartDate = Arrays.asList(getTableData(billingCycleSearchData).getRows())
          .stream()
          .filter(row -> row.getPeriodFrom() != null)
          .map(row -> LocalDateUtility.toLocalDate(row.getPeriodFrom()))
          .max((d1, d2) -> d1.compareTo(d2))
          .map(d -> d.plusMonths(1).withDayOfMonth(1))
          .orElse(LocalDateUtility.toLocalDate(taxGroupData.getPeriodBox().getFrom().getValue()).withDayOfMonth(1));

      formData.getPeriodBox().getFrom().setValue(LocalDateUtility.toDate(billingCycleStartDate));
      formData.getPeriodBox().getTo().setValue(LocalDateUtility.toDate(billingCycleStartDate.with(TemporalAdjusters.lastDayOfMonth())));
    }
    return formData;
  }

  @Override
  public BillingCycleFormData create(BillingCycleFormData formData) {
    Assertions.assertNull(formData.getBillingCycleId());

    validatePeriodBox(formData, new MultiStatus(), true);
    validateLinkings(formData, new MultiStatus(), true);
    formData.setBillingCycleId(new BigDecimal(SQL.getSequenceNextval(ISequenceTable.TABLE_NAME)));
    int rowCount = insert(SQL.getConnection(), formData);
    if (rowCount != 1) {
      throw new VetoException("Not able to create billing cycle.");
    }

    // LINKING
    DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM yyyy", LocalDateUtility.DE_CH);

    Optional.ofNullable(formData.getLinkingBox().getEmployersBox().getValue()).orElse(new HashSet<>())
        // map to employerTaxGroup
        .forEach(employerId -> {
          System.out.println("Update for emp: " + employerId);
          EmployerTaxGroupFormData erTaxGroupData = BEANS.get(EmployerTaxGroupService.class).getOrCreate(employerId, formData.getTaxGroup().getValue());
          Optional.ofNullable(formData.getLinkingBox().getEmployeesBox().getValue()).orElse(new HashSet<>())
              .stream()
              .filter(eeId -> {
                EmployeeFormData employeeData = new EmployeeFormData();
                employeeData.setPartnerId(eeId);
                employeeData = BEANS.get(EmployeeService.class).load(employeeData);
                return erTaxGroupData.getEmployerId().compareTo(employeeData.getEmployer().getValue()) == 0;
              }).forEach(eeId -> {
                // create or get eeTaxGroup
                EmployeeTaxGroupFormData eeTaxGroupData = BEANS.get(EmployeeTaxGroupService.class).getOrCreate(eeId, erTaxGroupData.getEmployerTaxGroupId());
                // create payslip
                PayslipFormData payslipData = new PayslipFormData();
                payslipData.setEmployeeTaxGroupId(eeTaxGroupData.getEmployeeTaxGroupId());
                payslipData.getTitle().setValue(monthFormatter.format(LocalDateUtility.toLocalDate(formData.getPeriodBox().getFrom().getValue())));
                payslipData.getBillingCycle().setValue(formData.getBillingCycleId());
                payslipData = BEANS.get(PayslipService.class).prepareCreate(payslipData);
                payslipData = BEANS.get(PayslipService.class).create(payslipData);

              });
        });

    // notify backup needed
    BEANS.get(IBackupService.class).notifyModification();
    return formData;

  }

  @Override
  public BillingCycleFormData load(BillingCycleFormData formData) {
    Assertions.assertNotNull(formData.getBillingCycleId());
    BillingCycle table = BillingCycle.BILLING_CYCLE;
    return mapToFormData(formData, DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .fetchOne(table, table.BILLING_CYCLE_NR.eq(formData.getBillingCycleId())));
  }

  @Override
  public BillingCycleFormData store(BillingCycleFormData formData) {
    Assertions.assertNotNull(formData.getBillingCycleId());
    validatePeriodBox(formData, new MultiStatus(), true);
    BillingCycle table = BillingCycle.BILLING_CYCLE;
    BillingCycleRecord record = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .fetchOne(table, table.BILLING_CYCLE_NR.eq(formData.getBillingCycleId()));
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
  public boolean delete(BigDecimal billingCycleId) {
    PayslipSearchFormData searchData = new PayslipSearchFormData();
    searchData.getBillingCycle().setValue(billingCycleId);
    if (BEANS.get(PayslipService.class).hasTableData(searchData)) {
      throw new VetoException("Can only delete empty billing cycles");
    }

    BillingCycle billingCycle = BillingCycle.BILLING_CYCLE;
    BillingCycleRecord rec = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .fetchOne(billingCycle, billingCycle.BILLING_CYCLE_NR.eq(billingCycleId));
    if (rec == null) {
      LOG.warn("Try to delete not existing record with id '{}'!", billingCycleId);
      return false;
    }
    if (rec.delete() != 1) {
      LOG.error("Could not delete record with id '{}'!", billingCycleId);
      return false;
    }
    return true;
  }

  public BillingCycleFormData getBillingCycleByPayslipId(BigDecimal payslipId) {
    BillingCycleFormData formData = new BillingCycleFormData();
    BillingCycle billingCycle = BillingCycle.BILLING_CYCLE;
    Payslip payslip = Payslip.PAYSLIP;
    return mapToFormData(formData, DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .select(billingCycle.fields())
        .from(billingCycle)
        .innerJoin(payslip).on(billingCycle.BILLING_CYCLE_NR.eq(payslip.BILLING_CYCLE_NR))
        .where(payslip.PAYSLIP_NR.eq(payslipId)).fetchOne());
  }

  @RemoteServiceAccessDenied
  public int insert(Connection connection, BigDecimal billingCylcleId, BigDecimal taxGroupId, String name, Date start, Date end) {
    BillingCycleFormData fd = new BillingCycleFormData();
    fd.setBillingCycleId(billingCylcleId);
    fd.getTaxGroup().setValue(taxGroupId);
    fd.getName().setValue(name);
    fd.getPeriodBox().getFrom().setValue(start);
    fd.getPeriodBox().getTo().setValue(end);
    return insert(connection, fd);
  }

  @RemoteServiceAccessDenied
  public int insert(Connection connection, BillingCycleFormData formData) {
    BillingCycle table = BillingCycle.BILLING_CYCLE;
    return mapToRecord(DSL.using(connection, SQLDialect.DERBY)
        .newRecord(table), formData).insert();

  }

  protected void validateLinkings(BillingCycleFormData formData, MultiStatus statusCollector, boolean throwVetoException) {

    EmployeeTaxGroupSearchFormData searchData = new EmployeeTaxGroupSearchFormData();
    searchData.getTaxGroup().setValue(formData.getTaxGroup().getValue());
    searchData.setEmployeeIds(Optional.ofNullable(formData.getLinkingBox().getEmployeesBox().getValue()).orElse(CollectionUtility.emptyHashSet()));
    searchData.getFinalizedRadioGroup().setValue(TriState.TRUE);
    CollectionUtility.arrayList(BEANS.get(EmployeeTaxGroupService.class).getTableData(searchData).getRows())
        .stream()
        .map(row -> new Status(
            new StringBuilder()
                .append("Tax group '")
                .append(BEANS.get(TaxGroupLookupService.class).getTaxGroupName(row.getTaxGroup()))
                .append("' of employee '")
                .append(BEANS.get(PartnerLookupService.class).getName(row.getEmployee()))
                .append("' is already finalized")
                .toString(),
            IStatus.ERROR))
        .reduce(statusCollector, (acc, st) -> {
          ((MultiStatus) acc).add(st);
          return acc;
        });
  }

  protected void validatePeriodBox(BillingCycleFormData formData, MultiStatus statusCollector, boolean throwVetoException) {
    BillingCycle billingCycle = BillingCycle.BILLING_CYCLE;
    if (!FieldValidators.notNullValidator()
        .with(billingCycle.START_DATE, formData.getPeriodBox().getFrom().getValue())
        .with(billingCycle.END_DATE, formData.getPeriodBox().getTo().getValue())
        .with(billingCycle.TAX_GROUP_NR, formData.getTaxGroup().getValue())
        .validate().isOK()) {
      return;
    }
    TaxGroupFormData taxGroupData = new TaxGroupFormData();
    taxGroupData.setTaxGroupId(formData.getTaxGroup().getValue());
    taxGroupData = BEANS.get(TaxGroupService.class).load(taxGroupData);

    // validate tax group date range
    if (!LocalDateUtility.isBetweenOrEqual(taxGroupData.getPeriodBox().getFrom().getValue(), taxGroupData.getPeriodBox().getTo().getValue(), formData.getPeriodBox().getFrom().getValue()) ||
        !LocalDateUtility.isBetweenOrEqual(taxGroupData.getPeriodBox().getFrom().getValue(), taxGroupData.getPeriodBox().getTo().getValue(), formData.getPeriodBox().getTo().getValue())) {
      IStatus status = new Status("Date period not in range of tax group date range.", IStatus.ERROR);
      statusCollector.add(status);
      if (throwVetoException) {
        throw new VetoException(new ProcessingStatus(status));
      }
    }
    Condition conditions = DSL.trueCondition();
    if (formData.getBillingCycleId() != null) {
      conditions = conditions.and(billingCycle.BILLING_CYCLE_NR.ne(formData.getBillingCycleId()));
    }

    Result<?> result = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .select(billingCycle.NAME, billingCycle.START_DATE, billingCycle.END_DATE)
        .from(billingCycle)
        .where(conditions)
        .and(DSL.or(billingCycle.START_DATE.le(formData.getPeriodBox().getFrom().getValue()).and(billingCycle.END_DATE.ge(formData.getPeriodBox().getFrom().getValue())),
            billingCycle.START_DATE.le(formData.getPeriodBox().getTo().getValue()).and(billingCycle.END_DATE.ge(formData.getPeriodBox().getTo().getValue()))))
        .fetch();
    if (result.size() > 0) {
      IStatus status = new Status("Date period overlapping with billing cylce '" + result.get(0).get(billingCycle.NAME) + "'", IStatus.ERROR);
      statusCollector.add(status);
      if (throwVetoException) {
        throw new VetoException(new ProcessingStatus(status));
      }
    }
  }

  protected BillingCycleRecord mapToRecord(BillingCycleRecord rec, BillingCycleFormData formData) {
    BillingCycle table = BillingCycle.BILLING_CYCLE;
    return rec
        .with(table.BILLING_CYCLE_NR, formData.getBillingCycleId())
        .with(table.TAX_GROUP_NR, formData.getTaxGroup().getValue())
        .with(table.NAME, formData.getName().getValue())
        .with(table.START_DATE, formData.getPeriodBox().getFrom().getValue())
        .with(table.END_DATE, formData.getPeriodBox().getTo().getValue());
  }

  protected BillingCycleFormData mapToFormData(BillingCycleFormData formData, Record rec) {
    if (rec == null) {
      return null;
    }
    BillingCycle table = BillingCycle.BILLING_CYCLE;
    formData.setBillingCycleId(rec.get(table.BILLING_CYCLE_NR));
    formData.getTaxGroup().setValue(rec.get(table.TAX_GROUP_NR));
    formData.getName().setValue(rec.get(table.NAME));
    formData.getPeriodBox().getFrom().setValue(rec.get(table.START_DATE));
    formData.getPeriodBox().getTo().setValue(rec.get(table.END_DATE));
    return formData;
  }
}

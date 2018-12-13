package ch.ahoegger.docbox.server.administration.taxgroup;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.ch.ahoegger.docbox.server.or.app.tables.TaxGroup;
import org.ch.ahoegger.docbox.server.or.app.tables.records.TaxGroupRecord;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingStatus;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.status.IStatus;
import org.eclipse.scout.rt.platform.status.Status;
import org.eclipse.scout.rt.platform.util.Assertions;
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
import ch.ahoegger.docbox.server.hr.employee.EmployeeService;
import ch.ahoegger.docbox.server.hr.employee.EmployeeTaxGroupService;
import ch.ahoegger.docbox.server.hr.employer.EmployerTaxGroupService;
import ch.ahoegger.docbox.server.jooq.FieldValidators;
import ch.ahoegger.docbox.shared.administration.hr.taxgroup.TaxGroupFormData;
import ch.ahoegger.docbox.shared.administration.hr.taxgroup.TaxGroupSearchFormData;
import ch.ahoegger.docbox.shared.administration.hr.taxgroup.TaxGroupTablePageData;
import ch.ahoegger.docbox.shared.administration.hr.taxgroup.TaxGroupTablePageData.TaxGroupTableRowData;
import ch.ahoegger.docbox.shared.administration.taxgroup.ITaxGroupService;
import ch.ahoegger.docbox.shared.backup.IBackupService;
import ch.ahoegger.docbox.shared.hr.employee.EmployeeFormData;
import ch.ahoegger.docbox.shared.hr.employer.EmployerTaxGroupFormData;
import ch.ahoegger.docbox.shared.hr.employer.EmployerTaxGroupSearchFormData;
import ch.ahoegger.docbox.shared.util.LocalDateUtility;

public class TaxGroupService implements ITaxGroupService {
  private static final Logger LOG = LoggerFactory.getLogger(TaxGroupService.class);

  @Override
  public TaxGroupTablePageData getTaxGroupTableData(TaxGroupSearchFormData formData) {

    TaxGroup tg = TaxGroup.TAX_GROUP.as("TG");

    Condition condition = DSL.trueCondition();

    // startDate
    if (formData.getStartDateFrom().getValue() != null) {
      condition = condition.and(tg.START_DATE.ge(formData.getStartDateFrom().getValue()));
    }
    if (formData.getStartDateTo().getValue() != null) {
      condition = condition.and(tg.START_DATE.le(formData.getStartDateTo().getValue()));
    }
    // endDate
    if (formData.getEndDateFrom().getValue() != null) {
      condition = condition.and(tg.END_DATE.ge(formData.getEndDateFrom().getValue()));
    }
    if (formData.getEndDateTo().getValue() != null) {
      condition = condition.and(tg.END_DATE.le(formData.getEndDateTo().getValue()));
    }

    List<TaxGroupTableRowData> rows = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .select(tg.TAX_GROUP_NR, tg.NAME, tg.START_DATE, tg.END_DATE)
        .from(tg)
        .where(condition)
        .orderBy(tg.START_DATE)
        .fetch()
        .stream()
        .map(rec -> {
          TaxGroupTableRowData rd = new TaxGroupTableRowData();
          rd.setTaxGroupId(rec.get(tg.TAX_GROUP_NR));
          rd.setName(rec.get(tg.NAME));
          rd.setStartDate(rec.get(tg.START_DATE));
          rd.setEndDate(rec.get(tg.END_DATE));
          return rd;
        })
        .collect(Collectors.toList());

    TaxGroupTablePageData pageData = new TaxGroupTablePageData();
    pageData.setRows(rows.toArray(new TaxGroupTableRowData[0]));
    return pageData;
  }

  @Override
  public TaxGroupFormData prepareCreate(TaxGroupFormData formData) {
    LocalDate startDate = Arrays.asList(getTaxGroupTableData(new TaxGroupSearchFormData()).getRows())
        .stream()
        .map(row -> row.getStartDate())
        .filter(date -> date != null)
        .map(d -> LocalDateUtility.toLocalDate(d))
        .max((d1, d2) -> d1.compareTo(d2))
        .map(d -> d.plusYears(1).withDayOfYear(1))
        .orElse(LocalDate.now().withDayOfYear(1));

    formData.getPeriodBox().getFrom().setValue(LocalDateUtility.toDate(startDate));
    formData.getPeriodBox().getTo().setValue(LocalDateUtility.toDate(startDate.with(TemporalAdjusters.lastDayOfYear())));
    return formData;
  }

  @Override
  public TaxGroupFormData create(TaxGroupFormData formData) {
    validate(formData, true);
    formData.setTaxGroupId(new BigDecimal(SQL.getSequenceNextval(ISequenceTable.TABLE_NAME)));

    TaxGroup taxGroup = TaxGroup.TAX_GROUP;
    FieldValidators.notNullValidator()
        .with(taxGroup.NAME, formData.getName().getValue())
        .with(taxGroup.START_DATE, formData.getPeriodBox().getFrom().getValue())
        .with(taxGroup.END_DATE, formData.getPeriodBox().getTo().getValue())
        .validateAndThrow();

    TaxGroupRecord newRecord = mapToRecord(taxGroup.newRecord(), formData);
    int rowCount = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .executeInsert(newRecord);

    if (rowCount != 1) {
      throw new VetoException("Not able to create taxGroup.");
    }

    // LINKING

    Optional.ofNullable(formData.getLinkingBox().getEmployersBox().getValue()).orElse(new HashSet<>())
        // map to employerTaxGroup
        .forEach(employerId -> {
          System.out.println("Update for emp: " + employerId);
          EmployerTaxGroupFormData erTaxGroupData = BEANS.get(EmployerTaxGroupService.class).getOrCreate(employerId, formData.getTaxGroupId());
          Optional.ofNullable(formData.getLinkingBox().getEmployeesBox().getValue()).orElse(new HashSet<>())
              .stream()
              .filter(eeId -> {
                EmployeeFormData employeeData = new EmployeeFormData();
                employeeData.setPartnerId(eeId);
                employeeData = BEANS.get(EmployeeService.class).load(employeeData);
                return erTaxGroupData.getEmployerId().compareTo(employeeData.getEmployer().getValue()) == 0;
              }).forEach(eeId -> {
                // create or get eeTaxGroup
                BEANS.get(EmployeeTaxGroupService.class).getOrCreate(eeId, erTaxGroupData.getEmployerTaxGroupId());

              });
        });

    // notify backup needed
    BEANS.get(IBackupService.class).notifyModification();
    return formData;
  }

  @Override
  public TaxGroupFormData load(TaxGroupFormData formData) {
    Assertions.assertNotNull(formData.getTaxGroupId());

    TaxGroup taxGroup = TaxGroup.TAX_GROUP;
    Record empRec = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .select(taxGroup.fields())
        .from(taxGroup)
        .where(taxGroup.TAX_GROUP_NR.eq(formData.getTaxGroupId())).fetchOne();

    mapToFormData(empRec, formData);
    return formData;
  }

  @Override
  public TaxGroupFormData store(TaxGroupFormData formData) {
    validate(formData, true);
    TaxGroup taxGroup = TaxGroup.TAX_GROUP;

    EmployerTaxGroupSearchFormData searchData = new EmployerTaxGroupSearchFormData();
    searchData.getTaxGroup().setValue(formData.getTaxGroupId());
    if (BEANS.get(EmployerTaxGroupService.class).hasTableData(searchData)) {
      throw new VetoException("Not allowed to update tax group alread linked to employers");
    }

    FieldValidators.notNullValidator()
        .with(taxGroup.NAME, formData.getName().getValue())
        .with(taxGroup.START_DATE, formData.getPeriodBox().getFrom().getValue())
        .with(taxGroup.END_DATE, formData.getPeriodBox().getTo().getValue())
        .validateAndThrow();

    TaxGroupRecord rec = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .fetchOne(taxGroup, taxGroup.TAX_GROUP_NR.eq(formData.getTaxGroupId()));

    FieldValidators.unmodifiableValidator()
        .with(taxGroup.TAX_GROUP_NR, formData.getTaxGroupId())
        .validateAndThrow(rec);

    rec = mapToRecord(rec, formData);

    if (rec == null) {
      LOG.warn("Try to update not existing record with id '{}'!", formData.getTaxGroupId());
      return null;
    }
    if (rec.update() != 1) {
      LOG.error("Could not update record with id '{}'!", formData.getTaxGroupId());
      return null;
    }
    // notify backup needed
    BEANS.get(IBackupService.class).notifyModification();

    return formData;
  }

  @Override
  public boolean delete(BigDecimal selectedValue) {
    EmployerTaxGroupSearchFormData searchData = new EmployerTaxGroupSearchFormData();
    searchData.getTaxGroup().setValue(selectedValue);
    if (BEANS.get(EmployerTaxGroupService.class).hasTableData(searchData)) {
      throw new VetoException("Can only delete empty TaxGroups");
    }

    TaxGroup taxGroup = TaxGroup.TAX_GROUP;
    TaxGroupRecord rec = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .fetchOne(taxGroup, taxGroup.TAX_GROUP_NR.eq(selectedValue));
    if (rec == null) {
      LOG.warn("Try to delete not existing record with id '{}'!", selectedValue);
      return false;
    }
    if (rec.delete() != 1) {
      LOG.error("Could not delete record with id '{}'!", selectedValue);
      return false;
    }
    return true;
  }

  @Override
  public IStatus validate(TaxGroupFormData formData) {
    return validate(formData, false);
  }

  public IStatus validate(TaxGroupFormData formData, boolean throwException) {
    TaxGroup taxGroup = TaxGroup.TAX_GROUP;
    if (!FieldValidators.notNullValidator()
        .with(taxGroup.START_DATE, formData.getPeriodBox().getFrom().getValue())
        .with(taxGroup.END_DATE, formData.getPeriodBox().getTo().getValue())
        .validate().isOK()) {
      return Status.OK_STATUS;
    }

    Condition conditions = DSL.trueCondition();
    if (formData.getTaxGroupId() != null) {
      conditions = conditions.and(taxGroup.TAX_GROUP_NR.ne(formData.getTaxGroupId()));
    }
    Result<?> result = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .select(taxGroup.TAX_GROUP_NR, taxGroup.NAME, taxGroup.START_DATE, taxGroup.END_DATE)
        .from(taxGroup)
        .where(conditions)
        .and(DSL.or(taxGroup.START_DATE.le(formData.getPeriodBox().getFrom().getValue())
            .and(taxGroup.END_DATE.ge(formData.getPeriodBox().getFrom().getValue())),
            taxGroup.START_DATE.le(formData.getPeriodBox().getTo().getValue())
                .and(taxGroup.END_DATE.ge(formData.getPeriodBox().getTo().getValue()))))
        .fetch();
    if (result.size() > 0) {
      Status status = new Status("Date period overlapping with tax group  '" + result.get(0).get(taxGroup.NAME) + "'", IStatus.ERROR);
      if (throwException) {
        throw new VetoException(new ProcessingStatus(status));
      }
      return status;
    }

    return Status.OK_STATUS;
  }

  @RemoteServiceAccessDenied
  public int insert(Connection connection, BigDecimal taxGroupId, String name, Date startDate, Date endDate) {
    TaxGroupFormData fd = new TaxGroupFormData();
    fd.setTaxGroupId(taxGroupId);
    fd.getName().setValue(name);
    fd.getPeriodBox().getFrom().setValue(startDate);
    fd.getPeriodBox().getTo().setValue(endDate);
    return DSL.using(connection, SQLDialect.DERBY)
        .executeInsert(mapToRecord(new TaxGroupRecord(), fd));

  }

  protected TaxGroupRecord mapToRecord(TaxGroupRecord rec, TaxGroupFormData formData) {
    if (formData == null) {
      return null;
    }
    TaxGroup t = TaxGroup.TAX_GROUP;
    return rec
        .with(t.END_DATE, formData.getPeriodBox().getTo().getValue())
        .with(t.NAME, formData.getName().getValue())
        .with(t.START_DATE, formData.getPeriodBox().getFrom().getValue())
        .with(t.TAX_GROUP_NR, formData.getTaxGroupId());

  }

  protected TaxGroupFormData mapToFormData(Record rec, TaxGroupFormData formData) {
    if (rec == null) {
      return null;
    }
    formData.getPeriodBox().getTo().setValue(rec.get(TaxGroup.TAX_GROUP.END_DATE));
    formData.getName().setValue(rec.get(TaxGroup.TAX_GROUP.NAME));
    formData.getPeriodBox().getFrom().setValue(rec.get(TaxGroup.TAX_GROUP.START_DATE));
    formData.setTaxGroupId(rec.get(TaxGroup.TAX_GROUP.TAX_GROUP_NR));
    return formData;
  }
}

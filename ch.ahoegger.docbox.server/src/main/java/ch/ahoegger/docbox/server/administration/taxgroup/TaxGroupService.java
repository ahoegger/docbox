package ch.ahoegger.docbox.server.administration.taxgroup;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.ch.ahoegger.docbox.server.or.app.tables.EmployeeTaxGroup;
import org.ch.ahoegger.docbox.server.or.app.tables.PayslipAccounting;
import org.ch.ahoegger.docbox.server.or.app.tables.TaxGroup;
import org.ch.ahoegger.docbox.server.or.app.tables.records.TaxGroupRecord;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.server.jdbc.SQL;
import org.eclipse.scout.rt.shared.servicetunnel.RemoteServiceAccessDenied;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.TableLike;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.or.definition.table.ISequenceTable;
import ch.ahoegger.docbox.shared.administration.taxgroup.ITaxGroupService;
import ch.ahoegger.docbox.shared.backup.IBackupService;
import ch.ahoegger.docbox.shared.hr.tax.TaxGroupFormData;
import ch.ahoegger.docbox.shared.hr.tax.TaxGroupSearchFormData;
import ch.ahoegger.docbox.shared.hr.tax.TaxGroupTablePageData;
import ch.ahoegger.docbox.shared.hr.tax.TaxGroupTablePageData.TaxGroupTableRowData;
import ch.ahoegger.docbox.shared.util.LocalDateUtility;

public class TaxGroupService implements ITaxGroupService {
  private static final Logger LOG = LoggerFactory.getLogger(TaxGroupService.class);

  @Override
  public TaxGroupTablePageData getTaxGroupTableData(TaxGroupSearchFormData formData) {

    TaxGroup tg = TaxGroup.TAX_GROUP.as("TG");
    EmployeeTaxGroup etg = EmployeeTaxGroup.EMPLOYEE_TAX_GROUP.as("ETG");
    TableLike<?> table = tg;

    Condition condition = DSL.trueCondition();

    if (formData.getPartner().getValue() != null) {
      condition = condition.and(etg.PARTNER_NR.eq(formData.getPartner().getValue()));
      table = tg.innerJoin(etg).on(etg.TAX_GROUP_NR.eq(tg.TAX_GROUP_NR));
    }
    if (formData.getPartner().getValue() != null) {
      // join

    }
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
        .from(table)
        .where(condition)
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
    LocalDate firstOfYear = LocalDate.now().withDayOfYear(1);
    LocalDate lastOfYear = firstOfYear.withDayOfYear(firstOfYear.lengthOfYear());
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");

    formData.getName().setValue(firstOfYear.format(formatter));
    formData.getStartDate().setValue(LocalDateUtility.toDate(firstOfYear));
    formData.getEndDate().setValue(LocalDateUtility.toDate(lastOfYear));
    return formData;
  }

  @Override
  public TaxGroupFormData create(TaxGroupFormData formData) {
    formData.setTaxGroupId(new BigDecimal(SQL.getSequenceNextval(ISequenceTable.TABLE_NAME)));

    int rowCount = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .executeInsert(mapToRecord(new TaxGroupRecord(), formData));

    if (rowCount == 1) {
      // notify backup needed
      BEANS.get(IBackupService.class).notifyModification();
      return formData;
    }
    return null;
  }

  @RemoteServiceAccessDenied
  public int insert(Connection connection, BigDecimal taxGroupId, String name, Date startDate, Date endDate) {
    return DSL.using(connection, SQLDialect.DERBY)
        .executeInsert(mapToRecord(new TaxGroupRecord(), taxGroupId, name, startDate, endDate));
  }

  @Override
  public TaxGroupFormData load(TaxGroupFormData formData) {
    TaxGroup t = TaxGroup.TAX_GROUP;
    PayslipAccounting pg = PayslipAccounting.PAYSLIP_ACCOUNTING;
    Condition condition = DSL.trueCondition();
    condition = condition.and(t.TAX_GROUP_NR.eq(formData.getTaxGroupId()));
    if (formData.getPartnerId() != null) {
      condition = condition.and(pg.PARTNER_NR.eq(formData.getPartnerId()));
    }
    // aliases
    Field<BigDecimal> workingHours = DSL.sum(pg.WORKING_HOURS).as("WORKING_HOURS");
    Field<BigDecimal> bruttoWage = DSL.sum(pg.BRUTTO_WAGE).as("BRUTTO_WAGE");
    Field<BigDecimal> nettoWage = DSL.sum(pg.NETTO_WAGE).as("NETTO_WAGE");

    Field<BigDecimal> sourceTax = DSL.sum(pg.SOURCE_TAX).as("SOURCE_TAX");
    Field<BigDecimal> socialSecurtiyTax = DSL.sum(pg.SOCIAL_SECURITY_TAX).as("SOCIAL_SECURITY_TAX");
    Field<BigDecimal> vacationExtra = DSL.sum(pg.VACATION_EXTRA).as("VACATION_EXTRA");

    formData = toFormData(DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .select(t.TAX_GROUP_NR, t.NAME, t.START_DATE, t.END_DATE, workingHours, bruttoWage, nettoWage, sourceTax, socialSecurtiyTax, vacationExtra)
        .from(t)
        .leftJoin(pg).on(t.TAX_GROUP_NR.eq(pg.TAX_GROUP_NR))
        .where(condition)
        .groupBy(t.TAX_GROUP_NR, t.NAME, t.START_DATE, t.END_DATE)
        .fetchOne(), workingHours, bruttoWage, nettoWage, sourceTax, socialSecurtiyTax, vacationExtra);

    return formData;
  }

  @Override
  public TaxGroupFormData store(TaxGroupFormData formData) {
    TaxGroup e = TaxGroup.TAX_GROUP;

    TaxGroupRecord rec = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .fetchOne(e, e.TAX_GROUP_NR.eq(formData.getTaxGroupId()));
    if (rec == null) {
      LOG.warn("Try to update not existing record with id '{}'!", formData.getTaxGroupId());
      return null;
    }
    if (mapToRecord(rec, formData).update() != 1) {
      LOG.error("Could not update record with id '{}'!", formData.getTaxGroupId());
      return null;
    }
    // notify backup needed
    BEANS.get(IBackupService.class).notifyModification();

    return formData;
  }

  @RemoteServiceAccessDenied
  public int createRow(Connection connection, BigDecimal taxGroupId, String name, Date startDate, Date endDate) {
    return DSL.using(connection, SQLDialect.DERBY)
        .executeInsert(mapToRecord(new TaxGroupRecord(), taxGroupId, name, startDate, endDate));

  }

  protected TaxGroupRecord mapToRecord(TaxGroupRecord rec, TaxGroupFormData fd) {
    if (fd == null) {
      return null;
    }
    return mapToRecord(rec, fd.getTaxGroupId(), fd.getName().getValue(), fd.getStartDate().getValue(), fd.getEndDate().getValue());

  }

  protected TaxGroupRecord mapToRecord(TaxGroupRecord rec, BigDecimal taxGroupId, String name, Date startDate, Date endDate) {
    TaxGroup t = TaxGroup.TAX_GROUP;
    return rec
        .with(t.END_DATE, endDate)
        .with(t.NAME, name)
        .with(t.START_DATE, startDate)
        .with(t.TAX_GROUP_NR, taxGroupId);

  }

  protected TaxGroupFormData toFormData(Record rec, Field<BigDecimal> workingHours, Field<BigDecimal> bruttoWage, Field<BigDecimal> nettoWage, Field<BigDecimal> sourceTax, Field<BigDecimal> socialSecurtiyTax,
      Field<BigDecimal> vacationExtra) {
    if (rec == null) {
      return null;
    }
    TaxGroupFormData fd = new TaxGroupFormData();
    fd.getEndDate().setValue(rec.get(TaxGroup.TAX_GROUP.END_DATE));
    fd.getName().setValue(rec.get(TaxGroup.TAX_GROUP.NAME));
    fd.getStartDate().setValue(rec.get(TaxGroup.TAX_GROUP.START_DATE));
    fd.setTaxGroupId(rec.get(TaxGroup.TAX_GROUP.TAX_GROUP_NR));
    if (workingHours != null) {
      fd.getNettoWage().setValue(rec.get(nettoWage));
    }
    if (bruttoWage != null) {
      fd.getBruttoWage().setValue(rec.get(bruttoWage));
    }
    if (workingHours != null) {
      fd.getWorkHours().setValue(rec.get(workingHours));
    }
    if (sourceTax != null) {
      fd.getSourceTax().setValue(rec.get(sourceTax));
    }

    if (socialSecurtiyTax != null) {
      fd.getSocualInsurance().setValue(rec.get(socialSecurtiyTax));
    }
    if (vacationExtra != null) {
      fd.getVacationExtra().setValue(rec.get(vacationExtra));
    }
    return fd;
  }
}

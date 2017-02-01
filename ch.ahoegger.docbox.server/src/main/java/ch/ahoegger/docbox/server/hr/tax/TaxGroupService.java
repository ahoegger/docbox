package ch.ahoegger.docbox.server.hr.tax;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.ch.ahoegger.docbox.server.or.app.tables.TaxGroup;
import org.ch.ahoegger.docbox.server.or.app.tables.records.TaxGroupRecord;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.eclipse.scout.rt.server.jdbc.SQL;
import org.eclipse.scout.rt.shared.servicetunnel.RemoteServiceAccessDenied;
import org.jooq.Condition;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.or.definition.table.ISequenceTable;
import ch.ahoegger.docbox.shared.backup.IBackupService;
import ch.ahoegger.docbox.shared.hr.tax.ITaxGroupService;
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

    Condition condition = DSL.trueCondition();

    if (formData.getHasEndDate() != null && !formData.getHasEndDate().isUndefined()) {
      if (formData.getHasEndDate().isTrue()) {
        condition = condition.and(tg.END_DATE.isNotNull());
      }
      else {
        condition = condition.and(tg.END_DATE.isNull());
      }
    }

    List<TaxGroupTableRowData> rows = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .select(tg.TAX_GROUP_NR, tg.NAME, tg.START_DATE, tg.END_DATE)
        .from(tg)
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
    LocalDate date = LocalDate.now()
        .withDayOfMonth(1)
        .withMonth(1);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");
    formData.getName().setValue(date.format(formatter));
    formData.getStartDate().setValue(LocalDateUtility.toDate(date));
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

  @Override
  public TaxGroupFormData load(TaxGroupFormData formData) {
    TaxGroup t = TaxGroup.TAX_GROUP;
    return toFormData(DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .fetchOne(t, t.TAX_GROUP_NR.eq(formData.getTaxGroupId())));
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
  public int createRow(ISqlService sqlService, BigDecimal taxGroupId, String name, Date startDate, Date endDate) {
    return DSL.using(sqlService.getConnection(), SQLDialect.DERBY)
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

  protected TaxGroupFormData toFormData(TaxGroupRecord rec) {
    if (rec == null) {
      return null;
    }
    TaxGroupFormData fd = new TaxGroupFormData();
    fd.getEndDate().setValue(rec.getEndDate());
    fd.getName().setValue(rec.getName());
    fd.getStartDate().setValue(rec.getStartDate());
    fd.setTaxGroupId(rec.getTaxGroupNr());
    return fd;
  }
}

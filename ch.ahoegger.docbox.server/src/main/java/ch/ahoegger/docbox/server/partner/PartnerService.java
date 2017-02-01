package ch.ahoegger.docbox.server.partner;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.ch.ahoegger.docbox.server.or.app.tables.DocumentPartner;
import org.ch.ahoegger.docbox.server.or.app.tables.Partner;
import org.ch.ahoegger.docbox.server.or.app.tables.records.PartnerRecord;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.eclipse.scout.rt.server.jdbc.SQL;
import org.jooq.Condition;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.or.definition.table.ISequenceTable;
import ch.ahoegger.docbox.shared.backup.IBackupService;
import ch.ahoegger.docbox.shared.partner.IPartnerService;
import ch.ahoegger.docbox.shared.partner.PartnerFormData;
import ch.ahoegger.docbox.shared.partner.PartnerSearchFormData;
import ch.ahoegger.docbox.shared.partner.PartnerTableData;
import ch.ahoegger.docbox.shared.partner.PartnerTableData.PartnerTableRowData;
import ch.ahoegger.docbox.shared.util.LocalDateUtility;

/**
 * <h3>{@link PartnerService}</h3>
 *
 * @author Andreas Hoegger
 */
public class PartnerService implements IPartnerService {
  private static final Logger LOG = LoggerFactory.getLogger(PartnerService.class);

  @Override
  public PartnerTableData getTableData(PartnerSearchFormData formData) {
    Partner p = Partner.PARTNER.as("P");
    DocumentPartner docPar = DocumentPartner.DOCUMENT_PARTNER.as("DOC_PAR");

    Condition condition = DSL.trueCondition();
    // search criteria name
    if (StringUtility.hasText(formData.getName().getValue())) {
      condition = condition.and(p.NAME.lower().contains(formData.getName().getValue().toLowerCase()));
    }
    // active
    if (formData.getActiveBox().getValue() != null) {
      switch (formData.getActiveBox().getValue()) {
        case TRUE:
          condition = condition.and(
              p.END_DATE.ge(LocalDateUtility.today())
                  .or(p.END_DATE.isNull()));
          break;
        case FALSE:
          condition = condition.and(
              p.END_DATE.lessThan(new Date()));
          break;
      }
    }
    // document id
    if (formData.getDocumentId() != null) {
      condition = condition.and(docPar.DOCUMENT_NR.eq(formData.getDocumentId()));
    }

    List<PartnerTableRowData> rowDatas = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .select(p.PARTNER_NR, p.NAME, p.START_DATE, p.END_DATE)
        .from(p)
        .leftOuterJoin(docPar)
        .on(p.PARTNER_NR.eq(docPar.PARTNER_NR))
        .where(condition)
        .groupBy(p.PARTNER_NR, p.NAME, p.START_DATE, p.END_DATE)
        .fetch()
        .stream()
        .map(rec -> {
          PartnerTableRowData rd = new PartnerTableRowData();
          rd.setPartnerId(rec.get(p.PARTNER_NR));
          rd.setName(rec.get(p.NAME));
          rd.setStartDate(rec.get(p.START_DATE));
          rd.setEndDate(rec.get(p.END_DATE));
          return rd;
        })
        .collect(Collectors.toList());

    PartnerTableData tableData = new PartnerTableData();
    tableData.setRows(rowDatas.toArray(new PartnerTableRowData[0]));

    return tableData;
  }

  @Override
  public PartnerFormData prepareCreate(PartnerFormData formData) {
    formData.getPartnerBox().getStartDate().setValue(LocalDateUtility.today());
    return formData;
  }

  @Override
  public PartnerFormData create(PartnerFormData formData) {

    formData.setPartnerId(new BigDecimal(SQL.getSequenceNextval(ISequenceTable.TABLE_NAME)));

    if (DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .executeInsert(toRecord(formData)) == 1) {
      // notify backup needed
      BEANS.get(IBackupService.class).notifyModification();
      return formData;
    }
    return null;
  }

  @Override
  public PartnerFormData load(PartnerFormData formData) {
    Partner p = Partner.PARTNER;
    return toFormData(DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .fetchOne(p, p.PARTNER_NR.eq(formData.getPartnerId())));
  }

  @Override
  public PartnerFormData store(PartnerFormData formData) {
    if (DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .executeUpdate(toRecord(formData)) == 1) {
      // notify backup needed
      BEANS.get(IBackupService.class).notifyModification();
      return formData;
    }
    return null;

  }

  @Override
  public boolean delete(BigDecimal partnerId) {
    Partner p = Partner.PARTNER;
    PartnerRecord rec = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .fetchOne(p, p.PARTNER_NR.eq(partnerId));
    if (rec == null) {
      LOG.warn("Try to delete not existing record with id '{}'!", partnerId);
      return false;
    }
    if (rec.delete() != 1) {
      LOG.error("Could not delete record with id '{}'!", partnerId);
      return false;
    }

    // notify backup needed
    BEANS.get(IBackupService.class).notifyModification();
    return true;

  }

  public int insert(ISqlService sqlService, BigDecimal patnerId, String name, String description, Date startDate, Date endDate) {
    return DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .executeInsert(mapToRecord(new PartnerRecord(), patnerId, name, description, startDate, endDate));
  }

  protected PartnerRecord toRecord(PartnerFormData fd) {
    return mapToRecord(new PartnerRecord(), fd);
  }

  protected PartnerRecord mapToRecord(PartnerRecord rec, PartnerFormData fd) {
    return mapToRecord(rec, fd.getPartnerId(), fd.getPartnerBox().getName().getValue(), fd.getPartnerBox().getDescription().getValue(), fd.getPartnerBox().getStartDate().getValue(), fd.getPartnerBox().getEndDate().getValue());
  }

  protected PartnerRecord mapToRecord(PartnerRecord rec, BigDecimal patnerId, String name, String description, Date startDate, Date endDate) {
    return rec
        .with(Partner.PARTNER.DESCRIPTION, description)
        .with(Partner.PARTNER.END_DATE, endDate)
        .with(Partner.PARTNER.NAME, name)
        .with(Partner.PARTNER.PARTNER_NR, patnerId)
        .with(Partner.PARTNER.START_DATE, startDate);
  }

  protected PartnerFormData toFormData(PartnerRecord rec) {
    if (rec == null) {
      return null;
    }
    PartnerFormData fd = new PartnerFormData();
    fd.setPartnerId(rec.getPartnerNr());
    fd.getPartnerBox().getName().setValue(rec.getName());
    fd.getPartnerBox().getDescription().setValue(rec.getDescription());
    fd.getPartnerBox().getEndDate().setValue(rec.getEndDate());
    fd.getPartnerBox().getStartDate().setValue(rec.getStartDate());
    return fd;
  }

}

package ch.ahoegger.docbox.server.partner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.holders.BooleanHolder;
import org.eclipse.scout.rt.platform.holders.NVPair;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.platform.util.TypeCastUtility;
import org.eclipse.scout.rt.server.jdbc.SQL;
import org.eclipse.scout.rt.shared.servicetunnel.RemoteServiceAccessDenied;

import ch.ahoegger.docbox.server.document.DocumentPartnerService;
import ch.ahoegger.docbox.shared.ISequenceTable;
import ch.ahoegger.docbox.shared.backup.IBackupService;
import ch.ahoegger.docbox.shared.document.IDocumentPartnerTable;
import ch.ahoegger.docbox.shared.partner.IPartnerService;
import ch.ahoegger.docbox.shared.partner.IPartnerTable;
import ch.ahoegger.docbox.shared.partner.Partner;
import ch.ahoegger.docbox.shared.partner.PartnerFormData;
import ch.ahoegger.docbox.shared.partner.PartnerSearchFormData;
import ch.ahoegger.docbox.shared.partner.PartnerTableData;
import ch.ahoegger.docbox.shared.util.LocalDateUtility;
import ch.ahoegger.docbox.shared.util.SqlFramentBuilder;

/**
 * <h3>{@link PartnerService}</h3>
 *
 * @author Andreas Hoegger
 */
public class PartnerService implements IPartnerService, IPartnerTable {

  @Override
  public PartnerTableData getTableData(PartnerSearchFormData formData) {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("SELECT ").append(SqlFramentBuilder.columnsAliased(TABLE_ALIAS, PARTNER_NR, NAME, START_DATE, END_DATE))
        .append(" FROM ").append(TABLE_NAME).append(" AS ").append(TABLE_ALIAS).append(" ")
        .append(SqlFramentBuilder.WHERE_DEFAULT);

    // search criteria name
    if (StringUtility.hasText(formData.getName().getValue())) {
      statementBuilder.append(" AND ").append(SqlFramentBuilder.whereStringContains(NAME, formData.getName().getValue()));
    }
    // seach criteria active
    if (formData.getActiveBox().getValue() != null) {
      switch (formData.getActiveBox().getValue()) {
        case TRUE:
          statementBuilder.append(" AND (").append(SqlFramentBuilder.columnsAliased(TABLE_ALIAS, END_DATE)).append(" >= ").append("CURRENT_DATE")
              .append(" OR ").append(SqlFramentBuilder.columnsAliased(TABLE_ALIAS, END_DATE)).append(" IS NULL)");
          break;
        case FALSE:
          statementBuilder.append(" AND ").append(SqlFramentBuilder.columnsAliased(TABLE_ALIAS, END_DATE)).append(" < ").append("CURRENT_DATE");
          break;
      }
    }

    statementBuilder.append(" INTO ")
        .append(":{td.partnerId}, ")
        .append(":{td.name}, ")
        .append(":{td.startDate}, ")
        .append(":{td.endDate} ");

    PartnerTableData tableData = new PartnerTableData();
    SQL.selectInto(statementBuilder.toString(),
        new NVPair("td", tableData),
        formData);
    return tableData;
  }

  @RemoteServiceAccessDenied
  public List<Partner> getPartners(BigDecimal documentId) {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("SELECT ").append(SqlFramentBuilder.columnsAliased(TABLE_ALIAS, PARTNER_NR, NAME, DESCRIPTION, START_DATE, END_DATE));
    statementBuilder.append(" FROM ").append(TABLE_NAME).append(" AS ").append(TABLE_ALIAS).append(", ").append(IDocumentPartnerTable.TABLE_NAME).append(" AS ").append(IDocumentPartnerTable.TABLE_ALIAS);
    statementBuilder.append(" WHERE ").append(SqlFramentBuilder.columnsAliased(IDocumentPartnerTable.TABLE_ALIAS, IDocumentPartnerTable.PARTNER_NR)).append(" = ").append(SqlFramentBuilder.columnsAliased(TABLE_ALIAS, PARTNER_NR));
    statementBuilder.append(" AND ").append(SqlFramentBuilder.columnsAliased(IDocumentPartnerTable.TABLE_ALIAS, IDocumentPartnerTable.DOCUMENT_NR)).append(" = ").append(":documentId");
    statementBuilder.append(" ORDER BY ").append(SqlFramentBuilder.columnsAliased(TABLE_ALIAS, NAME));
    Object[][] rawResult = SQL.select(statementBuilder.toString(), new NVPair("documentId", documentId));

    return Arrays.stream(rawResult).map(row -> new Partner()
        .withPartnerId(TypeCastUtility.castValue(row[0], Long.class))
        .withName(TypeCastUtility.castValue(row[1], String.class))
        .withDescription(TypeCastUtility.castValue(row[2], String.class))
        .withStartDate(TypeCastUtility.castValue(row[3], Date.class))
        .withEndDate(TypeCastUtility.castValue(row[4], Date.class)))
        .collect(Collectors.toList());

  }

  @Override
  public PartnerFormData prepareCreate(PartnerFormData formData) {
    formData.getPartnerBox().getStartDate().setValue(LocalDateUtility.today());
    return formData;
  }

  @Override
  public PartnerFormData create(PartnerFormData formData) {
    formData.setPartnerId(new BigDecimal(SQL.getSequenceNextval(ISequenceTable.TABLE_NAME)));
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("INSERT INTO ").append(TABLE_NAME);
    statementBuilder.append(" (").append(SqlFramentBuilder.columns(PARTNER_NR, NAME, DESCRIPTION, START_DATE, END_DATE)).append(")");
    statementBuilder.append(" VALUES (:partnerId, :name, :description, :startDate, :endDate )");
    SQL.insert(statementBuilder.toString(), formData, formData.getPartnerBox());

    // notify backup needed
    BEANS.get(IBackupService.class).notifyModification();

    return formData;
  }

  @Override
  public PartnerFormData load(PartnerFormData formData) {
    BooleanHolder exists = new BooleanHolder();
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("SELECT TRUE, ").append(SqlFramentBuilder.columns(SqlFramentBuilder.columns(NAME, DESCRIPTION, START_DATE, END_DATE)));
    statementBuilder.append(" FROM ").append(TABLE_NAME);
    statementBuilder.append(" WHERE ").append(PARTNER_NR).append(" = :partnerId");
    statementBuilder.append(" INTO :exists, :name, :description, :startDate, :endDate");
    SQL.selectInto(statementBuilder.toString(),
        new NVPair("exists", exists),
        formData, formData.getPartnerBox());
    if (exists.getValue() == null) {
      return null;
    }
    return formData;
  }

  @Override
  public PartnerFormData store(PartnerFormData formData) {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("UPDATE ").append(TABLE_NAME).append(" SET ");
    statementBuilder.append(NAME).append("= :name, ");
    statementBuilder.append(DESCRIPTION).append("= :description, ");
    statementBuilder.append(START_DATE).append("= :startDate, ");
    statementBuilder.append(END_DATE).append("= :endDate ");
    statementBuilder.append(" WHERE ").append(PARTNER_NR).append(" = :partnerId");
    SQL.update(statementBuilder.toString(), formData, formData.getPartnerBox());

    // notify backup needed
    BEANS.get(IBackupService.class).notifyModification();

    return formData;
  }

  @Override
  public void delete(BigDecimal partnerId) {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("DELETE FROM ").append(TABLE_NAME).append(" WHERE ").append(PARTNER_NR).append(" = :partnerId");
    SQL.delete(statementBuilder.toString(), new NVPair("partnerId", partnerId));

    // notify backup needed
    BEANS.get(IBackupService.class).notifyModification();

    // delete document category connection
    BEANS.get(DocumentPartnerService.class).deletePartnerId(partnerId);
  }

}

package ch.ahoegger.docbox.server.partner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.holders.NVPair;
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
import ch.ahoegger.docbox.shared.util.SqlFramentBuilder;

/**
 * <h3>{@link PartnerService}</h3>
 *
 * @author Andreas Hoegger
 */
public class PartnerService implements IPartnerService, IPartnerTable {

  @RemoteServiceAccessDenied
  public List<Partner> getPartners(Long documentId) {
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
    formData.getStartDate().setValue(new Date());
    return formData;
  }

  @Override
  public PartnerFormData create(PartnerFormData formData) {
    formData.setPartnerId(new BigDecimal(SQL.getSequenceNextval(ISequenceTable.TABLE_NAME)));
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("INSERT INTO ").append(TABLE_NAME);
    statementBuilder.append(" (").append(SqlFramentBuilder.columns(PARTNER_NR, NAME, DESCRIPTION, START_DATE, END_DATE)).append(")");
    statementBuilder.append(" VALUES (:partnerId, :name, :description, :startDate, :endDate )");
    SQL.insert(statementBuilder.toString(), formData);

    // notify backup needed
    BEANS.get(IBackupService.class).notifyModification();

    return formData;
  }

  @Override
  public PartnerFormData load(PartnerFormData formData) {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("SELECT ").append(SqlFramentBuilder.columns(SqlFramentBuilder.columns(NAME, DESCRIPTION, START_DATE, END_DATE)));
    statementBuilder.append(" FROM ").append(TABLE_NAME);
    statementBuilder.append(" WHERE ").append(PARTNER_NR).append(" = :partnerId");
    statementBuilder.append(" INTO :name, :description, :startDate, :endDate");
    SQL.selectInto(statementBuilder.toString(), formData);
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
    SQL.update(statementBuilder.toString(), formData);

    // notify backup needed
    BEANS.get(IBackupService.class).notifyModification();

    return formData;
  }

  @Override
  public void delete(Long partnerId) {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("DELETE FROM ").append(TABLE_NAME).append(" WHERE ").append(PARTNER_NR).append(" = :partnerId");
    SQL.delete(statementBuilder.toString(), new NVPair("partnerId", partnerId));

    // notify backup needed
    BEANS.get(IBackupService.class).notifyModification();

    // delete document category connection
    BEANS.get(DocumentPartnerService.class).deletePartnerId(partnerId);
  }

}

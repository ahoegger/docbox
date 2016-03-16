package ch.ahoegger.docbox.server.partner;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.scout.rt.platform.ApplicationScoped;
import org.eclipse.scout.rt.platform.holders.NVPair;
import org.eclipse.scout.rt.platform.util.TypeCastUtility;
import org.eclipse.scout.rt.server.jdbc.SQL;

import ch.ahoegger.docbox.server.database.SqlFramentBuilder;
import ch.ahoegger.docbox.shared.document.IDocumentPartnerTable;
import ch.ahoegger.docbox.shared.partner.IPartnerTable;
import ch.ahoegger.docbox.shared.partner.Partner;

/**
 * <h3>{@link PartnerService}</h3>
 *
 * @author aho
 */
@ApplicationScoped
public class PartnerService implements IPartnerTable {

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
}

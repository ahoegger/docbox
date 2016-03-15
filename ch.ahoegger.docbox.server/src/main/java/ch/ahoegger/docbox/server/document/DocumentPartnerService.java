package ch.ahoegger.docbox.server.document;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.scout.rt.platform.ApplicationScoped;
import org.eclipse.scout.rt.platform.holders.NVPair;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.server.jdbc.SQL;

import ch.ahoegger.docbox.server.database.SqlFramentBuilder;
import ch.ahoegger.docbox.shared.document.IDocumentPartnerTable;

/**
 * <h3>{@link DocumentPartnerService}</h3>
 *
 * @author aho
 */
@ApplicationScoped
public class DocumentPartnerService implements IDocumentPartnerTable {

  public Set<BigDecimal> getPartnerIds(Long documentId) {
    Set<BigDecimal> partnerIds = new HashSet<BigDecimal>();
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("SELECT ").append(PARTNER_NR).append(" FROM ").append(TABLE_NAME);
    statementBuilder.append(" WHERE ").append(DOCUMENT_NR).append(" = :documentId");
    Object[][] rawResult = SQL.select(statementBuilder.toString(),
        new NVPair("documentId", documentId));
    for (Object[] o : rawResult) {
      partnerIds.add((BigDecimal) o[0]);
    }
    return partnerIds;
  }

  public void createDocumentPartners(Long documentId, Set<BigDecimal> partnerIds) {
    if (CollectionUtility.hasElements(partnerIds)) {
      for (BigDecimal partnerId : partnerIds) {
        StringBuilder statementBuilder = new StringBuilder();
        statementBuilder.append("INSERT INTO ").append(TABLE_NAME).append(" (");
        statementBuilder.append(SqlFramentBuilder.columns(DOCUMENT_NR, PARTNER_NR));
        statementBuilder.append(") VALUES (");
        statementBuilder.append(":documentId, :partnerId");
        statementBuilder.append(")");
        SQL.insert(statementBuilder.toString(),
            new NVPair("documentId", documentId),
            new NVPair("partnerId", partnerId));
      }
    }
  }
}

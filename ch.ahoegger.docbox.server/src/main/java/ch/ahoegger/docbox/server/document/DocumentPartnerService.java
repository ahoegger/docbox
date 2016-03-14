package ch.ahoegger.docbox.server.document;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.scout.rt.platform.ApplicationScoped;
import org.eclipse.scout.rt.platform.holders.NVPair;
import org.eclipse.scout.rt.server.jdbc.SQL;

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
}

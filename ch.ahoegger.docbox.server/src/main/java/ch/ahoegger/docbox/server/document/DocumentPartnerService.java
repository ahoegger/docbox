package ch.ahoegger.docbox.server.document;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.scout.rt.platform.ApplicationScoped;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.holders.NVPair;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.server.jdbc.SQL;
import org.eclipse.scout.rt.shared.servicetunnel.RemoteServiceAccessDenied;

import ch.ahoegger.docbox.shared.backup.IBackupService;
import ch.ahoegger.docbox.shared.document.IDocumentPartnerTable;
import ch.ahoegger.docbox.shared.util.SqlFramentBuilder;

/**
 * <h3>{@link DocumentPartnerService}</h3>
 *
 * @author Andreas Hoegger
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

        // notify backup needed
        BEANS.get(IBackupService.class).notifyModification();

      }
    }
    // notify backup needed
    BEANS.get(IBackupService.class).notifyModification();
  }

  /**
   * @param documentId
   * @param value
   */
  @RemoteServiceAccessDenied
  public void updateDocumentPartner(Long documentId, Set<BigDecimal> partnerIds) {
    deleteByDocumentId(documentId);
    createDocumentPartners(documentId, partnerIds);
  }

  @RemoteServiceAccessDenied
  public void deleteByDocumentId(Long documentId) {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("DELETE FROM ").append(TABLE_NAME).append(" WHERE ").append(DOCUMENT_NR).append(" = :documentId");
    SQL.delete(statementBuilder.toString(), new NVPair("documentId", documentId));

    // notify backup needed
    BEANS.get(IBackupService.class).notifyModification();

  }

  public void deletePartnerId(Long partnerId) {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("DELETE FROM ").append(TABLE_NAME).append(" WHERE ").append(PARTNER_NR).append(" = :partnerId");
    SQL.delete(statementBuilder.toString(), new NVPair("partnerId", partnerId));
    // notify backup needed
    BEANS.get(IBackupService.class).notifyModification();

  }
}

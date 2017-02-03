package ch.ahoegger.docbox.server.document;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Set;
import java.util.stream.Collectors;

import org.ch.ahoegger.docbox.server.or.app.tables.DocumentPartner;
import org.eclipse.scout.rt.platform.ApplicationScoped;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.server.jdbc.SQL;
import org.eclipse.scout.rt.shared.servicetunnel.RemoteServiceAccessDenied;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import ch.ahoegger.docbox.shared.backup.IBackupService;

/**
 * <h3>{@link DocumentPartnerService}</h3>
 *
 * @author Andreas Hoegger
 */
@ApplicationScoped
public class DocumentPartnerService {

  public Set<BigDecimal> getPartnerIds(BigDecimal documentId) {
    DocumentPartner t = DocumentPartner.DOCUMENT_PARTNER;

    return DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .select(t.PARTNER_NR)
        .from(t)
        .where(t.DOCUMENT_NR.eq(documentId))
        .fetch()
        .stream()
        .map(rec -> rec.get(t.PARTNER_NR))
        .collect(Collectors.toSet());
  }

  public void createDocumentPartners(BigDecimal documentId, Set<BigDecimal> partnerIds) {
    if (CollectionUtility.hasElements(partnerIds)) {
      DocumentPartner t = DocumentPartner.DOCUMENT_PARTNER;

      DSL.using(SQL.getConnection(), SQLDialect.DERBY)
          .batchInsert(
              partnerIds
                  .stream()
                  .map(id -> t.newRecord()
                      .with(t.DOCUMENT_NR, documentId)
                      .with(t.PARTNER_NR, id))
                  .collect(Collectors.toList()))
          .execute();

      // notify backup needed
      BEANS.get(IBackupService.class).notifyModification();

    }
  }

  /**
   * @param documentId
   * @param value
   */
  @RemoteServiceAccessDenied
  public void updateDocumentPartner(BigDecimal documentId, Set<BigDecimal> partnerIds) {
    deleteByDocumentId(documentId);
    createDocumentPartners(documentId, partnerIds);
  }

  @RemoteServiceAccessDenied
  public void deleteByDocumentId(BigDecimal documentId) {
    DocumentPartner t = DocumentPartner.DOCUMENT_PARTNER;

    DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .delete(t)
        .where(t.DOCUMENT_NR.eq(documentId))
        .execute();

    // notify backup needed
    BEANS.get(IBackupService.class).notifyModification();

  }

  public void deleteByPartnerId(BigDecimal partnerId) {
    DocumentPartner t = DocumentPartner.DOCUMENT_PARTNER;

    DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .delete(t)
        .where(t.PARTNER_NR.eq(partnerId))
        .execute();

    // notify backup needed
    BEANS.get(IBackupService.class).notifyModification();
  }

  @RemoteServiceAccessDenied
  public int insert(Connection connection, BigDecimal documentId, BigDecimal partnerId) {
    DocumentPartner t = DocumentPartner.DOCUMENT_PARTNER;
    return DSL.using(connection, SQLDialect.DERBY)
        .newRecord(t)
        .with(t.DOCUMENT_NR, documentId)
        .with(t.PARTNER_NR, partnerId)
        .insert();
  }
}

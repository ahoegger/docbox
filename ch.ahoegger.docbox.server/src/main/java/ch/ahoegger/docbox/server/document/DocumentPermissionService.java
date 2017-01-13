package ch.ahoegger.docbox.server.document;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

import org.ch.ahoegger.docbox.server.or.app.tables.DocumentPermission;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.server.jdbc.SQL;
import org.eclipse.scout.rt.shared.servicetunnel.RemoteServiceAccessDenied;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import ch.ahoegger.docbox.shared.backup.IBackupService;
import ch.ahoegger.docbox.shared.security.permission.IPermissionService;
import ch.ahoegger.docbox.shared.security.permission.PermissionCodeType;

/**
 * <h3>{@link DocumentPermissionService}</h3>
 *
 * @author Andreas Hoegger
 */
public class DocumentPermissionService implements IPermissionService {

  @Override
  public boolean hasReadAccess(String userId, BigDecimal entityId) {
    DocumentPermission docPer = DocumentPermission.DOCUMENT_PERMISSION;
    return DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .select(docPer.PERMISSION)
        .from(docPer)
        .where(docPer.USERNAME.eq(userId))
        .and(docPer.DOCUMENT_NR.eq(entityId))
        .fetch()
        .stream()
        .map(rec -> rec.get(docPer.PERMISSION))
        .findFirst().orElse(PermissionCodeType.NoneCode.ID) >= PermissionCodeType.ReadCode.ID;

  }

  @Override
  public boolean hasWriteAccess(BigDecimal entityId) {
    return false;
  }

  public Map<String, Integer> getPermissions(BigDecimal documentId) {

    DocumentPermission docPer = DocumentPermission.DOCUMENT_PERMISSION;
    return DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .select(docPer.USERNAME, docPer.PERMISSION)
        .from(docPer)
        .where(docPer.DOCUMENT_NR.eq(documentId))
        .fetch()
        .stream()
        .collect(Collectors.toMap(rec -> rec.get(docPer.USERNAME), rec -> rec.get(docPer.PERMISSION), (p1, p2) -> Math.max(p1, p2)));

  }

  public void createDocumentPermissions(BigDecimal documentId, Map<String, Integer> permissions) {
    DocumentPermission docPer = DocumentPermission.DOCUMENT_PERMISSION;

    DSL.using(SQL.getConnection(), SQLDialect.DERBY).batchInsert(
        permissions.entrySet().stream()
            .map(e -> docPer.newRecord()
                .with(docPer.DOCUMENT_NR, documentId)
                .with(docPer.USERNAME, e.getKey())
                .with(docPer.PERMISSION, e.getValue()))
            .collect(Collectors.toList()))
        .execute();

    // notify backup needed
    BEANS.get(IBackupService.class).notifyModification();
  }

  /**
   * @param documentId
   * @param permissions
   */
  @RemoteServiceAccessDenied
  public void updateDocumentPermissions(BigDecimal documentId, Map<String, Integer> permissions) {
    deleteDocumentPermissions(documentId);
    createDocumentPermissions(documentId, permissions);
  }

  @RemoteServiceAccessDenied
  public void deleteDocumentPermissions(BigDecimal documentId) {
    DocumentPermission docPer = DocumentPermission.DOCUMENT_PERMISSION;

    DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .delete(docPer)
        .where(docPer.DOCUMENT_NR.eq(documentId))
        .execute();

    // notify backup needed
    BEANS.get(IBackupService.class).notifyModification();

  }
}

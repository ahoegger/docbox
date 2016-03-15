package ch.ahoegger.docbox.server.document;

import java.math.BigDecimal;
import java.util.Date;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.holders.NVPair;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.server.jdbc.SQL;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;
import org.eclipse.scout.rt.shared.servicetunnel.RemoteServiceAccessDenied;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.server.ServerSession;
import ch.ahoegger.docbox.server.database.SqlFramentBuilder;
import ch.ahoegger.docbox.server.document.store.DocumentStoreService;
import ch.ahoegger.docbox.server.security.permission.DefaultPermissionService;
import ch.ahoegger.docbox.server.security.permission.UserPermission;
import ch.ahoegger.docbox.shared.ISequenceTable;
import ch.ahoegger.docbox.shared.document.DocumentFormData;
import ch.ahoegger.docbox.shared.document.DocumentFormData.Permissions.PermissionsRowData;
import ch.ahoegger.docbox.shared.document.DocumentSearchFormData;
import ch.ahoegger.docbox.shared.document.DocumentTableData;
import ch.ahoegger.docbox.shared.document.IDocumentService;
import ch.ahoegger.docbox.shared.document.IDocumentTable;
import ch.ahoegger.docbox.shared.security.permission.EntityReadPermission;
import ch.ahoegger.docbox.shared.security.permission.IPermissionTable;

/**
 * <h3>{@link DocumentService}</h3>
 *
 * @author aho
 */
public class DocumentService implements IDocumentService, IDocumentTable {
  private static final Logger LOG = LoggerFactory.getLogger(DocumentService.class);

  @Override
  public DocumentTableData getTableData(DocumentSearchFormData formData) {
    StringBuilder sqlBuilder = new StringBuilder();
    sqlBuilder.append("SELECT ");
    sqlBuilder.append(SqlFramentBuilder.columnsAliased("DOC", DOCUMENT_NR, ABSTRACT, DOCUMENT_URL));
    sqlBuilder.append(" FROM ").append(TABLE_NAME).append(" ").append(TABLE_ALIAS);
    sqlBuilder.append(", ").append(IPermissionTable.TABLE_NAME).append(" ").append(IPermissionTable.TABLE_ALIAS);
    sqlBuilder.append(" WHERE 1 = 1");
    sqlBuilder.append(" AND ").append(IPermissionTable.TABLE_ALIAS).append(".").append(IPermissionTable.USERNAME).append(" = '").append(ServerSession.get().getUserId()).append("'");
    sqlBuilder.append(" AND ").append(TABLE_ALIAS).append(".").append(DOCUMENT_NR).append(" = ").append(IPermissionTable.TABLE_ALIAS).append(".").append(IPermissionTable.ENTITY_NR);
    sqlBuilder.append(" AND ").append(IPermissionTable.TABLE_ALIAS).append(".").append(IPermissionTable.PERMISSION).append(" >= ").append(IPermissionTable.PERMISSION_READ);

    if (StringUtility.hasText(formData.getAbstract().getValue())) {
      sqlBuilder.append(" AND ").append(SqlFramentBuilder.whereStringContains(TABLE_ALIAS, ABSTRACT, formData.getAbstract().getValue()));
    }
    sqlBuilder.append(" INTO ");
    sqlBuilder.append(":{td.documentId}, ");
    sqlBuilder.append(":{td.abstract}, ");
    sqlBuilder.append(":{td.documentPath} ");
    DocumentTableData tableData = new DocumentTableData();
    SQL.selectInto(sqlBuilder.toString(), new NVPair("td", tableData));
    return tableData;
  }

  @Override
  public DocumentFormData prepareCreate(DocumentFormData formData) {
    formData.getCapturedDate().setValue(new Date());
    for (UserPermission up : BEANS.get(DefaultPermissionService.class).getDefaultPermissions()) {
      PermissionsRowData row = formData.getPermissions().addRow();
      row.setUser(up.getUsername());
      row.setPermission(up.getPermission());
    }
    return formData;
  }

  @Override
  public void create(DocumentFormData formData) {
    // create document
    Long documentId = SQL.getSequenceNextval(ISequenceTable.TABLE_NAME);
    formData.setDocumentId(documentId);
    String documentPath = BEANS.get(DocumentStoreService.class).store(formData.getDocument().getValue(), formData.getCapturedDate().getValue(), documentId);
    formData.setDocumentPath(documentPath);

    // create document
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("INSERT INTO ").append(TABLE_NAME).append(" (");
    statementBuilder.append(SqlFramentBuilder.columns(DOCUMENT_NR, ABSTRACT, DOCUMENT_DATE, INSERT_DATE, VALID_DATE, DOCUMENT_URL, ORIGINAL_STORAGE));
    statementBuilder.append(") VALUES (");
    statementBuilder.append(":documentId, :abstract, :documentDate, :capturedDate, :validDate, :documentPath, :originalStorage");
    statementBuilder.append(" )");
    SQL.insert(statementBuilder.toString(), formData);

    // partner

  }

  @Override
  public void store(DocumentFormData formData) {
    LOG.debug("Store document.");
  }

  @Override
  public DocumentFormData load(DocumentFormData formData) {
    if (!ACCESS.check(new EntityReadPermission(formData.getDocumentId()))) {
//    if (!ACCESS.check(new EntityReadPermission(formData.getDocumentId()))) {
      throw new VetoException("Access denied");
    }

    return loadTrusted(formData);

  }

  @RemoteServiceAccessDenied
  public DocumentFormData loadTrusted(DocumentFormData formData) {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("SELECT ").append(SqlFramentBuilder.columns(ABSTRACT, DOCUMENT_DATE, INSERT_DATE, VALID_DATE, DOCUMENT_URL, ORIGINAL_STORAGE));
    statementBuilder.append(" FROM ").append(TABLE_NAME).append(" WHERE ").append(DOCUMENT_NR).append(" = :documentId");
    statementBuilder.append(" INTO ").append(":abstract,  :documentDate, :capturedDate, :validDate, :documentPath, :originalStorage");
    SQL.selectInto(statementBuilder.toString(), formData);

    // partners
    for (BigDecimal partnerId : BEANS.get(DocumentPartnerService.class).getPartnerIds(formData.getDocumentId())) {
      formData.getPartners().addRow().setPartner(partnerId);
    }

    return formData;
  }
}

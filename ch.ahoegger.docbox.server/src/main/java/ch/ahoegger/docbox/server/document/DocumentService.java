package ch.ahoegger.docbox.server.document;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

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
import ch.ahoegger.docbox.server.partner.PartnerService;
import ch.ahoegger.docbox.server.security.permission.DefaultPermissionService;
import ch.ahoegger.docbox.shared.ISequenceTable;
import ch.ahoegger.docbox.shared.document.DocumentFormData;
import ch.ahoegger.docbox.shared.document.DocumentFormData.Permissions.PermissionsRowData;
import ch.ahoegger.docbox.shared.document.DocumentSearchFormData;
import ch.ahoegger.docbox.shared.document.DocumentTableData;
import ch.ahoegger.docbox.shared.document.IDocumentPartnerTable;
import ch.ahoegger.docbox.shared.document.IDocumentPermissionTable;
import ch.ahoegger.docbox.shared.document.IDocumentService;
import ch.ahoegger.docbox.shared.document.IDocumentTable;
import ch.ahoegger.docbox.shared.security.permission.EntityReadPermission;

/**
 * <h3>{@link DocumentService}</h3>
 *
 * @author Andreas Hoegger
 */
public class DocumentService implements IDocumentService, IDocumentTable {
  private static final Logger LOG = LoggerFactory.getLogger(DocumentService.class);

  @Override
  public DocumentTableData getTableData(DocumentSearchFormData formData) {
    StringBuilder sqlBuilder = new StringBuilder();
    sqlBuilder.append("SELECT ")
        .append(SqlFramentBuilder.columnsAliased(TABLE_ALIAS, DOCUMENT_NR, ABSTRACT, CONVERSATION_NR, DOCUMENT_DATE, INSERT_DATE, DOCUMENT_URL))
        .append(", ").append(SqlFramentBuilder.columnsAliased(IDocumentPermissionTable.TABLE_ALIAS + "_OWNER", IDocumentPermissionTable.USERNAME));

    sqlBuilder.append(" FROM ").append(TABLE_NAME).append(" AS ").append(TABLE_ALIAS);
    // join with permission
    sqlBuilder.append("  INNER JOIN ").append(IDocumentPermissionTable.TABLE_NAME).append(" ").append(IDocumentPermissionTable.TABLE_ALIAS)
        .append(" ON ").append(TABLE_ALIAS).append(".").append(SqlFramentBuilder.columnsAliased(TABLE_ALIAS, DOCUMENT_NR)).append(" = ")
        .append(SqlFramentBuilder.columnsAliased(IDocumentPermissionTable.TABLE_ALIAS, IDocumentPermissionTable.DOCUMENT_NR));
    // join with owner
    sqlBuilder.append(" LEFT OUTER JOIN ").append(IDocumentPermissionTable.TABLE_NAME).append(" AS ").append(IDocumentPermissionTable.TABLE_ALIAS + "_OWNER")
        .append(" ON ").append(SqlFramentBuilder.columnsAliased(IDocumentPermissionTable.TABLE_ALIAS + "_OWNER", IDocumentPermissionTable.DOCUMENT_NR)).append(" = ").append(SqlFramentBuilder.columnsAliased(TABLE_ALIAS, DOCUMENT_NR))
        .append(" AND ").append(SqlFramentBuilder.columnsAliased(IDocumentPermissionTable.TABLE_ALIAS + "_OWNER", IDocumentPermissionTable.PERMISSION)).append(" = ").append(IDocumentPermissionTable.PERMISSION_OWNER);

    sqlBuilder.append(" WHERE 1 = 1");
    // check permission
    sqlBuilder.append(" AND ").append(IDocumentPermissionTable.TABLE_ALIAS).append(".").append(IDocumentPermissionTable.USERNAME).append(" = '").append(ServerSession.get().getUserId()).append("'");
    sqlBuilder.append(" AND ").append(IDocumentPermissionTable.TABLE_ALIAS).append(".").append(IDocumentPermissionTable.PERMISSION).append(" >= ").append(IDocumentPermissionTable.PERMISSION_READ);
//

    // abstract search criteria
    if (StringUtility.hasText(formData.getAbstract().getValue())) {
      sqlBuilder.append(" AND ").append(SqlFramentBuilder.whereStringContains(TABLE_ALIAS, ABSTRACT, formData.getAbstract().getValue()));
    }
    // partner search criteria
    if (formData.getPartner().getValue() != null) {
      sqlBuilder.append(" AND EXISTS ( SELECT 1 FROM ").append(IDocumentPartnerTable.TABLE_NAME).append(" AS ").append(IDocumentPartnerTable.TABLE_ALIAS);
      sqlBuilder.append(" WHERE ").append(SqlFramentBuilder.columnsAliased(IDocumentPartnerTable.TABLE_ALIAS, IDocumentPartnerTable.PARTNER_NR)).append(" = ").append(":partner");
      sqlBuilder.append(" AND ").append(SqlFramentBuilder.columnsAliased(IDocumentPartnerTable.TABLE_ALIAS, IDocumentPartnerTable.DOCUMENT_NR)).append(" = ").append(SqlFramentBuilder.columnsAliased(TABLE_ALIAS, DOCUMENT_NR));
      sqlBuilder.append(")");
    }

    sqlBuilder.append(" INTO ");
    sqlBuilder.append(":{td.documentId}, ");
    sqlBuilder.append(":{td.abstract}, ");
    sqlBuilder.append(":{td.conversation}, ");
    sqlBuilder.append(":{td.documentDate}, ");
    sqlBuilder.append(":{td.capturedDate}, ");
    sqlBuilder.append(":{td.documentPath}, ");
    sqlBuilder.append(":{td.owner} ");
    DocumentTableData tableData = new DocumentTableData();
    SQL.selectInto(sqlBuilder.toString(),
        new NVPair("td", tableData),
        formData);

    // partners

    Arrays.stream(tableData.getRows()).forEach(row -> row.setPartner(BEANS.get(PartnerService.class).getPartners(row.getDocumentId())
        .stream().map(p -> p.getName()).reduce((p1, p2) -> p1 + ", " + p2)
        .orElse("")));

    return tableData;
  }

  @Override
  public DocumentFormData prepareCreate(DocumentFormData formData) {
    formData.getCapturedDate().setValue(new Date());
    Map<String, Integer> defaultPermissions = BEANS.get(DefaultPermissionService.class).getDefaultPermissions();
    defaultPermissions.remove(ServerSession.get().getUserId());
    defaultPermissions.put(ServerSession.get().getUserId(), IDocumentPermissionTable.PERMISSION_OWNER);
    for (Entry<String, Integer> permission : defaultPermissions.entrySet()) {
      PermissionsRowData row = formData.getPermissions().addRow();
      row.setUser(permission.getKey());
      row.setPermission(permission.getValue());

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
    BEANS.get(DocumentPartnerService.class).createDocumentPartners(documentId,
        Arrays.stream(formData.getPartners().getRows())
            .filter(r -> r.getPartner() != null)
            .map(r -> r.getPartner()).collect(Collectors.toSet()));

    // categories
    BEANS.get(DocumentCategoryService.class).createDocumentCategories(documentId, formData.getCategoriesBox().getValue());

    // permissions
    Map<String, Integer> permissions = Arrays.stream(formData.getPermissions().getRows())
        .filter(row -> StringUtility.hasText(row.getUser()) && row.getPermission() != null)
        .collect(Collectors.toMap(
            row -> row.getUser(),
            row -> row.getPermission(),
            (p1, p2) -> Math.max(p1, p2)));
    BEANS.get(DocumentPermissionService.class).createDocumentPermissions(documentId, permissions);

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
    // categories
    formData.getCategoriesBox().setValue(BEANS.get(DocumentCategoryService.class).getCategoryIds(formData.getDocumentId()));

    // permissions
    for (Entry<String, Integer> permission : BEANS.get(DocumentPermissionService.class).getPermissions(formData.getDocumentId()).entrySet()) {
      PermissionsRowData row = formData.getPermissions().addRow();
      row.setUser(permission.getKey());
      row.setPermission(permission.getValue());
    }

    return formData;
  }
}

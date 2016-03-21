package ch.ahoegger.docbox.server.document;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.holders.NVPair;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
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
import ch.ahoegger.docbox.shared.administration.user.IUserTable;
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
    Object[][] rawRes = SQL.select("SELECT * FROM " + TABLE_NAME);

    StringBuilder sqlBuilder = new StringBuilder();
    sqlBuilder.append("SELECT ")
        .append(SqlFramentBuilder.columnsAliased(TABLE_ALIAS, DOCUMENT_NR, ABSTRACT, CONVERSATION_NR, DOCUMENT_DATE, INSERT_DATE, DOCUMENT_URL))
        .append(", ").append(SqlFramentBuilder.columnsAliased(IDocumentPermissionTable.TABLE_ALIAS + "_OWNER", IDocumentPermissionTable.USERNAME));

    sqlBuilder.append(" FROM ").append(TABLE_NAME).append(" AS ").append(TABLE_ALIAS);
    // join with owner
    sqlBuilder.append(" LEFT OUTER JOIN ").append(IDocumentPermissionTable.TABLE_NAME).append(" AS ").append(IDocumentPermissionTable.TABLE_ALIAS + "_OWNER")
        .append(" ON ").append(SqlFramentBuilder.columnsAliased(IDocumentPermissionTable.TABLE_ALIAS + "_OWNER", IDocumentPermissionTable.DOCUMENT_NR)).append(" = ").append(SqlFramentBuilder.columnsAliased(TABLE_ALIAS, DOCUMENT_NR))
        .append(" AND ").append(SqlFramentBuilder.columnsAliased(IDocumentPermissionTable.TABLE_ALIAS + "_OWNER", IDocumentPermissionTable.PERMISSION)).append(" = ").append(IDocumentPermissionTable.PERMISSION_OWNER);

    sqlBuilder.append(" WHERE 1 = 1");
    // permission
    // either admin
    sqlBuilder.append(" AND ( EXISTS ( SELECT 1 FROM ").append(IUserTable.TABLE_NAME).append(" AS ").append(IUserTable.TABLE_ALIAS)
        .append(" WHERE ").append(SqlFramentBuilder.columnsAliased(IUserTable.TABLE_ALIAS, IUserTable.USERNAME)).append(" = :username")
        .append(" AND ").append(SqlFramentBuilder.columnsAliased(IUserTable.TABLE_ALIAS, IUserTable.ADMINISTRATOR))
        .append(")");
    // or permission
    sqlBuilder.append(" OR EXISTS (SELECT 1 FROM ").append(IDocumentPermissionTable.TABLE_NAME).append(" AS ").append(IDocumentPermissionTable.TABLE_ALIAS + "_1")
        .append(" WHERE ").append(SqlFramentBuilder.columnsAliased(IDocumentPermissionTable.TABLE_ALIAS + "_1", IDocumentPermissionTable.DOCUMENT_NR)).append(" = ").append(SqlFramentBuilder.columnsAliased(TABLE_ALIAS, DOCUMENT_NR))
        .append(" AND ").append(SqlFramentBuilder.columnsAliased(IDocumentPermissionTable.TABLE_ALIAS + "_1", IDocumentPermissionTable.USERNAME)).append(" = :username")
        .append(" AND ").append(SqlFramentBuilder.columnsAliased(IDocumentPermissionTable.TABLE_ALIAS + "_1", IDocumentPermissionTable.PERMISSION)).append(" >= ").append(IDocumentPermissionTable.PERMISSION_READ)
        .append(")");
    sqlBuilder.append(" )");

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
    // document date from
    if (formData.getDocumentDateFrom().getValue() != null) {
      sqlBuilder.append(" AND ").append(SqlFramentBuilder.columnsAliased(TABLE_ALIAS, DOCUMENT_DATE)).append(" >= ").append(":documentDateFrom");
    }
    // document date to
    if (formData.getDocumentDateTo().getValue() != null) {
      sqlBuilder.append(" AND ").append(SqlFramentBuilder.columnsAliased(TABLE_ALIAS, DOCUMENT_DATE)).append(" <= ").append(":documentDateTo");
    }
    // active document (valid date)
    if (formData.getActiveBox().getValue() != null) {
      switch (formData.getActiveBox().getValue()) {
        case Active:
          sqlBuilder.append(" AND (").append(SqlFramentBuilder.columnsAliased(TABLE_ALIAS, VALID_DATE)).append(" >= ").append(":today")
              .append(" OR ").append(SqlFramentBuilder.columnsAliased(TABLE_ALIAS, VALID_DATE)).append(" IS NULL)");
          break;
        case Inactive:
          sqlBuilder.append(" AND ").append(SqlFramentBuilder.columnsAliased(TABLE_ALIAS, VALID_DATE)).append(" < ").append(":today");
          break;
      }
    }
    // owner
    if (StringUtility.hasText(formData.getOwner().getValue())) {
      sqlBuilder.append(" AND ").append(SqlFramentBuilder.columnsAliased(IDocumentPermissionTable.TABLE_ALIAS + "_OWNER", IDocumentPermissionTable.USERNAME))
          .append(" = :owner");

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
    System.out.println("USERNAME: " + ServerSession.get().getUserId());
    Calendar cal = Calendar.getInstance();
    DateUtility.truncCalendar(cal);
    SQL.selectInto(sqlBuilder.toString(),
        new NVPair("td", tableData),
        new NVPair("username", ServerSession.get().getUserId()),
        new NVPair("today", cal.getTime()),
        formData);

    // partners

    Arrays.stream(tableData.getRows()).forEach(row -> row.setPartner(BEANS.get(PartnerService.class).getPartners(row.getDocumentId())
        .stream().map(p -> p.getName()).reduce((p1, p2) -> p1 + ", " + p2)
        .orElse("")));

    return tableData;
  }

  @Override
  public DocumentFormData prepareCreate(DocumentFormData formData) {
    Calendar cal = Calendar.getInstance();
    DateUtility.truncCalendar(cal);
    formData.getCapturedDate().setValue(cal.getTime());
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
  public DocumentFormData create(DocumentFormData formData) {
    // create document
    Long documentId = SQL.getSequenceNextval(ISequenceTable.TABLE_NAME);
    formData.setDocumentId(documentId);
    String documentPath = BEANS.get(DocumentStoreService.class).store(formData.getDocument().getValue(), formData.getCapturedDate().getValue(), documentId);
    formData.setDocumentPath(documentPath);
    // reset binary resource only used for creation
    formData.getDocument().setValue(null);

    // create document
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("INSERT INTO ").append(TABLE_NAME).append(" (");
    statementBuilder.append(SqlFramentBuilder.columns(DOCUMENT_NR, ABSTRACT, DOCUMENT_DATE, INSERT_DATE, VALID_DATE, DOCUMENT_URL, ORIGINAL_STORAGE, CONVERSATION_NR));
    statementBuilder.append(") VALUES (");
    statementBuilder.append(":documentId, :abstract, :documentDate, :capturedDate, :validDate, :documentPath, :originalStorage, :conversation");
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

    return formData;

  }

  @Override
  public void store(DocumentFormData formData) {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("UPDATE ").append(TABLE_NAME).append(" SET ")
        .append(ABSTRACT).append("= :abstract, ")
        .append(DOCUMENT_DATE).append("= :documentDate, ")
        .append(VALID_DATE).append("= :validDate, ")
        .append(DOCUMENT_URL).append("= :documentPath, ")
        .append(ORIGINAL_STORAGE).append("= :originalStorage, ")
        .append(CONVERSATION_NR).append("= :conversation ")
        .append(" WHERE ").append(DOCUMENT_NR).append(" = :documentId");
    SQL.update(statementBuilder.toString(), formData);

    // categories
    BEANS.get(DocumentCategoryService.class).updateDocumentCategories(formData.getDocumentId(), formData.getCategoriesBox().getValue());

    // permissions
    Map<String, Integer> permissions = Arrays.stream(formData.getPermissions().getRows())
        .filter(row -> StringUtility.hasText(row.getUser()) && row.getPermission() != null)
        .collect(Collectors.toMap(
            row -> row.getUser(),
            row -> row.getPermission(),
            (p1, p2) -> Math.max(p1, p2)));
    BEANS.get(DocumentPermissionService.class).updateDocumentPermissions(formData.getDocumentId(), permissions);
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
    statementBuilder.append("SELECT ").append(SqlFramentBuilder.columns(ABSTRACT, DOCUMENT_DATE, INSERT_DATE, VALID_DATE, DOCUMENT_URL, ORIGINAL_STORAGE, CONVERSATION_NR));
    statementBuilder.append(" FROM ").append(TABLE_NAME).append(" WHERE ").append(DOCUMENT_NR).append(" = :documentId");
    statementBuilder.append(" INTO ").append(":abstract,  :documentDate, :capturedDate, :validDate, :documentPath, :originalStorage, :conversation");
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

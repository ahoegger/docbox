package ch.ahoegger.docbox.server.document;

import java.math.BigDecimal;
import java.security.AccessController;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.security.auth.Subject;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.holders.NVPair;
import org.eclipse.scout.rt.platform.job.IFuture;
import org.eclipse.scout.rt.platform.job.Jobs;
import org.eclipse.scout.rt.platform.resource.BinaryResource;
import org.eclipse.scout.rt.platform.util.BooleanUtility;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.platform.util.TypeCastUtility;
import org.eclipse.scout.rt.platform.util.concurrent.IRunnable;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.eclipse.scout.rt.server.context.ServerRunContexts;
import org.eclipse.scout.rt.server.jdbc.SQL;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;
import org.eclipse.scout.rt.shared.servicetunnel.RemoteServiceAccessDenied;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.server.ServerSession;
import ch.ahoegger.docbox.server.document.store.DocumentStoreService;
import ch.ahoegger.docbox.server.ocr.DocumentOcrService;
import ch.ahoegger.docbox.server.ocr.ParseDocumentJob;
import ch.ahoegger.docbox.server.partner.PartnerService;
import ch.ahoegger.docbox.server.security.permission.DefaultPermissionService;
import ch.ahoegger.docbox.shared.ISequenceTable;
import ch.ahoegger.docbox.shared.administration.user.IUserTable;
import ch.ahoegger.docbox.shared.backup.IBackupService;
import ch.ahoegger.docbox.shared.document.DocumentFormData;
import ch.ahoegger.docbox.shared.document.DocumentFormData.Permissions.PermissionsRowData;
import ch.ahoegger.docbox.shared.document.DocumentSearchFormData;
import ch.ahoegger.docbox.shared.document.DocumentTableData;
import ch.ahoegger.docbox.shared.document.IDocumentCategoryTable;
import ch.ahoegger.docbox.shared.document.IDocumentPartnerTable;
import ch.ahoegger.docbox.shared.document.IDocumentPermissionTable;
import ch.ahoegger.docbox.shared.document.IDocumentService;
import ch.ahoegger.docbox.shared.document.IDocumentTable;
import ch.ahoegger.docbox.shared.ocr.IDocumentOcrTable;
import ch.ahoegger.docbox.shared.security.permission.AdministratorPermission;
import ch.ahoegger.docbox.shared.security.permission.EntityReadPermission;
import ch.ahoegger.docbox.shared.util.SqlFramentBuilder;

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
    // join with owner
    sqlBuilder.append(" LEFT OUTER JOIN ").append(IDocumentPermissionTable.TABLE_NAME).append(" AS ").append(IDocumentPermissionTable.TABLE_ALIAS + "_OWNER")
        .append(" ON ").append(SqlFramentBuilder.columnsAliased(IDocumentPermissionTable.TABLE_ALIAS + "_OWNER", IDocumentPermissionTable.DOCUMENT_NR)).append(" = ").append(SqlFramentBuilder.columnsAliased(TABLE_ALIAS, DOCUMENT_NR))
        .append(" AND ").append(SqlFramentBuilder.columnsAliased(IDocumentPermissionTable.TABLE_ALIAS + "_OWNER", IDocumentPermissionTable.PERMISSION)).append(" = ").append(IDocumentPermissionTable.PERMISSION_OWNER);

    // join with ocr
    sqlBuilder.append(" LEFT OUTER JOIN ").append(IDocumentOcrTable.TABLE_NAME).append(" AS ").append(IDocumentOcrTable.TABLE_ALIAS)
        .append(" ON ").append(SqlFramentBuilder.columnsAliased(TABLE_ALIAS, DOCUMENT_NR)).append(" = ").append(SqlFramentBuilder.columnsAliased(IDocumentOcrTable.TABLE_ALIAS, IDocumentOcrTable.DOCUMENT_NR));

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
    // conversation search criteria
    if (formData.getConversation().getValue() != null) {
      sqlBuilder.append(" AND ").append(SqlFramentBuilder.columnsAliased(TABLE_ALIAS, CONVERSATION_NR)).append(" = ").append(":conversation");
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
        case TRUE:
          sqlBuilder.append(" AND (").append(SqlFramentBuilder.columnsAliased(TABLE_ALIAS, VALID_DATE)).append(" >= ").append(":today")
              .append(" OR ").append(SqlFramentBuilder.columnsAliased(TABLE_ALIAS, VALID_DATE)).append(" IS NULL)");
          break;
        case FALSE:
          sqlBuilder.append(" AND ").append(SqlFramentBuilder.columnsAliased(TABLE_ALIAS, VALID_DATE)).append(" < ").append(":today");
          break;
      }
    }
    // owner
    if (StringUtility.hasText(formData.getOwner().getValue())) {
      sqlBuilder.append(" AND ").append(SqlFramentBuilder.columnsAliased(IDocumentPermissionTable.TABLE_ALIAS + "_OWNER", IDocumentPermissionTable.USERNAME))
          .append(" = :owner");

    }
// search criteria cagegories
    if (formData.getCategoriesBox().getValue() != null) {
      sqlBuilder.append(" AND ( SELECT COUNT(1) FROM ").append(IDocumentCategoryTable.TABLE_NAME).append(" AS ").append(IDocumentCategoryTable.TABLE_ALIAS)
          .append(" WHERE ").append(SqlFramentBuilder.columnsAliased(IDocumentCategoryTable.TABLE_ALIAS, IDocumentCategoryTable.DOCUMENT_NR)).append(" = ").append(SqlFramentBuilder.columnsAliased(TABLE_ALIAS, DOCUMENT_NR))
          .append(" AND ").append(SqlFramentBuilder.columnsAliased(IDocumentCategoryTable.TABLE_ALIAS, IDocumentCategoryTable.CATEGORY_NR)).append(" IN (")
          .append(
              formData.getCategoriesBox().getValue()
                  .stream()
                  .filter(key -> key != null)
                  .map(key -> key.toString())
                  .collect(Collectors.joining(", ")))
          .append("))")
          .append(" = ").append(formData.getCategoriesBox().getValue().size());
    }

    // search criteria ocr text
    List<String> lowerCaseOcrSearchTerms = Arrays.stream(formData.getOcrSearchTable().getRows())
        .map(r -> r.getSearchText())
        .filter(text -> StringUtility.hasText(text))
        .map(text -> text.toLowerCase())
        .collect(Collectors.toList());
    if (lowerCaseOcrSearchTerms.size() > 0) {
      for (String ocrSearchItem : lowerCaseOcrSearchTerms) {
        sqlBuilder.append(" AND ").append(SqlFramentBuilder.whereStringContains(IDocumentOcrTable.TABLE_ALIAS, IDocumentOcrTable.TEXT, ocrSearchItem));
      }
    }
    // active document (valid date)
    if (formData.getParsedContentBox().getValue() != null) {
      switch (formData.getParsedContentBox().getValue()) {
        case TRUE:
          sqlBuilder.append(" AND ").append(SqlFramentBuilder.columnsAliased(IDocumentOcrTable.TABLE_ALIAS, IDocumentOcrTable.TEXT)).append(" IS NOT NULL ");
          break;
        case FALSE:
          sqlBuilder.append(" AND ").append(SqlFramentBuilder.columnsAliased(IDocumentOcrTable.TABLE_ALIAS, IDocumentOcrTable.TEXT)).append(" IS NULL ");
          break;
      }
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

    Arrays.stream(tableData.getRows()).map(row -> row.getAbstract() + " - " + row.getConversation()).forEach(System.out::println);

    return tableData;
  }

  @Override
  public DocumentFormData prepareCreate(DocumentFormData formData) {
    Calendar cal = Calendar.getInstance();
    DateUtility.truncCalendar(cal);
    formData.getCapturedDate().setValue(cal.getTime());
    formData.getParseOcr().setValue(true);
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
  public DocumentFormData create(final DocumentFormData formData) {
    // create document
    BigDecimal documentId = BigDecimal.valueOf(SQL.getSequenceNextval(ISequenceTable.TABLE_NAME));
    formData.setDocumentId(documentId);
    final BinaryResource binaryResource = formData.getDocument().getValue();
    String documentPath = BEANS.get(DocumentStoreService.class).store(binaryResource, formData.getCapturedDate().getValue(), documentId);
    formData.setDocumentPath(documentPath);
    // reset binary resource only used for creation
    formData.getDocument().setValue(null);

    // create document
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("INSERT INTO ").append(TABLE_NAME).append(" (");
    statementBuilder.append(SqlFramentBuilder.columns(DOCUMENT_NR, ABSTRACT, DOCUMENT_DATE, INSERT_DATE, VALID_DATE, DOCUMENT_URL, ORIGINAL_STORAGE, CONVERSATION_NR, PARSE_OCR));
    statementBuilder.append(") VALUES (");
    statementBuilder.append(":documentId, :abstract, :documentDate, :capturedDate, :validDate, :documentPath, :originalStorage, :conversation, :parseOcr");
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

    // ocr
    if (formData.getParseOcr().getValue()) {
      ParseDocumentJob job = new ParseDocumentJob(formData.getDocumentId());
      job.schedule();
    }

    // notify backup needed
    BEANS.get(IBackupService.class).notifyModification();

    return formData;

  }

  @Override
  public void store(DocumentFormData formData) {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("UPDATE ").append(TABLE_NAME).append(" SET ")
        .append(ABSTRACT).append("= :abstract, ")
        .append(DOCUMENT_DATE).append("= :documentDate, ")
        .append(VALID_DATE).append("= :validDate, ")
        .append(ORIGINAL_STORAGE).append("= :originalStorage, ")
        .append(CONVERSATION_NR).append("= :conversation, ")
        .append(PARSE_OCR).append("= :parseOcr ")
        .append(" WHERE ").append(DOCUMENT_NR).append(" = :documentId");
    SQL.update(statementBuilder.toString(), formData);

    // partner
    BEANS.get(DocumentPartnerService.class).updateDocumentPartner(formData.getDocumentId(),
        Arrays.stream(formData.getPartners().getRows()).map(row -> row.getPartner())
            .filter(pId -> pId != null)
            .collect(Collectors.toSet()));

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

    // ocr
    if (formData.getParseOcr().getValue()) {
      if (!BEANS.get(DocumentOcrService.class).exists(formData.getDocumentId())) {
        ParseDocumentJob job = new ParseDocumentJob(formData.getDocumentId());
        job.schedule();
      }
    }
    else {
      DocumentOcrService service = BEANS.get(DocumentOcrService.class);
      service.delete(formData.getDocumentId());
    }

    // notify backup needed
    BEANS.get(IBackupService.class).notifyModification();
  }

  @Override
  public DocumentFormData load(DocumentFormData formData) {
    if (!ACCESS.check(new EntityReadPermission(formData.getDocumentId()))) {
      throw new VetoException("Access denied");
    }

    return loadTrusted(formData);

  }

  @RemoteServiceAccessDenied
  public DocumentFormData loadTrusted(DocumentFormData formData) {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("SELECT ").append(SqlFramentBuilder.columns(ABSTRACT, DOCUMENT_DATE, INSERT_DATE, VALID_DATE, DOCUMENT_URL, ORIGINAL_STORAGE, CONVERSATION_NR, PARSE_OCR));
    statementBuilder.append(" FROM ").append(TABLE_NAME).append(" WHERE ").append(DOCUMENT_NR).append(" = :documentId");
    statementBuilder.append(" INTO ").append(":abstract,  :documentDate, :capturedDate, :validDate, :documentPath, :originalStorage, :conversation, :parseOcr");
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
    // ocr
    if (BooleanUtility.nvl(formData.getParseOcr().getValue(), false)) {
      formData.setHasOcrText(BEANS.get(DocumentOcrService.class).exists(formData.getDocumentId()));
    }
    else {
      formData.setHasOcrText(false);
    }

    return formData;
  }

  @Override
  public void buildOcrOfMissingDocuments() {

    buildOcrOfMissingDocuments(null);
  }

  @Override
  public void buildOcrOfMissingDocuments(List<BigDecimal> documentIdsRaw) {
    buildOcrOfMissingDocumentsInternal(documentIdsRaw);
  }

  protected IFuture<Void> buildOcrOfMissingDocumentsInternal(List<BigDecimal> documentIdsRaw) {
    if (!ACCESS.check(new AdministratorPermission())) {
      throw new VetoException("Access denied");
    }
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("SELECT ").append(SqlFramentBuilder.columnsAliased(TABLE_ALIAS, DOCUMENT_NR))
        .append(" FROM ").append(TABLE_NAME).append(" AS ").append(TABLE_ALIAS)
        .append(" LEFT JOIN ").append(IDocumentOcrTable.TABLE_NAME).append(" AS ").append(IDocumentOcrTable.TABLE_ALIAS)
        .append(" ON ").append(SqlFramentBuilder.columnsAliased(TABLE_ALIAS, DOCUMENT_NR)).append(" = ").append(SqlFramentBuilder.columnsAliased(IDocumentOcrTable.TABLE_ALIAS, IDocumentOcrTable.DOCUMENT_NR))
        .append(" WHERE ").append(SqlFramentBuilder.columnsAliased(IDocumentOcrTable.TABLE_ALIAS, IDocumentOcrTable.DOCUMENT_NR)).append(" IS NULL")
        .append(" AND ").append(PARSE_OCR);
    if (documentIdsRaw != null) {
      statementBuilder.append(" AND ").append(SqlFramentBuilder.columnsAliased(TABLE_ALIAS, DOCUMENT_NR)).append(" IN (")
          .append(documentIdsRaw.stream().map(id -> id.toString()).collect(Collectors.joining(", ")))
          .append(")");
    }
    Object[][] rawResult = SQL.select(statementBuilder.toString());

    final List<BigDecimal> documentIds = Arrays.stream(rawResult).map(row -> TypeCastUtility.castValue(row[0], BigDecimal.class)).collect(Collectors.toList());

    return Jobs.schedule(new IRunnable() {
      @Override
      public void run() throws Exception {

        for (BigDecimal docId : documentIds) {
          try {
            LOG.info("build ocr for {}.", docId);
            new ParseDocumentJob(docId).schedule().awaitDone();
          }
          catch (Exception e) {
            LOG.error(String.format("Cold not parse document with id '%s'.", docId), e);
          }
        }
      }
    }, Jobs.newInput().withRunContext(
        ServerRunContexts.empty()
            .withSubject(Subject.getSubject(AccessController.getContext()))));
  }

  @Override
  public void deletePasedConent(List<BigDecimal> documentIds) {
    if (!ACCESS.check(new AdministratorPermission())) {
      throw new VetoException("Access denied");
    }
    for (BigDecimal id : documentIds) {
      DocumentOcrService service = BEANS.get(DocumentOcrService.class);
      service.delete(id);
    }

  }

  public void delete(BigDecimal documentId) {
    if (!ACCESS.check(new AdministratorPermission())) {
      throw new VetoException("Access denied");
    }
    DocumentFormData documentData = new DocumentFormData();
    documentData.setDocumentId(documentId);
    documentData = load(documentData);

    // document file
    BEANS.get(DocumentStoreService.class).delete(documentData.getDocumentPath());

    // partner
    BEANS.get(DocumentPartnerService.class).deleteByDocumentId(documentData.getDocumentId());

    // categories
    BEANS.get(DocumentCategoryService.class).deleteByDocumentId(documentData.getDocumentId());

    // permissions
    BEANS.get(DocumentPermissionService.class).deleteDocumentPermissions(documentData.getDocumentId());

    // ocr
    deletePasedConent(CollectionUtility.arrayList(documentData.getDocumentId()));

    // notify backup needed
    BEANS.get(IBackupService.class).notifyModification();
  }
}

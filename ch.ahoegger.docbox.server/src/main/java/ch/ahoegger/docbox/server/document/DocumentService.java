package ch.ahoegger.docbox.server.document;

import java.math.BigDecimal;
import java.security.AccessController;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.security.auth.Subject;

import org.ch.ahoegger.docbox.server.or.app.tables.DocboxUser;
import org.ch.ahoegger.docbox.server.or.app.tables.Document;
import org.ch.ahoegger.docbox.server.or.app.tables.DocumentCategory;
import org.ch.ahoegger.docbox.server.or.app.tables.DocumentOcr;
import org.ch.ahoegger.docbox.server.or.app.tables.DocumentPartner;
import org.ch.ahoegger.docbox.server.or.app.tables.DocumentPermission;
import org.ch.ahoegger.docbox.server.or.app.tables.records.DocumentRecord;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingStatus;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.job.IFuture;
import org.eclipse.scout.rt.platform.job.Jobs;
import org.eclipse.scout.rt.platform.resource.BinaryResource;
import org.eclipse.scout.rt.platform.status.IStatus;
import org.eclipse.scout.rt.platform.util.BooleanUtility;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.platform.util.concurrent.IRunnable;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.eclipse.scout.rt.server.context.ServerRunContexts;
import org.eclipse.scout.rt.server.jdbc.SQL;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;
import org.eclipse.scout.rt.shared.servicetunnel.RemoteServiceAccessDenied;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record2;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.SelectConditionStep;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.or.definition.table.ISequenceTable;
import ch.ahoegger.docbox.server.ServerSession;
import ch.ahoegger.docbox.server.document.store.DocumentStoreService;
import ch.ahoegger.docbox.server.ocr.DocumentOcrService;
import ch.ahoegger.docbox.server.ocr.ParseDocumentJob;
import ch.ahoegger.docbox.server.partner.PartnerService;
import ch.ahoegger.docbox.server.security.permission.DefaultPermissionService;
import ch.ahoegger.docbox.server.util.FieldValidator;
import ch.ahoegger.docbox.server.util.FutureUtility;
import ch.ahoegger.docbox.server.util.NullFuture;
import ch.ahoegger.docbox.shared.backup.IBackupService;
import ch.ahoegger.docbox.shared.document.DocumentFormData;
import ch.ahoegger.docbox.shared.document.DocumentFormData.Permissions.PermissionsRowData;
import ch.ahoegger.docbox.shared.document.DocumentSearchFormData;
import ch.ahoegger.docbox.shared.document.DocumentTableData;
import ch.ahoegger.docbox.shared.document.DocumentTableData.DocumentTableRowData;
import ch.ahoegger.docbox.shared.document.IDocumentService;
import ch.ahoegger.docbox.shared.document.ReplaceDocumentFormData;
import ch.ahoegger.docbox.shared.ocr.OcrLanguageCodeType;
import ch.ahoegger.docbox.shared.partner.PartnerSearchFormData;
import ch.ahoegger.docbox.shared.security.permission.AdministratorPermission;
import ch.ahoegger.docbox.shared.security.permission.EntityReadPermission;
import ch.ahoegger.docbox.shared.security.permission.PermissionCodeType;
import ch.ahoegger.docbox.shared.util.LocalDateUtility;

/**
 * <h3>{@link DocumentService}</h3>
 *
 * @author Andreas Hoegger
 */
public class DocumentService implements IDocumentService {
  private static final Logger LOG = LoggerFactory.getLogger(DocumentService.class);

  @Override
  public DocumentTableData getTableData(DocumentSearchFormData formData) {

    String username = ServerSession.get().getUserId();
    Date today = LocalDateUtility.toDate(LocalDate.now());
    Document doc = Document.DOCUMENT.as("DOC");
    DocumentPermission docPerOwner = DocumentPermission.DOCUMENT_PERMISSION.as("DOC_PER_OWNER");
    DocumentOcr docOcr = DocumentOcr.DOCUMENT_OCR.as("DOC_OCR");
    DocboxUser user = DocboxUser.DOCBOX_USER.as("USR");
    DocumentPermission docPerCheck = DocumentPermission.DOCUMENT_PERMISSION.as("DOC_PER_CHECK");

    DSLContext dsl = DSL.using(SQL.getConnection(), SQLDialect.DERBY);
    // conditions
    List<Condition> conditions = new ArrayList<>();
    // abstract search criteria
    if (StringUtility.hasText(formData.getAbstract().getValue())) {
      conditions.add(doc.ABSTRACT.lower().contains(formData.getAbstract().getValue().toLowerCase()));
    }

    // partner search criteria

    if (formData.getPartner().getValue() != null) {
      DocumentPartner docPartner = DocumentPartner.DOCUMENT_PARTNER.as("DOC_PAR");
      conditions.add(DSL.exists(
          dsl.selectOne()
              .from(docPartner)
              .where(
                  docPartner.PARTNER_NR.eq(formData.getPartner().getValue())
                      .and(docPartner.DOCUMENT_NR.eq(doc.DOCUMENT_NR)))));
    }
    // conversation search criteria
    if (formData.getConversation().getValue() != null) {
      conditions.add(doc.CONVERSATION_NR.eq(formData.getConversation().getValue()));
    }
    // document date from
    if (formData.getDocumentDateFrom().getValue() != null) {
      conditions.add(doc.DOCUMENT_DATE.ge(formData.getDocumentDateFrom().getValue()));
    }
    // document date to
    if (formData.getDocumentDateTo().getValue() != null) {
      conditions.add(doc.DOCUMENT_DATE.le(formData.getDocumentDateTo().getValue()));
    }
    // active document (valid date)
    if (formData.getActiveBox().getValue() != null) {
      switch (formData.getActiveBox().getValue()) {
        case TRUE:
          conditions.add(doc.VALID_DATE.ge(today)
              .or(doc.VALID_DATE.isNull()));
          break;
        case FALSE:
          conditions.add(doc.VALID_DATE.lessThan(today));
          break;
      }
    }
    // owner
    if (StringUtility.hasText(formData.getOwner().getValue())) {
      conditions.add(docPerOwner.USERNAME.eq(formData.getOwner().getValue()));

    }
    // search criteria cagegories
    if (formData.getCategoriesBox().getValue() != null) {
      DocumentCategory docCat = DocumentCategory.DOCUMENT_CATEGORY.as("DOC_CAT");
      conditions.add(DSL.selectCount().from(docCat)
          .where(docCat.DOCUMENT_NR.eq(doc.DOCUMENT_NR)
              .and(docCat.CATEGORY_NR.in(formData.getCategoriesBox().getValue())))
          .asField().eq(formData.getCategoriesBox().getValue().size()));

    }

    // search criteria ocr text
    List<String> lowerCaseOcrSearchTerms = Arrays.stream(formData.getOcrSearchTable().getRows())
        .map(r -> r.getSearchText())
        .filter(text -> StringUtility.hasText(text))
        .map(text -> text.toLowerCase())
        .collect(Collectors.toList());
    if (lowerCaseOcrSearchTerms.size() > 0) {
      Condition cond = DSL.trueCondition();
      for (String ocrSearchItem : lowerCaseOcrSearchTerms) {
        cond = cond.and(docOcr.TEXT.lower().contains(ocrSearchItem));
      }
      conditions.add(cond);
    }
    // active document (valid date)
    if (formData.getParsedContentBox().getValue() != null) {
      switch (formData.getParsedContentBox().getValue()) {
        case TRUE:
          conditions.add(docOcr.TEXT.isNotNull());
          break;
        case FALSE:
          conditions.add(docOcr.TEXT.isNull());
          break;
      }
    }
    // search criteria parse failure
    if (formData.getParseFailure().getValue() != null && formData.getParseFailure().getValue()) {
      conditions.add(docOcr.FAILED_REASON.isNotNull());
    }

    SelectConditionStep<Record> query = dsl.select(doc.DOCUMENT_NR, doc.ABSTRACT, doc.CONVERSATION_NR, doc.DOCUMENT_DATE, doc.INSERT_DATE, doc.DOCUMENT_URL)
        .select(docOcr.PARSE_COUNT)
        .select(docPerOwner.USERNAME)
        .from(doc)
        .leftOuterJoin(docPerOwner)
        .on(docPerOwner.DOCUMENT_NR.eq(doc.DOCUMENT_NR).and(docPerOwner.PERMISSION.eq(PermissionCodeType.OwnerCode.ID)))
        .leftOuterJoin(docOcr)
        .on(doc.DOCUMENT_NR.eq(docOcr.DOCUMENT_NR))
        .where(conditions)
        .andExists(
            dsl.selectOne()
                .from(user)
                .where(
                    user.USERNAME.eq(username)
                        .and(user.ADMINISTRATOR))
                .orExists(
                    dsl.selectOne()
                        .from(docPerCheck)
                        .where(
                            doc.DOCUMENT_NR.eq(docPerCheck.DOCUMENT_NR)
                                .and(docPerCheck.USERNAME.eq(username))
                                .and(docPerCheck.PERMISSION.ge(PermissionCodeType.ReadCode.ID)))));

    DocumentTableData tableData = new DocumentTableData();

    Result<Record> fetchResult = query.fetch();
    tableData.setRows(
        fetchResult
            .stream()
            .map(rec -> {
              DocumentTableRowData row = new DocumentTableRowData();
              row.setDocumentId(rec.get(doc.DOCUMENT_NR));
              row.setAbstract(rec.get(doc.ABSTRACT));
              row.setConversation(rec.get(doc.CONVERSATION_NR));
              row.setDocumentDate(rec.get(doc.DOCUMENT_DATE));
              row.setCapturedDate(rec.get(doc.INSERT_DATE));
              row.setDocumentPath(rec.get(doc.DOCUMENT_URL));
              row.setOcrParseCount(rec.get(docOcr.PARSE_COUNT));
              row.setOwner(rec.get(docPerOwner.USERNAME));

              PartnerSearchFormData partnerSeachData = new PartnerSearchFormData();
              partnerSeachData.setDocumentId(row.getDocumentId());
              row.setPartner(Arrays.stream(BEANS.get(PartnerService.class).getTableData(partnerSeachData).getRows())
                  .map(r -> r.getName())
                  .reduce((p1, p2) -> p1 + ", " + p2)
                  .orElse(""));

              return row;
            })
            .collect(Collectors.toList())
            .toArray(new DocumentTableRowData[]{}));

    return tableData;
  }

  @Override
  public DocumentFormData prepareCreate(DocumentFormData formData) {
    Calendar cal = Calendar.getInstance();
    DateUtility.truncCalendar(cal);
    formData.getCapturedDate().setValue(cal.getTime());
    Map<String, Integer> defaultPermissions = BEANS.get(DefaultPermissionService.class).getDefaultPermissions();
    defaultPermissions.remove(ServerSession.get().getUserId());
    defaultPermissions.put(ServerSession.get().getUserId(), PermissionCodeType.OwnerCode.ID);
    for (Entry<String, Integer> permission : defaultPermissions.entrySet()) {
      PermissionsRowData row = formData.getPermissions().addRow();
      row.setUser(permission.getKey());
      row.setPermission(permission.getValue());

    }
    formData.getParseOcr().setValue(true);
    formData.getOcrLanguage().setValue(OcrLanguageCodeType.GermanCode.ID);

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
    int rowCount = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .executeInsert(toRecord(formData.getDocumentId(), formData.getAbstract().getValue(), formData.getDocumentDate().getValue(), formData.getCapturedDate().getValue(), formData.getValidDate().getValue(), formData.getDocumentPath(),
            formData.getOriginalStorage().getValue(), formData.getConversation().getValue(), formData.getParseOcr().getValue(), formData.getOcrLanguage().getValue()));
    if (rowCount < 1) {
      return null;
    }

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
      ParseDocumentJob job = new ParseDocumentJob(formData.getDocumentId(), OcrLanguageCodeType.GermanCode.ID);
      job.schedule();
    }

    // notify backup needed
    BEANS.get(IBackupService.class).notifyModification();

    return formData;

  }

  @Override
  public void store(DocumentFormData formData) {
    storeInternal(formData);
  }

  protected IFuture<Void> storeInternal(DocumentFormData formData) {

    Document table = Document.DOCUMENT;
    DocumentRecord doc = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .fetchOne(table, table.DOCUMENT_NR.eq(formData.getDocumentId()));

    if (doc == null) {
      return new NullFuture<>();
    }
    // validate
    FieldValidator validator = new FieldValidator();
    validator.add(FieldValidator.unmodifiableValidator(table.DOCUMENT_URL, formData.getDocumentPath()));
    validator.add(FieldValidator.unmodifiableValidator(table.INSERT_DATE, formData.getCapturedDate().getValue()));
    IStatus validateStatus = validator.validate(doc);
    if (!validateStatus.isOK()) {
      throw new VetoException(new ProcessingStatus(validateStatus));
    }

    FieldValidator ocrRequiredValidator = new FieldValidator();
    ocrRequiredValidator.add(FieldValidator.unmodifiableValidator(table.OCR_LANGUAGE, formData.getOcrLanguage().getValue()));
    ocrRequiredValidator.add(FieldValidator.unmodifiableValidator(table.PARSE_OCR, formData.getParseOcr().getValue()));

    boolean updateOcr = !ocrRequiredValidator.validate(doc).isOK();

    int rowCount = mapToRecord(doc, formData.getDocumentId(), formData.getAbstract().getValue(), formData.getDocumentDate().getValue(), formData.getCapturedDate().getValue(), formData.getValidDate().getValue(), formData.getDocumentPath(),
        formData.getOriginalStorage().getValue(), formData.getConversation().getValue(), formData.getParseOcr().getValue(), formData.getOcrLanguage().getValue())
            .update();

    if (rowCount < 1) {
      return new NullFuture<>();
    }

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

    IFuture<Void> result = new NullFuture<>();
    // ocr
    if (updateOcr) {
      if (formData.getParseOcr().getValue()) {
        ParseDocumentJob job = new ParseDocumentJob(formData.getDocumentId(), formData.getOcrLanguage().getValue());
        result = FutureUtility.<String, Void> map(job.schedule(), (res) -> (Void) null);
      }
      else {
        DocumentOcrService service = BEANS.get(DocumentOcrService.class);
        service.delete(formData.getDocumentId());
      }
    }

    // notify backup needed
    BEANS.get(IBackupService.class).notifyModification();
    return result;
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

    Document doc = Document.DOCUMENT;
    DocumentFormData result = toFormData(DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .fetchOne(doc, doc.DOCUMENT_NR.eq(formData.getDocumentId())));

    if (result == null) {
      return null;
    }

    // partners
    for (BigDecimal partnerId : BEANS.get(DocumentPartnerService.class).getPartnerIds(result.getDocumentId())) {
      result.getPartners().addRow().setPartner(partnerId);
    }
    // categories
    result.getCategoriesBox().setValue(BEANS.get(DocumentCategoryService.class).getCategoryIds(result.getDocumentId()));

    // permissions
    for (Entry<String, Integer> permission : BEANS.get(DocumentPermissionService.class).getPermissions(result.getDocumentId()).entrySet()) {
      PermissionsRowData row = result.getPermissions().addRow();
      row.setUser(permission.getKey());
      row.setPermission(permission.getValue());
    }
    // ocr
    if (BooleanUtility.nvl(result.getParseOcr().getValue(), false)) {
      result.setHasOcrText(BEANS.get(DocumentOcrService.class).exists(result.getDocumentId()));
    }
    else {
      result.setHasOcrText(false);
    }

    return result;
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

    Document doc = Document.DOCUMENT.as("DOC");
    DocumentOcr docOcr = DocumentOcr.DOCUMENT_OCR.as("DOC_OCR");
    Condition condition = DSL.trueCondition();
    if (documentIdsRaw != null) {
      condition = doc.DOCUMENT_NR.in(documentIdsRaw);
    }

    List<Record2<BigDecimal, String>> res = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .select(doc.DOCUMENT_NR, doc.OCR_LANGUAGE)
        .from(doc)
        .leftJoin(docOcr)
        .on(doc.DOCUMENT_NR.eq(docOcr.DOCUMENT_NR))
        .where(doc.PARSE_OCR)
        .and(docOcr.DOCUMENT_NR.isNull().or(docOcr.FAILED_REASON.isNotNull().and(docOcr.PARSE_COUNT.le(5))))
        .and(condition)
        .fetch()
        .stream()
        .collect(Collectors.toList());

    return Jobs.schedule(new IRunnable() {
      @Override
      public void run() throws Exception {

        res.forEach(rec -> {
          BigDecimal docId = rec.get(doc.DOCUMENT_NR);
          String language = rec.get(doc.OCR_LANGUAGE);
          try {
            LOG.info("build ocr for {} in language '{}'.", docId, language);
            new ParseDocumentJob(docId, language).schedule().awaitDone();
          }
          catch (Exception e) {
            LOG.error(String.format("Cold not parse document with id '%s'.", docId), e);
          }
        });
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

  @Override
  public boolean delete(BigDecimal documentId) {
    if (!ACCESS.check(new AdministratorPermission())) {
      throw new VetoException("Access denied");
    }

    DocumentFormData documentData = new DocumentFormData();
    documentData.setDocumentId(documentId);
    documentData = load(documentData);
    if (documentData == null) {
      return false;
    }
    Document doc = Document.DOCUMENT;
    if (DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .delete(doc)
        .where(doc.DOCUMENT_NR.eq(documentId))
        .execute() < 1) {
      return false;
    }

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
    return true;
  }

  @Override
  public void replaceDocument(ReplaceDocumentFormData formData) {
    if (!ACCESS.check(new AdministratorPermission())) {
      throw new VetoException("Access denied");
    }
    DocumentFormData docData = new DocumentFormData();
    docData.setDocumentId(formData.getDocumentId());
    docData = loadTrusted(docData);

    // delete old
    DocumentStoreService documentStoreService = BEANS.get(DocumentStoreService.class);
    if (!documentStoreService.delete(docData.getDocumentPath())) {
      LOG.warn(String.format("Cold not delete document for replacement '%s'.", docData.getDocumentPath()));
    }
    // ocr
    deletePasedConent(CollectionUtility.arrayList(formData.getDocumentId()));

    // store new
    final BinaryResource binaryResource = formData.getDocument().getValue();
    documentStoreService.store(binaryResource, docData.getCapturedDate().getValue(), formData.getDocumentId());

    // ocr
    if (docData.getParseOcr().getValue()) {
      ParseDocumentJob job = new ParseDocumentJob(formData.getDocumentId(), OcrLanguageCodeType.GermanCode.ID);
      job.schedule();
    }
  }

  @RemoteServiceAccessDenied
  public int insert(Connection connection, BigDecimal documentId, String abstractText, Date documentDate,
      Date capturedDate, Date validDate, String docPath, String originalStorage, BigDecimal conversationId, boolean parseOcr, String ocrLanguage) {
    return DSL.using(connection, SQLDialect.DERBY)
        .executeInsert(toRecord(documentId, abstractText, documentDate, capturedDate, validDate, docPath, originalStorage, conversationId, parseOcr, ocrLanguage));
  }

  protected DocumentRecord toRecord(BigDecimal documentId, String abstractText, Date documentDate,
      Date capturedDate, Date validDate, String docPath, String originalStorage, BigDecimal conversationId, boolean parseOcr, String ocrLanguage) {
    return mapToRecord(new DocumentRecord(), documentId, abstractText, documentDate, capturedDate, validDate, docPath, originalStorage, conversationId, parseOcr, ocrLanguage);
  }

  protected DocumentRecord mapToRecord(DocumentRecord rec, BigDecimal documentId, String abstractText, Date documentDate,
      Date capturedDate, Date validDate, String docPath, String originalStorage, BigDecimal conversationId, boolean parseOcr, String ocrLanguage) {
    Document t = Document.DOCUMENT;
    return rec
        .with(t.ABSTRACT, abstractText)
        .with(t.CONVERSATION_NR, conversationId)
        .with(t.DOCUMENT_DATE, documentDate)
        .with(t.DOCUMENT_NR, documentId)
        .with(t.DOCUMENT_URL, docPath)
        .with(t.INSERT_DATE, capturedDate)
        .with(t.OCR_LANGUAGE, ocrLanguage)
        .with(t.ORIGINAL_STORAGE, originalStorage)
        .with(t.PARSE_OCR, parseOcr)
        .with(t.VALID_DATE, validDate);
  }

  protected DocumentFormData toFormData(DocumentRecord rec) {
    if (rec == null) {
      return null;
    }
    Document doc = Document.DOCUMENT;
    DocumentFormData res = new DocumentFormData();
    res.setDocumentId(rec.get(doc.DOCUMENT_NR));
    res.getAbstract().setValue(rec.get(doc.ABSTRACT));
    res.getDocumentDate().setValue(rec.get(doc.DOCUMENT_DATE));
    res.getCapturedDate().setValue(rec.get(doc.INSERT_DATE));
    res.getValidDate().setValue(rec.get(doc.VALID_DATE));
    res.setDocumentPath(rec.get(doc.DOCUMENT_URL));
    res.getOcrLanguage().setValue(rec.get(doc.OCR_LANGUAGE));
    res.getOriginalStorage().setValue(rec.get(doc.ORIGINAL_STORAGE));
    res.getConversation().setValue(rec.get(doc.CONVERSATION_NR));
    res.getParseOcr().setValue(rec.get(doc.PARSE_OCR));
    return res;
  }
}

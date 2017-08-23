package ch.ahoegger.docbox.server.app.dev;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.ch.ahoegger.docbox.server.or.app.tables.Category;
import org.ch.ahoegger.docbox.server.or.app.tables.Conversation;
import org.ch.ahoegger.docbox.server.or.app.tables.DefaultPermissionTable;
import org.ch.ahoegger.docbox.server.or.app.tables.Document;
import org.ch.ahoegger.docbox.server.or.app.tables.DocumentCategory;
import org.ch.ahoegger.docbox.server.or.app.tables.DocumentOcr;
import org.ch.ahoegger.docbox.server.or.app.tables.DocumentPartner;
import org.ch.ahoegger.docbox.server.or.app.tables.DocumentPermission;
import org.ch.ahoegger.docbox.server.or.app.tables.Entity;
import org.ch.ahoegger.docbox.server.or.app.tables.Partner;
import org.ch.ahoegger.docbox.server.or.app.tables.PrimaryKeySeq;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.CreateImmediately;
import org.eclipse.scout.rt.platform.Replace;
import org.eclipse.scout.rt.platform.config.CONFIG;
import org.eclipse.scout.rt.platform.exception.ExceptionHandler;
import org.eclipse.scout.rt.platform.resource.BinaryResource;
import org.eclipse.scout.rt.platform.resource.BinaryResources;
import org.eclipse.scout.rt.platform.util.FileUtility;
import org.eclipse.scout.rt.platform.util.IOUtility;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.platform.util.concurrent.IRunnable;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.or.definition.table.IEmployeeTable;
import ch.ahoegger.docbox.or.definition.table.IPostingGroupTable;
import ch.ahoegger.docbox.or.definition.table.ISequenceTable;
import ch.ahoegger.docbox.server.SuperUserRunContextProducer;
import ch.ahoegger.docbox.server.administration.user.UserService;
import ch.ahoegger.docbox.server.category.CategoryService;
import ch.ahoegger.docbox.server.conversation.ConversationService;
import ch.ahoegger.docbox.server.database.DerbySqlService;
import ch.ahoegger.docbox.server.document.DocumentCategoryService;
import ch.ahoegger.docbox.server.document.DocumentPartnerService;
import ch.ahoegger.docbox.server.document.DocumentPermissionService;
import ch.ahoegger.docbox.server.document.DocumentService;
import ch.ahoegger.docbox.server.document.store.DocumentStoreService;
import ch.ahoegger.docbox.server.hr.billing.PostingGroupService;
import ch.ahoegger.docbox.server.hr.employee.EmployeeService;
import ch.ahoegger.docbox.server.hr.entity.EntityService;
import ch.ahoegger.docbox.server.ocr.DocumentOcrService;
import ch.ahoegger.docbox.server.or.generator.table.ITableStatement;
import ch.ahoegger.docbox.server.partner.PartnerService;
import ch.ahoegger.docbox.server.security.SecurityService;
import ch.ahoegger.docbox.server.security.permission.DefaultPermissionService;
import ch.ahoegger.docbox.shared.hr.billing.PostingGroupCodeType.UnbilledCode;
import ch.ahoegger.docbox.shared.hr.entity.EntityTypeCodeType;
import ch.ahoegger.docbox.shared.ocr.OcrLanguageCodeType;
import ch.ahoegger.docbox.shared.security.permission.PermissionCodeType;
import ch.ahoegger.docbox.shared.util.LocalDateUtility;

/**
 * <h3>{@link DevDerbySqlService}</h3>
 *
 * @author Andreas Hoegger
 */
@CreateImmediately
@Replace
public class DevDerbySqlService extends DerbySqlService {
  private static final Logger LOG = LoggerFactory.getLogger(DevDerbySqlService.class);

  private BigDecimal documentId01;
  private BigDecimal documentId02;
  private BigDecimal documentId03;
  private BigDecimal documentId04;

  private BigDecimal categoryId01;
  private BigDecimal categoryId02;
  private BigDecimal categoryId03;

  private BigDecimal conversationId01;
  private BigDecimal conversationId02;
  private BigDecimal conversationId03;

  private BigDecimal partnerId01;
  private BigDecimal partnerId02;
  private BigDecimal partnerId03_employee;

  private BigDecimal entityId01;
  private BigDecimal entityId02;
  private BigDecimal entityId03;
  private BigDecimal entityId04;

  private BigDecimal postingGroupId01;
  private BigDecimal postingGroupId02;

  private static final LocalDate TODAY = LocalDate.now();

  private static String SYSTEM_USER = System.getProperty("user.name").toLowerCase();

  @PostConstruct
  public void setupDb() {
    if (StringUtility.isNullOrEmpty(CONFIG.getPropertyValue(DatabaseLocationProperty.class))) {
      // init in memory db
      try {
        BEANS.get(SuperUserRunContextProducer.class).produce().run(new IRunnable() {

          @Override
          public void run() throws Exception {
            initializeDatabase();
          }
        });
      }
      catch (RuntimeException e) {
        BEANS.get(ExceptionHandler.class).handle(e);
      }
    }
  }

  @Override
  protected String getConfiguredJdbcMappingName() {
    if (StringUtility.isNullOrEmpty(CONFIG.getPropertyValue(DatabaseLocationProperty.class))) {
      // in memory
      return "jdbc:derby:memory:contacts-database;create=true";
    }
    return super.getConfiguredJdbcMappingName();
  }

  protected void initializeDatabase() {

    List<ITableStatement> tableTasks = BEANS.all(ITableStatement.class);
    // create tables
    for (ITableStatement task : tableTasks) {
      task.createTable(getConnection());
    }

    // create test data
    insertSequenceInitialValue(this);
    createUsers(this);
    insertCategories(this);
    insertConversations(this);
    insertDefaultPermissions(this);
    insertPartners(this);
    insertDocuments(this);
    insertDocumentCategory(this);
    insertDocumentPartner(this);
    insertDocumentPermission(this);
    insertDocumentOcr(this);
    // hr
    insertEmployers(this);
    insertPostingGroups(this);
    insertEntities(this);
  }

  protected void insertSequenceInitialValue(ISqlService sqlService) {
    String sql = DSL.using(SQLDialect.DERBY).insertInto(PrimaryKeySeq.PRIMARY_KEY_SEQ, PrimaryKeySeq.PRIMARY_KEY_SEQ.LAST_VAL).values(BigDecimal.valueOf(2000l)).toString();
    sqlService.insert(sql);

  }

  protected void createUsers(ISqlService sqlService) {
    UserService userService = BEANS.get(UserService.class);
    userService.insert(sqlService.getConnection(), String.format("Dev.user[%s]", SYSTEM_USER), "development", SYSTEM_USER, new String(BEANS.get(SecurityService.class).createPasswordHash("".toCharArray())), true, true);
    userService.insert(sqlService.getConnection(), "Cuttis", "Bolion", "cuttis", new String(BEANS.get(SecurityService.class).createPasswordHash("pwd".toCharArray())), true, false);
    userService.insert(sqlService.getConnection(), "Bob", "Miller", "bob", new String(BEANS.get(SecurityService.class).createPasswordHash("pwd".toCharArray())), true, false);
    userService.insert(sqlService.getConnection(), "Admin", "Manager", "admin", new String(BEANS.get(SecurityService.class).createPasswordHash("manager".toCharArray())), true, true);
    userService.insert(sqlService.getConnection(), "Inactive", "Name", "inactive-user", new String(BEANS.get(SecurityService.class).createPasswordHash("pwd".toCharArray())), false, false);
  }

  protected void insertCategories(ISqlService sqlService) {
    LOG.info("SQL-DEV create rows for: {}", Category.CATEGORY.getName());

    // ids
    categoryId01 = BigDecimal.valueOf(getSequenceNextval(ISequenceTable.TABLE_NAME));
    categoryId02 = BigDecimal.valueOf(getSequenceNextval(ISequenceTable.TABLE_NAME));
    categoryId03 = BigDecimal.valueOf(getSequenceNextval(ISequenceTable.TABLE_NAME));

    CategoryService categoryService = BEANS.get(CategoryService.class);
    categoryService.insertRow(sqlService.getConnection(), categoryId01, "Work", "anything work related.", LocalDateUtility.today(), null);
    categoryService.insertRow(sqlService.getConnection(), categoryId02, "Household", "some window cleaning stuff.", LocalDateUtility.today(), null);
    categoryService.insertRow(sqlService.getConnection(), categoryId03, "Past category", "inactive category.",
        LocalDateUtility.toDate(TODAY.minusDays(150)),
        LocalDateUtility.toDate(TODAY.minusDays(10)));
  }

  protected void insertConversations(ISqlService sqlService) {
    LOG.info("SQL-DEV create rows for: {}", Conversation.CONVERSATION.getName());
    // ids
    conversationId01 = BigDecimal.valueOf(getSequenceNextval(ISequenceTable.TABLE_NAME));
    conversationId02 = BigDecimal.valueOf(getSequenceNextval(ISequenceTable.TABLE_NAME));
    conversationId03 = BigDecimal.valueOf(getSequenceNextval(ISequenceTable.TABLE_NAME));

    ConversationService conversationService = BEANS.get(ConversationService.class);
    conversationService.insert(sqlService.getConnection(), conversationId01, "House selling", "everything related with house selling.", LocalDateUtility.today(), null);
    conversationService.insert(sqlService.getConnection(), conversationId02, "Ponny order", "all documents to get a ponny.", LocalDateUtility.today(), null);
    conversationService.insert(sqlService.getConnection(), conversationId03, "Without partner rel", "all documents to get a ponny.", LocalDateUtility.today(), null);
  }

  protected void insertDefaultPermissions(ISqlService sqlService) {
    LOG.info("SQL-DEV create rows for: {}", DefaultPermissionTable.DEFAULT_PERMISSION_TABLE.getName());
    DefaultPermissionService defaultPermissionTableTask = BEANS.get(DefaultPermissionService.class);
    defaultPermissionTableTask.insertRow(sqlService.getConnection(), "admin", PermissionCodeType.WriteCode.ID);
    defaultPermissionTableTask.insertRow(sqlService.getConnection(), "bob", PermissionCodeType.ReadCode.ID);
  }

  protected void insertPartners(ISqlService sqlService) {
    LOG.info("SQL-DEV create rows for: {}", Partner.PARTNER.getName());
    // ids
    partnerId01 = BigDecimal.valueOf(getSequenceNextval(ISequenceTable.TABLE_NAME));
    partnerId02 = BigDecimal.valueOf(getSequenceNextval(ISequenceTable.TABLE_NAME));
    partnerId03_employee = BigDecimal.valueOf(getSequenceNextval(ISequenceTable.TABLE_NAME));

    BEANS.get(PartnerService.class).insert(sqlService.getConnection(), partnerId01, "Gorak Inc", "A special company", LocalDateUtility.today(), null);
    BEANS.get(PartnerService.class).insert(sqlService.getConnection(), partnerId02, "Solan Org", "Some other comapny", LocalDateUtility.today(), null);
    BEANS.get(PartnerService.class).insert(sqlService.getConnection(), partnerId03_employee, "Hans Muster", "An employee", LocalDateUtility.today(), null);
  }

  protected void insertDocuments(ISqlService sqlService) {
    LOG.info("SQL-DEV create rows for: {}", Document.DOCUMENT.getName());

    DocumentService documentService = BEANS.get(DocumentService.class);
    try {
      documentId01 = BigDecimal.valueOf(getSequenceNextval(ISequenceTable.TABLE_NAME));
      insertDocument(sqlService.getConnection(), documentService, documentId01, "A sample document",
          LocalDateUtility.today(),
          LocalDateUtility.today(),
          LocalDateUtility.today(),
          "2016_03_08_124640.pdf", null, null);

      documentId02 = BigDecimal.valueOf(getSequenceNextval(ISequenceTable.TABLE_NAME));
      insertDocument(sqlService.getConnection(), documentService, documentId02, "Bobs document",
          LocalDateUtility.toDate(TODAY.minusYears(1)),
          LocalDateUtility.today(),
          null,
          "2016_03_08_124640.pdf", null, null);
      documentId03 = BigDecimal.valueOf(getSequenceNextval(ISequenceTable.TABLE_NAME));
      insertDocument(sqlService.getConnection(), documentService, documentId03, "Multiple partner document",
          LocalDateUtility.toDate(TODAY.minusYears(3)),
          LocalDateUtility.today(),
          null,
          "2016_03_08_124640.pdf", null, null);

      documentId04 = BigDecimal.valueOf(getSequenceNextval(ISequenceTable.TABLE_NAME));
      insertDocument(sqlService.getConnection(), documentService, documentId04, "Txt document",
          LocalDateUtility.today(),
          LocalDateUtility.today(),
          null,
          "sampleTextFile.txt", null, null);

    }
    catch (IOException e) {
      LOG.error("Could not add dev documents to data store.", e);
    }
  }

  protected void insertDocument(Connection connection, DocumentService documentService, BigDecimal documentId, String abstractText, Date documentDate,
      Date insertDate, Date validDate, String fileName, String originalStorage, BigDecimal conversationId)
      throws IOException {

    // create db record
    // add document
    URL resource = DevDerbySqlService.class.getClassLoader().getResource("devDocuments/" + fileName);
    BinaryResource br = BinaryResources.create().withFilename(fileName).withContentType(FileUtility.getContentTypeForExtension(FileUtility.getFileExtension(fileName))).withContent(IOUtility.readFromUrl(resource))
        .withLastModified(System.currentTimeMillis()).build();
//    BinaryResource br = new BinaryResource(fileName, FileUtility.getContentTypeForExtension(FileUtility.getFileExtension(fileName)), IOUtility.getContent(resource.openStream()),
//        System.currentTimeMillis());
    String docPath = BEANS.get(DocumentStoreService.class).store(br, insertDate, documentId);

    documentService.insert(connection, documentId, abstractText, documentDate, insertDate, validDate, docPath, originalStorage, conversationId, true, OcrLanguageCodeType.GermanCode.ID);
  }

  protected void insertDocumentCategory(ISqlService sqlService) {
    LOG.info("SQL-DEV create rows for: {}", DocumentCategory.DOCUMENT_CATEGORY.getName());
    DocumentCategoryService documentCategoryTableTask = BEANS.get(DocumentCategoryService.class);
    documentCategoryTableTask.insert(sqlService.getConnection(), documentId01, categoryId01);
    documentCategoryTableTask.insert(sqlService.getConnection(), documentId02, categoryId02);
    documentCategoryTableTask.insert(sqlService.getConnection(), documentId02, categoryId03);
    documentCategoryTableTask.insert(sqlService.getConnection(), documentId03, categoryId01);
    documentCategoryTableTask.insert(sqlService.getConnection(), documentId03, categoryId02);
  }

  protected void insertDocumentPartner(ISqlService sqlService) {
    LOG.info("SQL-DEV create rows for: {}", DocumentPartner.DOCUMENT_PARTNER.getName());
    DocumentPartnerService documentPartnerTableTask = BEANS.get(DocumentPartnerService.class);
    documentPartnerTableTask.insert(sqlService.getConnection(), documentId01, partnerId01);
    documentPartnerTableTask.insert(sqlService.getConnection(), documentId03, partnerId01);
    documentPartnerTableTask.insert(sqlService.getConnection(), documentId03, partnerId02);
  }

  protected void insertDocumentPermission(ISqlService sqlService) {
    LOG.info("SQL-DEV create rows for: {}", DocumentPermission.DOCUMENT_PERMISSION.getName());
    DocumentPermissionService documentPermissionTableTask = BEANS.get(DocumentPermissionService.class);
    documentPermissionTableTask.insert(sqlService.getConnection(), "admin", documentId01, PermissionCodeType.WriteCode.ID);
    documentPermissionTableTask.insert(sqlService.getConnection(), "admin", documentId02, PermissionCodeType.WriteCode.ID);
    documentPermissionTableTask.insert(sqlService.getConnection(), "admin", documentId03, PermissionCodeType.WriteCode.ID);
    documentPermissionTableTask.insert(sqlService.getConnection(), "cuttis", documentId02, PermissionCodeType.ReadCode.ID);
    documentPermissionTableTask.insert(sqlService.getConnection(), "bob", documentId02, PermissionCodeType.WriteCode.ID);
  }

  protected void insertDocumentOcr(ISqlService sqlService) {
    LOG.info("SQL-DEV create rows for: {}", DocumentOcr.DOCUMENT_OCR.getName());
    DocumentOcrService task = BEANS.get(DocumentOcrService.class);
    task.insert(sqlService.getConnection(), documentId01, "parsed ocr test", true, 1, null);
    task.insert(sqlService.getConnection(), documentId02, null, true, 2, "Failure");
  }

  protected void insertEmployers(ISqlService sqlService) {
    LOG.info("SQL-DEV create rows for: {}", IEmployeeTable.TABLE_NAME);

    BEANS.get(EmployeeService.class).insert(sqlService.getConnection(), partnerId03_employee, "Hans", "Muster", "Mountainview 01 e", "CA-90501 Santa Barbara e", "12.2568.2154.69", "PC 50-101-89-7",
        LocalDateUtility.toDate(LocalDate.of(1968, 10, 02)), BigDecimal.valueOf(26.50),
        BigDecimal.valueOf(6.225), BigDecimal.valueOf(5.0), BigDecimal.valueOf(8.33),
        "Bart Simpson & Marth Simpson er", "742 Evergreen Terrace er", "Springfield er", "bart@simpson.spring", "+1 (0)7510 2152");
  }

  protected void insertPostingGroups(ISqlService sqlService) {
    LOG.info("SQL-DEV create rows for: {}", IPostingGroupTable.TABLE_NAME);

    postingGroupId01 = BigDecimal.valueOf(getSequenceNextval(ISequenceTable.TABLE_NAME));
    postingGroupId02 = BigDecimal.valueOf(getSequenceNextval(ISequenceTable.TABLE_NAME));

    BEANS.get(PostingGroupService.class).insert(sqlService.getConnection(), postingGroupId01, partnerId03_employee, null, documentId02, "September 2016", LocalDateUtility.toDate(LocalDate.of(2016, 10, 02)), BigDecimal.valueOf(9.25),
        BigDecimal.valueOf(256.5),
        BigDecimal.valueOf(230.50), BigDecimal.valueOf(-10.55),
        BigDecimal.valueOf(-5.55),
        BigDecimal.valueOf(9.87));
    BEANS.get(PostingGroupService.class).insert(sqlService.getConnection(), postingGroupId02, partnerId03_employee, null, documentId02, "Oktober 2016", LocalDateUtility.toDate(LocalDate.of(2016, 11, 02)), BigDecimal.valueOf(10.5),
        BigDecimal.valueOf(256.5),
        BigDecimal.valueOf(230.50), BigDecimal.valueOf(-10.55),
        BigDecimal.valueOf(-5.55),
        BigDecimal.valueOf(9.87));
  }

  protected void insertEntities(ISqlService sqlService) {
    LOG.info("SQL-DEV create rows for: {}", Entity.ENTITY.getName());

    entityId01 = BigDecimal.valueOf(getSequenceNextval(ISequenceTable.TABLE_NAME));
    entityId02 = BigDecimal.valueOf(getSequenceNextval(ISequenceTable.TABLE_NAME));
    entityId03 = BigDecimal.valueOf(getSequenceNextval(ISequenceTable.TABLE_NAME));
    entityId04 = BigDecimal.valueOf(getSequenceNextval(ISequenceTable.TABLE_NAME));

    EntityService entityService = BEANS.get(EntityService.class);
    entityService.insert(sqlService.getConnection(), entityId01, partnerId03_employee, postingGroupId01, EntityTypeCodeType.WorkCode.ID, LocalDateUtility.toDate(LocalDate.of(2016, 9, 04)), BigDecimal.valueOf(3.5), null, "Sept work 1");
    entityService.insert(sqlService.getConnection(), entityId02, partnerId03_employee, postingGroupId01, EntityTypeCodeType.WorkCode.ID, LocalDateUtility.toDate(LocalDate.of(2016, 9, 11)), BigDecimal.valueOf(4.25), null, "Sept work 2");
    entityService.insert(sqlService.getConnection(), entityId03, partnerId03_employee, UnbilledCode.ID, EntityTypeCodeType.WorkCode.ID, LocalDateUtility.toDate(TODAY.minusDays(10)), BigDecimal.valueOf(5.5), null, "First work");
    entityService.insert(sqlService.getConnection(), entityId04, partnerId03_employee, UnbilledCode.ID, EntityTypeCodeType.WorkCode.ID, LocalDateUtility.toDate(TODAY.minusDays(1)), BigDecimal.valueOf(2.25), null, "Second work");
    for (int i = 0; i < 30; i++) {
      createEntity(sqlService, i);
    }
  }

  protected void createEntity(ISqlService sqlService, int counter) {
    BigDecimal entityId = BigDecimal.valueOf(getSequenceNextval(ISequenceTable.TABLE_NAME));

    BEANS.get(EntityService.class).insert(sqlService.getConnection(), entityId, partnerId03_employee, UnbilledCode.ID, EntityTypeCodeType.WorkCode.ID,
        LocalDateUtility.toDate(LocalDate.of(2016, 12, 01).plusDays(counter)), BigDecimal.valueOf(2.5), null, "Dez work " + counter);

    entityId = BigDecimal.valueOf(getSequenceNextval(ISequenceTable.TABLE_NAME));
    BEANS.get(EntityService.class).insert(sqlService.getConnection(), entityId, partnerId03_employee, UnbilledCode.ID, EntityTypeCodeType.ExpenseCode.ID,
        LocalDateUtility.toDate(LocalDate.of(2016, 12, 01).plusDays(counter)), null, BigDecimal.valueOf(12.35), "Dez expense " + counter);
  }

}

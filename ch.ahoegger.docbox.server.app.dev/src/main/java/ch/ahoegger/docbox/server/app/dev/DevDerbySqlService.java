package ch.ahoegger.docbox.server.app.dev;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.server.SuperUserRunContextProducer;
import ch.ahoegger.docbox.server.database.DerbySqlService;
import ch.ahoegger.docbox.server.database.dev.initialization.CategoryTableTask;
import ch.ahoegger.docbox.server.database.dev.initialization.ConversationTableTask;
import ch.ahoegger.docbox.server.database.dev.initialization.DefaultPermissionTableTask;
import ch.ahoegger.docbox.server.database.dev.initialization.DocumentCategoryTableTask;
import ch.ahoegger.docbox.server.database.dev.initialization.DocumentPartnerTableTask;
import ch.ahoegger.docbox.server.database.dev.initialization.DocumentPermissionTableTask;
import ch.ahoegger.docbox.server.database.dev.initialization.DocumentTableTask;
import ch.ahoegger.docbox.server.database.dev.initialization.EmployeeTableTask;
import ch.ahoegger.docbox.server.database.dev.initialization.EntityTableTask;
import ch.ahoegger.docbox.server.database.dev.initialization.ITableTask;
import ch.ahoegger.docbox.server.database.dev.initialization.PartnerTableTask;
import ch.ahoegger.docbox.server.database.dev.initialization.PostingGroupTableTask;
import ch.ahoegger.docbox.server.database.dev.initialization.SequenceTask;
import ch.ahoegger.docbox.server.database.dev.initialization.UserTableTask;
import ch.ahoegger.docbox.server.document.store.DocumentStoreService;
import ch.ahoegger.docbox.server.security.SecurityService;
import ch.ahoegger.docbox.shared.ISequenceTable;
import ch.ahoegger.docbox.shared.category.ICategoryTable;
import ch.ahoegger.docbox.shared.conversation.IConversationTable;
import ch.ahoegger.docbox.shared.document.IDocumentCategoryTable;
import ch.ahoegger.docbox.shared.document.IDocumentPartnerTable;
import ch.ahoegger.docbox.shared.document.IDocumentPermissionTable;
import ch.ahoegger.docbox.shared.document.IDocumentTable;
import ch.ahoegger.docbox.shared.hr.billing.IPostingGroupTable;
import ch.ahoegger.docbox.shared.hr.billing.PostingGroupCodeType.UnbilledCode;
import ch.ahoegger.docbox.shared.hr.employee.IEmployeeTable;
import ch.ahoegger.docbox.shared.hr.entity.EntityTypeCodeType;
import ch.ahoegger.docbox.shared.hr.entity.IEntityTable;
import ch.ahoegger.docbox.shared.partner.IPartnerTable;
import ch.ahoegger.docbox.shared.security.permission.IDefaultPermissionTable;
import ch.ahoegger.docbox.shared.util.LocalDateUtility;

/**
 * <h3>{@link DevDerbySqlService}</h3>
 *
 * @author Andreas Hoegger
 */
@CreateImmediately
@Replace
public class DevDerbySqlService extends DerbySqlService {
  private static final Logger LOG = LoggerFactory.getLogger(CategoryTableTask.class);

  private BigDecimal documentId01;
  private BigDecimal documentId02;
  private BigDecimal documentId03;
  private BigDecimal documentId04;

  private Long categoryId01;
  private Long categoryId02;
  private Long categoryId03;

  private Long conversationId01;
  private Long conversationId02;
  private Long conversationId03;

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

    List<ITableTask> tableTasks = BEANS.all(ITableTask.class);
    // create tables
    for (ITableTask task : tableTasks) {
      task.createTable(this);
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
    // hr
    insertEmployers(this);
    insertPostingGroups(this);
    insertEntities(this);
  }

  protected void insertSequenceInitialValue(ISqlService sqlService) {
    BEANS.get(SequenceTask.class).insertInitialValue(sqlService, 0);

  }

  protected void createUsers(ISqlService sqlService) {
    UserTableTask userTableTask = BEANS.get(UserTableTask.class);
    userTableTask.insertUser(sqlService, String.format("Dev.user[%s]", SYSTEM_USER), "development", SYSTEM_USER, new String(BEANS.get(SecurityService.class).createPasswordHash("".toCharArray())), true, true);
    userTableTask.insertUser(sqlService, "Cuttis", "Bolion", "cuttis", new String(BEANS.get(SecurityService.class).createPasswordHash("pwd".toCharArray())), true, false);
    userTableTask.insertUser(sqlService, "Bob", "Miller", "bob", new String(BEANS.get(SecurityService.class).createPasswordHash("pwd".toCharArray())), true, false);
    userTableTask.insertUser(sqlService, "Admin", "Manager", "admin", new String(BEANS.get(SecurityService.class).createPasswordHash("manager".toCharArray())), true, true);
  }

  protected void insertCategories(ISqlService sqlService) {
    LOG.info("SQL-DEV create rows for: {}", ICategoryTable.TABLE_NAME);

    // ids
    categoryId01 = getSequenceNextval(ISequenceTable.TABLE_NAME);
    categoryId02 = getSequenceNextval(ISequenceTable.TABLE_NAME);
    categoryId03 = getSequenceNextval(ISequenceTable.TABLE_NAME);

    CategoryTableTask categoryTableTask = BEANS.get(CategoryTableTask.class);
    categoryTableTask.createCategoryRow(sqlService, categoryId01, "Work", "anything work related.", LocalDateUtility.today(), null);
    categoryTableTask.createCategoryRow(sqlService, categoryId02, "Household", "some window cleaning stuff.", LocalDateUtility.today(), null);
    categoryTableTask.createCategoryRow(sqlService, categoryId03, "Past category", "inactive category.",
        LocalDateUtility.toDate(TODAY.minusDays(150)),
        LocalDateUtility.toDate(TODAY.minusDays(10)));
  }

  protected void insertConversations(ISqlService sqlService) {
    LOG.info("SQL-DEV create rows for: {}", IConversationTable.TABLE_NAME);
    // ids
    conversationId01 = getSequenceNextval(ISequenceTable.TABLE_NAME);
    conversationId02 = getSequenceNextval(ISequenceTable.TABLE_NAME);
    conversationId03 = getSequenceNextval(ISequenceTable.TABLE_NAME);

    ConversationTableTask conversationTableTask = BEANS.get(ConversationTableTask.class);
    conversationTableTask.createConversationRow(sqlService, conversationId01, "House selling", "everything related with house selling.", LocalDateUtility.today(), null);
    conversationTableTask.createConversationRow(sqlService, conversationId02, "Ponny order", "all documents to get a ponny.", LocalDateUtility.today(), null);
    conversationTableTask.createConversationRow(sqlService, conversationId03, "Without partner rel", "all documents to get a ponny.", LocalDateUtility.today(), null);
  }

  protected void insertDefaultPermissions(ISqlService sqlService) {
    LOG.info("SQL-DEV create rows for: {}", IDefaultPermissionTable.TABLE_NAME);
    DefaultPermissionTableTask defaultPermissionTableTask = BEANS.get(DefaultPermissionTableTask.class);
    defaultPermissionTableTask.createDefaultPermissionRow(sqlService, "admin", IDocumentPermissionTable.PERMISSION_WRITE);
    defaultPermissionTableTask.createDefaultPermissionRow(sqlService, "bob", IDocumentPermissionTable.PERMISSION_READ);
  }

  protected void insertPartners(ISqlService sqlService) {
    LOG.info("SQL-DEV create rows for: {}", IPartnerTable.TABLE_NAME);
    // ids
    partnerId01 = BigDecimal.valueOf(getSequenceNextval(ISequenceTable.TABLE_NAME));
    partnerId02 = BigDecimal.valueOf(getSequenceNextval(ISequenceTable.TABLE_NAME));
    partnerId03_employee = BigDecimal.valueOf(getSequenceNextval(ISequenceTable.TABLE_NAME));

    PartnerTableTask partnerTableTask = BEANS.get(PartnerTableTask.class);
    partnerTableTask.createPartnerRow(sqlService, partnerId01, "Gorak Inc", "A special company", LocalDateUtility.today(), null);
    partnerTableTask.createPartnerRow(sqlService, partnerId02, "Solan Org", "Some other comapny", LocalDateUtility.today(), null);
    partnerTableTask.createPartnerRow(sqlService, partnerId03_employee, "Hans Muster", "An employee", LocalDateUtility.today(), null);
  }

  protected void insertDocuments(ISqlService sqlService) {
    LOG.info("SQL-DEV create rows for: {}", IDocumentTable.TABLE_NAME);

    DocumentTableTask documentTableTask = BEANS.get(DocumentTableTask.class);
    try {
      documentId01 = BigDecimal.valueOf(getSequenceNextval(ISequenceTable.TABLE_NAME));
      insertDocument(sqlService, documentTableTask, documentId01, "A sample document",
          LocalDateUtility.today(),
          LocalDateUtility.today(),
          LocalDateUtility.today(),
          "2016_03_08_124640.pdf", null, null);

      documentId02 = BigDecimal.valueOf(getSequenceNextval(ISequenceTable.TABLE_NAME));
      insertDocument(sqlService, documentTableTask, documentId02, "Bobs document",
          LocalDateUtility.toDate(TODAY.minusYears(1)),
          LocalDateUtility.today(),
          null,
          "2016_03_08_124640.pdf", null, null);
      documentId03 = BigDecimal.valueOf(getSequenceNextval(ISequenceTable.TABLE_NAME));
      insertDocument(sqlService, documentTableTask, documentId03, "Multiple partner document",
          LocalDateUtility.toDate(TODAY.minusYears(3)),
          LocalDateUtility.today(),
          null,
          "2016_03_08_124640.pdf", null, null);

      documentId04 = BigDecimal.valueOf(getSequenceNextval(ISequenceTable.TABLE_NAME));
      insertDocument(sqlService, documentTableTask, documentId04, "Txt document",
          LocalDateUtility.today(),
          LocalDateUtility.today(),
          null,
          "sampleTextFile.txt", null, null);

    }
    catch (IOException e) {
      LOG.error("Could not add dev documents to data store.", e);
    }
  }

  protected void insertDocument(ISqlService sqlService, DocumentTableTask documentTableTask, BigDecimal documentId, String abstractText, Date documentDate,
      Date insertDate, Date validDate, String fileName, String originalStorage, Long conversationId)
      throws IOException {

    // create db record
    // add document
    URL resource = DevDerbySqlService.class.getClassLoader().getResource("devDocuments/" + fileName);
    BinaryResource br = BinaryResources.create().withFilename(fileName).withContentType(FileUtility.getContentTypeForExtension(FileUtility.getFileExtension(fileName))).withContent(IOUtility.readFromUrl(resource))
        .withLastModified(System.currentTimeMillis()).build();
//    BinaryResource br = new BinaryResource(fileName, FileUtility.getContentTypeForExtension(FileUtility.getFileExtension(fileName)), IOUtility.getContent(resource.openStream()),
//        System.currentTimeMillis());
    String docPath = BEANS.get(DocumentStoreService.class).store(br, insertDate, documentId);

    documentTableTask.createDocumentRow(sqlService, documentId, abstractText, documentDate, insertDate, validDate, docPath, originalStorage, conversationId, true);
  }

  protected void insertDocumentCategory(ISqlService sqlService) {
    LOG.info("SQL-DEV create rows for: {}", IDocumentCategoryTable.TABLE_NAME);
    DocumentCategoryTableTask documentCategoryTableTask = BEANS.get(DocumentCategoryTableTask.class);
    documentCategoryTableTask.createDocumentCategoryRow(sqlService, documentId01, categoryId01);
    documentCategoryTableTask.createDocumentCategoryRow(sqlService, documentId02, categoryId02);
    documentCategoryTableTask.createDocumentCategoryRow(sqlService, documentId02, categoryId03);
    documentCategoryTableTask.createDocumentCategoryRow(sqlService, documentId03, categoryId01);
    documentCategoryTableTask.createDocumentCategoryRow(sqlService, documentId03, categoryId02);
  }

  protected void insertDocumentPartner(ISqlService sqlService) {
    LOG.info("SQL-DEV create rows for: {}", IDocumentPartnerTable.TABLE_NAME);
    DocumentPartnerTableTask documentPartnerTableTask = BEANS.get(DocumentPartnerTableTask.class);
    documentPartnerTableTask.createDocumentPartnerRow(sqlService, documentId01, partnerId01);
    documentPartnerTableTask.createDocumentPartnerRow(sqlService, documentId03, partnerId01);
    documentPartnerTableTask.createDocumentPartnerRow(sqlService, documentId03, partnerId02);
  }

  protected void insertDocumentPermission(ISqlService sqlService) {
    LOG.info("SQL-DEV create rows for: {}", IDocumentPermissionTable.TABLE_NAME);
    DocumentPermissionTableTask documentPermissionTableTask = BEANS.get(DocumentPermissionTableTask.class);
    documentPermissionTableTask.createDocumentPermissionRow(sqlService, "admin", documentId01, IDocumentPermissionTable.PERMISSION_WRITE);
    documentPermissionTableTask.createDocumentPermissionRow(sqlService, "admin", documentId02, IDocumentPermissionTable.PERMISSION_WRITE);
    documentPermissionTableTask.createDocumentPermissionRow(sqlService, "admin", documentId03, IDocumentPermissionTable.PERMISSION_WRITE);
    documentPermissionTableTask.createDocumentPermissionRow(sqlService, "cuttis", documentId02, IDocumentPermissionTable.PERMISSION_READ);
    documentPermissionTableTask.createDocumentPermissionRow(sqlService, "bob", documentId02, IDocumentPermissionTable.PERMISSION_WRITE);
  }

  protected void insertEmployers(ISqlService sqlService) {
    LOG.info("SQL-DEV create rows for: {}", IEmployeeTable.TABLE_NAME);

    EmployeeTableTask employerTableTask = BEANS.get(EmployeeTableTask.class);
    employerTableTask.createEmployerRow(sqlService, partnerId03_employee, "Hans", "Muster", "Mountainview 01", "CA-90501 Santa Barbara", "12.2568.2154.69", "PC 50-101-89-7", 26.50,
        "Bart Simpson & Marth Simpson", "742 Evergreen Terrace", "Springfield", "bart@simpson.spring", "+1 (0)7510 2152");
  }

  protected void insertPostingGroups(ISqlService sqlService) {
    LOG.info("SQL-DEV create rows for: {}", IPostingGroupTable.TABLE_NAME);

    postingGroupId01 = BigDecimal.valueOf(getSequenceNextval(ISequenceTable.TABLE_NAME));
    postingGroupId02 = BigDecimal.valueOf(getSequenceNextval(ISequenceTable.TABLE_NAME));

    PostingGroupTableTask postingGroupTableTask = BEANS.get(PostingGroupTableTask.class);
    postingGroupTableTask.createRow(sqlService, postingGroupId01, partnerId03_employee, documentId02, "September 2016", LocalDateUtility.toDate(LocalDate.of(2016, 10, 02)), BigDecimal.valueOf(9.25), BigDecimal.valueOf(256.5),
        BigDecimal.valueOf(230.50), BigDecimal.valueOf(-10.55),
        BigDecimal.valueOf(-5.55),
        BigDecimal.valueOf(9.87));
    postingGroupTableTask.createRow(sqlService, postingGroupId02, partnerId03_employee, documentId02, "Oktober 2016", LocalDateUtility.toDate(LocalDate.of(2016, 11, 02)), BigDecimal.valueOf(10.5), BigDecimal.valueOf(256.5),
        BigDecimal.valueOf(230.50), BigDecimal.valueOf(-10.55),
        BigDecimal.valueOf(-5.55),
        BigDecimal.valueOf(9.87));
  }

  protected void insertEntities(ISqlService sqlService) {
    LOG.info("SQL-DEV create rows for: {}", IEntityTable.TABLE_NAME);

    entityId01 = BigDecimal.valueOf(getSequenceNextval(ISequenceTable.TABLE_NAME));
    entityId02 = BigDecimal.valueOf(getSequenceNextval(ISequenceTable.TABLE_NAME));
    entityId03 = BigDecimal.valueOf(getSequenceNextval(ISequenceTable.TABLE_NAME));
    entityId04 = BigDecimal.valueOf(getSequenceNextval(ISequenceTable.TABLE_NAME));

    EntityTableTask entityTableTask = BEANS.get(EntityTableTask.class);
    entityTableTask.createEntityRow(sqlService, entityId01, partnerId03_employee, postingGroupId01, EntityTypeCodeType.WorkCode.ID, LocalDateUtility.toDate(LocalDate.of(2016, 9, 04)), BigDecimal.valueOf(3.5), null, "Sept work 1", null);
    entityTableTask.createEntityRow(sqlService, entityId02, partnerId03_employee, postingGroupId01, EntityTypeCodeType.WorkCode.ID, LocalDateUtility.toDate(LocalDate.of(2016, 9, 11)), BigDecimal.valueOf(4.25), null, "Sept work 2", null);
    entityTableTask.createEntityRow(sqlService, entityId03, partnerId03_employee, UnbilledCode.ID, EntityTypeCodeType.WorkCode.ID, LocalDateUtility.toDate(TODAY.minusDays(10)), BigDecimal.valueOf(5.5), null, "First work", null);
    entityTableTask.createEntityRow(sqlService, entityId04, partnerId03_employee, UnbilledCode.ID, EntityTypeCodeType.WorkCode.ID, LocalDateUtility.toDate(TODAY.minusDays(1)), BigDecimal.valueOf(2.25), null, "Second work", null);
    for (int i = 0; i < 50; i++) {
      createEntity(sqlService, i);
    }
  }

  protected void createEntity(ISqlService sqlService, int counter) {
    BigDecimal entityId = BigDecimal.valueOf(getSequenceNextval(ISequenceTable.TABLE_NAME));

    EntityTableTask entityTableTask = BEANS.get(EntityTableTask.class);
    entityTableTask.createEntityRow(sqlService, entityId, partnerId03_employee, UnbilledCode.ID, EntityTypeCodeType.WorkCode.ID,
        LocalDateUtility.toDate(LocalDate.of(2016, 12, 04).plusDays(counter)), BigDecimal.valueOf(2.5), null, "Dez work " + counter, null);
  }

}

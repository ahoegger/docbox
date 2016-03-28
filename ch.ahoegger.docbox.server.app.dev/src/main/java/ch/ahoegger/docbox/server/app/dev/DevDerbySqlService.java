package ch.ahoegger.docbox.server.app.dev;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.CreateImmediately;
import org.eclipse.scout.rt.platform.Replace;
import org.eclipse.scout.rt.platform.config.CONFIG;
import org.eclipse.scout.rt.platform.exception.ExceptionHandler;
import org.eclipse.scout.rt.platform.resource.BinaryResource;
import org.eclipse.scout.rt.platform.util.IOUtility;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.platform.util.concurrent.IRunnable;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
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
import ch.ahoegger.docbox.server.database.dev.initialization.ITableTask;
import ch.ahoegger.docbox.server.database.dev.initialization.PartnerTableTask;
import ch.ahoegger.docbox.server.database.dev.initialization.SequenceTask;
import ch.ahoegger.docbox.server.database.dev.initialization.UserTableTask;
import ch.ahoegger.docbox.server.document.store.DocumentStoreService;
import ch.ahoegger.docbox.server.security.SecurityService;
import ch.ahoegger.docbox.shared.category.ICategoryTable;
import ch.ahoegger.docbox.shared.conversation.IConversationTable;
import ch.ahoegger.docbox.shared.document.IDocumentCategoryTable;
import ch.ahoegger.docbox.shared.document.IDocumentPartnerTable;
import ch.ahoegger.docbox.shared.document.IDocumentPermissionTable;
import ch.ahoegger.docbox.shared.document.IDocumentTable;
import ch.ahoegger.docbox.shared.partner.IPartnerTable;
import ch.ahoegger.docbox.shared.security.permission.IDefaultPermissionTable;

/**
 * <h3>{@link DevDerbySqlService}</h3>
 *
 * @author aho
 */
@CreateImmediately
@Replace
public class DevDerbySqlService extends DerbySqlService {
  private static final Logger LOG = LoggerFactory.getLogger(CategoryTableTask.class);

  public static final Long SEQ_START_ROLE = 200l;
  public static final Long SEQ_START_PARTNER = 300l;
  public static final Long SEQ_START_DOCUMENT = 400l;
  public static final Long SEQ_START_CATEGORY = 500l;
  public static final Long SEQ_START_CONVERSATION = 600l;

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
  }

  protected void insertSequenceInitialValue(ISqlService sqlService) {
    BEANS.get(SequenceTask.class).insertInitialValue(sqlService, 1000);
  }

  protected void createUsers(ISqlService sqlService) {
    UserTableTask userTableTask = BEANS.get(UserTableTask.class);
    userTableTask.insertUser(sqlService, "Cuttis", "Bolion", "cuttis", new String(BEANS.get(SecurityService.class).createPasswordHash("pwd".toCharArray())), true, false);
    userTableTask.insertUser(sqlService, "Bob", "Miller", "bob", new String(BEANS.get(SecurityService.class).createPasswordHash("pwd".toCharArray())), true, false);
    userTableTask.insertUser(sqlService, "Admin", "Manager", "admin", new String(BEANS.get(SecurityService.class).createPasswordHash("manager".toCharArray())), true, true);
  }

  protected void insertCategories(ISqlService sqlService) {
    LOG.info("SQL-DEV create rows for: {}", ICategoryTable.TABLE_NAME);
    CategoryTableTask categoryTableTask = BEANS.get(CategoryTableTask.class);
    categoryTableTask.createCategoryRow(sqlService, SEQ_START_CATEGORY, "Work", "anything work related.", new Date(), null);
    categoryTableTask.createCategoryRow(sqlService, SEQ_START_CATEGORY + 1, "Household", "some window cleaning stuff.", new Date(), null);
  }

  protected void insertConversations(ISqlService sqlService) {
    LOG.info("SQL-DEV create rows for: {}", IConversationTable.TABLE_NAME);
    ConversationTableTask conversationTableTask = BEANS.get(ConversationTableTask.class);
    conversationTableTask.createConversationRow(sqlService, SEQ_START_CONVERSATION, "House selling", "everything related with house selling.", new Date(), null);
    conversationTableTask.createConversationRow(sqlService, SEQ_START_CONVERSATION + 1, "Ponny order", "all documents to get a ponny.", new Date(), null);
    conversationTableTask.createConversationRow(sqlService, SEQ_START_CONVERSATION + 2, "Without partner rel", "all documents to get a ponny.", new Date(), null);
  }

  protected void insertDefaultPermissions(ISqlService sqlService) {
    LOG.info("SQL-DEV create rows for: {}", IDefaultPermissionTable.TABLE_NAME);
    DefaultPermissionTableTask defaultPermissionTableTask = BEANS.get(DefaultPermissionTableTask.class);
    defaultPermissionTableTask.createDefaultPermissionRow(sqlService, "admin", IDocumentPermissionTable.PERMISSION_WRITE);
    defaultPermissionTableTask.createDefaultPermissionRow(sqlService, "bob", IDocumentPermissionTable.PERMISSION_READ);
  }

  protected void insertPartners(ISqlService sqlService) {
    LOG.info("SQL-DEV create rows for: {}", IPartnerTable.TABLE_NAME);
    PartnerTableTask partnerTableTask = BEANS.get(PartnerTableTask.class);
    partnerTableTask.createPartnerRow(sqlService, SEQ_START_PARTNER, "Gorak Inc", "A special company", new Date(), null);
    partnerTableTask.createPartnerRow(sqlService, SEQ_START_PARTNER + 1, "Solan Org", "Some other comapny", new Date(), null);
  }

  protected void insertDocuments(ISqlService sqlService) {
    LOG.info("SQL-DEV create rows for: {}", IDocumentTable.TABLE_NAME);

    DocumentTableTask documentTableTask = BEANS.get(DocumentTableTask.class);
    try {
      Calendar cal = Calendar.getInstance();
      DateUtility.truncCalendar(cal);

      insertDocument(sqlService, documentTableTask, SEQ_START_DOCUMENT, "A sample document", cal.getTime(), new Date(), new Date(), "2016_03_08_124640.pdf", null, null);
      cal.add(Calendar.YEAR, -1);
      insertDocument(sqlService, documentTableTask, SEQ_START_DOCUMENT + 1, "Bobs document", cal.getTime(), new Date(), new Date(), "2016_03_08_124640.pdf", null, null);
      cal.add(Calendar.YEAR, -3);
      insertDocument(sqlService, documentTableTask, SEQ_START_DOCUMENT + 2, "Multiple partner document", cal.getTime(), new Date(), new Date(), "2016_03_08_124640.pdf", null, null);
    }
    catch (IOException e) {
      LOG.error("Could not add dev documents to data store.", e);
    }
  }

  protected void insertDocument(ISqlService sqlService, DocumentTableTask documentTableTask, long documentId, String abstractText, Date documentDate,
      Date insertDate, Date validDate, String fileName, String originalStorage, Long conversationId)
      throws IOException {

    // create db record
    // add document
    URL resource = DevDerbySqlService.class.getClassLoader().getResource("devDocuments/" + fileName);
    BinaryResource br = new BinaryResource(fileName, "pdf", IOUtility.getContent(resource.openStream()),
        System.currentTimeMillis());
    String docPath = BEANS.get(DocumentStoreService.class).store(br, insertDate, documentId);

    documentTableTask.createDocumentRow(sqlService, documentId, abstractText, documentDate, insertDate, validDate, docPath, originalStorage, conversationId);
  }

  protected void insertDocumentCategory(ISqlService sqlService) {
    LOG.info("SQL-DEV create rows for: {}", IDocumentCategoryTable.TABLE_NAME);
    DocumentCategoryTableTask documentCategoryTableTask = BEANS.get(DocumentCategoryTableTask.class);
    documentCategoryTableTask.createDocumentCategoryRow(sqlService, SEQ_START_DOCUMENT, SEQ_START_CATEGORY);
    documentCategoryTableTask.createDocumentCategoryRow(sqlService, SEQ_START_DOCUMENT + 2, SEQ_START_CATEGORY);
    documentCategoryTableTask.createDocumentCategoryRow(sqlService, SEQ_START_DOCUMENT + 2, SEQ_START_CATEGORY + 1);
  }

  protected void insertDocumentPartner(ISqlService sqlService) {
    LOG.info("SQL-DEV create rows for: {}", IDocumentPartnerTable.TABLE_NAME);
    DocumentPartnerTableTask documentPartnerTableTask = BEANS.get(DocumentPartnerTableTask.class);
    documentPartnerTableTask.createDocumentPartnerRow(sqlService, SEQ_START_DOCUMENT, SEQ_START_PARTNER);
    documentPartnerTableTask.createDocumentPartnerRow(sqlService, SEQ_START_DOCUMENT + 2, SEQ_START_PARTNER);
    documentPartnerTableTask.createDocumentPartnerRow(sqlService, SEQ_START_DOCUMENT + 2, SEQ_START_PARTNER + 1);
  }

  protected void insertDocumentPermission(ISqlService sqlService) {
    LOG.info("SQL-DEV create rows for: {}", IDocumentPermissionTable.TABLE_NAME);
    DocumentPermissionTableTask documentPermissionTableTask = BEANS.get(DocumentPermissionTableTask.class);
    documentPermissionTableTask.createDocumentPermissionRow(sqlService, "admin", SEQ_START_DOCUMENT, IDocumentPermissionTable.PERMISSION_WRITE);
    documentPermissionTableTask.createDocumentPermissionRow(sqlService, "admin", SEQ_START_DOCUMENT + 1, IDocumentPermissionTable.PERMISSION_WRITE);
    documentPermissionTableTask.createDocumentPermissionRow(sqlService, "admin", SEQ_START_DOCUMENT + 2, IDocumentPermissionTable.PERMISSION_WRITE);
    documentPermissionTableTask.createDocumentPermissionRow(sqlService, "cuttis", SEQ_START_DOCUMENT + 1, IDocumentPermissionTable.PERMISSION_READ);
    documentPermissionTableTask.createDocumentPermissionRow(sqlService, "bob", SEQ_START_DOCUMENT + 1, IDocumentPermissionTable.PERMISSION_WRITE);
  }
}

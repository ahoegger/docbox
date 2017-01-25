package ch.ahoegger.docbox.server.document;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.BeanMetaData;
import org.eclipse.scout.rt.platform.IBean;
import org.eclipse.scout.rt.platform.IBeanManager;
import org.eclipse.scout.rt.platform.Platform;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.platform.util.ObjectUtility;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.ahoegger.docbox.server.database.dev.initialization.CategoryTableTask;
import ch.ahoegger.docbox.server.database.dev.initialization.ConversationTableTask;
import ch.ahoegger.docbox.server.database.dev.initialization.DocumentCategoryTableTask;
import ch.ahoegger.docbox.server.database.dev.initialization.DocumentPartnerTableTask;
import ch.ahoegger.docbox.server.database.dev.initialization.DocumentPermissionTableTask;
import ch.ahoegger.docbox.server.database.dev.initialization.DocumentTableTask;
import ch.ahoegger.docbox.server.database.dev.initialization.PartnerTableTask;
import ch.ahoegger.docbox.server.database.dev.initialization.UserTableTask;
import ch.ahoegger.docbox.server.ocr.DocumentOcrService;
import ch.ahoegger.docbox.server.test.util.AbstractTestWithDatabase;
import ch.ahoegger.docbox.server.test.util.DocboxAssert;
import ch.ahoegger.docbox.server.test.util.IdGenerateService;
import ch.ahoegger.docbox.shared.document.DocumentFormData;
import ch.ahoegger.docbox.shared.document.DocumentFormData.Partners.PartnersRowData;
import ch.ahoegger.docbox.shared.document.DocumentFormData.Permissions.PermissionsRowData;
import ch.ahoegger.docbox.shared.document.IDocumentService;
import ch.ahoegger.docbox.shared.ocr.OcrLanguageCodeType;
import ch.ahoegger.docbox.shared.security.permission.PermissionCodeType;

/**
 * <h3>{@link DocumentService_ModifyTest}</h3>
 *
 * @author Andreas Hoegger
 */
public class DocumentService_ModifyTest extends AbstractTestWithDatabase {

  private final String userId01 = SUBJECT_NAME;
  private final String userId02 = "username02";

  private final BigDecimal documentId = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private final BigDecimal categoryId01 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private final BigDecimal categoryId02 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private final BigDecimal conversationId01 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private final BigDecimal conversationId02 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private final BigDecimal partnerId01 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private final BigDecimal partnerId02 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();

  private Date m_today;

  private static List<IBean<?>> s_mockBeans = new ArrayList<IBean<?>>();

  @Override
  public void setupDb() throws Exception {
    super.setupDb();

    ISqlService sqlService = BEANS.get(ISqlService.class);

    Calendar cal = Calendar.getInstance();
    DateUtility.truncCalendar(cal);

    m_today = cal.getTime();
    // create user
    BEANS.get(UserTableTask.class).insert(sqlService, "name01", "firstname01", userId01, "secret", true, true);
    BEANS.get(UserTableTask.class).insert(sqlService, "name02", "firstname02", userId02, "secret", true, false);

    // create category
    BEANS.get(CategoryTableTask.class).insert(sqlService, categoryId01, "category01", "dec01", m_today, null);
    BEANS.get(CategoryTableTask.class).insert(sqlService, categoryId02, "category02", "dec02", m_today, null);

    // create conversation
    BEANS.get(ConversationTableTask.class).insert(sqlService, conversationId01, "con01", "dec01", m_today, null);
    BEANS.get(ConversationTableTask.class).insert(sqlService, conversationId02, "con02", "dec02", m_today, null);

    // create partner
    BEANS.get(PartnerTableTask.class).insert(sqlService, partnerId01, "partner01", "desc01", m_today, null);
    BEANS.get(PartnerTableTask.class).insert(sqlService, partnerId02, "partner02", "desc02", m_today, null);

    // create document
    Date docData = cal.getTime();
    Date insertDate = m_today;
    BEANS.get(DocumentTableTask.class).insert(sqlService, documentId, "doc 01", docData, insertDate, null, "2016_03_08_124640.pdf", "origStorage", conversationId01, false, null);
    // links
    BEANS.get(DocumentPartnerTableTask.class).insert(sqlService, documentId, partnerId01);
    BEANS.get(DocumentCategoryTableTask.class).insert(sqlService, documentId, categoryId01);
    BEANS.get(DocumentPermissionTableTask.class).insert(sqlService, userId01, documentId, PermissionCodeType.ReadCode.ID);

  }

  @BeforeClass
  public static void createMocks() {
    IBeanManager beanManager = Platform.get().getBeanManager();
    s_mockBeans.add(beanManager.registerBean(
        new BeanMetaData(DocumentOcrService.class)
            .withOrder(-10).withInitialInstance(new DocumentOcrService() {
              @Override
              public Boolean exists(BigDecimal documentId) {
                return true;
              }

            })));
  }

  @AfterClass
  public static void afterClass() {
    IBeanManager beanManager = Platform.get().getBeanManager();
    s_mockBeans.forEach(b -> beanManager.unregisterBean(b));
  }

  @Test
  public void testModifyAbstract() {
    IDocumentService service = BEANS.get(IDocumentService.class);

    DocumentFormData fd = loadDocument(service, documentId);
    // modify
    fd.getAbstract().setValue("modifiedText");
    service.store(fd);

    // compare to new loaded
    assertEqualsDb(service, fd);
  }

  @Test
  public void testModifyCapturedDate() {
    IDocumentService service = BEANS.get(IDocumentService.class);

    DocumentFormData fd = loadDocument(service, documentId);
    // modify
    Calendar cal = Calendar.getInstance();
    cal.set(2013, 3, 3);
    DateUtility.truncCalendar(cal);
    fd.getCapturedDate().setValue(cal.getTime());
    service.store(fd);

    DocumentFormData dbRef = loadDocument(service, documentId);

    Assert.assertFalse(fd.getCapturedDate().getValue().equals(dbRef.getCapturedDate().getValue()));
    Assert.assertEquals(m_today, dbRef.getCapturedDate().getValue());
  }

  @Test
  public void testAddCategory() {
    IDocumentService service = BEANS.get(IDocumentService.class);

    DocumentFormData fd = loadDocument(service, documentId);
    // modify
    Set<BigDecimal> categories = new HashSet<BigDecimal>(fd.getCategoriesBox().getValue());
    categories.add(categoryId02);
    fd.getCategoriesBox().setValue(categories);
    service.store(fd);

    // compare to new loaded
    DocumentFormData refFd = assertEqualsDb(service, fd);
    Assert.assertEquals(CollectionUtility.arrayList(categoryId01, categoryId02),
        refFd.getCategoriesBox().getValue().stream()
            .map(bd -> bd).sorted()
            .collect(Collectors.toList()));

  }

  @Test
  public void testRemoveCategory() {
    IDocumentService service = BEANS.get(IDocumentService.class);

    DocumentFormData fd = loadDocument(service, documentId);
    // modify
    Set<BigDecimal> categories = new HashSet<BigDecimal>(fd.getCategoriesBox().getValue());
    categories.remove(categoryId01);
    fd.getCategoriesBox().setValue(categories);
    service.store(fd);

    // compare to new loaded
    DocumentFormData refFd = assertEqualsDb(service, fd);
    Assert.assertEquals(CollectionUtility.arrayList(),
        refFd.getCategoriesBox().getValue().stream()
            .map(bd -> bd).sorted()
            .collect(Collectors.toList()));

  }

  @Test
  public void testReplaceCategory() {
    IDocumentService service = BEANS.get(IDocumentService.class);

    DocumentFormData fd = loadDocument(service, documentId);
    // modify
    Set<BigDecimal> categories = new HashSet<BigDecimal>();
    categories.add(categoryId02);
    fd.getCategoriesBox().setValue(categories);
    service.store(fd);

    // compare to new loaded
    DocumentFormData refFd = assertEqualsDb(service, fd);
    Assert.assertEquals(1, refFd.getCategoriesBox().getValue().size());
    Assert.assertEquals(CollectionUtility.arrayList(categoryId02),
        refFd.getCategoriesBox().getValue().stream()
            .map(bd -> bd).sorted()
            .collect(Collectors.toList()));

  }

  @Test
  public void testModifyConversation() {
    IDocumentService service = BEANS.get(IDocumentService.class);

    DocumentFormData fd = loadDocument(service, documentId);
    // modify
    fd.getConversation().setValue(conversationId02);
    service.store(fd);

    // compare to new loaded
    DocumentFormData refFd = assertEqualsDb(service, fd);
    Assert.assertEquals(conversationId02, Optional.of(refFd.getConversation().getValue()).map(bd -> bd).orElse(null));
  }

  @Test
  public void testModifyDocumentDate() {
    IDocumentService service = BEANS.get(IDocumentService.class);

    DocumentFormData fd = loadDocument(service, documentId);
    // modify
    Calendar cal = Calendar.getInstance();
    DateUtility.truncCalendar(cal);
    cal.set(2013, 05, 03);
    Date docDate = cal.getTime();
    fd.getDocumentDate().setValue(docDate);
    service.store(fd);

    // compare to new loaded
    DocumentFormData refFd = assertEqualsDb(service, fd);
    Assert.assertEquals(docDate, Optional.of(refFd.getDocumentDate().getValue()).orElse(null));

  }

  @Test
  public void testUnmodifiableDocumentPath() {
    IDocumentService service = BEANS.get(IDocumentService.class);

    DocumentFormData fd = loadDocument(service, documentId);
    // modify
    fd.setDocumentPath("modified/path");
    service.store(fd);

    // compare to new loaded
    DocumentFormData refFd = loadDocument(service, documentId);
    Assert.assertFalse("modified/path".equals(refFd.getDocumentPath()));
  }

  @Test
  public void testOcrLanguage() {
    IDocumentService service = BEANS.get(IDocumentService.class);

    DocumentFormData fd = loadDocument(service, documentId);
    // modify
    fd.getOcrLanguage().setValue(OcrLanguageCodeType.EnglishCode.ID);
    service.store(fd);

    // compare to new loaded
    DocumentFormData refFd = loadDocument(service, documentId);
    Assert.assertEquals(OcrLanguageCodeType.EnglishCode.ID, refFd.getOcrLanguage().getValue());
  }

  @Test
  public void testParseOcr() {
    IDocumentService service = BEANS.get(IDocumentService.class);

    DocumentFormData fd = loadDocument(service, documentId);
    // modify
    fd.getParseOcr().setValue(true);
    service.store(fd);

    // compare to new loaded
    DocumentFormData refFd = loadDocument(service, documentId);
    Assert.assertTrue(refFd.getParseOcr().getValue());
  }

  @Test
  public void testModifyOriginalStorage() {
    IDocumentService service = BEANS.get(IDocumentService.class);

    DocumentFormData fd = loadDocument(service, documentId);
    // modify
    fd.getOriginalStorage().setValue("mod storage");
    service.store(fd);

    // compare to new loaded
    DocumentFormData refFd = assertEqualsDb(service, fd);
    Assert.assertEquals("mod storage", refFd.getOriginalStorage().getValue());
  }

  @Test
  public void testAddPartner() {
    IDocumentService service = BEANS.get(IDocumentService.class);

    DocumentFormData fd = loadDocument(service, documentId);
    // modify
    PartnersRowData newRow = fd.getPartners().addRow();
    newRow.setPartner(partnerId02);
    service.store(fd);

    // compare to new loaded
    DocumentFormData refFd = assertEqualsDb(service, fd);
    Assert.assertEquals(CollectionUtility.arrayList(partnerId01, partnerId02),
        Arrays.stream(refFd.getPartners().getRows())
            .map(row -> row.getPartner())
            .filter(p -> p != null)
            .sorted()
            .collect(Collectors.toList()));

  }

  @Test
  public void testRemovePartner() {
    IDocumentService service = BEANS.get(IDocumentService.class);

    DocumentFormData fd = loadDocument(service, documentId);
    // modify
    for (PartnersRowData rd : fd.getPartners().getRows()) {
      if (ObjectUtility.equals(partnerId01, rd.getPartner())) {
        fd.getPartners().removeRow(rd);
      }
    }
    service.store(fd);

    // compare to new loaded
    DocumentFormData refFd = assertEqualsDb(service, fd);
    Assert.assertEquals(CollectionUtility.arrayList(),
        Arrays.stream(refFd.getPartners().getRows())
            .map(row -> row.getPartner())
            .filter(p -> p != null)
            .map(bd -> bd)
            .sorted()
            .collect(Collectors.toList()));

  }

  @Test
  public void testReplacePartner() {
    IDocumentService service = BEANS.get(IDocumentService.class);

    DocumentFormData fd = loadDocument(service, documentId);
    // modify
    fd.getPartners().clearRows();
    PartnersRowData newRow = fd.getPartners().addRow();
    newRow.setPartner(partnerId02);
    service.store(fd);

    // compare to new loaded
    DocumentFormData refFd = assertEqualsDb(service, fd);
    Assert.assertEquals(CollectionUtility.arrayList(partnerId02),
        Arrays.stream(refFd.getPartners().getRows())
            .map(row -> row.getPartner())
            .filter(p -> p != null)
            .sorted()
            .collect(Collectors.toList()));

  }

  @Test
  public void testChangePermissionOfUser() {
    IDocumentService service = BEANS.get(IDocumentService.class);

    DocumentFormData fd = loadDocument(service, documentId);
    // modify
    PermissionsRowData rd = Arrays.stream(fd.getPermissions().getRows()).filter(row -> userId01.equals(row.getUser())).findFirst().get();
    rd.setPermission(PermissionCodeType.WriteCode.ID);

    service.store(fd);

    // compare to new loaded
    DocumentFormData refFd = assertEqualsDb(service, fd);
    List<PermissionsRowData> sortedValidPermissions =
        Arrays.stream(refFd.getPermissions().getRows())
            .filter(row -> row.getUser() != null && row.getPermission() != null)
            .sorted((p1, p2) -> p1.getUser()
                .compareTo(p2.getUser()))
            .collect(Collectors.toList());

    Assert.assertEquals(CollectionUtility.arrayList(userId01),
        sortedValidPermissions.stream().map(row -> row.getUser()).collect(Collectors.toList()));
    Assert.assertEquals(CollectionUtility.arrayList(PermissionCodeType.WriteCode.ID),
        sortedValidPermissions.stream().map(row -> row.getPermission()).collect(Collectors.toList()));

  }

  @Test
  public void testAddPermissionUser() {
    IDocumentService service = BEANS.get(IDocumentService.class);

    DocumentFormData fd = loadDocument(service, documentId);
    // modify
    PermissionsRowData newRow = fd.getPermissions().addRow();
    newRow.setUser(userId02);
    newRow.setPermission(PermissionCodeType.WriteCode.ID);
    service.store(fd);

    // compare to new loaded
    DocumentFormData refFd = assertEqualsDb(service, fd);
    List<PermissionsRowData> sortedValidPermissions =
        Arrays.stream(refFd.getPermissions().getRows())
            .filter(row -> row.getUser() != null && row.getPermission() != null)
            .sorted((p1, p2) -> p1.getUser().compareTo(p2.getUser()))
            .collect(Collectors.toList());

    Assert.assertEquals(CollectionUtility.arrayList(userId01, userId02),
        sortedValidPermissions.stream().map(row -> row.getUser()).collect(Collectors.toList()));
    Assert.assertEquals(CollectionUtility.arrayList(PermissionCodeType.ReadCode.ID, PermissionCodeType.WriteCode.ID),
        sortedValidPermissions.stream().map(row -> row.getPermission()).collect(Collectors.toList()));

  }

  @Test
  public void testRemovePermissionUser() {
    IDocumentService service = BEANS.get(IDocumentService.class);

    DocumentFormData fd = loadDocument(service, documentId);
    // modify

    for (PermissionsRowData rd : fd.getPermissions().getRows()) {
      if (userId01.equals(rd.getUser())) {
        fd.getPermissions().removeRow(rd);
      }
    }
    service.store(fd);

    // compare to new loaded
    DocumentFormData refFd = assertEqualsDb(service, fd);
    List<PermissionsRowData> sortedValidPermissions =
        Arrays.stream(refFd.getPermissions().getRows())
            .filter(row -> row.getUser() != null && row.getPermission() != null)
            .sorted((p1, p2) -> p1.getUser().compareTo(p2.getUser()))
            .collect(Collectors.toList());

    Assert.assertEquals(CollectionUtility.arrayList(),
        sortedValidPermissions.stream().map(row -> row.getUser()).collect(Collectors.toList()));

    Assert.assertEquals(CollectionUtility.arrayList(),
        sortedValidPermissions.stream().map(row -> row.getPermission()).collect(Collectors.toList()));

  }

  @Test
  public void testReplacePermissionUser() {
    IDocumentService service = BEANS.get(IDocumentService.class);

    DocumentFormData fd = loadDocument(service, documentId);
    // modify
    fd.getPermissions().clearRows();
    PermissionsRowData newRow = fd.getPermissions().addRow();
    newRow.setUser(userId02);
    newRow.setPermission(PermissionCodeType.WriteCode.ID);

    service.store(fd);

    // compare to new loaded
    DocumentFormData refFd = assertEqualsDb(service, fd);
    List<PermissionsRowData> sortedValidPermissions =
        Arrays.stream(refFd.getPermissions().getRows())
            .filter(row -> row.getUser() != null && row.getPermission() != null)
            .sorted((p1, p2) -> p1.getUser().compareTo(p2.getUser()))
            .collect(Collectors.toList());

    Assert.assertEquals(CollectionUtility.arrayList(userId02),
        sortedValidPermissions.stream().map(row -> row.getUser()).collect(Collectors.toList()));

    Assert.assertEquals(CollectionUtility.arrayList(PermissionCodeType.WriteCode.ID),
        sortedValidPermissions.stream().map(row -> row.getPermission()).collect(Collectors.toList()));

  }

  private DocumentFormData loadDocument(IDocumentService service, BigDecimal docId) {
    DocumentFormData fd = new DocumentFormData();
    fd.setDocumentId(docId);
    fd = service.load(fd);
    return fd;
  }

  private DocumentFormData assertEqualsDb(IDocumentService service, DocumentFormData fd) {
    DocumentFormData dbFormData = loadDocument(service, fd.getDocumentId());
    DocboxAssert.assertEquals(dbFormData, fd);
    return dbFormData;

  }
}

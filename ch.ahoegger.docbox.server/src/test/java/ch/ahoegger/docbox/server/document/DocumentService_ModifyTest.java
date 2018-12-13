package ch.ahoegger.docbox.server.document;

import java.math.BigDecimal;
import java.sql.Connection;
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
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.platform.util.ObjectUtility;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import ch.ahoegger.docbox.server.category.CategoryService;
import ch.ahoegger.docbox.server.conversation.ConversationService;
import ch.ahoegger.docbox.server.ocr.DocumentOcrService;
import ch.ahoegger.docbox.server.partner.PartnerService;
import ch.ahoegger.docbox.server.test.util.AbstractTestWithDatabase;
import ch.ahoegger.docbox.server.test.util.DocboxAssert;
import ch.ahoegger.docbox.server.test.util.IdGenerateService;
import ch.ahoegger.docbox.server.test.util.TestDataGenerator;
import ch.ahoegger.docbox.shared.document.DocumentFormData;
import ch.ahoegger.docbox.shared.document.DocumentFormData.Partners.PartnersRowData;
import ch.ahoegger.docbox.shared.document.DocumentFormData.Permissions.PermissionsRowData;
import ch.ahoegger.docbox.shared.ocr.OcrLanguageCodeType;
import ch.ahoegger.docbox.shared.security.permission.PermissionCodeType;

/**
 * <h3>{@link DocumentService_ModifyTest}</h3>
 *
 * @author Andreas Hoegger
 */
public class DocumentService_ModifyTest extends AbstractTestWithDatabase {

  private static DocumentService service;

  private final BigDecimal id_document = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private final BigDecimal id_category_01 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private final BigDecimal id_category_02 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private final BigDecimal id_conversation_01 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private final BigDecimal id_conversation_02 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private final BigDecimal id_partner_01 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private final BigDecimal id_partner_02 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();

  private Date m_today;

  private static List<IBean<?>> s_mockBeans = new ArrayList<IBean<?>>();

  @BeforeClass
  public static void initService() {
    service = BEANS.get(DocumentService.class);
  }

  @Override
  protected void execSetupDb(Connection connection, TestDataGenerator testDataGenerator) throws Exception {
    Calendar cal = Calendar.getInstance();
    DateUtility.truncCalendar(cal);

    m_today = cal.getTime();

    // create category
    BEANS.get(CategoryService.class).insertRow(connection, id_category_01, "category01", "dec01", m_today, null);
    BEANS.get(CategoryService.class).insertRow(connection, id_category_02, "category02", "dec02", m_today, null);

    // create conversation
    BEANS.get(ConversationService.class).insert(connection, id_conversation_01, "con01", "dec01", m_today, null);
    BEANS.get(ConversationService.class).insert(connection, id_conversation_02, "con02", "dec02", m_today, null);

    // create partner
    BEANS.get(PartnerService.class).insert(connection, id_partner_01, "partner01", "desc01", m_today, null);
    BEANS.get(PartnerService.class).insert(connection, id_partner_02, "partner02", "desc02", m_today, null);

    // create document
    Date docData = cal.getTime();
    Date insertDate = m_today;
    BEANS.get(DocumentService.class).insert(connection, id_document, "doc 01", docData, insertDate, null, "2016_03_08_124640.pdf", "origStorage", id_conversation_01, false, null);
    // links
    BEANS.get(DocumentPartnerService.class).insert(connection, id_document, id_partner_01);
    BEANS.get(DocumentCategoryService.class).insert(connection, id_document, id_category_01);
    BEANS.get(DocumentPermissionService.class).insert(connection, USER, id_document, PermissionCodeType.ReadCode.ID);

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
    DocumentFormData fd = loadDocument(id_document);
    // modify
    fd.getAbstract().setValue("modifiedText");
    service.store(fd);

    // compare to new loaded
    assertEqualsDb(fd);
  }

  @Test(expected = VetoException.class)
  public void testUnmodifyCapturedDate() {
    DocumentFormData fd = loadDocument(id_document);
    // modify
    Calendar cal = Calendar.getInstance();
    cal.set(2013, 3, 3);
    DateUtility.truncCalendar(cal);
    fd.getCapturedDate().setValue(cal.getTime());
    service.store(fd);

    DocumentFormData dbRef = loadDocument(id_document);

    Assert.assertFalse(fd.getCapturedDate().getValue().equals(dbRef.getCapturedDate().getValue()));
    Assert.assertEquals(m_today, dbRef.getCapturedDate().getValue());
  }

  @Test
  public void testAddCategory() {
    DocumentFormData fd = loadDocument(id_document);
    // modify
    Set<BigDecimal> categories = new HashSet<BigDecimal>(fd.getCategoriesBox().getValue());
    categories.add(id_category_02);
    fd.getCategoriesBox().setValue(categories);
    service.store(fd);

    // compare to new loaded
    DocumentFormData refFd = assertEqualsDb(fd);
    Assert.assertEquals(CollectionUtility.arrayList(id_category_01, id_category_02),
        refFd.getCategoriesBox().getValue().stream()
            .map(bd -> bd).sorted()
            .collect(Collectors.toList()));

  }

  @Test
  public void testRemoveCategory() {
    DocumentFormData fd = loadDocument(id_document);
    // modify
    Set<BigDecimal> categories = new HashSet<BigDecimal>(fd.getCategoriesBox().getValue());
    categories.remove(id_category_01);
    fd.getCategoriesBox().setValue(categories);
    service.store(fd);

    // compare to new loaded
    DocumentFormData refFd = assertEqualsDb(fd);
    Assert.assertEquals(CollectionUtility.arrayList(),
        refFd.getCategoriesBox().getValue().stream()
            .map(bd -> bd).sorted()
            .collect(Collectors.toList()));

  }

  @Test
  public void testReplaceCategory() {
    DocumentFormData fd = loadDocument(id_document);
    // modify
    Set<BigDecimal> categories = new HashSet<BigDecimal>();
    categories.add(id_category_02);
    fd.getCategoriesBox().setValue(categories);
    service.store(fd);

    // compare to new loaded
    DocumentFormData refFd = assertEqualsDb(fd);
    Assert.assertEquals(1, refFd.getCategoriesBox().getValue().size());
    Assert.assertEquals(CollectionUtility.arrayList(id_category_02),
        refFd.getCategoriesBox().getValue().stream()
            .map(bd -> bd).sorted()
            .collect(Collectors.toList()));

  }

  @Test
  public void testModifyConversation() {
    DocumentFormData fd = loadDocument(id_document);
    // modify
    fd.getConversation().setValue(id_conversation_02);
    service.store(fd);

    // compare to new loaded
    DocumentFormData refFd = assertEqualsDb(fd);
    Assert.assertEquals(id_conversation_02, Optional.of(refFd.getConversation().getValue()).map(bd -> bd).orElse(null));
  }

  @Test
  public void testModifyDocumentDate() {
    DocumentFormData fd = loadDocument(id_document);
    // modify
    Calendar cal = Calendar.getInstance();
    DateUtility.truncCalendar(cal);
    cal.set(2013, 05, 03);
    Date docDate = cal.getTime();
    fd.getDocumentDate().setValue(docDate);
    service.store(fd);

    // compare to new loaded
    DocumentFormData refFd = assertEqualsDb(fd);
    Assert.assertEquals(docDate, Optional.of(refFd.getDocumentDate().getValue()).orElse(null));

  }

  @Test(expected = VetoException.class)
  public void testUnmodifiableDocumentPath() {
    DocumentFormData fd = loadDocument(id_document);
    // modify
    fd.setDocumentPath("modified/path");
    service.store(fd);
    // expect exception
  }

  @Test
  public void testOcrLanguage() {
    DocumentFormData fd = loadDocument(id_document);
    // modify
    fd.getOcrLanguage().setValue(OcrLanguageCodeType.EnglishCode.ID);
    service.store(fd);

    // compare to new loaded
    DocumentFormData refFd = loadDocument(id_document);
    Assert.assertEquals(OcrLanguageCodeType.EnglishCode.ID, refFd.getOcrLanguage().getValue());
  }

  @Test
  @Ignore
  public void testParseOcr() {

    DocumentFormData fd = loadDocument(id_document);
    // modify
    fd.getParseOcr().setValue(true);
    service.storeInternal(fd).awaitDone();

    // compare to new loaded
    DocumentFormData refFd = loadDocument(id_document);
    Assert.assertTrue(refFd.getParseOcr().getValue());
  }

  @Test
  public void testModifyOriginalStorage() {
    DocumentFormData fd = loadDocument(id_document);
    // modify
    fd.getOriginalStorage().setValue("mod storage");
    service.store(fd);

    // compare to new loaded
    DocumentFormData refFd = assertEqualsDb(fd);
    Assert.assertEquals("mod storage", refFd.getOriginalStorage().getValue());
  }

  @Test
  public void testAddPartner() {
    DocumentFormData fd = loadDocument(id_document);
    // modify
    PartnersRowData newRow = fd.getPartners().addRow();
    newRow.setPartner(id_partner_02);
    service.store(fd);

    // compare to new loaded
    DocumentFormData refFd = assertEqualsDb(fd);
    Assert.assertEquals(CollectionUtility.arrayList(id_partner_01, id_partner_02),
        Arrays.stream(refFd.getPartners().getRows())
            .map(row -> row.getPartner())
            .filter(p -> p != null)
            .sorted()
            .collect(Collectors.toList()));

  }

  @Test
  public void testRemovePartner() {
    DocumentFormData fd = loadDocument(id_document);
    // modify
    for (PartnersRowData rd : fd.getPartners().getRows()) {
      if (ObjectUtility.equals(id_partner_01, rd.getPartner())) {
        fd.getPartners().removeRow(rd);
      }
    }
    service.store(fd);

    // compare to new loaded
    DocumentFormData refFd = assertEqualsDb(fd);
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
    DocumentFormData fd = loadDocument(id_document);
    // modify
    fd.getPartners().clearRows();
    PartnersRowData newRow = fd.getPartners().addRow();
    newRow.setPartner(id_partner_02);
    service.store(fd);

    // compare to new loaded
    DocumentFormData refFd = assertEqualsDb(fd);
    Assert.assertEquals(CollectionUtility.arrayList(id_partner_02),
        Arrays.stream(refFd.getPartners().getRows())
            .map(row -> row.getPartner())
            .filter(p -> p != null)
            .sorted()
            .collect(Collectors.toList()));

  }

  @Test
  public void testChangePermissionOfUser() {
    DocumentFormData fd = loadDocument(id_document);
    // modify
    PermissionsRowData rd = Arrays.stream(fd.getPermissions().getRows()).filter(row -> USER.equals(row.getUser())).findFirst().get();
    rd.setPermission(PermissionCodeType.WriteCode.ID);

    service.store(fd);

    // compare to new loaded
    DocumentFormData refFd = assertEqualsDb(fd);
    List<PermissionsRowData> sortedValidPermissions =
        Arrays.stream(refFd.getPermissions().getRows())
            .filter(row -> row.getUser() != null && row.getPermission() != null)
            .sorted((p1, p2) -> p1.getUser()
                .compareTo(p2.getUser()))
            .collect(Collectors.toList());

    Assert.assertEquals(CollectionUtility.arrayList(USER),
        sortedValidPermissions.stream().map(row -> row.getUser()).collect(Collectors.toList()));
    Assert.assertEquals(CollectionUtility.arrayList(PermissionCodeType.WriteCode.ID),
        sortedValidPermissions.stream().map(row -> row.getPermission()).collect(Collectors.toList()));

  }

  @Test
  public void testAddPermissionUser() {
    DocumentFormData fd = loadDocument(id_document);
    // modify
    PermissionsRowData newRow = fd.getPermissions().addRow();
    newRow.setUser(USER_INACTIVE);
    newRow.setPermission(PermissionCodeType.WriteCode.ID);
    service.store(fd);

    // compare to new loaded
    DocumentFormData refFd = assertEqualsDb(fd);
    List<PermissionsRowData> sortedValidPermissions =
        Arrays.stream(refFd.getPermissions().getRows())
            .filter(row -> row.getUser() != null && row.getPermission() != null)
            .sorted((p1, p2) -> p1.getUser().compareTo(p2.getUser()))
            .collect(Collectors.toList());

    Assert.assertEquals(CollectionUtility.arrayList(USER, USER_INACTIVE),
        sortedValidPermissions.stream().map(row -> row.getUser()).collect(Collectors.toList()));
    Assert.assertEquals(CollectionUtility.arrayList(PermissionCodeType.ReadCode.ID, PermissionCodeType.WriteCode.ID),
        sortedValidPermissions.stream().map(row -> row.getPermission()).collect(Collectors.toList()));

  }

  @Test
  public void testRemovePermissionUser() {
    DocumentFormData fd = loadDocument(id_document);
    // modify

    for (PermissionsRowData rd : fd.getPermissions().getRows()) {
      if (USER.equals(rd.getUser())) {
        fd.getPermissions().removeRow(rd);
      }
    }
    service.store(fd);

    // compare to new loaded
    DocumentFormData refFd = assertEqualsDb(fd);
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
    DocumentFormData fd = loadDocument(id_document);
    // modify
    fd.getPermissions().clearRows();
    PermissionsRowData newRow = fd.getPermissions().addRow();
    newRow.setUser(USER_INACTIVE);
    newRow.setPermission(PermissionCodeType.WriteCode.ID);

    service.store(fd);

    // compare to new loaded
    DocumentFormData refFd = assertEqualsDb(fd);
    List<PermissionsRowData> sortedValidPermissions =
        Arrays.stream(refFd.getPermissions().getRows())
            .filter(row -> row.getUser() != null && row.getPermission() != null)
            .sorted((p1, p2) -> p1.getUser().compareTo(p2.getUser()))
            .collect(Collectors.toList());

    Assert.assertEquals(CollectionUtility.arrayList(USER_INACTIVE),
        sortedValidPermissions.stream().map(row -> row.getUser()).collect(Collectors.toList()));

    Assert.assertEquals(CollectionUtility.arrayList(PermissionCodeType.WriteCode.ID),
        sortedValidPermissions.stream().map(row -> row.getPermission()).collect(Collectors.toList()));

  }

  private DocumentFormData loadDocument(BigDecimal docId) {
    DocumentFormData fd = new DocumentFormData();
    fd.setDocumentId(docId);
    fd = service.load(fd);
    return fd;
  }

  private DocumentFormData assertEqualsDb(DocumentFormData fd) {
    DocumentFormData dbFormData = loadDocument(fd.getDocumentId());
    DocboxAssert.assertEquals(dbFormData, fd);
    return dbFormData;

  }
}

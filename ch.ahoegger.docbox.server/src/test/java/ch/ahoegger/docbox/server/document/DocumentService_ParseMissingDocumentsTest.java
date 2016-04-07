package ch.ahoegger.docbox.server.document;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.BeanMetaData;
import org.eclipse.scout.rt.platform.IBean;
import org.eclipse.scout.rt.platform.IBeanManager;
import org.eclipse.scout.rt.platform.Platform;
import org.eclipse.scout.rt.platform.resource.BinaryResource;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.ahoegger.docbox.server.database.dev.initialization.DocumentOcrTableTask;
import ch.ahoegger.docbox.server.database.dev.initialization.DocumentTableTask;
import ch.ahoegger.docbox.server.database.dev.initialization.UserTableTask;
import ch.ahoegger.docbox.server.ocr.OcrParseResult;
import ch.ahoegger.docbox.server.ocr.OcrParseService;
import ch.ahoegger.docbox.server.test.util.AbstractTestWithDatabase;
import ch.ahoegger.docbox.server.test.util.IdGenerateService;
import ch.ahoegger.docbox.server.test.util.TestDocumentStoreService;
import ch.ahoegger.docbox.shared.document.ocr.DocumentOcrFormData;
import ch.ahoegger.docbox.shared.document.store.IDocumentStoreService;
import ch.ahoegger.docbox.shared.ocr.IDocumentOcrService;

/**
 * <h3>{@link DocumentService_ParseMissingDocumentsTest}</h3>
 *
 * @author aho
 */
public class DocumentService_ParseMissingDocumentsTest extends AbstractTestWithDatabase {
  private static final String username01 = SUBJECT_NAME;

  private static final Long documentId01 = BEANS.get(IdGenerateService.class).getNextId();
  private static final Long documentId02 = BEANS.get(IdGenerateService.class).getNextId();
  private static final Long documentId03 = BEANS.get(IdGenerateService.class).getNextId();
  private static final Long documentId04 = BEANS.get(IdGenerateService.class).getNextId();

  private static List<IBean<?>> s_mockBeans = new ArrayList<IBean<?>>();

  @BeforeClass
  public static void beforeClass() {
    IBeanManager beanManager = Platform.get().getBeanManager();
    s_mockBeans.add(beanManager.registerBean(
        new BeanMetaData(OcrParseService.class)
            .withOrder(-10).withInitialInstance(new OcrParseService() {
              @Override
              public OcrParseResult parsePdf(BinaryResource pdfResource) {
                return new OcrParseResult().withText("parsed").withOcrParsed(true);
              }

            })));
    s_mockBeans.add(beanManager.registerBean(
        new BeanMetaData(IDocumentStoreService.class)
            .withOrder(-10).withInitialInstance(new TestDocumentStoreService() {
              @Override
              public BinaryResource getDocument(Long documentId) {
                return new BinaryResource("dummy", "dummy".getBytes());
              }
            })));
  }

  @AfterClass
  public static void afterClass() {
    IBeanManager beanManager = Platform.get().getBeanManager();
    s_mockBeans.forEach(b -> beanManager.unregisterBean(b));
  }

  @Override
  public void setupDb() throws Exception {
    super.setupDb();

    ISqlService sqlService = BEANS.get(ISqlService.class);

    BEANS.get(UserTableTask.class).insertUser(sqlService, "name01", "firstname01", username01, "secret", true, true);
    // categories
    LocalDate today = LocalDate.now();

    BEANS.get(DocumentTableTask.class).createDocumentRow(sqlService, documentId01, "Cats Document",
        Date.from(today.minusDays(20).atStartOfDay(ZoneId.systemDefault()).toInstant()),
        Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant()),
        null, "2016_03_08_124640.pdf", null, null, true);

    BEANS.get(DocumentTableTask.class).createDocumentRow(sqlService, documentId02, "Abstract Document",
        Date.from(today.minusDays(20).atStartOfDay(ZoneId.systemDefault()).toInstant()),
        Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant()),
        null, "2016_03_08_124640.pdf", null, null, true);

    BEANS.get(DocumentTableTask.class).createDocumentRow(sqlService, documentId03, "Dogs Document",
        Date.from(today.minusDays(20).atStartOfDay(ZoneId.systemDefault()).toInstant()),
        Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant()),
        null, "2016_03_08_124640.pdf", null, null, true);

    BEANS.get(DocumentTableTask.class).createDocumentRow(sqlService, documentId04, "All fish are wet",
        Date.from(today.minusDays(20).atStartOfDay(ZoneId.systemDefault()).toInstant()),
        Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant()),
        null, "2016_03_08_124640.pdf", null, null, false);

    BEANS.get(DocumentOcrTableTask.class).createDocumentOcrRow(sqlService, documentId01, "parsed01", true, false);

  }

  @Test
  public void testParseAll() {
    DocumentService service = BEANS.get(DocumentService.class);
    service.buildOcrOfMissingDocumentsInternal(null).awaitDone();

    assertOcrData(documentId01, true);
    assertOcrData(documentId02, true);
    assertOcrData(documentId03, true);
    assertOcrData(documentId04, false);

  }

  @Test
  public void testParseSome() {
    DocumentService service = BEANS.get(DocumentService.class);
    service.buildOcrOfMissingDocumentsInternal(CollectionUtility.arrayList(documentId02)).awaitDone();

    assertOcrData(documentId01, true);
    assertOcrData(documentId02, true);
    assertOcrData(documentId03, false);
    assertOcrData(documentId04, false);

  }

  private void assertOcrData(Long documentId, boolean exist) {
    DocumentOcrFormData fd1 = new DocumentOcrFormData();
    fd1.setDocumentId(documentId);
    DocumentOcrFormData fd2 = BEANS.get(IDocumentOcrService.class).load(fd1);
    if (exist) {
      Assert.assertNotNull(String.format("Expected document '%s' to have parsed content.", documentId), fd2);
    }
    else {
      Assert.assertNull(String.format("Expected document '%s' to have NO parsed content.", documentId), fd2);
    }
  }

}

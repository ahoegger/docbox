package ch.ahoegger.docbox.server.document;

import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.BeanMetaData;
import org.eclipse.scout.rt.platform.IBean;
import org.eclipse.scout.rt.platform.IBeanManager;
import org.eclipse.scout.rt.platform.Platform;
import org.eclipse.scout.rt.platform.resource.BinaryResource;
import org.eclipse.scout.rt.platform.resource.BinaryResources;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.platform.util.FileUtility;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.ahoegger.docbox.server.ocr.DocumentOcrService;
import ch.ahoegger.docbox.server.ocr.OcrParseResult;
import ch.ahoegger.docbox.server.ocr.OcrParseService;
import ch.ahoegger.docbox.server.test.util.AbstractTestWithDatabase;
import ch.ahoegger.docbox.server.test.util.IdGenerateService;
import ch.ahoegger.docbox.server.test.util.TestDocumentStoreService;
import ch.ahoegger.docbox.shared.document.ocr.DocumentOcrFormData;
import ch.ahoegger.docbox.shared.document.store.IDocumentStoreService;
import ch.ahoegger.docbox.shared.ocr.IDocumentOcrService;
import ch.ahoegger.docbox.shared.ocr.OcrLanguageCodeType;
import ch.ahoegger.docbox.shared.util.LocalDateUtility;

/**
 * <h3>{@link DocumentService_ParseMissingDocumentsTest}</h3>
 *
 * @author Andreas Hoegger
 */
public class DocumentService_ParseMissingDocumentsTest extends AbstractTestWithDatabase {

  private static final BigDecimal documentId01 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private static final BigDecimal documentId02 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private static final BigDecimal documentId03 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private static final BigDecimal documentId04 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();

  private static List<IBean<?>> s_mockBeans = new ArrayList<IBean<?>>();

  @BeforeClass
  public static void beforeClass() {
    IBeanManager beanManager = Platform.get().getBeanManager();
    s_mockBeans.add(beanManager.registerBean(
        new BeanMetaData(OcrParseService.class)
            .withOrder(-10).withInitialInstance(new OcrParseService() {
              @Override
              public OcrParseResult parsePdf(InputStream pdfInputStream, String language, Path tessdataDirectory) {
                return new OcrParseResult().withText("parsed").withOcrParsed(true);
              }

            })));
    s_mockBeans.add(beanManager.registerBean(
        new BeanMetaData(IDocumentStoreService.class)
            .withOrder(-10).withInitialInstance(new TestDocumentStoreService() {
              @Override
              public BinaryResource getDocument(BigDecimal documentId) {
                return BinaryResources.create()
                    .withFilename("dummy.pdf")
                    .withContentType(FileUtility.getMimeType("dummy.pdf"))
                    .withContent("dummpy".getBytes()).build();
              }
            })));
  }

  @AfterClass
  public static void afterClass() {
    IBeanManager beanManager = Platform.get().getBeanManager();
    s_mockBeans.forEach(b -> beanManager.unregisterBean(b));
  }

  @Override
  protected void execSetupDb(Connection connection) throws Exception {

    // categories
    LocalDate today = LocalDate.now();

    BEANS.get(DocumentService.class).insert(connection, documentId01, "Cats Document",
        LocalDateUtility.toDate(today.minusDays(20)),
        LocalDateUtility.toDate(today),
        null, "2016_03_08_124640.pdf", null, null, true, OcrLanguageCodeType.GermanCode.ID);

    BEANS.get(DocumentService.class).insert(connection, documentId02, "Abstract Document",
        LocalDateUtility.toDate(today.minusDays(20)),
        LocalDateUtility.toDate(today),
        null, "2016_03_08_124640.pdf", null, null, true, OcrLanguageCodeType.GermanCode.ID);

    BEANS.get(DocumentService.class).insert(connection, documentId03, "Dogs Document",
        LocalDateUtility.toDate(today.minusDays(20)),
        LocalDateUtility.toDate(today),
        null, "2016_03_08_124640.pdf", null, null, true, OcrLanguageCodeType.GermanCode.ID);

    BEANS.get(DocumentService.class).insert(connection, documentId04, "All fish are wet",
        LocalDateUtility.toDate(today.minusDays(20)),
        LocalDateUtility.toDate(today),
        null, "2016_03_08_124640.pdf", null, null, false, null);

    BEANS.get(DocumentOcrService.class).insert(connection, documentId01, "parsed01", true, 1, null);

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

  private void assertOcrData(BigDecimal documentId, boolean exist) {
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

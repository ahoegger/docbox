package ch.ahoegger.docbox.server.document;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.resource.BinaryResource;
import org.eclipse.scout.rt.platform.resource.BinaryResources;
import org.eclipse.scout.rt.platform.util.FileUtility;
import org.eclipse.scout.rt.platform.util.IOUtility;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import ch.ahoegger.docbox.server.database.dev.initialization.DocumentTableTask;
import ch.ahoegger.docbox.server.document.store.DocumentStoreService;
import ch.ahoegger.docbox.server.ocr.DocumentOcrService;
import ch.ahoegger.docbox.server.test.util.AbstractTestWithDatabase;
import ch.ahoegger.docbox.server.test.util.IdGenerateService;
import ch.ahoegger.docbox.shared.document.ocr.DocumentOcrFormData;
import ch.ahoegger.docbox.shared.ocr.OcrLanguageCodeType;
import ch.ahoegger.docbox.shared.util.LocalDateUtility;

/**
 * <h3>{@link DocumentService_MultipleOcrParseTest}</h3>
 *
 * @author aho
 */
@Ignore
public class DocumentService_MultipleOcrParseTest extends AbstractTestWithDatabase {

  private List<BigDecimal> m_documentIds = new ArrayList<>();
  private static final String FILE_NAME = "aiw.pdf";
//  private static final String FILE_NAME = "withoutTextInfo.pdf";

  @Override
  public void setupDb() throws Exception {
    super.setupDb();

    ISqlService sqlService = BEANS.get(ISqlService.class);

    for (int i = 0; i < 4; i++) {
      BigDecimal docId = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
      m_documentIds.add(docId);
      createDocument(sqlService, docId, LocalDateUtility.toDate(LocalDate.now().plusDays(10 - i)), FILE_NAME);
    }

  }

  private void createDocument(ISqlService sqlService, BigDecimal documentId, Date insertDate, String fileName) throws IOException {
    URL resource = getClass().getClassLoader().getResource("devDocuments/" + fileName);
    BinaryResource br = BinaryResources.create().withFilename(fileName).withContentType(FileUtility.getContentTypeForExtension(FileUtility.getFileExtension(fileName))).withContent(IOUtility.readFromUrl(resource))
        .withLastModified(System.currentTimeMillis()).build();
    String docPath = BEANS.get(DocumentStoreService.class).store(br, insertDate, documentId);
    BEANS.get(DocumentTableTask.class).insert(sqlService, documentId, "", insertDate, insertDate, null, docPath, null, null, true, OcrLanguageCodeType.GermanCode.ID);
  }

  @Test
  public void createAllOcr() {

    BEANS.get(DocumentService.class).buildOcrOfMissingDocumentsInternal(m_documentIds).awaitDone();
    for (BigDecimal docId : m_documentIds) {
      DocumentOcrFormData fd = new DocumentOcrFormData();
      fd.setDocumentId(docId);
      fd = BEANS.get(DocumentOcrService.class).load(fd);
      Assert.assertTrue(StringUtility.hasText(fd.getText().getValue()));
      Assert.assertTrue(StringUtility.isNullOrEmpty(fd.getParseFailedReason().getValue()));
    }

  }
}

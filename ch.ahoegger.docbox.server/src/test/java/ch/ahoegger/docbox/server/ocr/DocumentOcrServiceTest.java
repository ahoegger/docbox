package ch.ahoegger.docbox.server.ocr;

import java.math.BigDecimal;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.junit.Assert;
import org.junit.Test;

import ch.ahoegger.docbox.server.database.dev.initialization.DocumentOcrTableTask;
import ch.ahoegger.docbox.server.test.util.AbstractTestWithDatabase;
import ch.ahoegger.docbox.server.test.util.IdGenerateService;

/**
 * <h3>{@link DocumentOcrServiceTest}</h3>
 *
 * @author aho
 */
public class DocumentOcrServiceTest extends AbstractTestWithDatabase {

  private BigDecimal documentId = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();

  @Override
  public void setupDb() throws Exception {
    super.setupDb();

    ISqlService sqlService = BEANS.get(ISqlService.class);

    BEANS.get(DocumentOcrTableTask.class).insert(sqlService, documentId, "sample", true, 1, null);

  }

  @Test
  public void testExist() {
    Assert.assertTrue(BEANS.get(DocumentOcrService.class).exists(documentId));
    Assert.assertFalse(BEANS.get(DocumentOcrService.class).exists(BigDecimal.valueOf(22229999)));
  }

  @Test
  public void deleteNotExisting() {
    Assert.assertTrue(BEANS.get(DocumentOcrService.class).delete(documentId));
    Assert.assertFalse(BEANS.get(DocumentOcrService.class).delete(BigDecimal.valueOf(22229999)));
  }

}

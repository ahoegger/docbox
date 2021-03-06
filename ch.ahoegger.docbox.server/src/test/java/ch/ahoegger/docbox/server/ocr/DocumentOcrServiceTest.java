package ch.ahoegger.docbox.server.ocr;

import java.math.BigDecimal;
import java.sql.Connection;

import org.eclipse.scout.rt.platform.BEANS;
import org.junit.Assert;
import org.junit.Test;

import ch.ahoegger.docbox.server.test.util.AbstractTestWithDatabase;
import ch.ahoegger.docbox.server.test.util.IdGenerateService;
import ch.ahoegger.docbox.server.test.util.TestDataGenerator;

/**
 * <h3>{@link DocumentOcrServiceTest}</h3>
 *
 * @author Andreas Hoegger
 */
public class DocumentOcrServiceTest extends AbstractTestWithDatabase {

  private BigDecimal documentId = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();

  @Override
  protected void execSetupDb(Connection connection, TestDataGenerator testDataGenerator) throws Exception {
    BEANS.get(DocumentOcrService.class).insert(connection, documentId, "sample", true, 1, null);

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

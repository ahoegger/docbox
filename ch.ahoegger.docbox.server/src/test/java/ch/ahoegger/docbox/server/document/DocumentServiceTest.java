package ch.ahoegger.docbox.server.document;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;

import org.ch.ahoegger.docbox.server.or.app.tables.Category;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.resource.BinaryResource;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.Assert;
import org.junit.Test;

import ch.ahoegger.docbox.server.conversation.ConversationService;
import ch.ahoegger.docbox.server.document.store.DocumentStoreService;
import ch.ahoegger.docbox.server.ocr.DocumentOcrService;
import ch.ahoegger.docbox.server.partner.PartnerService;
import ch.ahoegger.docbox.server.test.util.AbstractTestWithDatabase;
import ch.ahoegger.docbox.server.test.util.DocboxAssert;
import ch.ahoegger.docbox.server.test.util.IdGenerateService;
import ch.ahoegger.docbox.server.test.util.TestDataGenerator;
import ch.ahoegger.docbox.shared.document.DocumentFormData;
import ch.ahoegger.docbox.shared.document.DocumentFormData.Partners.PartnersRowData;
import ch.ahoegger.docbox.shared.document.IDocumentService;

/**
 * <h3>{@link DocumentServiceTest}</h3>
 *
 * @author Andreas Hoegger
 */
public class DocumentServiceTest extends AbstractTestWithDatabase {

  private static final BigDecimal categoryId01 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();

  private static final BigDecimal partnerId01 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private static final BigDecimal partnerId02 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();

  private static final BigDecimal conversationId01 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private static final BigDecimal conversationId02 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();

  private static final BigDecimal documentId01 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private static final BigDecimal documentId02 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private static final BigDecimal documentId03 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private static final BigDecimal documentId04 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();

  @Override
  protected void execSetupDb(Connection connection, TestDataGenerator testDataGenerator) throws Exception {

    // categories
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DAY_OF_MONTH, 5);
    DateUtility.truncCalendar(cal);
    Date documentDate = cal.getTime();
    Category categoryTable = Category.CATEGORY;
    DSL.using(connection, SQLDialect.DERBY).newRecord(categoryTable)
        .with(categoryTable.CATEGORY_NR, categoryId01)
        .with(categoryTable.NAME, "sampleCatetory")
        .with(categoryTable.START_DATE, documentDate)
        .insert();

    // partner
    BEANS.get(PartnerService.class).insert(connection, partnerId01, "partner01", null, cal.getTime(), null);
    BEANS.get(PartnerService.class).insert(connection, partnerId02, "partner02", null, cal.getTime(), null);

    // conversation
    BEANS.get(ConversationService.class).insert(connection, conversationId01, "con01", null, cal.getTime(), null);
    BEANS.get(ConversationService.class).insert(connection, conversationId02, "con02", null, cal.getTime(), null);

    cal = Calendar.getInstance();
    cal.set(1982, 04, 20);
    BEANS.get(DocumentService.class).insert(connection, documentId01, "Cats Document", cal.getTime(), cal.getTime(), cal.getTime(), "2016_03_08_124640.pdf", null, null, false, null);
    cal.set(1982, 04, 21);
    BEANS.get(DocumentService.class).insert(connection, documentId02, "Abstract Document", cal.getTime(), cal.getTime(), cal.getTime(), "2016_03_08_124640.pdf", null, null, false, null);
    cal.set(1982, 04, 22);
    BEANS.get(DocumentService.class).insert(connection, documentId03, "Dogs Document", cal.getTime(), cal.getTime(), cal.getTime(), "2016_03_08_124640.pdf", null, null, false, null);
    cal.set(1982, 04, 23);
    BEANS.get(DocumentService.class).insert(connection, documentId04, "All fish are wet", cal.getTime(), cal.getTime(), cal.getTime(), "2016_03_08_124640.pdf", null, null, false, null);

    // ocr
    BEANS.get(DocumentOcrService.class).insert(connection, documentId04, "All fish are wet", true, 1, null);

  }

  @Test
  public void testCreate() {
    IDocumentService service = BEANS.get(IDocumentService.class);
    DocumentFormData fd1 = new DocumentFormData();
    fd1.getDocument().setValue(new BinaryResource("documentInserTest.pdf", "content".getBytes()));
    fd1.getAbstract().setValue("document insert test");
    fd1 = service.prepareCreate(fd1);
    fd1.getParseOcr().setValue(false);
    Calendar cal = Calendar.getInstance();
    cal.set(1997, 12, 25);
    DateUtility.truncCalendar(cal);
    fd1.getDocumentDate().setValue(cal.getTime());
    fd1.getConversation().setValue(conversationId01);
    PartnersRowData p1 = fd1.getPartners().addRow();
    p1.setPartner(partnerId01);
    fd1.getCategoriesBox().setValue(CollectionUtility.hashSet(new BigDecimal(900)));

    fd1 = service.create(fd1);

    DocumentFormData fd2 = new DocumentFormData();
    fd2.setDocumentId(fd1.getDocumentId());
    fd2 = service.load(fd2);

    Assert.assertTrue(BEANS.get(DocumentStoreService.class).get(fd1.getDocumentPath()).getContentLength() > 0);
    DocboxAssert.assertEquals(fd1, fd2);
  }

  @Test
  public void testDelete() {
    IDocumentService service = BEANS.get(IDocumentService.class);
    service.delete(documentId04);

    DocumentFormData fd = new DocumentFormData();
    fd.setDocumentId(documentId04);
    Assert.assertNull(service.load(fd));

    Assert.assertFalse(BEANS.get(DocumentOcrService.class).exists(documentId04));
  }

  @Test
  public void testModify() {

  }
}

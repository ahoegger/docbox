package ch.ahoegger.docbox.server.test.document;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.resource.BinaryResource;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.junit.Test;

import ch.ahoegger.docbox.server.database.initialization.CategoryTableTask;
import ch.ahoegger.docbox.server.database.initialization.ConversationTableTask;
import ch.ahoegger.docbox.server.database.initialization.DocumentTableTask;
import ch.ahoegger.docbox.server.database.initialization.PartnerTableTask;
import ch.ahoegger.docbox.server.database.initialization.UserTableTask;
import ch.ahoegger.docbox.server.test.util.AbstractTestWithDatabase;
import ch.ahoegger.docbox.server.test.util.DocboxAssert;
import ch.ahoegger.docbox.server.test.util.IdGenerateService;
import ch.ahoegger.docbox.shared.document.DocumentFormData;
import ch.ahoegger.docbox.shared.document.DocumentFormData.Partners.PartnersRowData;
import ch.ahoegger.docbox.shared.document.IDocumentService;

/**
 * <h3>{@link DocumentServiceTest}</h3>
 *
 * @author aho
 */
public class DocumentServiceTest extends AbstractTestWithDatabase {
  private static final String username01 = SUBJECT_NAME;

  private static final Long categoryId01 = BEANS.get(IdGenerateService.class).getNextId();

  private static final Long partnerId01 = BEANS.get(IdGenerateService.class).getNextId();
  private static final Long partnerId02 = BEANS.get(IdGenerateService.class).getNextId();

  private static final Long conversationId01 = BEANS.get(IdGenerateService.class).getNextId();
  private static final Long conversationId02 = BEANS.get(IdGenerateService.class).getNextId();

  private static final Long documentId01 = BEANS.get(IdGenerateService.class).getNextId();
  private static final Long documentId02 = BEANS.get(IdGenerateService.class).getNextId();
  private static final Long documentId03 = BEANS.get(IdGenerateService.class).getNextId();
  private static final Long documentId04 = BEANS.get(IdGenerateService.class).getNextId();

  @Override
  public void setupDb() throws Exception {
    super.setupDb();

    ISqlService sqlService = BEANS.get(ISqlService.class);

    BEANS.get(UserTableTask.class).createUser(sqlService, "name01", "firstname01", username01, "secret", true, true);
    // categories
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DAY_OF_MONTH, 5);
    DateUtility.truncCalendar(cal);
    Date documentDate = cal.getTime();
    BEANS.get(CategoryTableTask.class).createCategoryRow(sqlService, categoryId01, "sampleCategoriy", null, documentDate, null);

    // partner
    BEANS.get(PartnerTableTask.class).createPartnerRow(sqlService, partnerId01, "partner01", null, cal.getTime(), null);
    BEANS.get(PartnerTableTask.class).createPartnerRow(sqlService, partnerId02, "partner02", null, cal.getTime(), null);

    // conversation
    BEANS.get(ConversationTableTask.class).createConversationRow(sqlService, conversationId01, "con01", null, cal.getTime(), null);
    BEANS.get(ConversationTableTask.class).createConversationRow(sqlService, conversationId02, "con02", null, cal.getTime(), null);

    cal = Calendar.getInstance();
    cal.set(1982, 04, 20);
    BEANS.get(DocumentTableTask.class).createDocumentRow(sqlService, documentId01, "Cats Document", cal.getTime(), cal.getTime(), cal.getTime(), "2016_03_08_124640.pdf", null, null);
    cal.set(1982, 04, 21);
    BEANS.get(DocumentTableTask.class).createDocumentRow(sqlService, documentId02, "Abstract Document", cal.getTime(), cal.getTime(), cal.getTime(), "2016_03_08_124640.pdf", null, null);
    cal.set(1982, 04, 22);
    BEANS.get(DocumentTableTask.class).createDocumentRow(sqlService, documentId03, "Dogs Document", cal.getTime(), cal.getTime(), cal.getTime(), "2016_03_08_124640.pdf", null, null);
    cal.set(1982, 04, 23);
    BEANS.get(DocumentTableTask.class).createDocumentRow(sqlService, documentId04, "All fish are wet", cal.getTime(), cal.getTime(), cal.getTime(), "2016_03_08_124640.pdf", null, null);
  }

  @Test
  public void testCreate() {
    IDocumentService service = BEANS.get(IDocumentService.class);
    DocumentFormData fd1 = new DocumentFormData();
    fd1.getDocument().setValue(new BinaryResource("documentInserTest.pdf", "content".getBytes()));
    fd1.getAbstract().setValue("document insert test");
    fd1 = service.prepareCreate(fd1);
    Calendar cal = Calendar.getInstance();
    cal.set(1997, 12, 25);
    DateUtility.truncCalendar(cal);
    fd1.getDocumentDate().setValue(cal.getTime());
    fd1.getConversation().setValue(new BigDecimal(conversationId01));
    PartnersRowData p1 = fd1.getPartners().addRow();
    p1.setPartner(new BigDecimal(partnerId01));
    fd1.getCategoriesBox().setValue(CollectionUtility.hashSet(new BigDecimal(900)));

    fd1 = service.create(fd1);

    DocumentFormData fd2 = new DocumentFormData();
    fd2.setDocumentId(fd1.getDocumentId());
    fd2 = service.load(fd2);

    DocboxAssert.assertEquals(fd1, fd2);
  }

  @Test
  public void testModify() {

  }
}

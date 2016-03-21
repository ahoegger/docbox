package ch.ahoegger.docbox.server.test.document;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.resource.BinaryResource;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.ahoegger.docbox.server.database.initialization.CategoryTableTask;
import ch.ahoegger.docbox.server.database.initialization.ConversationTableTask;
import ch.ahoegger.docbox.server.database.initialization.DocumentTableTask;
import ch.ahoegger.docbox.server.database.initialization.PartnerTableTask;
import ch.ahoegger.docbox.server.database.initialization.UserTableTask;
import ch.ahoegger.docbox.server.test.util.AbstractTestWithDatabase;
import ch.ahoegger.docbox.server.test.util.DocboxAssert;
import ch.ahoegger.docbox.shared.document.DocumentFormData;
import ch.ahoegger.docbox.shared.document.DocumentFormData.Partners.PartnersRowData;
import ch.ahoegger.docbox.shared.document.IDocumentService;

/**
 * <h3>{@link DocumentServiceTest}</h3>
 *
 * @author aho
 */
public class DocumentServiceTest extends AbstractTestWithDatabase {
  @BeforeClass
  public static void createTestRows() throws IOException {

    ISqlService sqlService = BEANS.get(ISqlService.class);

    BEANS.get(UserTableTask.class).createUser(sqlService, "name", "firstname", "admin", "secret", true, true);
    // categories
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DAY_OF_MONTH, 5);
    DateUtility.truncCalendar(cal);
    Date documentDate = cal.getTime();
    BEANS.get(CategoryTableTask.class).createCategoryRow(sqlService, 900, "sampleCategoriy", null, documentDate, null);

    // partner
    BEANS.get(PartnerTableTask.class).createPartnerRow(sqlService, 120, "partner01", null, cal.getTime(), null);
    BEANS.get(PartnerTableTask.class).createPartnerRow(sqlService, 121, "partner02", null, cal.getTime(), null);

    // conversation
    BEANS.get(ConversationTableTask.class).createConversationRow(sqlService, 130, "con01", null, cal.getTime(), null);
    BEANS.get(ConversationTableTask.class).createConversationRow(sqlService, 131, "con02", null, cal.getTime(), null);

    cal = Calendar.getInstance();
    cal.set(1982, 04, 20);
    BEANS.get(DocumentTableTask.class).createDocumentRow(sqlService, 2000, "Cats Document", cal.getTime(), cal.getTime(), cal.getTime(), "2016_03_08_124640.pdf", null, null);
    cal.set(1982, 04, 21);
    BEANS.get(DocumentTableTask.class).createDocumentRow(sqlService, 2001, "Abstract Document", cal.getTime(), cal.getTime(), cal.getTime(), "2016_03_08_124640.pdf", null, null);
    cal.set(1982, 04, 22);
    BEANS.get(DocumentTableTask.class).createDocumentRow(sqlService, 2002, "Dogs Document", cal.getTime(), cal.getTime(), cal.getTime(), "2016_03_08_124640.pdf", null, null);
    cal.set(1982, 04, 23);
    BEANS.get(DocumentTableTask.class).createDocumentRow(sqlService, 2003, "All fish are wet", cal.getTime(), cal.getTime(), cal.getTime(), "2016_03_08_124640.pdf", null, null);
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
    fd1.getConversation().setValue(new BigDecimal(130));
    PartnersRowData p1 = fd1.getPartners().addRow();
    p1.setPartner(new BigDecimal(120));
    fd1.getCategoriesBox().setValue(CollectionUtility.hashSet(new BigDecimal(900)));

    fd1 = service.create(fd1);

    DocumentFormData fd2 = new DocumentFormData();
    fd2.setDocumentId(fd1.getDocumentId());
    fd2 = service.load(fd2);

    DocboxAssert.assertEquals(fd1, fd2);
  }
}

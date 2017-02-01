package ch.ahoegger.docbox.server.conversation;

import java.math.BigDecimal;
import java.util.Calendar;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.junit.Assert;
import org.junit.Test;

import ch.ahoegger.docbox.server.test.util.AbstractTestWithDatabase;
import ch.ahoegger.docbox.server.test.util.DocboxAssert;
import ch.ahoegger.docbox.server.test.util.IdGenerateService;
import ch.ahoegger.docbox.shared.conversation.ConversationFormData;
import ch.ahoegger.docbox.shared.conversation.IConversationService;

/**
 * <h3>{@link ConversationServiceTest}</h3>
 *
 * @author Andreas Hoegger
 */

public class ConversationServiceTest extends AbstractTestWithDatabase {

  private static final BigDecimal conversationId01 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private static final BigDecimal conversationId02 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();

  @Override
  public void setupDb() throws Exception {
    super.setupDb();

    ISqlService sqlService = BEANS.get(ISqlService.class);
    Calendar cal = Calendar.getInstance();
    DateUtility.truncCalendar(cal);
    cal.set(1999, 04, 29);
    BEANS.get(ConversationService.class).insert(sqlService, conversationId01, "sample conversation 01", "some notes", cal.getTime(), null);
    cal.set(1999, 04, 30);
    BEANS.get(ConversationService.class).insert(sqlService, conversationId02, "sample conversation 02", "some notes", cal.getTime(), null);
  }

  @Test
  public void testCreateConversation() {
    IConversationService service = BEANS.get(IConversationService.class);

    ConversationFormData fd1 = new ConversationFormData();
    fd1 = service.prepareCreate(fd1);

    fd1.getName().setValue("inserted conversation");
    fd1.getNotes().setValue("some notes");
    Calendar cal = Calendar.getInstance();
    DateUtility.truncCalendar(cal);
    cal.set(2014, 03, 22);

    fd1.getStartDate().setValue(cal.getTime());
    cal.set(2099, 03, 22);
    fd1.getEndDate().setValue(cal.getTime());
    fd1 = service.create(fd1);

    ConversationFormData fd2 = new ConversationFormData();
    fd2.setConversationId(fd1.getConversationId());
    fd2 = service.load(fd2);

    DocboxAssert.assertEquals(fd1, fd2);
  }

  @Test
  public void testModifyConversation() {
    IConversationService service = BEANS.get(IConversationService.class);

    ConversationFormData fd1 = new ConversationFormData();
    fd1.setConversationId(conversationId02);
    fd1 = service.load(fd1);

    fd1.getName().setValue("modified.name");
    fd1.getNotes().setValue("modified.notes");
    Calendar cal = Calendar.getInstance();
    DateUtility.truncCalendar(cal);
    cal.set(2014, 03, 28);
    fd1.getStartDate().setValue(cal.getTime());
    cal.set(2088, 12, 28);
    fd1.getEndDate().setValue(cal.getTime());

    fd1 = service.store(fd1);

    ConversationFormData fd2 = new ConversationFormData();
    fd2.setConversationId(conversationId02);
    fd2 = service.load(fd2);

    DocboxAssert.assertEquals(fd1, fd2);

  }

  @Test
  public void testDelete() {
    IConversationService service = BEANS.get(IConversationService.class);
    service.delete(conversationId02);

    ConversationFormData fd1 = new ConversationFormData();
    fd1.setConversationId(conversationId02);
    fd1 = service.load(fd1);
    Assert.assertNull(fd1);

  }

}

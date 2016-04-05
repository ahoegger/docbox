package ch.ahoegger.docbox.server.conversation;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.stream.Collectors;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.platform.util.TriState;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.junit.Assert;
import org.junit.Test;

import ch.ahoegger.docbox.server.database.dev.initialization.ConversationTableTask;
import ch.ahoegger.docbox.server.test.util.AbstractTestWithDatabase;
import ch.ahoegger.docbox.server.test.util.IdGenerateService;
import ch.ahoegger.docbox.shared.conversation.ConversationLookupCall;

/**
 * <h3>{@link ConversationLookupServiceTest}</h3>
 *
 * @author aho
 */
public class ConversationLookupServiceTest extends AbstractTestWithDatabase {

  private static final Long conversationId01 = BEANS.get(IdGenerateService.class).getNextId();
  private static final Long conversationId02 = BEANS.get(IdGenerateService.class).getNextId();
  private static final Long conversationId03 = BEANS.get(IdGenerateService.class).getNextId();
  private static final Long conversationId04 = BEANS.get(IdGenerateService.class).getNextId();

  @Override
  public void setupDb() throws Exception {
    super.setupDb();

    ISqlService sqlService = BEANS.get(ISqlService.class);
    LocalDate today = LocalDate.now();

    BEANS.get(ConversationTableTask.class).createConversationRow(sqlService, conversationId01, "sample conversation 01", "some notes",
        Date.from(today.minusDays(20).atStartOfDay(ZoneId.systemDefault()).toInstant()), null);

    // till yesterday
    BEANS.get(ConversationTableTask.class).createConversationRow(sqlService, conversationId02, "sample conversation 02", "some notes",
        Date.from(today.minusDays(10).atStartOfDay(ZoneId.systemDefault()).toInstant()), Date.from(today.minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));

    // till today
    BEANS.get(ConversationTableTask.class).createConversationRow(sqlService, conversationId03, "sample conversation 03", "some notes",
        Date.from(today.minusDays(10).atStartOfDay(ZoneId.systemDefault()).toInstant()),
        Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant()));

    // till tomorrow
    BEANS.get(ConversationTableTask.class).createConversationRow(sqlService, conversationId04, "sample conversation 04", "some notes",
        Date.from(today.minusDays(10).atStartOfDay(ZoneId.systemDefault()).toInstant()),
        Date.from(today.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));

  }

  @Test
  public void testAll() {
    ConversationLookupCall lookupCall = new ConversationLookupCall();
    lookupCall.setActive(TriState.UNDEFINED);
    Assert.assertEquals(CollectionUtility.arrayList(conversationId01, conversationId02, conversationId03, conversationId04),
        lookupCall.getDataByAll().stream()
            .map(row -> row.getKey())
            .map(key -> key.longValue())
            .sorted()
            .collect(Collectors.toList()));
  }

  @Test
  public void testActiveOnly() {
    ConversationLookupCall lookupCall = new ConversationLookupCall();
    lookupCall.setActive(TriState.TRUE);
    Assert.assertEquals(CollectionUtility.arrayList(conversationId01, conversationId03, conversationId04),
        lookupCall.getDataByAll().stream()
            .map(row -> row.getKey())
            .map(key -> key.longValue())
            .sorted()
            .collect(Collectors.toList()));
  }

  @Test
  public void testInactiveOnly() {
    ConversationLookupCall lookupCall = new ConversationLookupCall();
    lookupCall.setActive(TriState.FALSE);
    Assert.assertEquals(CollectionUtility.arrayList(conversationId02),
        lookupCall.getDataByAll().stream()
            .map(row -> row.getKey())
            .map(key -> key.longValue())
            .sorted()
            .collect(Collectors.toList()));
  }

}

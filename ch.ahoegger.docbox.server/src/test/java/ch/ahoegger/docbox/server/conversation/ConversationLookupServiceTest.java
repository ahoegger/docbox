package ch.ahoegger.docbox.server.conversation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.Collectors;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.platform.util.TriState;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.junit.Assert;
import org.junit.Test;

import ch.ahoegger.docbox.server.test.util.AbstractTestWithDatabase;
import ch.ahoegger.docbox.server.test.util.IdGenerateService;
import ch.ahoegger.docbox.shared.conversation.ConversationLookupCall;
import ch.ahoegger.docbox.shared.util.LocalDateUtility;

/**
 * <h3>{@link ConversationLookupServiceTest}</h3>
 *
 * @author Andreas Hoegger
 */
public class ConversationLookupServiceTest extends AbstractTestWithDatabase {

  private static final BigDecimal conversationId01 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private static final BigDecimal conversationId02 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private static final BigDecimal conversationId03 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private static final BigDecimal conversationId04 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();

  @Override
  public void setupDb() throws Exception {
    super.setupDb();

    ISqlService sqlService = BEANS.get(ISqlService.class);
    LocalDate today = LocalDate.now();

    BEANS.get(ConversationService.class).insert(sqlService.getConnection(), conversationId01, "sample conversation 01", "some notes",
        LocalDateUtility.toDate(today.minusDays(20)), null);

    // till yesterday
    BEANS.get(ConversationService.class).insert(sqlService.getConnection(), conversationId02, "sample conversation 02", "some notes",
        LocalDateUtility.toDate(today.minusDays(10)),
        LocalDateUtility.toDate(today.minusDays(1)));

    // till today
    BEANS.get(ConversationService.class).insert(sqlService.getConnection(), conversationId03, "sample conversation 03", "some notes",
        LocalDateUtility.toDate(today.minusDays(10)),
        LocalDateUtility.toDate(today));

    // till tomorrow
    BEANS.get(ConversationService.class).insert(sqlService.getConnection(), conversationId04, "sample conversation 04", "some notes",
        LocalDateUtility.toDate(today.minusDays(10)),
        LocalDateUtility.toDate(today.plusDays(1)));

  }

  @Test
  public void testAll() {
    ConversationLookupCall lookupCall = new ConversationLookupCall();
    lookupCall.setActive(TriState.UNDEFINED);
    Assert.assertEquals(CollectionUtility.arrayList(conversationId01, conversationId02, conversationId03, conversationId04),
        lookupCall.getDataByAll().stream()
            .map(row -> row.getKey())
            .map(key -> key)
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
            .map(key -> key)
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
            .map(key -> key)
            .sorted()
            .collect(Collectors.toList()));
  }

}

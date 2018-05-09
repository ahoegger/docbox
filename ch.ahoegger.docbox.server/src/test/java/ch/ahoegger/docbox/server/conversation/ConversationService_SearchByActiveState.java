package ch.ahoegger.docbox.server.conversation;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.platform.util.TriState;
import org.junit.Assert;
import org.junit.Test;

import ch.ahoegger.docbox.server.test.util.AbstractTestWithDatabase;
import ch.ahoegger.docbox.server.test.util.IdGenerateService;
import ch.ahoegger.docbox.shared.conversation.ConversationSearchFormData;
import ch.ahoegger.docbox.shared.conversation.ConversationTableData;
import ch.ahoegger.docbox.shared.conversation.IConversationService;
import ch.ahoegger.docbox.shared.util.LocalDateUtility;

/**
 * <h3>{@link ConversationService_SearchByActiveState}</h3>
 *
 * @author Andreas Hoegger
 */
public class ConversationService_SearchByActiveState extends AbstractTestWithDatabase {

  private static final BigDecimal conversationId01 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private static final BigDecimal conversationId02 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private static final BigDecimal conversationId03 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private static final BigDecimal conversationId04 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();

  @Override
  protected void execSetupDb(Connection connection) throws Exception {
    LocalDate today = LocalDate.now();

    BEANS.get(ConversationService.class).insert(connection, conversationId01, "sample conversation 01", "some notes",
        LocalDateUtility.toDate(today.minusDays(20)), null);

    // till yesterday
    BEANS.get(ConversationService.class).insert(connection, conversationId02, "sample conversation 02", "some notes",
        LocalDateUtility.toDate(today.minusDays(10)),
        LocalDateUtility.toDate(today.minusDays(1)));

    // till today
    BEANS.get(ConversationService.class).insert(connection, conversationId03, "sample conversation 03", "some notes",
        LocalDateUtility.toDate(today.minusDays(10)),
        LocalDateUtility.toDate(today));

    // till tomorrow
    BEANS.get(ConversationService.class).insert(connection, conversationId04, "sample conversation 04", "some notes",
        LocalDateUtility.toDate(today.minusDays(10)),
        LocalDateUtility.toDate(today.plusDays(1)));
  }

  @Test
  public void testFindActive() {
    IConversationService service = BEANS.get(IConversationService.class);
    ConversationSearchFormData sd = new ConversationSearchFormData();
    sd.getActiveBox().setValue(TriState.TRUE);
    ConversationTableData tableData = service.getTableData(sd);

    Assert.assertEquals(CollectionUtility.arrayList(conversationId01, conversationId03, conversationId04),
        Arrays.stream(tableData.getRows()).map(row -> row.getConversationId())
            .map(bigDecKey -> bigDecKey)
            .sorted()
            .collect(Collectors.toList()));
  }

  @Test
  public void testFindInactive() {
    IConversationService service = BEANS.get(IConversationService.class);
    ConversationSearchFormData sd = new ConversationSearchFormData();
    sd.getActiveBox().setValue(TriState.FALSE);
    ConversationTableData tableData = service.getTableData(sd);

    Assert.assertEquals(CollectionUtility.arrayList(conversationId02),
        Arrays.stream(tableData.getRows()).map(row -> row.getConversationId())
            .map(bigDecKey -> bigDecKey)
            .sorted()
            .collect(Collectors.toList()));
  }

  @Test
  public void testFindAll() {
    IConversationService service = BEANS.get(IConversationService.class);
    ConversationSearchFormData sd = new ConversationSearchFormData();
    sd.getActiveBox().setValue(TriState.UNDEFINED);
    ConversationTableData tableData = service.getTableData(sd);

    Assert.assertEquals(CollectionUtility.arrayList(conversationId01, conversationId02, conversationId03, conversationId04),
        Arrays.stream(tableData.getRows()).map(row -> row.getConversationId())
            .map(bigDecKey -> bigDecKey)
            .sorted()
            .collect(Collectors.toList()));
  }

}

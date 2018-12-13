package ch.ahoegger.docbox.server.conversation;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.junit.Assert;
import org.junit.Test;

import ch.ahoegger.docbox.server.test.util.AbstractTestWithDatabase;
import ch.ahoegger.docbox.server.test.util.IdGenerateService;
import ch.ahoegger.docbox.server.test.util.TestDataGenerator;
import ch.ahoegger.docbox.shared.conversation.ConversationSearchFormData;
import ch.ahoegger.docbox.shared.conversation.ConversationTableData;
import ch.ahoegger.docbox.shared.conversation.IConversationService;
import ch.ahoegger.docbox.shared.util.LocalDateUtility;

/**
 * <h3>{@link ConversationService_SearchByName}</h3>
 *
 * @author Andreas Hoegger
 */
public class ConversationService_SearchByName extends AbstractTestWithDatabase {

  private static final BigDecimal conversationId01 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private static final BigDecimal conversationId02 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private static final BigDecimal conversationId03 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private static final BigDecimal conversationId04 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();

  @Override
  protected void execSetupDb(Connection connection, TestDataGenerator testDataGenerator) throws Exception {

    LocalDate today = LocalDate.now();

    BEANS.get(ConversationService.class).insert(connection, conversationId01, "dook haagen", "some notes",
        LocalDateUtility.toDate(today.minusDays(20)),
        null);

    // till yesterday
    BEANS.get(ConversationService.class).insert(connection, conversationId02, "smill donat", "some notes",
        LocalDateUtility.toDate(today.minusDays(10)),
        null);

    // till today
    BEANS.get(ConversationService.class).insert(connection, conversationId03, "bluk onack", "some notes",
        LocalDateUtility.toDate(today.minusDays(10)),
        null);

    // till tomorrow
    BEANS.get(ConversationService.class).insert(connection, conversationId04, "7 sense of moon", "some notes",
        LocalDateUtility.toDate(today.minusDays(10)),
        null);
  }

  @Test
  public void testFindActive() {
    IConversationService service = BEANS.get(IConversationService.class);
    ConversationSearchFormData sd = new ConversationSearchFormData();
    sd.getName().setValue("mill");
    ConversationTableData tableData = service.getTableData(sd);

    Assert.assertEquals(CollectionUtility.arrayList(conversationId02),
        Arrays.stream(tableData.getRows()).map(row -> row.getConversationId())
            .map(bigDecKey -> bigDecKey)
            .sorted()
            .collect(Collectors.toList()));
  }

  @Test
  public void testFindInactive() {
    IConversationService service = BEANS.get(IConversationService.class);
    ConversationSearchFormData sd = new ConversationSearchFormData();
    sd.getName().setValue("7");
    ConversationTableData tableData = service.getTableData(sd);

    Assert.assertEquals(CollectionUtility.arrayList(conversationId04),
        Arrays.stream(tableData.getRows()).map(row -> row.getConversationId())
            .map(bigDecKey -> bigDecKey)
            .sorted()
            .collect(Collectors.toList()));
  }

  @Test
  public void testFindAll() {
    IConversationService service = BEANS.get(IConversationService.class);
    ConversationSearchFormData sd = new ConversationSearchFormData();
    sd.getName().setValue("ona");
    ConversationTableData tableData = service.getTableData(sd);

    Assert.assertEquals(CollectionUtility.arrayList(conversationId02, conversationId03),
        Arrays.stream(tableData.getRows()).map(row -> row.getConversationId())
            .map(bigDecKey -> bigDecKey)
            .sorted()
            .collect(Collectors.toList()));
  }

}

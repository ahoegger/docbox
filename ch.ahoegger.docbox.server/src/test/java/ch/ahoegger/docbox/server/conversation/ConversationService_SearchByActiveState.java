package ch.ahoegger.docbox.server.conversation;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
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
import ch.ahoegger.docbox.shared.conversation.ConversationSearchFormData;
import ch.ahoegger.docbox.shared.conversation.ConversationTableData;
import ch.ahoegger.docbox.shared.conversation.IConversationService;

/**
 * <h3>{@link ConversationService_SearchByActiveState}</h3>
 *
 * @author aho
 */
public class ConversationService_SearchByActiveState extends AbstractTestWithDatabase {

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
  public void testFindActive() {
    IConversationService service = BEANS.get(IConversationService.class);
    ConversationSearchFormData sd = new ConversationSearchFormData();
    sd.getActiveBox().setValue(TriState.TRUE);
    ConversationTableData tableData = service.getTableData(sd);

    Assert.assertEquals(CollectionUtility.arrayList(conversationId01, conversationId03, conversationId04),
        Arrays.stream(tableData.getRows()).map(row -> row.getConversationId())
            .map(bigDecKey -> bigDecKey.longValue())
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
            .map(bigDecKey -> bigDecKey.longValue())
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
            .map(bigDecKey -> bigDecKey.longValue())
            .sorted()
            .collect(Collectors.toList()));
  }

}

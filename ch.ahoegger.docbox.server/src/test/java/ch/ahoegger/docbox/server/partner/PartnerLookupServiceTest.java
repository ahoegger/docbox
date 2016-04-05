package ch.ahoegger.docbox.server.partner;

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

import ch.ahoegger.docbox.server.database.dev.initialization.PartnerTableTask;
import ch.ahoegger.docbox.server.test.util.AbstractTestWithDatabase;
import ch.ahoegger.docbox.server.test.util.IdGenerateService;
import ch.ahoegger.docbox.shared.partner.PartnerLookupCall;

/**
 * <h3>{@link PartnerLookupServiceTest}</h3>
 *
 * @author aho
 */
public class PartnerLookupServiceTest extends AbstractTestWithDatabase {

  private static final Long partnerId01 = BEANS.get(IdGenerateService.class).getNextId();
  private static final Long partnerId02 = BEANS.get(IdGenerateService.class).getNextId();
  private static final Long partnerId03 = BEANS.get(IdGenerateService.class).getNextId();
  private static final Long partnerId04 = BEANS.get(IdGenerateService.class).getNextId();

  @Override
  public void setupDb() throws Exception {
    super.setupDb();

    ISqlService sqlService = BEANS.get(ISqlService.class);
    LocalDate today = LocalDate.now();

    BEANS.get(PartnerTableTask.class).createPartnerRow(sqlService, partnerId01, "partner01", "some notes",
        Date.from(today.minusDays(20).atStartOfDay(ZoneId.systemDefault()).toInstant()), null);

    // till yesterday
    BEANS.get(PartnerTableTask.class).createPartnerRow(sqlService, partnerId02, "partner02", "some notes",
        Date.from(today.minusDays(10).atStartOfDay(ZoneId.systemDefault()).toInstant()), Date.from(today.minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));

    // till today
    BEANS.get(PartnerTableTask.class).createPartnerRow(sqlService, partnerId03, "partner03", "some notes",
        Date.from(today.minusDays(10).atStartOfDay(ZoneId.systemDefault()).toInstant()),
        Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant()));

    // till tomorrow
    BEANS.get(PartnerTableTask.class).createPartnerRow(sqlService, partnerId04, "partner04", "some notes",
        Date.from(today.minusDays(10).atStartOfDay(ZoneId.systemDefault()).toInstant()),
        Date.from(today.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));

  }

  @Test
  public void testAll() {
    PartnerLookupCall lookupCall = new PartnerLookupCall();
    lookupCall.setActive(TriState.UNDEFINED);
    Assert.assertEquals(CollectionUtility.arrayList(partnerId01, partnerId02, partnerId03, partnerId04),
        lookupCall.getDataByAll().stream()
            .map(row -> row.getKey())
            .map(key -> key.longValue())
            .sorted()
            .collect(Collectors.toList()));
  }

  @Test
  public void testActiveOnly() {
    PartnerLookupCall lookupCall = new PartnerLookupCall();
    lookupCall.setActive(TriState.TRUE);
    Assert.assertEquals(CollectionUtility.arrayList(partnerId01, partnerId03, partnerId04),
        lookupCall.getDataByAll().stream()
            .map(row -> row.getKey())
            .map(key -> key.longValue())
            .sorted()
            .collect(Collectors.toList()));
  }

  @Test
  public void testInactiveOnly() {
    PartnerLookupCall lookupCall = new PartnerLookupCall();
    lookupCall.setActive(TriState.FALSE);
    Assert.assertEquals(CollectionUtility.arrayList(partnerId02),
        lookupCall.getDataByAll().stream()
            .map(row -> row.getKey())
            .map(key -> key.longValue())
            .sorted()
            .collect(Collectors.toList()));
  }

}

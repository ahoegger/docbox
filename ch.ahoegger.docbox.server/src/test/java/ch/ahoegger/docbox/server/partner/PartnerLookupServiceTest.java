package ch.ahoegger.docbox.server.partner;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.stream.Collectors;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.platform.util.TriState;
import org.junit.Assert;
import org.junit.Test;

import ch.ahoegger.docbox.server.test.util.AbstractTestWithDatabase;
import ch.ahoegger.docbox.server.test.util.IdGenerateService;
import ch.ahoegger.docbox.shared.partner.PartnerLookupCall;
import ch.ahoegger.docbox.shared.util.LocalDateUtility;

/**
 * <h3>{@link PartnerLookupServiceTest}</h3>
 *
 * @author Andreas Hoegger
 */
public class PartnerLookupServiceTest extends AbstractTestWithDatabase {

  private static final BigDecimal partnerId01 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private static final BigDecimal partnerId02 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private static final BigDecimal partnerId03 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private static final BigDecimal partnerId04 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();

  @Override
  protected void execSetupDb(Connection connection) throws Exception {
    LocalDate today = LocalDate.now();

    BEANS.get(PartnerService.class).insert(connection, partnerId01, "partner01", "some notes",
        LocalDateUtility.toDate(today.minusDays(20)),
        null);

    // till yesterday
    BEANS.get(PartnerService.class).insert(connection, partnerId02, "partner02", "some notes",
        LocalDateUtility.toDate(today.minusDays(10)),
        LocalDateUtility.toDate(today.minusDays(1)));

    // till today
    BEANS.get(PartnerService.class).insert(connection, partnerId03, "partner03", "some notes",
        LocalDateUtility.toDate(today.minusDays(10)),
        LocalDateUtility.toDate(today));

    // till tomorrow
    BEANS.get(PartnerService.class).insert(connection, partnerId04, "partner04", "some notes",
        LocalDateUtility.toDate(today.minusDays(10)),
        LocalDateUtility.toDate(today.plusDays(1)));

  }

  @Test
  public void testAll() {
    PartnerLookupCall lookupCall = new PartnerLookupCall();
    lookupCall.setActive(TriState.UNDEFINED);
    Assert.assertEquals(CollectionUtility.arrayList(partnerId01, partnerId02, partnerId03, partnerId04),
        lookupCall.getDataByAll().stream()
            .map(row -> row.getKey())
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
            .sorted()
            .collect(Collectors.toList()));
  }

}

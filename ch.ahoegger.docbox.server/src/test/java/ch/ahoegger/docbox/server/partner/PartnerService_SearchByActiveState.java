package ch.ahoegger.docbox.server.partner;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.platform.util.TriState;
import org.junit.Assert;
import org.junit.Test;

import ch.ahoegger.docbox.server.test.util.AbstractTestWithDatabase;
import ch.ahoegger.docbox.server.test.util.IdGenerateService;
import ch.ahoegger.docbox.shared.partner.IPartnerService;
import ch.ahoegger.docbox.shared.partner.PartnerSearchFormData;
import ch.ahoegger.docbox.shared.partner.PartnerTableData;

/**
 * <h3>{@link PartnerService_SearchByActiveState}</h3>
 *
 * @author Andreas Hoegger
 */
public class PartnerService_SearchByActiveState extends AbstractTestWithDatabase {

  private static final BigDecimal partnerId01 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private static final BigDecimal partnerId02 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private static final BigDecimal partnerId03 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private static final BigDecimal partnerId04 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();

  @Override
  protected void execSetupDb(Connection connection) throws Exception {
    LocalDate today = LocalDate.now();

    BEANS.get(PartnerService.class).insert(connection, partnerId01, "sample conversation 01", "some notes",
        Date.from(today.minusDays(20).atStartOfDay(ZoneId.systemDefault()).toInstant()), null);

    // till yesterday
    BEANS.get(PartnerService.class).insert(connection, partnerId02, "sample conversation 02", "some notes",
        Date.from(today.minusDays(10).atStartOfDay(ZoneId.systemDefault()).toInstant()), Date.from(today.minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));

    // till today
    BEANS.get(PartnerService.class).insert(connection, partnerId03, "sample conversation 03", "some notes",
        Date.from(today.minusDays(10).atStartOfDay(ZoneId.systemDefault()).toInstant()),
        Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant()));

    // till tomorrow
    BEANS.get(PartnerService.class).insert(connection, partnerId04, "sample conversation 04", "some notes",
        Date.from(today.minusDays(10).atStartOfDay(ZoneId.systemDefault()).toInstant()),
        Date.from(today.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
  }

  @Test
  public void testFindActive() {
    IPartnerService service = BEANS.get(IPartnerService.class);
    PartnerSearchFormData sd = new PartnerSearchFormData();
    sd.getActiveBox().setValue(TriState.TRUE);
    PartnerTableData tableData = service.getTableData(sd);

    Assert.assertEquals(CollectionUtility.arrayList(partnerId01, partnerId03, partnerId04),
        Arrays.stream(tableData.getRows()).map(row -> row.getPartnerId())
            .map(bigDecKey -> bigDecKey)
            .sorted()
            .collect(Collectors.toList()));
  }

  @Test
  public void testFindInactive() {
    IPartnerService service = BEANS.get(IPartnerService.class);
    PartnerSearchFormData sd = new PartnerSearchFormData();
    sd.getActiveBox().setValue(TriState.FALSE);
    PartnerTableData tableData = service.getTableData(sd);

    Assert.assertEquals(CollectionUtility.arrayList(partnerId02),
        Arrays.stream(tableData.getRows()).map(row -> row.getPartnerId())
            .map(bigDecKey -> bigDecKey)
            .sorted()
            .collect(Collectors.toList()));
  }

  @Test
  public void testFindAll() {
    IPartnerService service = BEANS.get(IPartnerService.class);
    PartnerSearchFormData sd = new PartnerSearchFormData();
    sd.getActiveBox().setValue(TriState.UNDEFINED);
    PartnerTableData tableData = service.getTableData(sd);

    Assert.assertEquals(CollectionUtility.arrayList(partnerId01, partnerId02, partnerId03, partnerId04),
        Arrays.stream(tableData.getRows()).map(row -> row.getPartnerId())
            .map(bigDecKey -> bigDecKey)
            .sorted()
            .collect(Collectors.toList()));
  }

}

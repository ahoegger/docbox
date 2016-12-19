package ch.ahoegger.docbox.server.partner;

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

import ch.ahoegger.docbox.server.database.dev.initialization.PartnerTableTask;
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

  private static final Long partnerId01 = BEANS.get(IdGenerateService.class).getNextId();
  private static final Long partnerId02 = BEANS.get(IdGenerateService.class).getNextId();
  private static final Long partnerId03 = BEANS.get(IdGenerateService.class).getNextId();
  private static final Long partnerId04 = BEANS.get(IdGenerateService.class).getNextId();

  @Override
  public void setupDb() throws Exception {
    super.setupDb();

    ISqlService sqlService = BEANS.get(ISqlService.class);
    LocalDate today = LocalDate.now();

    BEANS.get(PartnerTableTask.class).createPartnerRow(sqlService, partnerId01, "sample conversation 01", "some notes",
        Date.from(today.minusDays(20).atStartOfDay(ZoneId.systemDefault()).toInstant()), null);

    // till yesterday
    BEANS.get(PartnerTableTask.class).createPartnerRow(sqlService, partnerId02, "sample conversation 02", "some notes",
        Date.from(today.minusDays(10).atStartOfDay(ZoneId.systemDefault()).toInstant()), Date.from(today.minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));

    // till today
    BEANS.get(PartnerTableTask.class).createPartnerRow(sqlService, partnerId03, "sample conversation 03", "some notes",
        Date.from(today.minusDays(10).atStartOfDay(ZoneId.systemDefault()).toInstant()),
        Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant()));

    // till tomorrow
    BEANS.get(PartnerTableTask.class).createPartnerRow(sqlService, partnerId04, "sample conversation 04", "some notes",
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
            .map(bigDecKey -> bigDecKey.longValue())
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
            .map(bigDecKey -> bigDecKey.longValue())
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
            .map(bigDecKey -> bigDecKey.longValue())
            .sorted()
            .collect(Collectors.toList()));
  }

}

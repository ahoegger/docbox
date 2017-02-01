package ch.ahoegger.docbox.server.partner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.junit.Assert;
import org.junit.Test;

import ch.ahoegger.docbox.server.test.util.AbstractTestWithDatabase;
import ch.ahoegger.docbox.server.test.util.IdGenerateService;
import ch.ahoegger.docbox.shared.partner.IPartnerService;
import ch.ahoegger.docbox.shared.partner.PartnerSearchFormData;
import ch.ahoegger.docbox.shared.partner.PartnerTableData;
import ch.ahoegger.docbox.shared.util.LocalDateUtility;

/**
 * <h3>{@link PartnerService_SearchByName}</h3>
 *
 * @author Andreas Hoegger
 */
public class PartnerService_SearchByName extends AbstractTestWithDatabase {

  private static final BigDecimal partnerId01 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private static final BigDecimal partnerId02 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private static final BigDecimal partnerId03 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private static final BigDecimal partnerId04 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();

  @Override
  public void setupDb() throws Exception {
    super.setupDb();

    ISqlService sqlService = BEANS.get(ISqlService.class);
    LocalDate today = LocalDate.now();

    BEANS.get(PartnerService.class).insert(sqlService, partnerId01, "dook haagen", "some notes",
        LocalDateUtility.toDate(today.minusDays(20)),
        null);

    // till yesterday
    BEANS.get(PartnerService.class).insert(sqlService, partnerId02, "smill donat", "some notes",
        LocalDateUtility.toDate(today.minusDays(10)),
        null);

    // till today
    BEANS.get(PartnerService.class).insert(sqlService, partnerId03, "bluk onack", "some notes",
        LocalDateUtility.toDate(today.minusDays(10)),
        null);

    // till tomorrow
    BEANS.get(PartnerService.class).insert(sqlService, partnerId04, "7 sense of moon", "some notes",
        LocalDateUtility.toDate(today.minusDays(10)),
        null);
  }

  @Test
  public void testFindActive() {
    IPartnerService service = BEANS.get(IPartnerService.class);
    PartnerSearchFormData sd = new PartnerSearchFormData();
    sd.getName().setValue("mill");
    PartnerTableData tableData = service.getTableData(sd);

    Assert.assertEquals(CollectionUtility.arrayList(partnerId02),
        Arrays.stream(tableData.getRows()).map(row -> row.getPartnerId())
            .map(bigDecKey -> bigDecKey)
            .sorted()
            .collect(Collectors.toList()));
  }

  @Test
  public void testFindInactive() {
    IPartnerService service = BEANS.get(IPartnerService.class);
    PartnerSearchFormData sd = new PartnerSearchFormData();
    sd.getName().setValue("7");
    PartnerTableData tableData = service.getTableData(sd);

    Assert.assertEquals(CollectionUtility.arrayList(partnerId04),
        Arrays.stream(tableData.getRows()).map(row -> row.getPartnerId())
            .map(bigDecKey -> bigDecKey)
            .sorted()
            .collect(Collectors.toList()));
  }

  @Test
  public void testFindAll() {
    IPartnerService service = BEANS.get(IPartnerService.class);
    PartnerSearchFormData sd = new PartnerSearchFormData();
    sd.getName().setValue("ona");
    PartnerTableData tableData = service.getTableData(sd);

    Assert.assertEquals(CollectionUtility.arrayList(partnerId02, partnerId03),
        Arrays.stream(tableData.getRows()).map(row -> row.getPartnerId())
            .map(bigDecKey -> bigDecKey)
            .sorted()
            .collect(Collectors.toList()));
  }

}

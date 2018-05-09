package ch.ahoegger.docbox.server.partner;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Calendar;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.junit.Assert;
import org.junit.Test;

import ch.ahoegger.docbox.server.test.util.AbstractTestWithDatabase;
import ch.ahoegger.docbox.server.test.util.DocboxAssert;
import ch.ahoegger.docbox.server.test.util.IdGenerateService;
import ch.ahoegger.docbox.shared.partner.IPartnerService;
import ch.ahoegger.docbox.shared.partner.PartnerFormData;

/**
 * <h3>{@link PartnerServiceTest}</h3>
 *
 * @author Andreas Hoegger
 */

public class PartnerServiceTest extends AbstractTestWithDatabase {

  private static final BigDecimal partnerId01 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private static final BigDecimal partnerId02 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();

  @Override
  protected void execSetupDb(Connection connection) throws Exception {
    Calendar cal = Calendar.getInstance();
    DateUtility.truncCalendar(cal);
    cal.set(1999, 04, 29);
    BEANS.get(PartnerService.class).insert(connection, partnerId01, "patnerName01", "desc01", cal.getTime(), null);
    cal.set(1999, 04, 30);
    BEANS.get(PartnerService.class).insert(connection, partnerId02, "patnerName02", "desc02", cal.getTime(), null);
  }

  @Test
  public void testCreate() {
    IPartnerService service = BEANS.get(IPartnerService.class);

    PartnerFormData fd1 = new PartnerFormData();
    fd1 = service.prepareCreate(fd1);

    fd1.getPartnerBox().getName().setValue("inserted conversation");
    fd1.getPartnerBox().getDescription().setValue("some notes");
    Calendar cal = Calendar.getInstance();
    DateUtility.truncCalendar(cal);
    cal.set(2014, 03, 22);

    fd1.getPartnerBox().getStartDate().setValue(cal.getTime());
    cal.set(2099, 03, 22);
    fd1.getPartnerBox().getEndDate().setValue(cal.getTime());
    fd1 = service.create(fd1);

    PartnerFormData fd2 = new PartnerFormData();
    fd2.setPartnerId(fd1.getPartnerId());
    fd2 = service.load(fd2);

    DocboxAssert.assertEquals(fd1, fd2);
  }

  @Test
  public void testModify() {
    IPartnerService service = BEANS.get(IPartnerService.class);

    PartnerFormData fd1 = new PartnerFormData();
    fd1.setPartnerId(partnerId02);
    fd1 = service.load(fd1);

    fd1.getPartnerBox().getName().setValue("modified.name");
    fd1.getPartnerBox().getDescription().setValue("modified.notes");
    Calendar cal = Calendar.getInstance();
    DateUtility.truncCalendar(cal);
    cal.set(2014, 03, 28);
    fd1.getPartnerBox().getStartDate().setValue(cal.getTime());
    cal.set(2088, 12, 28);
    fd1.getPartnerBox().getEndDate().setValue(cal.getTime());

    fd1 = service.store(fd1);

    PartnerFormData fd2 = new PartnerFormData();
    fd2.setPartnerId(partnerId02);
    fd2 = service.load(fd2);

    DocboxAssert.assertEquals(fd1, fd2);

  }

  @Test
  public void testDelete() {
    IPartnerService service = BEANS.get(IPartnerService.class);
    service.delete(partnerId02);

    PartnerFormData fd1 = new PartnerFormData();
    fd1.setPartnerId(partnerId02);
    fd1 = service.load(fd1);
    Assert.assertNull(fd1);

  }

}

package ch.ahoegger.docbox.server.hr.employee;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Calendar;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.junit.Test;

import ch.ahoegger.docbox.server.database.dev.initialization.PartnerTableTask;
import ch.ahoegger.docbox.server.test.util.AbstractTestWithDatabase;
import ch.ahoegger.docbox.server.test.util.DocboxAssert;
import ch.ahoegger.docbox.server.test.util.IdGenerateService;
import ch.ahoegger.docbox.shared.hr.employee.EmployeeFormData;
import ch.ahoegger.docbox.shared.hr.employee.EmployeeFormData.PartnerGroupBox;
import ch.ahoegger.docbox.shared.hr.employee.IEmployeeService;
import ch.ahoegger.docbox.shared.util.LocalDateUtility;

public class EmployeeServiceTest extends AbstractTestWithDatabase {

  private Long partnerId01 = BEANS.get(IdGenerateService.class).getNextId();

  @Override
  public void setupDb() throws Exception {
    super.setupDb();

    ISqlService sqlService = BEANS.get(ISqlService.class);
    Calendar cal = Calendar.getInstance();
    DateUtility.truncCalendar(cal);
    cal.set(1999, 04, 29);
    BEANS.get(PartnerTableTask.class).createPartnerRow(sqlService, partnerId01, "patnerName01", "desc01", cal.getTime(), null);
  }

  @Test
  public void testCreateWithoutPartner() {

    IEmployeeService service = BEANS.get(IEmployeeService.class);

    EmployeeFormData fd1 = new EmployeeFormData();
    fd1 = service.prepareCreate(fd1);

    fd1.setPartnerId(BigDecimal.valueOf(partnerId01));
    fd1.getAddressLine1().setValue("addressLine01");
    fd1.getAddressLine2().setValue("addressLine02");
    fd1.getAccountNumber().setValue("PC 50-1589-242-2");
    fd1.getAhvNumber().setValue("50-4544 656");
    fd1.getFirstName().setValue("Max");
    fd1.getHourlyWage().setValue(BigDecimal.valueOf(26.75));
    fd1.getLastName().setValue("Beeloq");

    fd1 = service.create(fd1);

    EmployeeFormData fd2 = new EmployeeFormData();
    fd2.setPartnerId(fd1.getPartnerId());
    fd2 = service.load(fd2);

    DocboxAssert.resetFormData(fd2.getPartnerGroupBox(), PartnerGroupBox.class, true);

    DocboxAssert.assertEquals(fd1, fd2);
  }

  @Test
  public void testCreateWithPartner() {

    IEmployeeService service = BEANS.get(IEmployeeService.class);

    EmployeeFormData fd1 = new EmployeeFormData();
    fd1 = service.prepareCreate(fd1);
    fd1.getPartnerGroupBox().getName().setValue("Max Beeloq");
    fd1.getPartnerGroupBox().getDescription().setValue("Desc text");
    fd1.getPartnerGroupBox().getStartDate().setValue(LocalDateUtility.today());
    fd1.getPartnerGroupBox().getEndDate().setValue(LocalDateUtility.toDate(LocalDate.now().plusMonths(3)));

    fd1.getAddressLine1().setValue("addressLine01");
    fd1.getAddressLine2().setValue("addressLine02");
    fd1.getAccountNumber().setValue("PC 50-1589-242-2");
    fd1.getAhvNumber().setValue("50-4544 656");
    fd1.getFirstName().setValue("Max");
    fd1.getHourlyWage().setValue(BigDecimal.valueOf(26.75));
    fd1.getLastName().setValue("Beeloq");

    fd1 = service.create(fd1);

    EmployeeFormData fd2 = new EmployeeFormData();
    fd2.setPartnerId(fd1.getPartnerId());
    fd2 = service.load(fd2);

    DocboxAssert.assertEquals(fd1, fd2);
  }
}

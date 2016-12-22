package ch.ahoegger.docbox.server.hr.employee;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Calendar;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.junit.Assert;
import org.junit.Test;

import ch.ahoegger.docbox.server.database.dev.initialization.EmployeeTableTask;
import ch.ahoegger.docbox.server.database.dev.initialization.PartnerTableTask;
import ch.ahoegger.docbox.server.test.util.AbstractTestWithDatabase;
import ch.ahoegger.docbox.server.test.util.DocboxAssert;
import ch.ahoegger.docbox.server.test.util.IdGenerateService;
import ch.ahoegger.docbox.shared.hr.employee.EmployeeFormData;
import ch.ahoegger.docbox.shared.hr.employee.EmployeeFormData.PartnerGroupBox;
import ch.ahoegger.docbox.shared.hr.employee.IEmployeeService;
import ch.ahoegger.docbox.shared.util.LocalDateUtility;

public class EmployeeServiceTest extends AbstractTestWithDatabase {

  private BigDecimal partnerId01 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private BigDecimal partnerId02_employee = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();

  @Override
  public void setupDb() throws Exception {
    super.setupDb();

    ISqlService sqlService = BEANS.get(ISqlService.class);
    Calendar cal = Calendar.getInstance();
    DateUtility.truncCalendar(cal);
    cal.set(1999, 04, 29);
    BEANS.get(PartnerTableTask.class).createPartnerRow(sqlService, partnerId01, "patnerName01", "desc01", cal.getTime(), null);

    BEANS.get(PartnerTableTask.class).createPartnerRow(sqlService, partnerId02_employee, "employee02", "desc02", LocalDateUtility.toDate(LocalDate.now().minusDays(5)), null);

    BEANS.get(EmployeeTableTask.class).createEmployerRow(sqlService, partnerId02_employee, "Homer", "Simpson", "Nashvill Street 12a", "Santa Barbara CA-90051", "ahv123564789", "iban987654321", 26.30,
        "Master Bob & Minor Molar", "Mountainview 12", "CA-90153 Santa Tropee", "master.bob@blu.com", "5445621236");
  }

  @Test
  public void testLoadEmployee() {
    IEmployeeService service = BEANS.get(IEmployeeService.class);
    EmployeeFormData fd1 = new EmployeeFormData();
    fd1.setPartnerId(partnerId02_employee);
    fd1 = service.load(fd1);

    Assert.assertEquals("Master Bob & Minor Molar", fd1.getEmployerBox().getAddressLine1().getValue());
    Assert.assertEquals("Mountainview 12", fd1.getEmployerBox().getAddressLine2().getValue());
    Assert.assertEquals("CA-90153 Santa Tropee", fd1.getEmployerBox().getAddressLine3().getValue());
    Assert.assertEquals("master.bob@blu.com", fd1.getEmployerBox().getEmail().getValue());
    Assert.assertEquals("5445621236", fd1.getEmployerBox().getPhone().getValue());
  }

  @Test
  public void testCreateWithoutPartner() {

    IEmployeeService service = BEANS.get(IEmployeeService.class);

    EmployeeFormData fd1 = new EmployeeFormData();
    fd1 = service.prepareCreate(fd1);

    fd1.setPartnerId(partnerId01);
    fd1.getEmployeeBox().getAddressLine1().setValue("addressLine01");
    fd1.getEmployeeBox().getAddressLine2().setValue("addressLine02");
    fd1.getEmployeeBox().getAccountNumber().setValue("PC 50-1589-242-2");
    fd1.getEmployeeBox().getAhvNumber().setValue("50-4544 656");
    fd1.getEmployeeBox().getFirstName().setValue("Max");
    fd1.getEmployeeBox().getHourlyWage().setValue(BigDecimal.valueOf(26.75));
    fd1.getEmployeeBox().getLastName().setValue("Beeloq");
    fd1.getEmployerBox().getAddressLine1().setValue("ad line 1");
    fd1.getEmployerBox().getAddressLine2().setValue("ad line 2");
    fd1.getEmployerBox().getAddressLine3().setValue("ad line 3");
    fd1.getEmployerBox().getEmail().setValue("email emp");
    fd1.getEmployerBox().getPhone().setValue("phone emp");

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

    fd1.getEmployeeBox().getAddressLine1().setValue("addressLine01");
    fd1.getEmployeeBox().getAddressLine2().setValue("addressLine02");
    fd1.getEmployeeBox().getAccountNumber().setValue("PC 50-1589-242-2");
    fd1.getEmployeeBox().getAhvNumber().setValue("50-4544 656");
    fd1.getEmployeeBox().getFirstName().setValue("Max");
    fd1.getEmployeeBox().getHourlyWage().setValue(BigDecimal.valueOf(26.75));
    fd1.getEmployeeBox().getLastName().setValue("Beeloq");
    fd1.getEmployerBox().getAddressLine1().setValue("ad line 1");
    fd1.getEmployerBox().getAddressLine2().setValue("ad line 2");
    fd1.getEmployerBox().getAddressLine3().setValue("ad line 3");
    fd1.getEmployerBox().getEmail().setValue("email emp");
    fd1.getEmployerBox().getPhone().setValue("phone emp");

    fd1 = service.create(fd1);

    EmployeeFormData fd2 = new EmployeeFormData();
    fd2.setPartnerId(fd1.getPartnerId());
    fd2 = service.load(fd2);

    DocboxAssert.assertEquals(fd1, fd2);
  }

  @Test
  public void testModify() {
    IEmployeeService service = BEANS.get(IEmployeeService.class);
    EmployeeFormData fd1 = new EmployeeFormData();
    fd1.setPartnerId(partnerId02_employee);
    fd1 = service.load(fd1);

    fd1.getEmployeeBox().getAddressLine1().setValue("addressLine01 mod");
    fd1.getEmployeeBox().getAddressLine2().setValue("addressLine02 mod");
    fd1.getEmployeeBox().getAccountNumber().setValue("Acc mod");
    fd1.getEmployeeBox().getAhvNumber().setValue("50-4544 229");
    fd1.getEmployeeBox().getFirstName().setValue("Albert");
    fd1.getEmployeeBox().getHourlyWage().setValue(BigDecimal.valueOf(25.25));
    fd1.getEmployeeBox().getLastName().setValue("Berok");
    fd1.getEmployerBox().getAddressLine1().setValue("ad line 1 mod");
    fd1.getEmployerBox().getAddressLine2().setValue("ad line 2 mod");
    fd1.getEmployerBox().getAddressLine3().setValue("ad line 3 mod");
    fd1.getEmployerBox().getEmail().setValue("email emp mod");
    fd1.getEmployerBox().getPhone().setValue("phone emp mod");

    fd1 = service.store(fd1);

    EmployeeFormData fd2 = new EmployeeFormData();
    fd2.setPartnerId(partnerId02_employee);
    fd2 = service.load(fd2);

    DocboxAssert.assertEquals(fd1, fd2);
  }

}

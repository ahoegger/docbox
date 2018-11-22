package ch.ahoegger.docbox.server.hr.employee;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.Calendar;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.junit.Assert;
import org.junit.Test;

import ch.ahoegger.docbox.server.hr.AddressFormData;
import ch.ahoegger.docbox.server.hr.AddressService;
import ch.ahoegger.docbox.server.partner.PartnerService;
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
  protected void execSetupDb(Connection connection) throws Exception {
    Calendar cal = Calendar.getInstance();
    DateUtility.truncCalendar(cal);
    cal.set(1999, 04, 29);
    BEANS.get(PartnerService.class).insert(connection, partnerId01, "patnerName01", "desc01", cal.getTime(), null);

    BEANS.get(PartnerService.class).insert(connection, partnerId02_employee, "employee02", "desc02", LocalDateUtility.toDate(LocalDate.now().minusDays(5)), null);

    BigDecimal addressId = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
    BEANS.get(AddressService.class).insert(new AddressFormData().withAddressNr(addressId).withLine1("Nashvill Street 12a").withPlz("CA-90051").withCity("Santa Barbara"));

    BEANS.get(EmployeeService.class).insert(connection, partnerId02_employee, "Homer", "Simpson", addressId, "ahv123564789", "iban987654321",
        LocalDateUtility.toDate(LocalDate.of(1972, 12, 31)), BigDecimal.valueOf(26.30),
        BigDecimal.valueOf(6.225), BigDecimal.valueOf(5.0), BigDecimal.valueOf(8.33),
        EMPLOYER_ID);
  }

  @Test
  public void testLoadEmployee() {
    IEmployeeService service = BEANS.get(IEmployeeService.class);
    EmployeeFormData fd1 = new EmployeeFormData();
    fd1.setPartnerId(partnerId02_employee);
    fd1 = service.load(fd1);

    Assert.assertEquals("iban987654321", fd1.getEmployeeBox().getAccountNumber().getValue());
    Assert.assertEquals("ahv123564789", fd1.getEmployeeBox().getAhvNumber().getValue());
    Assert.assertEquals("Nashvill Street 12a", fd1.getEmployeeBox().getAddressBox().getLine1().getValue());
  }

  @Test
  public void testCreateWithoutPartner() {

    IEmployeeService service = BEANS.get(IEmployeeService.class);

    EmployeeFormData fd1 = new EmployeeFormData();
    fd1 = service.prepareCreate(fd1);
    fd1.getEmployer().setValue(EMPLOYER_ID);
    fd1.setPartnerId(partnerId01);
    fd1.getEmployeeBox().getAccountNumber().setValue("PC 50-1589-242-2");
    fd1.getEmployeeBox().getAhvNumber().setValue("50-4544 656");
    fd1.getEmployeeBox().getFirstName().setValue("Max");
    fd1.getEmploymentBox().getHourlyWage().setValue(BigDecimal.valueOf(26.75));
    fd1.getEmployeeBox().getLastName().setValue("Beeloq");

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
    fd1.getEmployer().setValue(EMPLOYER_ID);
    fd1 = service.prepareCreate(fd1);
    fd1.getPartnerGroupBox().getName().setValue("Max Beeloq");
    fd1.getPartnerGroupBox().getDescription().setValue("Desc text");
    fd1.getPartnerGroupBox().getStartDate().setValue(LocalDateUtility.today());
    fd1.getPartnerGroupBox().getEndDate().setValue(LocalDateUtility.toDate(LocalDate.now().plusMonths(3)));

    fd1.getEmployeeBox().getAccountNumber().setValue("PC 50-1589-242-2");
    fd1.getEmployeeBox().getAhvNumber().setValue("50-4544 656");
    fd1.getEmployeeBox().getFirstName().setValue("Max");
    fd1.getEmploymentBox().getHourlyWage().setValue(BigDecimal.valueOf(26.75));
    fd1.getEmployeeBox().getLastName().setValue("Beeloq");

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

    fd1.getEmployeeBox().getAccountNumber().setValue("Acc mod");
    fd1.getEmployeeBox().getAhvNumber().setValue("50-4544 229");
    fd1.getEmployeeBox().getFirstName().setValue("Albert");
    fd1.getEmploymentBox().getHourlyWage().setValue(BigDecimal.valueOf(25.25));
    fd1.getEmployeeBox().getLastName().setValue("Berok");

    fd1 = service.store(fd1);

    EmployeeFormData fd2 = new EmployeeFormData();
    fd2.setPartnerId(partnerId02_employee);
    fd2 = service.load(fd2);

    DocboxAssert.assertEquals(fd1, fd2);
  }

}

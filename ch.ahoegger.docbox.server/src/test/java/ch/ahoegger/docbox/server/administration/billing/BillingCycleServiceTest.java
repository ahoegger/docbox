package ch.ahoegger.docbox.server.administration.billing;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.ahoegger.docbox.server.administration.hr.billing.BillingCycleService;
import ch.ahoegger.docbox.server.hr.billing.payslip.PayslipService;
import ch.ahoegger.docbox.server.hr.employee.EmployeeTaxGroupService;
import ch.ahoegger.docbox.server.hr.employer.EmployerTaxGroupService;
import ch.ahoegger.docbox.server.test.util.AbstractTestWithDatabase;
import ch.ahoegger.docbox.server.test.util.DocboxAssert;
import ch.ahoegger.docbox.server.test.util.IdGenerateService;
import ch.ahoegger.docbox.server.test.util.TestDataGenerator;
import ch.ahoegger.docbox.shared.administration.hr.billingcycle.BillingCycleFormData;
import ch.ahoegger.docbox.shared.hr.billing.payslip.PayslipSearchFormData;
import ch.ahoegger.docbox.shared.hr.billing.payslip.PayslipTableData.PayslipTableRowData;
import ch.ahoegger.docbox.shared.hr.employee.EmployeeTaxGroupSearchFormData;
import ch.ahoegger.docbox.shared.hr.employee.EmployeeTaxGroupTableData.EmployeeTaxGroupTableRowData;
import ch.ahoegger.docbox.shared.hr.employer.EmployerTaxGroupSearchFormData;
import ch.ahoegger.docbox.shared.hr.employer.EmployerTaxGroupTableData.EmployerTaxGroupTableRowData;
import ch.ahoegger.docbox.shared.hr.entity.EntityTypeCodeType.WorkCode;
import ch.ahoegger.docbox.shared.util.LocalDateUtility;

/**
 * <h3>{@link BillingCycleServiceTest}</h3>
 *
 * @author aho
 */
public class BillingCycleServiceTest extends AbstractTestWithDatabase {

  private static BillingCycleService service;
  private TestDataGenerator m_testDataGenerator;
  private BigDecimal id_taxGroup = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private BigDecimal id_employer2 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private BigDecimal id_employee2 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private BigDecimal id_billingCycle_2005_jun = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();

  @BeforeClass
  public static void initService() {
    service = BEANS.get(BillingCycleService.class);
  }

  @Override
  protected void execSetupDb(Connection connection, TestDataGenerator testDataGenerator) throws Exception {
    m_testDataGenerator = testDataGenerator;
    id_taxGroup = testDataGenerator.nextId();
    id_employer2 = testDataGenerator.nextId();
    id_employee2 = testDataGenerator.nextId();
    testDataGenerator
        .createTaxGroup(id_taxGroup, "2005", LocalDate.of(2005, 01, 01), LocalDate.of(2005, 12, 31))
        .createBillingCycle(id_billingCycle_2005_jun, id_taxGroup, LocalDate.of(2005, 06, 01))
        .createEmployer(id_employer2, "Emp2", "aStreet", "plz", "city", null, null)
        .createEmployerUser(id_employer2, m_testDataGenerator.id_user_bartSimpson_active)
        .createPartner(id_employee2, "EE2", null, LocalDate.of(2000, 01, 01), null)
        .createAnyEmployee(id_employee2, id_employer2);

  }

  @Test(expected = VetoException.class)
  public void testCreateAlreadyExisting() {
    BillingCycleFormData fd1 = new BillingCycleFormData();
    service.prepareCreate(fd1);
    fd1.getTaxGroup().setValue(id_taxGroup);
    fd1.getPeriodBox().getFrom().setValue(LocalDateUtility.toDate(LocalDate.of(2005, 06, 01)));
    fd1.getPeriodBox().getTo().setValue(LocalDateUtility.toDate(LocalDate.of(2005, 06, 30)));
    fd1.getName().setValue("Jun 2005");
    service.create(fd1);
  }

  @Test(expected = VetoException.class)
  public void testCreateEndOverlapping() {
    BillingCycleFormData fd1 = new BillingCycleFormData();
    service.prepareCreate(fd1);
    fd1.getTaxGroup().setValue(id_taxGroup);
    fd1.getPeriodBox().getFrom().setValue(LocalDateUtility.toDate(LocalDate.of(2005, 06, 30)));
    fd1.getPeriodBox().getTo().setValue(LocalDateUtility.toDate(LocalDate.of(2005, 07, 31)));
    fd1.getName().setValue("BitJunJul 2005");
    service.create(fd1);
  }

  @Test(expected = VetoException.class)
  public void testCreateStartOverlapping() {
    BillingCycleFormData fd1 = new BillingCycleFormData();
    service.prepareCreate(fd1);
    fd1.getTaxGroup().setValue(id_taxGroup);
    fd1.getPeriodBox().getFrom().setValue(LocalDateUtility.toDate(LocalDate.of(2005, 05, 15)));
    fd1.getPeriodBox().getTo().setValue(LocalDateUtility.toDate(LocalDate.of(2005, 06, 01)));
    fd1.getName().setValue("test");
    service.create(fd1);
  }

  @Test(expected = VetoException.class)
  public void testCreateIncluding() {
    BillingCycleFormData fd1 = new BillingCycleFormData();
    service.prepareCreate(fd1);
    fd1.getTaxGroup().setValue(id_taxGroup);
    fd1.getPeriodBox().getFrom().setValue(LocalDateUtility.toDate(LocalDate.of(2005, 06, 15)));
    fd1.getPeriodBox().getTo().setValue(LocalDateUtility.toDate(LocalDate.of(2005, 06, 20)));
    fd1.getName().setValue("test");
    service.create(fd1);
  }

  @Test
  public void testCreateBillingCycleWithLinking() {
    BillingCycleFormData fd1 = new BillingCycleFormData();
    service.prepareCreate(fd1);
    fd1.getTaxGroup().setValue(id_taxGroup);
    fd1.getPeriodBox().getFrom().setValue(LocalDateUtility.toDate(LocalDate.of(2005, 01, 01)));
    fd1.getPeriodBox().getTo().setValue(LocalDateUtility.toDate(LocalDate.of(2005, 01, 31)));
    fd1.getName().setValue("Jan 2005");
    fd1.getLinkingBox().getEmployersBox().setValue(CollectionUtility.hashSet(m_testDataGenerator.id_employer_simpsonsInc));
    fd1.getLinkingBox().getEmployeesBox().setValue(CollectionUtility.hashSet(m_testDataGenerator.id_partner_nanny));
    service.create(fd1);

    // expect erTaxGroup created
    EmployerTaxGroupSearchFormData searchData = new EmployerTaxGroupSearchFormData();
    searchData.getTaxGroup().setValue(id_taxGroup);
    searchData.getEmployer().setValue(m_testDataGenerator.id_employer_simpsonsInc);
    List<EmployerTaxGroupTableRowData> resultErTaxGroupSeach = Arrays.asList(BEANS.get(EmployerTaxGroupService.class).getTableData(searchData).getRows());
    Assert.assertEquals(CollectionUtility.arrayList(m_testDataGenerator.id_employer_simpsonsInc),
        resultErTaxGroupSeach
            .stream().map(row -> row.getEmployer())
            .sorted()
            .collect(Collectors.toList()));

    // expect eeTaxGroup created
    EmployeeTaxGroupSearchFormData eeSearchData = new EmployeeTaxGroupSearchFormData();
    eeSearchData.getEmployee().setValue(m_testDataGenerator.id_partner_nanny);
    eeSearchData.setEmployerTaxGroupId(resultErTaxGroupSeach.get(0).getEmployerTaxGroupId());
    List<EmployeeTaxGroupTableRowData> resEeTaxGroupSearch = Arrays.asList(BEANS.get(EmployeeTaxGroupService.class).getTableData(eeSearchData).getRows());
    Assert.assertEquals(CollectionUtility.arrayList(m_testDataGenerator.id_partner_nanny), resEeTaxGroupSearch
        .stream()
        .map(row -> row.getEmployee())
        .sorted()
        .collect(Collectors.toList()));

    // expect billing cycle
    PayslipSearchFormData payslipSearchData = new PayslipSearchFormData();
    payslipSearchData.setEmployeeTaxGroupId(resEeTaxGroupSearch.get(0).getId());
    payslipSearchData.getBillingCycle().setValue(fd1.getBillingCycleId());
    List<PayslipTableRowData> payslipSearchResult = Arrays.asList(BEANS.get(PayslipService.class).getTableData(payslipSearchData).getRows());

    Assert.assertEquals(CollectionUtility.arrayList(m_testDataGenerator.id_partner_nanny), payslipSearchResult
        .stream()
        .map(row -> row.getEmployee())
        .sorted()
        .collect(Collectors.toList()));
  }

  @Test
  public void testModifyWithEntities() {
    m_testDataGenerator
        .createEntity(m_testDataGenerator.nextId(), m_testDataGenerator.id_payslip_nanny_2000_01, WorkCode.ID, LocalDate.of(2000, 01, 12), BigDecimal.valueOf(2), null, "work1")
        .createEntity(m_testDataGenerator.nextId(), m_testDataGenerator.id_payslip_nanny_2000_01, WorkCode.ID, LocalDate.of(2000, 01, 20), BigDecimal.valueOf(4), null, "work2");
    BillingCycleFormData fd1 = new BillingCycleFormData();
    fd1.setBillingCycleId(m_testDataGenerator.id_billingCycle2000_01);
    fd1 = service.load(fd1);
    fd1.getPeriodBox().getFrom().setValue(LocalDateUtility.toDate(LocalDate.of(2000, 01, 12)));
    fd1.getPeriodBox().getTo().setValue(LocalDateUtility.toDate(LocalDate.of(2000, 01, 20)));
    service.store(fd1);

    BillingCycleFormData fd2 = new BillingCycleFormData();
    fd2.setBillingCycleId(m_testDataGenerator.id_billingCycle2000_01);
    fd2 = service.load(fd2);

    DocboxAssert.assertEquals(fd1, fd2);

  }

  @Test(expected = VetoException.class)
  public void testModifyWithEntitiesOutOfPeriod() {
    m_testDataGenerator
        .createEntity(m_testDataGenerator.nextId(), m_testDataGenerator.id_payslip_nanny_2000_01, WorkCode.ID, LocalDate.of(2000, 01, 12), BigDecimal.valueOf(2), null, "work1")
        .createEntity(m_testDataGenerator.nextId(), m_testDataGenerator.id_payslip_nanny_2000_01, WorkCode.ID, LocalDate.of(2000, 01, 20), BigDecimal.valueOf(4), null, "work2");
    BillingCycleFormData fd1 = new BillingCycleFormData();
    fd1.setBillingCycleId(m_testDataGenerator.id_billingCycle2000_01);
    fd1 = service.load(fd1);
    fd1.getPeriodBox().getFrom().setValue(LocalDateUtility.toDate(LocalDate.of(2000, 01, 13)));
    fd1.getPeriodBox().getTo().setValue(LocalDateUtility.toDate(LocalDate.of(2000, 01, 20)));
    service.store(fd1);

  }
}

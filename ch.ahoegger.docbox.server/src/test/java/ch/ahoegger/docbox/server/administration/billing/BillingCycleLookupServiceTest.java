package ch.ahoegger.docbox.server.administration.billing;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.stream.Collectors;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.ahoegger.docbox.server.administration.hr.billing.BillingCycleLookupService;
import ch.ahoegger.docbox.server.test.util.AbstractTestWithDatabase;
import ch.ahoegger.docbox.server.test.util.DocboxAssert;
import ch.ahoegger.docbox.server.test.util.IdGenerateService;
import ch.ahoegger.docbox.server.test.util.TestDataGenerator;
import ch.ahoegger.docbox.shared.administration.hr.billing.BillingCycleLookupCall;

/**
 * <h3>{@link BillingCycleLookupServiceTest}</h3>
 *
 * @author aho
 */
public class BillingCycleLookupServiceTest extends AbstractTestWithDatabase {

  @SuppressWarnings("unused")
  private static BillingCycleLookupService service;
  private TestDataGenerator m_testDataGenerator;

  private BigDecimal id_erTaxGroup_2005 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private BigDecimal id_taxGroup_2005 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private BigDecimal id_billingCycle_jan = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private BigDecimal id_billingCycle_feb = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private BigDecimal id_billingCycle_mar = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private BigDecimal id_eeTaxGroup_2005 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private BigDecimal id_payslip_jan = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private BigDecimal id_taxGroup_2004 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private BigDecimal id_billingCycle_2004_jun = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private BigDecimal id_erTaxGroup_2004 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private BigDecimal id_eeTaxGroup_2004 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();

  @BeforeClass
  public static void initService() {
    service = BEANS.get(BillingCycleLookupService.class);
  }

  @Override
  protected void execSetupDb(Connection connection, TestDataGenerator testDataGenerator) throws Exception {
    m_testDataGenerator = testDataGenerator;

    m_testDataGenerator
        .createTaxGroup(id_taxGroup_2004, "2004", LocalDate.of(2004, 01, 01), LocalDate.of(2004, 12, 31))
        .createEmployerTaxGroup(id_erTaxGroup_2004, m_testDataGenerator.id_employer_simpsonsInc, id_taxGroup_2004, null)
        .createEmployeeTaxGroup(id_eeTaxGroup_2004, id_erTaxGroup_2004, m_testDataGenerator.id_partner_nanny, null, LocalDate.of(2004, 01, 01), LocalDate.of(2004, 12, 31))
        .createBillingCycle(id_billingCycle_2004_jun, id_taxGroup_2004, LocalDate.of(2004, 06, 01))
        .createTaxGroup(id_taxGroup_2005, "abc", LocalDate.of(2005, 01, 01), LocalDate.of(2005, 12, 31))
        .createEmployerTaxGroup(id_erTaxGroup_2005, m_testDataGenerator.id_employer_simpsonsInc, id_taxGroup_2005, null)
        .createEmployeeTaxGroup(id_eeTaxGroup_2005, id_erTaxGroup_2005, m_testDataGenerator.id_partner_nanny, null, LocalDate.of(2005, 01, 01), LocalDate.of(2005, 12, 31))
        .createBillingCycle(id_billingCycle_jan, id_taxGroup_2005, LocalDate.of(2005, 01, 01))
        .createBillingCycle(id_billingCycle_feb, id_taxGroup_2005, LocalDate.of(2005, 02, 01))
        .createBillingCycle(id_billingCycle_mar, id_taxGroup_2005, LocalDate.of(2005, 03, 01))
        .createPayslip(id_payslip_jan, id_billingCycle_jan, id_eeTaxGroup_2005, null);
  }

  @Test
  public void testNotEmployee() {
    BillingCycleLookupCall call = new BillingCycleLookupCall();
    call.setNotEmployeeId(m_testDataGenerator.id_partner_nanny);

    DocboxAssert.assertEqualsSort(CollectionUtility.arrayList(id_billingCycle_2004_jun, id_billingCycle_feb, id_billingCycle_mar), call.getDataByAll()
        .stream()
        .map(row -> row.getKey())
        .collect(Collectors.toList()));
  }

  @Test
  public void testOfTaxGroupAndNotEmployee() {
    BillingCycleLookupCall call = new BillingCycleLookupCall();
    call.setTaxGroupId(id_taxGroup_2005);
    call.setNotEmployeeId(m_testDataGenerator.id_partner_nanny);

    DocboxAssert.assertEqualsSort(CollectionUtility.arrayList(id_billingCycle_feb, id_billingCycle_mar), call.getDataByAll()
        .stream()
        .map(row -> row.getKey())
        .collect(Collectors.toList()));
  }

  @Test
  public void testOfEmployee() {
    BillingCycleLookupCall call = new BillingCycleLookupCall();
    call.setEmployeeId(m_testDataGenerator.id_partner_nanny);

    DocboxAssert.assertEqualsSort(CollectionUtility.arrayList(id_billingCycle_jan, m_testDataGenerator.id_billingCycle2000_01), call.getDataByAll()
        .stream()
        .map(row -> row.getKey())
        .collect(Collectors.toList()));
  }

  @Test
  public void testOfTaxGroupAndEmployee() {
    BillingCycleLookupCall call = new BillingCycleLookupCall();
    call.setTaxGroupId(id_taxGroup_2005);
    call.setEmployeeId(m_testDataGenerator.id_partner_nanny);

    DocboxAssert.assertEqualsSort(CollectionUtility.arrayList(id_billingCycle_jan), call.getDataByAll()
        .stream()
        .map(row -> row.getKey())
        .collect(Collectors.toList()));
  }
}

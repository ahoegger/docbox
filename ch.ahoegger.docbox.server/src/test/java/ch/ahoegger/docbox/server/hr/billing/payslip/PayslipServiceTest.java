package ch.ahoegger.docbox.server.hr.billing.payslip;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDate;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.ahoegger.docbox.server.test.util.AbstractTestWithDatabase;
import ch.ahoegger.docbox.server.test.util.IdGenerateService;
import ch.ahoegger.docbox.server.test.util.TestDataGenerator;
import ch.ahoegger.docbox.shared.hr.billing.payslip.PayslipFormData;
import ch.ahoegger.docbox.shared.hr.entity.EntityTypeCodeType.ExpenseCode;
import ch.ahoegger.docbox.shared.hr.entity.EntityTypeCodeType.WorkCode;

/**
 * <h3>{@link PayslipServiceTest}</h3>
 *
 * @author Andreas Hoegger
 */
public class PayslipServiceTest extends AbstractTestWithDatabase {

  private static PayslipService payslipService;

  private TestDataGenerator m_testDataGenerator;

  private BigDecimal id_payslip_2000_04 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private BigDecimal id_taxGroup_1995 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private BigDecimal id_eeTaxGroupFinalized_1995 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private BigDecimal id_erTaxGroupFinalized_1995 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private BigDecimal id_billingCycle_1995_jan = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private BigDecimal id_billingCycle_2000_apr = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();

  @BeforeClass
  public static void loadService() {
    payslipService = BEANS.get(PayslipService.class);
  }

  @Override
  protected void execSetupDb(Connection connection, TestDataGenerator testDataGenerator) throws Exception {
    m_testDataGenerator = testDataGenerator;
    BigDecimal statementId = m_testDataGenerator.nextId();

    m_testDataGenerator
        .createTaxGroup(id_taxGroup_1995, "1995", LocalDate.of(1995, 01, 01), LocalDate.of(1995, 12, 31))
        .createEmployeeTaxGroup(id_eeTaxGroupFinalized_1995, id_taxGroup_1995, m_testDataGenerator.id_partner_nanny, m_testDataGenerator.id_statement_any, LocalDate.of(1995, 01, 01), LocalDate.of(1995, 12, 31))
        .createEmployerTaxGroup(id_erTaxGroupFinalized_1995, m_testDataGenerator.id_employer_simpsonsInc, id_taxGroup_1995, m_testDataGenerator.id_statement_any)
        .createBillingCycle(id_billingCycle_1995_jan, id_taxGroup_1995, LocalDate.of(1995, 01, 01))
        .createBillingCycle(id_billingCycle_2000_apr, m_testDataGenerator.id_taxGroup2000, LocalDate.of(2000, 04, 01))
        .createAnyStatement(statementId)
        .createPayslip(id_payslip_2000_04, id_billingCycle_2000_apr, m_testDataGenerator.id_employeeTaxGroup_nanny_2000, statementId);
  }

  @Test
  public void testFinalize() {
    m_testDataGenerator.createEntity(m_testDataGenerator.nextId(), m_testDataGenerator.id_payslip_nanny_2000_01, WorkCode.ID, LocalDate.of(2000, 01, 02), BigDecimal.valueOf(2), null, null)
        .createEntity(m_testDataGenerator.nextId(), m_testDataGenerator.id_payslip_nanny_2000_01, ExpenseCode.ID, LocalDate.of(2000, 01, 02), null, BigDecimal.valueOf(22.5), null);

    PayslipFormData fd1 = new PayslipFormData();
    fd1.setPayslipId(m_testDataGenerator.id_payslip_nanny_2000_01);
    payslipService.load(fd1);
    fd1.getPayslipDocumentAbstract().setValue("Test abstract");
    payslipService.finalize(fd1);

    Assert.assertNotNull(fd1.getStatementId());
  }

  @Test(expected = VetoException.class)
  public void testFinalizeFinalized() {
    PayslipFormData fd1 = new PayslipFormData();
    fd1.setPayslipId(id_payslip_2000_04);
    payslipService.load(fd1);
    payslipService.finalize(fd1);
  }

  @Test(expected = VetoException.class)
  public void testCreateForFinalizedTaxGroup() {
    PayslipFormData fd1 = new PayslipFormData();
    fd1.setEmployeeTaxGroupId(id_eeTaxGroupFinalized_1995);
    fd1.getBillingCycle().setValue(id_billingCycle_1995_jan);
    fd1 = payslipService.prepareCreate(fd1);
    payslipService.create(fd1);
  }

//  @Override
//  protected void execSetupDb(Connection connection) throws Exception {
//
//    // create document
//    Date docCaptureDate = LocalDateUtility.toDate(LocalDate.now().minusDays(5));
//    String documentPath = BEANS.get(DocumentStoreService.class).store(new BinaryResource("payslip.pdf", "content".getBytes()), docCaptureDate, documentId);
//
//    BEANS.get(PartnerService.class).insert(connection, partnerId, "employee01", "desc01", docCaptureDate, null);
//    BigDecimal addressId = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
//    BEANS.get(AddressService.class).insert(new AddressFormData().withAddressNr(addressId).withLine1("Nashvill Street 12a").withPlz("CA-90051").withCity("Santa Barbara"));
//    BEANS.get(EmployeeService.class).insert(connection, partnerId, "Homer", "Simpson", addressId, "ahv123564789", "iban987654321", SourceTax.ID,
//        LocalDateUtility.toDate(LocalDate.of(1993, 02, 15)), BigDecimal.valueOf(26.30), BigDecimal.valueOf(6.225), BigDecimal.valueOf(5.0), BigDecimal.valueOf(8.33), EMPLOYER_ID);
//
//    BEANS.get(DocumentService.class).insert(connection, documentId, "Abstract", docCaptureDate, LocalDateUtility.toDate(LocalDate.now().minusDays(4)), null, documentPath, null, null, true, OcrLanguageCodeType.GermanCode.ID);
//
//    BEANS.get(DocumentPartnerService.class).insert(connection, documentId, partnerId);
//
//    BEANS.get(PayslipService.class).insert(connection, payslipGroupId, partnerId, EMPLOYER_ID, null, documentId, BigDecimal.valueOf(-1), "Dezember ",
//        LocalDateUtility.toDate(LocalDate.of(2016, 12, 1)),
//        LocalDateUtility.toDate(LocalDate.of(2016, 12, 31)),
//        LocalDateUtility.toDate(LocalDate.of(2017, 1, 5)),
//        BigDecimal.valueOf(5.0), BigDecimal.valueOf(200.3),
//        BigDecimal.valueOf(197.3),
//        BigDecimal.valueOf(10.3), BigDecimal.valueOf(1.3), BigDecimal.valueOf(2.3));
//    BEANS.get(EntityService.class).insert(connection, expenceId, partnerId, payslipGroupId, ExpenseCode.ID, LocalDateUtility.toDate(LocalDate.now().minusDays(2)), null, BigDecimal.valueOf(23), "desc");
//    BEANS.get(EntityService.class).insert(connection, workId, partnerId, payslipGroupId, WorkCode.ID, LocalDateUtility.toDate(LocalDate.now().minusDays(2)), BigDecimal.valueOf(3), null, "desc");
//
//  }
//
//  @Test
//  public void testDelete() {
//    DocumentFormData dfd = new DocumentFormData();
//    dfd.setDocumentId(documentId);
//    Assert.assertNotNull(BEANS.get(DocumentService.class).load(dfd));
//    DocumentFormData docData = new DocumentFormData();
//    docData.setDocumentId(documentId);
//    docData = BEANS.get(DocumentService.class).load(docData);
//
//    BEANS.get(PayslipService.class).delete(payslipGroupId);
//
//    Assert.assertFalse(BEANS.get(DocumentStoreService.class).exists(docData.getDocumentPath()));
//    // unbilled group still exists
//    Assert.assertEquals(0, BEANS.get(PayslipService.class).getTableData(new PayslipSearchFormData()).getRowCount());
//    Assert.assertEquals(0, BEANS.get(DocumentService.class).getTableData(new DocumentSearchFormData()).getRowCount());
//    Assert.assertEquals(0, BEANS.get(DocumentPartnerService.class).getPartnerIds(documentId).size());
//    EntitySearchFormData sd = new EntitySearchFormData();
//    sd.setPayslipId(payslipGroupId);
//    Assert.assertEquals(0, BEANS.get(EntityService.class).getEntityTableData(sd).getRowCount());
//    sd.setPayslipId(UnbilledCode.ID);
//    Assert.assertEquals(2, BEANS.get(EntityService.class).getEntityTableData(sd).getRowCount());
//    Assert.assertNull(BEANS.get(DocumentService.class).load(dfd));
//
//  }
}

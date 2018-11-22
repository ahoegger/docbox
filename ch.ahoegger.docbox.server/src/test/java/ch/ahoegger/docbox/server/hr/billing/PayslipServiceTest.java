package ch.ahoegger.docbox.server.hr.billing;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.Date;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.resource.BinaryResource;
import org.junit.Assert;
import org.junit.Test;

import ch.ahoegger.docbox.server.document.DocumentPartnerService;
import ch.ahoegger.docbox.server.document.DocumentService;
import ch.ahoegger.docbox.server.document.store.DocumentStoreService;
import ch.ahoegger.docbox.server.hr.AddressFormData;
import ch.ahoegger.docbox.server.hr.AddressService;
import ch.ahoegger.docbox.server.hr.employee.EmployeeService;
import ch.ahoegger.docbox.server.hr.entity.EntityService;
import ch.ahoegger.docbox.server.partner.PartnerService;
import ch.ahoegger.docbox.server.test.util.AbstractTestWithDatabase;
import ch.ahoegger.docbox.server.test.util.IdGenerateService;
import ch.ahoegger.docbox.shared.document.DocumentFormData;
import ch.ahoegger.docbox.shared.document.DocumentSearchFormData;
import ch.ahoegger.docbox.shared.hr.billing.PayslipCodeType.UnbilledCode;
import ch.ahoegger.docbox.shared.hr.billing.PayslipSearchFormData;
import ch.ahoegger.docbox.shared.hr.entity.EntitySearchFormData;
import ch.ahoegger.docbox.shared.hr.entity.EntityTypeCodeType.ExpenseCode;
import ch.ahoegger.docbox.shared.hr.entity.EntityTypeCodeType.WorkCode;
import ch.ahoegger.docbox.shared.hr.tax.TaxCodeType.SourceTax;
import ch.ahoegger.docbox.shared.ocr.OcrLanguageCodeType;
import ch.ahoegger.docbox.shared.util.LocalDateUtility;

/**
 * <h3>{@link PayslipServiceTest}</h3>
 *
 * @author Andreas Hoegger
 */
public class PayslipServiceTest extends AbstractTestWithDatabase {

  private BigDecimal partnerId = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private BigDecimal documentId = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private BigDecimal payslipGroupId = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private BigDecimal expenceId = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private BigDecimal workId = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();

  @Override
  protected void execSetupDb(Connection connection) throws Exception {

    // create document
    Date docCaptureDate = LocalDateUtility.toDate(LocalDate.now().minusDays(5));
    String documentPath = BEANS.get(DocumentStoreService.class).store(new BinaryResource("payslip.pdf", "content".getBytes()), docCaptureDate, documentId);

    BEANS.get(PartnerService.class).insert(connection, partnerId, "employee01", "desc01", docCaptureDate, null);
    BigDecimal addressId = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
    BEANS.get(AddressService.class).insert(new AddressFormData().withAddressNr(addressId).withLine1("Nashvill Street 12a").withPlz("CA-90051").withCity("Santa Barbara"));
    BEANS.get(EmployeeService.class).insert(connection, partnerId, "Homer", "Simpson", addressId, "ahv123564789", "iban987654321", SourceTax.ID,
        LocalDateUtility.toDate(LocalDate.of(1993, 02, 15)), BigDecimal.valueOf(26.30), BigDecimal.valueOf(6.225), BigDecimal.valueOf(5.0), BigDecimal.valueOf(8.33), EMPLOYER_ID);

    BEANS.get(DocumentService.class).insert(connection, documentId, "Abstract", docCaptureDate, LocalDateUtility.toDate(LocalDate.now().minusDays(4)), null, documentPath, null, null, true, OcrLanguageCodeType.GermanCode.ID);

    BEANS.get(DocumentPartnerService.class).insert(connection, documentId, partnerId);

    BEANS.get(PayslipService.class).insert(connection, payslipGroupId, partnerId, null, documentId, "Dezember ",
        LocalDateUtility.toDate(LocalDate.of(2016, 12, 1)),
        LocalDateUtility.toDate(LocalDate.of(2016, 12, 31)),
        LocalDateUtility.toDate(LocalDate.of(2017, 1, 5)),
        BigDecimal.valueOf(5.0), BigDecimal.valueOf(200.3),
        BigDecimal.valueOf(197.3),
        BigDecimal.valueOf(10.3), BigDecimal.valueOf(1.3), BigDecimal.valueOf(2.3));
    BEANS.get(EntityService.class).insert(connection, expenceId, partnerId, payslipGroupId, ExpenseCode.ID, LocalDateUtility.toDate(LocalDate.now().minusDays(2)), null, BigDecimal.valueOf(23), "desc");
    BEANS.get(EntityService.class).insert(connection, workId, partnerId, payslipGroupId, WorkCode.ID, LocalDateUtility.toDate(LocalDate.now().minusDays(2)), BigDecimal.valueOf(3), null, "desc");

  }

  @Test
  public void testDelete() {
    DocumentFormData dfd = new DocumentFormData();
    dfd.setDocumentId(documentId);
    Assert.assertNotNull(BEANS.get(DocumentService.class).load(dfd));
    DocumentFormData docData = new DocumentFormData();
    docData.setDocumentId(documentId);
    docData = BEANS.get(DocumentService.class).load(docData);

    BEANS.get(PayslipService.class).delete(payslipGroupId);

    Assert.assertFalse(BEANS.get(DocumentStoreService.class).exists(docData.getDocumentPath()));
    // unbilled group still exists
    Assert.assertEquals(0, BEANS.get(PayslipService.class).getTableData(new PayslipSearchFormData()).getRowCount());
    Assert.assertEquals(0, BEANS.get(DocumentService.class).getTableData(new DocumentSearchFormData()).getRowCount());
    Assert.assertEquals(0, BEANS.get(DocumentPartnerService.class).getPartnerIds(documentId).size());
    EntitySearchFormData sd = new EntitySearchFormData();
    sd.setPayslipId(payslipGroupId);
    Assert.assertEquals(0, BEANS.get(EntityService.class).getEntityTableData(sd).getRowCount());
    sd.setPayslipId(UnbilledCode.ID);
    Assert.assertEquals(2, BEANS.get(EntityService.class).getEntityTableData(sd).getRowCount());
    Assert.assertNull(BEANS.get(DocumentService.class).load(dfd));

  }
}

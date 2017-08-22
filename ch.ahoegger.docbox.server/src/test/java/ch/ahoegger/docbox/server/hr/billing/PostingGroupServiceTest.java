package ch.ahoegger.docbox.server.hr.billing;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.resource.BinaryResource;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.junit.Assert;
import org.junit.Test;

import ch.ahoegger.docbox.server.document.DocumentPartnerService;
import ch.ahoegger.docbox.server.document.DocumentService;
import ch.ahoegger.docbox.server.document.store.DocumentStoreService;
import ch.ahoegger.docbox.server.hr.employee.EmployeeService;
import ch.ahoegger.docbox.server.hr.entity.EntityService;
import ch.ahoegger.docbox.server.partner.PartnerService;
import ch.ahoegger.docbox.server.test.util.AbstractTestWithDatabase;
import ch.ahoegger.docbox.server.test.util.IdGenerateService;
import ch.ahoegger.docbox.shared.document.DocumentFormData;
import ch.ahoegger.docbox.shared.document.DocumentSearchFormData;
import ch.ahoegger.docbox.shared.hr.billing.PostingGroupCodeType.UnbilledCode;
import ch.ahoegger.docbox.shared.hr.billing.PostingGroupSearchFormData;
import ch.ahoegger.docbox.shared.hr.entity.EntitySearchFormData;
import ch.ahoegger.docbox.shared.hr.entity.EntityTypeCodeType.ExpenseCode;
import ch.ahoegger.docbox.shared.hr.entity.EntityTypeCodeType.WorkCode;
import ch.ahoegger.docbox.shared.ocr.OcrLanguageCodeType;
import ch.ahoegger.docbox.shared.util.LocalDateUtility;

/**
 * <h3>{@link PostingGroupServiceTest}</h3>
 *
 * @author aho
 */
public class PostingGroupServiceTest extends AbstractTestWithDatabase {

  private BigDecimal partnerId = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private BigDecimal documentId = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private BigDecimal postingGroupId = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private BigDecimal expenceId = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private BigDecimal workId = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();

  @Override
  public void setupDb() throws Exception {
    super.setupDb();

    // create document
    Date docCaptureDate = LocalDateUtility.toDate(LocalDate.now().minusDays(5));
    String documentPath = BEANS.get(DocumentStoreService.class).store(new BinaryResource("payslip.pdf", "content".getBytes()), docCaptureDate, documentId);
    ISqlService sqlService = BEANS.get(ISqlService.class);

    BEANS.get(PartnerService.class).insert(sqlService.getConnection(), partnerId, "employee01", "desc01", docCaptureDate, null);
    BEANS.get(EmployeeService.class).insert(sqlService.getConnection(), partnerId, "Homer", "Simpson", "Nashvill Street 12a", "Santa Barbara CA-90051", "ahv123564789", "iban987654321", BigDecimal.valueOf(26.30),
        BigDecimal.valueOf(6.225), BigDecimal.valueOf(5.0), BigDecimal.valueOf(8.33),
        "Master Bob & Minor Molar", "Mountainview 12", "CA-90153 Santa Tropee", "master.bob@blu.com", "5445621236");

    BEANS.get(DocumentService.class).insert(sqlService.getConnection(), documentId, "Abstract", docCaptureDate, LocalDateUtility.toDate(LocalDate.now().minusDays(4)), null, documentPath, null, null, true, OcrLanguageCodeType.GermanCode.ID);

    BEANS.get(DocumentPartnerService.class).insert(sqlService.getConnection(), documentId, partnerId);

    BEANS.get(PostingGroupService.class).insert(sqlService.getConnection(), postingGroupId, partnerId, null, documentId, "Dez", LocalDateUtility.toDate(LocalDate.now()), BigDecimal.valueOf(5.0), BigDecimal.valueOf(200.3),
        BigDecimal.valueOf(197.3),
        BigDecimal.valueOf(10.3), BigDecimal.valueOf(1.3), BigDecimal.valueOf(2.3));
    BEANS.get(EntityService.class).insert(sqlService.getConnection(), expenceId, partnerId, postingGroupId, ExpenseCode.ID, LocalDateUtility.toDate(LocalDate.now().minusDays(2)), null, BigDecimal.valueOf(23), "desc");
    BEANS.get(EntityService.class).insert(sqlService.getConnection(), workId, partnerId, postingGroupId, WorkCode.ID, LocalDateUtility.toDate(LocalDate.now().minusDays(2)), BigDecimal.valueOf(3), null, "desc");

  }

  @Test
  public void testDelete() {
    DocumentFormData dfd = new DocumentFormData();
    dfd.setDocumentId(documentId);
    Assert.assertNotNull(BEANS.get(DocumentService.class).load(dfd));
    DocumentFormData docData = new DocumentFormData();
    docData.setDocumentId(documentId);
    docData = BEANS.get(DocumentService.class).load(docData);

    BEANS.get(PostingGroupService.class).delete(postingGroupId);

    Assert.assertFalse(BEANS.get(DocumentStoreService.class).exists(docData.getDocumentPath()));
    // unbilled group still exists
    Assert.assertEquals(0, BEANS.get(PostingGroupService.class).getTableData(new PostingGroupSearchFormData()).getRowCount());
    Assert.assertEquals(0, BEANS.get(DocumentService.class).getTableData(new DocumentSearchFormData()).getRowCount());
    Assert.assertEquals(0, BEANS.get(DocumentPartnerService.class).getPartnerIds(documentId).size());
    EntitySearchFormData sd = new EntitySearchFormData();
    sd.setPostingGroupId(postingGroupId);
    Assert.assertEquals(0, BEANS.get(EntityService.class).getEntityTableData(sd).getRowCount());
    sd.setPostingGroupId(UnbilledCode.ID);
    Assert.assertEquals(2, BEANS.get(EntityService.class).getEntityTableData(sd).getRowCount());
    Assert.assertNull(BEANS.get(DocumentService.class).load(dfd));

  }
}

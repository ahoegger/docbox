package ch.ahoegger.docbox.server.hr.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.junit.Assert;
import org.junit.Test;

import ch.ahoegger.docbox.server.document.DocumentService;
import ch.ahoegger.docbox.server.hr.billing.PostingGroupService;
import ch.ahoegger.docbox.server.partner.PartnerService;
import ch.ahoegger.docbox.server.test.util.AbstractTestWithDatabase;
import ch.ahoegger.docbox.server.test.util.DocboxAssert;
import ch.ahoegger.docbox.server.test.util.IdGenerateService;
import ch.ahoegger.docbox.shared.hr.billing.PostingGroupCodeType.UnbilledCode;
import ch.ahoegger.docbox.shared.hr.entity.EntityFormData;
import ch.ahoegger.docbox.shared.hr.entity.EntitySearchFormData;
import ch.ahoegger.docbox.shared.hr.entity.EntityTablePageData;
import ch.ahoegger.docbox.shared.hr.entity.EntityTypeCodeType;
import ch.ahoegger.docbox.shared.hr.entity.EntityTypeCodeType.ExpenseCode;
import ch.ahoegger.docbox.shared.hr.entity.IEntityService;
import ch.ahoegger.docbox.shared.ocr.OcrLanguageCodeType;
import ch.ahoegger.docbox.shared.util.LocalDateUtility;

public class EntityServiceTest extends AbstractTestWithDatabase {

  private BigDecimal partnerId01 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private BigDecimal documentId01 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private BigDecimal postingGroupId01 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private BigDecimal entityId01 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private BigDecimal entityId02 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private BigDecimal entityId03 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();

  @Override
  public void setupDb() throws Exception {
    super.setupDb();

    ISqlService sqlService = BEANS.get(ISqlService.class);
    BEANS.get(PartnerService.class).insert(sqlService.getConnection(), partnerId01, "patnerName01", "desc01", LocalDateUtility.today(), null);

    BEANS.get(DocumentService.class).insert(sqlService.getConnection(), documentId01, "All fish are wet", LocalDateUtility.toDate(LocalDate.now().minusDays(3)), LocalDateUtility.today(), null, "2016_03_08_124640.pdf", null, null, false,
        OcrLanguageCodeType.GermanCode.ID);

    BEANS.get(PostingGroupService.class).insert(sqlService.getConnection(), postingGroupId01, partnerId01, UnbilledCode.ID, documentId01, "August 2016",
        LocalDateUtility.toDate(LocalDate.of(2016, 8, 1)),
        LocalDateUtility.toDate(LocalDate.of(2016, 8, 31)),
        LocalDateUtility.toDate(LocalDate.of(2016, 9, 2)),
        BigDecimal.valueOf(234.9), BigDecimal.valueOf(10.5),
        BigDecimal.valueOf(232.1),
        BigDecimal.valueOf(-10.0),
        BigDecimal.valueOf(-4.5), BigDecimal.valueOf(5.30));

    BEANS.get(EntityService.class).insert(sqlService.getConnection(), entityId01, partnerId01, postingGroupId01, EntityTypeCodeType.WorkCode.ID, LocalDateUtility.today(), BigDecimal.valueOf(3.25), null, "Work01");

    BEANS.get(EntityService.class).insert(sqlService.getConnection(), entityId02, partnerId01, UnbilledCode.ID, EntityTypeCodeType.WorkCode.ID, LocalDateUtility.today(), BigDecimal.valueOf(3.25), null, "Work01");
    BEANS.get(EntityService.class).insert(sqlService.getConnection(), entityId03, partnerId01, UnbilledCode.ID, EntityTypeCodeType.ExpenseCode.ID, LocalDateUtility.today(), null, BigDecimal.valueOf(3.25), "Expense01");
  }

  @Test
  public void testUnbiled() {
    EntitySearchFormData searchFd = new EntitySearchFormData();
    searchFd.getPartnerId().setValue(partnerId01);
    searchFd.setPostingGroupId(UnbilledCode.ID);
    EntityTablePageData entityTableData = BEANS.get(IEntityService.class).getEntityTableData(searchFd);
    Assert.assertEquals(2, entityTableData.getRowCount());
  }

  @Test
  public void testCreate() {
    IEntityService service = BEANS.get(IEntityService.class);

    EntityFormData fd1 = new EntityFormData();
    fd1 = service.prepareCreate(fd1);

    fd1.getExpenseAmount().setValue(BigDecimal.valueOf(10).setScale(2));
    fd1.getText().setValue("a desc");
    fd1.getEntityDate().setValue(LocalDateUtility.today());
    fd1.setEntityType(ExpenseCode.ID);
    fd1.getWorkHours().setValue(BigDecimal.valueOf(2.50).setScale(2));
    fd1.setPartnerId(BigDecimal.valueOf(2222));
    fd1.setPostingGroupId(UnbilledCode.ID);

    fd1 = service.create(fd1);

    EntityFormData fd2 = new EntityFormData();
    fd2.setEntityId(fd1.getEntityId());
    fd2 = service.load(fd2);

    DocboxAssert.assertEquals(fd1, fd2);
  }

  @Test
  public void testModifyWork() {
    IEntityService service = BEANS.get(IEntityService.class);

    EntityFormData fd = new EntityFormData();
    fd.setEntityId(entityId02);
    fd = service.load(fd);

    fd.getWorkHours().setValue(BigDecimal.valueOf(5).setScale(2));
    fd.getText().setValue("Modified text");
    fd.getEntityDate().setValue(LocalDateUtility.toDate(LocalDate.now().minusDays(2)));

    service.store(fd);

    EntityFormData dbRef = new EntityFormData();
    dbRef.setEntityId(entityId02);
    dbRef = service.load(dbRef);

    DocboxAssert.assertEquals(fd, dbRef);
  }

  @Test(expected = VetoException.class)
  public void testModifyWorkPartnerId() {
    IEntityService service = BEANS.get(IEntityService.class);

    EntityFormData fd = new EntityFormData();
    fd.setEntityId(entityId02);
    fd = service.load(fd);

    fd.setPartnerId(BigDecimal.valueOf(22222));

    service.store(fd);

    EntityFormData dbRef = new EntityFormData();
    dbRef.setEntityId(entityId02);
    dbRef = service.load(dbRef);

    DocboxAssert.assertEquals(fd, dbRef);
  }

  @Test(expected = VetoException.class)
  public void testModifyWorkPostingGroup() {
    IEntityService service = BEANS.get(IEntityService.class);

    EntityFormData fd = new EntityFormData();
    fd.setEntityId(entityId02);
    fd = service.load(fd);

    fd.setPostingGroupId(BigDecimal.valueOf(22222));

    service.store(fd);

    EntityFormData dbRef = new EntityFormData();
    dbRef.setEntityId(entityId02);
    dbRef = service.load(dbRef);

    DocboxAssert.assertEquals(fd, dbRef);
  }

  @Test
  public void testModifyExpense() {
    IEntityService service = BEANS.get(IEntityService.class);

    EntityFormData fd = new EntityFormData();
    fd.setEntityId(entityId03);
    fd = service.load(fd);

    fd.getExpenseAmount().setValue(BigDecimal.valueOf(33).setScale(2));
    fd.getText().setValue("Modified text");
    fd.getEntityDate().setValue(LocalDateUtility.toDate(LocalDate.now().minusDays(2)));

    service.store(fd);

    EntityFormData dbRef = new EntityFormData();
    dbRef.setEntityId(entityId03);
    dbRef = service.load(dbRef);

    DocboxAssert.assertEquals(fd, dbRef);
  }
}

package ch.ahoegger.docbox.server.hr.entity;

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDate;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.ahoegger.docbox.server.test.util.AbstractTestWithDatabase;
import ch.ahoegger.docbox.server.test.util.DocboxAssert;
import ch.ahoegger.docbox.server.test.util.TestDataGenerator;
import ch.ahoegger.docbox.shared.hr.entity.EntityFormData;
import ch.ahoegger.docbox.shared.hr.entity.EntityTypeCodeType.ExpenseCode;
import ch.ahoegger.docbox.shared.hr.entity.EntityTypeCodeType.WorkCode;
import ch.ahoegger.docbox.shared.util.LocalDateUtility;

public class EntityServiceTest extends AbstractTestWithDatabase {

  private static EntityService entityService;

  private TestDataGenerator m_testDataGenerator;
  private BigDecimal id_payslip_2000_04_finalized;

  private BigDecimal id_entity_finalized;
  private BigDecimal id_entity_01;

  @BeforeClass
  public static void loadService() {
    entityService = BEANS.get(EntityService.class);
  }

  @Override
  protected void execSetupDb(Connection connection, TestDataGenerator testDataGenerator) throws Exception {
    m_testDataGenerator = testDataGenerator;
    id_payslip_2000_04_finalized = m_testDataGenerator.nextId();
    id_entity_finalized = m_testDataGenerator.nextId();
    id_entity_01 = m_testDataGenerator.nextId();
    BigDecimal billingCycleId = m_testDataGenerator.nextId(),
        statementId = m_testDataGenerator.nextId();

    m_testDataGenerator
        .createBillingCycle(billingCycleId, m_testDataGenerator.id_taxGroup2000, LocalDate.of(2000, 04, 01))
        .createAnyStatement(statementId)
        .createPayslip(id_payslip_2000_04_finalized, billingCycleId, m_testDataGenerator.id_employeeTaxGroup_nanny_2000, statementId)
        .createEntity(id_entity_finalized, id_payslip_2000_04_finalized, WorkCode.ID, LocalDate.of(2000, 4, 22), BigDecimal.valueOf(5.9), null, "work to finalized")
        .createEntity(id_entity_01, m_testDataGenerator.id_payslip_nanny_2000_01, WorkCode.ID, LocalDate.of(2000, 1, 22), BigDecimal.valueOf(5.9), null, "work item");
  }

  @Test
  public void testCreateWork() {
    EntityFormData fd1 = new EntityFormData();
    fd1.setPayslipId(m_testDataGenerator.id_payslip_nanny_2000_01);
    fd1.setEntityType(WorkCode.ID);
    fd1.getEntityDate().setValue(LocalDateUtility.toDate(LocalDate.of(2000, 01, 02)));
    fd1.getText().setValue("Work item1");
    fd1.getWorkHours().setValue(BigDecimal.valueOf(9.5));
    fd1 = entityService.create(fd1);
    assertNotNull(fd1.getEntityId());

    EntityFormData fd2 = new EntityFormData();
    fd2.setEntityId(fd1.getEntityId());
    fd2 = entityService.load(fd2);

    DocboxAssert.assertEquals(fd1, fd2);
  }

  @Test
  public void testCreateExpense() {
    EntityFormData fd1 = new EntityFormData();
    fd1.setPayslipId(m_testDataGenerator.id_payslip_nanny_2000_01);
    fd1.setEntityType(ExpenseCode.ID);
    fd1.getEntityDate().setValue(LocalDateUtility.toDate(LocalDate.of(2000, 01, 02)));
    fd1.getText().setValue("Expense item1");
    fd1.getExpenseAmount().setValue(BigDecimal.valueOf(9.5));
    fd1 = entityService.create(fd1);
    assertNotNull(fd1.getEntityId());

    EntityFormData fd2 = new EntityFormData();
    fd2.setEntityId(fd1.getEntityId());
    fd2 = entityService.load(fd2);

    DocboxAssert.assertEquals(fd1, fd2);
  }

  @Test
  public void testModify() {
    EntityFormData fd1 = new EntityFormData();
    fd1.setEntityId(id_entity_01);
    entityService.load(fd1);
    fd1.getWorkHours().setValue(BigDecimal.valueOf(2));
    entityService.store(fd1);

    EntityFormData fd2 = new EntityFormData();
    fd2.setEntityId(fd1.getEntityId());
    entityService.load(fd2);

    DocboxAssert.assertEquals(fd1, fd2);
  }

  @Test(expected = VetoException.class)
  public void testAddEntryToFinalizedPayslip() {
    EntityFormData fd1 = new EntityFormData();
    fd1.setPayslipId(id_payslip_2000_04_finalized);
    fd1.setEntityType(WorkCode.ID);
    fd1.getEntityDate().setValue(LocalDateUtility.toDate(LocalDate.of(2000, 01, 02)));
    fd1.getText().setValue("Work item1");
    fd1.getWorkHours().setValue(BigDecimal.valueOf(9.5));
    fd1 = entityService.create(fd1);
  }

  @Test(expected = VetoException.class)
  public void testEditFinalizedEntity() {
    EntityFormData fd1 = new EntityFormData();
    fd1.setPayslipId(id_payslip_2000_04_finalized);
    entityService.load(fd1);
    fd1.getWorkHours().setValue(BigDecimal.valueOf(2));
    entityService.store(fd1);
  }
}

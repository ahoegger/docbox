package ch.ahoegger.docbox.server.hr;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDate;

import org.eclipse.scout.rt.platform.BEANS;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.ahoegger.docbox.server.hr.statement.StatementBean;
import ch.ahoegger.docbox.server.hr.statement.StatementService;
import ch.ahoegger.docbox.server.test.util.AbstractTestWithDatabase;
import ch.ahoegger.docbox.server.test.util.DocboxAssert;
import ch.ahoegger.docbox.server.test.util.IdGenerateService;
import ch.ahoegger.docbox.server.test.util.TestBackupService;
import ch.ahoegger.docbox.server.test.util.TestDataGenerator;
import ch.ahoegger.docbox.shared.util.LocalDateUtility;

/**
 * <h3>{@link StatementServiceTest}</h3>
 *
 * @author aho
 */
public class StatementServiceTest extends AbstractTestWithDatabase {

  private static StatementService service;

  private TestDataGenerator m_testDataGenerator;

  private BigDecimal id_document = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();

  @BeforeClass
  public static void initService() {
    service = BEANS.get(StatementService.class);
  }

  @Override
  protected void execSetupDb(Connection connection, TestDataGenerator testDataGenerator) throws Exception {
    m_testDataGenerator = testDataGenerator;

    m_testDataGenerator
        .newDocumentBean(id_document)
        .anyContent()
        .withAbstractText("All fish are wet")
        .withDocPath("2016_03_08_124640.pdf")
        .create()
        .newDocumentOcrBean(id_document)
        .create();
  }

  @Test
  public void testCreateStatement() {
    StatementBean bean1 = new StatementBean()
        .withAccountNumber("abc")
        .withBruttoWage(BigDecimal.valueOf(256))
        .withDocumentId(id_document)
        .withExpenses(BigDecimal.valueOf(20.55))
        .withHourlyWage(BigDecimal.valueOf(21.35))
        .withNettoWage(BigDecimal.valueOf(254))
        .withSocialInsuranceRate(BigDecimal.valueOf(6.554))
        .withSourceTax(BigDecimal.valueOf(12.35))
        .withSourceTaxRate(BigDecimal.valueOf(5.00))
        .withStatementDate(LocalDateUtility.toDate(LocalDate.now()))
        .withTaxType(ch.ahoegger.docbox.shared.hr.tax.TaxCodeType.SourceTax.ID)
        .withVacationExtra(BigDecimal.valueOf(12.5))
        .withWage(BigDecimal.valueOf(254.3))
        .withWorkingHours(BigDecimal.valueOf(11));

    bean1 = service.create(bean1);

    StatementBean bean2 = new StatementBean().withStatementId(bean1.getStatementId());
    bean2 = service.load(bean2);

    DocboxAssert.assertEquals(bean1, bean2);

    Assert.assertTrue(BEANS.get(TestBackupService.class).isBackupNeeded());

  }

}

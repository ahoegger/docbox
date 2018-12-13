package ch.ahoegger.docbox.server.administration.taxgroup;

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

import ch.ahoegger.docbox.server.hr.employee.EmployeeTaxGroupService;
import ch.ahoegger.docbox.server.hr.employer.EmployerTaxGroupService;
import ch.ahoegger.docbox.server.test.util.AbstractTestWithDatabase;
import ch.ahoegger.docbox.server.test.util.DocboxAssert;
import ch.ahoegger.docbox.server.test.util.TestDataGenerator;
import ch.ahoegger.docbox.shared.administration.hr.taxgroup.TaxGroupFormData;
import ch.ahoegger.docbox.shared.hr.employee.EmployeeTaxGroupSearchFormData;
import ch.ahoegger.docbox.shared.hr.employer.EmployerTaxGroupSearchFormData;
import ch.ahoegger.docbox.shared.hr.employer.EmployerTaxGroupTableData.EmployerTaxGroupTableRowData;
import ch.ahoegger.docbox.shared.util.LocalDateUtility;

/**
 * <h3>{@link TaxGroupServiceTest}</h3>
 *
 * @author aho
 */
public class TaxGroupServiceTest extends AbstractTestWithDatabase {

  private static TaxGroupService service;

  private TestDataGenerator m_testDataGenerator;
  private BigDecimal id_taxGroup;
  private BigDecimal id_employer2;
  private BigDecimal id_employee2;

  @BeforeClass
  public static void initService() {
    service = BEANS.get(TaxGroupService.class);
  }

  @Override
  protected void execSetupDb(Connection connection, TestDataGenerator testDataGenerator) throws Exception {
    m_testDataGenerator = testDataGenerator;
    // setup second employer/employee
    id_taxGroup = testDataGenerator.nextId();
    id_employer2 = testDataGenerator.nextId();
    id_employee2 = testDataGenerator.nextId();
    testDataGenerator
        .createTaxGroup(id_taxGroup, "2005", LocalDate.of(2005, 01, 01), LocalDate.of(2005, 12, 31))
        .createEmployer(id_employer2, "Emp2", "aStreet", "plz", "city", null, null)
        .createEmployerUser(id_employer2, m_testDataGenerator.id_user_bartSimpson_active)
        .createPartner(id_employee2, "EE2", null, LocalDate.of(2000, 01, 01), null)
        .createAnyEmployee(id_employee2, id_employer2);
  }

  @Test
  public void testCreateWithLinking() {
    TaxGroupFormData fd1 = new TaxGroupFormData();
    service.prepareCreate(fd1);
    fd1.getName().setValue("2002");
    fd1.getPeriodBox().getFrom().setValue(LocalDateUtility.toDate(LocalDate.of(2002, 01, 01)));
    fd1.getPeriodBox().getTo().setValue(LocalDateUtility.toDate(LocalDate.of(2002, 12, 31)));
    fd1.getLinkingBox().getEmployersBox().setValue(CollectionUtility.hashSet(m_testDataGenerator.id_employer_simpsonsInc));
    fd1.getLinkingBox().getEmployeesBox().setValue(CollectionUtility.hashSet(m_testDataGenerator.id_partner_nanny));
    fd1 = service.create(fd1);

    // expect erTaxGroup created
    EmployerTaxGroupSearchFormData searchData = new EmployerTaxGroupSearchFormData();
    searchData.getTaxGroup().setValue(fd1.getTaxGroupId());
    searchData.getEmployer().setValue(m_testDataGenerator.id_employer_simpsonsInc);
    List<EmployerTaxGroupTableRowData> resErTaxGroupSearch = Arrays.asList(BEANS.get(EmployerTaxGroupService.class).getTableData(searchData).getRows());
    Assert.assertEquals(
        CollectionUtility.arrayList(m_testDataGenerator.id_employer_simpsonsInc),
        resErTaxGroupSearch
            .stream().map(row -> row.getEmployer())
            .sorted()
            .collect(Collectors.toList()));

    // expect eeTaxGroup created
    EmployeeTaxGroupSearchFormData eeSearchData = new EmployeeTaxGroupSearchFormData();
    eeSearchData.getEmployee().setValue(m_testDataGenerator.id_partner_nanny);
    eeSearchData.setEmployerTaxGroupId(resErTaxGroupSearch.get(0).getEmployerTaxGroupId());
    List<BigDecimal> eeTaxGroupIds = Arrays.asList(BEANS.get(EmployeeTaxGroupService.class).getTableData(eeSearchData).getRows())
        .stream()
        .map(row -> row.getEmployee())
        .sorted()
        .collect(Collectors.toList());
    Assert.assertEquals(CollectionUtility.arrayList(m_testDataGenerator.id_partner_nanny), eeTaxGroupIds);
  }

  @Test
  public void testCreateNew() {
    TaxGroupFormData fd1 = new TaxGroupFormData();
    service.prepareCreate(fd1);
    fd1.getName().setValue("2003");
    fd1.getPeriodBox().getFrom().setValue(LocalDateUtility.toDate(LocalDate.of(2003, 01, 01)));
    fd1.getPeriodBox().getTo().setValue(LocalDateUtility.toDate(LocalDate.of(2003, 01, 01)));
    service.create(fd1);

    TaxGroupFormData fd2 = new TaxGroupFormData();
    fd2.setTaxGroupId(fd1.getTaxGroupId());
    fd2 = service.load(fd2);
    DocboxAssert.assertEquals(fd1, fd2);
  }

  @Test(expected = VetoException.class)
  public void testCreateWithoutToDate() {
    TaxGroupFormData fd1 = new TaxGroupFormData();
    service.prepareCreate(fd1);
    fd1.getName().setValue("2003");
    fd1.getPeriodBox().getFrom().setValue(LocalDateUtility.toDate(LocalDate.of(2003, 01, 01)));
    fd1.getPeriodBox().getTo().setValue(null);
    service.create(fd1);
  }

  @Test(expected = VetoException.class)
  public void testCreateOverlapping() {
    TaxGroupFormData fd1 = new TaxGroupFormData();
    service.prepareCreate(fd1);
    fd1.getName().setValue("2001-2");
    fd1.getPeriodBox().getFrom().setValue(LocalDateUtility.toDate(LocalDate.of(2001, 10, 01)));
    fd1.getPeriodBox().getTo().setValue(LocalDateUtility.toDate(LocalDate.of(2002, 02, 01)));
    service.create(fd1);
  }

  @Test(expected = VetoException.class)
  public void testModifyPrimaryKey() {
    TaxGroupFormData fd1 = new TaxGroupFormData();
    fd1.setTaxGroupId(id_taxGroup);
    service.load(fd1);
    fd1.setTaxGroupId(BigDecimal.valueOf(-1));
    service.store(fd1);
  }

  @Test
  public void testModify() {
    TaxGroupFormData fd1 = new TaxGroupFormData();
    fd1.setTaxGroupId(id_taxGroup);
    service.load(fd1);
    fd1.getName().setValue("New name");
    service.store(fd1);

    TaxGroupFormData fd2 = new TaxGroupFormData();
    fd2.setTaxGroupId(id_taxGroup);
    service.load(fd2);

    DocboxAssert.assertEquals(fd1, fd2);

  }

}

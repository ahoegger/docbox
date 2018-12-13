package ch.ahoegger.docbox.server.test.util;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

import org.ch.ahoegger.docbox.server.or.app.tables.EmployerUser;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.Assertions;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import ch.ahoegger.docbox.server.administration.hr.billing.BillingCycleService;
import ch.ahoegger.docbox.server.administration.taxgroup.TaxGroupService;
import ch.ahoegger.docbox.server.administration.user.UserService;
import ch.ahoegger.docbox.server.hr.AddressFormData;
import ch.ahoegger.docbox.server.hr.AddressService;
import ch.ahoegger.docbox.server.hr.billing.payslip.PayslipService;
import ch.ahoegger.docbox.server.hr.employee.EmployeeService;
import ch.ahoegger.docbox.server.hr.employee.EmployeeTaxGroupService;
import ch.ahoegger.docbox.server.hr.employer.EmployerService;
import ch.ahoegger.docbox.server.hr.employer.EmployerTaxGroupService;
import ch.ahoegger.docbox.server.hr.entity.EntityService;
import ch.ahoegger.docbox.server.hr.statement.StatementBean;
import ch.ahoegger.docbox.server.hr.statement.StatementService;
import ch.ahoegger.docbox.server.partner.PartnerService;
import ch.ahoegger.docbox.shared.hr.employer.EmployerFormData;
import ch.ahoegger.docbox.shared.hr.tax.TaxCodeType.SourceTax;
import ch.ahoegger.docbox.shared.util.LocalDateUtility;

/**
 * <h3>{@link TestDataGenerator}</h3>
 *
 * @author aho
 */
public final class TestDataGenerator {

  public Connection m_connection;
  public BigDecimal id_anyAddress;
  public BigDecimal id_statement_any;
  public BigDecimal id_taxGroup2000;
  public BigDecimal id_billingCycle2000_01;
  public BigDecimal id_employerTaxGroup_2000_simpsonInc;
  public String id_user_admin;
  public String id_user_bartSimpson_active;
  public String id_user_donaldDuck_inactive;
  public BigDecimal id_employer_simpsonsInc;
  public BigDecimal id_taxGroup2001;
  public BigDecimal id_employeeTaxGroup_nanny_2000;
  public BigDecimal id_partner_nanny;
  public BigDecimal id_payslip_nanny_2000_01;

  public TestDataGenerator(Connection connection) {
    m_connection = connection;
  }

  public BigDecimal nextId() {
    return BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  }

  public TestDataGenerator createDefaults() {
    id_anyAddress = nextId();
    createAddress(id_anyAddress, "Boumstamm Lane 1", "542365", "Crocodile City");
    id_statement_any = nextId();
    createAnyStatement(id_statement_any);
    createUser_admin();
    createUser_bartSimpson_active();
    createUser_donaldDuck_inactive();
    createEmployer_SimpsonsInc();
    createTaxGroup_2000();
    createTaxGroup_2001();
    createBillingCycle2000_01();
    createEmployerUser(id_employer_simpsonsInc, id_user_bartSimpson_active);
    createEmployerTaxGroup_simpsonInc_2000();
    createPartner_nanny();
    createEmployee_nanny();
    createEmployeeTaxGroup_2000_nanny();
    createPayslip_nanny_2000_01();
    return this;
  }

  private TestDataGenerator createUser_admin() {
    id_user_admin = "admin";
    createUser("admin", "manager", id_user_admin, "secret", true, true);
    return this;
  }

  private TestDataGenerator createUser_bartSimpson_active() {
    id_user_bartSimpson_active = "bartSimpson";
    return createUser("Simpson", "Bart", id_user_bartSimpson_active, "seret", true, false);
  }

  private TestDataGenerator createUser_donaldDuck_inactive() {
    id_user_donaldDuck_inactive = "donaldDuck";
    return createUser("Duck", "Donald", id_user_donaldDuck_inactive, "seret", false, false);

  }

  public TestDataGenerator createUser(String name, String firstName, String userName, String password, boolean active, boolean admin) {
    BEANS.get(UserService.class).insert(m_connection, name, firstName, userName, password, active, admin);
    return this;
  }

  public TestDataGenerator createAddress(BigDecimal id, String line1, String plz, String city) {
    BEANS.get(AddressService.class).insert(m_connection, new AddressFormData().withAddressNr(id).withLine1(line1).withPlz(plz).withCity(city));
    return this;
  }

  private TestDataGenerator createEmployer_SimpsonsInc() {
    id_employer_simpsonsInc = nextId();
    return createEmployer(id_employer_simpsonsInc, "Simpsons Inc", "Jeanpuk street 3ab", "55333", "Clokeria", "mail@simpsons.org", "079 000 000");
  }

  public TestDataGenerator createEmployerAny(BigDecimal id, String name) {
    return createEmployer(id, name, "AnyStreet[" + id.intValue() + "]", "any PLZ", "any City", "mail@box.org", "079");
  }

  public TestDataGenerator createEmployer(BigDecimal id, String name, String addLine1, String addPlz, String addCity, String mail, String phone) {
    BigDecimal addId = nextId();
    createAddress(addId, addLine1, addPlz, addCity);
    EmployerFormData formData = new EmployerFormData();
    formData.setEmployerId(id);
    formData.getName().setValue(name);
    formData.getAddressBox().setAddressId(addId);
    formData.getEmail().setValue(mail);
    formData.getPhone().setValue(phone);
    BEANS.get(EmployerService.class).insert(m_connection, formData);
    return this;
  }

  public TestDataGenerator createEmployerUser(BigDecimal employerId, String username) {
    EmployerUser eu = EmployerUser.EMPLOYER_USER;
    DSL.using(m_connection, SQLDialect.DERBY)
        .newRecord(eu)
        .with(eu.EMPLOYER_NR, employerId)
        .with(eu.USERNAME, username)
        .insert();
    return this;

  }

  private TestDataGenerator createTaxGroup_2000() {
    id_taxGroup2000 = nextId();
    createTaxGroup(id_taxGroup2000, "2000", LocalDate.of(2000, 01, 01), LocalDate.of(2000, 12, 31));
    return this;
  }

  private TestDataGenerator createTaxGroup_2001() {
    id_taxGroup2001 = nextId();
    createTaxGroup(id_taxGroup2001, "2001", LocalDate.of(2001, 01, 01), LocalDate.of(2001, 12, 31));
    return this;
  }

  public TestDataGenerator createTaxGroup(BigDecimal id, String name, LocalDate start, LocalDate end) {
    BEANS.get(TaxGroupService.class).insert(m_connection, id, name, LocalDateUtility.toDate(start), LocalDateUtility.toDate(end));
    return this;
  }

  private TestDataGenerator createBillingCycle2000_01() {
    Assertions.assertNotNull(id_taxGroup2000);
    id_billingCycle2000_01 = nextId();
    createBillingCycle(id_billingCycle2000_01, id_taxGroup2000, "Januar 2000", LocalDate.of(2000, 01, 01), LocalDate.of(2000, 01, 31));
    return this;
  }

  public TestDataGenerator createBillingCycle(BigDecimal id, BigDecimal taxGroupId, LocalDate date) {
    return createBillingCycle(id, taxGroupId, LocalDateUtility.DATE_FORMATTER_MMMM.format(date), date.with(TemporalAdjusters.firstDayOfMonth()), date.with(TemporalAdjusters.firstDayOfMonth()));
  }

  public TestDataGenerator createBillingCycle(BigDecimal id, BigDecimal taxGroupId, String name, LocalDate start, LocalDate end) {
    BEANS.get(BillingCycleService.class).insert(m_connection, id, taxGroupId, name, LocalDateUtility.toDate(start), LocalDateUtility.toDate(end));
    return this;
  }

  private TestDataGenerator createEmployerTaxGroup_simpsonInc_2000() {
    id_employerTaxGroup_2000_simpsonInc = nextId();
    createEmployerTaxGroup(id_employerTaxGroup_2000_simpsonInc, id_employer_simpsonsInc, id_taxGroup2000, null);
    return this;
  }

  public TestDataGenerator createEmployerTaxGroup(BigDecimal id, BigDecimal employerId, BigDecimal taxGroupId, BigDecimal statementId) {
    BEANS.get(EmployerTaxGroupService.class).insert(m_connection, id, employerId, taxGroupId, statementId);
    return this;
  }

  private TestDataGenerator createPartner_nanny() {
    id_partner_nanny = nextId();
    return createPartner(id_partner_nanny, "Nanny Dalton", null, LocalDate.now(), null);
  }

  public TestDataGenerator createPartner(BigDecimal patnerId, String name, String description, LocalDate startDate, LocalDate endDate) {
    BEANS.get(PartnerService.class).insert(m_connection, patnerId, name, description, LocalDateUtility.toDate(startDate), LocalDateUtility.toDate(endDate));
    return this;
  }

  private TestDataGenerator createEmployee_nanny() {
    Assertions.assertNotNull(id_partner_nanny);
    return createEmployee(id_partner_nanny, "Nanny", "Dalton", id_anyAddress, "1523.2186.2156", "PC-30.600.222", SourceTax.ID, true, LocalDate.of(1990, 04, 15),
        BigDecimal.valueOf(25), BigDecimal.valueOf(6.225), BigDecimal.valueOf(5.0), BigDecimal.valueOf(8.33), id_employer_simpsonsInc);
  }

  public TestDataGenerator createEmployee(BigDecimal id, String firstname, String name, BigDecimal hourlyWage,
      BigDecimal socialInsuranceRate, BigDecimal sourceTaxRate, BigDecimal vacationExtraRate, BigDecimal employerId) {
    return createEmployee(id, firstname, name, id_anyAddress, null, null, SourceTax.ID, true, null,
        hourlyWage, socialInsuranceRate, sourceTaxRate, vacationExtraRate, employerId);
  }

  public TestDataGenerator createAnyEmployee(BigDecimal id, BigDecimal employerId) {
    return createEmployee(id, "first[" + id.intValue() + "]", "last[" + id.intValue() + "]", id_anyAddress, "1523.2186.2156", "PC-30.600.222", SourceTax.ID, true, LocalDate.of(1990, 04, 15),
        BigDecimal.valueOf(25), BigDecimal.valueOf(6.225), BigDecimal.valueOf(5.0), BigDecimal.valueOf(8.33), employerId);
  }

  public TestDataGenerator createEmployee(BigDecimal id, String firstname, String name, BigDecimal addressId, String ahvNumber, String accountNumber, BigDecimal taxType, boolean reducedLunch, LocalDate birthday, BigDecimal hourlyWage,
      BigDecimal socialInsuranceRate, BigDecimal sourceTaxRate, BigDecimal vacationExtraRate, BigDecimal employerId) {
    Assertions.assertNotNull(id_partner_nanny);
    BEANS.get(EmployeeService.class).insert(m_connection, id, firstname, name, addressId, ahvNumber, accountNumber, taxType, reducedLunch, LocalDateUtility.toDate(birthday), hourlyWage, socialInsuranceRate, sourceTaxRate, vacationExtraRate,
        employerId);
    return this;
  }

  private TestDataGenerator createEmployeeTaxGroup_2000_nanny() {
    Assertions.assertNotNull(id_partner_nanny);
    Assertions.assertNotNull(id_employerTaxGroup_2000_simpsonInc);
    id_employeeTaxGroup_nanny_2000 = nextId();
    createEmployeeTaxGroup(id_employeeTaxGroup_nanny_2000, id_employerTaxGroup_2000_simpsonInc, id_partner_nanny, null, LocalDate.of(2000, 01, 01), LocalDate.of(2000, 12, 31));
    return this;

  }

  public TestDataGenerator createEmployeeTaxGroup(BigDecimal employeeTaxGroupId, BigDecimal employerTaxGroupId, BigDecimal employeeId, BigDecimal statementId, LocalDate start, LocalDate end) {
    BEANS.get(EmployeeTaxGroupService.class).insert(m_connection, employeeTaxGroupId, employerTaxGroupId, employeeId, statementId, LocalDateUtility.toDate(start), LocalDateUtility.toDate(end));
    return this;
  }

  private TestDataGenerator createPayslip_nanny_2000_01() {
    Assertions.assertNotNull(id_billingCycle2000_01);
    Assertions.assertNotNull(id_employeeTaxGroup_nanny_2000);
    id_payslip_nanny_2000_01 = nextId();
    return createPayslip(id_payslip_nanny_2000_01, id_billingCycle2000_01, id_employeeTaxGroup_nanny_2000, null);
  }

  public TestDataGenerator createPayslip(BigDecimal payslipId, BigDecimal billingCycleId, BigDecimal employeeTaxGroupId, BigDecimal statementId) {
    BEANS.get(PayslipService.class).insert(m_connection, payslipId, billingCycleId, employeeTaxGroupId, statementId);
    return this;
  }

  public TestDataGenerator createEntity(BigDecimal entityId, BigDecimal payslipId, BigDecimal entityType, LocalDate date, BigDecimal workingHours, BigDecimal expense, String text) {
    BEANS.get(EntityService.class).insert(m_connection, entityId, payslipId, entityType, LocalDateUtility.toDate(date), workingHours, expense, text);
    return this;
  }

  public TestDataGenerator createAnyStatement(BigDecimal id) {
    return createStatement(id, null, SourceTax.ID, LocalDate.now(), "PC-3897549384", BigDecimal.valueOf(25.5),
        BigDecimal.valueOf(6.225), BigDecimal.valueOf(5.0), BigDecimal.valueOf(8.33), BigDecimal.valueOf(14), BigDecimal.valueOf(1450.4), BigDecimal.valueOf(1460.4), BigDecimal.valueOf(1245.4), BigDecimal.valueOf(1222),
        BigDecimal.valueOf(50.4), BigDecimal.valueOf(35.5), BigDecimal.valueOf(88), BigDecimal.valueOf(200));
  }

  public TestDataGenerator createStatement(BigDecimal statementId, BigDecimal documentId, BigDecimal taxType, LocalDate statementDate, String accountNumber, BigDecimal hourlyWage, BigDecimal socialInsuranceRate, BigDecimal sourceTaxRate,
      BigDecimal vacationExtraRate, BigDecimal workingHours, BigDecimal wage, BigDecimal bruttoWage, BigDecimal nettoWage, BigDecimal nettoWagePayout, BigDecimal sourceTax, BigDecimal socialInsuranceTax, BigDecimal vacationExtra,
      BigDecimal expenses) {
    BEANS.get(StatementService.class).insert(
        new StatementBean()
            .withStatementId(statementId)
            .withDocumentId(documentId)
            .withTaxType(taxType)
            .withStatementDate(LocalDateUtility.toDate(statementDate))
            .withAccountNumber(accountNumber)
            .withHourlyWage(hourlyWage)
            .withSocialInsuranceRate(socialInsuranceRate)
            .withSourceTaxRate(sourceTaxRate)
            .withVacationExtraRate(vacationExtraRate)
            .withWorkingHours(workingHours)
            .withWage(wage)
            .withBruttoWage(bruttoWage)
            .withNettoWage(nettoWage)
            .withNettoWagePayout(nettoWagePayout)
            .withSourceTax(sourceTax)
            .withSocialInsuranceTax(socialInsuranceTax)
            .withVacationExtra(vacationExtra)
            .withExpenses(expenses));
    return this;
  }

}

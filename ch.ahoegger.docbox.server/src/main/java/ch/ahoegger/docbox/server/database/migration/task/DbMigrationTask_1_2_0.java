package ch.ahoegger.docbox.server.database.migration.task;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.ch.ahoegger.docbox.server.or.app.tables.Address;
import org.ch.ahoegger.docbox.server.or.app.tables.BillingCycle;
import org.ch.ahoegger.docbox.server.or.app.tables.Employee;
import org.ch.ahoegger.docbox.server.or.app.tables.EmployeeTaxGroup;
import org.ch.ahoegger.docbox.server.or.app.tables.Employer;
import org.ch.ahoegger.docbox.server.or.app.tables.EmployerTaxGroup;
import org.ch.ahoegger.docbox.server.or.app.tables.EmployerUser;
import org.ch.ahoegger.docbox.server.or.app.tables.Entity;
import org.ch.ahoegger.docbox.server.or.app.tables.Partner;
import org.ch.ahoegger.docbox.server.or.app.tables.Payslip;
import org.ch.ahoegger.docbox.server.or.app.tables.Statement;
import org.ch.ahoegger.docbox.server.or.app.tables.TaxGroup;
import org.ch.ahoegger.docbox.server.or.app.tables.records.EmployeeTaxGroupRecord;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.TypeCastUtility;
import org.eclipse.scout.rt.server.jdbc.SQL;
import org.jooq.Field;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import ch.ahoegger.docbox.or.definition.table.IAddressTable;
import ch.ahoegger.docbox.or.definition.table.IBillingCicleTable;
import ch.ahoegger.docbox.or.definition.table.IEmployeeTable;
import ch.ahoegger.docbox.or.definition.table.IEmployeeTaxGroupTable;
import ch.ahoegger.docbox.or.definition.table.IEmployerTable;
import ch.ahoegger.docbox.or.definition.table.IEmployerUserTable;
import ch.ahoegger.docbox.or.definition.table.IEntityTable;
import ch.ahoegger.docbox.or.definition.table.IMigrationTable;
import ch.ahoegger.docbox.or.definition.table.IPayslipTable;
import ch.ahoegger.docbox.or.definition.table.ISequenceTable;
import ch.ahoegger.docbox.or.definition.table.IStatementTable;
import ch.ahoegger.docbox.server.database.migration.IMigrationTask;
import ch.ahoegger.docbox.server.database.migration.ITableDescription;
import ch.ahoegger.docbox.server.database.migration.MigrationService;
import ch.ahoegger.docbox.server.database.migration.MigrationUtility;
import ch.ahoegger.docbox.server.or.generator.Version;
import ch.ahoegger.docbox.server.or.generator.table.AddressTableStatement;
import ch.ahoegger.docbox.server.or.generator.table.BillingCycleTableStatement;
import ch.ahoegger.docbox.server.or.generator.table.EmployeeTaxGroupTableStatement;
import ch.ahoegger.docbox.server.or.generator.table.EmployerTableStatement;
import ch.ahoegger.docbox.server.or.generator.table.EmployerTaxGroupTableStatement;
import ch.ahoegger.docbox.server.or.generator.table.EmployerUserTableStatement;
import ch.ahoegger.docbox.server.or.generator.table.MigrationTableStatement;
import ch.ahoegger.docbox.server.or.generator.table.PayslipTableStatement;
import ch.ahoegger.docbox.server.or.generator.table.StatementTableStatement;
import ch.ahoegger.docbox.server.util.BigDecimalUtilitiy;
import ch.ahoegger.docbox.shared.hr.entity.EntityTypeCodeType;
import ch.ahoegger.docbox.shared.hr.tax.TaxCodeType;

/**
 * <h3>{@link DbMigrationTask_1_2_0}</h3>
 *
 * @author aho
 */
public class DbMigrationTask_1_2_0 implements IMigrationTask {

  public static final Version TARGET_VERSION = new Version(1, 2, 0);
  private BigDecimal m_employerId;

  private Map<String, String> m_nameMapping = new HashMap<>();

  @PostConstruct
  protected void init() {
    m_nameMapping.put(IEmployeeTable.EMPLOYEE_NR, "PARTNER_NR");
  }

  @Override
  public Version getVersion() {
    return TARGET_VERSION;
  }

  @Override
  public void run() {
    createMigrationTable();
    createAddressTable();
    updateEntityTable();
    updateEmployeeTable();
    createStatementTable();

    createAndFillBillingCycleTable();
    m_employerId = createAndFillEmployer();
    extractEmployeeAddressAndSetEmployer(m_employerId);
    createAndFillEmployerUser();
    createAndFillEmployerTaxGroup();
    createAndFillEmployeeTaxGroup();
    createAndFillPayslip();

    // cleanup

    cleanUpEmployeeTable();
    cleanUpEntity();
    dropPostingGroup();
    BEANS.get(MigrationService.class).insert(TARGET_VERSION);
  }

  /**
   *
   */

  protected void createMigrationTable() {
    ITableDescription tableDesc = MigrationUtility.getTableDesription(IMigrationTable.TABLE_NAME);
    if (tableDesc == null) {
      // create
      SQL.update(BEANS.get(MigrationTableStatement.class).getCreateTable());
    }
  }

  protected void createAddressTable() {
    ITableDescription tableDesc = MigrationUtility.getTableDesription(IAddressTable.TABLE_NAME);
    if (tableDesc == null) {
      SQL.update(BEANS.get(AddressTableStatement.class).getCreateTable());
    }

  }

  /**
   *
   */
  private void updateEntityTable() {
    ITableDescription tableDesc = MigrationUtility.getTableDesription(IEntityTable.TABLE_NAME);
    if (!tableDesc.hasColumn(IEntityTable.PAYSLIP_NR)) {
      SQL.update("ALTER TABLE " + IEntityTable.TABLE_NAME + " ADD " + IEntityTable.PAYSLIP_NR + " BIGINT DEFAULT -1 NOT NULL");
    }

  }

  private void updateEmployeeTable() {
    ITableDescription tableDesc = MigrationUtility.getTableDesription(IEmployeeTable.TABLE_NAME);
    if (tableDesc.hasColumn(m_nameMapping.get(IEmployeeTable.EMPLOYEE_NR))) {
      m_nameMapping.put(IEmployeeTable.EMPLOYEE_NR, IEmployeeTable.EMPLOYEE_NR);
      SQL.update("RENAME COLUMN " + IEmployeeTable.TABLE_NAME + ".PARTNER_NR TO " + IEmployeeTable.EMPLOYEE_NR);
    }
    if (!tableDesc.hasColumn(IEmployeeTable.ADDRESS_NR)) {
      SQL.update("ALTER TABLE " + IEmployeeTable.TABLE_NAME + " ADD " + IEmployeeTable.ADDRESS_NR + " BIGINT DEFAULT -1 NOT NULL");
    }
    if (!tableDesc.hasColumn(IEmployeeTable.EMPLOYER_NR)) {
      SQL.update("ALTER TABLE " + IEmployeeTable.TABLE_NAME + " ADD " + IEmployeeTable.EMPLOYER_NR + " BIGINT DEFAULT -1 NOT NULL");
    }
    if (!tableDesc.hasColumn(IEmployeeTable.EMPLOYER_NR)) {
      SQL.update("ALTER TABLE " + IEmployeeTable.TABLE_NAME + " ADD " + IEmployeeTable.TAX_TYPE + " BIGINT DEFAULT 31 NOT NULL");
    }
    if (!tableDesc.hasColumn(IEmployeeTable.REDUCED_LUNCH)) {
      SQL.update("ALTER TABLE " + IEmployeeTable.TABLE_NAME + " ADD " + IEmployeeTable.REDUCED_LUNCH + "  BOOLEAN DEFAULT TRUE NOT NULL");
    }

  }

  protected void createStatementTable() {
    ITableDescription tableDesc = MigrationUtility.getTableDesription(IStatementTable.TABLE_NAME);
    if (tableDesc == null) {
      SQL.update(BEANS.get(StatementTableStatement.class).getCreateTable());
    }
  }

  protected void createAndFillBillingCycleTable() {
    ITableDescription tableDesc = MigrationUtility.getTableDesription(IBillingCicleTable.TABLE_NAME);
    if (tableDesc == null) {
      final BillingCycle bs = BillingCycle.BILLING_CYCLE;

      SQL.update(BEANS.get(BillingCycleTableStatement.class).getCreateTable());

      Arrays.asList(SQL.select("SELECT NAME, START_DATE, END_DATE FROM POSTING_GROUP GROUP BY NAME,START_DATE, END_DATE ORDER BY START_DATE")).stream()
          .forEach(row -> {
            BigDecimal nextId = new BigDecimal(SQL.getSequenceNextval(ISequenceTable.TABLE_NAME));

            Date startDate = TypeCastUtility.castValue(row[1], Date.class);
            DSL.using(SQL.getConnection(), SQLDialect.DERBY).executeInsert(
                bs.newRecord()
                    .with(bs.BILLING_CYCLE_NR, nextId)
                    .with(bs.TAX_GROUP_NR, findTaxGroup(startDate))
                    .with(bs.NAME, TypeCastUtility.castValue(row[0], String.class))
                    .with(bs.START_DATE, startDate)
                    .with(bs.END_DATE, TypeCastUtility.castValue(row[2], Date.class)));
          });
    }
  }

  protected BigDecimal createAndFillEmployer() {
    ITableDescription tableDesc = MigrationUtility.getTableDesription(IEmployerTable.TABLE_NAME);
    if (tableDesc == null) {
      SQL.update(BEANS.get(EmployerTableStatement.class).getCreateTable());

      final Employer table = Employer.EMPLOYER;
      return Arrays.asList(SQL.select(
          "SELECT "
              + "EMPLOYER_ADDRESS_LINE1, " // 0
              + "EMPLOYER_ADDRESS_LINE2, " // 1
              + "EMPLOYER_ADDRESS_LINE3, " // 2
              + "EMPLOYER_EMAIL, " // 3
              + "EMPLOYER_PHONE " // 4
              + "FROM EMPLOYEE GROUP BY EMPLOYER_ADDRESS_LINE1, EMPLOYER_ADDRESS_LINE2, EMPLOYER_ADDRESS_LINE3, EMPLOYER_EMAIL, EMPLOYER_PHONE"))
          .stream()
          .map(row -> {
            BigDecimal addressId = createAddress(row[1], row[2]);
            BigDecimal nextId = createPartner(TypeCastUtility.castValue(row[0], String.class), null, getFirstEntityDate());

            DSL.using(SQL.getConnection(), SQLDialect.DERBY).executeInsert(
                table.newRecord()
                    .with(table.EMPLOYER_NR, nextId)
                    .with(table.ADDRESS_NR, addressId)
                    .with(table.EMAIL, TypeCastUtility.castValue(row[3], String.class))
                    .with(table.NAME, TypeCastUtility.castValue(row[0], String.class))
                    .with(table.PHONE, TypeCastUtility.castValue(row[4], String.class)));
            return nextId;
          })
          .findFirst().get();
    }
    else {
      throw new ProcessingException();
    }
  }

  /**
   * @param employerId
   */
  private void extractEmployeeAddressAndSetEmployer(BigDecimal employerId) {

    final Address table = Address.ADDRESS;
    final Employee employeeTable = Employee.EMPLOYEE;

    Arrays.asList(SQL.select("SELECT "
        + "EMPLOYEE_NR, " // 0
        + "ADDRESS_LINE1, " // 1
        + "ADDRESS_LINE2 " // 2
        + "FROM EMPLOYEE"))
        .stream()
        .forEach(row -> {
          BigDecimal nextId = new BigDecimal(SQL.getSequenceNextval(ISequenceTable.TABLE_NAME));
          String[] plzCityArr = splitPlzCity(row[2]);

          DSL.using(SQL.getConnection(), SQLDialect.DERBY).executeInsert(
              table.newRecord()
                  .with(table.ADDRESS_NR, nextId)
                  .with(table.LINE_1, TypeCastUtility.castValue(row[1], String.class))
                  .with(table.PLZ, plzCityArr[0])
                  .with(table.CITY, plzCityArr[1]));

          DSL.using(SQL.getConnection(), SQLDialect.DERBY)
              .update(employeeTable)
              .set(employeeTable.ADDRESS_NR, nextId)
              .set(employeeTable.EMPLOYER_NR, employerId)
              .where(employeeTable.EMPLOYEE_NR.eq(TypeCastUtility.castValue(row[0], BigDecimal.class)))
              .execute();

        });
  }

  protected void createAndFillEmployerUser() {
    ITableDescription tableDesc = MigrationUtility.getTableDesription(IEmployerUserTable.TABLE_NAME);
    if (tableDesc == null) {
      SQL.update(BEANS.get(EmployerUserTableStatement.class).getCreateTable());

      final EmployerUser table = EmployerUser.EMPLOYER_USER;

      DSL.using(SQL.getConnection(), SQLDialect.DERBY).executeInsert(
          table.newRecord()
              .with(table.USERNAME, "aho")
              .with(table.EMPLOYER_NR, m_employerId));
    }
  }

  protected void createAndFillEmployerTaxGroup() {

    ITableDescription tableDesc = MigrationUtility.getTableDesription(IEmployeeTaxGroupTable.TABLE_NAME);
    if (tableDesc == null) {
      SQL.update(BEANS.get(EmployerTaxGroupTableStatement.class).getCreateTable());

      final EmployerTaxGroup table = EmployerTaxGroup.EMPLOYER_TAX_GROUP;
      Arrays.asList(SQL.select("SELECT TAX_GROUP_NR FROM TAX_GROUP"))
          .stream()
          .forEach(row -> {
            BigDecimal nextId = new BigDecimal(SQL.getSequenceNextval(ISequenceTable.TABLE_NAME));

            DSL.using(SQL.getConnection(), SQLDialect.DERBY).executeInsert(
                table.newRecord()
                    .with(table.EMPLOYER_TAX_GROUP_NR, nextId)
                    .with(table.TAX_GROUP_NR, TypeCastUtility.castValue(row[0], BigDecimal.class))
                    .with(table.EMPLOYER_NR, m_employerId));

          });

    }
  }

  protected void createAndFillEmployeeTaxGroup() {
    ITableDescription tableDesc = MigrationUtility.getTableDesription(IEmployeeTaxGroupTable.TABLE_NAME);
    if (tableDesc == null) {
      SQL.update(BEANS.get(EmployeeTaxGroupTableStatement.class).getCreateTable());

      final EmployeeTaxGroup table = EmployeeTaxGroup.EMPLOYEE_TAX_GROUP;
      Arrays.asList(SQL.select("SELECT "
          + "E.EMPLOYEE_NR, " // 0
          + "TG.TAX_GROUP_NR, " //1
          + "TG.START_DATE, " // 2
          + "TG.END_DATE " // 3
          + "FROM EMPLOYEE E CROSS JOIN TAX_GROUP TG"))
          .stream()
          .forEach(row -> {
            BigDecimal nextId = new BigDecimal(SQL.getSequenceNextval(ISequenceTable.TABLE_NAME));

            EmployeeTaxGroupRecord rec = table.newRecord()
                .with(table.EMPLOYEE_TAX_GROUP_NR, nextId)
                .with(table.EMPLOYER_TAX_GROUP_NR, findEmployerTaxGroupByTaxGroupId(TypeCastUtility.castValue(row[1], BigDecimal.class)))
                .with(table.EMPLOYEE_NR, TypeCastUtility.castValue(row[0], BigDecimal.class))
                .with(table.START_DATE, TypeCastUtility.castValue(row[2], Date.class))
                .with(table.END_DATE, TypeCastUtility.castValue(row[3], Date.class));
            DSL.using(SQL.getConnection(), SQLDialect.DERBY).executeInsert(
                rec);

          });

    }
  }

  protected void createAndFillPayslip() {
    ITableDescription tableDesc = MigrationUtility.getTableDesription(IPayslipTable.TABLE_NAME);
    if (tableDesc == null) {
      SQL.update(BEANS.get(PayslipTableStatement.class).getCreateTable());

      final Payslip table = Payslip.PAYSLIP;

      Arrays.asList(
          SQL.select("SELECT "
              + "PG.POSTING_GROUP_NR, " // 0
              + "PG.PARTNER_NR , " // 1
              + "PG.START_DATE, " // 2
              + "PG.DOCUMENT_NR, " // 3
              + "PG.STATEMENT_DATE, " // 4
              + "PG.WORKING_HOURS, " // 5
              + "PG.BRUTTO_WAGE, " // 6
              + "PG.NETTO_WAGE, " // 7
              + "PG.SOURCE_TAX, " // 8
              + "PG.SOCIAL_SECURITY_TAX, " // 9
              + "PG.VACATION_EXTRA, " // 10
              + "E.ACCOUNT_NUMBER, " // 11
              + "E.HOURLY_WAGE, " // 12
              + "E.SOCIAL_INSURANCE_RATE, " // 13
              + "E.SOURCE_TAX_RATE, " // 14
              + "E.VACATION_EXTRA_RATE, "// 15
              + "PG.NAME " // 16
              + "FROM POSTING_GROUP PG LEFT OUTER JOIN EMPLOYEE E ON PG.PARTNER_NR = E.EMPLOYEE_NR"))
          .stream()
          .forEach(row -> {
            BigDecimal postingGroupId = TypeCastUtility.castValue(row[0], BigDecimal.class);
            BigDecimal nextId = new BigDecimal(SQL.getSequenceNextval(ISequenceTable.TABLE_NAME));
            BigDecimal statementId = createStatement(postingGroupId, row[3], row[4], row[11], row[12], row[13], row[14], row[15], row[5], row[6], row[7], row[8], row[9], row[10]);

            DSL.using(SQL.getConnection(), SQLDialect.DERBY).executeInsert(
                table.newRecord()
                    .with(table.BILLING_CYCLE_NR, findBillingCycleIdByName(row[16]))
                    .with(table.EMPLOYEE_TAX_GROUP_NR, findEmployeeTaxGroup(row[1], row[2]))
                    .with(table.PAYSLIP_NR, nextId)
                    .with(table.STATEMENT_NR, statementId));
            updateEntityPayslipNr(postingGroupId, nextId);

          });
    }
  }

  /**
   *
   */
  protected void cleanUpEmployeeTable() {

    // to remove EMPLOYER_ADDRESS_LINE2, EMPLOYER_ADDRESS_LINE1, ADDRESS_LINE1, EMPLOYER_EMAIL, ADDRESS_LINE2, EMPLOYER_PHONE, EMPLOYER_ADDRESS_LINE3
    Arrays.asList(new String[]{"EMPLOYER_ADDRESS_LINE2", "EMPLOYER_ADDRESS_LINE1", "ADDRESS_LINE1", "EMPLOYER_EMAIL", "ADDRESS_LINE2", "EMPLOYER_PHONE", "EMPLOYER_ADDRESS_LINE3"})
        .stream().forEach(col -> SQL.update("ALTER TABLE " + IEmployeeTable.TABLE_NAME + " DROP COLUMN " + col));
  }

  protected void cleanUpEntity() {
    Arrays.asList(new String[]{"POSTING_GROUP_NR", "PARTNER_NR"})
        .stream().forEach(col -> SQL.update("ALTER TABLE " + IEntityTable.TABLE_NAME + " DROP COLUMN " + col));
  }

  protected void dropPostingGroup() {
    SQL.update("DROP TABLE POSTING_GROUP");
  }

  // internal helper

  private BigDecimal createStatement(BigDecimal postingGroupId, Object documentId, Object statementDate, Object accountNumber, Object hourlyWageRaw, Object socialInsuranceRate, Object sourceTaxRate, Object vacationExtraRate,
      Object workingHoursRaw,
      Object bruttoWage, Object nettoWageRaw, Object sourceTax, Object socialSecurityTax, Object vacationExtra) {
    ITableDescription tableDesc = MigrationUtility.getTableDesription(IStatementTable.TABLE_NAME);
    if (tableDesc == null) {
      throw new ProcessingException("Statement table does not exist.");
    }
    Statement table = Statement.STATEMENT;
    BigDecimal nextId = new BigDecimal(SQL.getSequenceNextval(ISequenceTable.TABLE_NAME));
    BigDecimal nettoWage = TypeCastUtility.castValue(nettoWageRaw, BigDecimal.class);
    BigDecimal workingHours = TypeCastUtility.castValue(workingHoursRaw, BigDecimal.class);
    BigDecimal hourlyWage = TypeCastUtility.castValue(hourlyWageRaw, BigDecimal.class);
    DSL.using(SQL.getConnection(), SQLDialect.DERBY).executeInsert(
        table.newRecord()
            .with(table.ACCOUNT_NUMBER, TypeCastUtility.castValue(accountNumber, String.class))
            .with(table.BRUTTO_WAGE, TypeCastUtility.castValue(bruttoWage, BigDecimal.class))
            .with(table.DOCUMENT_NR, TypeCastUtility.castValue(documentId, BigDecimal.class))
            .with(table.EXPENSES, calculateExpenses(postingGroupId))
            .with(table.HOURLY_WAGE, hourlyWage)
            .with(table.NETTO_WAGE, nettoWage)
            .with(table.NETTO_WAGE_PAYOUT, BigDecimalUtilitiy.financeRound(nettoWage, BigDecimal.valueOf(0.05), RoundingMode.UP))
            .with(table.SOCIAL_INSURANCE_RATE, TypeCastUtility.castValue(socialInsuranceRate, BigDecimal.class))
            .with(table.SOCIAL_INSURANCE_TAX, TypeCastUtility.castValue(socialSecurityTax, BigDecimal.class).negate())
            .with(table.SOURCE_TAX, TypeCastUtility.castValue(sourceTax, BigDecimal.class).negate())
            .with(table.SOURCE_TAX_RATE, TypeCastUtility.castValue(sourceTaxRate, BigDecimal.class))
            .with(table.STATEMENT_DATE, TypeCastUtility.castValue(statementDate, Date.class))
            .with(table.STATEMENT_NR, nextId)
            .with(table.TAX_TYPE, TaxCodeType.SourceTax.ID)
            .with(table.VACATION_EXTRA, TypeCastUtility.castValue(vacationExtra, BigDecimal.class))
            .with(table.VACATION_EXTRA_RATE, TypeCastUtility.castValue(vacationExtraRate, BigDecimal.class))
            .with(table.WAGE, workingHours.multiply(hourlyWage).setScale(2, RoundingMode.HALF_UP))
            .with(table.WORKING_HOURS, workingHours));

    return nextId;
  }

  // ************************************************************************************************************************************************************
  // HELPER METHODS
  // ************************************************************************************************************************************************************

  /**
   * @param postingGroupId
   * @return
   */
  private BigDecimal calculateExpenses(BigDecimal postingGroupId) {
    Entity table = Entity.ENTITY;
    Field<BigDecimal> expenses = DSL.sum(table.EXPENSE_AMOUNT).as("EXPENSE_SUM");
    return DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .select(expenses)
        .from(table)
        .where("POSTING_GROUP_NR = ?", postingGroupId)
        .and(table.ENTITY_TYPE.eq(EntityTypeCodeType.ExpenseCode.ID))
        .fetchOne().get(expenses);
  }

  private void updateEntityPayslipNr(BigDecimal postingGroupNr, BigDecimal payslipNr) {
    Entity table = Entity.ENTITY;
    DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .update(table).set(table.PAYSLIP_NR, payslipNr)
        .where("POSTING_GROUP_NR = ?", postingGroupNr).execute();
  }

  private BigDecimal findBillingCycleIdByName(Object name) {
    BillingCycle table = BillingCycle.BILLING_CYCLE;
    return DSL.using(SQL.getConnection(), SQLDialect.DERBY).fetchOne(table, table.NAME.eq(TypeCastUtility.castValue(name, String.class))).get(table.BILLING_CYCLE_NR);
  }

  private BigDecimal findEmployeeTaxGroup(Object employeeId, Object startDate) {
    EmployeeTaxGroup table = EmployeeTaxGroup.EMPLOYEE_TAX_GROUP;
    Date date = TypeCastUtility.castValue(startDate, Date.class);
    return DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .fetchOne(table,
            table.EMPLOYEE_NR.eq(TypeCastUtility.castValue(employeeId, BigDecimal.class))
                .and(table.START_DATE.le(date))
                .and(table.END_DATE.ge(date)))
        .get(table.EMPLOYEE_TAX_GROUP_NR);
  }

  private BigDecimal findEmployerTaxGroupByTaxGroupId(BigDecimal taxGroupId) {
    EmployerTaxGroup table = EmployerTaxGroup.EMPLOYER_TAX_GROUP;
    return DSL.using(SQL.getConnection(), SQLDialect.DERBY).fetchOne(table, table.TAX_GROUP_NR.eq(taxGroupId)).get(table.EMPLOYER_TAX_GROUP_NR);
  }

  private BigDecimal createAddress(Object line1, Object plzCity) {
    String[] plzCityArr = splitPlzCity(plzCity);

    Address table = Address.ADDRESS;
    BigDecimal nextId = new BigDecimal(SQL.getSequenceNextval(ISequenceTable.TABLE_NAME));
    DSL.using(SQL.getConnection(), SQLDialect.DERBY).executeInsert(
        table.newRecord()
            .with(table.ADDRESS_NR, nextId)
            .with(table.LINE_1, TypeCastUtility.castValue(line1, String.class))
            .with(table.PLZ, plzCityArr[0])
            .with(table.CITY, plzCityArr[1]));
    return nextId;
  }

  private String[] splitPlzCity(Object input) {
    Matcher matcher = Pattern.compile("([0-9]{4})\\s(.*)$").matcher(TypeCastUtility.castValue(input, String.class));
    if (matcher.find()) {
      return new String[]{matcher.group(1), matcher.group(2)};
    }
    else {
      throw new ProcessingException("Could not parse plzCity: " + input);
    }
  }

  private Date getFirstEntityDate() {
    Entity table = Entity.ENTITY;
    Field<Date> firstEntityDate = DSL.min(table.ENTITY_DATE).as("FIRST_ENTITY_DATE");

    return DSL.using(SQL.getConnection(), SQLDialect.DERBY).select(firstEntityDate)
        .from(table).fetchOne().get(firstEntityDate);
  }

  private BigDecimal createPartner(String name, String description, Date startDate) {
    Partner table = Partner.PARTNER;
    BigDecimal nextId = new BigDecimal(SQL.getSequenceNextval(ISequenceTable.TABLE_NAME));

    DSL.using(SQL.getConnection(), SQLDialect.DERBY).executeInsert(
        table.newRecord()
            .with(table.PARTNER_NR, nextId)
            .with(table.NAME, name)
            .with(table.DESCRIPTION, description)
            .with(table.START_DATE, startDate));
    return nextId;
  }

  private BigDecimal findTaxGroup(Date date) {
    TaxGroup tg = TaxGroup.TAX_GROUP;

    return DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .select(tg.TAX_GROUP_NR)
        .from(tg)
        .where(tg.START_DATE.le(date).and(tg.END_DATE.ge(date)))
        .fetchOne().get(tg.TAX_GROUP_NR);
  }

}

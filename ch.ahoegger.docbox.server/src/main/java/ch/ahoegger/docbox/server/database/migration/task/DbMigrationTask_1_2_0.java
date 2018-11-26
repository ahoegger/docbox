package ch.ahoegger.docbox.server.database.migration.task;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.holders.Holder;
import org.eclipse.scout.rt.platform.holders.NVPair;
import org.eclipse.scout.rt.platform.util.TypeCastUtility;
import org.eclipse.scout.rt.server.jdbc.SQL;

import ch.ahoegger.docbox.or.definition.table.IAddressTable;
import ch.ahoegger.docbox.or.definition.table.IEmployeeTable;
import ch.ahoegger.docbox.or.definition.table.IEmployeeTaxGroupTable;
import ch.ahoegger.docbox.or.definition.table.IEmployerTable;
import ch.ahoegger.docbox.or.definition.table.IMigrationTable;
import ch.ahoegger.docbox.or.definition.table.IPayslipTable;
import ch.ahoegger.docbox.or.definition.table.ISequenceTable;
import ch.ahoegger.docbox.or.definition.table.IStatementTable;
import ch.ahoegger.docbox.server.database.migration.ITableDescription;
import ch.ahoegger.docbox.server.database.migration.MigrationService;
import ch.ahoegger.docbox.server.database.migration.MigrationUtility;
import ch.ahoegger.docbox.server.hr.AddressFormData;
import ch.ahoegger.docbox.server.or.generator.table.AddressTableStatement;
import ch.ahoegger.docbox.server.or.generator.table.EmployeeTaxGroupTableStatement;
import ch.ahoegger.docbox.server.or.generator.table.EmployerTableStatement;
import ch.ahoegger.docbox.server.or.generator.table.MigrationTableStatement;
import ch.ahoegger.docbox.server.or.generator.table.StatementTableStatement;
import ch.ahoegger.docbox.server.util.BigDecimalUtilitiy;

/**
 * <h3>{@link DbMigrationTask_1_2_0}</h3>
 *
 * @author aho
 */
public class DbMigrationTask_1_2_0 extends AbstractDbMigrationTask {

  public static final BigDecimal TARGET_VERSION = BigDecimal.valueOf(120);

  // TODO EMPLOYEE_TAX_GROUP
  @Override
  BigDecimal getVersion() {
    return TARGET_VERSION;
  }

  @Override
  void runMigration() {
    createMigrationTable();
    migratePostingGroupTable();
    createAddressTable();
    createEmployerTable();
    createEmployeeTaxGroupTable();
    createStatementTable();

    migrateEmloyeeTable();
    migratePayslipTable();
    BEANS.get(MigrationService.class).insert(TARGET_VERSION);
  }

  protected void createMigrationTable() {
    ITableDescription tableDesc = MigrationUtility.getTableDesription(IMigrationTable.TABLE_NAME);
    if (tableDesc == null) {
      // create
      SQL.update(BEANS.get(MigrationTableStatement.class).getCreateTable());
    }
  }

  protected void migratePostingGroupTable() {
    ITableDescription postingGroupDesc = MigrationUtility.getTableDesription("POSTING_GROUP");
    if (postingGroupDesc != null) {
      SQL.update("RENAME TABLE POSTING_GROUP TO PAYSLIP");
      SQL.update("RENAME COLUMN PAYSLIP.POSTING_GROUP_NR TO PAYSLIP_NR");
      SQL.update("RENAME COLUMN ENTITY.POSTING_GROUP_NR TO PAYSLIP_NR");
    }
  }

  protected void createAddressTable() {
    ITableDescription tableDesc = MigrationUtility.getTableDesription(IAddressTable.TABLE_NAME);
    if (tableDesc == null) {
      // create
      SQL.update(BEANS.get(AddressTableStatement.class).getCreateTable());
    }
  }

  protected void createEmployerTable() {
    ITableDescription tableDesc = MigrationUtility.getTableDesription(IEmployerTable.TABLE_NAME);
    if (tableDesc == null) {
      // create
      SQL.update(BEANS.get(EmployerTableStatement.class).getCreateTable());
    }
  }

  protected void createEmployeeTaxGroupTable() {
    ITableDescription tableDesc = MigrationUtility.getTableDesription(IEmployeeTaxGroupTable.TABLE_NAME);
    if (tableDesc == null) {
      // create
      SQL.update(BEANS.get(EmployeeTaxGroupTableStatement.class).getCreateTable());
    }
  }

  protected void createStatementTable() {
    ITableDescription tableDesc = MigrationUtility.getTableDesription(IStatementTable.TABLE_NAME);
    if (tableDesc == null) {
      // create
      SQL.update(BEANS.get(StatementTableStatement.class).getCreateTable());
    }
  }

  protected void migrateEmloyeeTable() {
    ITableDescription tableDesc = MigrationUtility.getTableDesription(IEmployeeTable.TABLE_NAME);
    if (!tableDesc.hasColumn(IEmployeeTable.ADDRESS_NR)) {
      SQL.update("ALTER TABLE EMPLOYEE ADD " + IEmployeeTable.ADDRESS_NR + " BIGINT DEFAULT -1 NOT NULL");
    }
    if (!tableDesc.hasColumn(IEmployeeTable.EMPLOYER_NR)) {
      SQL.update("ALTER TABLE EMPLOYEE ADD " + IEmployeeTable.EMPLOYER_NR + " BIGINT DEFAULT -1 NOT NULL");
    }
    if (!tableDesc.hasColumn(IEmployeeTable.TAX_TYPE)) {
      SQL.update("ALTER TABLE EMPLOYEE ADD " + IEmployeeTable.TAX_TYPE + " BIGINT DEFAULT 31 NOT NULL");
    }
    _extractEmployeeAddresses();
    _extractEmployer();

  }

  protected void migratePayslipTable() {
    ITableDescription tableDesc = MigrationUtility.getTableDesription(IPayslipTable.TABLE_NAME);
    if (!tableDesc.hasColumn(IPayslipTable.STATEMENT_NR)) {
      SQL.update("ALTER TABLE " + IPayslipTable.TABLE_NAME + " ADD " + IPayslipTable.STATEMENT_NR + " BIGINT DEFAULT -1 NOT NULL");
    }
    if (!tableDesc.hasColumn(IPayslipTable.EMPLOYER_NR)) {
      SQL.update("ALTER TABLE " + IPayslipTable.TABLE_NAME + " ADD " + IPayslipTable.EMPLOYER_NR + " BIGINT DEFAULT -1 NOT NULL");
    }
    Object[][] result = SQL.select("SELECT PARTNER_NR FROM EMPLOYEE");
    Arrays.stream(result).forEach(row -> migratePayslipTable(TypeCastUtility.castValue(row[0], BigDecimal.class)));
    // remove unnessesary columns

    SQL.update("ALTER TABLE " + IPayslipTable.TABLE_NAME + " DROP COLUMN STATEMENT_DATE");
    SQL.update("ALTER TABLE " + IPayslipTable.TABLE_NAME + " DROP COLUMN WORKING_HOURS");

    SQL.update("ALTER TABLE " + IPayslipTable.TABLE_NAME + " DROP COLUMN BRUTTO_WAGE");
    SQL.update("ALTER TABLE " + IPayslipTable.TABLE_NAME + " DROP COLUMN NETTO_WAGE");
    SQL.update("ALTER TABLE " + IPayslipTable.TABLE_NAME + " DROP COLUMN SOURCE_TAX");
    SQL.update("ALTER TABLE " + IPayslipTable.TABLE_NAME + " DROP COLUMN SOCIAL_SECURITY_TAX");
    SQL.update("ALTER TABLE " + IPayslipTable.TABLE_NAME + " DROP COLUMN VACATION_EXTRA");
  }

  protected void migratePayslipTable(BigDecimal employeeId) {
    ITableDescription tableDesc = MigrationUtility.getTableDesription(IPayslipTable.TABLE_NAME);
    if (tableDesc.hasColumn("NETTO_WAGE")) {
      Object[][] result = SQL.select("SELECT P.PAYSLIP_NR , E.ACCOUNT_NUMBER, E.HOURLY_WAGE, E.SOCIAL_INSURANCE_RATE,E.SOURCE_TAX_RATE, E.VACATION_EXTRA_RATE, "
          + "P.WORKING_HOURS, P.BRUTTO_WAGE,  P.NETTO_WAGE, P.SOURCE_TAX, P.SOCIAL_SECURITY_TAX, P.VACATION_EXTRA, P.STATEMENT_DATE "
          + "FROM " + IPayslipTable.TABLE_NAME + " P, " + IEmployeeTable.TABLE_NAME + " E "
          + "WHERE P.PARTNER_NR = E.PARTNER_NR "
          + "AND P.PARTNER_NR = :partnerId",
          new NVPair("partnerId", employeeId));
      Arrays.stream(result).forEach(row -> {

        BigDecimal statementId = new BigDecimal(SQL.getSequenceNextval(ISequenceTable.TABLE_NAME));

        SQL.update(" UPDATE " + IPayslipTable.TABLE_NAME + " SET STATEMENT_NR = :statementNr WHERE PAYSLIP_NR = :payslipNr", new NVPair("statementNr", statementId), new NVPair("payslipNr", row[0]));
        SQL.update(" UPDATE " + IPayslipTable.TABLE_NAME + " SET EMPLOYER_NR = :employerNr WHERE PAYSLIP_NR = :payslipNr", new NVPair("employerNr", employeeId), new NVPair("payslipNr", row[0]));
        BigDecimal wage = TypeCastUtility.castValue(row[6], BigDecimal.class).multiply(TypeCastUtility.castValue(row[2], BigDecimal.class)).setScale(2, RoundingMode.HALF_UP);

        BigDecimal expenses = TypeCastUtility.castValue(SQL.select("SELECT SUM(EXPENSE_AMOUNT) FROM ENTITY WHERE PAYSLIP_NR = :payslipNr AND ENTITY_TYPE = 100", new NVPair("payslipNr", row[0]))[0][0],
            BigDecimal.class);
        SQL.insert("INSERT INTO STATEMENT "
            + "(STATEMENT_NR, PARTNER_NR, STATEMENT_DATE, ACCOUNT_NUMBER, TAX_TYPE, HOURLY_WAGE, SOCIAL_INSURANCE_RATE, SOURCE_TAX_RATE, VACATION_EXTRA_RATE, WORKING_HOURS, WAGE, BRUTTO_WAGE, NETTO_WAGE, NETTO_WAGE_PAYOUT, SOURCE_TAX, SOCIAL_SECURITY_TAX, VACATION_EXTRA, EXPENSES ) VALUES "
            + "(:statementNr, :partnerNr, :statementDate, :accountNumber, :taxType, :hourlyWage, :socialInsuranceRate,  :sourceTaxRate,  :vacationExtraRate,  :workingHours, :wage, :bruttoWage, :nettoWage, :netoWagePayout, :sourceTax, :socialSecurityTax, :vactionExtra, :expenses)",
            new NVPair("statementNr", statementId),
            new NVPair("partnerNr", employeeId),
            new NVPair("accountNumber", row[1]),
            new NVPair("taxType", 31),
            new NVPair("hourlyWage", row[2]),
            new NVPair("socialInsuranceRate", row[3]),
            new NVPair("sourceTaxRate", TypeCastUtility.castValue(row[4], BigDecimal.class).multiply(BigDecimal.valueOf(-1l))),
            new NVPair("vacationExtraRate", row[5]),
            new NVPair("workingHours", row[6]),
            new NVPair("wage", wage),
            new NVPair("bruttoWage", row[7]),
            new NVPair("nettoWage", row[8]),
            new NVPair("netoWagePayout", BigDecimalUtilitiy.financeRound(TypeCastUtility.castValue(row[8], BigDecimal.class), BigDecimal.valueOf(0.05), RoundingMode.UP)),
            new NVPair("sourceTax", TypeCastUtility.castValue(row[9], BigDecimal.class).multiply(BigDecimal.valueOf(-1l))),
            new NVPair("socialSecurityTax", TypeCastUtility.castValue(row[10], BigDecimal.class).multiply(BigDecimal.valueOf(-1l))),
            new NVPair("vactionExtra", row[11]),
            new NVPair("statementDate", row[12]),
            new NVPair("expenses", expenses));
      });
    }

  }

  private void _extractEmployeeAddresses() {
    // entries
    Object[][] result = SQL.select("SELECT PARTNER_NR,  ADDRESS_LINE1, ADDRESS_LINE2 FROM EMPLOYEE");
    Arrays.stream(result).forEach(row -> {
      BigDecimal addressId = new BigDecimal(SQL.getSequenceNextval(ISequenceTable.TABLE_NAME));
      SQL.update(" UPDATE EMPLOYEE SET ADDRESS_NR = :addressId WHERE PARTNER_NR = :partnerNr", new NVPair("addressId", addressId), new NVPair("partnerNr", row[0]));

      AddressFormData address = new AddressFormData().withAddressNr(addressId).withLine1(TypeCastUtility.castValue(row[1], String.class));
      Matcher matcher = Pattern.compile("([0-9]{4})\\s(.*)$").matcher(TypeCastUtility.castValue(row[2], String.class));
      if (matcher.find()) {
        address.withPlz(matcher.group(1));
        address.withCity(matcher.group(2));

      }
      else {
        throw new ProcessingException();
      }
      SQL.insert("INSERT INTO ADDRESS (ADDRESS_NR, LINE_1, LINE_2, PLZ, CITY) VALUES (:addressId, :line1, :line2, :plz, :city)", address);
    });
    // remove columns
    SQL.update("ALTER TABLE EMPLOYEE DROP COLUMN ADDRESS_LINE1");
    SQL.update("ALTER TABLE EMPLOYEE DROP COLUMN ADDRESS_LINE2");
  }

  private BigDecimal m_employerNr;

  private void _extractEmployer() {
    final Holder<BigDecimal> employerIdHolder = new Holder<>();
    Object[][] result = SQL.select("SELECT PARTNER_NR,  EMPLOYER_ADDRESS_LINE1, EMPLOYER_ADDRESS_LINE2, EMPLOYER_ADDRESS_LINE3, EMPLOYER_EMAIL, EMPLOYER_PHONE FROM EMPLOYEE");
    Arrays.stream(result).forEach(row -> {
      if (employerIdHolder.getValue() == null) {
        employerIdHolder.setValue(new BigDecimal(SQL.getSequenceNextval(ISequenceTable.TABLE_NAME)));
        BigDecimal addressId = new BigDecimal(SQL.getSequenceNextval(ISequenceTable.TABLE_NAME));
        AddressFormData address = new AddressFormData().withAddressNr(addressId).withLine1(TypeCastUtility.castValue(row[2], String.class));
        parsePlzCity(address, TypeCastUtility.castValue(row[3], String.class));
        SQL.insert("INSERT INTO ADDRESS (ADDRESS_NR, LINE_1, LINE_2, PLZ, CITY) VALUES (:addressId, :line1, :line2, :plz, :city)", address);

        SQL.insert("INSERT INTO EMPLOYER (EMPLOYER_NR, ADDRESS_NR, NAME, EMAIL, PHONE) VALUES (:{employerNr},:{addressNr},:{name},:{email},:{phone})",
            new NVPair("employerNr", employerIdHolder.getValue()),
            new NVPair("addressNr", addressId),
            new NVPair("name", TypeCastUtility.castValue(row[1], String.class)),
            new NVPair("email", TypeCastUtility.castValue(row[4], String.class)),
            new NVPair("phone", TypeCastUtility.castValue(row[5], String.class)));
      }

      SQL.update(" UPDATE EMPLOYEE SET EMPLOYER_NR = :{employerNr} WHERE PARTNER_NR = :{partnerNr}", new NVPair("employerNr", employerIdHolder.getValue()), new NVPair("partnerNr", row[0]));

    });
    // remove columns
    SQL.update("ALTER TABLE EMPLOYEE DROP COLUMN EMPLOYER_ADDRESS_LINE1");
    SQL.update("ALTER TABLE EMPLOYEE DROP COLUMN EMPLOYER_ADDRESS_LINE2");
    SQL.update("ALTER TABLE EMPLOYEE DROP COLUMN EMPLOYER_EMAIL");
    SQL.update("ALTER TABLE EMPLOYEE DROP COLUMN EMPLOYER_PHONE");

    m_employerNr = employerIdHolder.getValue();
  }

  private void parsePlzCity(AddressFormData addressFormData, String raw) {
    Matcher matcher = Pattern.compile("([0-9]{4})\\s(.*)$").matcher(raw);
    if (matcher.find()) {
      addressFormData.withPlz(matcher.group(1));
      addressFormData.withCity(matcher.group(2));

    }
    else {
      throw new ProcessingException();
    }
  }

}

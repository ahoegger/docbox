package ch.ahoegger.docbox.server.database.migration.task;

import java.math.BigDecimal;
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
import ch.ahoegger.docbox.or.definition.table.IEmployerTable;
import ch.ahoegger.docbox.or.definition.table.IMigrationTable;
import ch.ahoegger.docbox.or.definition.table.ISequenceTable;
import ch.ahoegger.docbox.or.definition.table.IStatementTable;
import ch.ahoegger.docbox.server.database.migration.ITableDescription;
import ch.ahoegger.docbox.server.database.migration.MigrationService;
import ch.ahoegger.docbox.server.database.migration.MigrationUtility;
import ch.ahoegger.docbox.server.hr.AddressFormData;
import ch.ahoegger.docbox.server.or.generator.table.AddressTableStatement;
import ch.ahoegger.docbox.server.or.generator.table.EmployerTableStatement;
import ch.ahoegger.docbox.server.or.generator.table.MigrationTableStatement;
import ch.ahoegger.docbox.server.or.generator.table.StatementTableStatement;
import ch.ahoegger.docbox.shared.hr.tax.TaxCodeType.SourceTax;

/**
 * <h3>{@link DbMigrationTask_1_2_0}</h3>
 *
 * @author aho
 */
public class DbMigrationTask_1_2_0 extends AbstractDbMigrationTask {

  public static final BigDecimal TARGET_VERSION = BigDecimal.valueOf(120);

  @Override
  BigDecimal getVersion() {
    return TARGET_VERSION;
  }

  @Override
  void runMigration() {
    createMigrationTable();
    migratePayslipTable();
    createAddressTable();
    createEmployerTable();
    createStatementTable();
    migrateEmloyeeTable();
    BEANS.get(MigrationService.class).insert(TARGET_VERSION);
  }

  protected void createMigrationTable() {
    ITableDescription tableDesc = MigrationUtility.getTableDesription(IMigrationTable.TABLE_NAME);
    if (tableDesc == null) {
      // create
      SQL.update(BEANS.get(MigrationTableStatement.class).getCreateTable());
    }
  }

  protected void migratePayslipTable() {
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
      SQL.update("ALTER TABLE EMPLOYEE ADD " + IEmployeeTable.TAX_TYPE + " BIGINT DEFAULT :{taxType} NOT NULL", new NVPair("taxType", SourceTax.ID));
    }
    _extractEmployeeAddresses();
    _extractEmployer();

  }

  private void _extractEmployeeAddresses() {
    // entries
    Object[][] result = SQL.select("SELECT PARTNER_NR,  ADDRESS_LINE1, ADDRESS_LINE2 FROM EMPLOYEE");
    Arrays.stream(result).forEach(row -> {
      BigDecimal addressId = new BigDecimal(SQL.getSequenceNextval(ISequenceTable.TABLE_NAME));
      SQL.update(" UPDATE EMPLOYEE SET ADDRESS_NR = :{addressId} WHERE PARTNER_NR = :{partnerNr}", new NVPair("addressId", addressId), new NVPair("partnerNr", row[0]));

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

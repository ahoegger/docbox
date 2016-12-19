package ch.ahoegger.docbox.server.hr.employee;

import java.math.BigDecimal;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.holders.BooleanHolder;
import org.eclipse.scout.rt.platform.holders.NVPair;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.server.jdbc.SQL;

import ch.ahoegger.docbox.shared.backup.IBackupService;
import ch.ahoegger.docbox.shared.hr.employee.EmployeeFormData;
import ch.ahoegger.docbox.shared.hr.employee.IEmployeeService;
import ch.ahoegger.docbox.shared.hr.employee.IEmployeeTable;
import ch.ahoegger.docbox.shared.hr.employer.EmployeeSearchFormData;
import ch.ahoegger.docbox.shared.hr.employer.EmployeeTableData;
import ch.ahoegger.docbox.shared.hr.employer.EmployeeTableData.EmployeeTableRowData;
import ch.ahoegger.docbox.shared.partner.IPartnerService;
import ch.ahoegger.docbox.shared.partner.IPartnerTable;
import ch.ahoegger.docbox.shared.partner.PartnerFormData;
import ch.ahoegger.docbox.shared.util.SqlFramentBuilder;

/**
 * <h3>{@link EmployeeService}</h3>
 *
 * @author Andreas Hoegger
 */
public class EmployeeService implements IEmployeeService, IEmployeeTable {

  @Override
  public EmployeeTableData getTableData(EmployeeSearchFormData formData) {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("SELECT ").append(SqlFramentBuilder.columnsAliased(TABLE_ALIAS, PARTNER_NR, FIRST_NAME, LAST_NAME, ADDRESS_LINE1, ADDRESS_LINE2, AHV_NUMBER, ACCOUNT_NUMBER, HOURLY_WAGE)).append(", ")
        .append(SqlFramentBuilder.columnsAliased(IPartnerTable.TABLE_ALIAS, IPartnerTable.NAME, IPartnerTable.START_DATE, IPartnerTable.END_DATE))
        .append(" FROM ").append(TABLE_NAME).append(" AS ").append(TABLE_ALIAS).append(" ");

    // join with partner
    statementBuilder.append(" LEFT OUTER JOIN ").append(IPartnerTable.TABLE_NAME).append(" AS ").append(IPartnerTable.TABLE_ALIAS)
        .append(" ON ").append(SqlFramentBuilder.columnsAliased(TABLE_ALIAS, PARTNER_NR)).append(" = ").append(SqlFramentBuilder.columnsAliased(IPartnerTable.TABLE_ALIAS, IPartnerTable.PARTNER_NR));

    statementBuilder.append(" ").append(SqlFramentBuilder.WHERE_DEFAULT);

    // search criteria firstname
    if (StringUtility.hasText(formData.getFirstName().getValue())) {
      statementBuilder.append(" AND ").append(SqlFramentBuilder.whereStringContains(FIRST_NAME, formData.getFirstName().getValue()));
    }
    if (StringUtility.hasText(formData.getLastName().getValue())) {
      statementBuilder.append(" AND ").append(SqlFramentBuilder.whereStringContains(FIRST_NAME, formData.getLastName().getValue()));
    }
    // seach criteria active
    if (formData.getActiveBox().getValue() != null) {
      switch (formData.getActiveBox().getValue()) {
        case TRUE:
          statementBuilder.append(" AND (").append(SqlFramentBuilder.columnsAliased(IPartnerTable.TABLE_ALIAS, IPartnerTable.END_DATE)).append(" >= ").append("CURRENT_DATE")
              .append(" OR ").append(SqlFramentBuilder.columnsAliased(IPartnerTable.TABLE_ALIAS, IPartnerTable.END_DATE)).append(" IS NULL)");
          break;
        case FALSE:
          statementBuilder.append(" AND ").append(SqlFramentBuilder.columnsAliased(IPartnerTable.TABLE_ALIAS, IPartnerTable.END_DATE)).append(" < ").append("CURRENT_DATE");
          break;
      }
    }

    statementBuilder.append(" INTO ")
        .append(":{td.").append(EmployeeTableRowData.partnerId).append("}, ")
        .append(":{td.").append(EmployeeTableRowData.firstName).append("}, ")
        .append(":{td.").append(EmployeeTableRowData.lastName).append("}, ")
        .append(":{td.").append(EmployeeTableRowData.addressLine1).append("}, ")
        .append(":{td.").append(EmployeeTableRowData.addressLine2).append("}, ")
        .append(":{td.").append(EmployeeTableRowData.aHVNumber).append("}, ")
        .append(":{td.").append(EmployeeTableRowData.accountNumber).append("}, ")
        .append(":{td.").append(EmployeeTableRowData.hourlyWage).append("}, ")
        .append(":{td.").append(EmployeeTableRowData.displayName).append("}, ")
        .append(":{td.").append(EmployeeTableRowData.startDate).append("}, ")
        .append(":{td.").append(EmployeeTableRowData.endDate).append("} ");

    EmployeeTableData tableData = new EmployeeTableData();
    SQL.selectInto(statementBuilder.toString(),
        new NVPair("td", tableData),
        formData);
    return tableData;
  }

  @Override
  public EmployeeFormData prepareCreate(EmployeeFormData formData) {

    // TODO [aho] add business logic here.
    return formData;
  }

  @Override
  public EmployeeFormData create(EmployeeFormData formData) {
    IPartnerService partnerService = BEANS.get(IPartnerService.class);
    BigDecimal partnerId = formData.getPartnerId();
    if (partnerId == null) {
      // create partner
      PartnerFormData partnerData = new PartnerFormData();
      partnerService.prepareCreate(partnerData);
      formData.getPartnerGroupBox();
      partnerData.getPartnerBox().getName().setValue(formData.getPartnerGroupBox().getName().getValue());
      partnerData.getPartnerBox().getDescription().setValue(formData.getPartnerGroupBox().getDescription().getValue());
      partnerData.getPartnerBox().getStartDate().setValue(formData.getPartnerGroupBox().getStartDate().getValue());
      partnerData.getPartnerBox().getEndDate().setValue(formData.getPartnerGroupBox().getEndDate().getValue());
      partnerService.create(partnerData);
      partnerId = partnerData.getPartnerId();
    }

//    public static String FIRST_NAME = "FIRST_NAME";
//    public static int FIRST_NAME_LENGTH = 200;
//
//    public static String LAST_NAME = "LAST_NAME";
//    public static int LAST_NAME_LENGTH = 200;
//
//    public static String ADDRESS_LINE1 = "ADDRESS_LINE1";
//    public static int ADDRESS_LINE1_LENGTH = 1200;
//
//    public static String ADDRESS_LINE2 = "ADDRESS_LINE2";
//    public static int ADDRESS_LINE2_LENGTH = 1200;
//
//    public static String AHV_NUMBER = "AHV_NUMBER";
//    public static int AHV_NUMBER_LENGTH = 16;
//
//    public static String ACCOUNT_NUMBER = "ACCOUNT_NUMBER";
//    public static int ACCOUNT_NUMBER_LENGTH = 128;
//
//    public static String HOURLY_WAGE = "HOURLY_WAGE";
//    public static Double HOURLY_WAGE_MIN = Double.valueOf(0);
//    public static Double HOURLY_WAGE_MAX = Double.valueOf(200);

    formData.setPartnerId(partnerId);
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("INSERT INTO ").append(TABLE_NAME);
    statementBuilder.append(" (").append(SqlFramentBuilder.columns(PARTNER_NR, FIRST_NAME, LAST_NAME, ADDRESS_LINE1, ADDRESS_LINE2, AHV_NUMBER, ACCOUNT_NUMBER, HOURLY_WAGE)).append(")");
    statementBuilder.append(" VALUES (:partnerId, :firstName, :lastName, :addressLine1, :addressLine2, :ahvNumber, :accountNumber, :hourlyWage )");
    SQL.insert(statementBuilder.toString(), formData);

    // notify backup needed
    BEANS.get(IBackupService.class).notifyModification();

    return formData;
  }

  @Override
  public EmployeeFormData load(EmployeeFormData formData) {
    BooleanHolder exists = new BooleanHolder();
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("SELECT TRUE, ").append(SqlFramentBuilder.columns(SqlFramentBuilder.columnsAliased(IPartnerTable.TABLE_ALIAS, IPartnerTable.NAME, IPartnerTable.START_DATE, IPartnerTable.END_DATE, IPartnerTable.DESCRIPTION)))
        .append(", ").append(SqlFramentBuilder.columnsAliased(TABLE_ALIAS, FIRST_NAME, LAST_NAME, ADDRESS_LINE1, ADDRESS_LINE2, AHV_NUMBER, ACCOUNT_NUMBER, HOURLY_WAGE));
    statementBuilder.append(" FROM ").append(TABLE_NAME).append(" AS ").append(TABLE_ALIAS);
    // join
    statementBuilder.append(" JOIN ").append(IPartnerTable.TABLE_NAME).append(" AS ").append(IPartnerTable.TABLE_ALIAS)
        .append(" ON ").append(SqlFramentBuilder.columnsAliased(TABLE_ALIAS, PARTNER_NR)).append(" = ").append(SqlFramentBuilder.columnsAliased(IPartnerTable.TABLE_ALIAS, IPartnerTable.PARTNER_NR));
    statementBuilder.append(" WHERE ").append(SqlFramentBuilder.columnsAliased(TABLE_ALIAS, PARTNER_NR)).append(" = :partnerId");
    statementBuilder.append(" INTO :exists, :name, :startDate, :endDate, :description")
        .append(", :firstName, :lastName, :addressLine1,:addressLine2, :ahvNumber, :accountNumber, :hourlyWage");
    SQL.selectInto(statementBuilder.toString(),
        new NVPair("exists", exists),
        formData, formData.getPartnerGroupBox());
    if (exists.getValue() == null) {
      return null;
    }
    return formData;
  }

  @Override
  public EmployeeFormData store(EmployeeFormData formData) {

    // TODO [aho] add business logic here.
    return formData;
  }
}

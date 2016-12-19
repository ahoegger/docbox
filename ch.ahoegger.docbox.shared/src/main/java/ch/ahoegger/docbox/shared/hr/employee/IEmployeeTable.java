package ch.ahoegger.docbox.shared.hr.employee;

import java.math.BigDecimal;

import ch.ahoegger.docbox.shared.partner.IPartnerTable;

/**
 * <h3>{@link IEmployeeTable}</h3>
 *
 * @author Andreas Hoegger
 */
public interface IEmployeeTable {
  public static String TABLE_ALIAS = "EMP";
  public static String TABLE_NAME = "EMPLOYEE";

  // columns

  public static String PARTNER_NR = IPartnerTable.PARTNER_NR;

  public static String FIRST_NAME = "FIRST_NAME";
  public static int FIRST_NAME_LENGTH = 200;

  public static String LAST_NAME = "LAST_NAME";
  public static int LAST_NAME_LENGTH = 200;

  public static String ADDRESS_LINE1 = "ADDRESS_LINE1";
  public static int ADDRESS_LINE1_LENGTH = 1200;

  public static String ADDRESS_LINE2 = "ADDRESS_LINE2";
  public static int ADDRESS_LINE2_LENGTH = 1200;

  public static String AHV_NUMBER = "AHV_NUMBER";
  public static int AHV_NUMBER_LENGTH = 16;

  public static String ACCOUNT_NUMBER = "ACCOUNT_NUMBER";
  public static int ACCOUNT_NUMBER_LENGTH = 128;

  public static String HOURLY_WAGE = "HOURLY_WAGE";
  public static BigDecimal HOURLY_WAGE_MIN = BigDecimal.valueOf(0d);
  public static BigDecimal HOURLY_WAGE_MAX = BigDecimal.valueOf(200d);

}

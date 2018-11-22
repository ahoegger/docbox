package ch.ahoegger.docbox.or.definition.table;

import java.math.BigDecimal;

/**
 * <h3>{@link IEmployeeTable}</h3>
 *
 * @author Andreas Hoegger
 */
public interface IEmployeeTable {
  public static String TABLE_NAME = "EMPLOYEE";

  // columns

  public static String PARTNER_NR = IPartnerTable.PARTNER_NR;

  public static String EMPLOYER_NR = IEmployerTable.EMPLOYER_NR;

  public static String FIRST_NAME = "FIRST_NAME";
  public static int FIRST_NAME_LENGTH = 200;

  public static String LAST_NAME = "LAST_NAME";
  public static int LAST_NAME_LENGTH = 200;

  public static String ADDRESS_NR = IAddressTable.ADDRESS_NR;

  public static String AHV_NUMBER = "AHV_NUMBER";
  public static int AHV_NUMBER_LENGTH = 16;

  public static String BIRTHDAY = "BIRTHDAY";

  public static String ACCOUNT_NUMBER = "ACCOUNT_NUMBER";
  public static int ACCOUNT_NUMBER_LENGTH = 128;

  public static String HOURLY_WAGE = "HOURLY_WAGE";
  public static BigDecimal HOURLY_WAGE_MIN = BigDecimal.valueOf(0d);
  public static BigDecimal HOURLY_WAGE_MAX = BigDecimal.valueOf(200d);

  public static String SOCIAL_INSURANCE_RATE = "SOCIAL_INSURANCE_RATE";
  public static BigDecimal SOCIAL_INSURANCE_RATE_MIN = BigDecimal.valueOf(0d);
  public static BigDecimal SOCIAL_INSURANCE_RATE_MAX = BigDecimal.valueOf(100d);

  public static String SOURCE_TAX_RATE = "SOURCE_TAX_RATE";
  public static BigDecimal SOURCE_TAX_RATE_MIN = BigDecimal.valueOf(0d);
  public static BigDecimal SOURCE_TAX_RATE_MAX = BigDecimal.valueOf(100d);

  public static String VACATION_EXTRA_RATE = "VACATION_EXTRA_RATE";
  public static BigDecimal VACATION_EXTRA_RATE_MIN = BigDecimal.valueOf(0d);
  public static BigDecimal VACATION_EXTRA_RATE_MAX = BigDecimal.valueOf(100d);

}

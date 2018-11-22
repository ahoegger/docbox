package ch.ahoegger.docbox.or.definition.table;

import java.math.BigDecimal;

/**
 * <h3>{@link IStatementTable}</h3>
 *
 * @author Andreas Hoegger
 */
public interface IStatementTable {
  String TABLE_NAME = "STATEMENT";

  // columns

  String STATEMENT_NR = "STATEMENT_NR";

  String PARTNER_NR = IEmployeeTable.PARTNER_NR;

  String STATEMENT_DATE = "STATEMENT_DATE";

  String ACCOUNT_NUMBER = "ACCOUNT_NUMBER";
  int ACCOUNT_NUMBER_LENGTH = 128;

  String TAX_TYPE = "TAX_TYPE";

  String HOURLY_WAGE = "HOURLY_WAGE";
  BigDecimal HOURLY_WAGE_MIN = BigDecimal.valueOf(0d);
  BigDecimal HOURLY_WAGE_MAX = BigDecimal.valueOf(200d);

  String SOCIAL_INSURANCE_RATE = "SOCIAL_INSURANCE_RATE";
  BigDecimal SOCIAL_INSURANCE_RATE_MIN = BigDecimal.valueOf(0d);
  BigDecimal SOCIAL_INSURANCE_RATE_MAX = BigDecimal.valueOf(100d);

  String SOURCE_TAX_RATE = "SOURCE_TAX_RATE";
  BigDecimal SOURCE_TAX_RATE_MIN = BigDecimal.valueOf(0d);
  BigDecimal SOURCE_TAX_RATE_MAX = BigDecimal.valueOf(100d);

  String VACATION_EXTRA_RATE = "VACATION_EXTRA_RATE";
  BigDecimal VACATION_EXTRA_RATE_MIN = BigDecimal.valueOf(0d);
  BigDecimal VACATION_EXTRA_RATE_MAX = BigDecimal.valueOf(100d);

  public static String WORKING_HOURS = "WORKING_HOURS";
  public static BigDecimal WORKING_HOURS_MIN = BigDecimal.valueOf(0d);
  public static BigDecimal WORKING_HOURS_MAX = BigDecimal.valueOf(2000d);

  public static String BRUTTO_WAGE = "BRUTTO_WAGE";
  public static BigDecimal BRUTTO_WAGE_MIN = BigDecimal.valueOf(0d);
  public static BigDecimal BRUTTO_WAGE_MAX = BigDecimal.valueOf(20000d);

  public static String NETTO_WAGE = "NETTO_WAGE";
  public static BigDecimal NETTO_WAGE_MIN = BigDecimal.valueOf(0d);
  public static BigDecimal NETTO_WAGE_MAX = BigDecimal.valueOf(20000d);

  public static String SOURCE_TAX = "SOURCE_TAX";
  public static BigDecimal SOURCE_TAX_MIN = BigDecimal.valueOf(0d);
  public static BigDecimal SOURCE_TAX_MAX = BigDecimal.valueOf(99999d);

  public static String SOCIAL_SECURITY_TAX = "SOCIAL_SECURITY_TAX";
  public static BigDecimal SOCIAL_SECURITY_TAX_MIN = BigDecimal.valueOf(0d);
  public static BigDecimal SOCIAL_SECURITY_TAX_MAX = BigDecimal.valueOf(99999d);

  public static String VACATION_EXTRA = "VACATION_EXTRA";
  public static BigDecimal VACATION_EXTRA_TAX_MIN = BigDecimal.valueOf(0d);
  public static BigDecimal VACATION_EXTRA_TAX_MAX = BigDecimal.valueOf(2000d);
}

package ch.ahoegger.docbox.or.definition.table;

import java.math.BigDecimal;

/**
 * <h3>{@link IEmployeeTable}</h3>
 *
 * @author Andreas Hoegger
 */
public interface IEmployeeTable {
  String TABLE_NAME = "EMPLOYEE";

  // columns

  String PARTNER_NR = IPartnerTable.PARTNER_NR;

  String EMPLOYER_NR = IEmployerTable.EMPLOYER_NR;

  String FIRST_NAME = "FIRST_NAME";
  int FIRST_NAME_LENGTH = 200;

  String LAST_NAME = "LAST_NAME";
  int LAST_NAME_LENGTH = 200;

  static String ADDRESS_NR = IAddressTable.ADDRESS_NR;

  String AHV_NUMBER = "AHV_NUMBER";
  int AHV_NUMBER_LENGTH = 16;

  String BIRTHDAY = "BIRTHDAY";

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

}

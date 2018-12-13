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

  String DOCUMENT_NR = IDocumentTable.DOCUMENT_NR; // FK

  String TAX_TYPE = "TAX_TYPE";

  String STATEMENT_DATE = "STATEMENT_DATE";

  String ACCOUNT_NUMBER = "ACCOUNT_NUMBER";
  int ACCOUNT_NUMBER_LENGTH = 128;

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

  String WORKING_HOURS = "WORKING_HOURS";
  BigDecimal WORKING_HOURS_MIN = BigDecimal.valueOf(0d);
  BigDecimal WORKING_HOURS_MAX = BigDecimal.valueOf(9999999d);

  String WAGE = "WAGE";
  BigDecimal WAGE_MIN = BigDecimal.valueOf(0d);
  BigDecimal WAGE_MAX = BigDecimal.valueOf(9999999d);

  String BRUTTO_WAGE = "BRUTTO_WAGE";
  BigDecimal BRUTTO_WAGE_MIN = BigDecimal.valueOf(0d);
  BigDecimal BRUTTO_WAGE_MAX = BigDecimal.valueOf(9999999d);

  String NETTO_WAGE = "NETTO_WAGE";
  BigDecimal NETTO_WAGE_MIN = BigDecimal.valueOf(0d);
  BigDecimal NETTO_WAGE_MAX = BigDecimal.valueOf(9999999d);

  String NETTO_WAGE_PAYOUT = "NETTO_WAGE_PAYOUT";
  BigDecimal NETTO_WAGE_PAYOUT_MIN = BigDecimal.valueOf(0d);
  BigDecimal NETTO_WAGE_PAYOUT_MAX = BigDecimal.valueOf(9999999d);

  String SOURCE_TAX = "SOURCE_TAX";
  BigDecimal SOURCE_TAX_MIN = BigDecimal.valueOf(0d);
  BigDecimal SOURCE_TAX_MAX = BigDecimal.valueOf(9999999d);

  String SOCIAL_INSURANCE_TAX = "SOCIAL_INSURANCE_TAX";
  BigDecimal SOCIAL_SECURITY_TAX_MIN = BigDecimal.valueOf(0d);
  BigDecimal SOCIAL_SECURITY_TAX_MAX = BigDecimal.valueOf(9999999d);

  String VACATION_EXTRA = "VACATION_EXTRA";
  BigDecimal VACATION_EXTRA_TAX_MIN = BigDecimal.valueOf(0d);
  BigDecimal VACATION_EXTRA_TAX_MAX = BigDecimal.valueOf(9999999d);

  String EXPENSES = "EXPENSES";
  BigDecimal EXPENSES_MIN = BigDecimal.valueOf(0d);
  BigDecimal EXPENSES_MAX = BigDecimal.valueOf(9999999d);

}

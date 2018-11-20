package ch.ahoegger.docbox.or.definition.table;

import java.math.BigDecimal;

/**
 * <h3>{@link IEntityTable}</h3>
 *
 * @author Andreas Hoegger
 */
public interface IEntityTable {
  public static String TABLE_NAME = "ENTITY";

  // columns
  public static String ENTITY_NR = "ENTITY_NR";

  public static String PARTNER_NR = IPartnerTable.PARTNER_NR;

  public static String PAYSLIP_ACCOUNTING_NR = IPayslipAccountingTable.PAYSLIP_ACCOUNTING_NR;

  public static String ENTITY_TYPE = "ENTITY_TYPE";

  public static String DESCRIPTION = "DESCRIPTION";
  public static int DESCRIPTION_LENGTH = 2400;

  public static String ENTITY_DATE = "ENTITY_DATE";

  public static String WORKING_HOURS = "WORKING_HOURS";
  public static BigDecimal WORKING_HOURS_MIN = BigDecimal.valueOf(0d);
  public static BigDecimal WORKING_HOURS_MAX = BigDecimal.valueOf(24d);

  public static String EXPENSE_AMOUNT = "EXPENSE_AMOUNT";
  public static BigDecimal EXPENSE_AMOUNT_MIN = BigDecimal.valueOf(0d);
  public static BigDecimal EXPENSE_AMOUNT_MAX = BigDecimal.valueOf(999999d);

}

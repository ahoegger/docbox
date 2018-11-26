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
  String ENTITY_NR = "ENTITY_NR";

  String PARTNER_NR = IPartnerTable.PARTNER_NR;

  String PAYSLIP_NR = IPayslipTable.PAYSLIP_NR;

  String ENTITY_TYPE = "ENTITY_TYPE";

  String DESCRIPTION = "DESCRIPTION";
  int DESCRIPTION_LENGTH = 2400;

  String ENTITY_DATE = "ENTITY_DATE";

  String WORKING_HOURS = "WORKING_HOURS";
  BigDecimal WORKING_HOURS_MIN = BigDecimal.valueOf(0d);
  BigDecimal WORKING_HOURS_MAX = BigDecimal.valueOf(24d);

  String EXPENSE_AMOUNT = "EXPENSE_AMOUNT";
  BigDecimal EXPENSE_AMOUNT_MIN = BigDecimal.valueOf(0d);
  BigDecimal EXPENSE_AMOUNT_MAX = BigDecimal.valueOf(999999d);

}

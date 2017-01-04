package ch.ahoegger.docbox.shared.hr.entity;

import java.math.BigDecimal;

import ch.ahoegger.docbox.shared.hr.billing.IPostingGroupTable;
import ch.ahoegger.docbox.shared.partner.IPartnerTable;

/**
 * <h3>{@link IEntityTable}</h3>
 *
 * @author Andreas Hoegger
 */
public interface IEntityTable {
  public static String TABLE_ALIAS = "ENT";
  public static String TABLE_NAME = "ENTITY";

  // columns
  public static String ENTITY_NR = "ENTITY_NR";

  public static String PARTNER_NR = IPartnerTable.PARTNER_NR;

  public static String POSTING_GROUP_NR = IPostingGroupTable.POSTING_GROUP_NR;

  public static String ENTITY_TYPE = "ENTITY_TYPE";

  public static String DESCRIPTION = "DESCRIPTION";
  public static int DESCRIPTION_LENGTH = 2400;

  public static String ENTITY_DATE = "ENTITY_DATE";

  public static String HOURS = "HOURS";
  public static BigDecimal HOURS_MIN = BigDecimal.valueOf(0d);
  public static BigDecimal HOURS_MAX = BigDecimal.valueOf(24d);

  public static String AMOUNT = "AMOUNT";
  public static BigDecimal AMOUNT_MIN = BigDecimal.valueOf(0d);
  public static BigDecimal AMOUNT_MAX = BigDecimal.valueOf(999999d);

}

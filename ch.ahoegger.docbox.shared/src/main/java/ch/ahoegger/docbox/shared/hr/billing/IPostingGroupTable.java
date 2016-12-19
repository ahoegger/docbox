package ch.ahoegger.docbox.shared.hr.billing;

import java.math.BigDecimal;

import ch.ahoegger.docbox.shared.document.IDocumentTable;
import ch.ahoegger.docbox.shared.partner.IPartnerTable;

/**
 * <h3>{@link IPostingGroupTable}</h3>
 *
 * @author Andreas Hoegger
 */
public interface IPostingGroupTable {

  public static String TABLE_ALIAS = "PO_GR";
  public static String TABLE_NAME = "POSTING_GROUP";

  // columns

  public static String POSTING_GROUP_NR = "POSTING_GROUP_NR";

  public static String PARTNER_NR = IPartnerTable.PARTNER_NR;

  public static String DOCUMENT_NR = IDocumentTable.DOCUMENT_NR;

  public static String NAME = "NAME";
  public static int NAME_LENGTH = 200;

  public static String STATEMENT_DATE = "STATEMENT_DATE";

  public static String BRUTTO_WAGE = "BRUTTO_WAGE";
  public static BigDecimal BRUTTO_WAGE_MIN = BigDecimal.valueOf(0d);
  public static BigDecimal BRUTTO_WAGE_MAX = BigDecimal.valueOf(20000d);

  public static String NETTO_WAGE = "NETTO_WAGE";
  public static BigDecimal NETTO_WAGE_MIN = BigDecimal.valueOf(0d);
  public static BigDecimal NETTO_WAGE_MAX = BigDecimal.valueOf(20000d);

  public static String SOURCE_TAX = "SOURCE_TAX";
  public static BigDecimal SOURCE_TAX_MIN = BigDecimal.valueOf(-2000d);
  public static BigDecimal SOURCE_TAX_MAX = BigDecimal.valueOf(0d);

  public static String SOCIAL_SECURITY_TAX = "SOCIAL_SECURITY_TAX";
  public static BigDecimal SOCIAL_SECURITY_TAX_MIN = BigDecimal.valueOf(-2000d);
  public static BigDecimal SOCIAL_SECURITY_TAX_MAX = BigDecimal.valueOf(0d);

  public static String VACATION_EXTRA = "VACATION_EXTRA";
  public static BigDecimal VACATION_EXTRA_TAX_MIN = BigDecimal.valueOf(0d);
  public static BigDecimal VACATION_EXTRA_TAX_MAX = BigDecimal.valueOf(2000d);

}

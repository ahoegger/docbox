package ch.ahoegger.docbox.or.definition.table;

/**
 * <h3>{@link IPartnerTable}</h3>
 *
 * @author Andreas Hoegger
 */
public interface IPartnerTable {

  public static String TABLE_NAME = "PARTNER";

  public static String PARTNER_NR = "PARTNER_NR";
  public static String NAME = "NAME";
  public static int NAME_LENGTH = 1200;

  public static String DESCRIPTION = "DESCRIPTION";
  public static int DESCRIPTION_LENGTH = 2400;
  public static String START_DATE = "START_DATE";
  public static String END_DATE = "END_DATE";
}

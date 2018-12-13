package ch.ahoegger.docbox.or.definition.table;

/**
 * <h3>{@link IEmployerTable}</h3>
 *
 * @author Andreas Hoegger
 */
public interface IEmployerTable {
  public static String TABLE_NAME = "EMPLOYER";

  // columns
  public static String EMPLOYER_NR = "EMPLOYER_NR"; //PK, FK -> IPartnerTable.PARTNER_NR

  public static String ADDRESS_NR = IAddressTable.ADDRESS_NR;

  public static String NAME = "NAME";
  public static int NAME_LENGTH = 200;

  public static String EMAIL = "EMAIL";
  public static int EMAIL_LENGTH = 240;

  public static String PHONE = "PHONE";
  public static int PHONE_LENGTH = 120;
}

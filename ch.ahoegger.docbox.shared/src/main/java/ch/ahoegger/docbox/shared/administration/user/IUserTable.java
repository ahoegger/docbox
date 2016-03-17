package ch.ahoegger.docbox.shared.administration.user;

/**
 * <h3>{@link IUserTable}</h3>
 *
 * @author Andreas Hoegger
 */
public interface IUserTable {

  public static String TABLE_NAME = "DOCBOX_USER";

  public static String USERNAME = "USERNAME";
  public static int USERNAME_LENGTH = 240;
  public static String NAME = "NAME";
  public static int NAME_LENGTH = 240;
  public static String FIRSTNAME = "FIRSTNAME";
  public static int FIRSTNAME_LENGTH = 240;
  public static String PASSWORD = "PASSWORD";
  public static int PASSWORD_LENGTH = 480;

  public static String INSERT_DATE = "INSERT_DATE";
  public static String VALID_DATE = "VALID_DATE";

}

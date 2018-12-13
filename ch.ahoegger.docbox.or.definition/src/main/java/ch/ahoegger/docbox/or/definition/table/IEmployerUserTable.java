package ch.ahoegger.docbox.or.definition.table;

/**
 * <h3>{@link IEmployerUserTable}</h3>
 *
 * @author aho
 */
public interface IEmployerUserTable {
  public static String TABLE_NAME = "EMPLOYER_USER";

  public static String USERNAME = IUserTable.USERNAME; // PK, FK
  public static int USERNAME_LENGTH = IUserTable.USERNAME_LENGTH;
  public static String EMPLOYER_NR = IEmployerTable.EMPLOYER_NR; // PK, FK
}

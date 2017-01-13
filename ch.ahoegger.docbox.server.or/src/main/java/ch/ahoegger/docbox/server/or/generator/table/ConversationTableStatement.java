package ch.ahoegger.docbox.server.or.generator.table;

/**
 * <h3>{@link ConversationTableStatement}</h3>
 *
 * @author Andreas Hoegger
 */
public class ConversationTableStatement implements ITableStatement {
  public static String TABLE_NAME = "CONVERSATION";
  public static String TABLE_ALIAS = "CON";

  public static String CONVERSATION_NR = "CONVERSATION_NR";
  public static String NAME = "NAME";
  public static int NAME_LENGTH = 1200;

  public static String NOTES = "NOTES";
  public static int NOTES_LENGTH = 4800;
  public static String START_DATE = "START_DATE";
  public static String END_DATE = "END_DATE";

  @Override
  public String getCreateTable() {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("CREATE TABLE ").append(TABLE_NAME).append(" (");
    statementBuilder.append(CONVERSATION_NR).append(" BIGINT NOT NULL, ");
    statementBuilder.append(NAME).append(" VARCHAR(").append(NAME_LENGTH).append(") NOT NULL, ");
    statementBuilder.append(NOTES).append(" VARCHAR(").append(NOTES_LENGTH).append("), ");
    statementBuilder.append(START_DATE).append(" DATE, ");
    statementBuilder.append(END_DATE).append(" DATE, ");
    statementBuilder.append("PRIMARY KEY (").append(CONVERSATION_NR).append(")");
    statementBuilder.append(")");
    return statementBuilder.toString();
  }

}

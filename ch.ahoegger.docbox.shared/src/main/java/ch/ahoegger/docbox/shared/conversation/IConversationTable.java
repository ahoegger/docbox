package ch.ahoegger.docbox.shared.conversation;

/**
 * <h3>{@link IConversationTable}</h3>
 *
 * @author Andreas Hoegger
 */
public interface IConversationTable {

  public static String TABLE_NAME = "CONVERSATION";
  public static String TABLE_ALIAS = "CON";

  public static String CONVERSATION_NR = "CONVERSATION_NR";
  public static String NAME = "NAME";
  public static int NAME_LENGTH = 1200;

  public static String NOTES = "NOTES";
  public static int NOTES_LENGTH = 4800;
  public static String START_DATE = "START_DATE";
  public static String END_DATE = "END_DATE";
}

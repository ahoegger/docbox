package ch.ahoegger.docbox.shared.partner;

import ch.ahoegger.docbox.shared.conversation.IConversationTable;

/**
 * <h3>{@link IPartnerConversationTable}</h3>
 *
 * @author Andreas Hoegger
 */
public interface IPartnerConversationTable {

  public static String TABLE_NAME = "PARTNER_CONVERSATION";
  public static String TABLE_ALIAS = "P_CON";

  public static String CONVERSATION_NR = IConversationTable.CONVERSATION_NR;
  public static String PARTNER_NR = IPartnerTable.PARTNER_NR;
}

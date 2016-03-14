package ch.ahoegger.docbox.shared.document;

import ch.ahoegger.docbox.shared.partner.IPartnerTable;

/**
 * <h3>{@link IDocumentPartnerTable}</h3>
 *
 * @author aho
 */
public interface IDocumentPartnerTable {

  public static String TABLE_NAME = "DOCUMENT_PARTNER";
  public static String TABLE_ALIAS = "DP";

  public static String DOCUMENT_NR = IDocumentTable.DOCUMENT_NR;
  public static String PARTNER_NR = IPartnerTable.PARTNER_NR;
}

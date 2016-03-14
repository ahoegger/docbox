package ch.ahoegger.docbox.shared.document;

import org.eclipse.scout.rt.shared.security.BasicHierarchyPermission;

/**
 * <h3>{@link DocumentReadPermission}</h3>
 *
 * @author aho
 */
public class DocumentReadPermission extends BasicHierarchyPermission {
  private static final long serialVersionUID = 1L;

  private Long m_documentId;

  /**
   * @param name
   * @param level
   */
  public DocumentReadPermission(Long documentId) {
    super("ReadDocument" + "." + documentId, LEVEL_UNDEFINED);
    m_documentId = documentId;
  }

  protected boolean execCheckLevel(int userLevel) {
//    if (userLevel == LEVEL_OWN) {
//      return BEANS.get(IDocumentService.class).isOwnCompany(getCompanyId());
//    }
    return false;
  }

  public Long getDocumentId() {
    return m_documentId;
  }
}

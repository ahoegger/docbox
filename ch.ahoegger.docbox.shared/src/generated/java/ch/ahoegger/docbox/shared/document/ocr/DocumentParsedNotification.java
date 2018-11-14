package ch.ahoegger.docbox.shared.document.ocr;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <h3>{@link DocumentParsedNotification}</h3>
 *
 * @author Andreas Hoegger
 */
public class DocumentParsedNotification implements Serializable {

  private static final long serialVersionUID = 1L;

  private BigDecimal m_documentId;

  public DocumentParsedNotification withDocumentId(BigDecimal documentId) {
    m_documentId = documentId;
    return this;
  }

  public BigDecimal getDocumentId() {
    return m_documentId;
  }
}

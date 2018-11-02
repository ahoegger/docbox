package ch.ahoegger.docbox.server.ocr;

import java.math.BigDecimal;

/**
 * <h3>{@link ParseDescription}</h3>
 *
 * @author aho
 */
public class ParseDescription {

  private BigDecimal m_documentId;
  private String m_language;

  public ParseDescription withDocumentId(BigDecimal docId) {
    m_documentId = docId;
    return ParseDescription.this;
  }

  public BigDecimal getDocumentId() {
    return m_documentId;
  }

  public ParseDescription withLanguage(String language) {
    m_language = language;
    return ParseDescription.this;
  }

  public String getLanguage() {
    return m_language;
  }
}

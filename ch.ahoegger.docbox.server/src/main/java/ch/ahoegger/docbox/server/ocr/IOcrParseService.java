package ch.ahoegger.docbox.server.ocr;

import java.math.BigDecimal;

import org.eclipse.scout.rt.platform.ApplicationScoped;

/**
 * <h3>{@link IOcrParseService}</h3>
 *
 * @author aho
 */
@ApplicationScoped
public interface IOcrParseService extends IOcrParser {

  void schedule(ParseDescription parseDescription);

  class ParseDescription {
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
}

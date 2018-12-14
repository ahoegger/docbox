package ch.ahoegger.docbox.server.test.util.beans;

import java.math.BigDecimal;

import ch.ahoegger.docbox.server.test.util.TestDataGenerator;

/**
 * <h3>{@link DocumentOcrBean}</h3>
 *
 * @author aho
 */
public class DocumentOcrBean {

  private TestDataGenerator m_generator;

  private BigDecimal m_documentId;
  private String m_text;
  private boolean m_parsed;
  private int m_parseCount;
  private String m_parseFailedReason;

  public DocumentOcrBean(BigDecimal documentId, TestDataGenerator generator) {
    m_documentId = documentId;
    m_generator = generator;
  }

  public TestDataGenerator create() {
    return m_generator.create(this);
  }

  public DocumentOcrBean anyContent() {
    return withText("any parsed abstract")
        .withParsed(true)
        .withParseCount(1);
  }

  public BigDecimal getDocumentId() {
    return m_documentId;
  }

  public String getText() {
    return m_text;
  }

  public DocumentOcrBean withText(String text) {
    m_text = text;
    return this;
  }

  public boolean isParsed() {
    return m_parsed;
  }

  public DocumentOcrBean withParsed(boolean parsed) {
    m_parsed = parsed;
    return this;
  }

  public int getParseCount() {
    return m_parseCount;
  }

  public DocumentOcrBean withParseCount(int parseCount) {
    m_parseCount = parseCount;
    return this;
  }

  public String getParseFailedReason() {
    return m_parseFailedReason;
  }

  public DocumentOcrBean withParseFailedReason(String parseFailedReason) {
    m_parseFailedReason = parseFailedReason;
    return this;
  }

}

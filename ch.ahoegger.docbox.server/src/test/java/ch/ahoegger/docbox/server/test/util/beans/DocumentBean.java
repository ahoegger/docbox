package ch.ahoegger.docbox.server.test.util.beans;

import java.math.BigDecimal;
import java.time.LocalDate;

import ch.ahoegger.docbox.server.test.util.TestDataGenerator;

/**
 * <h3>{@link DocumentBean}</h3>
 *
 * @author aho
 */
public class DocumentBean {

  private TestDataGenerator m_generator;

  private final BigDecimal m_documentId;
  private String m_abstractText;
  private LocalDate m_documentDate;
  private LocalDate m_capturedDate;
  private LocalDate m_validDate;
  private String m_docPath;
  private String m_originalStorage;
  private BigDecimal m_conversationId;
  private boolean m_parseOcr;
  private String m_ocrLanguage;

  public DocumentBean(BigDecimal documentId, TestDataGenerator generator) {
    m_documentId = documentId;
    m_generator = generator;
  }

  public TestDataGenerator create() {
    return m_generator.create(this);
  }

  public DocumentBean anyContent() {
    return withAbstractText("any abstract")
        .withDocumentDate(LocalDate.now().minusDays(10))
        .withCapturedDate(LocalDate.now());
  }

  public BigDecimal getDocumentId() {
    return m_documentId;
  }

  public String getAbstractText() {
    return m_abstractText;
  }

  public DocumentBean withAbstractText(String abstractText) {
    m_abstractText = abstractText;
    return this;
  }

  public LocalDate getDocumentDate() {
    return m_documentDate;
  }

  public DocumentBean withDocumentDate(LocalDate documentDate) {
    m_documentDate = documentDate;
    return this;
  }

  public LocalDate getCapturedDate() {
    return m_capturedDate;
  }

  public DocumentBean withCapturedDate(LocalDate capturedDate) {
    m_capturedDate = capturedDate;
    return this;
  }

  public LocalDate getValidDate() {
    return m_validDate;
  }

  public DocumentBean withValidDate(LocalDate validDate) {
    m_validDate = validDate;
    return this;
  }

  public String getDocPath() {
    return m_docPath;
  }

  public DocumentBean withDocPath(String docPath) {
    m_docPath = docPath;
    return this;
  }

  public String getOriginalStorage() {
    return m_originalStorage;
  }

  public DocumentBean withOriginalStorage(String originalStorage) {
    m_originalStorage = originalStorage;
    return this;
  }

  public BigDecimal getConversationId() {
    return m_conversationId;
  }

  public DocumentBean withConversationId(BigDecimal conversationId) {
    m_conversationId = conversationId;
    return this;
  }

  public boolean isParseOcr() {
    return m_parseOcr;
  }

  public DocumentBean withParseOcr(boolean parseOcr) {
    m_parseOcr = parseOcr;
    return this;
  }

  public String getOcrLanguage() {
    return m_ocrLanguage;
  }

  public DocumentBean withOcrLanguage(String ocrLanguage) {
    m_ocrLanguage = ocrLanguage;
    return this;
  }

}

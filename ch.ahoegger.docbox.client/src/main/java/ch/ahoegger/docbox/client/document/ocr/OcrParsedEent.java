package ch.ahoegger.docbox.client.document.ocr;

import java.math.BigDecimal;
import java.util.Objects;

import org.eclipse.scout.rt.client.ui.desktop.datachange.DataChangeEvent;
import org.eclipse.scout.rt.platform.util.ChangeStatus;

/**
 * <h3>{@link OcrParsedEent}</h3>
 *
 * @author aho
 */
public class OcrParsedEent extends DataChangeEvent {
  private static final long serialVersionUID = 1L;
  private BigDecimal m_documentId;

  /**
   *
   */
  public OcrParsedEent(DocumentParsedNotificationHandler source, BigDecimal documentId) {
    super(source, IDocumentOcrEntity.ENTITY_KEY, ChangeStatus.UPDATED);
    m_documentId = documentId;
  }

  public BigDecimal getDocumentId() {
    return m_documentId;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), m_documentId);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    OcrParsedEent other = (OcrParsedEent) obj;
    if (m_documentId != other.m_documentId) {
      return false;
    }
    return super.equals(obj);
  }

}

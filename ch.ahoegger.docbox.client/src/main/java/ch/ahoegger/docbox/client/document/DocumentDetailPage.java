package ch.ahoegger.docbox.client.document;

import java.math.BigDecimal;

import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithNodes;
import org.eclipse.scout.rt.client.ui.form.IForm;

/**
 * <h3>{@link DocumentDetailPage}</h3>
 *
 * @author Andreas Hoegger
 */
public class DocumentDetailPage extends AbstractPageWithNodes {

  private BigDecimal m_documentId;

  public DocumentDetailPage(BigDecimal documentId) {
    m_documentId = documentId;
  }

  public BigDecimal getDocumentId() {
    return m_documentId;
  }

  @Override
  protected Class<? extends IForm> getConfiguredDetailForm() {
    return DocumentForm.class;
  }

  @Override
  public DocumentForm getDetailForm() {
    return (DocumentForm) super.getDetailForm();
  }

  @Override
  protected void startDetailForm() {
    getDetailForm().setHandler(new DocumentForm.DocumentFormPageHandler(getDetailForm(), getDocumentId()));
    getDetailForm().start();
  }

  @Override
  protected boolean getConfiguredLeaf() {
    return true;
  }
}

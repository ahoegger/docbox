package ch.ahoegger.docbox.client.document;

import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithNodes;
import org.eclipse.scout.rt.client.ui.form.IForm;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.shared.TEXTS;

import ch.ahoegger.docbox.shared.document.DocumentFormData;
import ch.ahoegger.docbox.shared.document.IDocumentService;

/**
 * <h3>{@link DocumentDetailPage}</h3>
 *
 * @author Andreas Hoegger
 */
public class DocumentDetailPage extends AbstractPageWithNodes {

  private long m_documentId;

  public DocumentDetailPage(long documentId) {
    m_documentId = documentId;

  }

  public long getDocumentId() {
    return m_documentId;
  }

  @Override
  protected void execInitPage() {
    DocumentFormData formData = new DocumentFormData();
    formData.setDocumentId(getDocumentId());

    formData = BEANS.get(IDocumentService.class).load(formData);
    String title = formData.getAbstract().getValue();
    if (StringUtility.isNullOrEmpty(title)) {
      title = TEXTS.get("Document");
    }
    else {
      if (title.length() > 16) {
        title = title.subSequence(0, 12) + "...";
      }
    }
    getCellForUpdate().setText(title);
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

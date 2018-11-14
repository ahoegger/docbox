package ch.ahoegger.docbox.client.document;

import java.math.BigDecimal;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.ui.form.fields.htmlfield.AbstractHtmlField;
import org.eclipse.scout.rt.platform.config.CONFIG;
import org.eclipse.scout.rt.platform.html.HTML;
import org.eclipse.scout.rt.platform.text.TEXTS;

import ch.ahoegger.docbox.client.document.DocumentLinkProperties.DocumentLinkDocumentIdParamName;
import ch.ahoegger.docbox.client.document.DocumentLinkProperties.DocumentLinkURI;

/**
 * <h3>{@link AbstractDocumentLinkField}</h3>
 *
 * @author Andreas Hoegger
 */
@FormData(defaultSubtypeSdkCommand = FormData.DefaultSubtypeSdkCommand.IGNORE)
public abstract class AbstractDocumentLinkField extends AbstractHtmlField {

  /**
   * Dimension name for the visible flag
   */
  String VISIBLE_DOC_ID = "VISIBLE_DOC_ID";

  private BigDecimal m_documentId;

  @Override
  protected boolean getConfiguredLabelVisible() {
    return false;
  }

  public void setDocumentId(BigDecimal documentId) {
    m_documentId = documentId;
    createContent();

  }

  public BigDecimal getDocumentId() {
    return m_documentId;
  }

  protected void createContent() {
    if (getDocumentId() == null) {
      setValue(null);
      setVisible(false, VISIBLE_DOC_ID);
    }
    else {
      setVisible(true, VISIBLE_DOC_ID);
      StringBuilder linkBuilder = new StringBuilder();
      linkBuilder.append(CONFIG.getPropertyValue(DocumentLinkURI.class));
      linkBuilder.append("?").append(CONFIG.getPropertyValue(DocumentLinkDocumentIdParamName.class)).append("=").append(getDocumentId());
      String encodedHtml = HTML.link(linkBuilder.toString(), TEXTS.get("Open")).addAttribute("target", "_blank").toHtml();
      setValue(encodedHtml);
    }
  }
}

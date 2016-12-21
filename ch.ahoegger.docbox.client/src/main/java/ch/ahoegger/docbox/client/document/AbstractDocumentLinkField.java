package ch.ahoegger.docbox.client.document;

import java.math.BigDecimal;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.ui.form.fields.htmlfield.AbstractHtmlField;
import org.eclipse.scout.rt.platform.config.CONFIG;
import org.eclipse.scout.rt.platform.html.HTML;
import org.eclipse.scout.rt.shared.TEXTS;

import ch.ahoegger.docbox.client.document.DocumentLinkProperties.DocumentLinkDocumentIdParamName;
import ch.ahoegger.docbox.client.document.DocumentLinkProperties.DocumentLinkURI;

/**
 * <h3>{@link AbstractDocumentLinkField}</h3>
 *
 * @author aho
 */
@FormData(defaultSubtypeSdkCommand = FormData.DefaultSubtypeSdkCommand.IGNORE)
public abstract class AbstractDocumentLinkField extends AbstractHtmlField {

  private BigDecimal m_documentId;

  public void setDocumentId(BigDecimal documentId) {
    m_documentId = documentId;

  }

  public BigDecimal getDocumentId() {
    return m_documentId;
  }

  protected void createContent() {
    StringBuilder linkBuilder = new StringBuilder();
    linkBuilder.append(CONFIG.getPropertyValue(DocumentLinkURI.class));
    linkBuilder.append("?").append(CONFIG.getPropertyValue(DocumentLinkDocumentIdParamName.class)).append("=").append(getDocumentId());
    String encodedHtml = HTML.link(linkBuilder.toString(), TEXTS.get("Open")).addAttribute("target", "_blank").toHtml();
    setValue(encodedHtml);
  }
}

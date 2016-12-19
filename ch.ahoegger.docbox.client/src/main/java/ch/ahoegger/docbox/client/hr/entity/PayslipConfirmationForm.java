package ch.ahoegger.docbox.client.hr.entity;

import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCloseButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.htmlfield.AbstractHtmlField;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.config.CONFIG;
import org.eclipse.scout.rt.platform.html.HTML;
import org.eclipse.scout.rt.shared.TEXTS;

import ch.ahoegger.docbox.client.document.DocumentLinkProperties.DocumentLinkDocumentIdParamName;
import ch.ahoegger.docbox.client.document.DocumentLinkProperties.DocumentLinkURI;
import ch.ahoegger.docbox.client.hr.entity.PayslipConfirmationForm.MainBox.CloseButton;

/**
 * <h3>{@link PayslipConfirmationForm}</h3>
 *
 * @author Andreas Hoegger
 */
public class PayslipConfirmationForm extends AbstractForm {

  private Long m_documentId;

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("PayslipCreated");
  }

  public void setDocumentId(Long documentId) {
    m_documentId = documentId;
  }

  public CloseButton getCloseButton() {
    return getFieldByClass(CloseButton.class);
  }

  public Long getDocumentId() {
    return m_documentId;
  }

  @Order(5000)
  public class MainBox extends AbstractGroupBox {

    @Override
    protected boolean getConfiguredBorderVisible() {
      return true;
    }

    @Override
    protected String getConfiguredBorderDecoration() {
      return BORDER_DECORATION_EMPTY;
    }

    @Override
    protected int getConfiguredGridColumnCount() {
      return 1;
    }

    @Order(1000)
    public class OpenHtmlField extends AbstractHtmlField {

      @Override
      protected void execInitField() {
        StringBuilder linkBuilder = new StringBuilder();
        linkBuilder.append(CONFIG.getPropertyValue(DocumentLinkURI.class));
        linkBuilder.append("?").append(CONFIG.getPropertyValue(DocumentLinkDocumentIdParamName.class)).append("=").append(getDocumentId());
        String encodedHtml = HTML.link(linkBuilder.toString(), TEXTS.get("OpenPdf")).addAttribute("target", "_blank").toHtml();
        setValue(encodedHtml);
      }

      @Override
      protected void execAppLinkAction(String ref) {
        doClose();
      }
    }

    @Order(2000)
    public class CloseButton extends AbstractCloseButton {

    }

  }

}

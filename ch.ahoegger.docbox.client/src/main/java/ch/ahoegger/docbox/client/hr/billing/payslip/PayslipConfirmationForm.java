package ch.ahoegger.docbox.client.hr.billing.payslip;

import java.math.BigDecimal;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCloseButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.htmlfield.AbstractHtmlField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.config.CONFIG;
import org.eclipse.scout.rt.platform.html.HTML;
import org.eclipse.scout.rt.platform.text.TEXTS;

import ch.ahoegger.docbox.client.document.DocumentLinkProperties.DocumentLinkDocumentIdParamName;
import ch.ahoegger.docbox.client.document.DocumentLinkProperties.DocumentLinkURI;
import ch.ahoegger.docbox.client.hr.billing.payslip.PayslipConfirmationForm.MainBox.CloseButton;
import ch.ahoegger.docbox.client.hr.billing.payslip.PayslipConfirmationForm.MainBox.OpenHtmlField;
import ch.ahoegger.docbox.shared.hr.billing.payslip.IPayslipConfirmationService;
import ch.ahoegger.docbox.shared.hr.billing.payslip.PayslipConfirmationFormData;

/**
 * <h3>{@link PayslipConfirmationForm}</h3>
 *
 * @author Andreas Hoegger
 */
@FormData(value = PayslipConfirmationFormData.class, sdkCommand = FormData.SdkCommand.CREATE)
public class PayslipConfirmationForm extends AbstractForm {

  private BigDecimal m_payslipId;
  private BigDecimal m_documentId;

  public PayslipConfirmationForm() {
    setHandler(new FormHandler());
  }

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("PayslipCreated");
  }

  public OpenHtmlField getOpenHtmlField() {
    return getFieldByClass(OpenHtmlField.class);
  }

  public CloseButton getCloseButton() {
    return getFieldByClass(CloseButton.class);
  }

  @FormData
  public void setPayslipId(BigDecimal payslipId) {
    m_payslipId = payslipId;
  }

  @FormData
  public BigDecimal getPayslipId() {
    return m_payslipId;
  }

  @FormData
  public BigDecimal getDocumentId() {
    return m_documentId;
  }

  @FormData
  public void setDocumentId(BigDecimal documentId) {
    m_documentId = documentId;
  }

  private void updateLink() {
    StringBuilder linkBuilder = new StringBuilder()
        .append(CONFIG.getPropertyValue(DocumentLinkURI.class))
        .append("?").append(CONFIG.getPropertyValue(DocumentLinkDocumentIdParamName.class)).append("=").append(getDocumentId());
    String encodedHtml = HTML.link(linkBuilder.toString(), TEXTS.get("OpenPdf")).addAttribute("target", "_blank").toHtml();
    getOpenHtmlField().setValue(encodedHtml);
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
        updateLink();
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

  public class FormHandler extends AbstractFormHandler {
    @Override
    protected void execLoad() {
      PayslipConfirmationFormData formData = new PayslipConfirmationFormData();
      exportFormData(formData);
      formData = BEANS.get(IPayslipConfirmationService.class).load(formData);
      importFormData(formData);
      updateLink();
    }

    @Override
    protected void execStore() {
    }
  }

}

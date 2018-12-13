package ch.ahoegger.docbox.shared.hr.billing.payslip;

import java.math.BigDecimal;

import javax.annotation.Generated;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;
import org.eclipse.scout.rt.shared.data.form.properties.AbstractPropertyData;

/**
 * <b>NOTE:</b><br>
 * This class is auto generated by the Scout SDK. No manual modifications recommended.
 */
@Generated(value = "ch.ahoegger.docbox.client.hr.billing.payslip.PayslipConfirmationForm", comments = "This class is auto generated by the Scout SDK. No manual modifications recommended.")
public class PayslipConfirmationFormData extends AbstractFormData {

  private static final long serialVersionUID = 1L;

  /**
   * access method for property DocumentId.
   */
  public BigDecimal getDocumentId() {
    return getDocumentIdProperty().getValue();
  }

  /**
   * access method for property DocumentId.
   */
  public void setDocumentId(BigDecimal documentId) {
    getDocumentIdProperty().setValue(documentId);
  }

  public DocumentIdProperty getDocumentIdProperty() {
    return getPropertyByClass(DocumentIdProperty.class);
  }

  public OpenHtml getOpenHtml() {
    return getFieldByClass(OpenHtml.class);
  }

  /**
   * access method for property PayslipId.
   */
  public BigDecimal getPayslipId() {
    return getPayslipIdProperty().getValue();
  }

  /**
   * access method for property PayslipId.
   */
  public void setPayslipId(BigDecimal payslipId) {
    getPayslipIdProperty().setValue(payslipId);
  }

  public PayslipIdProperty getPayslipIdProperty() {
    return getPropertyByClass(PayslipIdProperty.class);
  }

  public static class DocumentIdProperty extends AbstractPropertyData<BigDecimal> {

    private static final long serialVersionUID = 1L;
  }

  public static class OpenHtml extends AbstractValueFieldData<String> {

    private static final long serialVersionUID = 1L;
  }

  public static class PayslipIdProperty extends AbstractPropertyData<BigDecimal> {

    private static final long serialVersionUID = 1L;
  }
}

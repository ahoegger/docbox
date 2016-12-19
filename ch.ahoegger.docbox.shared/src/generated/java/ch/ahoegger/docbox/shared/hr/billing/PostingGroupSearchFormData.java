package ch.ahoegger.docbox.shared.hr.billing;

import java.math.BigDecimal;

import javax.annotation.Generated;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.properties.AbstractPropertyData;

/**
 * <b>NOTE:</b><br>
 * This class is auto generated by the Scout SDK. No manual modifications recommended.
 */
@Generated(value = "ch.ahoegger.docbox.client.hr.billing.PostingGroupSearchForm", comments = "This class is auto generated by the Scout SDK. No manual modifications recommended.")
public class PostingGroupSearchFormData extends AbstractFormData {

  private static final long serialVersionUID = 1L;

  /**
   * access method for property PartnerId.
   */
  public BigDecimal getPartnerId() {
    return getPartnerIdProperty().getValue();
  }

  /**
   * access method for property PartnerId.
   */
  public void setPartnerId(BigDecimal partnerId) {
    getPartnerIdProperty().setValue(partnerId);
  }

  public PartnerIdProperty getPartnerIdProperty() {
    return getPropertyByClass(PartnerIdProperty.class);
  }

  public static class PartnerIdProperty extends AbstractPropertyData<BigDecimal> {

    private static final long serialVersionUID = 1L;
  }
}

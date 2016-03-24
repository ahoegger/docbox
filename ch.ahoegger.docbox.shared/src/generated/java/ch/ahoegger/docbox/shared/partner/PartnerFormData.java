package ch.ahoegger.docbox.shared.partner;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Generated;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;
import org.eclipse.scout.rt.shared.data.form.properties.AbstractPropertyData;

/**
 * <b>NOTE:</b><br>
 * This class is auto generated by the Scout SDK. No manual modifications recommended.
 */
@Generated(value = "ch.ahoegger.docbox.client.partner.PartnerForm", comments = "This class is auto generated by the Scout SDK. No manual modifications recommended.")
public class PartnerFormData extends AbstractFormData {

  private static final long serialVersionUID = 1L;

  public Description getDescription() {
    return getFieldByClass(Description.class);
  }

  public EndDate getEndDate() {
    return getFieldByClass(EndDate.class);
  }

  public Name getName() {
    return getFieldByClass(Name.class);
  }

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

  public StartDate getStartDate() {
    return getFieldByClass(StartDate.class);
  }

  public static class Description extends AbstractValueFieldData<String> {

    private static final long serialVersionUID = 1L;
  }

  public static class EndDate extends AbstractValueFieldData<Date> {

    private static final long serialVersionUID = 1L;
  }

  public static class Name extends AbstractValueFieldData<String> {

    private static final long serialVersionUID = 1L;
  }

  public static class PartnerIdProperty extends AbstractPropertyData<BigDecimal> {

    private static final long serialVersionUID = 1L;
  }

  public static class StartDate extends AbstractValueFieldData<Date> {

    private static final long serialVersionUID = 1L;
  }
}
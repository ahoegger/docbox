package ch.ahoegger.docbox.shared.hr.tax;

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
@Generated(value = "ch.ahoegger.docbox.client.hr.tax.TaxGroupForm", comments = "This class is auto generated by the Scout SDK. No manual modifications recommended.")
public class TaxGroupFormData extends AbstractFormData {

  private static final long serialVersionUID = 1L;

  public EndDate getEndDate() {
    return getFieldByClass(EndDate.class);
  }

  public Name getName() {
    return getFieldByClass(Name.class);
  }

  public StartDate getStartDate() {
    return getFieldByClass(StartDate.class);
  }

  /**
   * access method for property TaxGroupId.
   */
  public BigDecimal getTaxGroupId() {
    return getTaxGroupIdProperty().getValue();
  }

  /**
   * access method for property TaxGroupId.
   */
  public void setTaxGroupId(BigDecimal taxGroupId) {
    getTaxGroupIdProperty().setValue(taxGroupId);
  }

  public TaxGroupIdProperty getTaxGroupIdProperty() {
    return getPropertyByClass(TaxGroupIdProperty.class);
  }

  public static class EndDate extends AbstractValueFieldData<Date> {

    private static final long serialVersionUID = 1L;
  }

  public static class Name extends AbstractValueFieldData<String> {

    private static final long serialVersionUID = 1L;
  }

  public static class StartDate extends AbstractValueFieldData<Date> {

    private static final long serialVersionUID = 1L;
  }

  public static class TaxGroupIdProperty extends AbstractPropertyData<BigDecimal> {

    private static final long serialVersionUID = 1L;
  }
}

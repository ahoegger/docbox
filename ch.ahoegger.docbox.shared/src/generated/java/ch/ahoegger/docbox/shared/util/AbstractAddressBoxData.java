package ch.ahoegger.docbox.shared.util;

import java.math.BigDecimal;

import javax.annotation.Generated;

import org.eclipse.scout.rt.shared.data.form.fields.AbstractFormFieldData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;
import org.eclipse.scout.rt.shared.data.form.properties.AbstractPropertyData;

/**
 * <b>NOTE:</b><br>
 * This class is auto generated by the Scout SDK. No manual modifications recommended.
 */
@Generated(value = "ch.ahoegger.docbox.client.util.AbstractAddressBox", comments = "This class is auto generated by the Scout SDK. No manual modifications recommended.")
public abstract class AbstractAddressBoxData extends AbstractFormFieldData {

  private static final long serialVersionUID = 1L;

  /**
   * access method for property AddressId.
   */
  public BigDecimal getAddressId() {
    return getAddressIdProperty().getValue();
  }

  /**
   * access method for property AddressId.
   */
  public void setAddressId(BigDecimal addressId) {
    getAddressIdProperty().setValue(addressId);
  }

  public AddressIdProperty getAddressIdProperty() {
    return getPropertyByClass(AddressIdProperty.class);
  }

  public City getCity() {
    return getFieldByClass(City.class);
  }

  public Line1 getLine1() {
    return getFieldByClass(Line1.class);
  }

  public Line2 getLine2() {
    return getFieldByClass(Line2.class);
  }

  public Plz getPlz() {
    return getFieldByClass(Plz.class);
  }

  public static class AddressIdProperty extends AbstractPropertyData<BigDecimal> {

    private static final long serialVersionUID = 1L;
  }

  public static class City extends AbstractValueFieldData<String> {

    private static final long serialVersionUID = 1L;
  }

  public static class Line1 extends AbstractValueFieldData<String> {

    private static final long serialVersionUID = 1L;
  }

  public static class Line2 extends AbstractValueFieldData<String> {

    private static final long serialVersionUID = 1L;
  }

  public static class Plz extends AbstractValueFieldData<String> {

    private static final long serialVersionUID = 1L;
  }
}
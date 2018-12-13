package ch.ahoegger.docbox.server.hr;

import java.math.BigDecimal;

import ch.ahoegger.docbox.shared.template.AbstractAddressBoxData;

/**
 * <h3>{@link AddressFormData}</h3>
 *
 * @author aho
 */
public class AddressFormData extends AbstractAddressBoxData {

  private static final long serialVersionUID = 1L;

  public AddressFormData withAddressNr(BigDecimal addressNr) {
    setAddressId(addressNr);
    return this;
  }

  public AddressFormData withLine1(String line1) {
    getLine1().setValue(line1);
    return this;
  }

  public AddressFormData withLine2(String line2) {
    getLine2().setValue(line2);
    return this;
  }

  public AddressFormData withPlz(String plz) {
    getPlz().setValue(plz);
    return this;
  }

  public AddressFormData withCity(String city) {
    getCity().setValue(city);
    return this;
  }
}

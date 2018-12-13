package ch.ahoegger.docbox.client.hr;

import java.math.BigDecimal;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.text.TEXTS;

import ch.ahoegger.docbox.or.definition.table.IAddressTable;
import ch.ahoegger.docbox.shared.template.AbstractAddressBoxData;

/**
 * <h3>{@link AbstractAddressBox}</h3>
 *
 * @author aho
 */
@FormData(value = AbstractAddressBoxData.class, defaultSubtypeSdkCommand = FormData.DefaultSubtypeSdkCommand.CREATE, sdkCommand = FormData.SdkCommand.CREATE)
public abstract class AbstractAddressBox extends AbstractGroupBox {

  private BigDecimal m_addressId;

  @FormData
  public BigDecimal getAddressId() {
    return m_addressId;
  }

  @FormData
  public void setAddressId(BigDecimal addressId) {
    m_addressId = addressId;
  }

  public CityField getCityField() {
    return getFieldByClass(CityField.class);
  }

  public PlzField getPlzField() {
    return getFieldByClass(PlzField.class);
  }

  public Line2Field getLine2Field() {
    return getFieldByClass(Line2Field.class);
  }

  public Line1Field getLine1Field() {
    return getFieldByClass(Line1Field.class);
  }

  @Order(1000)
  public class Line1Field extends AbstractStringField {
    @Override
    protected String getConfiguredLabel() {
      return TEXTS.get("AddressLine1");
    }

    @Override
    protected int getConfiguredMaxLength() {
      return IAddressTable.ADDRESS_LINE_LENGTH;
    }
  }

  @Order(2000)
  public class Line2Field extends AbstractStringField {
    @Override
    protected String getConfiguredLabel() {
      return TEXTS.get("AddressLine2");
    }

    @Override
    protected int getConfiguredMaxLength() {
      return IAddressTable.ADDRESS_LINE_LENGTH;
    }

  }

  @Order(3000)
  public class PlzField extends AbstractStringField {
    @Override
    protected String getConfiguredLabel() {
      return TEXTS.get("PLZ");
    }

    @Override
    protected int getConfiguredMaxLength() {
      return IAddressTable.PLZ_LENGTH;
    }
  }

  @Order(4000)
  public class CityField extends AbstractStringField {
    @Override
    protected String getConfiguredLabel() {
      return TEXTS.get("City");
    }

    @Override
    protected int getConfiguredMaxLength() {
      return IAddressTable.CITY_LENGTH;
    }
  }

}

package ch.ahoegger.docbox.client.hr.employee;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.text.TEXTS;

import ch.ahoegger.docbox.or.definition.table.IEmployeeTable;
import ch.ahoegger.docbox.shared.hr.employee.AbstractEmployerBoxData;

/**
 * <h3>{@link AbstractEmployerBox}</h3>
 *
 * @author Andreas Hoegger
 */
@FormData(value = AbstractEmployerBoxData.class, defaultSubtypeSdkCommand = FormData.DefaultSubtypeSdkCommand.CREATE, sdkCommand = FormData.SdkCommand.CREATE)
public abstract class AbstractEmployerBox extends AbstractGroupBox {
  @Override
  protected String getConfiguredLabel() {
    return TEXTS.get("Employer");
  }

  public PhoneField getPhoneField() {
    return getFieldByClass(PhoneField.class);
  }

  public EmailField getEmailField() {
    return getFieldByClass(EmailField.class);
  }

  public AddressLine3Field getAddressLine3Field() {
    return getFieldByClass(AddressLine3Field.class);
  }

  public AddressLine2Field getAddressLine2Field() {
    return getFieldByClass(AddressLine2Field.class);
  }

  public AddressLine1Field getAddressLine1Field() {
    return getFieldByClass(AddressLine1Field.class);
  }

  @Order(1000)
  public class AddressLine1Field extends AbstractStringField {
    @Override
    protected String getConfiguredLabel() {
      return TEXTS.get("AddressLine1");
    }

    @Override
    protected int getConfiguredMaxLength() {
      return IEmployeeTable.ADDRESS_LINE_LENGTH;
    }
  }

  @Order(2000)
  public class AddressLine2Field extends AbstractStringField {
    @Override
    protected String getConfiguredLabel() {
      return TEXTS.get("AddressLine2");
    }

    @Override
    protected int getConfiguredMaxLength() {
      return IEmployeeTable.ADDRESS_LINE_LENGTH;
    }
  }

  @Order(3000)
  public class AddressLine3Field extends AbstractStringField {
    @Override
    protected String getConfiguredLabel() {
      return TEXTS.get("AddressLine3");
    }

    @Override
    protected int getConfiguredMaxLength() {
      return IEmployeeTable.ADDRESS_LINE_LENGTH;
    }
  }

  @Order(4000)
  public class EmailField extends AbstractStringField {
    @Override
    protected String getConfiguredLabel() {
      return TEXTS.get("Email");
    }

    @Override
    protected int getConfiguredMaxLength() {
      return IEmployeeTable.EMPLOYER_EMAIL_LENGTH;
    }

  }

  @Order(5000)
  public class PhoneField extends AbstractStringField {
    @Override
    protected String getConfiguredLabel() {
      return TEXTS.get("Phone");
    }

    @Override
    protected int getConfiguredMaxLength() {
      return IEmployeeTable.EMPLOYER_PHONE_LENGTH;
    }
  }

}

package ch.ahoegger.docbox.client.hr.employee;

import java.math.BigDecimal;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.ui.form.fields.bigdecimalfield.AbstractBigDecimalField;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.shared.TEXTS;

import ch.ahoegger.docbox.shared.hr.employee.AbstractEmployeeBoxData;
import ch.ahoegger.docbox.shared.hr.employee.IEmployeeTable;

/**
 * <h3>{@link AbstractEmployeeBox}</h3>
 *
 * @author aho
 */
@FormData(value = AbstractEmployeeBoxData.class, defaultSubtypeSdkCommand = FormData.DefaultSubtypeSdkCommand.CREATE, sdkCommand = FormData.SdkCommand.CREATE)
public abstract class AbstractEmployeeBox extends AbstractGroupBox {

  @Override
  protected String getConfiguredLabel() {
    return TEXTS.get("Employee");
  }

//  @Override
//  protected boolean getConfiguredBorderVisible() {
//    return true;
//  }

  public AccountNumberField getAccountField() {
    return getFieldByClass(AccountNumberField.class);
  }

  public AddressLine1Field getAddressLine1Field() {
    return getFieldByClass(AddressLine1Field.class);
  }

  public AddressLine2Field getAddressLine2Field() {
    return getFieldByClass(AddressLine2Field.class);
  }

  public AhvNumberField getAhvNumberField() {
    return getFieldByClass(AhvNumberField.class);
  }

  public HourlyWageField getHourlyWageField() {
    return getFieldByClass(HourlyWageField.class);
  }

  public LastNameField getLastNameField() {
    return getFieldByClass(LastNameField.class);
  }

  public FirstNameField getFirstNameField() {
    return getFieldByClass(FirstNameField.class);
  }

  @Order(1000)
  public class FirstNameField extends AbstractStringField {
    @Override
    protected String getConfiguredLabel() {
      return TEXTS.get("Firstname");
    }

    @Override
    protected boolean getConfiguredMandatory() {
      return true;
    }

    @Override
    protected int getConfiguredMaxLength() {
      return IEmployeeTable.FIRST_NAME_LENGTH;
    }
  }

  @Order(2000)
  public class LastNameField extends AbstractStringField {
    @Override
    protected String getConfiguredLabel() {
      return TEXTS.get("Lastname");
    }

    @Override
    protected boolean getConfiguredMandatory() {
      return true;
    }

    @Override
    protected int getConfiguredMaxLength() {
      return IEmployeeTable.LAST_NAME_LENGTH;
    }
  }

  @Order(3000)
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

  @Order(4000)
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

  @Order(5000)
  public class AhvNumberField extends AbstractStringField {
    @Override
    protected String getConfiguredLabel() {
      return TEXTS.get("AHVNumber");
    }

    @Override
    protected int getConfiguredMaxLength() {
      return IEmployeeTable.AHV_NUMBER_LENGTH;
    }
  }

  @Order(6000)
  public class HourlyWageField extends AbstractBigDecimalField {
    @Override
    protected String getConfiguredLabel() {
      return TEXTS.get("HourlyWage");
    }

    @Override
    protected BigDecimal getConfiguredMinValue() {
      return IEmployeeTable.HOURLY_WAGE_MIN;
    }

    @Override
    protected BigDecimal getConfiguredMaxValue() {
      return IEmployeeTable.HOURLY_WAGE_MAX;
    }

  }

  @Order(10000)
  public class AccountNumberField extends AbstractStringField {
    @Override
    protected String getConfiguredLabel() {
      return TEXTS.get("AccountNumber");
    }

    @Override
    protected int getConfiguredMaxLength() {
      return IEmployeeTable.ACCOUNT_NUMBER_LENGTH;
    }
  }
}

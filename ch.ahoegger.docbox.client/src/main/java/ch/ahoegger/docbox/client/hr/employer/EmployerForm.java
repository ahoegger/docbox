package ch.ahoegger.docbox.client.hr.employer;

import java.math.BigDecimal;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.text.TEXTS;

import ch.ahoegger.docbox.client.hr.AbstractAddressBox;
import ch.ahoegger.docbox.client.hr.employer.EmployerForm.MainBox.AddressBox;
import ch.ahoegger.docbox.client.hr.employer.EmployerForm.MainBox.CancelButton;
import ch.ahoegger.docbox.client.hr.employer.EmployerForm.MainBox.EmailField;
import ch.ahoegger.docbox.client.hr.employer.EmployerForm.MainBox.NameField;
import ch.ahoegger.docbox.client.hr.employer.EmployerForm.MainBox.OkButton;
import ch.ahoegger.docbox.client.hr.employer.EmployerForm.MainBox.PhoneField;
import ch.ahoegger.docbox.or.definition.table.IEmployerTable;
import ch.ahoegger.docbox.shared.hr.employer.EmployerFormData;
import ch.ahoegger.docbox.shared.hr.employer.IEmployerService;

/**
 * <h3>{@link EmployerForm}</h3>
 *
 * @author aho
 */
@FormData(value = EmployerFormData.class, sdkCommand = FormData.SdkCommand.CREATE)
public class EmployerForm extends AbstractForm {
  private BigDecimal m_employerId;

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("Employer");
  }

  @FormData
  public BigDecimal getEmployerId() {
    return m_employerId;
  }

  @FormData
  public void setEmployerId(BigDecimal employerId) {
    m_employerId = employerId;
  }

  public EmailField getEmailField() {
    return getFieldByClass(EmailField.class);
  }

  public PhoneField getPhoneField() {
    return getFieldByClass(PhoneField.class);
  }

  public AddressBox getAddressBox() {
    return getFieldByClass(AddressBox.class);
  }

  public OkButton getOkButton() {
    return getFieldByClass(OkButton.class);
  }

  public CancelButton getCancelButton() {
    return getFieldByClass(CancelButton.class);
  }

  public NameField getNameField() {
    return getFieldByClass(NameField.class);
  }

  public void startNew() {
    startInternal(new NewHandler());
  }

  public void startModify() {
    startInternal(new ModifyHandler());
  }

  @Order(1000)
  public class MainBox extends AbstractGroupBox {

    @Override
    protected boolean getConfiguredBorderVisible() {
      return true;
    }

    @Override
    protected String getConfiguredBorderDecoration() {
      return BORDER_DECORATION_EMPTY;
    }

    @Order(1000)
    public class NameField extends AbstractStringField {
      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("Name");
      }

      @Override
      protected int getConfiguredMaxLength() {
        return IEmployerTable.NAME_LENGTH;
      }
    }

    @Order(2000)
    public class EmailField extends AbstractStringField {
      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("Email");
      }

      @Override
      protected int getConfiguredMaxLength() {
        return IEmployerTable.EMAIL_LENGTH;
      }
    }

    @Order(3000)
    public class PhoneField extends AbstractStringField {
      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("Phone");
      }

      @Override
      protected int getConfiguredMaxLength() {
        return IEmployerTable.PHONE_LENGTH;
      }
    }

    @Order(4000)
    public class AddressBox extends AbstractAddressBox {

    }

    @Order(10000)
    public class OkButton extends AbstractOkButton {
    }

    @Order(11000)
    public class CancelButton extends AbstractCancelButton {
    }
  }

  public class NewHandler extends AbstractFormHandler {
    @Override
    protected void execLoad() {
      EmployerFormData formData = new EmployerFormData();
      exportFormData(formData);
      formData = BEANS.get(IEmployerService.class).prepareCreate(formData);
      importFormData(formData);
    }

    @Override
    protected void execStore() {
      EmployerFormData formData = new EmployerFormData();
      exportFormData(formData);
      formData = BEANS.get(IEmployerService.class).create(formData);
      importFormData(formData);
    }
  }

  public class ModifyHandler extends AbstractFormHandler {
    @Override
    protected void execLoad() {
      EmployerFormData formData = new EmployerFormData();
      exportFormData(formData);
      formData = BEANS.get(IEmployerService.class).load(formData);
      importFormData(formData);
    }

    @Override
    protected void execStore() {
      EmployerFormData formData = new EmployerFormData();
      exportFormData(formData);
      formData = BEANS.get(IEmployerService.class).store(formData);
      importFormData(formData);
    }
  }

}

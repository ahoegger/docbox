package ch.ahoegger.docbox.client.hr.employee;

import java.math.BigDecimal;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.dto.FormData.SdkCommand;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.IFormFieldVisitor;
import org.eclipse.scout.rt.client.ui.form.fields.IFormField;
import org.eclipse.scout.rt.client.ui.form.fields.IValueField;
import org.eclipse.scout.rt.client.ui.form.fields.bigdecimalfield.AbstractBigDecimalField;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.placeholder.AbstractPlaceholderField;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.status.IStatus;
import org.eclipse.scout.rt.platform.status.Status;
import org.eclipse.scout.rt.shared.TEXTS;

import ch.ahoegger.docbox.client.hr.employee.EmployeeForm.MainBox.CancelButton;
import ch.ahoegger.docbox.client.hr.employee.EmployeeForm.MainBox.EmployeeBox.AccountNumberField;
import ch.ahoegger.docbox.client.hr.employee.EmployeeForm.MainBox.OkButton;
import ch.ahoegger.docbox.client.hr.employee.EmployeeForm.MainBox.PartnerField;
import ch.ahoegger.docbox.client.hr.employee.EmployeeForm.MainBox.PartnerGroupBox;
import ch.ahoegger.docbox.client.partner.AbstractPartnerBox;
import ch.ahoegger.docbox.client.partner.AbstractPartnerSmartField;
import ch.ahoegger.docbox.client.partner.IPartnerEntity;
import ch.ahoegger.docbox.shared.hr.employee.EmployeeFormData;
import ch.ahoegger.docbox.shared.hr.employee.IEmployeeService;
import ch.ahoegger.docbox.shared.hr.employee.IEmployeeTable;
import ch.ahoegger.docbox.shared.partner.IPartnerService;
import ch.ahoegger.docbox.shared.partner.PartnerFormData;

@FormData(value = EmployeeFormData.class, sdkCommand = FormData.SdkCommand.CREATE)
public class EmployeeForm extends AbstractForm {

  private BigDecimal m_partnerId;

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("Employee");
  }

  @Override
  protected void execStored() {
    getDesktop().dataChanged(IPartnerEntity.ENTITY_KEY, IEmployeeEntity.ENTITY_KEY);
  }

  public void startModify() {
    startInternalExclusive(new ModifyHandler());
  }

  public void startNew() {
    startInternal(new NewHandler());
  }

  @FormData
  public BigDecimal getPartnerId() {
    return m_partnerId;
  }

  @FormData
  public void setPartnerId(BigDecimal partnerId) {
    m_partnerId = partnerId;
  }

  public CancelButton getCancelButton() {
    return getFieldByClass(CancelButton.class);
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  public PartnerGroupBox getPartnerGroupBox() {
    return getFieldByClass(PartnerGroupBox.class);
  }

  public PartnerField getPartnerField() {
    return getFieldByClass(PartnerField.class);
  }

  public AccountNumberField getAccountField() {
    return getFieldByClass(AccountNumberField.class);
  }

  public OkButton getOkButton() {
    return getFieldByClass(OkButton.class);
  }

  @Order(1000)
  public class MainBox extends AbstractGroupBox {

    @Order(0)
    @FormData(sdkCommand = SdkCommand.IGNORE)
    public class PartnerField extends AbstractPartnerSmartField {
      @Override
      protected BigDecimal validateValueInternal(BigDecimal rawValue) {
        BigDecimal validatedValue = super.validateValueInternal(rawValue);
        if (validatedValue == null) {
          clearErrorStatus();
        }
        else {
          EmployeeFormData formData = new EmployeeFormData();
          formData.setPartnerId(validatedValue);
          formData = BEANS.get(IEmployeeService.class).load(formData);
          if (formData != null) {
            addErrorStatus(new Status(TEXTS.get("Error_partnerAlreadyEmployee"), IStatus.ERROR));
          }
          else {
            clearErrorStatus();
          }
        }
        return validatedValue;
      }

      @Override
      protected void execChangedValue() {
        setPartnerId(getValue());
      }
    }

    @Order(1000)
    public class PartnerGroupBox extends AbstractPartnerBox {
      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("Partner");
      }

      @Override
      protected boolean getConfiguredBorderVisible() {
        return true;
      }

      @Override
      protected Class<? extends IValueField> getConfiguredMasterField() {
        return PartnerField.class;
      }

      @Override
      protected void execChangedMasterValue(Object newMasterValue) {
        if (newMasterValue == null) {
          visitFields(new IFormFieldVisitor() {

            @Override
            public boolean visitField(IFormField field, int level, int fieldIndex) {
              if (field instanceof IValueField<?>) {
                ((IValueField) field).resetValue();
                setEnabled(true);
              }
              return true;
            }
          });
          setEnabled(true);
        }
        else {
          PartnerFormData partnerFormData = new PartnerFormData();
          partnerFormData.setPartnerId(getPartnerField().getValue());
          partnerFormData = BEANS.get(IPartnerService.class).load(partnerFormData);
          getNameField().importFormFieldData(partnerFormData.getPartnerBox().getName(), true);
          getDescriptionField().importFormFieldData(partnerFormData.getPartnerBox().getDescription(), true);
          getStartDateField().importFormFieldData(partnerFormData.getPartnerBox().getStartDate(), true);
          getEndDateField().importFormFieldData(partnerFormData.getPartnerBox().getEndDate(), true);
          getNameField().setValue(partnerFormData.getPartnerBox().getName().getValue());
          visitFields(new IFormFieldVisitor() {

            @Override
            public boolean visitField(IFormField field, int level, int fieldIndex) {
              if (field instanceof IValueField<?>) {
                setEnabled(false);
              }
              return true;
            }
          });
        }
      }

      @Order(3000)
      @FormData(sdkCommand = SdkCommand.IGNORE)
      public class PlaceHolderField01 extends AbstractPlaceholderField {

      }

    }

    @Order(2000)
    public class EmployeeBox extends AbstractGroupBox {

      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("Employee");
      }

      @Override
      protected boolean getConfiguredBorderVisible() {
        return true;
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
          return IEmployeeTable.ADDRESS_LINE1_LENGTH;
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
          return IEmployeeTable.ADDRESS_LINE2_LENGTH;
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

    public class OkButton extends AbstractOkButton {
    }

    @Order(101000)
    public class CancelButton extends AbstractCancelButton {
    }
  }

  public class ModifyHandler extends AbstractFormHandler {

    @Override
    protected void execLoad() {
      getPartnerField().setVisible(false);
      IEmployeeService service = BEANS.get(IEmployeeService.class);
      EmployeeFormData formData = new EmployeeFormData();
      exportFormData(formData);
      formData = service.load(formData);
      importFormData(formData);

    }

    @Override
    protected void execStore() {
      IEmployeeService service = BEANS.get(IEmployeeService.class);
      EmployeeFormData formData = new EmployeeFormData();
      exportFormData(formData);
      service.store(formData);
    }
  }

  public class NewHandler extends AbstractFormHandler {

    @Override
    protected void execLoad() {
      IEmployeeService service = BEANS.get(IEmployeeService.class);
      EmployeeFormData formData = new EmployeeFormData();
      exportFormData(formData);
      formData = service.prepareCreate(formData);
      importFormData(formData);

    }

    @Override
    protected void execStore() {
      IEmployeeService service = BEANS.get(IEmployeeService.class);
      EmployeeFormData formData = new EmployeeFormData();
      exportFormData(formData);
      service.create(formData);
    }
  }
}

package ch.ahoegger.docbox.client.administration.user;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.dto.FormData.SdkCommand;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.booleanfield.AbstractBooleanField;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.shared.TEXTS;

import ch.ahoegger.docbox.client.administration.user.UserForm.MainBox.CancelButton;
import ch.ahoegger.docbox.client.administration.user.UserForm.MainBox.FieldBox;
import ch.ahoegger.docbox.client.administration.user.UserForm.MainBox.FieldBox.ActiveField;
import ch.ahoegger.docbox.client.administration.user.UserForm.MainBox.FieldBox.AdministratorField;
import ch.ahoegger.docbox.client.administration.user.UserForm.MainBox.FieldBox.ChangePasswordField;
import ch.ahoegger.docbox.client.administration.user.UserForm.MainBox.FieldBox.FirstnameField;
import ch.ahoegger.docbox.client.administration.user.UserForm.MainBox.FieldBox.NameField;
import ch.ahoegger.docbox.client.administration.user.UserForm.MainBox.FieldBox.PasswordField;
import ch.ahoegger.docbox.client.administration.user.UserForm.MainBox.FieldBox.UsernameField;
import ch.ahoegger.docbox.client.administration.user.UserForm.MainBox.OkButton;
import ch.ahoegger.docbox.shared.administration.user.AdministratorLookupCall;
import ch.ahoegger.docbox.shared.administration.user.IUserService;
import ch.ahoegger.docbox.shared.administration.user.IUserTable;
import ch.ahoegger.docbox.shared.administration.user.UserFormData;
import ch.ahoegger.docbox.shared.administration.user.UserLookupCall;
import ch.ahoegger.docbox.shared.administration.user.UserValidationStatus;

/**
 * <h3>{@link UserForm}</h3>
 *
 * @author Andreas Hoegger
 */
@FormData(value = UserFormData.class, sdkCommand = SdkCommand.CREATE)
public class UserForm extends AbstractForm {
  public static enum FORM_MODE {
    NEW,
    PAGE,
    MODIFY
  }

  private final FORM_MODE m_formMode;

  public UserForm(FORM_MODE mode) {
    m_formMode = mode;
  }

  public FORM_MODE getFormMode() {
    return m_formMode;
  }

  @Override
  protected int getConfiguredDisplayHint() {
    return DISPLAY_HINT_VIEW;
  }

  @Override
  protected String getConfiguredDisplayViewId() {
    return VIEW_ID_CENTER;
  }

  @Override
  protected boolean getConfiguredMaximizeEnabled() {
    return true;
  }

  @Override
  protected void execInitForm() {
    switch (getFormMode()) {
      case MODIFY:
        setHandler(new EditHandler());
        break;
      case PAGE:
        setHandler(new PageHandler());
        break;
      case NEW:
        setHandler(new NewHandler());
        break;
    }
  }

  @Override
  protected void execStored() {
    getDesktop().dataChanged(IUserEntity.ENTITY_KEY);
  }

  public FieldBox getFieldBox() {
    return getFieldByClass(FieldBox.class);
  }

  public FirstnameField getFirstnameField() {
    return getFieldByClass(FirstnameField.class);
  }

  public UsernameField getUsernameField() {
    return getFieldByClass(UsernameField.class);
  }

  public PasswordField getPasswordField() {
    return getFieldByClass(PasswordField.class);
  }

  public ActiveField getActiveField() {
    return getFieldByClass(ActiveField.class);
  }

  public ChangePasswordField getChangePasswordField() {
    return getFieldByClass(ChangePasswordField.class);
  }

  public AdministratorField getAdministratorField() {
    return getFieldByClass(AdministratorField.class);
  }

  public NameField getNameField() {
    return getFieldByClass(NameField.class);
  }

  public OkButton getOkButton() {
    return getFieldByClass(OkButton.class);
  }

  public CancelButton getCancelButton() {
    return getFieldByClass(CancelButton.class);
  }

  public class MainBox extends AbstractGroupBox {

    @Order(10)
    public class FieldBox extends AbstractGroupBox {

      @Order(10)
      public class FirstnameField extends AbstractStringField {
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
          return IUserTable.FIRSTNAME_LENGTH;
        }
      }

      @Order(20)
      public class NameField extends AbstractStringField {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("Name");
        }

        @Override
        protected boolean getConfiguredMandatory() {
          return true;
        }

        @Override
        protected int getConfiguredMaxLength() {
          return IUserTable.NAME_LENGTH;
        }
      }

      @Order(30)
      public class UsernameField extends AbstractStringField {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("Username");
        }

        @Override
        protected boolean getConfiguredMandatory() {
          return true;
        }

        @Override
        protected boolean getConfiguredEnabled() {
          return false;
        }

        @Override
        protected boolean getConfiguredFormatLower() {
          return true;
        }

        @Override
        protected int getConfiguredMaxLength() {
          return IUserTable.USERNAME_LENGTH;
        }

        @Override
        protected String execValidateValue(String rawValue) {
          if (getFormMode() == FORM_MODE.NEW) {
            UserLookupCall call = new UserLookupCall();
            call.setKey(rawValue);
            if (!call.getDataByKey().isEmpty()) {
              throw new VetoException(TEXTS.get("Validate_UsernameAlreadyUsed"));
            }
          }
          return super.execValidateValue(rawValue);
        }
      }

      @Order(35)
      public class ChangePasswordField extends AbstractBooleanField {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("ChangePassword");
        }

        @Override
        protected void execChangedValue() {
          getPasswordField().setEnabled(getValue());
          getPasswordField().setMandatory(getValue());
          if (!getValue()) {
            getPasswordField().setValue(null);
          }
        }
      }

      @Order(40)
      public class PasswordField extends AbstractStringField {

        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("Password");
        }

        @Override
        protected boolean getConfiguredInputMasked() {
          return true;
        }

        @Override
        protected int getConfiguredMaxLength() {
          return IUserTable.PASSWORD_LENGTH;
        }
      }

      @Order(1020)
      public class ActiveField extends AbstractBooleanField {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("Active");
        }
      }

      @Order(2510)
      public class AdministratorField extends AbstractBooleanField {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("Administrator");
        }

        @Override
        protected Boolean execValidateValue(Boolean rawValue) {
          removeErrorStatus(UserValidationStatus.AdministratorAtLeastOne.class);
          if (!rawValue) {
            AdministratorLookupCall call = new AdministratorLookupCall();
            if (call.getDataByAll().stream()
                .filter(row -> !row.getKey().equals(getUsernameField().getValue())).count() < 1) {
              // only one admin left
              addErrorStatus(new UserValidationStatus.AdministratorAtLeastOne());
            }
          }
          return super.execValidateValue(rawValue);

        }
      }

    }

    @Order(200)
    public class OkButton extends AbstractOkButton {
    }

    @Order(210)
    public class CancelButton extends AbstractCancelButton {
    }

  }

  public class PageHandler extends AbstractFormHandler {
    @Override
    protected void execLoad() {
      if (getDisplayHint() != DISPLAY_HINT_VIEW) {
        setDisplayHint(DISPLAY_HINT_VIEW);
      }
      if (getDisplayViewId() != VIEW_ID_PAGE_TABLE) {
        setDisplayViewId(VIEW_ID_PAGE_TABLE);
      }

      getChangePasswordField().setVisible(false);
      getOkButton().setVisibleGranted(false);
      getCancelButton().setVisibleGranted(false);

      UserFormData formData = new UserFormData();
      exportFormData(formData);
      System.out.println(formData.getUsername().getValue());
      formData = BEANS.get(IUserService.class).load(formData);
      importFormData(formData);

      setEnabledGranted(false);
    }

    @Override
    protected void execStore() {
    }
  }

  public class NewHandler extends AbstractFormHandler {
    @Override
    protected void execLoad() {
      getUsernameField().setEnabled(true);
      getChangePasswordField().setVisible(false);
      UserFormData formData = new UserFormData();
      exportFormData(formData);
      formData = BEANS.get(IUserService.class).prepareCreate(formData);
      importFormData(formData);
    }

    @Override
    protected void execStore() {
      UserFormData formData = new UserFormData();
      exportFormData(formData);
      formData = BEANS.get(IUserService.class).create(formData);
      importFormData(formData);
    }
  }

  public class EditHandler extends AbstractFormHandler {
    @Override
    protected void execLoad() {
      getChangePasswordField().setVisible(true);
      getPasswordField().setEnabled(false);
      UserFormData formData = new UserFormData();
      exportFormData(formData);
      formData = BEANS.get(IUserService.class).load(formData);
      importFormData(formData);
    }

    @Override
    protected void execStore() {
      UserFormData formData = new UserFormData();
      exportFormData(formData);
      formData = BEANS.get(IUserService.class).store(formData);
      importFormData(formData);
    }
  }
}

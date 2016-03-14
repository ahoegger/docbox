package ch.ahoegger.docbox.client.administration.user;

import java.math.BigDecimal;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.dto.FormData.SdkCommand;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.datefield.AbstractDateField;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.listbox.AbstractListBox;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;

import ch.ahoegger.docbox.client.administration.user.UserForm.MainBox.CancelButton;
import ch.ahoegger.docbox.client.administration.user.UserForm.MainBox.FieldBox;
import ch.ahoegger.docbox.client.administration.user.UserForm.MainBox.FieldBox.FirstnameField;
import ch.ahoegger.docbox.client.administration.user.UserForm.MainBox.FieldBox.InsertDateField;
import ch.ahoegger.docbox.client.administration.user.UserForm.MainBox.FieldBox.NameField;
import ch.ahoegger.docbox.client.administration.user.UserForm.MainBox.FieldBox.PasswordField;
import ch.ahoegger.docbox.client.administration.user.UserForm.MainBox.FieldBox.RoleBox;
import ch.ahoegger.docbox.client.administration.user.UserForm.MainBox.FieldBox.UsernameField;
import ch.ahoegger.docbox.client.administration.user.UserForm.MainBox.FieldBox.ValidDateField;
import ch.ahoegger.docbox.client.administration.user.UserForm.MainBox.OkButton;
import ch.ahoegger.docbox.shared.administration.user.IUserService;
import ch.ahoegger.docbox.shared.administration.user.IUserTable;
import ch.ahoegger.docbox.shared.administration.user.UserFormData;
import ch.ahoegger.docbox.shared.document.DocumentFormData;
import ch.ahoegger.docbox.shared.document.IDocumentService;
import ch.ahoegger.docbox.shared.security.role.RoleLookupCall;

/**
 * <h3>{@link UserForm}</h3>
 *
 * @author aho
 */
@FormData(value = UserFormData.class, sdkCommand = SdkCommand.CREATE)
public class UserForm extends AbstractForm {

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

  public void startPage() {
    startInternal(new PageHandler());
  }

  public void startNew() {
    startInternal(new NewHandler());
  }

  public void startEdit() {
    startInternal(new EditHandler());
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

  public InsertDateField getInsertDateField() {
    return getFieldByClass(InsertDateField.class);
  }

  public ValidDateField getValidDateField() {
    return getFieldByClass(ValidDateField.class);
  }

  public RoleBox getRoleBox() {
    return getFieldByClass(RoleBox.class);
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
        protected int getConfiguredMaxLength() {
          return IUserTable.USERNAME_LENGTH;
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

      @Order(2000)
      public class InsertDateField extends AbstractDateField {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("CapturedOn");
        }

        @Override
        protected boolean getConfiguredMandatory() {
          return true;
        }
      }

      @Order(3000)
      public class ValidDateField extends AbstractDateField {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("ValidUntil");
        }
      }

      @Order(4000)
      public class RoleBox extends AbstractListBox<BigDecimal> {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("Role");
        }

        @Override
        protected int getConfiguredGridH() {
          return 6;
        }

        @Override
        protected Class<? extends ILookupCall<BigDecimal>> getConfiguredLookupCall() {
          return RoleLookupCall.class;
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

      getOkButton().setVisibleGranted(false);
      getCancelButton().setVisibleGranted(false);

      UserFormData formData = new UserFormData();
      exportFormData(formData);
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
    }

    @Override
    protected void execStore() {
      DocumentFormData formData = new DocumentFormData();
      exportFormData(formData);
      BEANS.get(IDocumentService.class).store(formData);
    }
  }

  public class EditHandler extends AbstractFormHandler {
    @Override
    protected void execLoad() {
      UserFormData formData = new UserFormData();
      exportFormData(formData);
      formData = BEANS.get(IUserService.class).load(formData);
      importFormData(formData);
    }

    @Override
    protected void execStore() {
    }
  }
}

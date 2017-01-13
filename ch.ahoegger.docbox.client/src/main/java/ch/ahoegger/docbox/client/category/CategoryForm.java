package ch.ahoegger.docbox.client.category;

import java.math.BigDecimal;
import java.util.Date;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.dto.FormData.SdkCommand;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.datefield.AbstractDateField;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.shared.TEXTS;

import ch.ahoegger.docbox.client.category.CategoryForm.MainBox.CancelButton;
import ch.ahoegger.docbox.client.category.CategoryForm.MainBox.FieldBox;
import ch.ahoegger.docbox.client.category.CategoryForm.MainBox.FieldBox.DescriptionField;
import ch.ahoegger.docbox.client.category.CategoryForm.MainBox.FieldBox.EndDateField;
import ch.ahoegger.docbox.client.category.CategoryForm.MainBox.FieldBox.NameField;
import ch.ahoegger.docbox.client.category.CategoryForm.MainBox.FieldBox.StartDateField;
import ch.ahoegger.docbox.client.category.CategoryForm.MainBox.OkButton;
import ch.ahoegger.docbox.or.definition.table.ICategoryTable;
import ch.ahoegger.docbox.shared.category.CategoryFormData;
import ch.ahoegger.docbox.shared.category.ICategoryService;
import ch.ahoegger.docbox.shared.validation.DateValidation;

/**
 * <h3>{@link CategoryForm}</h3>
 *
 * @author Andreas Hoegger
 */
@FormData(value = CategoryFormData.class, sdkCommand = SdkCommand.CREATE)
public class CategoryForm extends AbstractForm {

  private BigDecimal m_categoryId;

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("Category");
  }

  @Override
  protected int getConfiguredDisplayHint() {
    return DISPLAY_HINT_DIALOG;
  }

  @Override
  protected void execStored() {
    getDesktop().dataChanged(ICategoryEntity.ENTITY_KEY);
  }

  public void startNew() {
    startInternal(new NewHandler());
  }

  public void startModify() {
    startInternal(new ModifyHandler());
  }

  @FormData
  public BigDecimal getCategoryId() {
    return m_categoryId;
  }

  @FormData
  public void setCategoryId(BigDecimal categoryId) {
    m_categoryId = categoryId;
  }

  public DescriptionField getDescriptionField() {
    return getFieldByClass(DescriptionField.class);
  }

  public StartDateField getStartDateField() {
    return getFieldByClass(StartDateField.class);
  }

  public EndDateField getEndDateField() {
    return getFieldByClass(EndDateField.class);
  }

  public NameField getNameField() {
    return getFieldByClass(NameField.class);
  }

  public FieldBox getFieldBox() {
    return getFieldByClass(FieldBox.class);
  }

  public OkButton getOkButton() {
    return getFieldByClass(OkButton.class);
  }

  public CancelButton getCancelButton() {
    return getFieldByClass(CancelButton.class);
  }

  public class MainBox extends AbstractGroupBox {

    @Override
    protected int getConfiguredGridColumnCount() {
      return 1;
    }

    @Order(1000)
    public class FieldBox extends AbstractGroupBox {
      @Override
      protected int getConfiguredGridColumnCount() {
        return 1;
      }

      @Order(1000)
      public class NameField extends AbstractStringField {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("Name");
        }

        @Override
        protected int getConfiguredMaxLength() {
          return ICategoryTable.NAME_LENGTH;
        }

        @Override
        protected boolean getConfiguredMandatory() {
          return true;
        }
      }

      @Order(2000)
      public class DescriptionField extends AbstractStringField {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("Description");
        }

        @Override
        protected int getConfiguredMaxLength() {
          return ICategoryTable.DESCRIPTION_LENGTH;
        }
      }

      @Order(3000)
      public class StartDateField extends AbstractDateField {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("from");
        }

        @Override
        protected Date execValidateValue(Date rawValue) {
          DateValidation.validateFromTo(rawValue, getEndDateField().getValue());
          return rawValue;
        }
      }

      @Order(4000)
      public class EndDateField extends AbstractDateField {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("to");
        }

        @Override
        protected Date execValidateValue(Date rawValue) {
          DateValidation.validateFromTo(getStartDateField().getValue(), rawValue);
          return rawValue;
        }
      }

    }

    @Order(1000)
    public class OkButton extends AbstractOkButton {
    }

    @Order(1010)
    public class CancelButton extends AbstractCancelButton {
    }

  }

  public class NewHandler extends AbstractFormHandler {
    @Override
    protected void execLoad() {
      CategoryFormData formData = new CategoryFormData();
      exportFormData(formData);
      formData = BEANS.get(ICategoryService.class).prepareCreate(formData);
      importFormData(formData);
    }

    @Override
    protected void execStore() {
      CategoryFormData formData = new CategoryFormData();
      exportFormData(formData);
      formData = BEANS.get(ICategoryService.class).create(formData);
      importFormData(formData);
    }
  }

  public class ModifyHandler extends AbstractFormHandler {
    @Override
    protected void execLoad() {
      CategoryFormData formData = new CategoryFormData();
      exportFormData(formData);
      formData = BEANS.get(ICategoryService.class).load(formData);
      importFormData(formData);
    }

    @Override
    protected void execStore() {
      CategoryFormData formData = new CategoryFormData();
      exportFormData(formData);
      formData = BEANS.get(ICategoryService.class).store(formData);
      importFormData(formData);
    }
  }

}

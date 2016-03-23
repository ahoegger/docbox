package ch.ahoegger.docbox.client.conversation;

import java.math.BigDecimal;

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

import ch.ahoegger.docbox.client.conversation.ConversationForm.MainBox.CancelButton;
import ch.ahoegger.docbox.client.conversation.ConversationForm.MainBox.FieldBox;
import ch.ahoegger.docbox.client.conversation.ConversationForm.MainBox.FieldBox.EndDateField;
import ch.ahoegger.docbox.client.conversation.ConversationForm.MainBox.FieldBox.NameField;
import ch.ahoegger.docbox.client.conversation.ConversationForm.MainBox.FieldBox.NotesField;
import ch.ahoegger.docbox.client.conversation.ConversationForm.MainBox.FieldBox.StartDateField;
import ch.ahoegger.docbox.client.conversation.ConversationForm.MainBox.OkButton;
import ch.ahoegger.docbox.shared.category.ICategoryTable;
import ch.ahoegger.docbox.shared.conversation.ConversationFormData;
import ch.ahoegger.docbox.shared.conversation.IConversationService;
import ch.ahoegger.docbox.shared.conversation.IConversationTable;

/**
 * <h3>{@link ConversationForm}</h3>
 *
 * @author Andreas Hoegger
 */
@FormData(value = ConversationFormData.class, sdkCommand = SdkCommand.CREATE)
public class ConversationForm extends AbstractForm {

  private BigDecimal m_conversationId;

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("Conversation");
  }

  @Override
  protected int getConfiguredDisplayHint() {
    return DISPLAY_HINT_DIALOG;
  }

  @Override
  protected void execStored() {
    getDesktop().dataChanged(IConversationEntity.ENTITY_KEY);
  }

  public void startNew() {
    startInternal(new NewHandler());
  }

  public void startModify() {
    startInternal(new ModifyHandler());
  }

  @FormData
  public BigDecimal getConversationId() {
    return m_conversationId;
  }

  @FormData
  public void setConversationId(BigDecimal categoryId) {
    m_conversationId = categoryId;
  }

  public NotesField getNotesField() {
    return getFieldByClass(NotesField.class);
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
      public class NotesField extends AbstractStringField {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("Notes");
        }

        @Override
        protected int getConfiguredMaxLength() {
          return IConversationTable.NOTES_LENGTH;
        }
      }

      @Order(3000)
      public class StartDateField extends AbstractDateField {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("from");
        }
      }

      @Order(4000)
      public class EndDateField extends AbstractDateField {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("to");
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
      ConversationFormData formData = new ConversationFormData();
      exportFormData(formData);
      formData = BEANS.get(IConversationService.class).prepareCreate(formData);
      importFormData(formData);
    }

    @Override
    protected void execStore() {
      ConversationFormData formData = new ConversationFormData();
      exportFormData(formData);
      formData = BEANS.get(IConversationService.class).create(formData);
      importFormData(formData);
    }
  }

  public class ModifyHandler extends AbstractFormHandler {
    @Override
    protected void execLoad() {
      ConversationFormData formData = new ConversationFormData();
      exportFormData(formData);
      formData = BEANS.get(IConversationService.class).load(formData);
      importFormData(formData);
    }

    @Override
    protected void execStore() {
      ConversationFormData formData = new ConversationFormData();
      exportFormData(formData);
      formData = BEANS.get(IConversationService.class).store(formData);
      importFormData(formData);
    }
  }

}

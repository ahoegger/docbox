package ch.ahoegger.docbox.client.document.ocr;

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
import org.eclipse.scout.rt.shared.TEXTS;

import ch.ahoegger.docbox.client.document.ocr.DocumentOcrForm.MainBox.CancelButton;
import ch.ahoegger.docbox.client.document.ocr.DocumentOcrForm.MainBox.FieldBox.NotParsableField;
import ch.ahoegger.docbox.client.document.ocr.DocumentOcrForm.MainBox.FieldBox.OcrParsedField;
import ch.ahoegger.docbox.client.document.ocr.DocumentOcrForm.MainBox.FieldBox.TextField;
import ch.ahoegger.docbox.client.document.ocr.DocumentOcrForm.MainBox.OkButton;
import ch.ahoegger.docbox.shared.document.ocr.DocumentOcrFormData;
import ch.ahoegger.docbox.shared.ocr.IDocumentOcrService;

/**
 * <h3>{@link DocumentOcrForm}</h3>
 *
 * @author aho
 */
@FormData(value = DocumentOcrFormData.class, sdkCommand = SdkCommand.CREATE)
public class DocumentOcrForm extends AbstractForm {
  private Long m_documentId;

  public DocumentOcrForm() {
    setHandler(new FormHandler());
  }

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("DocumentContent");
  }

  @Override
  protected int getConfiguredDisplayHint() {
    return DISPLAY_HINT_DIALOG;
  }

  @FormData
  public void setDocumentId(Long documentId) {
    m_documentId = documentId;
  }

  @FormData
  public Long getDocumentId() {
    return m_documentId;
  }

  public TextField getTextField() {
    return getFieldByClass(TextField.class);
  }

  public NotParsableField getNotParsableField() {
    return getFieldByClass(NotParsableField.class);
  }

  public OcrParsedField getOcrParsedField() {
    return getFieldByClass(OcrParsedField.class);
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
      return 2;
    }

    @Order(1000)
    public class FieldBox extends AbstractGroupBox {
      @Override
      protected int getConfiguredGridColumnCount() {
        return 2;
      }

      @Order(1000)
      public class TextField extends AbstractStringField {
        @Override
        protected boolean getConfiguredLabelVisible() {
          return false;
        }

        @Override
        protected int getConfiguredGridW() {
          return 2;
        }

        @Override
        protected int getConfiguredGridH() {
          return 10;
        }

        @Override
        protected boolean getConfiguredMultilineText() {
          return true;
        }

        @Override
        protected boolean getConfiguredWrapText() {
          return true;
        }

        @Override
        protected int getConfiguredMaxLength() {
          return Integer.MAX_VALUE;
        }
      }

      @Order(2000)
      public class OcrParsedField extends AbstractBooleanField {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("OcrScanned");
        }

        @Override
        protected boolean getConfiguredEnabled() {
          return false;
        }
      }

      @Order(3000)
      public class NotParsableField extends AbstractBooleanField {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("NotParsable");
        }

        @Override
        protected boolean getConfiguredEnabled() {
          return false;
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

  public class FormHandler extends AbstractFormHandler {
    @Override
    protected void execLoad() {
      DocumentOcrFormData formData = new DocumentOcrFormData();
      exportFormData(formData);
      formData = BEANS.get(IDocumentOcrService.class).load(formData);
      importFormData(formData);
    }

    @Override
    protected void execStore() {
      // TODO
    }
  }

}

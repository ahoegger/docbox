package ch.ahoegger.docbox.client.document;

import java.math.BigDecimal;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.dto.FormData.SdkCommand;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.filechooserfield.AbstractFileChooserField;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.IGroupBoxBodyGrid;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.internal.VerticalSmartGroupBoxBodyGrid;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield.AbstractSmartField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.text.TEXTS;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;

import ch.ahoegger.docbox.client.document.ReplaceDocumentForm.MainBox.CancelButton;
import ch.ahoegger.docbox.client.document.ReplaceDocumentForm.MainBox.FieldBox;
import ch.ahoegger.docbox.client.document.ReplaceDocumentForm.MainBox.FieldBox.OcrLanguageField;
import ch.ahoegger.docbox.client.document.ReplaceDocumentForm.MainBox.OkButton;
import ch.ahoegger.docbox.shared.document.IDocumentService;
import ch.ahoegger.docbox.shared.document.ReplaceDocumentFormData;
import ch.ahoegger.docbox.shared.ocr.OcrLanguageCodeType;

/**
 * <h3>{@link ReplaceDocumentForm}</h3>
 *
 * @author aho
 */
@FormData(value = ReplaceDocumentFormData.class, sdkCommand = SdkCommand.CREATE)
public class ReplaceDocumentForm extends AbstractForm {
  private BigDecimal m_documentId;

  public ReplaceDocumentForm() {
    setHandler(new EditHandler());
  }

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("ReplaceDocument");
  }

  @Override
  protected String getConfiguredIconId() {
    return "font:icomoon \uf15c";
  }

  @Override
  protected int getConfiguredDisplayHint() {
    return DISPLAY_HINT_DIALOG;
  }

  @Override
  protected int getConfiguredModalityHint() {
    return MODALITY_HINT_MODAL;
  }

  @Override
  protected void execStored() {
    getDesktop().dataChanged(IDocumentEntity.ENTITY_KEY);
  }

  @FormData
  public void setDocumentId(BigDecimal documentId) {
    m_documentId = documentId;
  }

  @FormData
  public BigDecimal getDocumentId() {
    return m_documentId;
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  public FieldBox getFieldBox() {
    return getFieldByClass(FieldBox.class);
  }

  public OcrLanguageField getOcrLanguageField() {
    return getFieldByClass(OcrLanguageField.class);
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

      @Override
      protected Class<? extends IGroupBoxBodyGrid> getConfiguredBodyGrid() {
        return VerticalSmartGroupBoxBodyGrid.class;
      }

      @Order(100)
      public class DocumentField extends AbstractFileChooserField {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("Document");
        }

        @Override
        protected boolean getConfiguredMandatory() {
          return true;
        }
      }

      @Order(200)
      public class OcrLanguageField extends AbstractSmartField<String> {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("Language");
        }

        @Override
        protected Class<? extends ICodeType<?, String>> getConfiguredCodeType() {
          return OcrLanguageCodeType.class;
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

  public class EditHandler extends AbstractFormHandler {
    @Override
    protected void execLoad() {

    }

    @Override
    protected void execStore() {
      ReplaceDocumentFormData fd = new ReplaceDocumentFormData();
      exportFormData(fd);
      BEANS.get(IDocumentService.class).replaceDocument(fd);

    }
  }
}

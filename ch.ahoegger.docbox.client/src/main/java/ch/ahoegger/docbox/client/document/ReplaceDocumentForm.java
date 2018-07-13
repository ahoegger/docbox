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
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.text.TEXTS;

import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.CancelButton;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.AbstractField;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.CapturedDateField;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.CategoriesBox;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.ConversationField;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.DocumentDateField;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.DocumentField;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.LinksBox;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.LinksBox.OpenHtmlField;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.LinksBox.ShowOcrButton;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.OcrBox;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.OcrBox.OcrLanguageField;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.OcrBox.ParseOcrField;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.OriginalStorageField;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.PartnersField;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.PermissionsField;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.ValidDateField;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.OkButton;
import ch.ahoegger.docbox.shared.document.IDocumentService;
import ch.ahoegger.docbox.shared.document.ReplaceDocumentFormData;

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

  public DocumentDateField getDocumentDateField() {
    return getFieldByClass(DocumentDateField.class);
  }

  public CapturedDateField getCapturedDateField() {
    return getFieldByClass(CapturedDateField.class);
  }

  public ValidDateField getValidDateField() {
    return getFieldByClass(ValidDateField.class);
  }

  public OpenHtmlField getOpenHtmlField() {
    return getFieldByClass(OpenHtmlField.class);
  }

  public OriginalStorageField getOriginalStorageField() {
    return getFieldByClass(OriginalStorageField.class);
  }

  public DocumentField getDocumentField() {
    return getFieldByClass(DocumentField.class);
  }

  public AbstractField getAbstractTextField() {
    return getFieldByClass(AbstractField.class);
  }

  public CategoriesBox getCategoriesBox() {
    return getFieldByClass(CategoriesBox.class);
  }

  public PartnersField getPartnersField() {
    return getFieldByClass(PartnersField.class);
  }

  public PermissionsField getPermissionsField() {
    return getFieldByClass(PermissionsField.class);
  }

  public ConversationField getConversationField() {
    return getFieldByClass(ConversationField.class);
  }

  public ParseOcrField getParseOcrField() {
    return getFieldByClass(ParseOcrField.class);
  }

  public LinksBox getLinksBox() {
    return getFieldByClass(LinksBox.class);
  }

  public ShowOcrButton getShowOcrButton() {
    return getFieldByClass(ShowOcrButton.class);
  }

  public OcrBox getOcrBox() {
    return getFieldByClass(OcrBox.class);
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

      @Order(10)
      public class DocumentField extends AbstractFileChooserField {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("Document");
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

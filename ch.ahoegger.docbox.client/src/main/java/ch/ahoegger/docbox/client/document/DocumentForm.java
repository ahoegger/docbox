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
import org.eclipse.scout.rt.client.ui.form.fields.smartfield.AbstractSmartField;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;

import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.AbstractField;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.DocumentField;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.PartnerField;
import ch.ahoegger.docbox.shared.document.DocumentFormData;
import ch.ahoegger.docbox.shared.document.IDocumentService;
import ch.ahoegger.docbox.shared.partner.PatnerLookupCall;

/**
 * <h3>{@link DocumentForm}</h3>
 *
 * @author aho
 */
@FormData(value = DocumentFormData.class, sdkCommand = SdkCommand.CREATE)
public class DocumentForm extends AbstractForm {

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("Document");
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

  public void startNew() {
    startInternal(new NewHandler());
  }

  public PartnerField getPartnerField() {
    return getFieldByClass(PartnerField.class);
  }

  public DocumentField getDocumentField() {
    return getFieldByClass(DocumentField.class);
  }

  public AbstractField getAbstractTextField() {
    return getFieldByClass(AbstractField.class);
  }

  public class MainBox extends AbstractGroupBox {

    @Order(10)
    public class AbstractField extends AbstractStringField {
      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("Abstract");
      }

      @Override
      protected int getConfiguredGridH() {
        return 2;
      }

      @Override
      protected int getConfiguredMaxLength() {
        return 3000;
      }

      @Override
      protected boolean getConfiguredMultilineText() {
        return true;
      }

      @Override
      protected boolean getConfiguredWrapText() {
        return true;
      }
    }

    @Order(15)
    public class PartnerField extends AbstractSmartField<BigDecimal> {
      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("Partner");
      }

      @Override
      protected Class<? extends ILookupCall<BigDecimal>> getConfiguredLookupCall() {
        return PatnerLookupCall.class;
      }
    }

    @Order(20)
    public class DocumentField extends AbstractFileChooserField {
      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("Document");
      }
    }

    @Order(200)
    public class OkButton extends AbstractOkButton {
    }

    @Order(210)
    public class CancelButton extends AbstractCancelButton {
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

}

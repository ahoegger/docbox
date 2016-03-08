package ch.ahoegger.docbox.client.document;

import java.math.BigDecimal;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.dto.FormData.SdkCommand;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.datefield.AbstractDateField;
import org.eclipse.scout.rt.client.ui.form.fields.filechooserfield.AbstractFileChooserField;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.htmlfield.AbstractHtmlField;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield.AbstractSmartField;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.html.HTML;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;

import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.AbstractField;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.CapturedDateField;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.DocumentDateField;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.DocumentField;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.MyHtmlField;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.OriginalStorageField;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.PartnerField;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.ValidDateField;
import ch.ahoegger.docbox.shared.document.DocumentFormData;
import ch.ahoegger.docbox.shared.document.IDocumentService;
import ch.ahoegger.docbox.shared.partner.PartnerLookupCall;

/**
 * <h3>{@link DocumentForm}</h3>
 *
 * @author aho
 */
@FormData(value = DocumentFormData.class, sdkCommand = SdkCommand.CREATE)
public class DocumentForm extends AbstractForm {

  private Long m_documentId;
  private String m_documentPath;

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

  @FormData
  public void setDocumentId(Long documentId) {
    m_documentId = documentId;
  }

  @FormData
  public Long getDocumentId() {
    return m_documentId;
  }

  @FormData
  public String getDocumentPath() {
    return m_documentPath;
  }

  @FormData
  public void setDocumentPath(String documentPath) {
    m_documentPath = documentPath;
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

  public MyHtmlField getMyHtmlField() {
    return getFieldByClass(MyHtmlField.class);
  }

  public OriginalStorageField getOriginalStorageField() {
    return getFieldByClass(OriginalStorageField.class);
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
    public class FieldBox extends AbstractGroupBox {

      @Order(5)
      public class MyHtmlField extends AbstractHtmlField {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("Open");
        }

        @Override
        protected void execInitField() {
          String encodedHtml = HTML.link("http://www.google.ch", "Google").addAttribute("target", "_blank").toEncodedHtml();
          System.out.println(encodedHtml);
          setValue(encodedHtml);
          super.execInitField();
        }
      }

      @Order(7)
      public class CapturedDateField extends AbstractDateField {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("CapturedOn");
        }
      }

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
          return PartnerLookupCall.class;
        }
      }

      @Order(17)
      public class DocumentDateField extends AbstractDateField {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("DocumentDate");
        }
      }

      @Order(18)
      public class ValidDateField extends AbstractDateField {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("ValidUntil");
        }
      }

      @Order(19)
      public class OriginalStorageField extends AbstractStringField {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("OrignalStorage");
        }

        @Override
        protected int getConfiguredMaxLength() {
          return 128;
        }
      }

      @Order(20)
      public class DocumentField extends AbstractFileChooserField {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("Document");
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

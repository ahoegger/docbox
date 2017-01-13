package ch.ahoegger.docbox.client.document;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.dto.FormData.SdkCommand;
import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.ValueFieldMenuType;
import org.eclipse.scout.rt.client.ui.basic.table.TableAdapter;
import org.eclipse.scout.rt.client.ui.basic.table.TableEvent;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.FormEvent;
import org.eclipse.scout.rt.client.ui.form.FormListener;
import org.eclipse.scout.rt.client.ui.form.fields.IValueField;
import org.eclipse.scout.rt.client.ui.form.fields.booleanfield.AbstractBooleanField;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.datefield.AbstractDateField;
import org.eclipse.scout.rt.client.ui.form.fields.filechooserfield.AbstractFileChooserField;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.IGroupBoxBodyGrid;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.internal.VerticalSmartGroupBoxBodyGrid;
import org.eclipse.scout.rt.client.ui.form.fields.sequencebox.AbstractSequenceBox;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield.AbstractSmartField;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.resource.BinaryResource;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;

import ch.ahoegger.docbox.client.conversation.ConversationForm;
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
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.OriginalStorageField;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.ParseOcrField;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.PartnersField;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.PermissionsField;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.ValidDateField;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.OkButton;
import ch.ahoegger.docbox.client.document.field.AbstractCategoriesListBox;
import ch.ahoegger.docbox.client.document.field.AbstractPartnerTableField;
import ch.ahoegger.docbox.client.document.field.AbstractPartnerTableField.Table;
import ch.ahoegger.docbox.client.document.field.AbstractPermissionTableField;
import ch.ahoegger.docbox.client.document.ocr.DocumentOcrForm;
import ch.ahoegger.docbox.shared.conversation.ConversationLookupCall;
import ch.ahoegger.docbox.shared.document.DocumentFormData;
import ch.ahoegger.docbox.shared.document.IDocumentService;
import ch.ahoegger.docbox.shared.validation.DateValidation;

/**
 * <h3>{@link DocumentForm}</h3>
 *
 * @author Andreas Hoegger
 */
@FormData(value = DocumentFormData.class, sdkCommand = SdkCommand.CREATE)
public class DocumentForm extends AbstractForm {

  private BigDecimal m_documentId;
  private String m_documentPath;
  private Boolean m_hasOcrText;

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

  @FormData
  public String getDocumentPath() {
    return m_documentPath;
  }

  @FormData
  public void setDocumentPath(String documentPath) {
    m_documentPath = documentPath;
  }

  @FormData
  public Boolean getHasOcrText() {
    return m_hasOcrText;
  }

  @FormData
  public void setHasOcrText(Boolean hasOcrText) {
    m_hasOcrText = hasOcrText;
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

        @Override
        protected boolean getConfiguredVisible() {
          return false;
        }
      }

      @Order(20)
      public class LinksBox extends AbstractSequenceBox {
        @Override
        protected boolean getConfiguredLabelVisible() {
          return false;
        }

        @Override
        protected boolean getConfiguredAutoCheckFromTo() {
          return false;
        }

        @Order(10)
        public class OpenHtmlField extends AbstractDocumentLinkField {

        }

        @Order(2000)
        public class ShowOcrButton extends AbstractButton {
          @Override
          protected String getConfiguredLabel() {
            return TEXTS.get("ShowParsedContent");
          }

          @Override
          protected int getConfiguredDisplayStyle() {
            return DISPLAY_STYLE_LINK;
          }

          @Override
          protected boolean getConfiguredVisible() {
            // TODO
            return true;
          }

          @Override
          protected void execClickAction() {
            DocumentOcrForm form = new DocumentOcrForm();
            form.setDocumentId(getDocumentId());
            form.start();
          }
        }

      }

      @Order(30)
      public class CapturedDateField extends AbstractDateField {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("CapturedOn");
        }

        @Override
        protected boolean getConfiguredEnabled() {
          return false;
        }
      }

      @Order(40)
      public class AbstractField extends AbstractStringField {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("Abstract");
        }

        @Override
        protected boolean getConfiguredMandatory() {
          return true;
        }

        @Override
        protected int getConfiguredGridH() {
          return 2;
        }

        @Override
        protected double getConfiguredGridWeightY() {
          return 0;
        }

        @Override
        protected int getConfiguredGridW() {
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

      @Order(50)
      public class DocumentDateField extends AbstractDateField {

        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("DocumentDate");
        }

        @Override
        protected boolean getConfiguredMandatory() {
          return true;
        }
      }

      @Order(55)
      public class ValidDateField extends AbstractDateField {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("ValidUntil");
        }

        @Override
        protected Class<? extends IValueField> getConfiguredMasterField() {
          return DocumentDateField.class;
        }

        @Override
        protected Date execValidateValue(Date rawValue) {
          DateValidation.validateFromTo(getDocumentDateField().getValue(), rawValue);
          return rawValue;
        }
      }

      @Order(60)
      public class PartnersField extends AbstractPartnerTableField {

        @Override
        protected int getConfiguredGridH() {
          return 2;
        }

      }

      @Order(70)
      public class ConversationField extends AbstractSmartField<BigDecimal> {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("Conversation");
        }

        @Override
        protected void execInitField() {
          getPartnersField().getTable().addTableListener(new TableAdapter() {

            @Override
            public void tableChanged(TableEvent e) {
              switch (e.getType()) {
                case TableEvent.TYPE_ROWS_DELETED:
                case TableEvent.TYPE_ROWS_INSERTED:
                case TableEvent.TYPE_ROWS_UPDATED:
                  // TODO update smart value
                  break;
              }
            }
          });
        }

        @Override
        protected Class<? extends ILookupCall<BigDecimal>> getConfiguredLookupCall() {
          return ConversationLookupCall.class;
        }

        @Override
        protected void execPrepareLookup(ILookupCall<BigDecimal> call) {
          Table partnerTable = getPartnersField().getTable();

          call.setMaster(partnerTable.getPartnerColumn().getValues().stream().filter(v -> v != null).collect(Collectors.toList()));
          super.execPrepareLookup(call);
        }

        @Order(1000)
        public class NewConversationMenu extends AbstractMenu {
          @Override
          protected String getConfiguredText() {
            return TEXTS.get("New");
          }

          @Override
          protected Set<? extends IMenuType> getConfiguredMenuTypes() {
            return CollectionUtility.hashSet(ValueFieldMenuType.NotNull, ValueFieldMenuType.Null);
          }

          @Override
          protected void execAction() {
            ConversationForm form = new ConversationForm();
            form.startNew();
            form.addFormListener(new FormListener() {
              @Override
              public void formChanged(FormEvent e) {
                if (FormEvent.TYPE_STORE_AFTER == e.getType()) {
                  setValue(form.getConversationId());
                }
              }
            });
          }
        }

      }

      @Order(75)
      public class ParseOcrField extends AbstractBooleanField {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("ParseContent");
        }
      }

      @Order(80)
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

      @Order(90)
      public class PermissionsField extends AbstractPermissionTableField {

        @Override
        protected int getConfiguredGridH() {
          return 2;
        }

      }

      @Order(110)
      public class CategoriesBox extends AbstractCategoriesListBox {
        @Override
        protected int getConfiguredGridH() {
          return 9;
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

  public static abstract class AbstractDocumentFormHandler extends AbstractFormHandler {
    public AbstractDocumentFormHandler(DocumentForm form) {
      setFormInternal(form);
    }

    @Override
    protected void execLoad() {

    }

    @Override
    public DocumentForm getForm() {
      return (DocumentForm) super.getForm();
    }
  }

  public static class DocumentFormNewHandler extends AbstractDocumentFormHandler {
    private BigDecimal m_partnerId;
    private BigDecimal m_conversationId;
    private BinaryResource m_documentResource;

    /**
     * @param form
     */
    public DocumentFormNewHandler(DocumentForm form) {
      super(form);
    }

    public void setPartnerId(BigDecimal partnerId) {
      m_partnerId = partnerId;
    }

    public BigDecimal getPartnerId() {
      return m_partnerId;
    }

    public void setConversationId(BigDecimal conversationId) {
      m_conversationId = conversationId;
    }

    public BigDecimal getConversationId() {
      return m_conversationId;
    }

    /**
     * @param firstElement
     */
    public void setDocumentResource(BinaryResource documentResource) {
      m_documentResource = documentResource;
    }

    public BinaryResource getDocumentResource() {
      return m_documentResource;
    }

    @Override
    protected void execLoad() {
      DocumentFormData formData = new DocumentFormData();
      formData = BEANS.get(IDocumentService.class).prepareCreate(formData);
      getForm().importFormData(formData);
      getForm().getDocumentField().setVisible(true);
      getForm().getDocumentField().setMandatory(true);
      getForm().getOpenHtmlField().setVisible(false);
      getForm().getShowOcrButton().setVisible(false);
      if (getPartnerId() != null) {
        getForm().getPartnersField().setValue(CollectionUtility.hashSet(getPartnerId()));
      }
      if (getConversationId() != null) {
        getForm().getConversationField().setValue(getConversationId());
      }
      if (getDocumentResource() != null) {
        getForm().getDocumentField().setValue(getDocumentResource());
      }
      super.execLoad();
    }

    @Override
    protected void execStore() {
      DocumentFormData formData = new DocumentFormData();
      getForm().exportFormData(formData);
      BEANS.get(IDocumentService.class).create(formData);
    }

  }

  public static class DocumentFormEditHandler extends AbstractDocumentFormHandler {
    private final BigDecimal m_documentId;

    /**
     * @param form
     */
    public DocumentFormEditHandler(DocumentForm form, BigDecimal documentId) {
      super(form);
      m_documentId = documentId;
    }

    public BigDecimal getDocumentId() {
      return m_documentId;
    }

    @Override
    protected void execLoad() {
      DocumentFormData formData = new DocumentFormData();
      formData.setDocumentId(getDocumentId());
      formData = BEANS.get(IDocumentService.class).load(formData);
      getForm().importFormData(formData);
      getForm().setTitle(formData.getAbstract().getValue());
      getForm().getOpenHtmlField().setDocumentId(formData.getDocumentId());
      getForm().getShowOcrButton().setVisible(getForm().getHasOcrText());

    }

    @Override
    protected void execStore() {
      DocumentFormData formData = new DocumentFormData();
      getForm().exportFormData(formData);
      BEANS.get(IDocumentService.class).store(formData);
    }

  }

  public static class DocumentFormPageHandler extends AbstractDocumentFormHandler {
    private final BigDecimal m_documentId;

    public DocumentFormPageHandler(DocumentForm form, BigDecimal documentId) {
      super(form);
      m_documentId = documentId;
    }

    public BigDecimal getDocumentId() {
      return m_documentId;
    }

    @Override
    protected void execLoad() {
      if (getForm().getDisplayHint() != DISPLAY_HINT_VIEW) {
        getForm().setDisplayHint(DISPLAY_HINT_VIEW);
      }
      if (getForm().getDisplayViewId() != VIEW_ID_PAGE_TABLE) {
        getForm().setDisplayViewId(VIEW_ID_PAGE_TABLE);
      }

      getForm().getOkButton().setVisibleGranted(false);
      getForm().getCancelButton().setVisibleGranted(false);

      DocumentFormData formData = new DocumentFormData();
      formData.setDocumentId(getDocumentId());
      formData = BEANS.get(IDocumentService.class).load(formData);
      getForm().importFormData(formData);

      getForm().setTitle(formData.getAbstract().getValue());

      getForm().getOpenHtmlField().setDocumentId(formData.getDocumentId());

      getForm().setEnabledGranted(false);
      getForm().getShowOcrButton().setEnabledGranted(true);
      getForm().getShowOcrButton().setEnabled(getForm().getHasOcrText());
      getForm().getShowOcrButton().setVisible(getForm().getHasOcrText());
    }

    @Override
    protected void execStore() {
    }
  }

}

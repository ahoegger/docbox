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
import org.eclipse.scout.rt.client.ui.desktop.datachange.IDataChangeListener;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.FormEvent;
import org.eclipse.scout.rt.client.ui.form.FormListener;
import org.eclipse.scout.rt.client.ui.form.fields.IValueField;
import org.eclipse.scout.rt.client.ui.form.fields.booleanfield.AbstractBooleanField;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.datefield.AbstractDateField;
import org.eclipse.scout.rt.client.ui.form.fields.filechooserfield.AbstractFileChooserField;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.IGroupBoxBodyGrid;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.internal.VerticalSmartGroupBoxBodyGrid;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield.AbstractSmartField;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.client.ui.form.fields.tabbox.AbstractTabBox;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.resource.BinaryResource;
import org.eclipse.scout.rt.platform.text.TEXTS;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.platform.util.ObjectUtility;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;

import ch.ahoegger.docbox.client.conversation.ConversationForm;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.CancelButton;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.AbstractField;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.CapturedDateField;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.DocumentDateField;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.DocumentField;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.TabBox;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.TabBox.MetaDataBox;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.TabBox.MetaDataBox.CategoriesBox;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.TabBox.MetaDataBox.ConversationField;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.TabBox.MetaDataBox.OriginalStorageField;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.TabBox.MetaDataBox.PartnersField;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.TabBox.MetaDataBox.PermissionsField;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.TabBox.MetaDataBox.ValidDateField;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.TabBox.OcrBox;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.TabBox.OcrBox.OcrResultBox;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.OkButton;
import ch.ahoegger.docbox.client.document.ReplaceDocumentForm.MainBox.FieldBox.OcrLanguageField;
import ch.ahoegger.docbox.client.document.field.AbstractCategoriesListBox;
import ch.ahoegger.docbox.client.document.field.AbstractPartnerTableField;
import ch.ahoegger.docbox.client.document.field.AbstractPartnerTableField.Table;
import ch.ahoegger.docbox.client.document.field.AbstractPermissionTableField;
import ch.ahoegger.docbox.client.document.ocr.IDocumentOcrEntity;
import ch.ahoegger.docbox.client.document.ocr.OcrParsedEent;
import ch.ahoegger.docbox.shared.conversation.ConversationLookupCall;
import ch.ahoegger.docbox.shared.document.DocumentFormData;
import ch.ahoegger.docbox.shared.document.IDocumentService;
import ch.ahoegger.docbox.shared.document.OcrResultGroupBoxData;
import ch.ahoegger.docbox.shared.ocr.IDocumentOcrService;
import ch.ahoegger.docbox.shared.ocr.OcrLanguageCodeType;
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
  private IDataChangeListener m_internalDataChangeListener;

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("Document");
  }

  @Override
  protected String getConfiguredIconId() {
    return "font:icomoon \uf15c";
  }

  @Override
  protected boolean getConfiguredSaveNeededVisible() {
    return true;
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
    if (m_internalDataChangeListener == null) {
      m_internalDataChangeListener = event -> {
        OcrParsedEent parsedEvent = (OcrParsedEent) event;
        if (ObjectUtility.equals(parsedEvent.getDocumentId(), getDocumentId())) {
          reloadOcrResult();
        }
      };
    }
    getDesktop().dataChangeListeners().add(m_internalDataChangeListener, true, IDocumentOcrEntity.ENTITY_KEY);
  }

  @Override
  protected void execDisposeForm() {
    if (m_internalDataChangeListener != null) {
      getDesktop().dataChangeListeners().remove(m_internalDataChangeListener, IDocumentOcrEntity.ENTITY_KEY);
    }
  }

  @Override
  protected void execStored() {
    getDesktop().dataChanged(IDocumentEntity.ENTITY_KEY);
  }

  protected void reloadOcrResult() {
    OcrResultGroupBoxData ocrFormData = new OcrResultGroupBoxData();
    ocrFormData.setDocumentId(getDocumentId());
    ocrFormData = BEANS.get(IDocumentOcrService.class).load(ocrFormData);
    if (ocrFormData != null) {
      getOcrResultBox().setVisible(true);
      getOcrResultBox().getOcrParsedField().setValue(ocrFormData.getOcrParsed().getValue());
      getOcrResultBox().getOcrParsedField().markSaved();
      getOcrResultBox().getParsedTextField().setValue(ocrFormData.getParsedText().getValue());
      getOcrResultBox().getParsedTextField().markSaved();
      getOcrResultBox().getParseCountField().setValue(ocrFormData.getParseCount().getValue());
      getOcrResultBox().getParseCountField().markSaved();
      getOcrResultBox().getParseFailedReasonField().setValue(ocrFormData.getParseFailedReason().getValue());
      getOcrResultBox().getParseFailedReasonField().markSaved();
    }
    else {
      getOcrResultBox().setVisible(false);
    }

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

  public TabBox getTabBox() {
    return getFieldByClass(TabBox.class);
  }

  public MetaDataBox getMetaDataBox() {
    return getFieldByClass(MetaDataBox.class);
  }

  public OcrBox getOcrBox() {
    return getFieldByClass(OcrBox.class);
  }

  public OcrLanguageField getOcrLanguageField() {
    return getFieldByClass(OcrLanguageField.class);
  }

  public OcrResultBox getOcrResultBox() {
    return getFieldByClass(OcrResultBox.class);
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

      @Order(100)
      public class DocumentField extends AbstractFileChooserField {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("Document");
        }

        @Override
        protected boolean getConfiguredVisible() {
          return false;
        }

        @Override
        protected int getConfiguredGridW() {
          return 2;
        }
      }

      @Order(200)
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

      @Order(300)
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

      @Order(400)
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

      @Order(500)
      public class TabBox extends AbstractTabBox {

        @Order(1000)
        public class MetaDataBox extends AbstractGroupBox {
          @Override
          protected String getConfiguredLabel() {
            return TEXTS.get("MetaData");
          }

          @Override
          protected Class<? extends IGroupBoxBodyGrid> getConfiguredBodyGrid() {
            return VerticalSmartGroupBoxBodyGrid.class;
          }

          @Order(100)
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

          @Order(200)
          public class PartnersField extends AbstractPartnerTableField {

            @Override
            protected int getConfiguredGridH() {
              return 2;
            }

          }

          @Order(300)
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

          @Order(400)
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

          @Order(500)
          public class PermissionsField extends AbstractPermissionTableField {

            @Override
            protected int getConfiguredGridH() {
              return 2;
            }

          }

          @Order(600)
          public class CategoriesBox extends AbstractCategoriesListBox {
            @Override
            protected int getConfiguredGridH() {
              return 8;
            }
          }
        }

        @Order(2000)
        public class OcrBox extends AbstractGroupBox {

          @Override
          protected String getConfiguredLabel() {
            return TEXTS.get("OCR");
          }

          @Order(100)
          public class ParseOcrField extends AbstractBooleanField {
            @Override
            protected String getConfiguredLabel() {
              return TEXTS.get("ParseContent");
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

          @Order(300)
          public class OcrResultBox extends AbstractOcrResultGroupBox {

          }

        }

      }

    }

    @Order(1000)
    public class OkButton extends AbstractOkButton {
    }

    @Order(1010)
    public class CancelButton extends AbstractCancelButton {
    }

////    @Order(1000)
////    public class OpenPdfMenu extends AbstractMenu {
////      @Override
////      protected String getConfiguredText() {
////        return TEXTS.get("OpenPdf");
////      }
////
////      @Override
////      protected void execAction() {
////        StringBuilder linkBuilder = new StringBuilder();
////        linkBuilder.append(CONFIG.getPropertyValue(DocumentLinkURI.class));
////        linkBuilder.append("?").append(CONFIG.getPropertyValue(DocumentLinkDocumentIdParamName.class)).append("=").append(getDocumentId());
////        getDesktop().openUri(linkBuilder.toString(), OpenUriAction.NEW_WINDOW);
////      }
////    }
//
//    @Order(2000)
//    public class OpenOcrTextMenu extends AbstractMenu {
//      @Override
//      protected String getConfiguredText() {
//        return TEXTS.get("ShowParsedContent");
//      }
//
//      @Override
//      protected void execAction() {
//        DocumentOcrForm form = new DocumentOcrForm();
//        form.setDocumentId(getDocumentId());
//        form.start();
//      }
//    }

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
      getForm().getOcrResultBox().setVisible(false);
//      getForm().getMainBox().getMenuByClass(OpenPdfMenu.class).setVisible(false);
//      getForm().getMainBox().getMenuByClass(OpenOcrTextMenu.class).setVisible(false);
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
      getForm().reloadOcrResult();
    }

    @Override
    protected void execStore() {
      DocumentFormData formData = new DocumentFormData();
      getForm().exportFormData(formData);
      BEANS.get(IDocumentService.class).store(formData);
      if (getForm().getOcrResultBox().isVisible()) {
        OcrResultGroupBoxData ocrFormData = new OcrResultGroupBoxData();
        ocrFormData.setDocumentId(getDocumentId());
        ocrFormData.getParseCount().setValue(getForm().getOcrResultBox().getParseCountField().getValue());
        ocrFormData.getOcrParsed().setValue(getForm().getOcrResultBox().getOcrParsedField().getValue());
        ocrFormData.getParsedText().setValue(getForm().getOcrResultBox().getParsedTextField().getValue());
        ocrFormData.getParseFailedReason().setValue(getForm().getOcrResultBox().getParseFailedReasonField().getValue());
        BEANS.get(IDocumentOcrService.class).store(ocrFormData);
      }
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

      getForm().setAllEnabled(false);
      getForm().reloadOcrResult();

    }

  }

}

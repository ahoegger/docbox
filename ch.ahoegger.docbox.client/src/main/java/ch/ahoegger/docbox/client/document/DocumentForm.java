package ch.ahoegger.docbox.client.document;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.dto.FormData.SdkCommand;
import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.TableMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.ValueFieldMenuType;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.TableAdapter;
import org.eclipse.scout.rt.client.ui.basic.table.TableEvent;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.FormEvent;
import org.eclipse.scout.rt.client.ui.form.FormListener;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.datefield.AbstractDateField;
import org.eclipse.scout.rt.client.ui.form.fields.filechooserfield.AbstractFileChooserField;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.IGroupBoxBodyGrid;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.internal.VerticalSmartGroupBoxBodyGrid;
import org.eclipse.scout.rt.client.ui.form.fields.htmlfield.AbstractHtmlField;
import org.eclipse.scout.rt.client.ui.form.fields.listbox.AbstractListBox;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield.AbstractSmartField;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.client.ui.messagebox.MessageBox;
import org.eclipse.scout.rt.client.ui.messagebox.MessageBoxes;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.config.CONFIG;
import org.eclipse.scout.rt.platform.html.HTML;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;

import ch.ahoegger.docbox.client.category.CategoryForm;
import ch.ahoegger.docbox.client.category.CategoryTablePage;
import ch.ahoegger.docbox.client.conversation.ConversationForm;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.CancelButton;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.AbstractField;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.CapturedDateField;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.CategoriesBox;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.ConversationField;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.DocumentDateField;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.DocumentField;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.OpenHtmlField;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.OriginalStorageField;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.PartnersField;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.PermissionsField;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.ValidDateField;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.OkButton;
import ch.ahoegger.docbox.client.document.DocumentLinkProperties.DocumentLinkDocumentIdParamName;
import ch.ahoegger.docbox.client.document.DocumentLinkProperties.DocumentLinkURI;
import ch.ahoegger.docbox.client.document.field.AbstractPartnerTableField;
import ch.ahoegger.docbox.client.document.field.AbstractPartnerTableField.Table;
import ch.ahoegger.docbox.client.document.field.AbstractPermissionTableField;
import ch.ahoegger.docbox.shared.category.CategoryLookupCall;
import ch.ahoegger.docbox.shared.category.ICategoryService;
import ch.ahoegger.docbox.shared.conversation.ConversationLookupCall;
import ch.ahoegger.docbox.shared.document.DocumentFormData;
import ch.ahoegger.docbox.shared.document.IDocumentService;

/**
 * <h3>{@link DocumentForm}</h3>
 *
 * @author Andreas Hoegger
 */
@FormData(value = DocumentFormData.class, sdkCommand = SdkCommand.CREATE)
public class DocumentForm extends AbstractForm {

  private Long m_documentId;
  private String m_documentPath;
  private boolean m_addingFields = false;

  @Override
  public boolean isShowing() {
    if (m_addingFields) {
      return false;
    }
    return super.isShowing();
  }

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
    getDesktop().dataChanged(DocumentTablePage.DOCUMENT_ENTITY);
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
      public class OpenHtmlField extends AbstractHtmlField {

        @Override
        protected void execInitField() {
          StringBuilder linkBuilder = new StringBuilder();
          linkBuilder.append(CONFIG.getPropertyValue(DocumentLinkURI.class));
          linkBuilder.append("?").append(CONFIG.getPropertyValue(DocumentLinkDocumentIdParamName.class)).append("=").append(getDocumentId());
          String encodedHtml = HTML.link(linkBuilder.toString(), TEXTS.get("Open")).addAttribute("target", "_blank").toEncodedHtml();
          setValue(encodedHtml);
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
            Table partnerTable = getPartnersField().getTable();

            BigDecimal partnerId = partnerTable.getPartnerColumn().getValues().stream().filter(id -> id != null).findFirst().orElse(null);
            form.getPartnerField().setValue(partnerId);
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
      public class CategoriesBox extends AbstractListBox<BigDecimal> {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("Categories");
        }

        @Override
        protected int getConfiguredGridH() {
          return 8;
        }

        @Override
        protected Class<? extends ILookupCall<BigDecimal>> getConfiguredLookupCall() {
          return CategoryLookupCall.class;
        }

        @Override
        protected void execInitField() {
          registerDataChangeListener(CategoryTablePage.CATEGORY_ENTITY);
        }

        @Override
        protected void execDataChanged(Object... dataTypes) {
          loadListBoxData();
          super.execDataChanged(dataTypes);
        }

        @Override
        protected void execPrepareLookup(ILookupCall<BigDecimal> call) {
          call.setMaster(CategoryTablePage.CATEGORY_ENTITY);
        }

        public class CategoriesTable extends DefaultListBoxTable {

          @Order(1000)
          public class NewCategoryMenu extends AbstractMenu {
            @Override
            protected String getConfiguredText() {
              return TEXTS.get("New");
            }

            @Override
            protected Set<? extends IMenuType> getConfiguredMenuTypes() {
              return CollectionUtility.hashSet(TableMenuType.EmptySpace, TableMenuType.SingleSelection);
            }

            @Override
            protected void execAction() {
              CategoryForm form = new CategoryForm();
              form.startNew();
              form.addFormListener(new FormListener() {

                @Override
                public void formChanged(FormEvent e) {
                  if (e.getType() == FormEvent.TYPE_STORE_AFTER) {

                    ITableRow newRow = getKeyColumn().findRow(form.getCategoryId());
                    if (newRow != null) {
                      checkRow(newRow, true);
                    }
                  }
                }
              });
            }
          }

          @Order(1500)
          public class EditMenu extends AbstractMenu {
            @Override
            protected String getConfiguredText() {
              return TEXTS.get("Edit");
            }

            @Override
            protected Set<? extends IMenuType> getConfiguredMenuTypes() {
              return CollectionUtility.hashSet(TableMenuType.SingleSelection);
            }

            @Override
            protected void execAction() {
              CategoryForm form = new CategoryForm();
              form.setCategoryId(getKeyColumn().getSelectedValue());
              form.startModify();
            }
          }

          @Order(2000)
          public class DeleteCategoryMenu extends AbstractMenu {
            @Override
            protected String getConfiguredText() {
              return TEXTS.get("Delete");
            }

            @Override
            protected double getConfiguredViewOrder() {
              // TODO check admin
              return super.getConfiguredViewOrder();
            }

            @Override
            protected Set<? extends IMenuType> getConfiguredMenuTypes() {
              return CollectionUtility.hashSet(TableMenuType.SingleSelection);
            }

            @Override
            protected void execAction() {
              if (MessageBox.YES_OPTION == MessageBoxes.createYesNo()
                  .withHeader(TEXTS.get("Delete"))
                  .withBody(TEXTS.get("VerificationDelete", getTextColumn().getSelectedDisplayText())).show()) {
                BEANS.get(ICategoryService.class).delete(getKeyColumn().getSelectedValue().longValue());
                getDesktop().dataChanged(CategoryTablePage.CATEGORY_ENTITY);
              }
            }
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

      DocumentFormData formData = new DocumentFormData();
      exportFormData(formData);
      formData = BEANS.get(IDocumentService.class).load(formData);
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
      DocumentFormData formData = new DocumentFormData();
      formData = BEANS.get(IDocumentService.class).prepareCreate(formData);
      importFormData(formData);
      getDocumentField().setVisible(true);
      getDocumentField().setMandatory(true);
      getOpenHtmlField().setVisible(false);
    }

    @Override
    protected void execStore() {
      DocumentFormData formData = new DocumentFormData();
      exportFormData(formData);
      BEANS.get(IDocumentService.class).create(formData);
    }
  }

  public class EditHandler extends AbstractFormHandler {
    @Override
    protected void execLoad() {
      DocumentFormData formData = new DocumentFormData();
      exportFormData(formData);
      formData = BEANS.get(IDocumentService.class).load(formData);
      importFormData(formData);
    }

    @Override
    protected void execStore() {
      DocumentFormData formData = new DocumentFormData();
      exportFormData(formData);
      BEANS.get(IDocumentService.class).store(formData);

    }
  }

}

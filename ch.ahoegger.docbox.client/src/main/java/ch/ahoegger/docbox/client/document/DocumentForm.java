package ch.ahoegger.docbox.client.document;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.dto.FormData.SdkCommand;
import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.TableMenuType;
import org.eclipse.scout.rt.client.ui.basic.cell.Cell;
import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractSmartColumn;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.IFormField;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.datefield.AbstractDateField;
import org.eclipse.scout.rt.client.ui.form.fields.filechooserfield.AbstractFileChooserField;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.IGroupBoxBodyGrid;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.internal.HorizontalGroupBoxBodyGrid;
import org.eclipse.scout.rt.client.ui.form.fields.htmlfield.AbstractHtmlField;
import org.eclipse.scout.rt.client.ui.form.fields.listbox.AbstractListBox;
import org.eclipse.scout.rt.client.ui.form.fields.placeholder.AbstractPlaceholderField;
import org.eclipse.scout.rt.client.ui.form.fields.sequencebox.AbstractSequenceBox;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield.AbstractSmartField;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.client.ui.form.fields.tablefield.AbstractTableField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.config.CONFIG;
import org.eclipse.scout.rt.platform.html.HTML;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;
import org.eclipse.scout.rt.shared.services.lookup.ILookupRow;

import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.CancelButton;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.AbstractField;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.CapturedDateField;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.CategoriesBox;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.DocumentDateField;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.DocumentField;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.OpenHtmlField;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.OriginalStorageField;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.OwnerField;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.PartnerBox;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.PartnerBox.PartnerSequenceBox;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.PartnerBox.PartnerSequenceBox.MultipleButton;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.PartnerBox.PartnerSequenceBox.PartnerField;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.PartnerBox.PartnersField;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.PartnerBox.PartnersField.Table;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.PermissionsField;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.PlaceHolder01Field;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.TagField;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.FieldBox.ValidDateField;
import ch.ahoegger.docbox.client.document.DocumentForm.MainBox.OkButton;
import ch.ahoegger.docbox.client.document.DocumentLinkProperties.DocumentLinkDocumentIdParamName;
import ch.ahoegger.docbox.client.document.DocumentLinkProperties.DocumentLinkURI;
import ch.ahoegger.docbox.shared.administration.user.UserLookupCall;
import ch.ahoegger.docbox.shared.category.CategoryLookupCall;
import ch.ahoegger.docbox.shared.document.DocumentFormData;
import ch.ahoegger.docbox.shared.document.DocumentFormData.Partners;
import ch.ahoegger.docbox.shared.document.IDocumentService;
import ch.ahoegger.docbox.shared.partner.PartnerLookupCall;
import ch.ahoegger.docbox.shared.permission.PermissionLookupCall;

/**
 * <h3>{@link DocumentForm}</h3>
 *
 * @author aho
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

  public OwnerField getOwnerField() {
    return getFieldByClass(OwnerField.class);
  }

  public TagField getTagField() {
    return getFieldByClass(TagField.class);
  }

  public PartnerBox getPartnerBox() {
    return getFieldByClass(PartnerBox.class);
  }

  public CategoriesBox getCategoriesBox() {
    return getFieldByClass(CategoriesBox.class);
  }

  public PlaceHolder01Field getPlaceHolder01Field() {
    return getFieldByClass(PlaceHolder01Field.class);
  }

  public PartnerField getPartnerField() {
    return getFieldByClass(PartnerField.class);
  }

  public PartnersField getPartnersField() {
    return getFieldByClass(PartnersField.class);
  }

  public PartnerSequenceBox getPartnerSequenceBox() {
    return getFieldByClass(PartnerSequenceBox.class);
  }

  public MultipleButton getMultipleButton() {
    return getFieldByClass(MultipleButton.class);
  }

  public PermissionsField getPermissionsField() {
    return getFieldByClass(PermissionsField.class);
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
        return HorizontalGroupBoxBodyGrid.class;
      }

      @Order(10)
      public class DocumentField extends AbstractFileChooserField {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("Document");
        }

        @Override
        protected boolean getConfiguredMandatory() {
          return true;
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

      @Order(60)
      public class ValidDateField extends AbstractDateField {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("ValidUntil");
        }
      }

      @Order(70)
      public class OwnerField extends AbstractSmartField<BigDecimal> {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("Owner");
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
      public class TagField extends AbstractSmartField<String> {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("Tag");
        }

        @Override
        protected boolean getConfiguredEnabled() {
          return false;
        }
      }

      @Order(100)
      public class PlaceHolder01Field extends AbstractPlaceholderField {
      }

      @Order(110)
      public class PartnerBox extends AbstractGroupBox {

        @Override
        protected int getConfiguredGridW() {
          return 1;
        }

        @Override
        protected int getConfiguredGridColumnCount() {
          return 1;
        }

        @Override
        protected int getConfiguredGridH() {
          return 3;
        }

        @Order(10)
        public class PartnerSequenceBox extends AbstractSequenceBox {
          @Override
          protected String getConfiguredLabel() {
            return TEXTS.get("Partner");
          }

          @Override
          protected boolean getConfiguredAutoCheckFromTo() {
            return false;
          }

          @Order(10)
          @FormData(sdkCommand = SdkCommand.IGNORE)
          public class PartnerField extends AbstractSmartField<BigDecimal> {
            @Override
            protected String getConfiguredLabel() {
              return TEXTS.get("Partner");
            }

            @Override
            protected Class<? extends ILookupCall<BigDecimal>> getConfiguredLookupCall() {
              return PartnerLookupCall.class;
            }

            @Override
            protected void execChangedValue() {
              Table table = getPartnersField().getTable();
              table.deleteAllRows();
              if (getValue() != null) {
                ITableRow row = table.createRow();
                table.getPartnerColumn().setValue(row, getValue());
                table.addRow(row);
              }
            }

          }

          @Order(2000)
          public class MultipleButton extends AbstractButton {
            @Override
            protected String getConfiguredLabel() {
              return TEXTS.get("Multiple");
            }

            @Override
            protected boolean getConfiguredGridUseUiWidth() {
              return true;
            }

            @Override
            protected void execClickAction() {
              getPartnerSequenceBox().setVisible(false);
              getPartnersField().setVisible(true);
            }
          }

        }

        @Order(20)
        public class PartnersField extends AbstractTableField<PartnersField.Table> {

          @Override
          protected String getConfiguredLabel() {
            return TEXTS.get("Partners");
          }

          @Override
          protected boolean getConfiguredVisible() {
            return false;
          }

          @Override
          protected int getConfiguredGridH() {
            return 3;
          }

          public class Table extends AbstractTable {

            @Override
            protected boolean getConfiguredHeaderVisible() {
              return false;
            }

            @Override
            protected boolean getConfiguredAutoDiscardOnDelete() {
              return true;
            }

            @Override
            protected boolean getConfiguredMultiSelect() {
              return false;
            }

            @Override
            protected boolean getConfiguredAutoResizeColumns() {
              return true;
            }

            public PartnerColumn getPartnerColumn() {
              return getColumnSet().getColumnByClass(PartnerColumn.class);
            }

            @Order(10)
            public class PartnerColumn extends AbstractSmartColumn<BigDecimal> {

              @Override
              protected int getConfiguredWidth() {
                return 100;
              }

              @Override
              protected Class<? extends ILookupCall<BigDecimal>> getConfiguredLookupCall() {
                return PartnerLookupCall.class;
              }

              @Override
              protected boolean getConfiguredEditable() {
                return true;
              }
            }

            @Order(1000)
            public class AddMenu extends AbstractMenu {
              @Override
              protected String getConfiguredText() {
                return TEXTS.get("Add");
              }

              @Override
              protected Set<? extends IMenuType> getConfiguredMenuTypes() {
                return CollectionUtility.hashSet(TableMenuType.SingleSelection, TableMenuType.EmptySpace, TableMenuType.MultiSelection);
              }

              @Override
              protected void execAction() {
                addRow(createRow(), true);
              }
            }

            @Order(1010)
            public class DeleteMenu extends AbstractMenu {
              @Override
              protected String getConfiguredText() {
                return TEXTS.get("Delete");
              }

              @Override
              protected Set<? extends IMenuType> getConfiguredMenuTypes() {
                return CollectionUtility.hashSet(TableMenuType.SingleSelection);
              }

              @Override
              protected void execAction() {
                deleteRow(getSelectedRow());
              }
            }

          }

        }

      }

      @Order(120)
      public class CategoriesBox extends AbstractListBox<BigDecimal> {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("Categories");
        }

        @Override
        protected int getConfiguredGridH() {
          return 6;
        }

        @Override
        protected Class<? extends ILookupCall<BigDecimal>> getConfiguredLookupCall() {
          return CategoryLookupCall.class;
        }
      }

      @Order(2000)
      public class PermissionsField extends AbstractTableField<PermissionsField.Table> {

        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("Permissions");
        }

        @Override
        protected int getConfiguredGridH() {
          return 3;
        }

        public class Table extends AbstractTable {
          public PermissionColumn getPermissionColumn() {
            return getColumnSet().getColumnByClass(PermissionColumn.class);
          }

          public UserColumn getUserColumn() {
            return getColumnSet().getColumnByClass(UserColumn.class);
          }

          @Override
          protected boolean getConfiguredHeaderVisible() {
            return false;
          }

          @Override
          protected boolean getConfiguredAutoDiscardOnDelete() {
            return true;
          }

          @Override
          protected boolean getConfiguredMultiSelect() {
            return false;
          }

          @Override
          protected boolean getConfiguredAutoResizeColumns() {
            return true;
          }

          @Override
          protected void execContentChanged() {
            try {
              setTableChanging(true);
              for (ITableRow row : getRows()) {
                Cell cell = row.getCellForUpdate(getUserColumn());
                // TODO do not allow administators to be removed!
                //  cell.setEditable(false);
              }
            }
            finally {
              setTableChanging(false);
            }

          }

          @Order(10)
          public class UserColumn extends AbstractSmartColumn<String> {

            @Override
            protected int getConfiguredWidth() {
              return 100;
            }

            @Override
            protected Class<? extends ILookupCall<String>> getConfiguredLookupCall() {
              return UserLookupCall.class;
            }

            @Override
            protected boolean getConfiguredEditable() {
              return true;
            }

            @Override
            protected IFormField execPrepareEdit(ITableRow row) {
              return super.execPrepareEdit(row);
            }

            @Override
            protected IFormField prepareEditInternal(ITableRow row) {
              return new P_UserSmarField(row);
            }

            public class P_UserSmarField extends AbstractSmartField<String> {
              private ITableRow m_row;

              public P_UserSmarField(ITableRow row) {
                m_row = row;

              }

              @Override
              protected Class<? extends ILookupCall<String>> getConfiguredLookupCall() {
                return UserLookupCall.class;
              }

              @Override
              protected void execFilterBrowseLookupResult(ILookupCall<String> call, List<ILookupRow<String>> result) {
                filterDuplicateTypesLookupResult(m_row, result);
              }

              @Override
              protected void execFilterTextLookupResult(ILookupCall<String> call, List<ILookupRow<String>> result) {
                filterDuplicateTypesLookupResult(m_row, result);
              }

              /**
               * @param row
               * @param result
               */
              private void filterDuplicateTypesLookupResult(ITableRow row, List<ILookupRow<String>> result) {
                Set<String> alreadyUsed = new HashSet<String>(getUserColumn().getValues());
                alreadyUsed.remove(getUserColumn().getValue(row));
                for (Iterator<ILookupRow<String>> iterator = result.iterator(); iterator.hasNext();) {
                  ILookupRow<String> current = iterator.next();
                  if (alreadyUsed.contains(current.getKey())) {
                    iterator.remove();
                  }
                }
              }

            }

          }

          @Order(20)
          public class PermissionColumn extends AbstractSmartColumn<Integer> {

            @Override
            protected int getConfiguredWidth() {
              return 100;
            }

            @Override
            protected Class<? extends ILookupCall<Integer>> getConfiguredLookupCall() {
              return PermissionLookupCall.class;
            }

            @Override
            protected boolean getConfiguredEditable() {
              return true;
            }
          }

          @Order(1000)
          public class AddMenu extends AbstractMenu {
            @Override
            protected String getConfiguredText() {
              return TEXTS.get("Add");
            }

            @Override
            protected Set<? extends IMenuType> getConfiguredMenuTypes() {
              return CollectionUtility.hashSet(TableMenuType.SingleSelection, TableMenuType.EmptySpace, TableMenuType.MultiSelection);
            }

            @Override
            protected void execAction() {
              addRow(createRow(), true);
            }
          }

          @Order(1010)
          public class DeleteMenu extends AbstractMenu {
            @Override
            protected String getConfiguredText() {
              return TEXTS.get("Delete");
            }

            @Override
            protected Set<? extends IMenuType> getConfiguredMenuTypes() {
              return CollectionUtility.hashSet(TableMenuType.SingleSelection);
            }

            @Override
            protected void execAction() {
              deleteRow(getSelectedRow());
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

  protected void importPartners(Partners partners) {
    switch (partners.getRowCount()) {
      case 1:
        getPartnerField().setValue(partners.getRows()[0].getPartner());
      case 0:
        getPartnerSequenceBox().setVisible(true);
        getPartnersField().setVisible(false);
        break;
      default:
        getPartnerSequenceBox().setVisible(false);
        getPartnersField().setVisible(true);
        break;
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
      importPartners(formData.getPartners());

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
      getOpenHtmlField().setVisible(false);
    }

    @Override
    protected void execStore() {
      DocumentFormData formData = new DocumentFormData();
      exportFormData(formData);
      BEANS.get(IDocumentService.class).store(formData);
    }
  }

  public class EditHandler extends AbstractFormHandler {
    @Override
    protected void execLoad() {
      DocumentFormData formData = new DocumentFormData();
      exportFormData(formData);
      formData = BEANS.get(IDocumentService.class).load(formData);
      importFormData(formData);
      importPartners(formData.getPartners());
    }

    @Override
    protected void execStore() {
    }
  }

}

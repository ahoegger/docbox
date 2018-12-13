package ch.ahoegger.docbox.client.document;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.eclipse.scout.rt.client.dto.PageData;
import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenuSeparator;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.TableMenuType;
import org.eclipse.scout.rt.client.ui.basic.cell.Cell;
import org.eclipse.scout.rt.client.ui.basic.cell.ICell;
import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractBigDecimalColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractDateColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractIntegerColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractSmartColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.desktop.IDesktop;
import org.eclipse.scout.rt.client.ui.desktop.OpenUriAction;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.ISearchForm;
import org.eclipse.scout.rt.client.ui.dnd.IDNDSupport;
import org.eclipse.scout.rt.client.ui.dnd.ResourceListTransferObject;
import org.eclipse.scout.rt.client.ui.dnd.TransferObject;
import org.eclipse.scout.rt.client.ui.messagebox.MessageBox;
import org.eclipse.scout.rt.client.ui.messagebox.MessageBoxes;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.config.CONFIG;
import org.eclipse.scout.rt.platform.html.HTML;
import org.eclipse.scout.rt.platform.resource.BinaryResource;
import org.eclipse.scout.rt.platform.text.TEXTS;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;

import ch.ahoegger.docbox.client.document.DocumentLinkProperties.DocumentLinkDocumentIdParamName;
import ch.ahoegger.docbox.client.document.DocumentLinkProperties.DocumentLinkURI;
import ch.ahoegger.docbox.client.templates.AbstractDocboxPageWithTable;
import ch.ahoegger.docbox.shared.administration.user.UserLookupCall;
import ch.ahoegger.docbox.shared.conversation.ConversationLookupCall;
import ch.ahoegger.docbox.shared.document.DocumentSearchFormData;
import ch.ahoegger.docbox.shared.document.DocumentTableData;
import ch.ahoegger.docbox.shared.document.IDocumentService;
import ch.ahoegger.docbox.shared.security.permission.AdministratorPermission;

/**
 * <h3>{@link DocumentTablePage}</h3>
 *
 * @author Andreas Hoegger
 */
@PageData(DocumentTableData.class)
public class DocumentTablePage extends AbstractDocboxPageWithTable<DocumentTablePage.Table> {

  private BigDecimal m_conversationId;
  private BigDecimal m_partnerId;
  private BigDecimal m_categoryId;

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("Documents");
  }

  @Override
  protected void execInitPage() {
    registerDataChangeListener(IDocumentEntity.ENTITY_KEY);
  }

  @Override
  protected void execDisposePage() {
    super.execDisposePage();
  }

  @Override
  protected void execLoadData(SearchFilter filter) {
    importPageData(BEANS.get(IDocumentService.class).getTableData((DocumentSearchFormData) filter.getFormData()));
  }

  @Override
  protected Class<? extends ISearchForm> getConfiguredSearchForm() {
    return DocumentSearchForm.class;
  }

  @Override
  public DocumentSearchForm getSearchFormInternal() {
    return (DocumentSearchForm) super.getSearchFormInternal();
  }

  @Override
  protected ISearchForm createSearchForm() {
    DocumentSearchForm searchForm = (DocumentSearchForm) super.createSearchForm();
    if (getConversationId() != null) {
      searchForm.setHandler(new DocumentSearchForm.ConversationDocumentSearchHandler(searchForm, getConversationId()));
    }
    else if (getPartnerId() != null) {
      searchForm.setHandler(new DocumentSearchForm.PartnerDocumentSearchHandler(searchForm, getPartnerId()));
    }
    else if (getCategoryId() != null) {
      searchForm.setHandler(new DocumentSearchForm.CategoryDocumentSearchHandler(searchForm, getCategoryId()));
    }

    return searchForm;
  }

  @Override
  protected IPage<?> execCreateChildPage(ITableRow row) {
    return new DocumentDetailPage(getTable().getDocumentIdColumn().getValue(row));
  }

  @Override
  protected void updateCellFromTableCell(Cell pageCell, ICell summaryCell) {
    super.updateCellFromTableCell(pageCell, summaryCell);
    if (summaryCell != null) {
      pageCell.setText(Optional.ofNullable(summaryCell.getText())
          .map(t -> {
            if (t.length() > 200) {
              return t.subSequence(0, 200) + "...";
            }
            return t;
          }).orElse(TEXTS.get("Document")));
    }
  }

  public void setConversationId(BigDecimal value) {
    m_conversationId = value;
  }

  public BigDecimal getConversationId() {
    return m_conversationId;
  }

  public BigDecimal getPartnerId() {
    return m_partnerId;
  }

  public void setPartnerId(BigDecimal partnerId) {
    m_partnerId = partnerId;
  }

  /**
   * @param value
   */
  public void setCategoryId(BigDecimal categoryId) {
    m_categoryId = categoryId;
  }

  public BigDecimal getCategoryId() {
    return m_categoryId;
  }

  public class Table extends AbstractTable {

    @Override
    protected int getConfiguredDropType() {
      return IDNDSupport.TYPE_FILE_TRANSFER;
    }

    @Override
    protected void execDrop(ITableRow row, TransferObject t) {
      if (t instanceof ResourceListTransferObject) {
        List<BinaryResource> resources = ((ResourceListTransferObject) t).getResources();
        if (resources.size() > 0) {
          DocumentForm form = new DocumentForm();
          DocumentForm.DocumentFormNewHandler handler = new DocumentForm.DocumentFormNewHandler(form);
          handler.setPartnerId(getSearchFormInternal().getPartnerField().getValue());
          handler.setConversationId(getSearchFormInternal().getConversationField().getValue());
          handler.setDocumentResource(CollectionUtility.firstElement(resources));
          form.setHandler(handler);
          form.start();
        }
      }

    }

    public AbstractColumn getAbstractColumn() {
      return getColumnSet().getColumnByClass(AbstractColumn.class);
    }

    public DocumentPathColumn getDocumentPathColumn() {
      return getColumnSet().getColumnByClass(DocumentPathColumn.class);
    }

    public DocumentDateColumn getDocumentDateColumn() {
      return getColumnSet().getColumnByClass(DocumentDateColumn.class);
    }

    public CapturedDateColumn getCapturedDateColumn() {
      return getColumnSet().getColumnByClass(CapturedDateColumn.class);
    }

    public ConversationColumn getConversationColumn() {
      return getColumnSet().getColumnByClass(ConversationColumn.class);
    }

    public OcrParseCountColumn getOcrParseCountColumn() {
      return getColumnSet().getColumnByClass(OcrParseCountColumn.class);
    }

    public OwnerColumn getOwnerColumn() {
      return getColumnSet().getColumnByClass(OwnerColumn.class);
    }

    public PartnerColumn getPartnerColumn() {
      return getColumnSet().getColumnByClass(PartnerColumn.class);
    }

    public DocumentIdColumn getDocumentIdColumn() {
      return getColumnSet().getColumnByClass(DocumentIdColumn.class);
    }

    @Order(10)
    public class DocumentIdColumn extends AbstractBigDecimalColumn {

      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }

    }

    @Order(20)
    public class AbstractColumn extends AbstractStringColumn {
      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Abstract");
      }

      @Override
      protected int getConfiguredWidth() {
        return 400;
      }

      @Override
      protected int getConfiguredSortIndex() {
        return 1;
      }
    }

    @Order(30)
    public class PartnerColumn extends AbstractStringColumn {
      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Partner");
      }

      @Override
      protected int getConfiguredWidth() {
        return 250;
      }
    }

    @Order(522)
    public class ConversationColumn extends AbstractSmartColumn<BigDecimal> {
      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Conversation");
      }

      @Override
      protected int getConfiguredWidth() {
        return 250;
      }

      @Override
      protected Class<? extends ILookupCall<BigDecimal>> getConfiguredLookupCall() {
        return ConversationLookupCall.class;
      }
    }

    @Order(1015)
    public class DocumentDateColumn extends AbstractDateColumn {
      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Date");
      }

      @Override
      protected int getConfiguredWidth() {
        return 100;
      }

      @Override
      protected boolean getConfiguredSortAscending() {
        return false;
      }

      @Override
      protected int getConfiguredSortIndex() {
        return 0;
      }
    }

    @Order(1507)
    public class CapturedDateColumn extends AbstractDateColumn {
      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("CapturedOn");
      }

      @Override
      protected int getConfiguredWidth() {
        return 100;
      }
    }

    @Order(1753)
    public class OwnerColumn extends AbstractSmartColumn<String> {
      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Owner");
      }

      @Override
      protected int getConfiguredWidth() {
        return 160;
      }

      @Override
      protected Class<? extends ILookupCall<String>> getConfiguredLookupCall() {
        return UserLookupCall.class;
      }
    }

    @Order(1876)
    public class OcrParseCountColumn extends AbstractIntegerColumn {
      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("OCRParseCount");
      }

      @Override
      protected int getConfiguredWidth() {
        return 80;
      }

      @Override
      protected boolean getConfiguredVisible() {
        return false;
      }
    }

    @Order(2000)
    public class DocumentPathColumn extends AbstractStringColumn {

      @Override
      protected boolean getConfiguredHtmlEnabled() {
        return true;
      }

      @Override
      protected void execDecorateCell(Cell cell, ITableRow row) {
        StringBuilder linkBuilder = new StringBuilder();
        linkBuilder.append(CONFIG.getPropertyValue(DocumentLinkURI.class));
        linkBuilder.append("?").append(CONFIG.getPropertyValue(DocumentLinkDocumentIdParamName.class)).append("=").append(getDocumentIdColumn().getValue(row));
        String encodedHtml = HTML.link(linkBuilder.toString(), TEXTS.get("Open")).addAttribute("target", "_blank").toHtml();
        cell.setHtmlEnabled(true);
        cell.setText(encodedHtml);
      }

      @Override
      protected int getConfiguredWidth() {
        return 100;
      }

      @Override
      protected boolean getConfiguredVisible() {
        return true;
      }
    }

    @Order(1000)
    public class NewMenu extends AbstractMenu {
      @Override
      protected String getConfiguredText() {
        return TEXTS.get("New");
      }

      @Override
      protected String getConfiguredIconId() {
        return "font:icomoon \uf067";
      }

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.hashSet(TableMenuType.EmptySpace);
      }

      @Override
      protected void execAction() {
        DocumentForm form = new DocumentForm();
        DocumentForm.DocumentFormNewHandler handler = new DocumentForm.DocumentFormNewHandler(form);
        handler.setPartnerId(getSearchFormInternal().getPartnerField().getValue());
        handler.setConversationId(getSearchFormInternal().getConversationField().getValue());
        form.setHandler(handler);

        form.start();
      }
    }

    @Order(2000)
    public class EditMenu extends AbstractMenu {
      @Override
      protected String getConfiguredText() {
        return TEXTS.get("Edit");
      }

      @Override
      protected String getConfiguredIconId() {
        return "font:icomoon \ue903";
      }

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.hashSet(TableMenuType.SingleSelection);
      }

      @Override
      protected void execAction() {
        DocumentForm form = new DocumentForm();
        DocumentForm.DocumentFormEditHandler handler = new DocumentForm.DocumentFormEditHandler(form, getDocumentIdColumn().getSelectedValue());
        form.setHandler(handler);
        form.start();
      }
    }

    @Order(3000)
    public class OpenPdfMenu extends AbstractMenu {
      @Override
      protected String getConfiguredText() {
        return TEXTS.get("OpenPdf");
      }

      @Override
      protected String getConfiguredIconId() {
        return "font:icomoon \ue901";
      }

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.hashSet(TableMenuType.SingleSelection);
      }

      @Override
      protected void execAction() {
        StringBuilder linkBuilder = new StringBuilder();
        linkBuilder.append(CONFIG.getPropertyValue(DocumentLinkURI.class));
        linkBuilder.append("?").append(CONFIG.getPropertyValue(DocumentLinkDocumentIdParamName.class)).append("=").append(getDocumentIdColumn().getSelectedValue());

        IDesktop.CURRENT.get().openUri(linkBuilder.toString(), OpenUriAction.NEW_WINDOW);
      }
    }

    @Order(4000)
    public class AdvancedMenu extends AbstractMenu {
      @Override
      protected String getConfiguredText() {
        return TEXTS.get("Advanced");
      }

      @Override
      protected void execInitAction() {
        super.execInitAction();
        setVisibleGranted(ACCESS.check(new AdministratorPermission()));
      }

      @Order(100)
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
          if (MessageBoxes.createYesNo().withBody(TEXTS.get("DeleteConfirmationTextX", getAbstractColumn().getSelectedDisplayText())).show() == MessageBox.YES_OPTION) {
            BEANS.get(IDocumentService.class).delete(getDocumentIdColumn().getSelectedValue());
            IDesktop.CURRENT.get().dataChanged(IDocumentEntity.ENTITY_KEY);

          }
        }
      }

      @Order(1000)
      public class RepalceDocumentMenuMenu extends AbstractMenu {
        @Override
        protected String getConfiguredText() {
          return TEXTS.get("ReplaceDocument");
        }

        @Override
        protected Set<? extends IMenuType> getConfiguredMenuTypes() {
          return CollectionUtility.hashSet(TableMenuType.SingleSelection);
        }

        @Override
        protected void execAction() {
          ReplaceDocumentForm form = new ReplaceDocumentForm();
          form.setDocumentId(getDocumentIdColumn().getSelectedValue());
          form.start();
        }
      }

      @Order(2000)
      public class SeparatorMenu extends AbstractMenuSeparator {
      }

      @Order(3000)
      public class EnsureDocumentsParsedMenu extends AbstractMenu {
        @Override
        protected String getConfiguredText() {
          return TEXTS.get("EnsureDocumentsParsed");
        }

        @Override
        protected Set<? extends IMenuType> getConfiguredMenuTypes() {
          return CollectionUtility.hashSet(TableMenuType.SingleSelection, TableMenuType.MultiSelection);
        }

        @Override
        protected boolean getConfiguredEnabled() {
          return ACCESS.check(new AdministratorPermission());
        }

        @Override
        protected void execAction() {
          BEANS.get(IDocumentService.class).buildOcrOfMissingDocuments(getTable().getDocumentIdColumn().getSelectedValues());
        }
      }

      @Order(4000)
      public class DeleteParsedContentMenu extends AbstractMenu {
        @Override
        protected String getConfiguredText() {
          return TEXTS.get("DeleteParsedContent");
        }

        @Override
        protected Set<? extends IMenuType> getConfiguredMenuTypes() {
          return CollectionUtility.hashSet(TableMenuType.SingleSelection, TableMenuType.MultiSelection);
        }

        @Override
        protected boolean getConfiguredEnabled() {
          return ACCESS.check(new AdministratorPermission());
        }

        @Override
        protected void execAction() {
          BEANS.get(IDocumentService.class).deletePasedConent(getTable().getDocumentIdColumn().getSelectedValues());
        }
      }
    }

  }

}

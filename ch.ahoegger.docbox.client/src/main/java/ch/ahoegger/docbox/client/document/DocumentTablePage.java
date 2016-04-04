package ch.ahoegger.docbox.client.document;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import org.eclipse.scout.rt.client.dto.PageData;
import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.TableMenuType;
import org.eclipse.scout.rt.client.ui.basic.cell.Cell;
import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractDateColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractLongColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractSmartColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.ISearchForm;
import org.eclipse.scout.rt.client.ui.dnd.IDNDSupport;
import org.eclipse.scout.rt.client.ui.dnd.ResourceListTransferObject;
import org.eclipse.scout.rt.client.ui.dnd.TransferObject;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.config.CONFIG;
import org.eclipse.scout.rt.platform.html.HTML;
import org.eclipse.scout.rt.platform.resource.BinaryResource;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;

import ch.ahoegger.docbox.client.document.DocumentLinkProperties.DocumentLinkDocumentIdParamName;
import ch.ahoegger.docbox.client.document.DocumentLinkProperties.DocumentLinkURI;
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
public class DocumentTablePage extends AbstractPageWithTable<DocumentTablePage.Table> {

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("Documents");
  }

  @Override
  protected void execInitPage() {
    registerDataChangeListener(IDocumentEntity.ENTITY_KEY);
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
  protected IPage<?> execCreateChildPage(ITableRow row) {
    return new DocumentDetailPage(getTable().getDocumentIdColumn().getValue(row));
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
          form.getDocumentField().setValue(CollectionUtility.firstElement(resources));
          form.startNew();
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
    public class DocumentIdColumn extends AbstractLongColumn {

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
        return 300;
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
        return 200;
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
        return 100;
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
        return 100;
      }

      @Override
      protected Class<? extends ILookupCall<String>> getConfiguredLookupCall() {
        return UserLookupCall.class;
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
        String encodedHtml = HTML.link(linkBuilder.toString(), TEXTS.get("Open")).addAttribute("target", "_blank").toEncodedHtml();
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
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.hashSet(TableMenuType.EmptySpace, TableMenuType.SingleSelection, TableMenuType.MultiSelection);
      }

      @Override
      protected void execAction() {
        DocumentForm form = new DocumentForm();
        form.startNew();
        form.waitFor();
        if (form.isFormStored()) {
          reloadPage();
        }
      }
    }

    @Order(2000)
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

        DocumentForm form = new DocumentForm();
        form.setDocumentId(getDocumentIdColumn().getSelectedValue());
        form.startEdit();
      }

    }

    @Order(2500)
    public class AdministrationMenu extends AbstractMenu {
      @Override
      protected String getConfiguredText() {
        return TEXTS.get("Administration");
      }

      @Override
      protected boolean getConfiguredVisible() {
        return ACCESS.check(new AdministratorPermission());
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

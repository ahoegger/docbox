package ch.ahoegger.docbox.client.hr.billing;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.dto.FormData.SdkCommand;
import org.eclipse.scout.rt.client.dto.PageData;
import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.TableMenuType;
import org.eclipse.scout.rt.client.ui.basic.cell.Cell;
import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractBigDecimalColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractDateColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.ISearchForm;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.config.CONFIG;
import org.eclipse.scout.rt.platform.html.HTML;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.platform.util.ObjectUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

import ch.ahoegger.docbox.client.AbstractDocboxPageWithTable;
import ch.ahoegger.docbox.client.document.DocumentLinkProperties.DocumentLinkDocumentIdParamName;
import ch.ahoegger.docbox.client.document.DocumentLinkProperties.DocumentLinkURI;
import ch.ahoegger.docbox.client.hr.entity.EntityTablePage;
import ch.ahoegger.docbox.shared.hr.billing.IPostingGroupService;
import ch.ahoegger.docbox.shared.hr.billing.PostingGroupCodeType.UnbilledCode;
import ch.ahoegger.docbox.shared.hr.billing.PostingGroupSearchFormData;
import ch.ahoegger.docbox.shared.hr.billing.PostingGroupTableData;
import ch.ahoegger.docbox.shared.hr.entity.EntitySearchFormData;
import ch.ahoegger.docbox.shared.hr.entity.EntityTablePageData;
import ch.ahoegger.docbox.shared.hr.entity.IEntityService;

/**
 * <h3>{@link PostingGroupTablePage}</h3>
 *
 * @author Andreas Hoegger
 */
@PageData(PostingGroupTableData.class)
public class PostingGroupTablePage extends AbstractDocboxPageWithTable<PostingGroupTablePage.Table> {

  private BigDecimal m_partnerId;

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("Postings");
  }

  @Override
  protected void execLoadData(SearchFilter filter) {
    importPageData(BEANS.get(IPostingGroupService.class).getTableData((PostingGroupSearchFormData) filter.getFormData()));
  }

  @Override
  protected Class<? extends ISearchForm> getConfiguredSearchForm() {
    return PostingGroupSearchForm.class;
  }

  @Override
  protected void execInitPage() {
    registerDataChangeListener(IPostingGroupEntity.ENTITY_KEY);
  }

  @Override
  protected IPage<?> execCreateChildPage(ITableRow row) {
    EntityTablePage page = new EntityTablePage(getPartnerId());
    page.setPostingGroupId(getTable().getIdColumn().getValue(row));
    return page;
  }

  @Override
  public PostingGroupSearchForm getSearchFormInternal() {
    return (PostingGroupSearchForm) super.getSearchFormInternal();
  }

  @Override
  protected void execInitSearchForm() {
    super.execInitSearchForm();
    getSearchFormInternal().setPartnerId(getPartnerId());
  }

  public void setPartnerId(BigDecimal partnerId) {
    m_partnerId = partnerId;
  }

  public BigDecimal getPartnerId() {
    return m_partnerId;
  }

  public class Table extends AbstractTable {

    @Override
    protected boolean getConfiguredSortEnabled() {
      return false;
    }

    public DateColumn getDateColumn() {
      return getColumnSet().getColumnByClass(DateColumn.class);
    }

    public BruttoWageColumn getBruttoWageColumn() {
      return getColumnSet().getColumnByClass(BruttoWageColumn.class);
    }

    public NettoWageColumn getNettoWageColumn() {
      return getColumnSet().getColumnByClass(NettoWageColumn.class);
    }

    public SourceTaxColumn getSourceTaxColumn() {
      return getColumnSet().getColumnByClass(SourceTaxColumn.class);
    }

    public SocialSecurityTaxColumn getSocialSecurityTaxColumn() {
      return getColumnSet().getColumnByClass(SocialSecurityTaxColumn.class);
    }

    public VacationExtraColumn getVacationExtraColumn() {
      return getColumnSet().getColumnByClass(VacationExtraColumn.class);
    }

    public PartnerIdColumn getPartnerIdColumn() {
      return getColumnSet().getColumnByClass(PartnerIdColumn.class);
    }

    public DocumentIdColumn getDocumentIdColumn() {
      return getColumnSet().getColumnByClass(DocumentIdColumn.class);
    }

    public NameColumn getNameColumn() {
      return getColumnSet().getColumnByClass(NameColumn.class);
    }

    public SortGroupColumn getSortGroupColumn() {
      return getColumnSet().getColumnByClass(SortGroupColumn.class);
    }

    public IdColumn getIdColumn() {
      return getColumnSet().getColumnByClass(IdColumn.class);
    }

    @Order(1000)
    public class SortGroupColumn extends AbstractBigDecimalColumn {
      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }

      @Override
      protected int getConfiguredSortIndex() {
        return 1;
      }

    }

    @Order(2000)
    public class IdColumn extends AbstractBigDecimalColumn {

      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }
    }

    @Order(2500)
    public class PartnerIdColumn extends AbstractBigDecimalColumn {
      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }

    }

    @Order(2750)
    public class DocumentIdColumn extends AbstractBigDecimalColumn {
      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }
    }

    @Order(3000)
    public class NameColumn extends AbstractStringColumn {
      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("PostingGroup");
      }

      @Override
      protected int getConfiguredWidth() {
        return 200;
      }
    }

    @Order(4000)
    public class DateColumn extends AbstractDateColumn {
      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Date");
      }

      @Override
      protected int getConfiguredWidth() {
        return 120;
      }

      @Override
      protected int getConfiguredSortIndex() {
        return 2;
      }
    }

    @Order(5000)
    public class BruttoWageColumn extends AbstractBigDecimalColumn {
      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("BruttoWage");
      }

      @Override
      protected int getConfiguredWidth() {
        return 100;
      }
    }

    @Order(6000)
    public class NettoWageColumn extends AbstractBigDecimalColumn {
      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("NettoWage");
      }

      @Override
      protected int getConfiguredWidth() {
        return 100;
      }
    }

    @Order(7000)
    public class SourceTaxColumn extends AbstractBigDecimalColumn {
      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("SourceTax");
      }

      @Override
      protected int getConfiguredWidth() {
        return 100;
      }
    }

    @Order(8000)
    public class SocialSecurityTaxColumn extends AbstractBigDecimalColumn {
      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("SocialSecurityTax");
      }

      @Override
      protected int getConfiguredWidth() {
        return 100;
      }
    }

    @Order(9000)
    public class VacationExtraColumn extends AbstractBigDecimalColumn {
      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("VacationExtra");
      }

      @Override
      protected int getConfiguredWidth() {
        return 100;
      }
    }

    @Order(10000)
    @FormData(sdkCommand = SdkCommand.IGNORE)
    public class DocumentLinkColumn extends AbstractStringColumn {

      @Override
      protected boolean getConfiguredHtmlEnabled() {
        return true;
      }

      @Override
      protected void execDecorateCell(Cell cell, ITableRow row) {
        BigDecimal documentId = getDocumentIdColumn().getValue(row);
        if (documentId != null) {
          StringBuilder linkBuilder = new StringBuilder();
          linkBuilder.append(CONFIG.getPropertyValue(DocumentLinkURI.class));
          linkBuilder.append("?").append(CONFIG.getPropertyValue(DocumentLinkDocumentIdParamName.class)).append("=").append(documentId);
          String encodedHtml = HTML.link(linkBuilder.toString(), TEXTS.get("Open")).addAttribute("target", "_blank").toHtml();
          cell.setHtmlEnabled(true);
          cell.setText(encodedHtml);
        }
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
    public class NewPayslipMenu extends AbstractMenu {
      @Override
      protected String getConfiguredText() {
        return TEXTS.get("NewPayslip");
      }

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.hashSet(TableMenuType.SingleSelection);
      }

      @Override
      protected void execOwnerValueChanged(Object newOwnerValue) {
        setVisible(ObjectUtility.equals(getIdColumn().getValue(getTable().getSelectedRow()), UnbilledCode.ID));
      }

      @Override
      protected void execAction() {
        BigDecimal partnerId = getPartnerIdColumn().getValue(getSelectedRow());
        BigDecimal postingGroupId = getIdColumn().getValue(getTable().getSelectedRow());
        PostingGroupForm form = new PostingGroupForm();
        form.getPartnerField().setValue(partnerId);
        form.getPartnerField().setEnabled(false);
        EntitySearchFormData searchFilter = new EntitySearchFormData();
        searchFilter.setPostingGroupId(postingGroupId);
        searchFilter.getPartnerId().setValue(partnerId);
        EntityTablePageData entityTableData = BEANS.get(IEntityService.class).getEntityTableData(searchFilter);
        Date fromDate = Arrays.stream(entityTableData.getRows()).map(row -> row.getDate()).min((d1, d2) -> d1.compareTo(d2)).get();
        Date toDate = Arrays.stream(entityTableData.getRows()).map(row -> row.getDate()).max((d1, d2) -> d1.compareTo(d2)).get();
        form.getEntityDateFromField().setValue(fromDate);
        form.getEntityDateToField().setValue(toDate);
        form.startNew();
      }
    }

  }
}
package ch.ahoegger.docbox.client.hr.billing;

import java.math.BigDecimal;

import org.eclipse.scout.rt.client.dto.PageData;
import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractBigDecimalColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractDateColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.ISearchForm;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

import ch.ahoegger.docbox.client.AbstractDocboxPageWithTable;
import ch.ahoegger.docbox.client.hr.entity.EntityTablePage;
import ch.ahoegger.docbox.shared.hr.billing.IPostingGroupService;
import ch.ahoegger.docbox.shared.hr.billing.PostingGroupSearchFormData;
import ch.ahoegger.docbox.shared.hr.billing.PostingGroupTableData;

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
  public PostingGroupSearchForm getSearchFormInternal() {
    return (PostingGroupSearchForm) super.getSearchFormInternal();
  }

  @Override
  protected void execInitSearchForm() {
    super.execInitSearchForm();
    getSearchFormInternal().setPartnerId(getPartnerId());
  }

  @Override
  protected IPage<?> execCreateChildPage(ITableRow row) {
    EntityTablePage page = new EntityTablePage(getPartnerId());
    page.setPostingGroupId(getTable().getIdColumn().getValue(row));
    return page;
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

  }
}

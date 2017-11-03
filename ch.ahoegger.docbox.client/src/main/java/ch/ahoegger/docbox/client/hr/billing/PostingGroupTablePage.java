package ch.ahoegger.docbox.client.hr.billing;

import java.math.BigDecimal;
import java.util.Set;

import org.eclipse.scout.rt.client.context.ClientRunContexts;
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
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractSmartColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.desktop.IDesktop;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.ISearchForm;
import org.eclipse.scout.rt.client.ui.messagebox.MessageBox;
import org.eclipse.scout.rt.client.ui.messagebox.MessageBoxes;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.config.CONFIG;
import org.eclipse.scout.rt.platform.html.HTML;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.platform.util.ObjectUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;

import ch.ahoegger.docbox.client.AbstractDocboxPageWithTable;
import ch.ahoegger.docbox.client.document.DocumentLinkProperties.DocumentLinkDocumentIdParamName;
import ch.ahoegger.docbox.client.document.DocumentLinkProperties.DocumentLinkURI;
import ch.ahoegger.docbox.client.hr.entity.EntityTablePage;
import ch.ahoegger.docbox.shared.hr.billing.IPostingGroupService;
import ch.ahoegger.docbox.shared.hr.billing.PostingGroupCodeType.UnbilledCode;
import ch.ahoegger.docbox.shared.hr.billing.PostingGroupSearchFormData;
import ch.ahoegger.docbox.shared.hr.billing.PostingGroupTableData;
import ch.ahoegger.docbox.shared.hr.tax.TaxGroupLookupCall;
import ch.ahoegger.docbox.shared.partner.PartnerLookupCall;
import ch.ahoegger.docbox.shared.security.permission.AdministratorPermission;

/**
 * <h3>{@link PostingGroupTablePage}</h3>
 *
 * @author Andreas Hoegger
 */
@PageData(PostingGroupTableData.class)
public class PostingGroupTablePage extends AbstractDocboxPageWithTable<PostingGroupTablePage.Table> {

  private BigDecimal m_partnerId;
  private BigDecimal m_taxGroupId;

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
    if (getPartnerId() != null) {
      getSearchFormInternal().getPartnerSmartField().setValue(getPartnerId());
      getSearchFormInternal().getPartnerSmartField().setEnabled(false);
    }
    getSearchFormInternal().setTaxGroupId(getTaxGroupId());
  }

  public void setPartnerId(BigDecimal partnerId) {
    m_partnerId = partnerId;
  }

  public BigDecimal getPartnerId() {
    return m_partnerId;
  }

  public void setTaxGroupId(BigDecimal taxGroupId) {
    m_taxGroupId = taxGroupId;
  }

  public BigDecimal getTaxGroupId() {
    return m_taxGroupId;
  }

  protected IDesktop getDesktop() {
    return ClientRunContexts.copyCurrent().getDesktop();
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

    public WorkingHoursColumn getWorkingHoursColumn() {
      return getColumnSet().getColumnByClass(WorkingHoursColumn.class);
    }

    public TaxGroupColumn getTaxGroupColumn() {
      return getColumnSet().getColumnByClass(TaxGroupColumn.class);
    }

    public StartDateColumn getStartDateColumn() {
      return getColumnSet().getColumnByClass(StartDateColumn.class);
    }

    public EndDateColumn getEndDateColumn() {
      return getColumnSet().getColumnByClass(EndDateColumn.class);
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

    @Order(3500)
    public class StartDateColumn extends AbstractDateColumn {
      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("StartDate");
      }

      @Override
      protected int getConfiguredWidth() {
        return 120;
      }
    }

    @Order(3750)
    public class EndDateColumn extends AbstractDateColumn {
      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("EndDate");
      }

      @Override
      protected int getConfiguredWidth() {
        return 120;
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
    public class WorkingHoursColumn extends AbstractBigDecimalColumn {
      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Hours");
      }

      @Override
      protected int getConfiguredWidth() {
        return 100;
      }
    }

    @Order(6000)
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
        return 130;
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

    @Order(11000)
    public class TaxGroupColumn extends AbstractSmartColumn<BigDecimal> {
      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("TaxGroup");
      }

      @Override
      protected Class<? extends ILookupCall<BigDecimal>> getConfiguredLookupCall() {
        return TaxGroupLookupCall.class;
      }

      @Override
      protected int getConfiguredWidth() {
        return 140;
      }
    }

    @Order(12000)
    public class PartnerIdColumn extends AbstractSmartColumn<BigDecimal> {

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Partner");
      }

      @Override
      protected Class<? extends ILookupCall<BigDecimal>> getConfiguredLookupCall() {
        return PartnerLookupCall.class;
      }

      @Override
      protected int getConfiguredWidth() {
        return 150;
      }
    }

    @Order(13000)
    public class DocumentIdColumn extends AbstractBigDecimalColumn {
      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }
    }

    @Order(14000)
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

    @Order(50000)
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
        form.setPostingGroupId(postingGroupId);
        form.getPartnerField().setEnabled(false);
        form.startNew();
      }
    }

    @Order(50500)
    public class EditPostingGroupMenu extends AbstractMenu {
      @Override
      protected String getConfiguredText() {
        return TEXTS.get("Edit");
      }

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.hashSet(TableMenuType.SingleSelection);
      }

      @Override
      protected void execOwnerValueChanged(Object newOwnerValue) {

        setVisible(ObjectUtility.notEquals(getIdColumn().getValue(getTable().getSelectedRow()), UnbilledCode.ID), "unbilled");
      }

      @Override
      protected void execAction() {
        BigDecimal partnerId = getPartnerIdColumn().getValue(getSelectedRow());
        BigDecimal postingGroupId = getIdColumn().getValue(getTable().getSelectedRow());
        PostingGroupForm form = new PostingGroupForm();
        form.getPartnerField().setValue(partnerId);
        form.setPostingGroupId(postingGroupId);
        form.getPartnerField().setEnabled(false);
        form.startModify();
      }
    }

    @Order(51000)
    public class DeletePayslipMenu extends AbstractMenu {
      @Override
      protected String getConfiguredText() {
        return TEXTS.get("Delete");
      }

      @Override
      protected boolean getConfiguredVisible() {
        return ACCESS.check(new AdministratorPermission());
      }

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.hashSet(TableMenuType.SingleSelection);
      }

      @Override
      protected void execOwnerValueChanged(Object newOwnerValue) {
        setVisible(ObjectUtility.notEquals(getIdColumn().getValue(getTable().getSelectedRow()), UnbilledCode.ID));
      }

      @Override
      protected void execAction() {
        if (MessageBox.YES_OPTION == MessageBoxes.createYesNo()
            .withHeader(TEXTS.get("Delete"))
            .withBody(TEXTS.get("VerificationDelete", getNameColumn().getSelectedDisplayText())).show()) {
          BEANS.get(IPostingGroupService.class).delete(getIdColumn().getSelectedValue());

          getDesktop().dataChanged(IPostingGroupEntity.ENTITY_KEY);
        }
      }
    }

  }
}

package ch.ahoegger.docbox.client.hr.billing.payslip;

import java.math.BigDecimal;
import java.util.Set;

import org.eclipse.scout.rt.client.dto.Data;
import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.TableMenuType;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractBigDecimalColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractDateColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractSmartColumn;
import org.eclipse.scout.rt.client.ui.desktop.IDesktop;
import org.eclipse.scout.rt.client.ui.desktop.OpenUriAction;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.ISearchForm;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.classid.ClassId;
import org.eclipse.scout.rt.platform.config.CONFIG;
import org.eclipse.scout.rt.platform.text.TEXTS;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;

import ch.ahoegger.docbox.client.document.DocumentLinkProperties.DocumentLinkDocumentIdParamName;
import ch.ahoegger.docbox.client.document.DocumentLinkProperties.DocumentLinkURI;
import ch.ahoegger.docbox.client.hr.billing.payslip.PayslipTablePage.Table.NewPayslipMenu;
import ch.ahoegger.docbox.client.hr.billing.statement.AbstractStatementTable;
import ch.ahoegger.docbox.client.hr.employee.IEmployeeTaxGroupEntity;
import ch.ahoegger.docbox.client.hr.entity.EntityTablePage;
import ch.ahoegger.docbox.client.hr.entity.EntityTablePage.Table.NewExpenseMenu;
import ch.ahoegger.docbox.client.hr.entity.EntityTablePage.Table.NewWorkMenu;
import ch.ahoegger.docbox.client.templates.AbstractActionWithDataChangedListener;
import ch.ahoegger.docbox.client.templates.AbstractDocboxPageWithTable;
import ch.ahoegger.docbox.shared.administration.hr.billing.BillingCycleLookupCall;
import ch.ahoegger.docbox.shared.hr.billing.payslip.IPayslipService;
import ch.ahoegger.docbox.shared.hr.billing.payslip.PayslipSearchFormData;
import ch.ahoegger.docbox.shared.hr.billing.payslip.PayslipTableData;
import ch.ahoegger.docbox.shared.hr.billing.taxgroup.IEmployeeTaxGroupService;
import ch.ahoegger.docbox.shared.hr.employee.EmployeeLookupCall;
import ch.ahoegger.docbox.shared.hr.employee.EmployeeTaxGroupFormData;

/**
 * <h3>{@link PayslipTablePage}</h3>
 *
 * @author aho
 */
@Data(PayslipTableData.class)
public class PayslipTablePage extends AbstractDocboxPageWithTable<PayslipTablePage.Table> {

  /**
   * @param userPreferenceContext
   */
  public PayslipTablePage(String userPreferenceContext) {
    super(userPreferenceContext);
  }

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("PaySlip");
  }

  @Override
  protected void execLoadData(SearchFilter filter) {
    importPageData(BEANS.get(IPayslipService.class).getTableData((PayslipSearchFormData) filter.getFormData()));
  }

  @Override
  protected Class<? extends ISearchForm> getConfiguredSearchForm() {
    return PayslipSearchForm.class;
  }

  @Override
  protected void execInitPage() {
    registerDataChangeListener(IPayslipEntity.ENTITY_KEY, IPayslipEntity.ENTITY_KEY_FINALIZE);
  }

  @Override
  protected IPage<?> execCreateChildPage(ITableRow row) {
    EntityTablePage page = new EntityTablePage(getTable().getPayslipIdColumn().getValue(row));
    page.getTable().getMenuByClass(NewWorkMenu.class).setVisibleGranted(getTable().getStatementIdColumn().getValue(row) == null);
    page.getTable().getMenuByClass(NewExpenseMenu.class).setVisibleGranted(getTable().getStatementIdColumn().getValue(row) == null);
    page.getTable().getMenuByClass(EntityTablePage.Table.DeleteMenu.class).setVisibleGranted(getTable().getStatementIdColumn().getValue(row) == null);
    page.getTable().getMenuByClass(EntityTablePage.Table.EditMenu.class).setVisibleGranted(getTable().getStatementIdColumn().getValue(row) == null);
    return page;
  }

  @Override
  public PayslipSearchForm getSearchFormInternal() {
    return (PayslipSearchForm) super.getSearchFormInternal();
  }

  /**
   * @param value
   */
  public void setEmployeeTaxGroupId(BigDecimal value) {
    getSearchFormInternal().setEmployeeTaxGroupId(value);
    getTable().getMenuByClass(NewPayslipMenu.class).updateVisibility();
  }

  @Order(1000)
  @ClassId("f3d20370-ff6a-40cd-bd42-8c708bf8e6ee")

  public class Table extends AbstractStatementTable {

    @Override
    protected boolean getConfiguredRowIconVisible() {
      return true;
    }

    @Override
    protected void execDecorateRow(ITableRow row) {
      if (getStatementIdColumn().getValue(row) != null) {
        row.setIconId("font:icomoon \ue904");
      }
      super.execDecorateRow(row);
    }

    public PeriodFromColumn getPeriodFromColumn() {
      return getColumnSet().getColumnByClass(PeriodFromColumn.class);
    }

    public PeriodToColumn getPeriodToColumn() {
      return getColumnSet().getColumnByClass(PeriodToColumn.class);
    }

    public BillingCycleColumn getBillingCycleColumn() {
      return getColumnSet().getColumnByClass(BillingCycleColumn.class);
    }

    public EmployeeColumn getEmployeeColumn() {
      return getColumnSet().getColumnByClass(EmployeeColumn.class);
    }

    public PayslipIdColumn getPayslipIdColumn() {
      return getColumnSet().getColumnByClass(PayslipIdColumn.class);
    }

    @Order(1000)
    public class PayslipIdColumn extends AbstractBigDecimalColumn {
      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }
    }

    @Order(1500)
    public class EmployeeColumn extends AbstractSmartColumn<BigDecimal> {
      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Employee");
      }

      @Override
      protected Class<? extends ILookupCall<BigDecimal>> getConfiguredLookupCall() {
        return EmployeeLookupCall.class;
      }

      @Override
      protected int getConfiguredWidth() {
        return 180;
      }
    }

    @Order(2000)
    public class BillingCycleColumn extends AbstractSmartColumn<BigDecimal> {
      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("BillingCycle");
      }

      @Override
      protected Class<? extends ILookupCall<BigDecimal>> getConfiguredLookupCall() {
        return BillingCycleLookupCall.class;
      }

      @Override
      protected int getConfiguredWidth() {
        return 100;
      }
    }

    @Order(3000)
    public class PeriodFromColumn extends AbstractDateColumn {
      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("from");
      }

      @Override
      protected int getConfiguredWidth() {
        return 100;
      }
    }

    @Order(4000)
    public class PeriodToColumn extends AbstractDateColumn {
      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("to");
      }

      @Override
      protected int getConfiguredWidth() {
        return 100;
      }
    }

    @Order(50000)
    public class NewPayslipMenu extends AbstractActionWithDataChangedListener {
      @Override
      protected String getConfiguredText() {
        return TEXTS.get("NewPayslip");
      }

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.hashSet(TableMenuType.EmptySpace);
      }

      @Override
      protected Object[] getConfiguredDataChangeTypes() {
        return new Object[]{IEmployeeTaxGroupEntity.ENTITY_KEY};
      }

      @Override
      protected void execInitAction() {
        updateVisibility();
      }

      @Override
      protected boolean execComputeVisibility() {
        EmployeeTaxGroupFormData fd = new EmployeeTaxGroupFormData();
        if (getSearchFormInternal().getEmployeeTaxGroupId() != null) {
          fd.setEmployeeTaxGroupId(getSearchFormInternal().getEmployeeTaxGroupId());
          fd = BEANS.get(IEmployeeTaxGroupService.class).load(fd);
          return fd.getStatementId() == null;
        }
        else if (getSearchFormInternal().getEmployeeField().getValue() != null &&
            getSearchFormInternal().getTaxGroupField().getValue() != null) {
          fd.setEmployeeId(getSearchFormInternal().getEmployeeField().getValue());
          fd.setEmployeeId(getSearchFormInternal().getTaxGroupField().getValue());
          fd = BEANS.get(IEmployeeTaxGroupService.class).load(fd);
          return fd.getStatementId() == null;
        }
        return false;
      }

      @Override
      protected void execAction() {
        PayslipForm form = new PayslipForm();
        form.setEmployeeTaxGroupId(getSearchFormInternal().getEmployeeTaxGroupId());

        form.startNew();
      }
    }

    @Order(51000)
    public class FinalizeMenu extends AbstractMenu {
      @Override
      protected String getConfiguredText() {
        return TEXTS.get("Finalize");
      }

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.hashSet(TableMenuType.SingleSelection);
      }

      @Override
      protected void execOwnerValueChanged(Object newOwnerValue) {
        setVisible(getStatementIdColumn().getSelectedValue() == null);
      }

      @Override
      protected void execAction() {
        PayslipForm form = new PayslipForm();
        form.setPayslipId(getPayslipIdColumn().getSelectedValue());
        form.startFinalize();
      }
    }

    @Order(52000)
    public class ShowPayslipMenu extends AbstractMenu {
      @Override
      protected String getConfiguredText() {
        return TEXTS.get("ShowPayslip");
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
      protected void execOwnerValueChanged(Object newOwnerValue) {
        setVisible(getStatementIdColumn().getSelectedValue() != null);
      }

      @Override
      protected void execAction() {
        StringBuilder linkBuilder = new StringBuilder();
        linkBuilder.append(CONFIG.getPropertyValue(DocumentLinkURI.class));
        linkBuilder.append("?").append(CONFIG.getPropertyValue(DocumentLinkDocumentIdParamName.class)).append("=").append(getDocumentIdColumn().getSelectedValue());
        IDesktop.CURRENT.get().openUri(linkBuilder.toString(), OpenUriAction.NEW_WINDOW);
      }
    }

  }

}

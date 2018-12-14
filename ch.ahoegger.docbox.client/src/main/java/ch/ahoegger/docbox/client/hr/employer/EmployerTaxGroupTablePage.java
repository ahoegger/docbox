package ch.ahoegger.docbox.client.hr.employer;

import java.math.BigDecimal;
import java.util.Set;

import org.eclipse.scout.rt.client.dto.Data;
import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.TableMenuType;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractBigDecimalColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractSmartColumn;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.ISearchForm;
import org.eclipse.scout.rt.client.ui.messagebox.MessageBox;
import org.eclipse.scout.rt.client.ui.messagebox.MessageBoxes;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.classid.ClassId;
import org.eclipse.scout.rt.platform.text.TEXTS;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.platform.util.TriState;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;

import ch.ahoegger.docbox.client.hr.billing.payslip.PayslipTablePage;
import ch.ahoegger.docbox.client.hr.billing.statement.AbstractStatementTable;
import ch.ahoegger.docbox.client.hr.employee.IEmployeeTaxGroupEntity;
import ch.ahoegger.docbox.client.templates.AbstractActionWithDataChangedListener;
import ch.ahoegger.docbox.client.templates.AbstractDocboxPageWithTable;
import ch.ahoegger.docbox.client.templates.INotInherited;
import ch.ahoegger.docbox.shared.administration.taxgroup.TaxGroupLookupCall;
import ch.ahoegger.docbox.shared.hr.billing.taxgroup.IEmployeeTaxGroupService;
import ch.ahoegger.docbox.shared.hr.employee.EmployeeTaxGroupSearchFormData;
import ch.ahoegger.docbox.shared.hr.employer.EmployerLookupCall;
import ch.ahoegger.docbox.shared.hr.employer.EmployerTaxGroupSearchFormData;
import ch.ahoegger.docbox.shared.hr.employer.EmployerTaxGroupTableData;
import ch.ahoegger.docbox.shared.hr.employer.IEmployerTaxGroupService;

/**
 * <h3>{@link EmployerTaxGroupTablePage}</h3>
 *
 * @author aho
 */
@Data(EmployerTaxGroupTableData.class)
public class EmployerTaxGroupTablePage extends AbstractDocboxPageWithTable<EmployerTaxGroupTablePage.Table> {

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("EmployerTaxGroups");
  }

  @Override
  protected IPage<?> execCreateChildPage(ITableRow row) {
    PayslipTablePage page = new PayslipTablePage(getClass().getSimpleName() + "." + PayslipTablePage.class.getSimpleName());
    page.getTable().getEmployeeColumn().setVisible(true);
    page.getSearchFormInternal().getEmployerField().setValue(getTable().getEmployerColumn().getValue(row));
    page.getSearchFormInternal().getEmployerField().setEnabled(false);
    page.getSearchFormInternal().getTaxGroupField().setValue(getTable().getTaxGroupColumn().getValue(row));
    page.getSearchFormInternal().getTaxGroupField().setEnabled(false);
    return page;
  }

  @Override
  protected Class<? extends ISearchForm> getConfiguredSearchForm() {
    return EmployerTaxGroupSearchForm.class;
  }

  @Override
  protected void execLoadData(SearchFilter filter) {
    importPageData(BEANS.get(IEmployerTaxGroupService.class).getTableData((EmployerTaxGroupSearchFormData) filter.getFormData()));
  }

  @Override
  public EmployerTaxGroupSearchForm getSearchFormInternal() {
    return (EmployerTaxGroupSearchForm) super.getSearchFormInternal();
  }

  @Order(1000)
  @ClassId("1af9e377-4373-41fa-9da2-5f1c4592db25")
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

    public EmployerColumn getEmployerColumn() {
      return getColumnSet().getColumnByClass(EmployerColumn.class);
    }

    public TaxGroupColumn getTaxGroupColumn() {
      return getColumnSet().getColumnByClass(TaxGroupColumn.class);
    }

    public EmployerTaxGroupIdColumn getEmployerTaxGroupIdColumn() {
      return getColumnSet().getColumnByClass(EmployerTaxGroupIdColumn.class);
    }

    @Order(1000)
    public class EmployerTaxGroupIdColumn extends AbstractBigDecimalColumn {
      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }
    }

    @Order(2000)
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
        return 100;
      }
    }

    @Order(4000)
    public class EmployerColumn extends AbstractSmartColumn<BigDecimal> {
      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Employer");
      }

      @Override
      protected Class<? extends ILookupCall<BigDecimal>> getConfiguredLookupCall() {
        return EmployerLookupCall.class;
      }

      @Override
      protected int getConfiguredWidth() {
        return 160;
      }
    }

    @Order(10000)
    public class LinkTaxGroupMenu extends AbstractMenu implements INotInherited {
      @Override
      protected String getConfiguredText() {
        return TEXTS.get("LinkTaxGroup");
      }

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.hashSet(TableMenuType.EmptySpace);
      }

      @Override
      protected void execAction() {
        EmployerTaxGroupForm form = new EmployerTaxGroupForm();
        form.setEmployerId(getSearchFormInternal().getEmployerField().getValue());
        form.startNew();
      }
    }

    @Order(10000)
    public class DeleteMenu extends AbstractActionWithDataChangedListener {
      @Override
      protected String getConfiguredText() {
        return TEXTS.get("Delete");
      }

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.hashSet(TableMenuType.SingleSelection);
      }

      @Override
      protected Object[] getConfiguredDataChangeTypes() {
        return new Object[]{IEmployeeTaxGroupEntity.ENTITY_KEY};
      }

      @Override
      protected boolean execComputeVisibility() {
        EmployeeTaxGroupSearchFormData searchData = new EmployeeTaxGroupSearchFormData();
        searchData.setEmployerTaxGroupId(getTable().getEmployerTaxGroupIdColumn().getSelectedValue());
        return !BEANS.get(IEmployeeTaxGroupService.class).hasTableData(searchData);
      }

      @Override
      protected void execOwnerValueChanged(Object newOwnerValue) {
        updateVisibility();
      }

      @Override
      protected void execAction() {
        if (MessageBoxes.createYesNo().withBody(TEXTS.get("DeleteConfirmationTextX", getTaxGroupColumn().getSelectedDisplayText())).show() == MessageBox.YES_OPTION) {
          BEANS.get(IEmployerTaxGroupService.class).delete(getEmployerTaxGroupIdColumn().getSelectedValue());
          getDesktop().dataChanged(IEmployerTaxGroupEntity.ENTITY_KEY);
        }
      }
    }

    @Order(10000)
    public class FinalizeMenu extends AbstractActionWithDataChangedListener {
      @Override
      protected String getConfiguredText() {
        return TEXTS.get("Finalize");
      }

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.hashSet(TableMenuType.SingleSelection);
      }

      @Override
      protected Object[] getConfiguredDataChangeTypes() {
        return new Object[]{IEmployeeTaxGroupEntity.ENTITY_KEY};
      }

      @Override
      protected boolean execComputeVisibility() {
        if (getStatementIdColumn().getSelectedValue() != null) {
          return false;
        }
        EmployeeTaxGroupSearchFormData searchData = new EmployeeTaxGroupSearchFormData();
        searchData.setEmployerTaxGroupId(getTable().getEmployerTaxGroupIdColumn().getSelectedValue());
        searchData.getFinalizedRadioGroup().setValue(TriState.FALSE);
        return !BEANS.get(IEmployeeTaxGroupService.class).hasTableData(searchData);
      }

      @Override
      protected void execOwnerValueChanged(Object newOwnerValue) {
        updateVisibility();
      }

      @Override
      protected void execAction() {
        EmployerTaxGroupForm form = new EmployerTaxGroupForm();
        form.setEmployerTaxGroupId(getEmployerTaxGroupIdColumn().getSelectedValue());
        form.startFinalize();
      }
    }

  }

}

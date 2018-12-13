package ch.ahoegger.docbox.client.hr.employee;

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
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.ISearchForm;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.classid.ClassId;
import org.eclipse.scout.rt.platform.text.TEXTS;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.platform.util.TriState;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;

import ch.ahoegger.docbox.client.hr.billing.payslip.IPayslipEntity;
import ch.ahoegger.docbox.client.hr.billing.payslip.PayslipTablePage;
import ch.ahoegger.docbox.client.hr.billing.statement.AbstractStatementTable;
import ch.ahoegger.docbox.client.templates.AbstractActionWithDataChangedListener;
import ch.ahoegger.docbox.client.templates.AbstractDocboxPageWithTable;
import ch.ahoegger.docbox.client.templates.INotInherited;
import ch.ahoegger.docbox.shared.administration.taxgroup.TaxGroupLookupCall;
import ch.ahoegger.docbox.shared.hr.billing.payslip.IPayslipService;
import ch.ahoegger.docbox.shared.hr.billing.payslip.PayslipSearchFormData;
import ch.ahoegger.docbox.shared.hr.billing.taxgroup.IEmployeeTaxGroupService;
import ch.ahoegger.docbox.shared.hr.employee.EmployeeLookupCall;
import ch.ahoegger.docbox.shared.hr.employee.EmployeeTaxGroupSearchFormData;
import ch.ahoegger.docbox.shared.hr.employee.EmployeeTaxGroupTableData;

/**
 * <h3>{@link EmployeeTaxGroupTablePage}</h3>
 *
 * @author aho
 */
@Data(EmployeeTaxGroupTableData.class)
@ClassId("9bcbf2e9-c45c-476f-820e-2076a7620595")
public class EmployeeTaxGroupTablePage extends AbstractDocboxPageWithTable<EmployeeTaxGroupTablePage.Table> {

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("TaxGroup");
  }

  @Override
  protected Class<? extends ISearchForm> getConfiguredSearchForm() {
    return EmployeeTaxGroupSearchForm.class;
  }

  @Override
  public EmployeeTaxGroupSearchForm getSearchFormInternal() {
    return (EmployeeTaxGroupSearchForm) super.getSearchFormInternal();
  }

  @Override
  protected void execInitPage() {
    registerDataChangeListener(IEmployeeTaxGroupEntity.ENTITY_KEY);
  }

  @Override
  protected void execLoadData(SearchFilter filter) {
    importPageData(BEANS.get(IEmployeeTaxGroupService.class).getTableData((EmployeeTaxGroupSearchFormData) filter.getFormData()));
  }

  @Override
  protected IPage<?> execCreateChildPage(ITableRow row) {
    PayslipTablePage page = new PayslipTablePage(getClass().getSimpleName() + "." + PayslipTablePage.class.getSimpleName());
    page.setEmployeeTaxGroupId(getTable().getIdColumn().getValue(row));
    page.getTable().getEmployeeColumn().setDisplayable(false);
    return page;
  }

  @Order(1000)
  @ClassId("1b53a19d-7ea8-472b-87b0-bae662dcea7e")
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

    public EmployeeColumn getEmployeeColumn() {
      return getColumnSet().getColumnByClass(EmployeeColumn.class);
    }

    public EndColumn getEndColumn() {
      return getColumnSet().getColumnByClass(EndColumn.class);
    }

    public StartColumn getStartColumn() {
      return getColumnSet().getColumnByClass(StartColumn.class);
    }

    public TaxGroupColumn getTaxGroupColumn() {
      return getColumnSet().getColumnByClass(TaxGroupColumn.class);
    }

    public IdColumn getIdColumn() {
      return getColumnSet().getColumnByClass(IdColumn.class);
    }

    @Order(1000)
    public class IdColumn extends AbstractBigDecimalColumn {
      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }

      @Override
      protected boolean getConfiguredPrimaryKey() {
        return true;
      }
    }

    @Order(2000)
    public class TaxGroupColumn extends AbstractSmartColumn<BigDecimal> {
      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("TaxGroup");
      }

      @Override
      protected int getConfiguredWidth() {
        return 140;
      }

      @Override
      protected Class<? extends ILookupCall<BigDecimal>> getConfiguredLookupCall() {
        return TaxGroupLookupCall.class;
      }

      @Override
      public void setLookupCall(ILookupCall<BigDecimal> call) {
        ((TaxGroupLookupCall) call).setShortText(true);
        super.setLookupCall(call);
      }

    }

    @Order(3000)
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
        return 200;
      }
    }

    @Order(4000)
    public class StartColumn extends AbstractDateColumn {
      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("StartDate");
      }

      @Override
      protected int getConfiguredWidth() {
        return 100;
      }
    }

    @Order(5000)
    public class EndColumn extends AbstractDateColumn {
      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("EndDate");
      }

      @Override
      protected int getConfiguredWidth() {
        return 100;
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
        return CollectionUtility.hashSet(TableMenuType.EmptySpace);
      }

      @Override
      protected void execAction() {
        EmployeeTaxGroupForm form = new EmployeeTaxGroupForm();
        form.setEmployeeId(getSearchFormInternal().getEmployeeField().getValue());
        form.startNew();
      }
    }

    @Order(1500)
    public class EditMenu extends AbstractMenu implements INotInherited {
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
        EmployeeTaxGroupForm form = new EmployeeTaxGroupForm();
        form.setEmployeeTaxGroupId(getIdColumn().getSelectedValue());
        form.startModify();
      }

      @Override
      protected void execOwnerValueChanged(Object newOwnerValue) {
        setVisible(getStatementIdColumn().getSelectedValue() == null);
      }
    }

    @Order(1750)
    public class ViewMenu extends AbstractMenu {
      @Override
      protected String getConfiguredText() {
        return TEXTS.get("View");
      }

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.hashSet(TableMenuType.SingleSelection);
      }

      @Override
      protected void execAction() {
        EmployeeTaxGroupForm form = new EmployeeTaxGroupForm();
        form.setEmployeeTaxGroupId(getIdColumn().getSelectedValue());
        form.startView();
      }

      @Override
      protected void execOwnerValueChanged(Object newOwnerValue) {
        setVisible(getStatementIdColumn().getSelectedValue() != null);
      }
    }

    @Order(2000)
    public class FinalizeMenu extends AbstractActionWithDataChangedListener implements INotInherited {
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
        return new Object[]{IPayslipEntity.ENTITY_KEY, IPayslipEntity.ENTITY_KEY_FINALIZE};
      }

      @Override
      protected boolean execComputeVisibility() {
        if (getStatementIdColumn().getSelectedValue() != null) {
          return false;
        }
        PayslipSearchFormData payslipSearchFormData = new PayslipSearchFormData();
        payslipSearchFormData.getEmployee().setValue(getEmployeeColumn().getSelectedValue());
        payslipSearchFormData.getTaxGroup().setValue(getTaxGroupColumn().getSelectedValue());
        payslipSearchFormData.getFinalzedRadioGroup().setValue(TriState.FALSE);
        return !BEANS.get(IPayslipService.class).hasTableData(payslipSearchFormData);
      }

      @Override
      protected void execOwnerValueChanged(Object newOwnerValue) {
        updateVisibility();
      }

      @Override
      protected void execAction() {
        EmployeeTaxGroupForm form = new EmployeeTaxGroupForm();
        form.setEmployeeTaxGroupId(getIdColumn().getSelectedValue());
        form.startFinalize();
      }

    }

  }
}

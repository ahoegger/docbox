package ch.ahoegger.docbox.client.administration.hr.billing;

import java.math.BigDecimal;
import java.util.Set;

import org.eclipse.scout.rt.client.dto.Data;
import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.TableMenuType;
import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractBigDecimalColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractDateColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.desktop.IDesktop;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.ISearchForm;
import org.eclipse.scout.rt.client.ui.messagebox.MessageBox;
import org.eclipse.scout.rt.client.ui.messagebox.MessageBoxes;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.text.TEXTS;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

import ch.ahoegger.docbox.client.administration.hr.taxgroup.ITaxGroupEntity;
import ch.ahoegger.docbox.client.templates.AbstractActionWithDataChangedListener;
import ch.ahoegger.docbox.client.templates.AbstractDocboxPageWithTable;
import ch.ahoegger.docbox.or.definition.table.IBillingCicleTable;
import ch.ahoegger.docbox.shared.administration.hr.billing.IBillingCycleService;
import ch.ahoegger.docbox.shared.administration.hr.billingcycle.BillingCycleSearchFormData;
import ch.ahoegger.docbox.shared.administration.hr.billingcycle.BillingCycleTablePageData;
import ch.ahoegger.docbox.shared.hr.billing.payslip.IPayslipService;
import ch.ahoegger.docbox.shared.hr.billing.payslip.PayslipSearchFormData;

/**
 * <h3>{@link BillingCycleTablePage}</h3>
 *
 * @author aho
 */
@Data(BillingCycleTablePageData.class)
public class BillingCycleTablePage extends AbstractDocboxPageWithTable<BillingCycleTablePage.Table> {

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("BillingCycle");
  }

  @Override
  protected Class<? extends ISearchForm> getConfiguredSearchForm() {
    return BillingCycleSearchForm.class;
  }

  @Override
  protected void execInitPage() {
    registerDataChangeListener(IBillingCycleEntity.ENTITY_KEY);
  }

  @Override
  protected void execLoadData(SearchFilter filter) {
    importPageData(BEANS.get(IBillingCycleService.class).getTableData((BillingCycleSearchFormData) filter.getFormData()));
  }

  @Override
  public BillingCycleSearchForm getSearchFormInternal() {
    return (BillingCycleSearchForm) super.getSearchFormInternal();
  }

  public class Table extends AbstractTable {

    public PeriodFromColumn getPeriodFromColumn() {
      return getColumnSet().getColumnByClass(PeriodFromColumn.class);
    }

    public PeriodToColumn getPeriodToColumn() {
      return getColumnSet().getColumnByClass(PeriodToColumn.class);
    }

    public NameColumn getNameColumn() {
      return getColumnSet().getColumnByClass(NameColumn.class);
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
    public class NameColumn extends AbstractStringColumn {
      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Name");
      }

      @Override
      protected int getConfiguredWidth() {
        return IBillingCicleTable.NAME_LENGTH;
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

    @Order(10000)
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
        BillingCycleForm form = new BillingCycleForm();
        BigDecimal taxGroup = getSearchFormInternal().getTaxGroupField().getValue();
        if (taxGroup != null) {
          form.getTaxGroupField().setValue(taxGroup);
          form.getTaxGroupField().setEnabled(false);

        }
        form.startNew();
      }
    }

    @Order(11000)
    public class EditMenu extends AbstractActionWithDataChangedListener {
      @Override
      protected String getConfiguredText() {
        return TEXTS.get("Edit");
      }

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.hashSet(TableMenuType.SingleSelection);
      }

      @Override
      protected Object[] getConfiguredDataChangeTypes() {
        return new Object[]{ITaxGroupEntity.ENTITY_KEY};
      }

      @Override
      protected void execOwnerValueChanged(Object newOwnerValue) {
        updateVisibility();
      }

      @Override
      protected boolean execComputeVisibility() {
        PayslipSearchFormData searchData = new PayslipSearchFormData();
        searchData.getBillingCycle().setValue(getTable().getIdColumn().getSelectedValue());
        return !BEANS.get(IPayslipService.class).hasTableData(searchData);
      }

      @Override
      protected void execAction() {
        BillingCycleForm form = new BillingCycleForm();
        form.setBillingCycleId(getIdColumn().getSelectedValue());
        form.startModify();
      }
    }

    @Order(12000)
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
        return new Object[]{ITaxGroupEntity.ENTITY_KEY};
      }

      @Override
      protected boolean execComputeVisibility() {
        PayslipSearchFormData searchData = new PayslipSearchFormData();
        searchData.getBillingCycle().setValue(getTable().getIdColumn().getSelectedValue());
        return !BEANS.get(IPayslipService.class).hasTableData(searchData);
      }

      @Override
      protected void execOwnerValueChanged(Object newOwnerValue) {
        updateVisibility();
      }

      @Override
      protected void execAction() {
        if (MessageBoxes.createYesNo().withBody(TEXTS.get("DeleteConfirmationTextX", getNameColumn().getSelectedDisplayText())).show() == MessageBox.YES_OPTION) {
          BEANS.get(IBillingCycleService.class).delete(getIdColumn().getSelectedValue());
          IDesktop.CURRENT.get().dataChanged(IBillingCycleEntity.ENTITY_KEY);
        }
      }

    }

  }
}

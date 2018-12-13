package ch.ahoegger.docbox.client.administration.hr.taxgroup;

import java.util.Set;

import org.eclipse.scout.rt.client.dto.Data;
import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.TableMenuType;
import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractBigDecimalColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractDateColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.desktop.IDesktop;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.ISearchForm;
import org.eclipse.scout.rt.client.ui.messagebox.MessageBox;
import org.eclipse.scout.rt.client.ui.messagebox.MessageBoxes;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.text.TEXTS;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

import ch.ahoegger.docbox.client.administration.hr.billing.BillingCycleTablePage;
import ch.ahoegger.docbox.client.administration.hr.taxgroup.TaxGroupTablePage.Table;
import ch.ahoegger.docbox.client.templates.AbstractActionWithDataChangedListener;
import ch.ahoegger.docbox.shared.administration.hr.taxgroup.TaxGroupSearchFormData;
import ch.ahoegger.docbox.shared.administration.hr.taxgroup.TaxGroupTablePageData;
import ch.ahoegger.docbox.shared.administration.taxgroup.ITaxGroupService;
import ch.ahoegger.docbox.shared.hr.employer.EmployerTaxGroupSearchFormData;
import ch.ahoegger.docbox.shared.hr.employer.IEmployerTaxGroupService;

@Data(TaxGroupTablePageData.class)
public class TaxGroupTablePage extends AbstractPageWithTable<Table> {

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("TaxGroup");
  }

  @Override
  protected Class<? extends ISearchForm> getConfiguredSearchForm() {
    return TaxGroupSearchForm.class;
  }

  @Override
  public TaxGroupSearchForm getSearchFormInternal() {
    return (TaxGroupSearchForm) super.getSearchFormInternal();
  }

  @Override
  protected void execInitPage() {
    registerDataChangeListener(ITaxGroupEntity.ENTITY_KEY);
  }

  @Override
  protected IPage<?> execCreateChildPage(ITableRow row) {
    BillingCycleTablePage page = new BillingCycleTablePage();
    page.getSearchFormInternal().getTaxGroupField().setValue(getTable().getTaxGroupIdColumn().getValue(row));
    page.getSearchFormInternal().getTaxGroupField().setEnabled(false);
    return page;
  }

  @Override
  protected void execLoadData(SearchFilter filter) {
    importPageData(BEANS.get(ITaxGroupService.class).getTaxGroupTableData((TaxGroupSearchFormData) filter.getFormData()));
  }

  public class Table extends AbstractTable {

    public StartDateColumn getStartDateColumn() {
      return getColumnSet().getColumnByClass(StartDateColumn.class);
    }

    public EndDateColumn getEndDateColumn() {
      return getColumnSet().getColumnByClass(EndDateColumn.class);
    }

    public NameColumn getNameColumn() {
      return getColumnSet().getColumnByClass(NameColumn.class);
    }

    public TaxGroupIdColumn getTaxGroupIdColumn() {
      return getColumnSet().getColumnByClass(TaxGroupIdColumn.class);
    }

    @Order(1000)
    public class TaxGroupIdColumn extends AbstractBigDecimalColumn {

      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
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
        return 200;
      }
    }

    @Order(3000)
    public class StartDateColumn extends AbstractDateColumn {
      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("from");
      }

      @Override
      protected int getConfiguredWidth() {
        return 120;
      }

      @Override
      protected int getConfiguredSortIndex() {
        return 0;
      }

    }

    @Order(4000)
    public class EndDateColumn extends AbstractDateColumn {
      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("to");
      }

      @Override
      protected int getConfiguredWidth() {
        return 120;
      }
    }

    @Order(2000)
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
        TaxGroupForm form = new TaxGroupForm();
        form.startNew();
      }
    }

    @Order(3000)
    public class EditMenu extends AbstractActionWithDataChangedListener {
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
      protected Object[] getConfiguredDataChangeTypes() {
        return new Object[]{ITaxGroupEntity.ENTITY_KEY};
      }

      @Override
      protected void execOwnerValueChanged(Object newOwnerValue) {
        updateVisibility();
      }

      @Override
      protected boolean execComputeVisibility() {
        EmployerTaxGroupSearchFormData searchData = new EmployerTaxGroupSearchFormData();
        searchData.getTaxGroup().setValue(getTable().getTaxGroupIdColumn().getSelectedValue());
        return !BEANS.get(IEmployerTaxGroupService.class).hasTableData(searchData);
      }

      @Override
      protected void execAction() {
        TaxGroupForm form = new TaxGroupForm();
        form.setTaxGroupId(getTaxGroupIdColumn().getSelectedValue());
        form.startModify();
      }

    }

    @Order(4000)
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
        EmployerTaxGroupSearchFormData searchData = new EmployerTaxGroupSearchFormData();
        searchData.getTaxGroup().setValue(getTable().getTaxGroupIdColumn().getSelectedValue());
        return !BEANS.get(IEmployerTaxGroupService.class).hasTableData(searchData);
      }

      @Override
      protected void execOwnerValueChanged(Object newOwnerValue) {
        updateVisibility();
      }

      @Override
      protected void execAction() {
        if (MessageBoxes.createYesNo().withBody(TEXTS.get("DeleteConfirmationTextX", getNameColumn().getSelectedDisplayText())).show() == MessageBox.YES_OPTION) {
          BEANS.get(ITaxGroupService.class).delete(getTaxGroupIdColumn().getSelectedValue());
          IDesktop.CURRENT.get().dataChanged(ITaxGroupEntity.ENTITY_KEY);
        }
      }

    }

  }
}

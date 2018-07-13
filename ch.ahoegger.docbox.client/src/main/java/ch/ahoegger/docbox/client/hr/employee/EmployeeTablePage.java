package ch.ahoegger.docbox.client.hr.employee;

import java.math.BigDecimal;
import java.util.Set;

import org.eclipse.scout.rt.client.dto.PageData;
import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.TableMenuType;
import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractBigDecimalColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractDateColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.ISearchForm;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.text.TEXTS;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

import ch.ahoegger.docbox.or.definition.table.IEmployeeTable;
import ch.ahoegger.docbox.shared.hr.employee.IEmployeeService;
import ch.ahoegger.docbox.shared.hr.employer.EmployeeSearchFormData;
import ch.ahoegger.docbox.shared.hr.employer.EmployeeTableData;

/**
 * <h3>{@link EmployeeTablePage}</h3>
 *
 * @author Andreas Hoegger
 */
@PageData(EmployeeTableData.class)
public class EmployeeTablePage extends AbstractPageWithTable<EmployeeTablePage.Table> {

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("Employee");
  }

  @Override
  protected void execInitPage() {
    registerDataChangeListener(IEmployeeEntity.ENTITY_KEY);
  }

  @Override
  protected IPage<?> execCreateChildPage(ITableRow row) {
    EmployeeNodePage nodePage = new EmployeeNodePage();
    nodePage.setPartnerId(getTable().getPartnerIdColumn().getValue(row));
    return nodePage;
  }

  @Override
  protected void execLoadData(SearchFilter filter) {
    importPageData(BEANS.get(IEmployeeService.class).getTableData((EmployeeSearchFormData) filter.getFormData()));
  }

  @Override
  protected Class<? extends ISearchForm> getConfiguredSearchForm() {
    return EmployeeSearchForm.class;
  }

  public class Table extends AbstractTable {

    public AddressLine1Column getAddressLine1Column() {
      return getColumnSet().getColumnByClass(AddressLine1Column.class);
    }

    public AddressLine2Column getAddressLine2Column() {
      return getColumnSet().getColumnByClass(AddressLine2Column.class);
    }

    public AHVNumberColumn getAHVNumberColumn() {
      return getColumnSet().getColumnByClass(AHVNumberColumn.class);
    }

    public HourlyWageColumn getHourlyWageColumn() {
      return getColumnSet().getColumnByClass(HourlyWageColumn.class);
    }

    public StartDateColumn getStartDateColumn() {
      return getColumnSet().getColumnByClass(StartDateColumn.class);
    }

    public EndDateColumn getEndDateColumn() {
      return getColumnSet().getColumnByClass(EndDateColumn.class);
    }

    public FirstNameColumn getFirstNameColumn() {
      return getColumnSet().getColumnByClass(FirstNameColumn.class);
    }

    public LastNameColumn getLastNameColumn() {
      return getColumnSet().getColumnByClass(LastNameColumn.class);
    }

    public DisplayNameColumn getDisplayNameColumn() {
      return getColumnSet().getColumnByClass(DisplayNameColumn.class);
    }

    public AccountNumberColumn getAccountNumberColumn() {
      return getColumnSet().getColumnByClass(AccountNumberColumn.class);
    }

    public BirthdayColumn getBirthdayColumn() {
      return getColumnSet().getColumnByClass(BirthdayColumn.class);
    }

    public PartnerIdColumn getPartnerIdColumn() {
      return getColumnSet().getColumnByClass(PartnerIdColumn.class);
    }

    @Order(100)
    public class PartnerIdColumn extends AbstractBigDecimalColumn {

      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }

    }

    @Order(200)
    public class DisplayNameColumn extends AbstractStringColumn {
      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("DisplayName");
      }

      @Override
      protected int getConfiguredWidth() {
        return 180;
      }

      @Override
      protected boolean getConfiguredSummary() {
        return true;
      }
    }

    @Order(300)
    public class FirstNameColumn extends AbstractStringColumn {
      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Firstname");
      }

      @Override
      protected int getConfiguredWidth() {
        return 200;
      }

      @Override
      protected int getConfiguredSortIndex() {
        return 0;
      }
    }

    @Order(400)
    public class LastNameColumn extends AbstractStringColumn {
      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Name");
      }

      @Override
      protected int getConfiguredWidth() {
        return 200;
      }

      @Override
      protected int getConfiguredSortIndex() {
        return 0;
      }
    }

    @Order(500)
    public class AddressLine1Column extends AbstractStringColumn {
      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("AddressLine1");
      }

      @Override
      protected int getConfiguredWidth() {
        return 200;
      }
    }

    @Order(600)
    public class AddressLine2Column extends AbstractStringColumn {
      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("AddressLine2");
      }

      @Override
      protected int getConfiguredWidth() {
        return 200;
      }
    }

    @Order(700)
    public class AHVNumberColumn extends AbstractStringColumn {
      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("AHVNumber");
      }

      @Override
      protected boolean getConfiguredVisible() {
        return false;
      }

      @Override
      protected int getConfiguredWidth() {
        return 150;
      }
    }

    @Order(800)
    public class AccountNumberColumn extends AbstractStringColumn {
      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("AccountNumber");
      }

      @Override
      protected int getConfiguredWidth() {
        return 180;
      }
    }

    @Order(900)
    public class BirthdayColumn extends AbstractDateColumn {
      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Birthday");
      }
    }

    @Order(1000)
    public class HourlyWageColumn extends AbstractBigDecimalColumn {
      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("HourlyWage");
      }

      @Override
      protected int getConfiguredWidth() {
        return 100;
      }

      @Override
      protected BigDecimal getConfiguredMinValue() {
        return IEmployeeTable.HOURLY_WAGE_MIN;
      }

      @Override
      protected BigDecimal getConfiguredMaxValue() {
        return IEmployeeTable.HOURLY_WAGE_MAX;
      }
    }

    @Order(1100)
    public class StartDateColumn extends AbstractDateColumn {
      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("from");
      }

      @Override
      protected int getConfiguredWidth() {
        return 100;
      }
    }

    @Order(1200)
    public class EndDateColumn extends AbstractDateColumn {
      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("to");
      }

      @Override
      protected int getConfiguredWidth() {
        return 200;
      }
    }

    @Order(10000)
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
        EmployeeForm form = new EmployeeForm();
        form.setPartnerId(getPartnerIdColumn().getSelectedValue());
        form.startModify();
      }
    }

    @Order(10100)
    public class NewMenu extends AbstractMenu {
      @Override
      protected String getConfiguredText() {
        return TEXTS.get("New");
      }

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.hashSet(TableMenuType.SingleSelection, TableMenuType.EmptySpace);
      }

      @Override
      protected void execAction() {
        EmployeeForm form = new EmployeeForm();
        form.startNew();
      }
    }

  }

}

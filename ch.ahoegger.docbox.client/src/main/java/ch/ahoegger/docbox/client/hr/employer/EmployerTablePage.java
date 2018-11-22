package ch.ahoegger.docbox.client.hr.employer;

import java.util.Set;

import org.eclipse.scout.rt.client.dto.Data;
import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.TableMenuType;
import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractBigDecimalColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.text.TEXTS;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

import ch.ahoegger.docbox.client.AbstractDocboxPageWithTable;
import ch.ahoegger.docbox.shared.hr.employer.EmployerTablePageData;
import ch.ahoegger.docbox.shared.hr.employer.IEmployerService;

/**
 * <h3>{@link EmployerTablePage}</h3>
 *
 * @author aho
 */
@Data(EmployerTablePageData.class)
public class EmployerTablePage extends AbstractDocboxPageWithTable<EmployerTablePage.Table> {

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("Employer");
  }

  @Override
  protected void execLoadData(SearchFilter filter) {

    importPageData(BEANS.get(IEmployerService.class).getTableData());
  }

  public class Table extends AbstractTable {

    public EmployerIdColumn getEmployerIdColumn() {
      return getColumnSet().getColumnByClass(EmployerIdColumn.class);
    }

    public NameColumn getNameColumn() {
      return getColumnSet().getColumnByClass(NameColumn.class);
    }

    @Order(1000)
    public class EmployerIdColumn extends AbstractBigDecimalColumn {

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

    @Order(10000)
    public class EditEmployerMenu extends AbstractMenu {
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
        EmployerForm form = new EmployerForm();
        form.setEmployerId(getEmployerIdColumn().getSelectedValue());
        form.startModify();
      }
    }

    @Order(11000)
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
        EmployerForm form = new EmployerForm();
        form.startNew();
      }
    }

  }
}

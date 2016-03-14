package ch.ahoegger.docbox.client.administration.user;

import org.eclipse.scout.rt.client.dto.PageData;
import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

import ch.ahoegger.docbox.shared.administration.user.IUserService;
import ch.ahoegger.docbox.shared.administration.user.UserTablePageData;

/**
 * <h3>{@link UserTablePage}</h3>
 *
 * @author aho
 */
@PageData(UserTablePageData.class)
public class UserTablePage extends AbstractPageWithTable<UserTablePage.Table> {

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("Users");
  }

  @Override
  protected void execLoadData(SearchFilter filter) {
    importPageData(BEANS.get(IUserService.class).getUserTableData());
  }

  @Override
  protected IPage<?> execCreateChildPage(ITableRow row) {
    return new UserDetailPage(getTable().getUsernameColumn().getValue(row));
  }

  public class Table extends AbstractTable {

    public FirstnameColumn getFirstnameColumn() {
      return getColumnSet().getColumnByClass(FirstnameColumn.class);
    }

    public UsernameColumn getUsernameColumn() {
      return getColumnSet().getColumnByClass(UsernameColumn.class);
    }

    public NameColumn getNameColumn() {
      return getColumnSet().getColumnByClass(NameColumn.class);
    }

    @Order(1000)
    public class UsernameColumn extends AbstractStringColumn {
      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Username");
      }

      @Override
      protected int getConfiguredWidth() {
        return 200;
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
    public class FirstnameColumn extends AbstractStringColumn {
      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Firstname");
      }

      @Override
      protected int getConfiguredWidth() {
        return 200;
      }
    }

  }
}

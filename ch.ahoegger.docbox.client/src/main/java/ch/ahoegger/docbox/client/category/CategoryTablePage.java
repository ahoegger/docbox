package ch.ahoegger.docbox.client.category;

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

import ch.ahoegger.docbox.client.document.DocumentTablePage;
import ch.ahoegger.docbox.shared.category.CategorySearchFormData;
import ch.ahoegger.docbox.shared.category.CategoryTableData;
import ch.ahoegger.docbox.shared.category.ICategoryService;

/**
 * <h3>{@link CategoryTablePage}</h3>
 *
 * @author Andreas Hoegger
 */
@PageData(CategoryTableData.class)
public class CategoryTablePage extends AbstractPageWithTable<CategoryTablePage.Table> {
  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("Categories");
  }

  @Override
  protected void execInitPage() {
    registerDataChangeListener(ICategoryEntity.ENTITY_KEY);
  }

  @Override
  protected IPage<?> execCreateChildPage(ITableRow row) {
    DocumentTablePage documentTablePage = new DocumentTablePage();
    documentTablePage.setCategoryId(getTable().getCategoryIdColumn().getValue(row));

    return documentTablePage;
  }

  @Override
  protected void execLoadData(SearchFilter filter) {
    importPageData(BEANS.get(ICategoryService.class).getTableData((CategorySearchFormData) filter.getFormData()));
  }

  @Override
  protected Class<? extends ISearchForm> getConfiguredSearchForm() {
    return CategorySearchForm.class;
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

    public CategoryIdColumn getCategoryIdColumn() {
      return getColumnSet().getColumnByClass(CategoryIdColumn.class);
    }

    @Order(10)
    public class CategoryIdColumn extends AbstractBigDecimalColumn {

      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }

    }

    @Order(20)
    public class NameColumn extends AbstractStringColumn {
      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Name");
      }

      @Override
      protected int getConfiguredWidth() {
        return 300;
      }

      @Override
      protected int getConfiguredSortIndex() {
        return 0;
      }
    }

    @Order(30)
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

    @Order(40)
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

    @Order(1000)
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
        CategoryForm form = new CategoryForm();
        form.setCategoryId(getCategoryIdColumn().getSelectedValue());
        form.startModify();
      }
    }

    @Order(2000)
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
        CategoryForm form = new CategoryForm();
        form.startNew();
      }
    }

  }

}

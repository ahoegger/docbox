package ch.ahoegger.docbox.client.document.field;

import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.TableMenuType;
import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.TableAdapter;
import org.eclipse.scout.rt.client.ui.basic.table.TableEvent;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.form.fields.tablefield.AbstractTableField;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractFormFieldData;

/**
 * <h3>{@link AbstractOcrSearchTableField}</h3>
 *
 * @author Andreas Hoegger
 */
public abstract class AbstractOcrSearchTableField extends AbstractTableField<AbstractOcrSearchTableField.Table> {

  @Override
  protected String getConfiguredLabel() {
    return TEXTS.get("Texts");
  }

  @Override
  protected void execInitField() {
    getTable().addTableListener(new TableAdapter() {
      @Override
      public void tableChanged(TableEvent e) {
        switch (e.getType()) {
          case TableEvent.TYPE_ROWS_UPDATED:
            ensureEmptyRow();
            break;
        }
      }
    });
    ensureEmptyRow();
  }

  public void resetField() {
    getTable().discardAllRows();
    ensureEmptyRow();
  }

  private void ensureEmptyRow() {
    getTable().deleteRows(getTable().getRows().stream().filter(row -> StringUtility.isNullOrEmpty(getTable().getSearchTextColumn().getValue(row))).collect(Collectors.toList()));
    ITableRow row = getTable().createRow();
    getTable().addRow(row, false);
  }

  @Override
  public void importFormFieldData(AbstractFormFieldData source, boolean valueChangeTriggersEnabled) {
    super.importFormFieldData(source, valueChangeTriggersEnabled);
    ensureEmptyRow();
  }

  public class Table extends AbstractTable {

    @Override
    protected boolean getConfiguredHeaderVisible() {
      return false;
    }

    @Override
    protected boolean getConfiguredAutoDiscardOnDelete() {
      return true;
    }

    @Override
    protected boolean getConfiguredMultiSelect() {
      return false;
    }

    @Override
    protected boolean getConfiguredAutoResizeColumns() {
      return true;
    }

    public SearchTextColumn getSearchTextColumn() {
      return getColumnSet().getColumnByClass(SearchTextColumn.class);
    }

    @Order(20)
    public class SearchTextColumn extends AbstractStringColumn {

      @Override
      protected int getConfiguredWidth() {
        return 100;
      }

      @Override
      protected boolean getConfiguredEditable() {
        return true;
      }

    }

    @Order(1010)
    public class DeleteMenu extends AbstractMenu {
      @Override
      protected String getConfiguredText() {
        return TEXTS.get("Delete");
      }

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.hashSet(TableMenuType.SingleSelection);
      }

      @Override
      protected void execAction() {
        deleteRow(getSelectedRow());
      }
    }

  }
}

package ch.ahoegger.docbox.client.document.field;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.TableMenuType;
import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.TableAdapter;
import org.eclipse.scout.rt.client.ui.basic.table.TableEvent;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractSmartColumn;
import org.eclipse.scout.rt.client.ui.form.fields.IFormField;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield2.AbstractSmartField2;
import org.eclipse.scout.rt.client.ui.form.fields.tablefield.AbstractTableField;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.platform.util.TriState;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractFormFieldData;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;
import org.eclipse.scout.rt.shared.services.lookup.ILookupRow;

import ch.ahoegger.docbox.shared.administration.user.UserLookupCall;
import ch.ahoegger.docbox.shared.security.permission.PermissionCodeType;
import ch.ahoegger.docbox.shared.security.permission.PermissionLookupCall;

/**
 * <h3>{@link AbstractPermissionTableField}</h3>
 *
 * @author Andreas Hoegger
 */
public abstract class AbstractPermissionTableField extends AbstractTableField<AbstractPermissionTableField.Table> {

  @Override
  protected String getConfiguredLabel() {
    return TEXTS.get("Permissions");
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
  }

  private void ensureEmptyRow() {
    getTable().deleteRows(getTable().getRows().stream().filter(row -> (getTable().getUserColumn().getValue(row) == null && getTable().getPermissionColumn().getValue(row) == null)).collect(Collectors.toList()));
    ITableRow row = getTable().createRow();
    getTable().addRow(row, false);
  }

  @Override
  public void importFormFieldData(AbstractFormFieldData source, boolean valueChangeTriggersEnabled) {
    super.importFormFieldData(source, valueChangeTriggersEnabled);
    ensureEmptyRow();
  }

  protected void validateOwners() {
    if (getTable().getPermissionColumn().getValues().stream()
        .filter(p -> p != null)
        .filter(p -> p.intValue() == PermissionCodeType.OwnerCode.ID)
        .collect(Collectors.toList()).size() > 1) {
      addErrorStatus(TEXTS.get("Validate_OneOwnerOnly"));
    }
    else {
      clearErrorStatus();
    }
  }

  public class Table extends AbstractTable {

    public PermissionColumn getPermissionColumn() {
      return getColumnSet().getColumnByClass(PermissionColumn.class);
    }

    public UserColumn getUserColumn() {
      return getColumnSet().getColumnByClass(UserColumn.class);
    }

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

    @Override
    protected void execContentChanged() {
      validateOwners();
    }

    @Order(10)
    public class UserColumn extends AbstractSmartColumn<String> {

      @Override
      protected int getConfiguredWidth() {
        return 100;
      }

      @Override
      protected Class<? extends ILookupCall<String>> getConfiguredLookupCall() {
        return UserLookupCall.class;
      }

      @Override
      protected boolean getConfiguredEditable() {
        return true;
      }

      @Override
      protected IFormField execPrepareEdit(ITableRow row) {
        return super.execPrepareEdit(row);
      }

      @Override
      protected IFormField prepareEditInternal(ITableRow row) {
        return new P_UserSmarField(row);
      }

      public class P_UserSmarField extends AbstractSmartField2<String> {
        private ITableRow m_row;

        public P_UserSmarField(ITableRow row) {
          m_row = row;

        }

        @Override
        protected Class<? extends ILookupCall<String>> getConfiguredLookupCall() {
          return UserLookupCall.class;
        }

        @Override
        protected void execPrepareLookup(ILookupCall<String> call) {
          call.setActive(TriState.UNDEFINED);
          super.execPrepareLookup(call);
        }

        @Override
        protected void execFilterBrowseLookupResult(ILookupCall<String> call, List<ILookupRow<String>> result) {
          filterDuplicateTypesLookupResult(m_row, result);
        }

        @Override
        protected void execFilterTextLookupResult(ILookupCall<String> call, List<ILookupRow<String>> result) {
          filterDuplicateTypesLookupResult(m_row, result);
        }

        /**
         * @param row
         * @param result
         */
        private void filterDuplicateTypesLookupResult(ITableRow row, List<ILookupRow<String>> result) {
          Set<String> alreadyUsed = new HashSet<String>(getUserColumn().getValues());
          alreadyUsed.remove(getUserColumn().getValue(row));
          for (Iterator<ILookupRow<String>> iterator = result.iterator(); iterator.hasNext();) {
            ILookupRow<String> current = iterator.next();
            if (alreadyUsed.contains(current.getKey())) {
              iterator.remove();
            }
          }
        }

      }

    }

    @Order(20)
    public class PermissionColumn extends AbstractSmartColumn<Integer> {

      @Override
      protected int getConfiguredWidth() {
        return 100;
      }

      @Override
      protected Class<? extends ILookupCall<Integer>> getConfiguredLookupCall() {
        return PermissionLookupCall.class;
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

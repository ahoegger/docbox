package ch.ahoegger.docbox.client.document.field;

import java.math.BigDecimal;
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
import org.eclipse.scout.rt.client.ui.form.FormEvent;
import org.eclipse.scout.rt.client.ui.form.FormListener;
import org.eclipse.scout.rt.client.ui.form.fields.IFormField;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield.AbstractSmartField;
import org.eclipse.scout.rt.client.ui.form.fields.tablefield.AbstractTableField;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractFormFieldData;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;
import org.eclipse.scout.rt.shared.services.lookup.ILookupRow;

import ch.ahoegger.docbox.client.partner.PartnerForm;
import ch.ahoegger.docbox.shared.partner.PartnerLookupCall;

/**
 * <h3>{@link AbstractPartnerTableField}</h3>
 *
 * @author Andreas Hoegger
 */
public abstract class AbstractPartnerTableField extends AbstractTableField<AbstractPartnerTableField.Table> {

  @Override
  protected String getConfiguredLabel() {
    return TEXTS.get("Partners");
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
    getTable().deleteRows(getTable().getRows().stream().filter(row -> getTable().getPartnerColumn().getValue(row) == null).collect(Collectors.toList()));
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

    public PartnerColumn getPartnerColumn() {
      return getColumnSet().getColumnByClass(PartnerColumn.class);
    }

    @Order(20)
    public class PartnerColumn extends AbstractSmartColumn<BigDecimal> {

      @Override
      protected int getConfiguredWidth() {
        return 100;
      }

      @Override
      protected boolean getConfiguredEditable() {
        return true;
      }

      @Override
      protected Class<? extends ILookupCall<BigDecimal>> getConfiguredLookupCall() {
        return PartnerLookupCall.class;
      }

      @Override
      protected IFormField prepareEditInternal(ITableRow row) {
        return new P_PartnerSmarField(row);
      }

      public class P_PartnerSmarField extends AbstractSmartField<BigDecimal> {
        private ITableRow m_row;

        public P_PartnerSmarField(ITableRow row) {
          m_row = row;

        }

        @Override
        protected Class<? extends ILookupCall<BigDecimal>> getConfiguredLookupCall() {
          return PartnerLookupCall.class;
        }

        @Override
        protected void execFilterBrowseLookupResult(ILookupCall<BigDecimal> call, List<ILookupRow<BigDecimal>> result) {
          filterDuplicateTypesLookupResult(m_row, result);
        }

        @Override
        protected void execFilterTextLookupResult(ILookupCall<BigDecimal> call, List<ILookupRow<BigDecimal>> result) {
          filterDuplicateTypesLookupResult(m_row, result);
        }

        /**
         * @param row
         * @param result
         */
        private void filterDuplicateTypesLookupResult(ITableRow row, List<ILookupRow<BigDecimal>> result) {
          Set<BigDecimal> alreadyUsed = new HashSet<BigDecimal>(getPartnerColumn().getValues());
          alreadyUsed.remove(getPartnerColumn().getValue(row));
          for (Iterator<ILookupRow<BigDecimal>> iterator = result.iterator(); iterator.hasNext();) {
            ILookupRow<BigDecimal> current = iterator.next();
            if (alreadyUsed.contains(current.getKey())) {
              iterator.remove();
            }
          }
        }

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

    @Order(3000)
    public class NewPartnerMenu extends AbstractMenu {
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
        PartnerForm form = new PartnerForm();
        form.startNew();
        form.addFormListener(new FormListener() {

          @Override
          public void formChanged(FormEvent e) {
            if (FormEvent.TYPE_STORE_AFTER == e.getType()) {
              ITableRow row = addRow(createRow(), true);
              getPartnerColumn().setValue(row, form.getPartnerId());
            }
          }
        });
      }
    }

  }
}

package ch.ahoegger.docbox.client.document.field;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Set;

import org.eclipse.scout.rt.client.context.ClientRunContexts;
import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.TableMenuType;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.desktop.IDesktop;
import org.eclipse.scout.rt.client.ui.form.FormEvent;
import org.eclipse.scout.rt.client.ui.form.FormListener;
import org.eclipse.scout.rt.client.ui.form.fields.listbox.AbstractListBox;
import org.eclipse.scout.rt.client.ui.messagebox.MessageBox;
import org.eclipse.scout.rt.client.ui.messagebox.MessageBoxes;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;

import ch.ahoegger.docbox.client.category.CategoryForm;
import ch.ahoegger.docbox.client.category.ICategoryEntity;
import ch.ahoegger.docbox.shared.category.CategoryLookupCall;
import ch.ahoegger.docbox.shared.category.ICategoryService;

/**
 * <h3>{@link AbstractCategoriesListBox}</h3>
 *
 * @author aho
 */
public abstract class AbstractCategoriesListBox extends AbstractListBox<BigDecimal> {
  @Override
  protected String getConfiguredLabel() {
    return TEXTS.get("Categories");
  }

  @Override
  protected Class<? extends ILookupCall<BigDecimal>> getConfiguredLookupCall() {
    return CategoryLookupCall.class;
  }

  @Override
  protected void execInitField() {
    registerDataChangeListener(ICategoryEntity.ENTITY_KEY);
  }

  @Override
  protected void execDataChanged(Object... dataTypes) {
    if (Arrays.stream(dataTypes).filter(o -> o == ICategoryEntity.ENTITY_KEY).count() > 0) {
      loadListBoxData();
    }
  }

  @Override
  protected void execPrepareLookup(ILookupCall<BigDecimal> call) {
    call.setMaster(ICategoryEntity.ENTITY_KEY);
  }

  protected IDesktop getDesktop() {
    return ClientRunContexts.copyCurrent().getDesktop();
  }

  public class CategoriesTable extends DefaultListBoxTable {

    @Override
    protected boolean getConfiguredAutoDiscardOnDelete() {
      return false;
    }

    @Order(1000)
    public class NewCategoryMenu extends AbstractMenu {
      @Override
      protected String getConfiguredText() {
        return TEXTS.get("New");
      }

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.hashSet(TableMenuType.EmptySpace, TableMenuType.SingleSelection);
      }

      @Override
      protected void execAction() {
        CategoryForm form = new CategoryForm();
        form.startNew();
        form.addFormListener(new FormListener() {

          @Override
          public void formChanged(FormEvent e) {
            if (e.getType() == FormEvent.TYPE_STORE_AFTER) {

              ITableRow newRow = getKeyColumn().findRow(form.getCategoryId());
              if (newRow != null) {
                checkRow(newRow, true);
              }
            }
          }
        });
      }
    }

    @Order(1500)
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
        form.setCategoryId(getKeyColumn().getSelectedValue());
        form.startModify();
      }
    }

    @Order(2000)
    public class DeleteCategoryMenu extends AbstractMenu {
      @Override
      protected String getConfiguredText() {
        return TEXTS.get("Delete");
      }

      @Override
      protected double getConfiguredViewOrder() {
        // TODO check admin
        return super.getConfiguredViewOrder();
      }

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.hashSet(TableMenuType.SingleSelection);
      }

      @Override
      protected void execAction() {
        if (MessageBox.YES_OPTION == MessageBoxes.createYesNo()
            .withHeader(TEXTS.get("Delete"))
            .withBody(TEXTS.get("VerificationDelete", getTextColumn().getSelectedDisplayText())).show()) {
          BEANS.get(ICategoryService.class).delete(getKeyColumn().getSelectedValue().longValue());

          getDesktop().dataChanged(ICategoryEntity.ENTITY_KEY);
        }
      }
    }

  }

}

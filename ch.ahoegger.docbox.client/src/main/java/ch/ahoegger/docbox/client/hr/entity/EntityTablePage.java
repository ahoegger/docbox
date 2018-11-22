package ch.ahoegger.docbox.client.hr.entity;

import java.math.BigDecimal;
import java.util.Set;

import org.eclipse.scout.rt.client.context.ClientRunContexts;
import org.eclipse.scout.rt.client.dto.Data;
import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.TableMenuType;
import org.eclipse.scout.rt.client.ui.desktop.IDesktop;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.ISearchForm;
import org.eclipse.scout.rt.client.ui.messagebox.MessageBox;
import org.eclipse.scout.rt.client.ui.messagebox.MessageBoxes;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.text.TEXTS;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.platform.util.ObjectUtility;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

import ch.ahoegger.docbox.client.AbstractDocboxPageWithTable;
import ch.ahoegger.docbox.client.hr.entity.EntityTablePage.Table;
import ch.ahoegger.docbox.client.hr.entity.EntityTablePage.Table.NewExpenseMenu;
import ch.ahoegger.docbox.client.hr.entity.EntityTablePage.Table.NewWorkMenu;
import ch.ahoegger.docbox.shared.hr.billing.PayslipCodeType.UnbilledCode;
import ch.ahoegger.docbox.shared.hr.entity.EntitySearchFormData;
import ch.ahoegger.docbox.shared.hr.entity.EntityTablePageData;
import ch.ahoegger.docbox.shared.hr.entity.EntityTypeCodeType.ExpenseCode;
import ch.ahoegger.docbox.shared.hr.entity.EntityTypeCodeType.WorkCode;
import ch.ahoegger.docbox.shared.hr.entity.IEntityService;

@Data(EntityTablePageData.class)
public class EntityTablePage extends AbstractDocboxPageWithTable<Table> {

  private BigDecimal m_partnerId;
  private BigDecimal m_payslipId;

  public EntityTablePage(BigDecimal partnerId) {
    m_partnerId = partnerId;
  }

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("Entity");
  }

  @Override
  protected Class<? extends ISearchForm> getConfiguredSearchForm() {
    return EntitySearchForm.class;
  }

  @Override
  public EntitySearchForm getSearchFormInternal() {
    return (EntitySearchForm) super.getSearchFormInternal();
  }

  @Override
  protected void execInitPage() {
    registerDataChangeListener(IWorkItemEntity.WORK_ITEM_KEY);
  }

  @Override
  protected void execInitSearchForm() {
    getSearchFormInternal().getPartnerIdField().setValue(m_partnerId);
    getSearchFormInternal().setPayslipId(getPayslipId());
  }

  @Override
  protected void execLoadData(SearchFilter filter) {
    importPageData(BEANS.get(IEntityService.class).getEntityTableData((EntitySearchFormData) filter.getFormData()));
  }

  public void setPartnerId(BigDecimal partnerId) {
    m_partnerId = partnerId;
  }

  public BigDecimal getPartnerId() {
    return m_partnerId;
  }

  public void setPayslipId(BigDecimal payslipId) {
    m_payslipId = payslipId;
    getTable().getMenuByClass(NewWorkMenu.class).setVisible(ObjectUtility.equals(UnbilledCode.ID, m_payslipId));
    getTable().getMenuByClass(NewExpenseMenu.class).setVisible(ObjectUtility.equals(UnbilledCode.ID, m_payslipId));
  }

  public BigDecimal getPayslipId() {
    return m_payslipId;
  }

  protected IDesktop getDesktop() {
    return ClientRunContexts.copyCurrent().getDesktop();
  }

  public class Table extends AbstractEntityTable {

    @Order(1000)
    public class NewWorkMenu extends AbstractMenu {
      @Override
      protected String getConfiguredText() {
        return TEXTS.get("NewWork");
      }

      @Override
      protected String getConfiguredIconId() {
        return "font:icomoon \uf0ad";
      }

      @Override
      protected boolean getConfiguredVisible() {
        return false;
      }

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.hashSet(TableMenuType.SingleSelection, TableMenuType.EmptySpace);
      }

      @Override
      protected void execAction() {
        EntityForm form = new EntityForm(getPartnerId());
        form.setEntityType(WorkCode.ID);
        form.startNew();
      }
    }

    @Order(1000)
    public class NewExpenseMenu extends AbstractMenu {
      @Override
      protected String getConfiguredText() {
        return TEXTS.get("NewExpense");
      }

      @Override
      protected String getConfiguredIconId() {
        return "font:icomoon \ue900";
      }

      @Override
      protected boolean getConfiguredVisible() {
        return false;
      }

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.hashSet(TableMenuType.SingleSelection, TableMenuType.EmptySpace);
      }

      @Override
      protected void execAction() {
        EntityForm form = new EntityForm(getPartnerId());
        form.setEntityType(ExpenseCode.ID);
        form.startNew();
      }
    }

    @Order(2000)
    public class EditMenu extends AbstractMenu {
      @Override
      protected String getConfiguredText() {
        return TEXTS.get("EditEntity");
      }

      @Override
      protected String getConfiguredIconId() {
        return "font:icomoon \ue903";
      }

      @Override
      protected void execOwnerValueChanged(Object newOwnerValue) {
        setVisible(ObjectUtility.equals(getPayslipId(), UnbilledCode.ID));
      }

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.hashSet(TableMenuType.SingleSelection);
      }

      @Override
      protected void execAction() {
        EntityForm form = new EntityForm(getPartnerIdColumn().getSelectedValue());
        form.setEntityId(getEnityIdColumn().getSelectedValue());
        form.startModify();

      }
    }

    @Order(3000)
    public class ViewEntityMenu extends AbstractMenu {
      @Override
      protected String getConfiguredText() {
        return TEXTS.get("ViewEntity");
      }

      @Override
      protected void execOwnerValueChanged(Object newOwnerValue) {
        setVisible(ObjectUtility.notEquals(getPayslipId(), UnbilledCode.ID));
      }

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.hashSet(TableMenuType.SingleSelection);
      }

      @Override
      protected void execAction() {
        EntityForm form = new EntityForm(getPartnerIdColumn().getSelectedValue());
        form.setEntityId(getEnityIdColumn().getSelectedValue());
        form.startViewEntity();
      }
    }

    @Order(4000)
    public class DeleteMenu extends AbstractMenu {
      @Override
      protected String getConfiguredText() {
        return TEXTS.get("DeleteEntity");
      }

      @Override
      protected String getConfiguredIconId() {
        return "font:icomoon \ue902";
      }

      @Override
      protected void execInitAction() {
        setVisible(ObjectUtility.equals(getPayslipId(), UnbilledCode.ID));
      }

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.hashSet(TableMenuType.SingleSelection);
      }

      @Override
      protected void execAction() {
        if (MessageBox.YES_OPTION == MessageBoxes.createYesNo()
            .withHeader(TEXTS.get("Delete"))
            .withBody(TEXTS.get("VerificationDelete", getDateColumn().getSelectedDisplayText())).show()) {
          BEANS.get(IEntityService.class).delete(getEnityIdColumn().getSelectedValue());

          getDesktop().dataChanged(IWorkItemEntity.WORK_ITEM_KEY);
        }
      }
    }

  }
}

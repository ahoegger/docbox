package ch.ahoegger.docbox.client.hr.entity;

import java.math.BigDecimal;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.bigdecimalfield.AbstractBigDecimalField;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.datefield.AbstractDateField;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.text.TEXTS;
import org.eclipse.scout.rt.platform.util.ObjectUtility;

import ch.ahoegger.docbox.client.hr.entity.EntityForm.MainBox.CancelButton;
import ch.ahoegger.docbox.client.hr.entity.EntityForm.MainBox.EntityDateField;
import ch.ahoegger.docbox.client.hr.entity.EntityForm.MainBox.ExpenseAmountField;
import ch.ahoegger.docbox.client.hr.entity.EntityForm.MainBox.OkButton;
import ch.ahoegger.docbox.client.hr.entity.EntityForm.MainBox.SaveAndNewExpenseButton;
import ch.ahoegger.docbox.client.hr.entity.EntityForm.MainBox.SaveAndNewWorkButton;
import ch.ahoegger.docbox.client.hr.entity.EntityForm.MainBox.TextField;
import ch.ahoegger.docbox.client.hr.entity.EntityForm.MainBox.WorkHoursField;
import ch.ahoegger.docbox.or.definition.table.IEntityTable;
import ch.ahoegger.docbox.shared.hr.entity.EntityFormData;
import ch.ahoegger.docbox.shared.hr.entity.EntityTypeCodeType.ExpenseCode;
import ch.ahoegger.docbox.shared.hr.entity.EntityTypeCodeType.WorkCode;
import ch.ahoegger.docbox.shared.hr.entity.IEntityService;

@FormData(value = EntityFormData.class, sdkCommand = FormData.SdkCommand.CREATE)
public class EntityForm extends AbstractForm {

  private BigDecimal m_entityId;
  private BigDecimal m_entityType;
  private BigDecimal m_partnerId;
  private BigDecimal m_payslipAccountingGroupId;

  public EntityForm(BigDecimal partnerId) {
    m_partnerId = partnerId;
  }

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("Entity");
  }

  @FormData
  public void setEntityId(BigDecimal entityId) {
    m_entityId = entityId;
  }

  @FormData
  public BigDecimal getEntityId() {
    return m_entityId;
  }

  @FormData
  public void setEntityType(BigDecimal entityType) {
    m_entityType = entityType;
  }

  @FormData
  public BigDecimal getEntityType() {
    return m_entityType;
  }

  @FormData
  public void setPartnerId(BigDecimal partnerId) {
    m_partnerId = partnerId;
  }

  @FormData
  public BigDecimal getPartnerId() {
    return m_partnerId;
  }

  @FormData
  public void setPayslipAccountingId(BigDecimal payslipAccountingGroupId) {
    m_payslipAccountingGroupId = payslipAccountingGroupId;
  }

  @FormData
  public BigDecimal getPayslipAccountingId() {
    return m_payslipAccountingGroupId;
  }

  protected boolean isWork() {
    return ObjectUtility.equals(WorkCode.ID, getEntityType());
  }

  public void startModify() {
    startInternalExclusive(new ModifyHandler());
  }

  public void startNew() {
    startInternal(new NewHandler());
  }

  public void startViewEntity() {
    startInternal(new ViewEntityHandler());
  }

  public CancelButton getCancelButton() {
    return getFieldByClass(CancelButton.class);
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  public EntityDateField getEntityDateField() {
    return getFieldByClass(EntityDateField.class);
  }

  public WorkHoursField getWorkHoursField() {
    return getFieldByClass(WorkHoursField.class);
  }

  public ExpenseAmountField getExpenseAmountField() {
    return getFieldByClass(ExpenseAmountField.class);
  }

  public TextField getTextField() {
    return getFieldByClass(TextField.class);
  }

  public SaveAndNewWorkButton getSaveAndNewWorkButton() {
    return getFieldByClass(SaveAndNewWorkButton.class);
  }

  public SaveAndNewExpenseButton getSaveAndNewExpenseButton() {
    return getFieldByClass(SaveAndNewExpenseButton.class);
  }

  public OkButton getOkButton() {
    return getFieldByClass(OkButton.class);
  }

  @Order(1000)
  public class MainBox extends AbstractGroupBox {

    @Override
    protected boolean getConfiguredBorderVisible() {
      return true;
    }

    @Override
    protected String getConfiguredBorderDecoration() {
      return BORDER_DECORATION_EMPTY;
    }

    @Order(1000)
    public class EntityDateField extends AbstractDateField {
      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("Date");
      }
    }

    @Order(2000)
    public class WorkHoursField extends AbstractBigDecimalField {
      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("Hours");
      }

      @Override
      protected BigDecimal getConfiguredMinValue() {
        return IEntityTable.WORKING_HOURS_MIN;
      }

      @Override
      protected BigDecimal getConfiguredMaxValue() {
        return IEntityTable.WORKING_HOURS_MAX;
      }

    }

    @Order(3000)
    public class ExpenseAmountField extends AbstractBigDecimalField {
      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("Amount");
      }

      @Override
      protected BigDecimal getConfiguredMinValue() {
        return IEntityTable.EXPENSE_AMOUNT_MIN;
      }

      @Override
      protected BigDecimal getConfiguredMaxValue() {
        return IEntityTable.EXPENSE_AMOUNT_MAX;
      }

    }

    @Order(4000)
    public class TextField extends AbstractStringField {
      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("Text");
      }

      @Override
      protected int getConfiguredMaxLength() {
        return IEntityTable.DESCRIPTION_LENGTH;
      }

      @Override
      protected int getConfiguredGridW() {
        return 2;
      }
    }

    @Order(100000)
    public class OkButton extends AbstractOkButton {
    }

    @Order(101000)
    public class CancelButton extends AbstractCancelButton {
    }

    @Order(102000)
    public class SaveAndNewWorkButton extends AbstractButton {
      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("SaveAndNewWork");
      }

      @Override
      protected void execClickAction() {
        if (getHandler() instanceof ModifyHandler) {
          saveModify();
        }
        else if (getHandler() instanceof NewHandler) {
          saveNew();
        }
        setEntityType(WorkCode.ID);
        getWorkHoursField().setValue(null);
        getExpenseAmountField().setValue(null);
        getTextField().setValue(null);
        loadNew();
      }
    }

    @Order(103000)
    public class SaveAndNewExpenseButton extends AbstractButton {
      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("SaveAndNewExpense");
      }

      @Override
      protected void execClickAction() {
        if (getHandler() instanceof ModifyHandler) {
          saveModify();
        }
        else if (getHandler() instanceof NewHandler) {
          saveNew();
        }
        setEntityType(ExpenseCode.ID);
        getWorkHoursField().setValue(null);
        getExpenseAmountField().setValue(null);
        getTextField().setValue(null);
        loadNew();
      }
    }

  }

  protected void loadNew() {
    IEntityService service = BEANS.get(IEntityService.class);
    EntityFormData formData = new EntityFormData();
    exportFormData(formData);
    formData = service.prepareCreate(formData);
    importFormData(formData);
    handleEntityCodeUpdated();
  }

  protected void saveNew() {
    IEntityService service = BEANS.get(IEntityService.class);
    EntityFormData formData = new EntityFormData();
    exportFormData(formData);
    service.create(formData);
    getDesktop().dataChanged(IWorkItemEntity.WORK_ITEM_KEY);
  }

  protected void saveModify() {
    IEntityService service = BEANS.get(IEntityService.class);
    EntityFormData formData = new EntityFormData();
    exportFormData(formData);
    service.store(formData);
    getDesktop().dataChanged(IWorkItemEntity.WORK_ITEM_KEY);
  }

  protected void handleEntityCodeUpdated() {
    boolean isWork = isWork();
    getWorkHoursField().setVisible(isWork);
    getWorkHoursField().setMandatory(isWork);
    getExpenseAmountField().setVisible(!isWork);
    getExpenseAmountField().setMandatory(!isWork);
    if (isWork) {
      setSubTitle(TEXTS.get("Work"));
      setIconId("font:icomoon \uf0ad");
    }
    else {
      setSubTitle(TEXTS.get("Expense"));
      setIconId("font:icomoon \ue900");
    }
  }

  public class ModifyHandler extends AbstractFormHandler {

    @Override
    protected void execLoad() {
      IEntityService service = BEANS.get(IEntityService.class);
      EntityFormData formData = new EntityFormData();
      exportFormData(formData);
      formData = service.load(formData);
      importFormData(formData);
      handleEntityCodeUpdated();
    }

    @Override
    protected void execStore() {
      saveModify();
    }
  }

  public class NewHandler extends AbstractFormHandler {

    @Override
    protected void execLoad() {
      loadNew();
    }

    @Override
    protected void execStore() {
      saveNew();
    }
  }

  public class ViewEntityHandler extends AbstractFormHandler {
    @Override
    protected void execLoad() {
      IEntityService service = BEANS.get(IEntityService.class);
      EntityFormData formData = new EntityFormData();
      exportFormData(formData);
      formData = service.load(formData);
      importFormData(formData);
      handleEntityCodeUpdated();
      setEnabledGranted(false);
    }

    @Override
    protected void execStore() {
    }
  }

}

package ch.ahoegger.docbox.client.hr.entity;

import java.math.BigDecimal;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.bigdecimalfield.AbstractBigDecimalField;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.datefield.AbstractDateField;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.util.ObjectUtility;
import org.eclipse.scout.rt.shared.TEXTS;

import ch.ahoegger.docbox.client.hr.entity.EntityForm.MainBox.CancelButton;
import ch.ahoegger.docbox.client.hr.entity.EntityForm.MainBox.EntityDateField;
import ch.ahoegger.docbox.client.hr.entity.EntityForm.MainBox.ExpenseAmountField;
import ch.ahoegger.docbox.client.hr.entity.EntityForm.MainBox.OkButton;
import ch.ahoegger.docbox.client.hr.entity.EntityForm.MainBox.TextField;
import ch.ahoegger.docbox.client.hr.entity.EntityForm.MainBox.WorkHoursField;
import ch.ahoegger.docbox.shared.hr.entity.EntityFormData;
import ch.ahoegger.docbox.shared.hr.entity.EntityTypeCodeType.ExpenseCode;
import ch.ahoegger.docbox.shared.hr.entity.EntityTypeCodeType.WorkCode;
import ch.ahoegger.docbox.shared.hr.entity.IEntityService;
import ch.ahoegger.docbox.shared.hr.entity.IEntityTable;

@FormData(value = EntityFormData.class, sdkCommand = FormData.SdkCommand.CREATE)
public class EntityForm extends AbstractForm {

  private BigDecimal m_entityId;
  private BigDecimal m_entityType;
  private BigDecimal m_partnerId;
  private BigDecimal m_postingGroupId;

  public EntityForm(BigDecimal partnerId) {
    m_partnerId = partnerId;
  }

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("Entity");
  }

  @Override
  protected void execInitForm() {
    if (isWork()) {
      setSubTitle(TEXTS.get("Work"));
    }
    else {
      setSubTitle(TEXTS.get("Expense"));
    }
  }

  @Override
  protected void execStored() {
    getDesktop().dataChanged(IWorkItemEntity.WORK_ITEM_KEY);
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
  public void setPostingGroupId(BigDecimal postingGroupId) {
    m_postingGroupId = postingGroupId;
  }

  @FormData
  public BigDecimal getPostingGroupId() {
    return m_postingGroupId;
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
        return IEntityTable.HOURS_MIN;
      }

      @Override
      protected BigDecimal getConfiguredMaxValue() {
        return IEntityTable.HOURS_MAX;
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
        return IEntityTable.AMOUNT_MIN;
      }

      @Override
      protected BigDecimal getConfiguredMaxValue() {
        return IEntityTable.AMOUNT_MAX;
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
  }

  protected void handleEntityCodeUpdated() {
    getExpenseAmountField().setVisible(ObjectUtility.equals(ExpenseCode.ID, getEntityType()));
    getWorkHoursField().setVisible(ObjectUtility.equals(WorkCode.ID, getEntityType()));
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
      IEntityService service = BEANS.get(IEntityService.class);
      EntityFormData formData = new EntityFormData();
      exportFormData(formData);
      service.store(formData);
    }
  }

  public class NewHandler extends AbstractFormHandler {

    @Override
    protected void execLoad() {
      IEntityService service = BEANS.get(IEntityService.class);
      EntityFormData formData = new EntityFormData();
      exportFormData(formData);
      formData = service.prepareCreate(formData);
      importFormData(formData);
      handleEntityCodeUpdated();
    }

    @Override
    protected void execStore() {
      IEntityService service = BEANS.get(IEntityService.class);
      EntityFormData formData = new EntityFormData();
      exportFormData(formData);
      service.create(formData);
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

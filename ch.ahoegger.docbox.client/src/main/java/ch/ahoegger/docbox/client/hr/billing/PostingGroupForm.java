package ch.ahoegger.docbox.client.hr.billing;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.dto.FormData.SdkCommand;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.IValueField;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.datefield.AbstractDateField;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.sequencebox.AbstractSequenceBox;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.status.IStatus;
import org.eclipse.scout.rt.platform.status.Status;
import org.eclipse.scout.rt.shared.TEXTS;

import ch.ahoegger.docbox.client.document.AbstractDocumentLinkField;
import ch.ahoegger.docbox.client.document.IDocumentEntity;
import ch.ahoegger.docbox.client.hr.billing.PostingGroupForm.MainBox.CancelButton;
import ch.ahoegger.docbox.client.hr.billing.PostingGroupForm.MainBox.DateField;
import ch.ahoegger.docbox.client.hr.billing.PostingGroupForm.MainBox.DocumentAbstractField;
import ch.ahoegger.docbox.client.hr.billing.PostingGroupForm.MainBox.DocumentLinkField;
import ch.ahoegger.docbox.client.hr.billing.PostingGroupForm.MainBox.EntityDateBox;
import ch.ahoegger.docbox.client.hr.billing.PostingGroupForm.MainBox.EntityDateBox.FromField;
import ch.ahoegger.docbox.client.hr.billing.PostingGroupForm.MainBox.EntityDateBox.ToField;
import ch.ahoegger.docbox.client.hr.billing.PostingGroupForm.MainBox.OkButton;
import ch.ahoegger.docbox.client.hr.billing.PostingGroupForm.MainBox.PartnerField;
import ch.ahoegger.docbox.client.hr.billing.PostingGroupForm.MainBox.PostingCalculationBox;
import ch.ahoegger.docbox.client.hr.billing.PostingGroupForm.MainBox.TitleField;
import ch.ahoegger.docbox.client.hr.entity.IWorkItemEntity;
import ch.ahoegger.docbox.client.partner.AbstractPartnerSmartField;
import ch.ahoegger.docbox.shared.document.IDocumentTable;
import ch.ahoegger.docbox.shared.hr.billing.IPostingGroupService;
import ch.ahoegger.docbox.shared.hr.billing.IPostingGroupTable;
import ch.ahoegger.docbox.shared.hr.billing.PostingCalculationBoxData;
import ch.ahoegger.docbox.shared.hr.billing.PostingGroupFormData;

@FormData(value = PostingGroupFormData.class, sdkCommand = FormData.SdkCommand.CREATE)
public class PostingGroupForm extends AbstractForm {

  private BigDecimal m_documentId;
  private BigDecimal m_postingGroupId;

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("PostingGroup");
  }

  @Override
  protected boolean getConfiguredAskIfNeedSave() {
    return false;
  }

  @Override
  protected void execStored() {
    getDesktop().dataChanged(IDocumentEntity.ENTITY_KEY, IWorkItemEntity.WORK_ITEM_KEY, IPostingGroupEntity.ENTITY_KEY);
  }

  public void startModify() {
    startInternalExclusive(new ModifyHandler());
  }

  public void startNew() {
    startInternal(new NewHandler());
  }

  @FormData
  public BigDecimal getDocumentId() {
    return m_documentId;
  }

  @FormData
  public void setDocumentId(BigDecimal documentId) {
    m_documentId = documentId;
  }

  @FormData
  public BigDecimal getPostingGroupId() {
    return m_postingGroupId;
  }

  @FormData
  public void setPostingGroupId(BigDecimal postingGroupId) {
    m_postingGroupId = postingGroupId;
  }

  private void recalculateWage() {
    if (getEntityDateFromField().getValue() == null || getEntityDateToField().getValue() == null) {
      return;
    }
    PostingGroupFormData formData = new PostingGroupFormData();
    PostingGroupForm.this.exportFormData(formData);
    PostingCalculationBoxData calculationData = BEANS.get(IPostingGroupService.class).calculateWage(formData);
    getWageBox().getBruttoWageField().importFormFieldData(calculationData.getBruttoWage(), false);
    getWageBox().getEntitiesField().importFormFieldData(calculationData.getEntities(), false);
    getWageBox().getNettoWageField().importFormFieldData(calculationData.getNettoWage(), false);
    getWageBox().getSocialSecurityTaxField().importFormFieldData(calculationData.getSocialSecurityTax(), false);
    getWageBox().getSourceTaxField().importFormFieldData(calculationData.getSourceTax(), false);
    getWageBox().getVacationExtraField().importFormFieldData(calculationData.getVacationExtra(), false);
    getWageBox().getWorkingHoursField().importFormFieldData(calculationData.getWorkingHours(), false);
    if (calculationData.getEntities().getRowCount() < 1) {
      getWageBox().addErrorStatus(new Status(TEXTS.get("Error_payslipNoEntities"), IStatus.ERROR));
    }
  }

  public CancelButton getCancelButton() {
    return getFieldByClass(CancelButton.class);
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  public TitleField getTitleField() {
    return getFieldByClass(TitleField.class);
  }

  public PartnerField getPartnerField() {
    return getFieldByClass(PartnerField.class);
  }

  public DocumentLinkField getDocumentLinkField() {
    return getFieldByClass(DocumentLinkField.class);
  }

  public DateField getDateField() {
    return getFieldByClass(DateField.class);
  }

  public PostingCalculationBox getWageBox() {
    return getFieldByClass(PostingCalculationBox.class);
  }

  public PostingCalculationBox getPostingCalculationBox() {
    return getFieldByClass(PostingCalculationBox.class);
  }

  public EntityDateBox getEntityDateBox() {
    return getFieldByClass(EntityDateBox.class);
  }

  public FromField getEntityDateFromField() {
    return getFieldByClass(FromField.class);
  }

  public ToField getEntityDateToField() {
    return getFieldByClass(ToField.class);
  }

  public DocumentAbstractField getDocumentAbstractField() {
    return getFieldByClass(DocumentAbstractField.class);
  }

  public OkButton getOkButton() {
    return getFieldByClass(OkButton.class);
  }

  @Order(1000)
  public class MainBox extends AbstractGroupBox {

    @Order(1000)
    public class PartnerField extends AbstractPartnerSmartField {

    }

    @Order(2000)
    public class DocumentLinkField extends AbstractDocumentLinkField {

    }

    @Order(3000)
    public class TitleField extends AbstractStringField {
      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("Title");
      }

      @Override
      protected int getConfiguredMaxLength() {
        return IPostingGroupTable.NAME_LENGTH;
      }
    }

    @Order(4000)
    public class DateField extends AbstractDateField {
      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("Date");
      }
    }

    @Order(4500)
    public class EntityDateBox extends AbstractSequenceBox {
      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("EntityData");
      }

      @Override
      protected boolean getConfiguredAutoCheckFromTo() {
        return true;
      }

      @Order(1000)
      public class FromField extends AbstractDateField {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("from");
        }
      }

      @Order(2000)
      public class ToField extends AbstractDateField {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("to");
        }
      }
    }

    @Order(4750)
    public class DocumentAbstractField extends AbstractStringField {
      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("Abstract");
      }

      @Override
      protected int getConfiguredMaxLength() {
        return IDocumentTable.ABSTRACT_LENGTH;
      }

      @Override
      protected int getConfiguredGridW() {
        return 2;
      }
    }

    @Order(5000)
    @FormData(sdkCommand = SdkCommand.IGNORE)
    public class PostingCalculationBox extends AbstractPostingCalculationBox {

      @Override
      protected boolean getConfiguredEnabled() {
        return false;
      }

      @Override
      protected void execInitField() {
        P_FromToProptertyListener listener = new P_FromToProptertyListener();
        getEntityDateFromField().addPropertyChangeListener(listener);
        getEntityDateToField().addPropertyChangeListener(listener);
        recalculateWage();
      }

      private class P_FromToProptertyListener implements PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
          if (IValueField.PROP_VALUE.equals(evt.getPropertyName())) {
            recalculateWage();
          }
        }
      }

    }

    @Order(100000)
    public class OkButton extends AbstractOkButton {
    }

    @Order(101000)
    public class CancelButton extends AbstractCancelButton {
    }
  }

  public class ModifyHandler extends AbstractFormHandler {

    @Override
    protected void execLoad() {
    }

    @Override
    protected void execStore() {
    }
  }

  public class NewHandler extends AbstractFormHandler {

    @Override
    protected void execLoad() {
      IPostingGroupService service = BEANS.get(IPostingGroupService.class);
      PostingGroupFormData formData = new PostingGroupFormData();
      exportFormData(formData);
      formData = service.prepareCreate(formData);
      importFormData(formData);
      recalculateWage();

    }

    @Override
    protected void execPostLoad() {
      touch();
    }

    @Override
    protected void execStore() {
      IPostingGroupService service = BEANS.get(IPostingGroupService.class);
      PostingGroupFormData formData = new PostingGroupFormData();
      exportFormData(formData);
      formData = service.create(formData);

      PostingGroupConfirmationForm confirmationForm = new PostingGroupConfirmationForm();
      confirmationForm.setDocumentId(formData.getDocumentId());
      confirmationForm.start();

    }
  }

  public class ViewHandler extends AbstractFormHandler {
    @Override
    protected void execLoad() {
      IPostingGroupService service = BEANS.get(IPostingGroupService.class);
      PostingGroupFormData formData = new PostingGroupFormData();
      exportFormData(formData);
      formData = service.load(formData);
      importFormData(formData);
    }

    @Override
    protected void execStore() {
    }
  }

}

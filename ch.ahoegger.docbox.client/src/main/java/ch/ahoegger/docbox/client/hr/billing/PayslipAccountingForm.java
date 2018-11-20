package ch.ahoegger.docbox.client.hr.billing;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;

import org.eclipse.scout.rt.client.dto.FormData;
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
import org.eclipse.scout.rt.platform.text.TEXTS;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;

import ch.ahoegger.docbox.client.administration.taxgroup.AbstractTaxGroupSmartField;
import ch.ahoegger.docbox.client.document.AbstractDocumentLinkField;
import ch.ahoegger.docbox.client.document.IDocumentEntity;
import ch.ahoegger.docbox.client.hr.billing.PayslipAccountingForm.MainBox.CancelButton;
import ch.ahoegger.docbox.client.hr.billing.PayslipAccountingForm.MainBox.DateField;
import ch.ahoegger.docbox.client.hr.billing.PayslipAccountingForm.MainBox.DocumentAbstractField;
import ch.ahoegger.docbox.client.hr.billing.PayslipAccountingForm.MainBox.DocumentLinkField;
import ch.ahoegger.docbox.client.hr.billing.PayslipAccountingForm.MainBox.EntityDateBox;
import ch.ahoegger.docbox.client.hr.billing.PayslipAccountingForm.MainBox.EntityDateBox.FromField;
import ch.ahoegger.docbox.client.hr.billing.PayslipAccountingForm.MainBox.EntityDateBox.ToField;
import ch.ahoegger.docbox.client.hr.billing.PayslipAccountingForm.MainBox.OkButton;
import ch.ahoegger.docbox.client.hr.billing.PayslipAccountingForm.MainBox.PartnerField;
import ch.ahoegger.docbox.client.hr.billing.PayslipAccountingForm.MainBox.PayslipCalculationBox;
import ch.ahoegger.docbox.client.hr.billing.PayslipAccountingForm.MainBox.TaxGroupField;
import ch.ahoegger.docbox.client.hr.billing.PayslipAccountingForm.MainBox.TitleField;
import ch.ahoegger.docbox.client.hr.entity.IWorkItemEntity;
import ch.ahoegger.docbox.client.partner.AbstractPartnerSmartField;
import ch.ahoegger.docbox.or.definition.table.IDocumentTable;
import ch.ahoegger.docbox.or.definition.table.IPayslipAccountingTable;
import ch.ahoegger.docbox.shared.administration.taxgroup.TaxGroupLookupCall;
import ch.ahoegger.docbox.shared.hr.billing.IPayslipAccountingService;
import ch.ahoegger.docbox.shared.hr.billing.PayslipAccountingFormData;

@FormData(value = PayslipAccountingFormData.class, sdkCommand = FormData.SdkCommand.CREATE)
public class PayslipAccountingForm extends AbstractForm {

  private BigDecimal m_documentId;
  private BigDecimal m_payslipAccountingId;

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("PayslipAccounting");
  }

  @Override
  protected boolean getConfiguredAskIfNeedSave() {
    return false;
  }

  @Override
  protected void execStored() {
    getDesktop().dataChanged(IDocumentEntity.ENTITY_KEY, IWorkItemEntity.WORK_ITEM_KEY, IPayslipAccountingEntity.ENTITY_KEY);
  }

  public void startNew() {
    startInternal(new NewHandler());
  }

  public void startModify() {
    startInternal(new ModifyHandler());
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
  public BigDecimal getPayslipAccountingId() {
    return m_payslipAccountingId;
  }

  @FormData
  public void setPayslipAccountingId(BigDecimal payslipAccountingId) {
    m_payslipAccountingId = payslipAccountingId;
  }

  private void calculateWage() {
    if (getEntityDateFromField().getValue() == null || getEntityDateToField().getValue() == null) {
      return;
    }
    getPayslipCalculationBox().clearErrorStatus();
    PayslipAccountingFormData fd = new PayslipAccountingFormData();
    exportFormData(fd);
    fd = BEANS.get(IPayslipAccountingService.class).calculateWage(fd);
    importFormData(fd);
    if (fd.getPayslipCalculationBox().getEntities().getRowCount() < 1) {
      getPayslipCalculationBox().addErrorStatus(new Status(TEXTS.get("Error_payslipNoEntities"), IStatus.ERROR));
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

  public PayslipCalculationBox getPayslipCalculationBox() {
    return getFieldByClass(PayslipCalculationBox.class);
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

  public TaxGroupField getTaxGroupField() {
    return getFieldByClass(TaxGroupField.class);
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
        return IPayslipAccountingTable.NAME_LENGTH;
      }
    }

    @Order(4000)
    public class DateField extends AbstractDateField {
      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("Date");
      }
    }

    @Order(4250)
    public class TaxGroupField extends AbstractTaxGroupSmartField {
      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("TaxGroup");
      }

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }

      @Override
      protected Class<? extends ILookupCall<BigDecimal>> getConfiguredLookupCall() {
        return TaxGroupLookupCall.class;
      }

      @Override
      protected void execPrepareBrowseTaxGroupLookup(TaxGroupLookupCall call) {
        call.setStartDate(getEntityDateFromField().getValue());
        call.setEndDate(getEntityDateToField().getValue());
        call.setPartnerId(getPartnerField().getValue());
        super.execPrepareBrowseLookup(call);
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
    public class PayslipCalculationBox extends AbstractPayslipAccountingCalculationBox {

      @Override
      protected boolean getConfiguredEnabled() {
        return false;
      }
    }

    @Order(100000)
    public class OkButton extends AbstractOkButton {
    }

    @Order(101000)
    public class CancelButton extends AbstractCancelButton {
    }
  }

  private class P_FromToProptertyListener implements PropertyChangeListener {
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
      if (IValueField.PROP_VALUE.equals(evt.getPropertyName())) {
        calculateWage();
      }
    }
  }

  public class NewHandler extends AbstractFormHandler {

    @Override
    protected void execLoad() {
      IPayslipAccountingService service = BEANS.get(IPayslipAccountingService.class);
      PayslipAccountingFormData formData = new PayslipAccountingFormData();
      exportFormData(formData);
      formData = service.prepareCreate(formData);
      importFormData(formData);
      P_FromToProptertyListener listener = new P_FromToProptertyListener();
      getEntityDateFromField().addPropertyChangeListener(listener);
      getEntityDateToField().addPropertyChangeListener(listener);

    }

    @Override
    protected void execPostLoad() {
      touch();
    }

    @Override
    protected void execStore() {
      IPayslipAccountingService service = BEANS.get(IPayslipAccountingService.class);
      PayslipAccountingFormData formData = new PayslipAccountingFormData();
      exportFormData(formData);
      formData = service.create(formData);

      PayslipAccountingConfirmationForm confirmationForm = new PayslipAccountingConfirmationForm();
      confirmationForm.setDocumentId(formData.getDocumentId());
      confirmationForm.start();

    }
  }

  public class ModifyHandler extends AbstractFormHandler {
    @Override
    protected void execLoad() {
      IPayslipAccountingService service = BEANS.get(IPayslipAccountingService.class);
      PayslipAccountingFormData formData = new PayslipAccountingFormData();
      exportFormData(formData);
      formData = service.load(formData);
      importFormData(formData);
      getDocumentLinkField().setDocumentId(formData.getDocumentId());

      getPartnerField().setEnabled(false);
      getDocumentLinkField().setVisible(true);
      getTitleField().setEnabled(false);
      getDateField().setEnabled(false);
      getEntityDateBox().setEnabled(false);
      getDocumentAbstractField().setEnabled(false);

    }

    @Override
    protected void execStore() {
      IPayslipAccountingService service = BEANS.get(IPayslipAccountingService.class);
      PayslipAccountingFormData formData = new PayslipAccountingFormData();
      exportFormData(formData);
      service.store(formData);
    }
  }

}

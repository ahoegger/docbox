package ch.ahoegger.docbox.client.hr.billing.payslip;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.dto.FormData.SdkCommand;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.IValueField;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.client.ui.form.fields.tablefield.AbstractTableField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.classid.ClassId;
import org.eclipse.scout.rt.platform.text.TEXTS;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;

import ch.ahoegger.docbox.client.administration.hr.billing.AbstractBillingCycleSmartField;
import ch.ahoegger.docbox.client.administration.hr.taxgroup.AbstractTaxGroupSmartField;
import ch.ahoegger.docbox.client.hr.billing.AbstractStatementBox;
import ch.ahoegger.docbox.client.hr.billing.payslip.PayslipForm.MainBox.BillingCycleField;
import ch.ahoegger.docbox.client.hr.billing.payslip.PayslipForm.MainBox.CancelButton;
import ch.ahoegger.docbox.client.hr.billing.payslip.PayslipForm.MainBox.EmployeeField;
import ch.ahoegger.docbox.client.hr.billing.payslip.PayslipForm.MainBox.OkButton;
import ch.ahoegger.docbox.client.hr.billing.payslip.PayslipForm.MainBox.PayslipDocumentAbstractField;
import ch.ahoegger.docbox.client.hr.billing.payslip.PayslipForm.MainBox.PeriodBox;
import ch.ahoegger.docbox.client.hr.billing.payslip.PayslipForm.MainBox.TaxGroupField;
import ch.ahoegger.docbox.client.hr.billing.payslip.PayslipForm.MainBox.TitleField;
import ch.ahoegger.docbox.client.hr.billing.payslip.PayslipForm.MainBox.WageBox;
import ch.ahoegger.docbox.client.hr.billing.payslip.PayslipForm.MainBox.WageBox.EntitiesField;
import ch.ahoegger.docbox.client.hr.employee.AbstractEmployeeSmartField;
import ch.ahoegger.docbox.client.hr.entity.AbstractEntityTable;
import ch.ahoegger.docbox.client.templates.AbstractPeriodBox;
import ch.ahoegger.docbox.or.definition.table.IDocumentTable;
import ch.ahoegger.docbox.shared.administration.hr.billing.BillingCycleLookupCall;
import ch.ahoegger.docbox.shared.administration.hr.billing.IBillingCycleService;
import ch.ahoegger.docbox.shared.administration.hr.billingcycle.BillingCycleFormData;
import ch.ahoegger.docbox.shared.administration.taxgroup.TaxGroupLookupCall;
import ch.ahoegger.docbox.shared.hr.billing.payslip.IPayslipService;
import ch.ahoegger.docbox.shared.hr.billing.payslip.PayslipFormData;
import ch.ahoegger.docbox.shared.util.LocalDateUtility;

/**
 * <h3>{@link PayslipForm}</h3>
 *
 * @author aho
 */
@FormData(value = PayslipFormData.class, sdkCommand = FormData.SdkCommand.CREATE)
public class PayslipForm extends AbstractForm {

  private BigDecimal m_payslipId;

  // from backend
  private BigDecimal m_employeeTaxGroupId;
  private BigDecimal m_statementId;

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("PaySlip");
  }

  @FormData
  public BigDecimal getPayslipId() {
    return m_payslipId;
  }

  @FormData
  public void setPayslipId(BigDecimal payslipId) {
    m_payslipId = payslipId;
  }

  @FormData
  public BigDecimal getEmployeeTaxGroupId() {
    return m_employeeTaxGroupId;
  }

  @FormData
  public void setEmployeeTaxGroupId(BigDecimal employeeTaxGroupId) {
    m_employeeTaxGroupId = employeeTaxGroupId;
  }

  @FormData
  public BigDecimal getStatementId() {
    return m_statementId;
  }

  @FormData
  public void setStatementId(BigDecimal statementId) {
    m_statementId = statementId;
  }

  public void startNew() {
    startInternal(new NewHandler());
  }

  public void startFinalize() {
    startInternal(new FinalizeHandler());
  }

  public WageBox getWageBox() {
    return getFieldByClass(WageBox.class);
  }

  public CancelButton getCancelButton() {
    return getFieldByClass(CancelButton.class);
  }

  public PayslipDocumentAbstractField getPayslipDocumentAbstractField() {
    return getFieldByClass(PayslipDocumentAbstractField.class);
  }

  public TitleField getTitleField() {
    return getFieldByClass(TitleField.class);
  }

  public EmployeeField getEmployeeField() {
    return getFieldByClass(EmployeeField.class);
  }

  public TaxGroupField getTaxGroupField() {
    return getFieldByClass(TaxGroupField.class);
  }

  public EntitiesField getEntitiesField() {
    return getFieldByClass(EntitiesField.class);
  }

  public OkButton getOkButton() {
    return getFieldByClass(OkButton.class);
  }

  public PeriodBox getPeriodBox() {
    return getFieldByClass(PeriodBox.class);
  }

  public BillingCycleField getBillingCycleField() {
    return getFieldByClass(BillingCycleField.class);
  }

  protected void updateBillingCycle() {
    if (getBillingCycleField().getValue() != null) {
      BillingCycleFormData billingCycleData = new BillingCycleFormData();
      billingCycleData.setBillingCycleId(getBillingCycleField().getValue());
      billingCycleData = BEANS.get(IBillingCycleService.class).load(billingCycleData);
      getPeriodBox().getFromField().setValue(billingCycleData.getPeriodBox().getFrom().getValue());
      getPeriodBox().getToField().setValue(billingCycleData.getPeriodBox().getTo().getValue());
    }
  }

  protected void updateTitleAbstract() {
    if (getPeriodBox().getFromField().getValue() != null) {
      DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM yyyy", LocalDateUtility.DE_CH);
      LocalDate start = LocalDateUtility.toLocalDate(getPeriodBox().getFromField().getValue());
      getPayslipDocumentAbstractField().setValue(TEXTS.get("PayslipTitle", start.format(monthFormatter)));
      getTitleField().setValue(start.format(monthFormatter));
    }
  }

  @ClassId("dac4b427-cbcf-4414-ad8e-449efec2dd3e")
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
    public class EmployeeField extends AbstractEmployeeSmartField {
    }

    @Order(2000)
    public class TaxGroupField extends AbstractTaxGroupSmartField {
      @Override
      protected Class<? extends IValueField> getConfiguredMasterField() {
        return EmployeeField.class;
      }

      @Override
      protected void execInitField() {
        super.execInitField();
        setEnabled(getMasterField().getValue() != null);
      }

      @Override
      protected void execChangedMasterValue(Object newMasterValue) {
        setValue(null);
        setEnabled(newMasterValue != null);
      }

      @Override
      protected void execPrepareLookup(ILookupCall<BigDecimal> call) {
        super.execPrepareLookup(call);
        ((TaxGroupLookupCall) call).setEmployeeId(getEmployeeField().getValue());
      }
    }

    @Order(3000)
    public class BillingCycleField extends AbstractBillingCycleSmartField {
      @Override
      protected Class<? extends IValueField> getConfiguredMasterField() {
        return TaxGroupField.class;
      }

      @Override
      protected void execInitField() {
        super.execInitField();
        setEnabled(getMasterField().getValue() != null);
      }

      @Override
      protected void execChangedValue() {
        updateBillingCycle();
      }

      @Override
      protected void execChangedMasterValue(Object newMasterValue) {
        setValue(null);
        setEnabled(newMasterValue != null);
      }

      @Override
      protected void execPrepareLookup(ILookupCall<BigDecimal> callRaw) {
        BillingCycleLookupCall call = (BillingCycleLookupCall) callRaw;
        if (getTaxGroupField().getValue() != null) {
          call.setTaxGroupId(getTaxGroupField().getValue());

        }
        if (getHandler() instanceof NewHandler) {
          call.setNotEmployeeId(getEmployeeField().getValue());
        }
      }
    }

    @Order(4000)
    @FormData(sdkCommand = SdkCommand.IGNORE)
    public class PeriodBox extends AbstractPeriodBox {
      @Override
      protected boolean getConfiguredEnabled() {
        return false;
      }

      @Override
      protected void execFromChangedValue(Date value) {
        updateTitleAbstract();
      }

      @Override
      protected void execToChangedValue(Date value) {
        updateTitleAbstract();
      }

    }

    @Order(5000)
    public class TitleField extends AbstractStringField {
      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("Title");
      }

      @Override
      protected int getConfiguredMaxLength() {
        return 128;
      }
    }

    @Order(6000)
    public class PayslipDocumentAbstractField extends AbstractStringField {
      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("Abstract");
      }

      @Override
      protected int getConfiguredMaxLength() {
        return IDocumentTable.ABSTRACT_LENGTH;
      }
    }

    @Order(7000)
    public class WageBox extends AbstractStatementBox {
      @Override
      protected boolean getConfiguredEnabled() {
        return false;
      }

      @Order(1000)
      public class EntitiesField extends AbstractTableField<EntitiesField.Table> {

        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("Entities");
        }

        @Override
        protected byte getConfiguredLabelPosition() {
          return LABEL_POSITION_TOP;
        }

        @Override
        protected int getConfiguredGridW() {
          return 2;
        }

        @Override
        protected int getConfiguredGridH() {
          return 6;
        }

        public class Table extends AbstractEntityTable {

        }
      }

    }

    @Order(10000)
    public class OkButton extends AbstractOkButton {
    }

    @Order(11000)
    public class CancelButton extends AbstractCancelButton {
    }
  }

  public class NewHandler extends AbstractFormHandler {
    @Override
    protected void execLoad() {
      PayslipFormData formData = new PayslipFormData();
      exportFormData(formData);
      formData = BEANS.get(IPayslipService.class).prepareCreate(formData);
      importFormData(formData);
      getEmployeeField().setEnabled(formData.getEmployee().getValue() == null);
      getTaxGroupField().setEnabled(formData.getTaxGroup().getValue() == null);
      getWageBox().setVisible(false);

      updateBillingCycle();
      updateTitleAbstract();
    }

    @Override
    protected void execStore() {
      PayslipFormData formData = new PayslipFormData();
      exportFormData(formData);
      formData = BEANS.get(IPayslipService.class).create(formData);
      importFormData(formData);
      getDesktop().dataChanged(IPayslipEntity.ENTITY_KEY);
    }
  }

  public class FinalizeHandler extends AbstractFormHandler {

    @Override
    protected void execLoad() {
      PayslipFormData formData = new PayslipFormData();
      exportFormData(formData);
      formData = BEANS.get(IPayslipService.class).load(formData);
      importFormData(formData);
      // fields
      getEmployeeField().setEnabled(false);
      getTaxGroupField().setEnabled(false);
      getBillingCycleField().setEnabled(false);
      getOkButton().setLabel(TEXTS.get("Finalize"));
      getWageBox().setVisible(true);

      updateTitleAbstract();
      updateBillingCycle();
    }

    @Override
    protected void execPostLoad() {
      touch();
    }

    @Override
    protected void execStore() {
      PayslipFormData formData = new PayslipFormData();
      exportFormData(formData);
      formData = BEANS.get(IPayslipService.class).finalize(formData);
      importFormData(formData);

      getDesktop().dataChanged(IPayslipEntity.ENTITY_KEY_FINALIZE);
      if (formData.getStatementId() != null) {
        PayslipConfirmationForm confirmationForm = new PayslipConfirmationForm();
        confirmationForm.setPayslipId(formData.getPayslipId());
        confirmationForm.start();
      }
    }
  }

}

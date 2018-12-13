package ch.ahoegger.docbox.client.hr.billing.payslip;

import java.math.BigDecimal;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.dto.FormData.SdkCommand;
import org.eclipse.scout.rt.client.ui.action.keystroke.AbstractKeyStroke;
import org.eclipse.scout.rt.client.ui.action.keystroke.IKeyStroke;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractSearchForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.IValueField;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractResetButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractSearchButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.classid.ClassId;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;

import ch.ahoegger.docbox.client.administration.hr.billing.AbstractBillingCycleSmartField;
import ch.ahoegger.docbox.client.administration.hr.taxgroup.AbstractTaxGroupSmartField;
import ch.ahoegger.docbox.client.hr.billing.payslip.PayslipSearchForm.MainBox.BillingCycleField;
import ch.ahoegger.docbox.client.hr.billing.payslip.PayslipSearchForm.MainBox.EmployeeField;
import ch.ahoegger.docbox.client.hr.billing.payslip.PayslipSearchForm.MainBox.EmployerField;
import ch.ahoegger.docbox.client.hr.billing.payslip.PayslipSearchForm.MainBox.FinalzedRadioGroup;
import ch.ahoegger.docbox.client.hr.billing.payslip.PayslipSearchForm.MainBox.ResetButton;
import ch.ahoegger.docbox.client.hr.billing.payslip.PayslipSearchForm.MainBox.SearchButton;
import ch.ahoegger.docbox.client.hr.billing.payslip.PayslipSearchForm.MainBox.TaxGroupField;
import ch.ahoegger.docbox.client.hr.employee.AbstractEmployeeSmartField;
import ch.ahoegger.docbox.client.hr.employer.AbstractEmployerSmartField;
import ch.ahoegger.docbox.client.templates.AbstractFinalizedRadioGroup;
import ch.ahoegger.docbox.shared.administration.hr.billing.BillingCycleLookupCall;
import ch.ahoegger.docbox.shared.hr.billing.payslip.IPayslipService;
import ch.ahoegger.docbox.shared.hr.billing.payslip.PayslipSearchFormData;

/**
 * <h3>{@link PayslipSearchForm}</h3>
 *
 * @author aho
 */
@FormData(value = PayslipSearchFormData.class, sdkCommand = SdkCommand.CREATE)
public class PayslipSearchForm extends AbstractSearchForm {
  private BigDecimal m_employeeTaxGroupId;

  public PayslipSearchForm() {
    setHandler(new PayslipSearchForm.SearchHandler(this));
  }

  @FormData
  public BigDecimal getEmployeeTaxGroupId() {
    return m_employeeTaxGroupId;
  }

  @FormData
  public void setEmployeeTaxGroupId(BigDecimal employeeTaxGroupId) {
    m_employeeTaxGroupId = employeeTaxGroupId;
  }

  public TaxGroupField getTaxGroupField() {
    return getFieldByClass(TaxGroupField.class);
  }

  public EmployerField getEmployerField() {
    return getFieldByClass(EmployerField.class);
  }

  public ResetButton getResetButton() {
    return getFieldByClass(ResetButton.class);
  }

  public SearchButton getSearchButton() {
    return getFieldByClass(SearchButton.class);
  }

  public FinalzedRadioGroup getFinalzedRadioGroup() {
    return getFieldByClass(FinalzedRadioGroup.class);
  }

  public EmployeeField getEmployeeField() {
    return getFieldByClass(EmployeeField.class);
  }

  public BillingCycleField getBillingCycleField() {
    return getFieldByClass(BillingCycleField.class);
  }

  @ClassId("63cea6f5-6750-4b08-8cc3-901a15f5b1be")
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
    public class EmployerField extends AbstractEmployerSmartField {
    }

    @Order(2000)
    public class EmployeeField extends AbstractEmployeeSmartField {
    }

    @Order(3000)
    public class TaxGroupField extends AbstractTaxGroupSmartField {
    }

    @Order(4000)
    public class BillingCycleField extends AbstractBillingCycleSmartField {

      @Override
      protected Class<? extends IValueField> getConfiguredMasterField() {
        return TaxGroupField.class;
      }

      @Override
      protected void execChangedMasterValue(Object newMasterValue) {
        setValue(null);
      }

      @Override
      protected void execPrepareBrowseLookup(ILookupCall<BigDecimal> call) {
        super.execPrepareBrowseLookup(call);
        ((BillingCycleLookupCall) call).setTaxGroupId(getTaxGroupField().getValue());
      }
    }

    @Order(5000)
    public class FinalzedRadioGroup extends AbstractFinalizedRadioGroup {
    }

    @Order(6000)
    public class SearchButton extends AbstractSearchButton {
    }

    @Order(7000)
    public class ResetButton extends AbstractResetButton {
    }

    public class EnterKeyStroke extends AbstractKeyStroke {

      @Override
      protected String getConfiguredKeyStroke() {
        return IKeyStroke.ENTER;
      }

      @Override
      protected void execAction() {
        getSearchButton().doClick();
      }
    }

  }

  public static class SearchHandler extends AbstractFormHandler {
    private PayslipSearchForm m_form;

    SearchHandler(PayslipSearchForm form) {
      m_form = form;
    }

    @Override
    protected void execLoad() {
      PayslipSearchFormData searchData = new PayslipSearchFormData();
      m_form.exportFormData(searchData);
      searchData = BEANS.get(IPayslipService.class).loadSearch(searchData);
      m_form.importFormData(searchData);
      m_form.getEmployeeField().setEnabled(searchData.getEmployee().getValue() == null);
      m_form.getTaxGroupField().setEnabled(searchData.getTaxGroup().getValue() == null);
    }

    @Override
    protected void execStore() {
    }
  }

}

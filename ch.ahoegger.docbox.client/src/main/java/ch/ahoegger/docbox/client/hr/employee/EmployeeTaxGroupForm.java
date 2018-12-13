package ch.ahoegger.docbox.client.hr.employee;

import java.math.BigDecimal;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.placeholder.AbstractPlaceholderField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.classid.ClassId;
import org.eclipse.scout.rt.platform.status.IStatus;
import org.eclipse.scout.rt.platform.text.TEXTS;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;

import ch.ahoegger.docbox.client.administration.hr.taxgroup.AbstractTaxGroupSmartField;
import ch.ahoegger.docbox.client.hr.billing.AbstractStatementBox;
import ch.ahoegger.docbox.client.hr.employee.EmployeeTaxGroupForm.MainBox.CancelButton;
import ch.ahoegger.docbox.client.hr.employee.EmployeeTaxGroupForm.MainBox.MinPeriodBox;
import ch.ahoegger.docbox.client.hr.employee.EmployeeTaxGroupForm.MainBox.OkButton;
import ch.ahoegger.docbox.client.hr.employee.EmployeeTaxGroupForm.MainBox.PeriodBox;
import ch.ahoegger.docbox.client.hr.employee.EmployeeTaxGroupForm.MainBox.PlaceHolderField;
import ch.ahoegger.docbox.client.hr.employee.EmployeeTaxGroupForm.MainBox.StatusField;
import ch.ahoegger.docbox.client.hr.employee.EmployeeTaxGroupForm.MainBox.TaxGroupField;
import ch.ahoegger.docbox.client.hr.employee.EmployeeTaxGroupForm.MainBox.WageBox;
import ch.ahoegger.docbox.client.templates.AbstractPeriodBox;
import ch.ahoegger.docbox.client.templates.AbstractStatusField;
import ch.ahoegger.docbox.client.util.AbstractValidatableForm;
import ch.ahoegger.docbox.shared.administration.hr.taxgroup.TaxGroupFormData;
import ch.ahoegger.docbox.shared.administration.taxgroup.ITaxGroupService;
import ch.ahoegger.docbox.shared.administration.taxgroup.TaxGroupLookupCall;
import ch.ahoegger.docbox.shared.hr.billing.taxgroup.IEmployeeTaxGroupService;
import ch.ahoegger.docbox.shared.hr.employee.EmployeeTaxGroupFormData;

/**
 * <h3>{@link EmployeeTaxGroupForm}</h3>
 *
 * @author aho
 */
@FormData(value = EmployeeTaxGroupFormData.class, sdkCommand = FormData.SdkCommand.CREATE)
public class EmployeeTaxGroupForm extends AbstractValidatableForm {

  private BigDecimal m_employeeTaxGroupId;
  private BigDecimal m_employeeId;

  //
  private BigDecimal m_employerTaxGroupId;
  private BigDecimal m_statementId;

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("TaxGroup");
  }

  @Override
  protected void execStored() {
    getDesktop().dataChanged(IEmployeeTaxGroupEntity.ENTITY_KEY);
  }

  public void startNew() {
    startInternal(new NewHandler());
  }

  public void startView() {
    startInternal(new ViewHandler());
  }

  public void startModify() {
    startInternal(new ModifyHandler());
  }

  public void startFinalize() {
    startInternal(new FinalizeHandler());
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
  public BigDecimal getEmployeeId() {
    return m_employeeId;
  }

  @FormData
  public void setEmployeeId(BigDecimal employeeId) {
    m_employeeId = employeeId;
  }

  @FormData
  public BigDecimal getEmployerTaxGroupId() {
    return m_employerTaxGroupId;
  }

  @FormData
  public void setEmployerTaxGroupId(BigDecimal employerTaxGroupId) {
    m_employerTaxGroupId = employerTaxGroupId;
  }

  @FormData
  public void setStatementId(BigDecimal statementId) {
    m_statementId = statementId;
  }

  @FormData
  public BigDecimal getStatementId() {
    return m_statementId;
  }

  public CancelButton getCancelButton() {
    return getFieldByClass(CancelButton.class);
  }

  public PlaceHolderField getPlaceHolderField() {
    return getFieldByClass(PlaceHolderField.class);
  }

  public PeriodBox getPeriodBox() {
    return getFieldByClass(PeriodBox.class);
  }

  @Override
  public StatusField getStatusField() {
    return getFieldByClass(StatusField.class);
  }

  public MinPeriodBox getMinPeriodBox() {
    return getFieldByClass(MinPeriodBox.class);
  }

  public WageBox getWageBox() {
    return getFieldByClass(WageBox.class);
  }

  public OkButton getOkButton() {
    return getFieldByClass(OkButton.class);
  }

  public TaxGroupField getTaxGroupField() {
    return getFieldByClass(TaxGroupField.class);
  }

  @ClassId("27e133a4-547d-4e92-8d64-e3f5ba17d7d8")
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
    public class StatusField extends AbstractStatusField {
    }

    @Order(2000)
    public class TaxGroupField extends AbstractTaxGroupSmartField {

      @Override
      protected void execChangedValue() {
        if (getValue() != null) {
          TaxGroupFormData taxGroupData = new TaxGroupFormData();
          taxGroupData.setTaxGroupId(getValue());
          taxGroupData = BEANS.get(ITaxGroupService.class).load(taxGroupData);

          getPeriodBox().getFromField().setValue(taxGroupData.getPeriodBox().getFrom().getValue());
          getPeriodBox().getToField().setValue(taxGroupData.getPeriodBox().getTo().getValue());
        }
      }

      @Override
      protected void execPrepareLookup(ILookupCall<BigDecimal> call) {
        super.execPrepareLookup(call);
        // if new
        if (getHandler() instanceof NewHandler) {
          ((TaxGroupLookupCall) call).setNotEmployeeId(getEmployeeId());
        }
      }

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }
    }

    @Order(3000)
    public class PlaceHolderField extends AbstractPlaceholderField {
    }

    @Order(4000)
    public class PeriodBox extends AbstractPeriodBox {
      @Override
      protected boolean getConfiguredMandatoryFrom() {
        return true;
      }

      @Override
      protected boolean getConfiguredMandatoryTo() {
        return true;
      }

    }

    @Order(5000)
    public class MinPeriodBox extends AbstractPeriodBox {
      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("PayslipMinPeriod");
      }

      @Override
      protected boolean getConfiguredEnabled() {
        return false;
      }
    }

    @Order(6000)
    public class WageBox extends AbstractStatementBox {
      @Override
      protected boolean getConfiguredEnabled() {
        return false;
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
      EmployeeTaxGroupFormData formData = new EmployeeTaxGroupFormData();
      exportFormData(formData);
      formData = BEANS.get(IEmployeeTaxGroupService.class).prepareCreate(formData);
      importFormData(formData);

      // fields
      getWageBox().setVisible(false);
      getMinPeriodBox().setVisible(false);
      getTaxGroupField().setEnabled(formData.getTaxGroup().getValue() == null);
    }

    @Override
    protected void execPostLoad() {
      touch();
    }

    @Override
    protected void execStore() {
      EmployeeTaxGroupFormData formData = new EmployeeTaxGroupFormData();
      exportFormData(formData);
      formData = BEANS.get(IEmployeeTaxGroupService.class).create(formData);
      importFormData(formData);
    }
  }

  public class ViewHandler extends AbstractFormHandler {
    @Override
    protected void execLoad() {
      EmployeeTaxGroupFormData formData = new EmployeeTaxGroupFormData();
      exportFormData(formData);
      formData = BEANS.get(IEmployeeTaxGroupService.class).load(formData);
      importFormData(formData);
      setEnabledGranted(false);

    }

    @Override
    protected void execStore() {
    }
  }

  public class ModifyHandler extends AbstractFormHandler {
    @Override
    protected void execLoad() {
      EmployeeTaxGroupFormData formData = new EmployeeTaxGroupFormData();
      exportFormData(formData);
      formData = BEANS.get(IEmployeeTaxGroupService.class).load(formData);
      importFormData(formData);
      getTaxGroupField().setEnabled(false);
    }

    @Override
    protected boolean execValidate() {
      EmployeeTaxGroupFormData formData = new EmployeeTaxGroupFormData();
      exportFormData(formData);
      IStatus status = BEANS.get(IEmployeeTaxGroupService.class).validate(formData);
      getStatusField().setValidationStatus(status);
      return status.getSeverity() != IStatus.ERROR;
    }

    @Override
    protected void execStore() {
      EmployeeTaxGroupFormData formData = new EmployeeTaxGroupFormData();
      exportFormData(formData);
      formData = BEANS.get(IEmployeeTaxGroupService.class).store(formData);
      importFormData(formData);
    }
  }

  public class FinalizeHandler extends AbstractFormHandler {
    @Override
    protected void execLoad() {
      EmployeeTaxGroupFormData formData = new EmployeeTaxGroupFormData();
      exportFormData(formData);
      formData = BEANS.get(IEmployeeTaxGroupService.class).load(formData);
      importFormData(formData);
    }

    @Override
    protected void execPostLoad() {
      touch();
    }

    @Override
    protected boolean execValidate() {
      EmployeeTaxGroupFormData formData = new EmployeeTaxGroupFormData();
      exportFormData(formData);
      IStatus status = BEANS.get(IEmployeeTaxGroupService.class).validate(formData);
      getStatusField().setValidationStatus(status);
      return status.getSeverity() != IStatus.ERROR;
    }

    @Override
    protected void execStore() {
      EmployeeTaxGroupFormData formData = new EmployeeTaxGroupFormData();
      exportFormData(formData);
      formData = BEANS.get(IEmployeeTaxGroupService.class).finalize(formData);
      importFormData(formData);
    }
  }

}

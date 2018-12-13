package ch.ahoegger.docbox.client.hr.employer;

import java.math.BigDecimal;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.dto.FormData.SdkCommand;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.integerfield.AbstractIntegerField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.classid.ClassId;
import org.eclipse.scout.rt.platform.text.TEXTS;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;

import ch.ahoegger.docbox.client.administration.hr.taxgroup.AbstractTaxGroupSmartField;
import ch.ahoegger.docbox.client.hr.billing.AbstractStatementBox;
import ch.ahoegger.docbox.client.hr.employer.EmployerTaxGroupForm.MainBox.CalculationBox;
import ch.ahoegger.docbox.client.hr.employer.EmployerTaxGroupForm.MainBox.CancelButton;
import ch.ahoegger.docbox.client.hr.employer.EmployerTaxGroupForm.MainBox.EmployeeTaxGroupCountField;
import ch.ahoegger.docbox.client.hr.employer.EmployerTaxGroupForm.MainBox.OkButton;
import ch.ahoegger.docbox.client.hr.employer.EmployerTaxGroupForm.MainBox.TaxGroupField;
import ch.ahoegger.docbox.shared.administration.taxgroup.TaxGroupLookupCall;
import ch.ahoegger.docbox.shared.hr.employer.EmployerTaxGroupFormData;
import ch.ahoegger.docbox.shared.hr.employer.IEmployerTaxGroupService;

/**
 * <h3>{@link EmployerTaxGroupForm}</h3>
 *
 * @author aho
 */
@ClassId("50adf4c7-d2a9-48ca-8de6-1eddd8b753d4")
@FormData(value = EmployerTaxGroupFormData.class, sdkCommand = SdkCommand.CREATE)
public class EmployerTaxGroupForm extends AbstractForm {
  private BigDecimal m_employerTaxGroupId;
  private BigDecimal m_employerId;
  private BigDecimal m_statementId;

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("LinkTaxGroup");
  }

  public void startNew() {
    startInternal(new EmployerTaxGroupForm.NewHandler());
  }

  public void startFinalize() {
    startInternal(new EmployerTaxGroupForm.FinalizeHandler());
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
  public BigDecimal getEmployerId() {
    return m_employerId;
  }

  @FormData
  public void setEmployerId(BigDecimal employerId) {
    m_employerId = employerId;
  }

  @FormData
  public BigDecimal getStatementId() {
    return m_statementId;
  }

  @FormData
  public void setStatementId(BigDecimal statementId) {
    m_statementId = statementId;
  }

  public CalculationBox getCalculationBox() {
    return getFieldByClass(CalculationBox.class);
  }

  public CancelButton getCancelButton() {
    return getFieldByClass(CancelButton.class);
  }

  public EmployeeTaxGroupCountField getEmployeeTaxGroupCountField() {
    return getFieldByClass(EmployeeTaxGroupCountField.class);
  }

  public OkButton getOkButton() {
    return getFieldByClass(OkButton.class);
  }

  public TaxGroupField getTaxGroupField() {
    return getFieldByClass(TaxGroupField.class);
  }

  @ClassId("edefb480-f750-4ed7-93da-9cacca996377")
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
    public class TaxGroupField extends AbstractTaxGroupSmartField {
      @Override
      protected void execPrepareLookup(ILookupCall<BigDecimal> call) {
        if (getHandler() instanceof NewHandler) {
          ((TaxGroupLookupCall) call).setNotEmployerId(getEmployerId());
        }
      }
    }

    @Order(2000)
    public class EmployeeTaxGroupCountField extends AbstractIntegerField {
      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("StatementCount");
      }

      @Override
      protected void execInitField() {
        setValue(0);
      }

      @Override
      protected boolean getConfiguredEnabled() {
        return false;
      }
    }

    @Order(4000)
    public class CalculationBox extends AbstractStatementBox {
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
      EmployerTaxGroupFormData formData = new EmployerTaxGroupFormData();
      exportFormData(formData);
      formData = BEANS.get(IEmployerTaxGroupService.class).prepareCreate(formData);
      importFormData(formData);
      // fields
      getCalculationBox().setVisible(false);
    }

    @Override
    protected void execPostLoad() {
      if (getTaxGroupField().getValue() != null) {
        touch();
      }
    }

    @Override
    protected void execStore() {
      EmployerTaxGroupFormData formData = new EmployerTaxGroupFormData();
      exportFormData(formData);
      formData = BEANS.get(IEmployerTaxGroupService.class).create(formData);
      importFormData(formData);
      getDesktop().dataChanged(IEmployerEntity.ENTITY_KEY);
    }
  }

  public class FinalizeHandler extends AbstractFormHandler {
    @Override
    protected void execLoad() {
      EmployerTaxGroupFormData formData = new EmployerTaxGroupFormData();
      exportFormData(formData);
      formData = BEANS.get(IEmployerTaxGroupService.class).load(formData);
      importFormData(formData);
      // fields
      getTaxGroupField().setEnabled(getEmployeeTaxGroupCountField().getValue() > 0);
    }

    @Override
    protected void execPostLoad() {

      touch();
    }

    @Override
    protected void execStore() {
      EmployerTaxGroupFormData formData = new EmployerTaxGroupFormData();
      exportFormData(formData);
      formData = BEANS.get(IEmployerTaxGroupService.class).finalize(formData);
      importFormData(formData);
      getDesktop().dataChanged(IEmployerEntity.ENTITY_KEY);
    }
  }

}

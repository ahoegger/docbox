package ch.ahoegger.docbox.client.hr.tax;

import java.math.BigDecimal;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.classid.ClassId;
import org.eclipse.scout.rt.platform.text.TEXTS;

import ch.ahoegger.docbox.client.administration.taxgroup.AbstractTaxGroupSmartField;
import ch.ahoegger.docbox.client.hr.tax.EmployeeTaxGroupForm.MainBox.CancelButton;
import ch.ahoegger.docbox.client.hr.tax.EmployeeTaxGroupForm.MainBox.OKButton;
import ch.ahoegger.docbox.client.hr.tax.EmployeeTaxGroupForm.MainBox.TaxGroupField;
import ch.ahoegger.docbox.shared.hr.employee.IEmployeeTaxGroupService;
import ch.ahoegger.docbox.shared.hr.tax.EmployeeTaxGroupFormData;

/**
 * <h3>{@link EmployeeTaxGroupForm}</h3>
 *
 * @author aho
 */
@FormData(value = EmployeeTaxGroupFormData.class, sdkCommand = FormData.SdkCommand.CREATE)
public class EmployeeTaxGroupForm extends AbstractForm {
  private BigDecimal m_partnerId;

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("TaxGroup");
  }

  public TaxGroupField getTaxGroupField() {
    return getFieldByClass(TaxGroupField.class);
  }

  public CancelButton getCancelButton() {
    return getFieldByClass(CancelButton.class);
  }

  public OKButton getOKButton() {
    return getFieldByClass(OKButton.class);
  }

  @FormData
  public void setPartnerId(BigDecimal partnerId) {
    m_partnerId = partnerId;
  }

  @FormData
  public BigDecimal getPartnerId() {
    return m_partnerId;
  }

  public void startNew() {
    startInternal(new NewHandler());
  }

  @Order(1000)
  @ClassId("e150edd0-8ae2-4744-a000-0a440b02a6d8")
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
    @ClassId("deb10e43-62be-4f54-90c4-e664f76bb111")
    public class TaxGroupField extends AbstractTaxGroupSmartField {

    }

    @Order(10000)
    @ClassId("fd15008a-b85b-4d81-a1d0-8b69db976d3b")
    public class OKButton extends AbstractOkButton {
    }

    @Order(11000)
    @ClassId("2bc22498-9028-4d4b-ac15-0f446d3d0dbc")
    public class CancelButton extends AbstractCancelButton {
    }

  }

  public class NewHandler extends AbstractFormHandler {
    @Override
    protected void execLoad() {
    }

    @Override
    protected void execStore() {
      EmployeeTaxGroupFormData formData = new EmployeeTaxGroupFormData();
      exportFormData(formData);
      BEANS.get(IEmployeeTaxGroupService.class).create(formData);
      getDesktop().dataChanged(IEmployeeTaxGroupEntity.EMPLOYEE_TAX_GROUP_ITEM);
    }
  }

}

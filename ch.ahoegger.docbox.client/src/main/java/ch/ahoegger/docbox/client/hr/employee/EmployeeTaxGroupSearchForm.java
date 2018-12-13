package ch.ahoegger.docbox.client.hr.employee;

import java.math.BigDecimal;
import java.util.Set;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.dto.FormData.SdkCommand;
import org.eclipse.scout.rt.client.ui.action.keystroke.AbstractKeyStroke;
import org.eclipse.scout.rt.client.ui.action.keystroke.IKeyStroke;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractSearchForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractResetButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractSearchButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.classid.ClassId;

import ch.ahoegger.docbox.client.administration.hr.taxgroup.AbstractTaxGroupSmartField;
import ch.ahoegger.docbox.client.hr.employee.EmployeeTaxGroupSearchForm.MainBox.EmployeeField;
import ch.ahoegger.docbox.client.hr.employee.EmployeeTaxGroupSearchForm.MainBox.FinalizedRadioGroup;
import ch.ahoegger.docbox.client.hr.employee.EmployeeTaxGroupSearchForm.MainBox.ResetButton;
import ch.ahoegger.docbox.client.hr.employee.EmployeeTaxGroupSearchForm.MainBox.SearchButton;
import ch.ahoegger.docbox.client.hr.employee.EmployeeTaxGroupSearchForm.MainBox.TaxGroupField;
import ch.ahoegger.docbox.client.templates.AbstractFinalizedRadioGroup;
import ch.ahoegger.docbox.shared.hr.employee.EmployeeTaxGroupSearchFormData;

/**
 * <h3>{@link EmployeeTaxGroupSearchForm}</h3>
 *
 * @author aho
 */
@FormData(value = EmployeeTaxGroupSearchFormData.class, sdkCommand = SdkCommand.CREATE)
public class EmployeeTaxGroupSearchForm extends AbstractSearchForm {

  private BigDecimal m_employerTaxGroupId;
  private Set<BigDecimal> m_employeeIds;

  public EmployeeTaxGroupSearchForm() {
    setHandler(new EmployeeTaxGroupSearchForm.SearchHandler(this));
  }

  public TaxGroupField getTaxGroupField() {
    return getFieldByClass(TaxGroupField.class);
  }

  public ResetButton getResetButton() {
    return getFieldByClass(ResetButton.class);
  }

  public SearchButton getSearchButton() {
    return getFieldByClass(SearchButton.class);
  }

  public FinalizedRadioGroup getFinalizedBox() {
    return getFieldByClass(FinalizedRadioGroup.class);
  }

  public EmployeeField getEmployeeField() {
    return getFieldByClass(EmployeeField.class);
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
  public Set<BigDecimal> getEmployeeIds() {
    return m_employeeIds;
  }

  @FormData
  public void setEmployeeIds(Set<BigDecimal> employeeIds) {
    m_employeeIds = employeeIds;
  }

  @Order(1000)
  @ClassId("ca6143a0-46ce-44ab-86e3-02a82010d64c")
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
    }

    @Order(2000)
    public class EmployeeField extends AbstractEmployeeSmartField {
    }

    @Order(3000)
    public class FinalizedRadioGroup extends AbstractFinalizedRadioGroup {
    }

    @Order(10000)
    public class SearchButton extends AbstractSearchButton {
    }

    @Order(11000)
    public class ResetButton extends AbstractResetButton {
    }

    /**
     * Useful inside a wizard (starts search instead of clicking "next").
     */
    // TODO check if needed
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
    private EmployeeTaxGroupSearchForm m_form;

    /**
     * @param form
     */
    public SearchHandler(EmployeeTaxGroupSearchForm form) {
      m_form = form;
    }

    @Override
    protected void execLoad() {
//      LocalDate date = LocalDate.now().withDayOfMonth(1).minusMonths(1).minusYears(1);
//      m_form.getStartDateFromField().setValue(LocalDateUtility.toDate(date));
    }

    @Override
    protected void execStore() {
    }
  }
}

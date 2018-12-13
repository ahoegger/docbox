package ch.ahoegger.docbox.client.hr.employer;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.dto.FormData.SdkCommand;
import org.eclipse.scout.rt.client.ui.action.keystroke.AbstractKeyStroke;
import org.eclipse.scout.rt.client.ui.action.keystroke.IKeyStroke;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractSearchForm;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractResetButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractSearchButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.classid.ClassId;

import ch.ahoegger.docbox.client.administration.hr.taxgroup.AbstractTaxGroupSmartField;
import ch.ahoegger.docbox.client.hr.employer.EmployerTaxGroupSearchForm.MainBox.EmployerField;
import ch.ahoegger.docbox.client.hr.employer.EmployerTaxGroupSearchForm.MainBox.FinalizedRadioGroup;
import ch.ahoegger.docbox.client.hr.employer.EmployerTaxGroupSearchForm.MainBox.ResetButton;
import ch.ahoegger.docbox.client.hr.employer.EmployerTaxGroupSearchForm.MainBox.SearchButton;
import ch.ahoegger.docbox.client.hr.employer.EmployerTaxGroupSearchForm.MainBox.TaxGroupField;
import ch.ahoegger.docbox.client.templates.AbstractFinalizedRadioGroup;
import ch.ahoegger.docbox.shared.hr.employer.EmployerTaxGroupSearchFormData;

/**
 * <h3>{@link EmployerTaxGroupSearchForm}</h3>
 *
 * @author aho
 */
@FormData(value = EmployerTaxGroupSearchFormData.class, sdkCommand = SdkCommand.CREATE)
public class EmployerTaxGroupSearchForm extends AbstractSearchForm {

  public FinalizedRadioGroup getFinalizedRadioGroup() {
    return getFieldByClass(FinalizedRadioGroup.class);
  }

  public ResetButton getResetButton() {
    return getFieldByClass(ResetButton.class);
  }

  public SearchButton getSearchButton() {
    return getFieldByClass(SearchButton.class);
  }

  public TaxGroupField getTaxGroupField() {
    return getFieldByClass(TaxGroupField.class);
  }

  public EmployerField getEmployerField() {
    return getFieldByClass(EmployerField.class);
  }

  @ClassId("e1b07e22-81fb-4ddc-acdd-06de89cd727a")
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
    public class TaxGroupField extends AbstractTaxGroupSmartField {
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
}

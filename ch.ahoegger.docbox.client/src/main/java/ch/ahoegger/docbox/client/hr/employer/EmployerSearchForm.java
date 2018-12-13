package ch.ahoegger.docbox.client.hr.employer;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.dto.FormData.SdkCommand;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractSearchForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractResetButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractSearchButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.classid.ClassId;

import ch.ahoegger.docbox.client.administration.user.AbstractUserSmartField;
import ch.ahoegger.docbox.client.hr.employer.EmployerSearchForm.MainBox.ResetButton;
import ch.ahoegger.docbox.client.hr.employer.EmployerSearchForm.MainBox.SearchButton;
import ch.ahoegger.docbox.client.hr.employer.EmployerSearchForm.MainBox.UserField;
import ch.ahoegger.docbox.shared.hr.employer.EmployerSearchFormData;

/**
 * <h3>{@link EmployerSearchForm}</h3>
 *
 * @author aho
 */
@FormData(value = EmployerSearchFormData.class, sdkCommand = SdkCommand.CREATE)
public class EmployerSearchForm extends AbstractSearchForm {
  public EmployerSearchForm() {
    setHandler(new EmployerSearchForm.SearchHandler());
  }

  public ResetButton getResetButton() {
    return getFieldByClass(ResetButton.class);
  }

  public SearchButton getSearchButton() {
    return getFieldByClass(SearchButton.class);
  }

  public UserField getUserField() {
    return getFieldByClass(UserField.class);
  }

  @ClassId("c114ceae-574d-4f39-855d-840cf230a1d6")
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
    public class UserField extends AbstractUserSmartField {
    }

    @Order(10000)
    public class SearchButton extends AbstractSearchButton {
    }

    @Order(11000)
    public class ResetButton extends AbstractResetButton {
    }
  }

  public class SearchHandler extends AbstractFormHandler {
    @Override
    protected void execLoad() {
    }

    @Override
    protected void execStore() {
    }
  }
}

package ch.ahoegger.docbox.client.hr.tax;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractSearchForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractResetButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractSearchButton;
import org.eclipse.scout.rt.client.ui.form.fields.datefield.AbstractDateField;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.sequencebox.AbstractSequenceBox;
import org.eclipse.scout.rt.client.ui.form.fields.tabbox.AbstractTabBox;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.shared.TEXTS;

import ch.ahoegger.docbox.client.hr.tax.TaxGroupSearchForm.MainBox.ResetButton;
import ch.ahoegger.docbox.client.hr.tax.TaxGroupSearchForm.MainBox.SearchButton;
import ch.ahoegger.docbox.client.hr.tax.TaxGroupSearchForm.MainBox.SearchTabBox.SearchBox.EndDateBox;
import ch.ahoegger.docbox.client.hr.tax.TaxGroupSearchForm.MainBox.SearchTabBox.SearchBox.EndDateBox.EndDateFromField;
import ch.ahoegger.docbox.client.hr.tax.TaxGroupSearchForm.MainBox.SearchTabBox.SearchBox.EndDateBox.EndDateToField;
import ch.ahoegger.docbox.client.hr.tax.TaxGroupSearchForm.MainBox.SearchTabBox.SearchBox.StartDateBox;
import ch.ahoegger.docbox.client.hr.tax.TaxGroupSearchForm.MainBox.SearchTabBox.SearchBox.StartDateBox.StartDateFromField;
import ch.ahoegger.docbox.client.hr.tax.TaxGroupSearchForm.MainBox.SearchTabBox.SearchBox.StartDateBox.StartDateToField;
import ch.ahoegger.docbox.shared.hr.tax.TaxGroupSearchFormData;

@FormData(value = TaxGroupSearchFormData.class, sdkCommand = FormData.SdkCommand.CREATE)
public class TaxGroupSearchForm extends AbstractSearchForm {

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("TaxGroupSearch");
  }

  public void startModify() {
    startInternalExclusive(new ModifyHandler());
  }

  public void startNew() {
    startInternal(new NewHandler());
  }

  public StartDateBox getStartDateBox() {
    return getFieldByClass(StartDateBox.class);
  }

  public StartDateFromField getStartDateFromField() {
    return getFieldByClass(StartDateFromField.class);
  }

  public StartDateToField getStartDateToField() {
    return getFieldByClass(StartDateToField.class);
  }

  public EndDateBox getEndDateBox() {
    return getFieldByClass(EndDateBox.class);
  }

  public EndDateFromField getEndDateFromField() {
    return getFieldByClass(EndDateFromField.class);
  }

  public EndDateToField getEndDateToField() {
    return getFieldByClass(EndDateToField.class);
  }

  public ResetButton getResetButton() {
    return getFieldByClass(ResetButton.class);
  }

  public SearchButton getSearchButton() {
    return getFieldByClass(SearchButton.class);
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  @Order(1000)
  public class MainBox extends AbstractGroupBox {

    @Order(1000)
    public class SearchTabBox extends AbstractTabBox {

      @Order(1000)
      public class SearchBox extends AbstractGroupBox {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("Search");
        }

        @Order(1000)
        public class StartDateBox extends AbstractSequenceBox {
          @Override
          protected String getConfiguredLabel() {
            return TEXTS.get("StartDate");
          }

          @Order(1000)
          public class StartDateFromField extends AbstractDateField {
            @Override
            protected String getConfiguredLabel() {
              return TEXTS.get("from");
            }
          }

          @Order(2000)
          public class StartDateToField extends AbstractDateField {
            @Override
            protected String getConfiguredLabel() {
              return TEXTS.get("to");
            }
          }
        }

        @Order(2000)
        public class EndDateBox extends AbstractSequenceBox {
          @Override
          protected String getConfiguredLabel() {
            return TEXTS.get("EndDate");
          }

          @Order(1000)
          public class EndDateFromField extends AbstractDateField {
            @Override
            protected String getConfiguredLabel() {
              return TEXTS.get("from");
            }
          }

          @Order(2000)
          public class EndDateToField extends AbstractDateField {
            @Override
            protected String getConfiguredLabel() {
              return TEXTS.get("to");
            }
          }
        }
      }
    }

    @Order(10000)
    public class SearchButton extends AbstractSearchButton {
    }

    @Order(11000)
    public class ResetButton extends AbstractResetButton {
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
    }

    @Override
    protected void execStore() {
    }
  }
}

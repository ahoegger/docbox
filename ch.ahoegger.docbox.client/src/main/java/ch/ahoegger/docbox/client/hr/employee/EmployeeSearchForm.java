package ch.ahoegger.docbox.client.hr.employee;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.dto.FormData.SdkCommand;
import org.eclipse.scout.rt.client.ui.action.keystroke.AbstractKeyStroke;
import org.eclipse.scout.rt.client.ui.action.keystroke.IKeyStroke;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractSearchForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractRadioButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractResetButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractSearchButton;
import org.eclipse.scout.rt.client.ui.form.fields.datefield.AbstractDateField;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.radiobuttongroup.AbstractRadioButtonGroup;
import org.eclipse.scout.rt.client.ui.form.fields.sequencebox.AbstractSequenceBox;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.client.ui.form.fields.tabbox.AbstractTabBox;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.util.TriState;
import org.eclipse.scout.rt.shared.TEXTS;

import ch.ahoegger.docbox.client.hr.employee.EmployeeSearchForm.MainBox.ResetButton;
import ch.ahoegger.docbox.client.hr.employee.EmployeeSearchForm.MainBox.SearchButton;
import ch.ahoegger.docbox.client.hr.employee.EmployeeSearchForm.MainBox.SearchTabBox;
import ch.ahoegger.docbox.client.hr.employee.EmployeeSearchForm.MainBox.SearchTabBox.SearchBox;
import ch.ahoegger.docbox.client.hr.employee.EmployeeSearchForm.MainBox.SearchTabBox.SearchBox.ActiveBox;
import ch.ahoegger.docbox.client.hr.employee.EmployeeSearchForm.MainBox.SearchTabBox.SearchBox.BirthdayBox;
import ch.ahoegger.docbox.client.hr.employee.EmployeeSearchForm.MainBox.SearchTabBox.SearchBox.BirthdayBox.BirthdayToField;
import ch.ahoegger.docbox.client.hr.employee.EmployeeSearchForm.MainBox.SearchTabBox.SearchBox.BirthdayBox.BirtheayFromField;
import ch.ahoegger.docbox.client.hr.employee.EmployeeSearchForm.MainBox.SearchTabBox.SearchBox.FirstNameField;
import ch.ahoegger.docbox.client.hr.employee.EmployeeSearchForm.MainBox.SearchTabBox.SearchBox.LastNameField;
import ch.ahoegger.docbox.or.definition.table.IEmployeeTable;
import ch.ahoegger.docbox.shared.hr.employer.EmployeeSearchFormData;

/**
 * <h3>{@link EmployeeSearchForm}</h3>
 *
 * @author Andreas Hoegger
 */

@FormData(value = EmployeeSearchFormData.class, sdkCommand = SdkCommand.CREATE)
public class EmployeeSearchForm extends AbstractSearchForm {

  public EmployeeSearchForm() {
    setHandler(new SearchHandler());
  }

  public ActiveBox getActiveBox() {
    return getFieldByClass(ActiveBox.class);
  }

  public FirstNameField getFirstNameField() {
    return getFieldByClass(FirstNameField.class);
  }

  public LastNameField getLastNameField() {
    return getFieldByClass(LastNameField.class);
  }

  public SearchBox getSearchBox() {
    return getFieldByClass(SearchBox.class);
  }

  public SearchTabBox getSearchTabBox() {
    return getFieldByClass(SearchTabBox.class);
  }

  public SearchButton getSearchButton() {
    return getFieldByClass(SearchButton.class);
  }

  public BirthdayBox getBirthdayBox() {
    return getFieldByClass(BirthdayBox.class);
  }

  public BirtheayFromField getBirtheayFromField() {
    return getFieldByClass(BirtheayFromField.class);
  }

  public BirthdayToField getBirthdayToField() {
    return getFieldByClass(BirthdayToField.class);
  }

  public ResetButton getResetButton() {
    return getFieldByClass(ResetButton.class);
  }

  public class MainBox extends AbstractGroupBox {

    @Order(1000)
    public class SearchTabBox extends AbstractTabBox {

      @Order(10)
      public class SearchBox extends AbstractGroupBox {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("Search");
        }

        @Order(1000)
        public class FirstNameField extends AbstractStringField {
          @Override
          protected String getConfiguredLabel() {
            return TEXTS.get("Firstname");
          }

          @Override
          protected int getConfiguredMaxLength() {
            return IEmployeeTable.FIRST_NAME_LENGTH;
          }
        }

        @Order(2000)
        public class LastNameField extends AbstractStringField {
          @Override
          protected String getConfiguredLabel() {
            return TEXTS.get("Name");
          }

          @Override
          protected int getConfiguredMaxLength() {
            return IEmployeeTable.LAST_NAME_LENGTH;
          }
        }

        @Order(2000)
        public class ActiveBox extends AbstractRadioButtonGroup<TriState> {

          @Order(1000)
          public class ActiveButton extends AbstractRadioButton<TriState> {
            @Override
            protected TriState getConfiguredRadioValue() {
              return TriState.TRUE;
            }

            @Override
            protected String getConfiguredLabel() {
              return TEXTS.get("Active");
            }
          }

          @Order(2000)
          public class InactiveButton extends AbstractRadioButton<TriState> {
            @Override
            protected TriState getConfiguredRadioValue() {
              return TriState.FALSE;
            }

            @Override
            protected String getConfiguredLabel() {
              return TEXTS.get("Inactive");
            }
          }

          @Order(3000)
          public class AllButton extends AbstractRadioButton<TriState> {
            @Override
            protected TriState getConfiguredRadioValue() {
              return TriState.UNDEFINED;
            }

            @Override
            protected String getConfiguredLabel() {
              return TEXTS.get("All");
            }
          }
        }

        @Order(3000)
        public class BirthdayBox extends AbstractSequenceBox {
          @Override
          protected String getConfiguredLabel() {
            return TEXTS.get("Birthday");
          }

          @Override
          protected boolean getConfiguredAutoCheckFromTo() {
            return true;
          }

          @Order(1000)
          public class BirtheayFromField extends AbstractDateField {
            @Override
            protected String getConfiguredLabel() {
              return TEXTS.get("from");
            }
          }

          @Order(2000)
          public class BirthdayToField extends AbstractDateField {
            @Override
            protected String getConfiguredLabel() {
              return TEXTS.get("to");
            }
          }

        }

      }
    }

    @Order(200)
    public class SearchButton extends AbstractSearchButton {
    }

    @Order(210)
    public class ResetButton extends AbstractResetButton {
    }

    /**
     * Useful inside a wizard (starts search instead of clicking "next").
     */
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

  public class SearchHandler extends AbstractFormHandler {
    @Override
    protected void execLoad() {
      getActiveBox().setValue(TriState.TRUE);
    }

    @Override
    protected void execStore() {
    }
  }

}

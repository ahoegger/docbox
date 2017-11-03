package ch.ahoegger.docbox.client.hr.billing;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.dto.FormData.SdkCommand;
import org.eclipse.scout.rt.client.ui.action.keystroke.AbstractKeyStroke;
import org.eclipse.scout.rt.client.ui.action.keystroke.IKeyStroke;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractSearchForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractResetButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractSearchButton;
import org.eclipse.scout.rt.client.ui.form.fields.datefield.AbstractDateField;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.placeholder.AbstractPlaceholderField;
import org.eclipse.scout.rt.client.ui.form.fields.sequencebox.AbstractSequenceBox;
import org.eclipse.scout.rt.client.ui.form.fields.tabbox.AbstractTabBox;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.shared.TEXTS;

import ch.ahoegger.docbox.client.hr.billing.PostingGroupSearchForm.MainBox.SearchTabBox.SearchBox.EndDateBox;
import ch.ahoegger.docbox.client.hr.billing.PostingGroupSearchForm.MainBox.SearchTabBox.SearchBox.EndDateBox.EndDateFromField;
import ch.ahoegger.docbox.client.hr.billing.PostingGroupSearchForm.MainBox.SearchTabBox.SearchBox.EndDateBox.EndDateToField;
import ch.ahoegger.docbox.client.hr.billing.PostingGroupSearchForm.MainBox.SearchTabBox.SearchBox.PartnerSmartField;
import ch.ahoegger.docbox.client.hr.billing.PostingGroupSearchForm.MainBox.SearchTabBox.SearchBox.StartDateBox;
import ch.ahoegger.docbox.client.hr.billing.PostingGroupSearchForm.MainBox.SearchTabBox.SearchBox.StartDateBox.StartDateFromField;
import ch.ahoegger.docbox.client.hr.billing.PostingGroupSearchForm.MainBox.SearchTabBox.SearchBox.StartDateBox.StartDateToField;
import ch.ahoegger.docbox.client.hr.employee.EmployeeSearchForm.MainBox.ResetButton;
import ch.ahoegger.docbox.client.hr.employee.EmployeeSearchForm.MainBox.SearchButton;
import ch.ahoegger.docbox.client.partner.AbstractPartnerSmartField;
import ch.ahoegger.docbox.shared.hr.billing.PostingGroupSearchFormData;
import ch.ahoegger.docbox.shared.util.LocalDateUtility;

/**
 * <h3>{@link PostingGroupSearchForm}</h3>
 *
 * @author Andreas Hoegger
 */
@FormData(value = PostingGroupSearchFormData.class, sdkCommand = SdkCommand.CREATE)
public class PostingGroupSearchForm extends AbstractSearchForm {

  private BigDecimal m_taxGroupId;
  private Boolean m_includeUnbilled = Boolean.TRUE;

  public PostingGroupSearchForm() {
    setHandler(new SearchHandler(this));
  }

  @FormData
  public void setTaxGroupId(BigDecimal taxGroupId) {
    m_taxGroupId = taxGroupId;
  }

  @FormData
  public BigDecimal getTaxGroupId() {
    return m_taxGroupId;
  }

  @FormData
  public void setIncludeUnbilled(Boolean includeUnbilled) {
    m_includeUnbilled = includeUnbilled;
  }

  @FormData
  public Boolean getIncludeUnbilled() {
    return m_includeUnbilled;
  }

  public SearchButton getSearchButton() {
    return getFieldByClass(SearchButton.class);
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

  public PartnerSmartField getPartnerSmartField() {
    return getFieldByClass(PartnerSmartField.class);
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

        @Order(1)
        public class PartnerSmartField extends AbstractPartnerSmartField {
        }

        @Order(500)
        public class PlaceholderField extends AbstractPlaceholderField {
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

  public static class SearchHandler extends AbstractFormHandler {
    private PostingGroupSearchForm m_form;

    /**
     * @param form
     */
    public SearchHandler(PostingGroupSearchForm form) {
      m_form = form;
    }

    @Override
    protected void execLoad() {
      super.execLoad();
      LocalDate date = LocalDate.now().withDayOfMonth(1).minusMonths(6);
      m_form.getStartDateFromField().setValue(LocalDateUtility.toDate(date));
    }

    @Override
    protected void execStore() {
    }
  }
}

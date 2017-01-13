package ch.ahoegger.docbox.client.hr.billing;

import java.math.BigDecimal;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.dto.FormData.SdkCommand;
import org.eclipse.scout.rt.client.ui.action.keystroke.AbstractKeyStroke;
import org.eclipse.scout.rt.client.ui.action.keystroke.IKeyStroke;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractSearchForm;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractResetButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractSearchButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.tabbox.AbstractTabBox;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.shared.TEXTS;

import ch.ahoegger.docbox.client.hr.employee.EmployeeSearchForm.MainBox.ResetButton;
import ch.ahoegger.docbox.client.hr.employee.EmployeeSearchForm.MainBox.SearchButton;
import ch.ahoegger.docbox.shared.hr.billing.PostingGroupSearchFormData;

/**
 * <h3>{@link PostingGroupSearchForm}</h3>
 *
 * @author Andreas Hoegger
 */
@FormData(value = PostingGroupSearchFormData.class, sdkCommand = SdkCommand.CREATE)
public class PostingGroupSearchForm extends AbstractSearchForm {
  private BigDecimal m_partnerId;
  private BigDecimal m_taxGroupId;
  private Boolean m_includeUnbilled = Boolean.TRUE;

  @FormData
  public void setPartnerId(BigDecimal partnerId) {
    m_partnerId = partnerId;
  }

  @FormData
  public BigDecimal getPartnerId() {
    return m_partnerId;
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
}

package ch.ahoegger.docbox.client.hr.entity;

import java.math.BigDecimal;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.dto.FormData.SdkCommand;
import org.eclipse.scout.rt.client.ui.action.keystroke.AbstractKeyStroke;
import org.eclipse.scout.rt.client.ui.action.keystroke.IKeyStroke;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractSearchForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.bigdecimalfield.AbstractBigDecimalField;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractResetButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractSearchButton;
import org.eclipse.scout.rt.client.ui.form.fields.datefield.AbstractDateField;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.sequencebox.AbstractSequenceBox;
import org.eclipse.scout.rt.client.ui.form.fields.tabbox.AbstractTabBox;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.text.TEXTS;

import ch.ahoegger.docbox.client.hr.entity.EntitySearchForm.MainBox.ResetButton;
import ch.ahoegger.docbox.client.hr.entity.EntitySearchForm.MainBox.SearchButton;
import ch.ahoegger.docbox.client.hr.entity.EntitySearchForm.MainBox.SearchTabBox;
import ch.ahoegger.docbox.client.hr.entity.EntitySearchForm.MainBox.SearchTabBox.SearchBox;
import ch.ahoegger.docbox.client.hr.entity.EntitySearchForm.MainBox.SearchTabBox.SearchBox.EntityDateBox;
import ch.ahoegger.docbox.client.hr.entity.EntitySearchForm.MainBox.SearchTabBox.SearchBox.EntityDateBox.EntityDateFromField;
import ch.ahoegger.docbox.client.hr.entity.EntitySearchForm.MainBox.SearchTabBox.SearchBox.EntityDateBox.EntityDateToField;
import ch.ahoegger.docbox.client.hr.entity.EntitySearchForm.MainBox.SearchTabBox.SearchBox.PartnerIdField;
import ch.ahoegger.docbox.shared.hr.entity.EntitySearchFormData;

/**
 * <h3>{@link EntitySearchForm}</h3>
 *
 * @author Andreas Hoegger
 */

@FormData(value = EntitySearchFormData.class, sdkCommand = SdkCommand.CREATE)
public class EntitySearchForm extends AbstractSearchForm {

  private BigDecimal m_payslipId;

  public EntitySearchForm() {
    setHandler(new SearchHandler());
  }

  @FormData
  public void setPayslipId(BigDecimal payslipId) {
    m_payslipId = payslipId;
  }

  @FormData
  public BigDecimal getPayslipId() {
    return m_payslipId;
  }

  public SearchBox getSearchBox() {
    return getFieldByClass(SearchBox.class);
  }

  public SearchTabBox getSearchTabBox() {
    return getFieldByClass(SearchTabBox.class);
  }

  public EntityDateBox getEntityDateBox() {
    return getFieldByClass(EntityDateBox.class);
  }

  public EntityDateFromField getEntityDateFromField() {
    return getFieldByClass(EntityDateFromField.class);
  }

  public EntityDateToField getEntityDateToField() {
    return getFieldByClass(EntityDateToField.class);
  }

  public SearchButton getSearchButton() {
    return getFieldByClass(SearchButton.class);
  }

  public PartnerIdField getPartnerIdField() {
    return getFieldByClass(PartnerIdField.class);
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

        @Order(0)
        public class PartnerIdField extends AbstractBigDecimalField {

          @Override
          protected boolean getConfiguredVisible() {
            return false;
          }

          @Override
          protected BigDecimal getConfiguredMinValue() {
            return new BigDecimal("-9999999999999999999");
          }

          @Override
          protected BigDecimal getConfiguredMaxValue() {
            return new BigDecimal("9999999999999999999");
          }
        }

        @Order(1000)
        public class EntityDateBox extends AbstractSequenceBox {
          @Override
          protected String getConfiguredLabel() {
            return TEXTS.get("Entity");
          }

          @Order(1000)
          public class EntityDateFromField extends AbstractDateField {
            @Override
            protected String getConfiguredLabel() {
              return TEXTS.get("from");
            }
          }

          @Order(2000)
          public class EntityDateToField extends AbstractDateField {
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
    }

    @Override
    protected void execStore() {
    }
  }

}

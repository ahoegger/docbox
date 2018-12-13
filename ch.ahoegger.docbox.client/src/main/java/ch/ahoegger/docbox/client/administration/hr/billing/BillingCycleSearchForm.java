package ch.ahoegger.docbox.client.administration.hr.billing;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.dto.FormData.SdkCommand;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractSearchForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.text.TEXTS;

import ch.ahoegger.docbox.client.administration.hr.billing.BillingCycleSearchForm.MainBox.BillingCycleField;
import ch.ahoegger.docbox.client.administration.hr.billing.BillingCycleSearchForm.MainBox.TaxGroupField;
import ch.ahoegger.docbox.client.administration.hr.taxgroup.AbstractTaxGroupSmartField;
import ch.ahoegger.docbox.shared.administration.hr.billingcycle.BillingCycleSearchFormData;

/**
 * <h3>{@link BillingCycleSearchForm}</h3>
 *
 * @author aho
 */
@FormData(value = BillingCycleSearchFormData.class, sdkCommand = SdkCommand.CREATE)
public class BillingCycleSearchForm extends AbstractSearchForm {

  public BillingCycleSearchForm() {
    setHandler(new BillingCycleSearchForm.SearchHandler());
  }

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("TaxGroupSearch");
  }

  public BillingCycleField getBillingCycleField() {
    return getFieldByClass(BillingCycleField.class);
  }

  public TaxGroupField getTaxGroupField() {
    return getFieldByClass(TaxGroupField.class);
  }

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

    @Order(0)
    public class BillingCycleField extends AbstractBillingCycleSmartField {
    }

    @Order(1000)
    public class TaxGroupField extends AbstractTaxGroupSmartField {
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

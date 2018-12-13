package ch.ahoegger.docbox.client.administration.hr.billing;

import java.math.BigDecimal;

import org.eclipse.scout.rt.client.ui.form.fields.smartfield.AbstractSmartField;
import org.eclipse.scout.rt.platform.text.TEXTS;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;

import ch.ahoegger.docbox.shared.administration.hr.billing.BillingCycleLookupCall;

/**
 * <h3>{@link AbstractBillingCycleSmartField}</h3>
 *
 * @author aho
 */
public abstract class AbstractBillingCycleSmartField extends AbstractSmartField<BigDecimal> {

  @Override
  protected String getConfiguredLabel() {
    return TEXTS.get("BillingCycle");
  }

  @Override
  protected Class<? extends ILookupCall<BigDecimal>> getConfiguredLookupCall() {
    return BillingCycleLookupCall.class;
  }
}

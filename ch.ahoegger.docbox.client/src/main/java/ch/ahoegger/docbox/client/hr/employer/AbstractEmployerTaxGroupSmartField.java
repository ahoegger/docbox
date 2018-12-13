package ch.ahoegger.docbox.client.hr.employer;

import java.math.BigDecimal;

import org.eclipse.scout.rt.client.ui.form.fields.smartfield.AbstractSmartField;
import org.eclipse.scout.rt.platform.text.TEXTS;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;

import ch.ahoegger.docbox.shared.hr.employer.EmployerTaxGroupLookupCall;

/**
 * <h3>{@link AbstractEmployerTaxGroupSmartField}</h3>
 *
 * @author aho
 */
public abstract class AbstractEmployerTaxGroupSmartField extends AbstractSmartField<BigDecimal> {

  @Override
  protected String getConfiguredLabel() {
    return TEXTS.get("TaxGroup");
  }

  @Override
  protected Class<? extends ILookupCall<BigDecimal>> getConfiguredLookupCall() {
    return EmployerTaxGroupLookupCall.class;
  }
}

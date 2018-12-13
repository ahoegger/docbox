package ch.ahoegger.docbox.client.administration.hr.taxgroup;

import java.math.BigDecimal;

import org.eclipse.scout.rt.client.ui.form.fields.smartfield.AbstractSmartField;
import org.eclipse.scout.rt.platform.text.TEXTS;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;

import ch.ahoegger.docbox.shared.administration.taxgroup.TaxGroupLookupCall;

/**
 * <h3>{@link AbstractTaxGroupSmartField}</h3>
 *
 * @author aho
 */
public abstract class AbstractTaxGroupSmartField extends AbstractSmartField<BigDecimal> {
  @Override
  protected String getConfiguredLabel() {
    return TEXTS.get("TaxGroup");
  }

  @Override
  protected Class<? extends ILookupCall<BigDecimal>> getConfiguredLookupCall() {
    return TaxGroupLookupCall.class;
  }

  @Override
  protected void execPrepareBrowseLookup(ILookupCall<BigDecimal> call) {
    execPrepareBrowseTaxGroupLookup((TaxGroupLookupCall) call);
    super.execPrepareBrowseLookup(call);
  }

  protected void execPrepareBrowseTaxGroupLookup(TaxGroupLookupCall call) {
  }

}

package ch.ahoegger.docbox.client.partner;

import java.math.BigDecimal;

import org.eclipse.scout.rt.client.ui.form.fields.smartfield.AbstractSmartField;
import org.eclipse.scout.rt.platform.text.TEXTS;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;

import ch.ahoegger.docbox.shared.partner.PartnerLookupCall;

/**
 * <h3>{@link AbstractPartnerSmartField}</h3>
 *
 * @author Andreas Hoegger
 */
public class AbstractPartnerSmartField extends AbstractSmartField<BigDecimal> {
  @Override
  protected String getConfiguredLabel() {
    return TEXTS.get("Partner");
  }

  @Override
  protected Class<? extends ILookupCall<BigDecimal>> getConfiguredLookupCall() {
    return PartnerLookupCall.class;
  }

  @Override
  protected boolean getConfiguredActiveFilterEnabled() {
    return true;
  }
}

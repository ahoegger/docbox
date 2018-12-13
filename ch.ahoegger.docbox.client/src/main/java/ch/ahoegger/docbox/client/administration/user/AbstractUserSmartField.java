package ch.ahoegger.docbox.client.administration.user;

import org.eclipse.scout.rt.client.ui.form.fields.smartfield.AbstractSmartField;
import org.eclipse.scout.rt.platform.text.TEXTS;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;

import ch.ahoegger.docbox.shared.administration.user.UserLookupCall;

/**
 * <h3>{@link AbstractUserSmartField}</h3>
 *
 * @author aho
 */
public abstract class AbstractUserSmartField extends AbstractSmartField<String> {

  @Override
  protected String getConfiguredLabel() {
    return TEXTS.get("User");
  }

  @Override
  protected Class<? extends ILookupCall<String>> getConfiguredLookupCall() {
    return UserLookupCall.class;
  }
}

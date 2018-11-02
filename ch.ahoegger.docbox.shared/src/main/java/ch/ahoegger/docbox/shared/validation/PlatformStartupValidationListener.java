package ch.ahoegger.docbox.shared.validation;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.IPlatform;
import org.eclipse.scout.rt.platform.IPlatformListener;
import org.eclipse.scout.rt.platform.PlatformEvent;

/**
 * <h3>{@link PlatformStartupValidationListener}</h3>
 *
 * @author aho
 */
public class PlatformStartupValidationListener implements IPlatformListener {
  @Override
  public void stateChanged(PlatformEvent event) {
    if (event.getState() == IPlatform.State.PlatformStarted) {
      BEANS.all(IStartupValidatableBean.class).forEach(IStartupValidatableBean::validate);
    }
  }
}

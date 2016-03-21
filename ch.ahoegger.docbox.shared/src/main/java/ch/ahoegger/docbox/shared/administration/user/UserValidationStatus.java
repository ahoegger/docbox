package ch.ahoegger.docbox.shared.administration.user;

import org.eclipse.scout.rt.platform.status.Status;
import org.eclipse.scout.rt.shared.TEXTS;

/**
 * <h3>{@link UserValidationStatus}</h3>
 *
 * @author aho
 */
public final class UserValidationStatus {

  public static class AdministratorAtLeastOne extends Status {
    private static final long serialVersionUID = 1L;

    /**
     * @param message
     * @param severity
     */
    public AdministratorAtLeastOne() {
      super(TEXTS.get("Validate_OneAdminAtLeast"), ERROR);
    }

  }
}

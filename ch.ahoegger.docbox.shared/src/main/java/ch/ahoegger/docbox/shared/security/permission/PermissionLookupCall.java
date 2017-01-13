package ch.ahoegger.docbox.shared.security.permission;

import org.eclipse.scout.rt.shared.services.lookup.CodeLookupCall;

/**
 * <h3>{@link PermissionLookupCall}</h3>
 *
 * @author Andreas Hoegger
 */
public class PermissionLookupCall extends CodeLookupCall<Integer> {
  private static final long serialVersionUID = 1L;

  /**
   * @param codeTypeClass
   */
  public PermissionLookupCall() {
    super(PermissionCodeType.class);
  }

}

package ch.ahoegger.docbox.shared.permission;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.shared.services.lookup.ILookupRow;
import org.eclipse.scout.rt.shared.services.lookup.LocalLookupCall;
import org.eclipse.scout.rt.shared.services.lookup.LookupRow;

import ch.ahoegger.docbox.shared.document.IDocumentPermissionTable;

/**
 * <h3>{@link PermissionLookupCall}</h3>
 *
 * @author Andreas Hoegger
 */
public class PermissionLookupCall extends LocalLookupCall<Integer> {

  private static final long serialVersionUID = 1L;
  private boolean m_withOwner;

  public PermissionLookupCall() {
    this(true);

  }

  public PermissionLookupCall(boolean withOwner) {
    m_withOwner = withOwner;

  }

  @Override
  protected List<? extends ILookupRow<Integer>> execCreateLookupRows() {
    List<ILookupRow<Integer>> result = new ArrayList<ILookupRow<Integer>>();
    result.add(new LookupRow<Integer>(IDocumentPermissionTable.PERMISSION_READ, "read"));
    result.add(new LookupRow<Integer>(IDocumentPermissionTable.PERMISSION_WRITE, "write"));
    if (isWithOwner()) {
      result.add(new LookupRow<Integer>(IDocumentPermissionTable.PERMISSION_OWNER, "owner"));
    }
    return result;
  }

  public boolean isWithOwner() {
    return m_withOwner;
  }
}

package ch.ahoegger.docbox.client.templates;

import java.util.function.Predicate;

import org.eclipse.scout.rt.client.ui.action.IAction;

/**
 * <h3>{@link INotInherited}</h3>
 *
 * @author aho
 */
public interface INotInherited {
  static Predicate<IAction> FILTER = a -> !(a instanceof INotInherited);
}

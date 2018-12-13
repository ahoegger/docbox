package ch.ahoegger.docbox.client.templates;

import java.util.List;

import org.eclipse.scout.rt.client.ui.action.ActionUtility;
import org.eclipse.scout.rt.client.ui.action.menu.IMenu;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithNodes;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPageWithTable;

/**
 * <h3>{@link AbstractDocboxPageWithNodes}</h3>
 *
 * @author aho
 */
public abstract class AbstractDocboxPageWithNodes extends AbstractPageWithNodes {

  @Override
  protected List<IMenu> execComputeParentTablePageMenus(IPageWithTable<?> parentTablePage) {
    return ActionUtility.getActions(super.execComputeParentTablePageMenus(parentTablePage), INotInherited.FILTER);
  }

}

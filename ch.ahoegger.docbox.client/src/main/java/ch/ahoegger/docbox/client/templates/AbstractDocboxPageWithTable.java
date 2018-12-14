package ch.ahoegger.docbox.client.templates;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.scout.rt.client.context.ClientRunContexts;
import org.eclipse.scout.rt.client.ui.action.ActionUtility;
import org.eclipse.scout.rt.client.ui.action.menu.IMenu;
import org.eclipse.scout.rt.client.ui.basic.table.ITable;
import org.eclipse.scout.rt.client.ui.desktop.IDesktop;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPageWithTable;
import org.eclipse.scout.rt.platform.util.CollectionUtility;

import ch.ahoegger.docbox.client.ClientSession;

/**
 * <h3>{@link AbstractDocboxPageWithTable}</h3>
 *
 * @author Andreas Hoegger
 */
public abstract class AbstractDocboxPageWithTable<TABLE extends ITable> extends AbstractPageWithTable<TABLE> {

  private Set<Object> m_dataTypesNextActivation = new HashSet<>();

  /**
   *
   */
  public AbstractDocboxPageWithTable() {
    super();
  }

  /**
   * @param callInitializer
   * @param userPreferenceContext
   */
  public AbstractDocboxPageWithTable(boolean callInitializer, String userPreferenceContext) {
    super(callInitializer, userPreferenceContext);
  }

  /**
   * @param callInitializer
   */
  public AbstractDocboxPageWithTable(boolean callInitializer) {
    super(callInitializer);
  }

  /**
   * @param userPreferenceContext
   */
  public AbstractDocboxPageWithTable(String userPreferenceContext) {
    super(userPreferenceContext);
  }

  protected IDesktop getDesktop() {
    return ClientRunContexts.copyCurrent().getDesktop();
  }

  @Override
  protected void execDataChanged(Object... dataTypes) {
    if (getOutline().equals(ClientSession.get().getDesktop().getOutline())) {
      super.execDataChanged(dataTypes);
    }
    else {
      Arrays.stream(dataTypes).forEach(dt -> m_dataTypesNextActivation.add(dt));
    }
  }

  @Override
  protected void execPageActivated() {
    if (CollectionUtility.hasElements(m_dataTypesNextActivation)) {
      super.execDataChanged(m_dataTypesNextActivation.toArray());
      m_dataTypesNextActivation.clear();
    }

  }

  @Override
  protected List<IMenu> execComputeParentTablePageMenus(IPageWithTable<?> parentTablePage) {
    return ActionUtility.getActions(super.execComputeParentTablePageMenus(parentTablePage), INotInherited.FILTER);
  }

  @Override
  protected List<IMenu> execComputeTableEmptySpaceMenus() {
    return ActionUtility.getActions(super.execComputeTableEmptySpaceMenus(), INotInherited.FILTER);
  }
}

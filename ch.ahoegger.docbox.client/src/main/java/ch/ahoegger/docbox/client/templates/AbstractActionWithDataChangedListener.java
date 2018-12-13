package ch.ahoegger.docbox.client.templates;

import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.desktop.datachange.IDataChangeListener;

import ch.ahoegger.docbox.client.Desktop;

/**
 * <h3>{@link AbstractActionWithDataChangedListener}</h3>
 *
 * @author aho
 */
public abstract class AbstractActionWithDataChangedListener extends AbstractMenu {

  public static String VISIBILITY_DIMENSION_DATA_CHANGED = "VISIBILITY_DIMENSION_DATA_CHANGED";

  private static Object[] m_dataTypes;
  private IDataChangeListener m_dataChangedListener = (event) -> {
    updateVisibility();
  };

  protected Object[] getConfiguredDataChangeTypes() {
    return null;
  }

  @Override
  protected void execInitAction() {
    super.execInitAction();
    m_dataTypes = getConfiguredDataChangeTypes();
    if (m_dataTypes != null) {
      Desktop.CURRENT.get().addDataChangeListener(m_dataChangedListener, m_dataTypes);
    }
  }

  @Override
  protected void execDispose() {
    super.execDispose();
    if (m_dataTypes != null) {
      Desktop.CURRENT.get().removeDataChangeListener(m_dataChangedListener, m_dataTypes);
    }
  }

  public void updateVisibility() {
    setVisible(execComputeVisibility(), VISIBILITY_DIMENSION_DATA_CHANGED);
  }

  protected boolean execComputeVisibility() {
    return true;
  }

}

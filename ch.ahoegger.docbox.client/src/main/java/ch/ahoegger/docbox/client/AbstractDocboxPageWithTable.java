package ch.ahoegger.docbox.client;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.scout.rt.client.ui.basic.table.ITable;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.platform.util.CollectionUtility;

/**
 * <h3>{@link AbstractDocboxPageWithTable}</h3>
 *
 * @author Andreas Hoegger
 */
public class AbstractDocboxPageWithTable<TABLE extends ITable> extends AbstractPageWithTable<TABLE> {

  private Set<Object> m_dataTypesNextActivation = new HashSet<>();

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
}

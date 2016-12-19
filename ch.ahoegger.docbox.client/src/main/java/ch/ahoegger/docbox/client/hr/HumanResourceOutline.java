package ch.ahoegger.docbox.client.hr;

import java.util.List;

import org.eclipse.scout.rt.client.ui.desktop.outline.AbstractOutline;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.shared.TEXTS;

import ch.ahoegger.docbox.client.hr.employee.EmployeeTablePage;
import ch.ahoegger.docbox.shared.Icons;

/**
 * <h3>{@link HumanResourceOutline}</h3>
 *
 * @author Andreas Hoegger
 */
public class HumanResourceOutline extends AbstractOutline {

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("HumanResource");
  }

  @Override
  protected String getConfiguredIconId() {
    return Icons.Group;
  }

  @Override
  protected void execCreateChildPages(List<IPage<?>> pageList) {
    pageList.add(new EmployeeTablePage());
  }

}

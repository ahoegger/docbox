package ch.ahoegger.docbox.client.administration;

import java.util.List;

import org.eclipse.scout.rt.client.ui.desktop.outline.AbstractOutline;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.shared.TEXTS;

import ch.ahoegger.docbox.client.administration.user.UserTablePage;

/**
 * <h3>{@link AdministrationOutline}</h3>
 *
 * @author aho
 */
public class AdministrationOutline extends AbstractOutline {
  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("Administration");
  }

  @Override
  protected void execCreateChildPages(List<IPage<?>> pageList) {
    pageList.add(new UserTablePage());
  }
}

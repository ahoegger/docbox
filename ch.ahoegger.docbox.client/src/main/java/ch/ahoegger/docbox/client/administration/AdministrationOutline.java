package ch.ahoegger.docbox.client.administration;

import java.util.List;

import org.eclipse.scout.rt.client.ui.desktop.outline.AbstractOutline;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.platform.text.TEXTS;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;

import ch.ahoegger.docbox.client.administration.user.UserTablePage;
import ch.ahoegger.docbox.shared.security.permission.AdministratorPermission;

/**
 * <h3>{@link AdministrationOutline}</h3>
 *
 * @author Andreas Hoegger
 */
public class AdministrationOutline extends AbstractOutline {
  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("Administration");
  }

  @Override
  protected boolean getConfiguredVisible() {
    return ACCESS.check(new AdministratorPermission());
  }

  @Override
  protected void execCreateChildPages(List<IPage<?>> pageList) {
    pageList.add(new UserTablePage());
  }
}

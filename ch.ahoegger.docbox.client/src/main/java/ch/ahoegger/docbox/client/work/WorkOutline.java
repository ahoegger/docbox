package ch.ahoegger.docbox.client.work;

import java.util.List;

import org.eclipse.scout.rt.client.ui.desktop.outline.AbstractOutline;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.shared.TEXTS;

import ch.ahoegger.docbox.client.conversation.ConversationTablePage;
import ch.ahoegger.docbox.client.document.DocumentTablePage;
import ch.ahoegger.docbox.client.partner.PartnerTablePage;
import ch.ahoegger.docbox.shared.Icons;

/**
 * <h3>{@link WorkOutline}</h3>
 *
 * @author Andreas Hoegger
 */
@Order(1000)
public class WorkOutline extends AbstractOutline {

  @Override
  protected void execCreateChildPages(List<IPage<?>> pageList) {
    pageList.add(new DocumentTablePage());
    pageList.add(new ConversationTablePage());
    pageList.add(new PartnerTablePage());
  }

  @Override
  protected void execInitTree() {
    super.execInitTree();
//    ClientSession.get().getDesktop().activateFirstPage();
//    selectFirstNode();
  }

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("Work");
  }

  @Override
  protected String getConfiguredIconId() {
    return Icons.Pencil;
  }

}

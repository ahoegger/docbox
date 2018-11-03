package ch.ahoegger.docbox.client.document;

import java.math.BigDecimal;
import java.util.List;

import org.eclipse.scout.rt.client.ui.action.ActionUtility;
import org.eclipse.scout.rt.client.ui.action.menu.IMenu;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithNodes;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPageWithTable;
import org.eclipse.scout.rt.client.ui.form.IForm;

import ch.ahoegger.docbox.client.util.INotInheritedMenu;

/**
 * <h3>{@link DocumentDetailPage}</h3>
 *
 * @author Andreas Hoegger
 */
public class DocumentDetailPage extends AbstractPageWithNodes {

  private BigDecimal m_documentId;

  public DocumentDetailPage(BigDecimal documentId) {
    m_documentId = documentId;
  }

  public BigDecimal getDocumentId() {
    return m_documentId;
  }

  @Override
  protected Class<? extends IForm> getConfiguredDetailForm() {
    return DocumentForm.class;
  }

  @Override
  protected List<IMenu> execComputeParentTablePageMenus(IPageWithTable<?> parentTablePage) {
    return ActionUtility.getActions(super.execComputeParentTablePageMenus(parentTablePage), a -> !(a instanceof INotInheritedMenu));
  }

  @Override
  public DocumentForm getDetailForm() {
    return (DocumentForm) super.getDetailForm();
  }

  @Override
  protected void startDetailForm() {
    getDetailForm().setHandler(new DocumentForm.DocumentFormPageHandler(getDetailForm(), getDocumentId()));
    getDetailForm().start();
  }

  @Override
  protected boolean getConfiguredLeaf() {
    return true;
  }
}

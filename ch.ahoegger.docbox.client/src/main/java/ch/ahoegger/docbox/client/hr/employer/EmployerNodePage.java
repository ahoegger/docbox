package ch.ahoegger.docbox.client.hr.employer;

import java.math.BigDecimal;
import java.util.List;

import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithNodes;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;

import ch.ahoegger.docbox.client.hr.employee.EmployeeTablePage;

/**
 * <h3>{@link EmployerNodePage}</h3>
 *
 * @author aho
 */
public class EmployerNodePage extends AbstractPageWithNodes {
  private BigDecimal m_employerId;

  public void setEmployerId(BigDecimal employerId) {
    m_employerId = employerId;
  }

  public BigDecimal getEmployerId() {
    return m_employerId;
  }

  @Override
  protected void execCreateChildPages(List<IPage<?>> pageList) {
    EmployeeTablePage eeTablePage = new EmployeeTablePage(false);
    eeTablePage.getSearchFormInternal().getEmployerField().setValue(getEmployerId());
    eeTablePage.getSearchFormInternal().getEmployerField().setEnabled(false);
    pageList.add(eeTablePage);

    EmployerTaxGroupTablePage page = new EmployerTaxGroupTablePage();
    page.getSearchFormInternal().getEmployerField().setValue(getEmployerId());
    page.getSearchFormInternal().getEmployerField().setEnabled(false);

    pageList.add(page);
  }

}

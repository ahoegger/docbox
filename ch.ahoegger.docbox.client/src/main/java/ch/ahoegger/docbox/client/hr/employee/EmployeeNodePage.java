package ch.ahoegger.docbox.client.hr.employee;

import java.math.BigDecimal;
import java.util.List;

import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithNodes;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;

import ch.ahoegger.docbox.client.administration.taxgroup.TaxGroupTablePage;
import ch.ahoegger.docbox.client.document.DocumentTablePage;
import ch.ahoegger.docbox.client.hr.billing.PayslipTablePage;

public class EmployeeNodePage extends AbstractPageWithNodes {

  private BigDecimal m_partnerId;

  @Override
  protected void execCreateChildPages(List<IPage<?>> pageList) {
    PayslipTablePage page = new PayslipTablePage();
    page.setPartnerId(getPartnerId());
    pageList.add(page);

    DocumentTablePage documentTablePage = new DocumentTablePage();
    documentTablePage.setPartnerId(m_partnerId);
    pageList.add(documentTablePage);

    TaxGroupTablePage taxGroupTablePage = new TaxGroupTablePage();
    taxGroupTablePage.setPartnerId(getPartnerId());
    pageList.add(taxGroupTablePage);

  }

  public void setPartnerId(BigDecimal partnerId) {
    m_partnerId = partnerId;
  }

  public BigDecimal getPartnerId() {
    return m_partnerId;
  }

}

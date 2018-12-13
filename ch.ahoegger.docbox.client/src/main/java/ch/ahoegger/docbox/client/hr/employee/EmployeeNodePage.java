package ch.ahoegger.docbox.client.hr.employee;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithNodes;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.platform.util.TriState;

import ch.ahoegger.docbox.client.administration.hr.billing.IBillingCycleEntity;
import ch.ahoegger.docbox.client.document.DocumentTablePage;
import ch.ahoegger.docbox.client.hr.billing.payslip.IPayslipEntity;
import ch.ahoegger.docbox.client.hr.entity.EntityTablePage;
import ch.ahoegger.docbox.shared.hr.billing.payslip.IPayslipService;
import ch.ahoegger.docbox.shared.hr.billing.payslip.PayslipSearchFormData;

public class EmployeeNodePage extends AbstractPageWithNodes {

  private BigDecimal m_employeeId;

  @Override
  protected void execInitPage() {
    registerDataChangeListener(IPayslipEntity.ENTITY_KEY_FINALIZE, IBillingCycleEntity.ENTITY_KEY);
  }

  @Override
  protected void execDataChanged(Object... dataTypes) {
    super.execDataChanged(dataTypes);
    reloadPage();
  }

  @Override
  protected void execCreateChildPages(List<IPage<?>> pageList) {
    // open payslips
    PayslipSearchFormData etgFormData = new PayslipSearchFormData();
    etgFormData.getEmployee().setValue(m_employeeId);
    etgFormData.getFinalzedRadioGroup().setValue(TriState.FALSE);

    pageList.addAll(CollectionUtility.arrayList(BEANS.get(IPayslipService.class).getTableData(etgFormData).getRows())
        .stream().map(row -> {
          EntityTablePage ePage = new EntityTablePage();
          ePage.setPayslipId(row.getPayslipId());
          ePage.setTitle(row.getBillingCycle());
          ePage.getTable().getMenuByClass(EntityTablePage.Table.FinalizePayslipMenu.class).setVisible(true);
          return ePage;
        })
        .collect(Collectors.toList()));

    EmployeeTaxGroupTablePage page = new EmployeeTaxGroupTablePage();
    page.getSearchFormInternal().getEmployeeField().setValue(getEmployeeId());
    page.getSearchFormInternal().getEmployeeField().setEnabled(false);
//    page.setEmployeeId(getEmployeeId());
    pageList.add(page);

    DocumentTablePage documentTablePage = new DocumentTablePage();
    documentTablePage.setPartnerId(m_employeeId);
    pageList.add(documentTablePage);

//    TaxGroupTablePage taxGroupTablePage = new TaxGroupTablePage();
//    taxGroupTablePage.setPartnerId(getPartnerId());
//    pageList.add(taxGroupTablePage);

  }

  public void setEmployeeId(BigDecimal employeeId) {
    m_employeeId = employeeId;
  }

  public BigDecimal getEmployeeId() {
    return m_employeeId;
  }

}

package ch.ahoegger.docbox.shared.hr.billing.payslip;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Generated;

import org.eclipse.scout.rt.shared.data.basic.table.AbstractTableRowData;
import org.eclipse.scout.rt.shared.data.page.AbstractTablePageData;

/**
 * <b>NOTE:</b><br>
 * This class is auto generated by the Scout SDK. No manual modifications recommended.
 */
@Generated(value = "ch.ahoegger.docbox.client.hr.billing.payslip.PayslipTablePage", comments = "This class is auto generated by the Scout SDK. No manual modifications recommended.")
public class PayslipTableData extends AbstractTablePageData {

  private static final long serialVersionUID = 1L;

  @Override
  public PayslipTableRowData addRow() {
    return (PayslipTableRowData) super.addRow();
  }

  @Override
  public PayslipTableRowData addRow(int rowState) {
    return (PayslipTableRowData) super.addRow(rowState);
  }

  @Override
  public PayslipTableRowData createRow() {
    return new PayslipTableRowData();
  }

  @Override
  public Class<? extends AbstractTableRowData> getRowType() {
    return PayslipTableRowData.class;
  }

  @Override
  public PayslipTableRowData[] getRows() {
    return (PayslipTableRowData[]) super.getRows();
  }

  @Override
  public PayslipTableRowData rowAt(int index) {
    return (PayslipTableRowData) super.rowAt(index);
  }

  public void setRows(PayslipTableRowData[] rows) {
    super.setRows(rows);
  }

  public static class PayslipTableRowData extends AbstractTableRowData {

    private static final long serialVersionUID = 1L;
    public static final String payslipId = "payslipId";
    public static final String employee = "employee";
    public static final String billingCycle = "billingCycle";
    public static final String periodFrom = "periodFrom";
    public static final String periodTo = "periodTo";
    public static final String statementId = "statementId";
    public static final String documentId = "documentId";
    public static final String taxType = "taxType";
    public static final String statementDate = "statementDate";
    public static final String accountNumber = "accountNumber";
    public static final String hourlyWage = "hourlyWage";
    public static final String socialInsuranceRate = "socialInsuranceRate";
    public static final String sourceTaxRate = "sourceTaxRate";
    public static final String vacationExtraRate = "vacationExtraRate";
    public static final String workingHours = "workingHours";
    public static final String wage = "wage";
    public static final String brutto = "brutto";
    public static final String netto = "netto";
    public static final String payout = "payout";
    public static final String sourceTax = "sourceTax";
    public static final String socialInsuranceTax = "socialInsuranceTax";
    public static final String vacationExtra = "vacationExtra";
    public static final String expenses = "expenses";
    private BigDecimal m_payslipId;
    private BigDecimal m_employee;
    private BigDecimal m_billingCycle;
    private Date m_periodFrom;
    private Date m_periodTo;
    private BigDecimal m_statementId;
    private BigDecimal m_documentId;
    private BigDecimal m_taxType;
    private Date m_statementDate;
    private String m_accountNumber;
    private BigDecimal m_hourlyWage;
    private BigDecimal m_socialInsuranceRate;
    private BigDecimal m_sourceTaxRate;
    private BigDecimal m_vacationExtraRate;
    private BigDecimal m_workingHours;
    private BigDecimal m_wage;
    private BigDecimal m_brutto;
    private BigDecimal m_netto;
    private BigDecimal m_payout;
    private BigDecimal m_sourceTax;
    private BigDecimal m_socialInsuranceTax;
    private BigDecimal m_vacationExtra;
    private BigDecimal m_expenses;

    public BigDecimal getPayslipId() {
      return m_payslipId;
    }

    public void setPayslipId(BigDecimal newPayslipId) {
      m_payslipId = newPayslipId;
    }

    public BigDecimal getEmployee() {
      return m_employee;
    }

    public void setEmployee(BigDecimal newEmployee) {
      m_employee = newEmployee;
    }

    public BigDecimal getBillingCycle() {
      return m_billingCycle;
    }

    public void setBillingCycle(BigDecimal newBillingCycle) {
      m_billingCycle = newBillingCycle;
    }

    public Date getPeriodFrom() {
      return m_periodFrom;
    }

    public void setPeriodFrom(Date newPeriodFrom) {
      m_periodFrom = newPeriodFrom;
    }

    public Date getPeriodTo() {
      return m_periodTo;
    }

    public void setPeriodTo(Date newPeriodTo) {
      m_periodTo = newPeriodTo;
    }

    public BigDecimal getStatementId() {
      return m_statementId;
    }

    public void setStatementId(BigDecimal newStatementId) {
      m_statementId = newStatementId;
    }

    public BigDecimal getDocumentId() {
      return m_documentId;
    }

    public void setDocumentId(BigDecimal newDocumentId) {
      m_documentId = newDocumentId;
    }

    public BigDecimal getTaxType() {
      return m_taxType;
    }

    public void setTaxType(BigDecimal newTaxType) {
      m_taxType = newTaxType;
    }

    public Date getStatementDate() {
      return m_statementDate;
    }

    public void setStatementDate(Date newStatementDate) {
      m_statementDate = newStatementDate;
    }

    public String getAccountNumber() {
      return m_accountNumber;
    }

    public void setAccountNumber(String newAccountNumber) {
      m_accountNumber = newAccountNumber;
    }

    public BigDecimal getHourlyWage() {
      return m_hourlyWage;
    }

    public void setHourlyWage(BigDecimal newHourlyWage) {
      m_hourlyWage = newHourlyWage;
    }

    public BigDecimal getSocialInsuranceRate() {
      return m_socialInsuranceRate;
    }

    public void setSocialInsuranceRate(BigDecimal newSocialInsuranceRate) {
      m_socialInsuranceRate = newSocialInsuranceRate;
    }

    public BigDecimal getSourceTaxRate() {
      return m_sourceTaxRate;
    }

    public void setSourceTaxRate(BigDecimal newSourceTaxRate) {
      m_sourceTaxRate = newSourceTaxRate;
    }

    public BigDecimal getVacationExtraRate() {
      return m_vacationExtraRate;
    }

    public void setVacationExtraRate(BigDecimal newVacationExtraRate) {
      m_vacationExtraRate = newVacationExtraRate;
    }

    public BigDecimal getWorkingHours() {
      return m_workingHours;
    }

    public void setWorkingHours(BigDecimal newWorkingHours) {
      m_workingHours = newWorkingHours;
    }

    public BigDecimal getWage() {
      return m_wage;
    }

    public void setWage(BigDecimal newWage) {
      m_wage = newWage;
    }

    public BigDecimal getBrutto() {
      return m_brutto;
    }

    public void setBrutto(BigDecimal newBrutto) {
      m_brutto = newBrutto;
    }

    public BigDecimal getNetto() {
      return m_netto;
    }

    public void setNetto(BigDecimal newNetto) {
      m_netto = newNetto;
    }

    public BigDecimal getPayout() {
      return m_payout;
    }

    public void setPayout(BigDecimal newPayout) {
      m_payout = newPayout;
    }

    public BigDecimal getSourceTax() {
      return m_sourceTax;
    }

    public void setSourceTax(BigDecimal newSourceTax) {
      m_sourceTax = newSourceTax;
    }

    public BigDecimal getSocialInsuranceTax() {
      return m_socialInsuranceTax;
    }

    public void setSocialInsuranceTax(BigDecimal newSocialInsuranceTax) {
      m_socialInsuranceTax = newSocialInsuranceTax;
    }

    public BigDecimal getVacationExtra() {
      return m_vacationExtra;
    }

    public void setVacationExtra(BigDecimal newVacationExtra) {
      m_vacationExtra = newVacationExtra;
    }

    public BigDecimal getExpenses() {
      return m_expenses;
    }

    public void setExpenses(BigDecimal newExpenses) {
      m_expenses = newExpenses;
    }
  }
}

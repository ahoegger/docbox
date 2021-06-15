package ch.ahoegger.docbox.shared.hr.employee;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Generated;

import org.eclipse.scout.rt.platform.classid.ClassId;
import org.eclipse.scout.rt.shared.data.basic.table.AbstractTableRowData;
import org.eclipse.scout.rt.shared.data.page.AbstractTablePageData;

/**
 * <b>NOTE:</b><br>
 * This class is auto generated by the Scout SDK. No manual modifications recommended.
 */
@ClassId("9bcbf2e9-c45c-476f-820e-2076a7620595-formdata")
@Generated(value = "ch.ahoegger.docbox.client.hr.employee.EmployeeTaxGroupTablePage", comments = "This class is auto generated by the Scout SDK. No manual modifications recommended.")
public class EmployeeTaxGroupTableData extends AbstractTablePageData {

  private static final long serialVersionUID = 1L;

  @Override
  public EmployeeTaxGroupTableRowData addRow() {
    return (EmployeeTaxGroupTableRowData) super.addRow();
  }

  @Override
  public EmployeeTaxGroupTableRowData addRow(int rowState) {
    return (EmployeeTaxGroupTableRowData) super.addRow(rowState);
  }

  @Override
  public EmployeeTaxGroupTableRowData createRow() {
    return new EmployeeTaxGroupTableRowData();
  }

  @Override
  public Class<? extends AbstractTableRowData> getRowType() {
    return EmployeeTaxGroupTableRowData.class;
  }

  @Override
  public EmployeeTaxGroupTableRowData[] getRows() {
    return (EmployeeTaxGroupTableRowData[]) super.getRows();
  }

  @Override
  public EmployeeTaxGroupTableRowData rowAt(int index) {
    return (EmployeeTaxGroupTableRowData) super.rowAt(index);
  }

  public void setRows(EmployeeTaxGroupTableRowData[] rows) {
    super.setRows(rows);
  }

  public static class EmployeeTaxGroupTableRowData extends AbstractTableRowData {

    private static final long serialVersionUID = 1L;
    public static final String id = "id";
    public static final String taxGroup = "taxGroup";
    public static final String employee = "employee";
    public static final String start = "start";
    public static final String end = "end";
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
    public static final String pensionsFund = "pensionsFund";
    public static final String socialInsuranceTax = "socialInsuranceTax";
    public static final String vacationExtra = "vacationExtra";
    public static final String expenses = "expenses";
    private BigDecimal m_id;
    private BigDecimal m_taxGroup;
    private BigDecimal m_employee;
    private Date m_start;
    private Date m_end;
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
    private BigDecimal m_pensionsFund;
    private BigDecimal m_socialInsuranceTax;
    private BigDecimal m_vacationExtra;
    private BigDecimal m_expenses;

    public BigDecimal getId() {
      return m_id;
    }

    public void setId(BigDecimal newId) {
      m_id = newId;
    }

    public BigDecimal getTaxGroup() {
      return m_taxGroup;
    }

    public void setTaxGroup(BigDecimal newTaxGroup) {
      m_taxGroup = newTaxGroup;
    }

    public BigDecimal getEmployee() {
      return m_employee;
    }

    public void setEmployee(BigDecimal newEmployee) {
      m_employee = newEmployee;
    }

    public Date getStart() {
      return m_start;
    }

    public void setStart(Date newStart) {
      m_start = newStart;
    }

    public Date getEnd() {
      return m_end;
    }

    public void setEnd(Date newEnd) {
      m_end = newEnd;
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

    public BigDecimal getPensionsFund() {
      return m_pensionsFund;
    }

    public void setPensionsFund(BigDecimal newPensionsFund) {
      m_pensionsFund = newPensionsFund;
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
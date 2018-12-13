package ch.ahoegger.docbox.server.hr.statement;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <h3>{@link StatementBean}</h3>
 *
 * @author aho
 */
public class StatementBean {

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
  private BigDecimal m_bruttoWage;
  private BigDecimal m_nettoWage;
  private BigDecimal m_nettoWagePayout;
  private BigDecimal m_sourceTax;
  private BigDecimal m_socialInsuranceTax;
  private BigDecimal m_vacationExtra;
  private BigDecimal m_expenses;

  public BigDecimal getStatementId() {
    return m_statementId;
  }

  public StatementBean withStatementId(BigDecimal statementId) {
    m_statementId = statementId;
    return this;
  }

  public BigDecimal getDocumentId() {
    return m_documentId;
  }

  public StatementBean withDocumentId(BigDecimal documentId) {
    m_documentId = documentId;
    return this;
  }

  public BigDecimal getTaxType() {
    return m_taxType;
  }

  public StatementBean withTaxType(BigDecimal taxType) {
    m_taxType = taxType;
    return this;
  }

  public Date getStatementDate() {
    return m_statementDate;
  }

  public StatementBean withStatementDate(Date statementDate) {
    m_statementDate = statementDate;
    return this;
  }

  public String getAccountNumber() {
    return m_accountNumber;
  }

  public StatementBean withAccountNumber(String accountNumber) {
    m_accountNumber = accountNumber;
    return this;
  }

  public BigDecimal getHourlyWage() {
    return m_hourlyWage;
  }

  public StatementBean withHourlyWage(BigDecimal hourlyWage) {
    m_hourlyWage = hourlyWage;
    return this;
  }

  public BigDecimal getSocialInsuranceRate() {
    return m_socialInsuranceRate;
  }

  public StatementBean withSocialInsuranceRate(BigDecimal socialInsuranceRate) {
    m_socialInsuranceRate = socialInsuranceRate;
    return this;
  }

  public BigDecimal getSourceTaxRate() {
    return m_sourceTaxRate;
  }

  public StatementBean withSourceTaxRate(BigDecimal sourceTaxRate) {
    m_sourceTaxRate = sourceTaxRate;
    return this;
  }

  public BigDecimal getVacationExtraRate() {
    return m_vacationExtraRate;
  }

  public StatementBean withVacationExtraRate(BigDecimal vacationExtraRate) {
    m_vacationExtraRate = vacationExtraRate;
    return this;
  }

  public BigDecimal getWorkingHours() {
    return m_workingHours;
  }

  public StatementBean withWorkingHours(BigDecimal workingHours) {
    m_workingHours = workingHours;
    return this;
  }

  public BigDecimal getWage() {
    return m_wage;
  }

  public StatementBean withWage(BigDecimal wage) {
    m_wage = wage;
    return this;
  }

  public BigDecimal getBruttoWage() {
    return m_bruttoWage;
  }

  public StatementBean withBruttoWage(BigDecimal bruttoWage) {
    m_bruttoWage = bruttoWage;
    return this;
  }

  public BigDecimal getNettoWage() {
    return m_nettoWage;
  }

  public StatementBean withNettoWage(BigDecimal nettoWage) {
    m_nettoWage = nettoWage;
    return this;
  }

  public BigDecimal getNettoWagePayout() {
    return m_nettoWagePayout;
  }

  public StatementBean withNettoWagePayout(BigDecimal nettoWagePayout) {
    m_nettoWagePayout = nettoWagePayout;
    return this;
  }

  public BigDecimal getSourceTax() {
    return m_sourceTax;
  }

  public StatementBean withSourceTax(BigDecimal sourceTax) {
    m_sourceTax = sourceTax;
    return this;
  }

  public BigDecimal getSocialInsuranceTax() {
    return m_socialInsuranceTax;
  }

  public StatementBean withSocialInsuranceTax(BigDecimal socialInsuranceTax) {
    m_socialInsuranceTax = socialInsuranceTax;
    return this;
  }

  public BigDecimal getVacationExtra() {
    return m_vacationExtra;
  }

  public StatementBean withVacationExtra(BigDecimal vacationExtra) {
    m_vacationExtra = vacationExtra;
    return this;
  }

  public BigDecimal getExpenses() {
    return m_expenses;
  }

  public StatementBean withExpenses(BigDecimal expenses) {
    m_expenses = expenses;
    return this;
  }

}

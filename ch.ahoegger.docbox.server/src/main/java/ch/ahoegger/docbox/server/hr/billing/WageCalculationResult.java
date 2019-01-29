package ch.ahoegger.docbox.server.hr.billing;

import java.math.BigDecimal;

/**
 * <h3>{@link WageCalculationResult}</h3>
 *
 * @author aho
 */
public class WageCalculationResult {
  private BigDecimal m_workingHours;
  private BigDecimal m_wage;
  private BigDecimal m_bruttoWage;
  private BigDecimal m_nettoWage;
  private BigDecimal m_nettoWagePayout;
  private BigDecimal m_sourceTax;
  private BigDecimal m_socialInsuranceTax;
  private BigDecimal m_pensionsFund;
  private BigDecimal m_vacationExtra;
  private BigDecimal m_expenses;

  public BigDecimal getWorkingHours() {
    return m_workingHours;
  }

  public WageCalculationResult withWorkingHours(BigDecimal workingHours) {
    m_workingHours = workingHours;
    return this;
  }

  public BigDecimal getWage() {
    return m_wage;
  }

  public WageCalculationResult withWage(BigDecimal wage) {
    m_wage = wage;
    return this;
  }

  public BigDecimal getBruttoWage() {
    return m_bruttoWage;
  }

  public WageCalculationResult withBruttoWage(BigDecimal bruttoWage) {
    m_bruttoWage = bruttoWage;
    return this;
  }

  public BigDecimal getNettoWage() {
    return m_nettoWage;
  }

  public WageCalculationResult withNettoWage(BigDecimal nettoWage) {
    m_nettoWage = nettoWage;
    return this;
  }

  public BigDecimal getNettoWagePayout() {
    return m_nettoWagePayout;
  }

  public WageCalculationResult withNettoWagePayout(BigDecimal nettoWagePayout) {
    m_nettoWagePayout = nettoWagePayout;
    return this;
  }

  public BigDecimal getSourceTax() {
    return m_sourceTax;
  }

  public WageCalculationResult withSourceTax(BigDecimal sourceTax) {
    m_sourceTax = sourceTax;
    return this;
  }

  public BigDecimal getPensionsFund() {
    return m_pensionsFund;
  }

  public WageCalculationResult withPensionsFund(BigDecimal pensionsFund) {
    m_pensionsFund = pensionsFund;
    return this;
  }

  public BigDecimal getSocialInsuranceTax() {
    return m_socialInsuranceTax;
  }

  public WageCalculationResult withSocialInsuranceTax(BigDecimal socialInsuranceTax) {
    m_socialInsuranceTax = socialInsuranceTax;
    return this;
  }

  public BigDecimal getVacationExtra() {
    return m_vacationExtra;
  }

  public WageCalculationResult withVacationExtra(BigDecimal vacationExtra) {
    m_vacationExtra = vacationExtra;
    return this;
  }

  public BigDecimal getExpenses() {
    return m_expenses;
  }

  public WageCalculationResult withExpenses(BigDecimal expenses) {
    m_expenses = expenses;
    return this;
  }

}

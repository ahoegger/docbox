package org.ch.ahoegger.docbox.jasper.bean;

import java.math.BigDecimal;
import java.util.List;

/**
 * <h3>{@link WageCalculation}</h3>
 *
 * @author aho
 */
public class WageCalculation {

  private BigDecimal m_hoursTotal;
  private BigDecimal m_bruttoWage;
  private BigDecimal m_nettoWage;
  private BigDecimal m_socialSecuityTaxRelative;
  private BigDecimal m_socialSecuityTax;
  private BigDecimal m_sourceTaxRelative;
  private BigDecimal m_sourceTax;
  private BigDecimal m_vacationExtraRelative;
  private BigDecimal m_vacationExtra;
  private BigDecimal m_expencesTotal;

  private List<Expense> m_expenses;
  private List<Work> m_workItems;

  public BigDecimal getHoursTotal() {
    return m_hoursTotal;
  }

  public void setHoursTotal(BigDecimal hoursTotal) {
    m_hoursTotal = hoursTotal;
  }

  public BigDecimal getBruttoWage() {
    return m_bruttoWage;
  }

  public void setBruttoWage(BigDecimal bruttoWage) {
    m_bruttoWage = bruttoWage;
  }

  public BigDecimal getNettoWage() {
    return m_nettoWage;
  }

  public void setNettoWage(BigDecimal nettoWage) {
    m_nettoWage = nettoWage;
  }

  public void setSocialSecuityTaxRelative(BigDecimal socialSecuityTaxRelative) {
    m_socialSecuityTaxRelative = socialSecuityTaxRelative;
  }

  public BigDecimal getSocialSecuityTaxRelative() {
    return m_socialSecuityTaxRelative;
  }

  public BigDecimal getSocialSecuityTax() {
    return m_socialSecuityTax;
  }

  public void setSocialSecuityTax(BigDecimal socialSecuityTax) {
    m_socialSecuityTax = socialSecuityTax;
  }

  public void setSourceTaxRelative(BigDecimal sourceTaxRelative) {
    m_sourceTaxRelative = sourceTaxRelative;
  }

  public BigDecimal getSourceTaxRelative() {
    return m_sourceTaxRelative;
  }

  public BigDecimal getSourceTax() {
    return m_sourceTax;
  }

  public void setSourceTax(BigDecimal sourceTax) {
    m_sourceTax = sourceTax;
  }

  public void setVacationExtraRelative(BigDecimal vacationExtraRelative) {
    m_vacationExtraRelative = vacationExtraRelative;
  }

  public BigDecimal getVacationExtraRelative() {
    return m_vacationExtraRelative;
  }

  public BigDecimal getVacationExtra() {
    return m_vacationExtra;
  }

  public void setVacationExtra(BigDecimal vacationExtra) {
    m_vacationExtra = vacationExtra;
  }

  public BigDecimal getExpencesTotal() {
    return m_expencesTotal;
  }

  public void setExpencesTotal(BigDecimal expencesTotal) {
    m_expencesTotal = expencesTotal;
  }

  public void setWorkItems(List<Work> workItems) {
    m_workItems = workItems;
  }

  public List<Work> getWorkItems() {
    return m_workItems;
  }

  public void setExpenses(List<Expense> expenses) {
    m_expenses = expenses;
  }

  public List<Expense> getExpenses() {
    return m_expenses;
  }

}

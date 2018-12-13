package ch.ahoegger.docbox.server.hr.billing;

import java.math.BigDecimal;
import java.util.List;

import org.eclipse.scout.rt.platform.util.CollectionUtility;

import ch.ahoegger.docbox.server.util.BigDecimalUtilitiy;
import ch.ahoegger.docbox.shared.hr.entity.EntityTablePageData.EntityTableRowData;

/**
 * <h3>{@link WageCalculationInput}</h3>
 *
 * @author aho
 */
public class WageCalculationInput {

  private BigDecimal m_taxType;
  private BigDecimal m_hourlyWage;
  private BigDecimal m_socialInsuranceRate;
  private BigDecimal m_sourceTaxRate;
  private BigDecimal m_vacationExtraRate;
  private List<EntityTableRowData> m_workEntities = CollectionUtility.emptyArrayList();
  private List<EntityTableRowData> m_expenseEntities = CollectionUtility.emptyArrayList();

  public BigDecimal getTaxType() {
    return BigDecimalUtilitiy.orZero(m_taxType);
  }

  public WageCalculationInput withTaxType(BigDecimal taxType) {
    m_taxType = taxType;
    return this;
  }

  public BigDecimal getHourlyWage() {
    return BigDecimalUtilitiy.orZero(m_hourlyWage);
  }

  public WageCalculationInput withHourlyWage(BigDecimal hourlyWage) {
    m_hourlyWage = hourlyWage;
    return this;
  }

  public BigDecimal getSocialInsuranceRate() {
    return BigDecimalUtilitiy.orZero(m_socialInsuranceRate);
  }

  public WageCalculationInput withSocialInsuranceRate(BigDecimal socialInsuranceRate) {
    m_socialInsuranceRate = socialInsuranceRate;
    return this;
  }

  public BigDecimal getSourceTaxRate() {
    return BigDecimalUtilitiy.orZero(m_sourceTaxRate);
  }

  public WageCalculationInput withSourceTaxRate(BigDecimal sourceTaxRate) {
    m_sourceTaxRate = sourceTaxRate;
    return this;
  }

  public BigDecimal getVacationExtraRate() {
    return BigDecimalUtilitiy.orZero(m_vacationExtraRate);
  }

  public WageCalculationInput withVacationExtraRate(BigDecimal vacationExtraRate) {
    m_vacationExtraRate = vacationExtraRate;
    return this;
  }

  public List<EntityTableRowData> getWorkEntities() {
    return m_workEntities;
  }

  public WageCalculationInput withWorkEntities(List<EntityTableRowData> workEntities) {
    m_workEntities = workEntities;
    return this;
  }

  public List<EntityTableRowData> getExpenseEntities() {
    return m_expenseEntities;
  }

  public WageCalculationInput withExpenseEntities(List<EntityTableRowData> expenseEntities) {
    m_expenseEntities = expenseEntities;
    return this;
  }
}

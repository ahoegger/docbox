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
  private BigDecimal m_pensionsFund;
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

  public BigDecimal getPensionsFund() {
    return m_pensionsFund;
  }

  public StatementBean withPensionsFund(BigDecimal pensionsFund) {
    m_pensionsFund = pensionsFund;
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

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((m_accountNumber == null) ? 0 : m_accountNumber.hashCode());
    result = prime * result + ((m_bruttoWage == null) ? 0 : m_bruttoWage.hashCode());
    result = prime * result + ((m_documentId == null) ? 0 : m_documentId.hashCode());
    result = prime * result + ((m_expenses == null) ? 0 : m_expenses.hashCode());
    result = prime * result + ((m_hourlyWage == null) ? 0 : m_hourlyWage.hashCode());
    result = prime * result + ((m_nettoWage == null) ? 0 : m_nettoWage.hashCode());
    result = prime * result + ((m_nettoWagePayout == null) ? 0 : m_nettoWagePayout.hashCode());
    result = prime * result + ((m_socialInsuranceRate == null) ? 0 : m_socialInsuranceRate.hashCode());
    result = prime * result + ((m_socialInsuranceTax == null) ? 0 : m_socialInsuranceTax.hashCode());
    result = prime * result + ((m_sourceTax == null) ? 0 : m_sourceTax.hashCode());
    result = prime * result + ((m_sourceTaxRate == null) ? 0 : m_sourceTaxRate.hashCode());
    result = prime * result + ((m_statementDate == null) ? 0 : m_statementDate.hashCode());
    result = prime * result + ((m_statementId == null) ? 0 : m_statementId.hashCode());
    result = prime * result + ((m_taxType == null) ? 0 : m_taxType.hashCode());
    result = prime * result + ((m_vacationExtra == null) ? 0 : m_vacationExtra.hashCode());
    result = prime * result + ((m_vacationExtraRate == null) ? 0 : m_vacationExtraRate.hashCode());
    result = prime * result + ((m_wage == null) ? 0 : m_wage.hashCode());
    result = prime * result + ((m_workingHours == null) ? 0 : m_workingHours.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    StatementBean other = (StatementBean) obj;
    if (m_accountNumber == null) {
      if (other.m_accountNumber != null) {
        return false;
      }
    }
    else if (!m_accountNumber.equals(other.m_accountNumber)) {
      return false;
    }
    if (m_bruttoWage == null) {
      if (other.m_bruttoWage != null) {
        return false;
      }
    }
    else if (m_bruttoWage.compareTo(other.m_bruttoWage) != 0) {
      return false;
    }
    if (m_documentId == null) {
      if (other.m_documentId != null) {
        return false;
      }
    }
    else if (!m_documentId.equals(other.m_documentId)) {
      return false;
    }
    if (m_expenses == null) {
      if (other.m_expenses != null) {
        return false;
      }
    }
    else if (m_expenses.compareTo(other.m_expenses) != 0) {
      return false;
    }
    if (m_hourlyWage == null) {
      if (other.m_hourlyWage != null) {
        return false;
      }
    }
    else if (m_hourlyWage.compareTo(other.m_hourlyWage) != 0) {
      return false;
    }
    if (m_nettoWage == null) {
      if (other.m_nettoWage != null) {
        return false;
      }
    }
    else if (m_nettoWage.compareTo(other.m_nettoWage) != 0) {
      return false;
    }
    if (m_nettoWagePayout == null) {
      if (other.m_nettoWagePayout != null) {
        return false;
      }
    }
    else if (m_nettoWagePayout.compareTo(other.m_nettoWagePayout) != 0) {
      return false;
    }
    if (m_socialInsuranceRate == null) {
      if (other.m_socialInsuranceRate != null) {
        return false;
      }
    }
    else if (m_socialInsuranceRate.compareTo(other.m_socialInsuranceRate) != 0) {
      return false;
    }
    if (m_socialInsuranceTax == null) {
      if (other.m_socialInsuranceTax != null) {
        return false;
      }
    }
    else if (m_socialInsuranceTax.compareTo(other.m_socialInsuranceTax) != 0) {
      return false;
    }
    if (m_sourceTax == null) {
      if (other.m_sourceTax != null) {
        return false;
      }
    }
    else if (m_sourceTax.compareTo(other.m_sourceTax) != 0) {
      return false;
    }
    if (m_sourceTaxRate == null) {
      if (other.m_sourceTaxRate != null) {
        return false;
      }
    }
    else if (m_sourceTaxRate.compareTo(other.m_sourceTaxRate) != 0) {
      return false;
    }
    if (m_statementDate == null) {
      if (other.m_statementDate != null) {
        return false;
      }
    }
    else if (!m_statementDate.equals(other.m_statementDate)) {
      return false;
    }
    if (m_statementId == null) {
      if (other.m_statementId != null) {
        return false;
      }
    }
    else if (!m_statementId.equals(other.m_statementId)) {
      return false;
    }
    if (m_taxType == null) {
      if (other.m_taxType != null) {
        return false;
      }
    }
    else if (!m_taxType.equals(other.m_taxType)) {
      return false;
    }
    if (m_vacationExtra == null) {
      if (other.m_vacationExtra != null) {
        return false;
      }
    }
    else if (m_vacationExtra.compareTo(other.m_vacationExtra) != 0) {
      return false;
    }
    if (m_vacationExtraRate == null) {
      if (other.m_vacationExtraRate != null) {
        return false;
      }
    }
    else if (m_vacationExtraRate.compareTo(other.m_vacationExtraRate) != 0) {
      return false;
    }
    if (m_wage == null) {
      if (other.m_wage != null) {
        return false;
      }
    }
    else if (m_wage.compareTo(other.m_wage) != 0) {
      return false;
    }
    if (m_workingHours == null) {
      if (other.m_workingHours != null) {
        return false;
      }
    }
    else if (m_workingHours.compareTo(other.m_workingHours) != 0) {
      return false;
    }
    return true;
  }

}

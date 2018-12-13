package ch.ahoegger.docbox.shared.administration.hr.billing;

import java.math.BigDecimal;
import java.util.Date;

import org.eclipse.scout.rt.shared.services.lookup.ILookupService;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;

/**
 * <h3>{@link BillingCycleLookupCall}</h3>
 *
 * @author Andreas Hoegger
 */
public class BillingCycleLookupCall extends LookupCall<BigDecimal> {

  private static final long serialVersionUID = 1L;
  private BigDecimal m_taxGroupId;
  private BigDecimal m_employeeId;
  private BigDecimal m_notEmployeeId;

  private Date m_startDate;
  private Date m_endDate;

  @Override
  protected Class<? extends ILookupService<BigDecimal>> getConfiguredService() {
    return IBillingCycleLookupService.class;
  }

  public void setTaxGroupId(BigDecimal taxGroupId) {
    m_taxGroupId = taxGroupId;
  }

  public BigDecimal getTaxGroupId() {
    return m_taxGroupId;
  }

  public void setEmployeeId(BigDecimal employeeId) {
    m_employeeId = employeeId;
  }

  public BigDecimal getEmployeeId() {
    return m_employeeId;
  }

  public void setNotEmployeeId(BigDecimal notEmployeeId) {
    m_notEmployeeId = notEmployeeId;
  }

  public BigDecimal getNotEmployeeId() {
    return m_notEmployeeId;
  }

  public Date getStartDate() {
    return m_startDate;
  }

  public void setStartDate(Date startDate) {
    m_startDate = startDate;
  }

  public Date getEndDate() {
    return m_endDate;
  }

  public void setEndDate(Date endDate) {
    m_endDate = endDate;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((m_employeeId == null) ? 0 : m_employeeId.hashCode());
    result = prime * result + ((m_endDate == null) ? 0 : m_endDate.hashCode());
    result = prime * result + ((m_notEmployeeId == null) ? 0 : m_notEmployeeId.hashCode());
    result = prime * result + ((m_startDate == null) ? 0 : m_startDate.hashCode());
    result = prime * result + ((m_taxGroupId == null) ? 0 : m_taxGroupId.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!super.equals(obj)) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    BillingCycleLookupCall other = (BillingCycleLookupCall) obj;
    if (m_employeeId == null) {
      if (other.m_employeeId != null) {
        return false;
      }
    }
    else if (!m_employeeId.equals(other.m_employeeId)) {
      return false;
    }
    if (m_endDate == null) {
      if (other.m_endDate != null) {
        return false;
      }
    }
    else if (!m_endDate.equals(other.m_endDate)) {
      return false;
    }
    if (m_notEmployeeId == null) {
      if (other.m_notEmployeeId != null) {
        return false;
      }
    }
    else if (!m_notEmployeeId.equals(other.m_notEmployeeId)) {
      return false;
    }
    if (m_startDate == null) {
      if (other.m_startDate != null) {
        return false;
      }
    }
    else if (!m_startDate.equals(other.m_startDate)) {
      return false;
    }
    if (m_taxGroupId == null) {
      if (other.m_taxGroupId != null) {
        return false;
      }
    }
    else if (!m_taxGroupId.equals(other.m_taxGroupId)) {
      return false;
    }
    return true;
  }

}

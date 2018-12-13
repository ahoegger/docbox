package ch.ahoegger.docbox.shared.administration.taxgroup;

import java.math.BigDecimal;
import java.util.Date;

import org.eclipse.scout.rt.shared.services.lookup.ILookupService;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;

/**
 * <h3>{@link TaxGroupLookupCall}</h3>
 *
 * @author Andreas Hoegger
 */
public class TaxGroupLookupCall extends LookupCall<BigDecimal> {

  private static final long serialVersionUID = 1L;

  private BigDecimal m_notEmployerId;
  private BigDecimal m_employerId;

  private BigDecimal m_notEmployeeId;
  private BigDecimal m_employeeId;

  private Date m_startDate;
  private Date m_endDate;

  private boolean m_shortText = false;

  @Override
  protected Class<? extends ILookupService<BigDecimal>> getConfiguredService() {
    return ITaxGroupLookupService.class;
  }

  public void setShortText(boolean shortText) {
    m_shortText = shortText;
  }

  public boolean isShortText() {
    return m_shortText;
  }

  public void setEmployerId(BigDecimal employerId) {
    m_employerId = employerId;
  }

  public BigDecimal getEmployerId() {
    return m_employerId;
  }

  public void setNotEmployerId(BigDecimal notEmployerId) {
    m_notEmployerId = notEmployerId;
  }

  public BigDecimal getNotEmployerId() {
    return m_notEmployerId;
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
    result = prime * result + ((m_employerId == null) ? 0 : m_employerId.hashCode());
    result = prime * result + ((m_endDate == null) ? 0 : m_endDate.hashCode());
    result = prime * result + ((m_notEmployeeId == null) ? 0 : m_notEmployeeId.hashCode());
    result = prime * result + ((m_notEmployerId == null) ? 0 : m_notEmployerId.hashCode());
    result = prime * result + (m_shortText ? 1231 : 1237);
    result = prime * result + ((m_startDate == null) ? 0 : m_startDate.hashCode());
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
    TaxGroupLookupCall other = (TaxGroupLookupCall) obj;
    if (m_employeeId == null) {
      if (other.m_employeeId != null) {
        return false;
      }
    }
    else if (!m_employeeId.equals(other.m_employeeId)) {
      return false;
    }
    if (m_employerId == null) {
      if (other.m_employerId != null) {
        return false;
      }
    }
    else if (!m_employerId.equals(other.m_employerId)) {
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
    if (m_notEmployerId == null) {
      if (other.m_notEmployerId != null) {
        return false;
      }
    }
    else if (!m_notEmployerId.equals(other.m_notEmployerId)) {
      return false;
    }
    if (m_shortText != other.m_shortText) {
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
    return true;
  }

}

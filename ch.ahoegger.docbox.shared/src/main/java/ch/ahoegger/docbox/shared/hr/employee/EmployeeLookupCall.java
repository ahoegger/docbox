package ch.ahoegger.docbox.shared.hr.employee;

import java.math.BigDecimal;
import java.util.Set;

import org.eclipse.scout.rt.shared.services.lookup.ILookupService;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;

/**
 * <h3>{@link EmployeeLookupCall}</h3>
 *
 * @author Andreas Hoegger
 */
public class EmployeeLookupCall extends LookupCall<BigDecimal> {

  private static final long serialVersionUID = 1L;

  private Set<BigDecimal> m_employerIds;

  @Override
  protected Class<? extends ILookupService<BigDecimal>> getConfiguredService() {
    return IEmployeeLookupService.class;
  }

  public Set<BigDecimal> getEmployerIds() {
    return m_employerIds;
  }

  public void setEmployerIds(Set<BigDecimal> employerIds) {
    m_employerIds = employerIds;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((m_employerIds == null) ? 0 : m_employerIds.hashCode());
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
    EmployeeLookupCall other = (EmployeeLookupCall) obj;
    if (m_employerIds == null) {
      if (other.m_employerIds != null) {
        return false;
      }
    }
    else if (!m_employerIds.equals(other.m_employerIds)) {
      return false;
    }
    return true;
  }

}

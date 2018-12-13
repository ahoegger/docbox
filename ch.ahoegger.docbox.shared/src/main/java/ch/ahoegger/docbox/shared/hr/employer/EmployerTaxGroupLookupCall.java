package ch.ahoegger.docbox.shared.hr.employer;

import java.math.BigDecimal;

import org.eclipse.scout.rt.shared.services.lookup.ILookupService;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;

/**
 * <h3>{@link EmployerTaxGroupLookupCall}</h3>
 *
 * @author Andreas Hoegger
 */
public class EmployerTaxGroupLookupCall extends LookupCall<BigDecimal> {

  private static final long serialVersionUID = 1L;

  private BigDecimal m_employeeIdHasTaxGroup;
  private BigDecimal m_employeeIdHasNotTaxGroup;
  private boolean m_shortText = false;

  @Override
  protected Class<? extends ILookupService<BigDecimal>> getConfiguredService() {
    return IEmployerTaxGroupLookupService.class;
  }

  public void setShortText(boolean shortText) {
    m_shortText = shortText;
  }

  public boolean isShortText() {
    return m_shortText;
  }

  public BigDecimal getEmployeeIdHasTaxGroup() {
    return m_employeeIdHasTaxGroup;
  }

  public void setEmployeeIdHasTaxGroup(BigDecimal employeeIdHasTaxGroup) {
    m_employeeIdHasTaxGroup = employeeIdHasTaxGroup;
  }

  public BigDecimal getEmployeeIdHasNotTaxGroup() {
    return m_employeeIdHasNotTaxGroup;
  }

  public void setEmployeeIdHasNotTaxGroup(BigDecimal employeeIdHasNotTaxGroup) {
    m_employeeIdHasNotTaxGroup = employeeIdHasNotTaxGroup;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((m_employeeIdHasNotTaxGroup == null) ? 0 : m_employeeIdHasNotTaxGroup.hashCode());
    result = prime * result + ((m_employeeIdHasTaxGroup == null) ? 0 : m_employeeIdHasTaxGroup.hashCode());
    result = prime * result + (m_shortText ? 1231 : 1237);
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
    EmployerTaxGroupLookupCall other = (EmployerTaxGroupLookupCall) obj;
    if (m_employeeIdHasNotTaxGroup == null) {
      if (other.m_employeeIdHasNotTaxGroup != null) {
        return false;
      }
    }
    else if (!m_employeeIdHasNotTaxGroup.equals(other.m_employeeIdHasNotTaxGroup)) {
      return false;
    }
    if (m_employeeIdHasTaxGroup == null) {
      if (other.m_employeeIdHasTaxGroup != null) {
        return false;
      }
    }
    else if (!m_employeeIdHasTaxGroup.equals(other.m_employeeIdHasTaxGroup)) {
      return false;
    }
    if (m_shortText != other.m_shortText) {
      return false;
    }
    return true;
  }

}

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
  private BigDecimal m_partnerId;
  private Date m_startDate;
  private Date m_endDate;

  @Override
  protected Class<? extends ILookupService<BigDecimal>> getConfiguredService() {
    return ITaxGroupLookupService.class;
  }

  public BigDecimal getPartnerId() {
    return m_partnerId;
  }

  public void setPartnerId(BigDecimal partnerId) {
    m_partnerId = partnerId;
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
}

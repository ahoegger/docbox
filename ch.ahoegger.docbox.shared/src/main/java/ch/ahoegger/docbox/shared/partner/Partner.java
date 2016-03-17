package ch.ahoegger.docbox.shared.partner;

import java.util.Date;

/**
 * <h3>{@link Partner}</h3>
 *
 * @author Andreas Hoegger
 */
public class Partner {

  private Long m_partnerId;
  private String m_name;
  private String m_description;
  private Date m_startDate;
  private Date m_endDate;

  public Long getPartnerId() {
    return m_partnerId;
  }

  public Partner withPartnerId(Long partnerId) {
    m_partnerId = partnerId;
    return this;
  }

  public String getName() {
    return m_name;
  }

  public Partner withName(String name) {
    m_name = name;
    return this;
  }

  public String getDescription() {
    return m_description;
  }

  public Partner withDescription(String description) {
    m_description = description;
    return this;
  }

  public Date getStartDate() {
    return m_startDate;
  }

  public Partner withStartDate(Date startDate) {
    m_startDate = startDate;
    return this;
  }

  public Date getEndDate() {
    return m_endDate;
  }

  public Partner withEndDate(Date endDate) {
    m_endDate = endDate;
    return this;
  }

}

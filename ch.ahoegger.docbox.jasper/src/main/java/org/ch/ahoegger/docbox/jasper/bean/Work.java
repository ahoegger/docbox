package org.ch.ahoegger.docbox.jasper.bean;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <h3>{@link ReportWorkItem}</h3>
 *
 * @author Andreas Hoegger
 */
public class Work {

  private LocalDate m_date;
  private BigDecimal m_hours;
  private String m_text;

  public LocalDate getDate() {
    return m_date;
  }

  public void setDate(LocalDate date) {
    m_date = date;
  }

  public Work withDate(LocalDate date) {
    m_date = date;
    return this;
  }

  public BigDecimal getHours() {
    return m_hours;
  }

  public void setHours(BigDecimal hours) {
    m_hours = hours;
  }

  public Work widthHours(BigDecimal hours) {
    m_hours = hours;
    return this;
  }

  public String getText() {
    return m_text;
  }

  public void setText(String text) {
    m_text = text;
  }

  public Work withText(String text) {
    m_text = text;
    return this;
  }

}

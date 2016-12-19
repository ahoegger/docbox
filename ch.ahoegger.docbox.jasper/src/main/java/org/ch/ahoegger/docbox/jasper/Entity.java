package org.ch.ahoegger.docbox.jasper;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <h3>{@link Entity}</h3>
 *
 * @author Andreas Hoegger
 */
public class Entity {
  private LocalDate m_date;
  private BigDecimal m_hoursWorked;

  public LocalDate getDate() {
    return m_date;
  }

  public Entity withDate(LocalDate date) {
    m_date = date;
    return this;
  }

  public BigDecimal getHoursWorked() {
    return m_hoursWorked;
  }

  public Entity withHoursWorked(BigDecimal hoursWorked) {
    m_hoursWorked = hoursWorked;
    return this;
  }

}

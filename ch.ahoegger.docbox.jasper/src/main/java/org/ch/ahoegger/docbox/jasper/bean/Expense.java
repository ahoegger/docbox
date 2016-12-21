package org.ch.ahoegger.docbox.jasper.bean;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <h3>{@link ReportExpenseItem}</h3>
 *
 * @author aho
 */
public class Expense {

  private LocalDate m_date;
  private BigDecimal m_amount;
  private String m_text;

  public LocalDate getDate() {
    return m_date;
  }

  public void setDate(LocalDate date) {
    m_date = date;
  }

  public Expense withDate(LocalDate date) {
    m_date = date;
    return this;
  }

  public BigDecimal getAmount() {
    return m_amount;
  }

  public void setAmount(BigDecimal amount) {
    m_amount = amount;
  }

  public Expense withAmount(BigDecimal amount) {
    m_amount = amount;
    return this;
  }

  public String getText() {
    return m_text;
  }

  public void setText(String text) {
    m_text = text;
  }

  public Expense withText(String text) {
    m_text = text;
    return this;
  }

}

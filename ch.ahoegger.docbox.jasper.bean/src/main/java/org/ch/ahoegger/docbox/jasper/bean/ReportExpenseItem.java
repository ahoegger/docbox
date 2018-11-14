package org.ch.ahoegger.docbox.jasper.bean;

/**
 * <h3>{@link ReportExpenseItem}</h3>
 *
 * @author Andreas Hoegger
 */
public class ReportExpenseItem {

  public String m_date;
  private String m_amount;
  private String m_text;

  public void setDate(String date) {
    m_date = date;
  }

  public String getDate() {
    return m_date;
  }

  public void setAmount(String amount) {
    m_amount = amount;
  }

  public String getAmount() {
    return m_amount;
  }

  public void setText(String text) {
    m_text = text;
  }

  public String getText() {
    return m_text;
  }
}

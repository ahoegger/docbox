package org.ch.ahoegger.docbox.jasper.bean;

/**
 * <h3>{@link ReportWorkItem}</h3>
 *
 * @author aho
 */
public class ReportWorkItem {
  public String m_date;
  private String m_hours;
  private String m_text;

  public void setDate(String date) {
    m_date = date;
  }

  public String getDate() {
    return m_date;
  }

  public void setHours(String hours) {
    m_hours = hours;
  }

  public String getHours() {
    return m_hours;
  }

  public void setText(String text) {
    m_text = text;
  }

  public String getText() {
    return m_text;
  }
}

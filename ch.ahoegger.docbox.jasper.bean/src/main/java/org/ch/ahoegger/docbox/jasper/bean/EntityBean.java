package org.ch.ahoegger.docbox.jasper.bean;

/**
 * <h3>{@link EntityBean}</h3>
 *
 * @author Andreas Hoegger
 */
public class EntityBean {

  public String m_date;
  private String m_hours;

  public String getDate() {
    return m_date;
  }

  public void setDate(String date) {
    m_date = date;
  }

  public String getHours() {
    return m_hours;
  }

  public void setHours(String hours) {
    m_hours = hours;
  }

}

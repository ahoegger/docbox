package org.ch.ahoegger.docbox.jasper.bean;

import java.util.ArrayList;
import java.util.Collection;

/**
 * <h3>{@link Account}</h3>
 *
 * @author Andreas Hoegger
 */
public class Account {

  private String m_addressLine1;
  private String m_addressLine2;
  private String m_addressLine3;
  private String m_title;
  private String m_date;
  private String m_hoursInPeriod;
  private String m_hourWage;
  private String m_bruttoWage;
  private String m_nettoWage;
  private String m_nettoWageRounded;
  private String m_socialInsuracnePercentage;
  private String m_socialInsuracneAbsolute;
  private String m_vacationExtraPercentage;
  private String m_vacationExtraAbsolute;
  private String m_sourceTaxAbsolute;
  private String m_sourceTaxProcentage;

  private String m_iban;

  private Collection<EntityBean> m_entities = new ArrayList<>();

  public String getAddressLine1() {
    return m_addressLine1;
  }

  public void setAddressLine1(String addressLine1) {
    m_addressLine1 = addressLine1;
  }

  public String getAddressLine2() {
    return m_addressLine2;
  }

  public void setAddressLine2(String addressLine2) {
    m_addressLine2 = addressLine2;
  }

  public String getAddressLine3() {
    return m_addressLine3;
  }

  public void setAddressLine3(String addressLine3) {
    m_addressLine3 = addressLine3;
  }

  public String getTitle() {
    return m_title;
  }

  public void setTitle(String title) {
    m_title = title;
  }

  public String getDate() {
    return m_date;
  }

  public void setDate(String date) {
    m_date = date;
  }

  public String getHoursInPeriod() {
    return m_hoursInPeriod;
  }

  public void setHoursInPeriod(String hoursInPeriod) {
    m_hoursInPeriod = hoursInPeriod;
  }

  public String getHourWage() {
    return m_hourWage;
  }

  public void setHourWage(String hourWage) {
    m_hourWage = hourWage;
  }

  public String getBruttoWage() {
    return m_bruttoWage;
  }

  public void setBruttoWage(String bruttoWage) {
    m_bruttoWage = bruttoWage;
  }

  public String getNettoWage() {
    return m_nettoWage;
  }

  public void setNettoWage(String nettoWage) {
    m_nettoWage = nettoWage;
  }

  public String getNettoWageRounded() {
    return m_nettoWageRounded;
  }

  public void setNettoWageRounded(String nettoWageRounded) {
    m_nettoWageRounded = nettoWageRounded;
  }

  public String getSocialInsuracnePercentage() {
    return m_socialInsuracnePercentage;
  }

  public void setSocialInsuracnePercentage(String socialInsuracnePercentage) {
    m_socialInsuracnePercentage = socialInsuracnePercentage;
  }

  public String getSocialInsuracneAbsolute() {
    return m_socialInsuracneAbsolute;
  }

  public void setSocialInsuracneAbsolute(String socialInsuracneAbsolute) {
    m_socialInsuracneAbsolute = socialInsuracneAbsolute;
  }

  public String getVacationExtraPercentage() {
    return m_vacationExtraPercentage;
  }

  public void setVacationExtraPercentage(String vacationExtraPercentage) {
    m_vacationExtraPercentage = vacationExtraPercentage;
  }

  public String getVacationExtraAbsolute() {
    return m_vacationExtraAbsolute;
  }

  public void setVacationExtraAbsolute(String vacationExtraAbsolute) {
    m_vacationExtraAbsolute = vacationExtraAbsolute;
  }

  public String getIban() {
    return m_iban;
  }

  public void setIban(String iban) {
    m_iban = iban;
  }

  public String getSourceTaxAbsolute() {
    return m_sourceTaxAbsolute;
  }

  public void setSourceTaxAbsolute(String sourceTaxAbsolute) {
    m_sourceTaxAbsolute = sourceTaxAbsolute;
  }

  public String getSourceTaxProcentage() {
    return m_sourceTaxProcentage;
  }

  public void setSourceTaxProcentage(String sourceTaxProcentage) {
    m_sourceTaxProcentage = sourceTaxProcentage;
  }

  public Collection<EntityBean> getEntities() {
    return m_entities;
  }

  public void setEntities(Collection<EntityBean> entities) {
    m_entities = entities;
  }

}

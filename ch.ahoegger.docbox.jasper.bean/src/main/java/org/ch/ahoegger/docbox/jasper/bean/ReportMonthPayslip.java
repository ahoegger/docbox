package org.ch.ahoegger.docbox.jasper.bean;

import java.util.ArrayList;
import java.util.Collection;

/**
 * <h3>{@link ReportMonthPayslip}</h3>
 *
 * @author Andreas Hoegger
 */
public class ReportMonthPayslip {

  // employee
  private String m_addressLine1;
  private String m_addressLine2;
  private String m_addressLine3;
  private String m_title;
  private String m_date;
  private String m_hoursInPeriod;
  private String m_hourWage;
  private String m_bruttoWage;
  private String m_wage;
  private String m_nettoWage;
  private String m_nettoWageRounded;
  private String m_expenseTotal;
  private String m_socialInsuracnePercentage;
  private String m_socialInsuracneAbsolute;
  private String m_vacationExtraPercentage;
  private String m_vacationExtraAbsolute;
  private String m_sourceTaxAbsolute;
  private String m_sourceTaxProcentage;
  private String m_iban;

  private Collection<ReportWorkItem> m_workItems = new ArrayList<>();
  private Collection<ReportExpenseItem> m_expenseItems = new ArrayList<>();

  // employer
  private String m_employerAddressLine1;
  private String m_employerAddressLine2;
  private String m_employerAddressLine3;
  private String m_employerEmail;
  private String m_employerPhone;

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

  public void setExpenseTotal(String expenseTotal) {
    m_expenseTotal = expenseTotal;
  }

  public String getExpenseTotal() {
    return m_expenseTotal;
  }

  public String getHourWage() {
    return m_hourWage;
  }

  public void setHourWage(String hourWage) {
    m_hourWage = hourWage;
  }

  public void setWage(String wage) {
    m_wage = wage;
  }

  public String getWage() {
    return m_wage;
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

  public void setWorkItems(Collection<ReportWorkItem> workItems) {
    m_workItems = workItems;
  }

  public Collection<ReportWorkItem> getWorkItems() {
    return m_workItems;
  }

  public void setExpenseItems(Collection<ReportExpenseItem> expenseItems) {
    m_expenseItems = expenseItems;
  }

  public Collection<ReportExpenseItem> getExpenseItems() {
    return m_expenseItems;
  }

  public String getEmployerAddressLine1() {
    return m_employerAddressLine1;
  }

  public void setEmployerAddressLine1(String employerAddressLine1) {
    m_employerAddressLine1 = employerAddressLine1;
  }

  public String getEmployerAddressLine2() {
    return m_employerAddressLine2;
  }

  public void setEmployerAddressLine2(String employerAddressLine2) {
    m_employerAddressLine2 = employerAddressLine2;
  }

  public String getEmployerAddressLine3() {
    return m_employerAddressLine3;
  }

  public void setEmployerAddressLine3(String employerAddressLine3) {
    m_employerAddressLine3 = employerAddressLine3;
  }

  public String getEmployerEmail() {
    return m_employerEmail;
  }

  public void setEmployerEmail(String employerEmail) {
    m_employerEmail = employerEmail;
  }

  public String getEmployerPhone() {
    return m_employerPhone;
  }

  public void setEmployerPhone(String employerPhone) {
    m_employerPhone = employerPhone;
  }

}

package org.ch.ahoegger.docbox.jasper.bean;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

/**
 * <h3>{@link TestFactory}</h3>
 *
 * @author Andreas Hoegger
 */
public class TestFactory {

  static public final Locale CH = new Locale("de", "CH");

  public static Collection<ReportMonthPayslip> generateCollection() {
    List<ReportWorkItem> workItems = new ArrayList<>();
    workItems.add(createWorkItemBean("12. Dez 2016", 5.5));
    workItems.add(createWorkItemBean("17. Dez 2016", 2));
    workItems.add(createWorkItemBean("07. Dez 2016", 0.25));

    List<ReportExpenseItem> expenseItems = new ArrayList<>();
    expenseItems.add(createExpenseItemBean("10. Dez 2016", "Mr Propper", 12.25));
    expenseItems.add(createExpenseItemBean("24. Dez 2016", "Cleaner for the window outside of the balkony", 124.25));
    expenseItems.add(createExpenseItemBean("31. Dez 2016", "New Years Prosecco", 33.25));

    return Arrays.asList(createAccount("Lohnabrechnung Dezember 2016", "6.251", "-2.50", "8.5", workItems, expenseItems));
  }

  private static ReportMonthPayslip createAccount(String title, String insuranceRate, String insuranceAbs, String vacationRate, List<ReportWorkItem> workItems, List<ReportExpenseItem> expenseItems) {
    ReportMonthPayslip account = new ReportMonthPayslip();
    account.setAddressLine1("Hans Muster");
    account.setAddressLine2("Bergstrasse 5a");
    account.setAddressLine3("5706 Boniswil");
    account.setDate("15.12.2016");
    account.setTitle(title);
    account.setSocialInsuracnePercentage(insuranceRate);
    account.setSocialInsuracneAbsolute(insuranceAbs);
    account.setVacationExtraPercentage("8.5");
    account.setVacationExtraAbsolute("9.50");
    account.setHoursInPeriod("5.5");
    account.setHourWage("26.00");
    account.setIban("CH12364216564215-56");
    account.setBruttoWage("130.00");
    account.setNettoWage("126.28");
    account.setNettoWageRounded("126.30");
    account.setSourceTaxAbsolute("-12.05");
    account.setSourceTaxProcentage("5");
    account.setWorkItems(workItems);
    account.setExpenseItems(expenseItems);
    account.setExpenseTotal("322.35");
    account.setEmployerAddressLine1("Bart Employer & Mel Chef");
    account.setEmployerAddressLine2("Sunset Street 23a");
    account.setEmployerAddressLine3("Santa Sunshine, CA 59872-15");
    account.setEmployerEmail("bart.employer.sunshine");
    account.setEmployerPhone("+1 (0)568 054 21");
    return account;
  }

  private static ReportWorkItem createWorkItemBean(String date, double hours) {
    NumberFormat numFormat = NumberFormat.getInstance(CH);
    numFormat.setMaximumFractionDigits(2);
    numFormat.setMinimumFractionDigits(2);

    ReportWorkItem result = new ReportWorkItem();
    result.setDate(date);
    result.setHours(numFormat.format(hours));
    return result;
  }

  private static ReportExpenseItem createExpenseItemBean(String date, String text, double amount) {
    NumberFormat numFormat = NumberFormat.getInstance(CH);
    numFormat.setMaximumFractionDigits(2);
    numFormat.setMinimumFractionDigits(2);

    ReportExpenseItem item = new ReportExpenseItem();
    item.setAmount(numFormat.format(amount));
    item.setDate(date);
    item.setText(text);
    return item;
  }
}

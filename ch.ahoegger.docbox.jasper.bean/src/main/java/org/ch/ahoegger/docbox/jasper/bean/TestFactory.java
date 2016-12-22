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
    workItems.add(createBean("12. Dez 2016", 5.5));
    workItems.add(createBean("17. Dez 2016", 2));
    workItems.add(createBean("07. Dez 2016", 0.25));
    return Arrays.asList(createAccount("Lohnabrechnung Dezember 2016", "6.251", "-2.50", "8.5", workItems));
  }

  private static ReportMonthPayslip createAccount(String title, String insuranceRate, String insuranceAbs, String vacationRate, List<ReportWorkItem> workItems) {
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
    account.setEmployerAddressLine1("Bart Employer & Mel Chef");
    account.setEmployerAddressLine2("Sunset Street 23a");
    account.setEmployerAddressLine3("Santa Sunshine, CA 59872-15");
    account.setEmployerEmail("bart.employer.sunshine");
    account.setEmployerPhone("+1 (0)568 054 21");
    return account;
  }

  private static ReportWorkItem createBean(String date, double hours) {
    NumberFormat numFormat = NumberFormat.getInstance(CH);
    numFormat.setMaximumFractionDigits(2);
    numFormat.setMinimumFractionDigits(2);

    ReportWorkItem result = new ReportWorkItem();
    result.setDate(date);
    result.setHours(numFormat.format(hours));
    return result;
  }
}

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

  public static Collection<Account> generateCollection() {
    List<EntityBean> result = new ArrayList<>();
    result.add(createBean("12. Dez 2016", 5.5));
    result.add(createBean("17. Dez 2016", 2));
    result.add(createBean("07. Dez 2016", 0.25));
    return Arrays.asList(createAccount("Lohnabrechnung Dezember 2016", "6.251", "-2.50", "8.5", result));
  }

  private static Account createAccount(String title, String insuranceRate, String insuranceAbs, String vacationRate, List<EntityBean> entities) {
    Account account = new Account();
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
    account.setNettoWage("126.30");
    account.setSourceTaxAbsolute("-12.05");
    account.setSourceTaxProcentage("5");
    account.setEntities(entities);
    return account;
  }

  private static EntityBean createBean(String date, double hours) {
    NumberFormat numFormat = NumberFormat.getInstance(CH);
    numFormat.setMaximumFractionDigits(2);
    numFormat.setMinimumFractionDigits(2);

    EntityBean result = new EntityBean();
    result.setDate(date);
    result.setHours(numFormat.format(hours));
    return result;
  }
}

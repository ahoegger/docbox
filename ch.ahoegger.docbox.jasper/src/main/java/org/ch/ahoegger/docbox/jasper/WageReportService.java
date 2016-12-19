package org.ch.ahoegger.docbox.jasper;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.ch.ahoegger.docbox.jasper.bean.Account;
import org.ch.ahoegger.docbox.jasper.bean.EntityBean;
import org.eclipse.scout.rt.platform.ApplicationScoped;
import org.eclipse.scout.rt.platform.config.CONFIG;
import org.eclipse.scout.rt.platform.util.CollectionUtility;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@ApplicationScoped
public class WageReportService {

  private Locale DE_CH = new Locale("de", "CH");
  private DateTimeFormatter m_dateFormatter;
  private NumberFormat m_formatFloat2FractionDigits;
  private NumberFormat m_simpleNumberFormt;

  @PostConstruct
  protected void initFormatters() {
    m_dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    m_formatFloat2FractionDigits = NumberFormat.getInstance(DE_CH);
    m_formatFloat2FractionDigits.setMaximumFractionDigits(2);
    m_formatFloat2FractionDigits.setMinimumFractionDigits(2);

    m_simpleNumberFormt = NumberFormat.getInstance(DE_CH);
  }

  public byte[] createReport(String title, String addressLine1, String addressLine2, String addressLine3, LocalDate date, List<Entity> entities, BigDecimal hourWage, String iban) {
    Account account = new Account();
    account.setTitle(title);
    account.setAddressLine1(addressLine1);
    account.setAddressLine2(addressLine2);
    account.setAddressLine3(addressLine3);
    account.setDate(date.format(m_dateFormatter));
    List<EntityBean> entityBeans = entities.stream().sorted((e1, e2) -> e1.getDate().compareTo(e2.getDate()))
        .map(e -> {
          EntityBean eb = new EntityBean();
          eb.setDate(e.getDate().format(m_dateFormatter));
          eb.setHours(m_formatFloat2FractionDigits.format(e.getHoursWorked()));
          return eb;

        }).collect(Collectors.toList());
    account.setEntities(entityBeans);
    account.setIban(iban);

    BigDecimal hoursInPeriod = entities.stream().map(e -> e.getHoursWorked())
        .reduce((h1, h2) -> h1.add(h2)).get();
    account = calculateWage(account, hoursInPeriod, hourWage);

//    entities.stream().map(e -> e.getHoursWorked()).redu

    InputStream billStream = getClass().getResourceAsStream("/jasper/bill.jrxml");
    InputStream billSubreportStream = getClass().getResourceAsStream("/jasper/bill_subreport1.jrxml");

    try {
      JasperReport jasperMasterReport = JasperCompileManager.compileReport(billStream);
      JasperReport jasperSubReport = JasperCompileManager.compileReport(billSubreportStream);
      Map<String, Object> parameters = new HashMap<>();
      parameters.put("subreportParameter", jasperSubReport);
      JRBeanCollectionDataSource connection = new JRBeanCollectionDataSource(CollectionUtility.arrayList(account));
      JasperPrint print = JasperFillManager.fillReport(jasperMasterReport, parameters, connection);
      JasperExportManager.exportReportToPdfFile(print, "D:/test.pdf");
      return JasperExportManager.exportReportToPdf(print);
    }
    catch (JRException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return null;
    }

//    JasperReport jasperSubReport = JasperCompileManager.compileReport(subReportSource);
//
//    Map<String, Object> parameters = new HashMap()<String, Object>;
//    parameters.put("subreportParameter", jasperSubReport);
//
//    JasperFillManager.fillReportToFile(jasperMasterReport, parameters, connection);
//
//    // Generate jasper print
//    JasperPrint jprint = (JasperPrint) JasperFillManager.fillReport(jasperFileName, hm, conn);
//
//    // Export pdf file
//    JasperExportManager.exportReportToPdfFile(jprint, pdfFileName);
  }

  private Account calculateWage(Account account, BigDecimal hoursInPeriod, BigDecimal hourlyWage) {
    account.setHoursInPeriod(m_formatFloat2FractionDigits.format(
        hoursInPeriod));
    account.setHourWage(m_formatFloat2FractionDigits.format(hourlyWage));
    BigDecimal bruttoWage = hoursInPeriod.multiply(hourlyWage);
    BigDecimal nettoWage = new BigDecimal(bruttoWage.doubleValue());
    account.setBruttoWage(m_formatFloat2FractionDigits.format(bruttoWage));
    BigDecimal socialSecurityPercentage = CONFIG.getPropertyValue(PayslipProperties.SocialInsurancePercentageProperty.class);
    account.setSocialInsuracnePercentage(m_simpleNumberFormt.format(socialSecurityPercentage));
    BigDecimal socialSecurityAbs = socialSecurityPercentage.divide(BigDecimal.valueOf(-100.0)).multiply(bruttoWage);
    nettoWage = nettoWage.add(socialSecurityAbs);
    account.setSocialInsuracneAbsolute(m_formatFloat2FractionDigits.format(socialSecurityAbs));
    BigDecimal sourceTaxPercentage = CONFIG.getPropertyValue(PayslipProperties.SourceTaxPercentageProperty.class);
    account.setSourceTaxProcentage(m_simpleNumberFormt.format(sourceTaxPercentage));
    BigDecimal sourceTaxAbs = sourceTaxPercentage.divide(BigDecimal.valueOf(-100.0)).multiply(bruttoWage);
    nettoWage = nettoWage.add(sourceTaxAbs);
    account.setSourceTaxAbsolute(m_formatFloat2FractionDigits.format(sourceTaxAbs));

    BigDecimal vacationExtraPercentage = CONFIG.getPropertyValue(PayslipProperties.VacationExtraPercentageProperty.class);
    account.setVacationExtraPercentage(m_simpleNumberFormt.format(vacationExtraPercentage));
    BigDecimal vacationExtraAbs = vacationExtraPercentage.divide(BigDecimal.valueOf(100.0)).multiply(bruttoWage);
    nettoWage = nettoWage.add(vacationExtraAbs);
    account.setVacationExtraAbsolute(m_formatFloat2FractionDigits.format(vacationExtraAbs));
    account.setNettoWage(m_formatFloat2FractionDigits.format(nettoWage));
    account.setNettoWageRounded(m_formatFloat2FractionDigits.format(financeRound(nettoWage, BigDecimal.valueOf(0.05), RoundingMode.UP)));
    return account;

  }

  public BigDecimal financeRound(BigDecimal value, BigDecimal increment,
      RoundingMode roundingMode) {
    if (increment.signum() == 0) {
      // 0 increment does not make much sense, but prevent division by 0
      return value;
    }
    else {
      BigDecimal divided = value.divide(increment, 0, roundingMode);
      BigDecimal result = divided.multiply(increment);
      return result;
    }
  }
}

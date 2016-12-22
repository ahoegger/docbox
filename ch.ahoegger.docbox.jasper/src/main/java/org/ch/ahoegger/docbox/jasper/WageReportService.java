package org.ch.ahoegger.docbox.jasper;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.ch.ahoegger.docbox.jasper.bean.ReportExpenseItem;
import org.ch.ahoegger.docbox.jasper.bean.ReportMonthPayslip;
import org.ch.ahoegger.docbox.jasper.bean.ReportWorkItem;
import org.ch.ahoegger.docbox.jasper.bean.WageCalculation;
import org.eclipse.scout.rt.platform.ApplicationScoped;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.PlatformExceptionTranslator;
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

  @PostConstruct
  protected void initFormatters() {
    m_dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    m_formatFloat2FractionDigits = NumberFormat.getInstance(DE_CH);
    m_formatFloat2FractionDigits.setMaximumFractionDigits(2);
    m_formatFloat2FractionDigits.setMinimumFractionDigits(2);

  }

  public byte[] createMonthlyReport(String title, String addressLine1, String addressLine2, String addressLine3, LocalDate date, String iban, BigDecimal hourWage, WageCalculation wageCalculation,
      String employerAddressLine1, String employerAddressLine2, String employerAddressLine3, String employerEmail, String employerPhone) {
    ReportMonthPayslip account = new ReportMonthPayslip();
    account.setTitle(title);
    account.setAddressLine1(addressLine1);
    account.setAddressLine2(addressLine2);
    account.setAddressLine3(addressLine3);
    account.setDate(date.format(m_dateFormatter));
    account.setWorkItems(wageCalculation.getWorkItems()
        .stream()
        .map(in -> {
          ReportWorkItem out = new ReportWorkItem();
          out.setDate(in.getDate().format(m_dateFormatter));
          out.setHours(m_formatFloat2FractionDigits.format(in.getHours()));
          out.setText(in.getText());
          return out;
        })
        .collect(Collectors.toList()));

    account.setExpenses(wageCalculation.getExpenses()
        .stream()
        .map(in -> {
          ReportExpenseItem out = new ReportExpenseItem();
          out.setDate(in.getDate().format(m_dateFormatter));
          out.setAmount(m_formatFloat2FractionDigits.format(in.getAmount()));
          out.setText(in.getText());
          return out;
        })
        .collect(Collectors.toList()));

    account.setIban(iban);
    // calculations
    account.setBruttoWage(m_formatFloat2FractionDigits.format(wageCalculation.getBruttoWage()));
    account.setHoursInPeriod(m_formatFloat2FractionDigits.format(wageCalculation.getHoursTotal()));
    account.setHourWage(m_formatFloat2FractionDigits.format(hourWage));
    account.setNettoWage(m_formatFloat2FractionDigits.format(wageCalculation.getNettoWage()));
    account.setNettoWageRounded(m_formatFloat2FractionDigits.format(financeRound(wageCalculation.getNettoWage(), BigDecimal.valueOf(0.05), RoundingMode.UP)));
    account.setSocialInsuracneAbsolute(m_formatFloat2FractionDigits.format(wageCalculation.getSocialSecuityTax()));
    account.setSocialInsuracnePercentage(m_formatFloat2FractionDigits.format(wageCalculation.getSocialSecuityTaxRelative().multiply(BigDecimal.valueOf(100.0))));
    account.setSourceTaxAbsolute(m_formatFloat2FractionDigits.format(wageCalculation.getSourceTax()));
    account.setSourceTaxProcentage(m_formatFloat2FractionDigits.format(wageCalculation.getSourceTaxRelative().multiply(BigDecimal.valueOf(100.0))));
    account.setVacationExtraAbsolute(m_formatFloat2FractionDigits.format(wageCalculation.getVacationExtra()));
    account.setVacationExtraPercentage(m_formatFloat2FractionDigits.format(wageCalculation.getVacationExtraRelative().multiply(BigDecimal.valueOf(100.0))));
    // employer
    account.setEmployerAddressLine1(employerAddressLine1);
    account.setEmployerAddressLine2(employerAddressLine2);
    account.setEmployerAddressLine3(employerAddressLine3);
    account.setEmployerEmail(employerEmail);
    account.setEmployerPhone(employerPhone);

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
      throw BEANS.get(PlatformExceptionTranslator.class).translate(e);
    }

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

package ch.ahoegger.docbox.server.hr.billing;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.ch.ahoegger.docbox.jasper.bean.ReportExpenseItem;
import org.ch.ahoegger.docbox.jasper.bean.ReportMonthPayslip;
import org.ch.ahoegger.docbox.jasper.bean.ReportWorkItem;
import org.eclipse.scout.rt.platform.ApplicationScoped;

import ch.ahoegger.docbox.server.hr.statement.StatementBean;
import ch.ahoegger.docbox.shared.hr.billing.PayslipFormData;
import ch.ahoegger.docbox.shared.hr.employee.EmployeeFormData;
import ch.ahoegger.docbox.shared.hr.employer.EmployerFormData;
import ch.ahoegger.docbox.shared.hr.entity.EntityTablePageData.EntityTableRowData;
import ch.ahoegger.docbox.shared.hr.tax.TaxCodeType;
import ch.ahoegger.docbox.shared.util.LocalDateUtility;

/**
 * <h3>{@link MonthlyReportBeanMapper}</h3>
 *
 * @author aho
 */
@ApplicationScoped
public class MonthlyReportBeanMapper {

  private Locale DE_CH = new Locale("de", "CH");
  private DateTimeFormatter m_dateFormatter;
  private DateTimeFormatter m_dateFormatterLong;
  private NumberFormat m_formatFloat2FractionDigits;
  private NumberFormat m_formatFloat3FractionDigits;

  @PostConstruct
  protected void initFormatters() {
    m_dateFormatter = DateTimeFormatter.ofPattern("dd. MMM. yyyy", DE_CH);
    m_dateFormatterLong = DateTimeFormatter.ofPattern("dd. MMMM yyyy", DE_CH);

    m_formatFloat2FractionDigits = NumberFormat.getInstance(DE_CH);
    m_formatFloat2FractionDigits.setMaximumFractionDigits(2);
    m_formatFloat2FractionDigits.setMinimumFractionDigits(2);

    m_formatFloat3FractionDigits = NumberFormat.getInstance(DE_CH);
    m_formatFloat3FractionDigits.setMaximumFractionDigits(3);
    m_formatFloat3FractionDigits.setMinimumFractionDigits(3);

  }

  public ReportMonthPayslip map(EmployerFormData employerData, EmployeeFormData employeeData, PayslipFormData payslipData, StatementBean statementBean, List<EntityTableRowData> workItems, List<EntityTableRowData> expenseItems) {

    ReportMonthPayslip reportBean = new ReportMonthPayslip();
    reportBean.setAddressLine1(employeeData.getEmployeeBox().getFirstName().getValue() + " " + employeeData.getEmployeeBox().getLastName().getValue());
    reportBean.setAddressLine2(employeeData.getEmployeeBox().getAddressBox().getLine1().getValue());
    reportBean.setAddressLine3(employeeData.getEmployeeBox().getAddressBox().getPlz().getValue() + " " + employeeData.getEmployeeBox().getAddressBox().getCity().getValue());
    reportBean.setBruttoWage(m_formatFloat2FractionDigits.format(statementBean.getBruttoWage()));
    reportBean.setDate(LocalDateUtility.toLocalDate(statementBean.getStatementDate()).format(m_dateFormatterLong));
    reportBean.setEmployerAddressLine1(employerData.getName().getValue());
    reportBean.setEmployerAddressLine2(employerData.getAddressBox().getLine1().getValue());
    reportBean.setEmployerAddressLine3(employerData.getAddressBox().getPlz().getValue() + " " + employerData.getAddressBox().getCity().getValue());
    reportBean.setEmployerEmail(employerData.getEmail().getValue());
    reportBean.setEmployerPhone(employerData.getPhone().getValue());
    reportBean.setExpenseItems(expenseItems.stream()
        .map(in -> {
          ReportExpenseItem out = new ReportExpenseItem();
          out.setDate(LocalDateUtility.toLocalDate(in.getDate()).format(m_dateFormatter));
          out.setAmount(m_formatFloat2FractionDigits.format(in.getAmount()));
          out.setText(in.getText());
          return out;
        })
        .collect(Collectors.toList()));
    reportBean.setExpenseTotal(m_formatFloat2FractionDigits.format(statementBean.getExpenses()));
    reportBean.setHoursInPeriod(m_formatFloat2FractionDigits.format(statementBean.getWorkingHours()));
    reportBean.setHourWage(m_formatFloat2FractionDigits.format(statementBean.getHourlyWage()));
    reportBean.setIban(employeeData.getEmployeeBox().getAccountNumber().getValue());
    reportBean.setNettoWage(m_formatFloat2FractionDigits.format(statementBean.getNettoWage()));
    reportBean.setNettoWageRounded(m_formatFloat2FractionDigits.format(statementBean.getNettoWageRounded()));
    reportBean.setSocialInsuracneAbsolute(m_formatFloat2FractionDigits.format(statementBean.getSocialInsuranceTax().multiply(BigDecimal.valueOf(-1))));
    reportBean.setSocialInsuracnePercentage(m_formatFloat3FractionDigits.format(statementBean.getSocialInsuranceRate().negate()));
    reportBean.setSourceTax(TaxCodeType.SourceTax.ID.equals(employeeData.getEmploymentBox().getTaxType().getValue()));
    reportBean.setSourceTaxAbsolute(m_formatFloat2FractionDigits.format(statementBean.getSourceTax().negate()));
    reportBean.setSourceTaxProcentage(m_formatFloat2FractionDigits.format(statementBean.getSourceTaxRate().multiply(BigDecimal.valueOf(-1))));
    reportBean.setTitle(payslipData.getTitle().getValue());
    reportBean.setVacationExtraAbsolute(m_formatFloat2FractionDigits.format(statementBean.getVacationExtra()));
    reportBean.setVacationExtraPercentage(m_formatFloat2FractionDigits.format(statementBean.getVacationExtraRate()));
    reportBean.setWage(m_formatFloat2FractionDigits.format(statementBean.getWage()));
    reportBean.setWorkItems(workItems.stream()
        .map(row -> {
          ReportWorkItem out = new ReportWorkItem();
          out.setDate(LocalDateUtility.toLocalDate(row.getDate()).format(m_dateFormatter));
          out.setHours(m_formatFloat2FractionDigits.format(row.getHours()));
          out.setText(row.getText());
          return out;
        }).collect(Collectors.toList()));
    return reportBean;
  }

}

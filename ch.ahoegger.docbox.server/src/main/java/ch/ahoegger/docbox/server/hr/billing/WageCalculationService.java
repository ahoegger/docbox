package ch.ahoegger.docbox.server.hr.billing;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.ch.ahoegger.docbox.jasper.bean.Expense;
import org.ch.ahoegger.docbox.jasper.bean.WageCalculation;
import org.ch.ahoegger.docbox.jasper.bean.Work;
import org.eclipse.scout.rt.platform.ApplicationScoped;
import org.eclipse.scout.rt.shared.servicetunnel.RemoteServiceAccessDenied;

import ch.ahoegger.docbox.shared.hr.entity.EntityTablePageData.EntityTableRowData;
import ch.ahoegger.docbox.shared.hr.entity.EntityTypeCodeType.ExpenseCode;
import ch.ahoegger.docbox.shared.hr.entity.EntityTypeCodeType.WorkCode;
import ch.ahoegger.docbox.shared.util.LocalDateUtility;

/**
 * <h3>{@link WageCalculationService}</h3>
 *
 * @author aho
 */
@ApplicationScoped
public class WageCalculationService {

  @RemoteServiceAccessDenied
  public WageCalculation calculateWage(List<EntityTableRowData> entityDatas, BigDecimal hourlyWage, BigDecimal socialInsuranceRate, BigDecimal sourceTaxRate, BigDecimal vacationExtraRate) {
    List<Work> workItems = entityDatas.stream().filter(row -> WorkCode.isEqual(row.getEntityType()))
        .map(row -> new Work().widthHours(row.getHours()).withDate(LocalDateUtility.toLocalDate(row.getDate())).withText(row.getText()))
        .collect(Collectors.toList());
    List<Expense> expenses = entityDatas.stream().filter(row -> ExpenseCode.isEqual(row.getEntityType()))
        .map(row -> new Expense().withAmount(row.getAmount()).withDate(LocalDateUtility.toLocalDate(row.getDate())).withText(Optional.ofNullable(row.getText()).orElse("")))
        .collect(Collectors.toList());

    BigDecimal hoursWorked = workItems.stream()
        .map(workItem -> workItem.getHours())
        .reduce((h1, h2) -> h1.add(h2))
        .orElse(BigDecimal.ZERO)
        .setScale(2, RoundingMode.HALF_UP);

    BigDecimal expensesTotal = expenses.stream()
        .map(expense -> expense.getAmount())
        .reduce((h1, h2) -> h1.add(h2))
        .orElse(BigDecimal.ZERO)
        .setScale(2, RoundingMode.HALF_UP);

    BigDecimal wage = hoursWorked.multiply(hourlyWage);
    BigDecimal vacationExtraRelative = vacationExtraRate.divide(BigDecimal.valueOf(100.0));
    BigDecimal vacationExtra = vacationExtraRelative.multiply(wage);
    BigDecimal bruttoWage = wage.add(vacationExtra);

    BigDecimal socialSecurityInsuranceRelative = socialInsuranceRate.divide(BigDecimal.valueOf(-100.0));
    BigDecimal socialSecurityTax = socialSecurityInsuranceRelative.multiply(bruttoWage);
    BigDecimal sourceTaxRelative = sourceTaxRate.divide(BigDecimal.valueOf(-100.0));
    BigDecimal sourceTax = sourceTaxRelative.multiply(bruttoWage);
    BigDecimal nettoWage = bruttoWage.add(expensesTotal).add(socialSecurityTax).add(sourceTax);

    WageCalculation result = new WageCalculation();
    result.setWorkItems(workItems);
    result.setExpenses(expenses);
    result.setWage(wage.setScale(2, RoundingMode.HALF_UP));
    result.setBruttoWage(bruttoWage.setScale(2, RoundingMode.HALF_UP));
    result.setNettoWage(nettoWage.setScale(2, RoundingMode.HALF_UP));
    result.setExpencesTotal(expensesTotal.setScale(2, RoundingMode.HALF_UP));
    result.setHoursTotal(hoursWorked.setScale(2, RoundingMode.HALF_UP));
    result.setSocialSecuityTaxRelative(socialSecurityInsuranceRelative);
    result.setSocialSecuityTax(socialSecurityTax.setScale(2, RoundingMode.HALF_UP));
    result.setSourceTaxRelative(sourceTaxRelative);
    result.setSourceTax(sourceTax.setScale(2, RoundingMode.HALF_UP));
    result.setVacationExtraRelative(vacationExtraRelative);
    result.setVacationExtra(vacationExtra.setScale(2, RoundingMode.HALF_UP));
    return result;
  }
}

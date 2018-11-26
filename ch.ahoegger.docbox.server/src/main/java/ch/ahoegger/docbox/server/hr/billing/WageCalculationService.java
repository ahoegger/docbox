package ch.ahoegger.docbox.server.hr.billing;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.eclipse.scout.rt.platform.ApplicationScoped;
import org.eclipse.scout.rt.shared.servicetunnel.RemoteServiceAccessDenied;

import ch.ahoegger.docbox.server.util.BigDecimalUtilitiy;
import ch.ahoegger.docbox.shared.hr.tax.TaxCodeType;

/**
 * <h3>{@link WageCalculationService}</h3>
 *
 * @author Andreas Hoegger
 */
@ApplicationScoped
public class WageCalculationService {

  @RemoteServiceAccessDenied
  public WageCalculationResult calculateWage(WageCalculationInput input) {
    WageCalculationResult result = new WageCalculationResult()
        .withWorkingHours(input.getWorkEntities().stream()
            .map(work -> work.getHours())
            .reduce((h1, h2) -> h1.add(h2))
            .orElse(BigDecimal.ZERO)
            .setScale(2, RoundingMode.HALF_UP))
        .withExpenses(input.getExpenseEntities().stream()
            .map(expense -> expense.getAmount())
            .reduce((h1, h2) -> h1.add(h2))
            .orElse(BigDecimal.ZERO)
            .setScale(2, RoundingMode.HALF_UP));

    result.withWage(result.getWorkingHours().multiply(input.getHourlyWage()).setScale(2, RoundingMode.HALF_UP));
    result.withVacationExtra(input.getVacationExtraRate().divide(BigDecimal.valueOf(100.0)).multiply(result.getWage()).setScale(2, RoundingMode.HALF_UP));
    result.withBruttoWage(result.getWage().add(result.getVacationExtra()).setScale(2, RoundingMode.HALF_UP));
    result.withSocialInsuranceTax(result.getBruttoWage().multiply(input.getSocialInsuranceRate().divide(BigDecimal.valueOf(100.0))).setScale(2, RoundingMode.HALF_UP));
    if (TaxCodeType.SourceTax.ID.equals(input.getTaxType())) {
      result.withSourceTax(result.getBruttoWage().multiply(input.getSourceTaxRate().divide(BigDecimal.valueOf(100.0))).setScale(2, RoundingMode.HALF_UP));
    }
    else {
      result.withSourceTax(BigDecimal.ZERO);
    }
    result.withNettoWage(result.getBruttoWage().add(result.getExpenses()).subtract(result.getSocialInsuranceTax()).subtract(result.getSourceTax()));
    result.withNettoWageRounded(BigDecimalUtilitiy.financeRound(result.getNettoWage(), BigDecimal.valueOf(0.05), RoundingMode.UP));

    return result;

  }

}

package org.ch.ahoegger.docbox.jasper;

import java.math.BigDecimal;

/**
 * <h3>{@link PayslipProperties}</h3>
 *
 * @author Andreas Hoegger
 */
public final class PayslipProperties {

  public static final class SocialInsurancePercentageProperty extends AbstractProcentageProperty {
    @Override
    public String getKey() {
      return "docbox.report.payslip.insurancePercentage";
    }

    @Override
    protected BigDecimal getDefaultValue() {
      return new BigDecimal(6.225);
    }
  }

  public static final class VacationExtraPercentageProperty extends AbstractProcentageProperty {
    @Override
    public String getKey() {
      return "docbox.report.payslip.vacationExtraPercentage";
    }

    @Override
    protected BigDecimal getDefaultValue() {
      return new BigDecimal(8.33);
    }
  }

  public static final class SourceTaxPercentageProperty extends AbstractProcentageProperty {
    @Override
    public String getKey() {
      return "docbox.report.payslip.SourceTaxPercentage";
    }

    @Override
    protected BigDecimal getDefaultValue() {
      return new BigDecimal(5.0);
    }
  }
}

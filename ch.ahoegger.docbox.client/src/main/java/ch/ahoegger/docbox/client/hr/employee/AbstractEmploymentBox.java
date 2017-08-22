package ch.ahoegger.docbox.client.hr.employee;

import java.math.BigDecimal;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.ui.form.fields.bigdecimalfield.AbstractBigDecimalField;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.shared.TEXTS;

import ch.ahoegger.docbox.or.definition.table.IEmployeeTable;
import ch.ahoegger.docbox.shared.hr.employee.AbstractEmploymentBoxData;

/**
 * <h3>{@link AbstractEmploymentBox}</h3>
 *
 * @author aho
 */
@FormData(value = AbstractEmploymentBoxData.class, defaultSubtypeSdkCommand = FormData.DefaultSubtypeSdkCommand.CREATE, sdkCommand = FormData.SdkCommand.CREATE)
public class AbstractEmploymentBox extends AbstractGroupBox {

  @Override
  protected String getConfiguredLabel() {
    return TEXTS.get("Employment");
  }

  public SourceTaxRateField getSourceTaxRateField() {
    return getFieldByClass(SourceTaxRateField.class);
  }

  public VacationExtraRateField getVacationExtraRateField() {
    return getFieldByClass(VacationExtraRateField.class);
  }

  public SocialInsuranceRateField getSocialInsuranceRateField() {
    return getFieldByClass(SocialInsuranceRateField.class);
  }

  public HourlyWageField getHourlyWageField() {
    return getFieldByClass(HourlyWageField.class);
  }

  @Order(1000)
  public class HourlyWageField extends AbstractBigDecimalField {
    @Override
    protected String getConfiguredLabel() {
      return TEXTS.get("HourlyWage");
    }

    @Override
    protected BigDecimal getConfiguredMinValue() {
      return IEmployeeTable.HOURLY_WAGE_MIN;
    }

    @Override
    protected BigDecimal getConfiguredMaxValue() {
      return IEmployeeTable.HOURLY_WAGE_MAX;
    }
  }

  @Order(2000)
  public class SocialInsuranceRateField extends AbstractBigDecimalField {
    @Override
    protected String getConfiguredLabel() {
      return TEXTS.get("SoucialInsuranceRate");
    }

    @Override
    protected int getConfiguredMaxFractionDigits() {
      return 3;
    }

    @Override
    protected BigDecimal getConfiguredMinValue() {
      return IEmployeeTable.SOCIAL_INSURANCE_RATE_MIN;
    }

    @Override
    protected BigDecimal getConfiguredMaxValue() {
      return IEmployeeTable.SOCIAL_INSURANCE_RATE_MAX;
    }
  }

  @Order(3000)
  public class SourceTaxRateField extends AbstractBigDecimalField {
    @Override
    protected String getConfiguredLabel() {
      return TEXTS.get("SourceTax");
    }

    @Override
    protected int getConfiguredMaxFractionDigits() {
      return 3;
    }

    @Override
    protected BigDecimal getConfiguredMinValue() {
      return IEmployeeTable.SOURCE_TAX_RATE_MIN;
    }

    @Override
    protected BigDecimal getConfiguredMaxValue() {
      return IEmployeeTable.SOURCE_TAX_RATE_MAX;
    }
  }

  @Order(4000)
  public class VacationExtraRateField extends AbstractBigDecimalField {
    @Override
    protected String getConfiguredLabel() {
      return TEXTS.get("VacationExtra");
    }

    @Override
    protected int getConfiguredMaxFractionDigits() {
      return 3;
    }

    @Override
    protected BigDecimal getConfiguredMinValue() {
      return IEmployeeTable.VACATION_EXTRA_RATE_MIN;
    }

    @Override
    protected BigDecimal getConfiguredMaxValue() {
      return IEmployeeTable.VACATION_EXTRA_RATE_MAX;
    }
  }

}

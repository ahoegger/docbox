package ch.ahoegger.docbox.client.hr.billing;

import java.math.BigDecimal;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.ui.form.fields.bigdecimalfield.AbstractBigDecimalField;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.text.TEXTS;

import ch.ahoegger.docbox.or.definition.table.IStatementTable;
import ch.ahoegger.docbox.shared.hr.billing.AbstractStatementBoxData;

/**
 * <h3>{@link AbstractStatementBox}</h3>
 *
 * @author Andreas Hoegger
 */
@FormData(value = AbstractStatementBoxData.class, defaultSubtypeSdkCommand = FormData.DefaultSubtypeSdkCommand.CREATE, sdkCommand = FormData.SdkCommand.CREATE)
public abstract class AbstractStatementBox extends AbstractGroupBox {

  @Override
  protected String getConfiguredLabel() {
    return TEXTS.get("Calculation");
  }

  public SocialSecurityTaxField getSocialSecurityTaxField() {
    return getFieldByClass(SocialSecurityTaxField.class);
  }

  public SourceTaxField getSourceTaxField() {
    return getFieldByClass(SourceTaxField.class);
  }

  public VacationExtraField getVacationExtraField() {
    return getFieldByClass(VacationExtraField.class);
  }

  public WorkingHoursField getWorkingHoursField() {
    return getFieldByClass(WorkingHoursField.class);
  }

  public NettoWageField getNettoWageField() {
    return getFieldByClass(NettoWageField.class);
  }

  public BruttoWageField getBruttoWageField() {
    return getFieldByClass(BruttoWageField.class);
  }

  @Order(0)
  public class WorkingHoursField extends AbstractBigDecimalField {
    @Override
    protected String getConfiguredLabel() {
      return TEXTS.get("Hours");
    }

    @Override
    protected BigDecimal getConfiguredMinValue() {
      return IStatementTable.WORKING_HOURS_MIN;
    }

    @Override
    protected BigDecimal getConfiguredMaxValue() {
      return IStatementTable.WORKING_HOURS_MAX;
    }
  }

  @Order(1000)
  public class BruttoWageField extends AbstractBigDecimalField {
    @Override
    protected String getConfiguredLabel() {
      return TEXTS.get("BruttoWage");
    }

    @Override
    protected BigDecimal getConfiguredMinValue() {
      return IStatementTable.BRUTTO_WAGE_MIN;
    }

    @Override
    protected BigDecimal getConfiguredMaxValue() {
      return IStatementTable.BRUTTO_WAGE_MAX;
    }
  }

  @Order(2000)
  public class NettoWageField extends AbstractBigDecimalField {
    @Override
    protected String getConfiguredLabel() {
      return TEXTS.get("NettoWage");
    }

    @Override
    protected BigDecimal getConfiguredMinValue() {
      return IStatementTable.NETTO_WAGE_MIN;
    }

    @Override
    protected BigDecimal getConfiguredMaxValue() {
      return IStatementTable.NETTO_WAGE_MAX;
    }
  }

  @Order(3000)
  public class SocialSecurityTaxField extends AbstractBigDecimalField {
    @Override
    protected String getConfiguredLabel() {
      return TEXTS.get("SocialSecurityTax");
    }

    @Override
    protected BigDecimal getConfiguredMinValue() {
      return IStatementTable.SOCIAL_SECURITY_TAX_MIN;
    }

    @Override
    protected BigDecimal getConfiguredMaxValue() {
      return IStatementTable.SOCIAL_SECURITY_TAX_MAX;
    }
  }

  @Order(4000)
  public class SourceTaxField extends AbstractBigDecimalField {
    @Override
    protected String getConfiguredLabel() {
      return TEXTS.get("SourceTax");
    }

    @Override
    protected BigDecimal getConfiguredMinValue() {
      return IStatementTable.SOURCE_TAX_MIN;
    }

    @Override
    protected BigDecimal getConfiguredMaxValue() {
      return IStatementTable.SOURCE_TAX_MAX;
    }
  }

  @Order(5000)
  public class VacationExtraField extends AbstractBigDecimalField {
    @Override
    protected String getConfiguredLabel() {
      return TEXTS.get("VacationExtra");
    }

    @Override
    protected BigDecimal getConfiguredMinValue() {
      return IStatementTable.VACATION_EXTRA_TAX_MIN;
    }

    @Override
    protected BigDecimal getConfiguredMaxValue() {
      return IStatementTable.VACATION_EXTRA_TAX_MAX;
    }
  }

}

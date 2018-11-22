package ch.ahoegger.docbox.client.hr.billing;

import java.math.BigDecimal;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.ui.form.fields.bigdecimalfield.AbstractBigDecimalField;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.tablefield.AbstractTableField;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.text.TEXTS;

import ch.ahoegger.docbox.client.hr.entity.AbstractEntityTable;
import ch.ahoegger.docbox.or.definition.table.IPayslipTable;
import ch.ahoegger.docbox.shared.hr.billing.AbstractPayslipBoxData;

/**
 * <h3>{@link AbstractPayslipCalculationBox}</h3>
 *
 * @author Andreas Hoegger
 */
@FormData(value = AbstractPayslipBoxData.class, defaultSubtypeSdkCommand = FormData.DefaultSubtypeSdkCommand.CREATE, sdkCommand = FormData.SdkCommand.CREATE)
public abstract class AbstractPayslipCalculationBox extends AbstractGroupBox {

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

  public EntitiesField getEntitiesField() {
    return getFieldByClass(EntitiesField.class);
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
      return IPayslipTable.WORKING_HOURS_MIN;
    }

    @Override
    protected BigDecimal getConfiguredMaxValue() {
      return IPayslipTable.WORKING_HOURS_MAX;
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
      return IPayslipTable.BRUTTO_WAGE_MIN;
    }

    @Override
    protected BigDecimal getConfiguredMaxValue() {
      return IPayslipTable.BRUTTO_WAGE_MAX;
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
      return IPayslipTable.NETTO_WAGE_MIN;
    }

    @Override
    protected BigDecimal getConfiguredMaxValue() {
      return IPayslipTable.NETTO_WAGE_MAX;
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
      return IPayslipTable.SOCIAL_SECURITY_TAX_MIN;
    }

    @Override
    protected BigDecimal getConfiguredMaxValue() {
      return IPayslipTable.SOCIAL_SECURITY_TAX_MAX;
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
      return IPayslipTable.SOURCE_TAX_MIN;
    }

    @Override
    protected BigDecimal getConfiguredMaxValue() {
      return IPayslipTable.SOURCE_TAX_MAX;
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
      return IPayslipTable.VACATION_EXTRA_TAX_MIN;
    }

    @Override
    protected BigDecimal getConfiguredMaxValue() {
      return IPayslipTable.VACATION_EXTRA_TAX_MAX;
    }
  }

  @Order(6000)
  public class EntitiesField extends AbstractTableField<EntitiesField.Table> {

    @Override
    protected String getConfiguredLabel() {
      return TEXTS.get("Entities");
    }

    @Override
    protected byte getConfiguredLabelPosition() {
      return LABEL_POSITION_TOP;
    }

    @Override
    protected int getConfiguredGridW() {
      return 2;
    }

    @Override
    protected int getConfiguredGridH() {
      return 6;
    }

    public class Table extends AbstractEntityTable {

    }
  }

}

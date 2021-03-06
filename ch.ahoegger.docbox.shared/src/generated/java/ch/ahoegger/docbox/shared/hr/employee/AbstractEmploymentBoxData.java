package ch.ahoegger.docbox.shared.hr.employee;

import java.math.BigDecimal;

import javax.annotation.Generated;

import org.eclipse.scout.rt.shared.data.form.fields.AbstractFormFieldData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;

/**
 * <b>NOTE:</b><br>
 * This class is auto generated by the Scout SDK. No manual modifications recommended.
 */
@Generated(value = "ch.ahoegger.docbox.client.hr.employee.AbstractEmploymentBox", comments = "This class is auto generated by the Scout SDK. No manual modifications recommended.")
public class AbstractEmploymentBoxData extends AbstractFormFieldData {

  private static final long serialVersionUID = 1L;

  public HourlyWage getHourlyWage() {
    return getFieldByClass(HourlyWage.class);
  }

  public PensionsFund getPensionsFund() {
    return getFieldByClass(PensionsFund.class);
  }

  public ReducedLunch getReducedLunch() {
    return getFieldByClass(ReducedLunch.class);
  }

  public SocialInsuranceRate getSocialInsuranceRate() {
    return getFieldByClass(SocialInsuranceRate.class);
  }

  public SourceTaxRate getSourceTaxRate() {
    return getFieldByClass(SourceTaxRate.class);
  }

  public TaxType getTaxType() {
    return getFieldByClass(TaxType.class);
  }

  public VacationExtraRate getVacationExtraRate() {
    return getFieldByClass(VacationExtraRate.class);
  }

  public static class HourlyWage extends AbstractValueFieldData<BigDecimal> {

    private static final long serialVersionUID = 1L;
  }

  public static class PensionsFund extends AbstractValueFieldData<BigDecimal> {

    private static final long serialVersionUID = 1L;
  }

  public static class ReducedLunch extends AbstractValueFieldData<Boolean> {

    private static final long serialVersionUID = 1L;
  }

  public static class SocialInsuranceRate extends AbstractValueFieldData<BigDecimal> {

    private static final long serialVersionUID = 1L;
  }

  public static class SourceTaxRate extends AbstractValueFieldData<BigDecimal> {

    private static final long serialVersionUID = 1L;
  }

  public static class TaxType extends AbstractValueFieldData<BigDecimal> {

    private static final long serialVersionUID = 1L;
  }

  public static class VacationExtraRate extends AbstractValueFieldData<BigDecimal> {

    private static final long serialVersionUID = 1L;
  }
}

package ch.ahoegger.docbox.shared.hr.employee;

import java.math.BigDecimal;
import java.util.Set;

import javax.annotation.Generated;

import org.eclipse.scout.rt.platform.util.TriState;
import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;
import org.eclipse.scout.rt.shared.data.form.properties.AbstractPropertyData;

/**
 * <b>NOTE:</b><br>
 * This class is auto generated by the Scout SDK. No manual modifications recommended.
 */
@Generated(value = "ch.ahoegger.docbox.client.hr.employee.EmployeeTaxGroupSearchForm", comments = "This class is auto generated by the Scout SDK. No manual modifications recommended.")
public class EmployeeTaxGroupSearchFormData extends AbstractFormData {

  private static final long serialVersionUID = 1L;

  public Employee getEmployee() {
    return getFieldByClass(Employee.class);
  }

  /**
   * access method for property EmployeeIds.
   */
  public Set<BigDecimal> getEmployeeIds() {
    return getEmployeeIdsProperty().getValue();
  }

  /**
   * access method for property EmployeeIds.
   */
  public void setEmployeeIds(Set<BigDecimal> employeeIds) {
    getEmployeeIdsProperty().setValue(employeeIds);
  }

  public EmployeeIdsProperty getEmployeeIdsProperty() {
    return getPropertyByClass(EmployeeIdsProperty.class);
  }

  /**
   * access method for property EmployerTaxGroupId.
   */
  public BigDecimal getEmployerTaxGroupId() {
    return getEmployerTaxGroupIdProperty().getValue();
  }

  /**
   * access method for property EmployerTaxGroupId.
   */
  public void setEmployerTaxGroupId(BigDecimal employerTaxGroupId) {
    getEmployerTaxGroupIdProperty().setValue(employerTaxGroupId);
  }

  public EmployerTaxGroupIdProperty getEmployerTaxGroupIdProperty() {
    return getPropertyByClass(EmployerTaxGroupIdProperty.class);
  }

  public FinalizedRadioGroup getFinalizedRadioGroup() {
    return getFieldByClass(FinalizedRadioGroup.class);
  }

  public TaxGroup getTaxGroup() {
    return getFieldByClass(TaxGroup.class);
  }

  public static class Employee extends AbstractValueFieldData<BigDecimal> {

    private static final long serialVersionUID = 1L;
  }

  public static class EmployeeIdsProperty extends AbstractPropertyData<Set<BigDecimal>> {

    private static final long serialVersionUID = 1L;
  }

  public static class EmployerTaxGroupIdProperty extends AbstractPropertyData<BigDecimal> {

    private static final long serialVersionUID = 1L;
  }

  public static class FinalizedRadioGroup extends AbstractValueFieldData<TriState> {

    private static final long serialVersionUID = 1L;
  }

  public static class TaxGroup extends AbstractValueFieldData<BigDecimal> {

    private static final long serialVersionUID = 1L;
  }
}

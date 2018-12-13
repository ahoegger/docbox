package ch.ahoegger.docbox.client.hr.employee;

import java.math.BigDecimal;

import org.eclipse.scout.rt.client.ui.form.fields.smartfield.AbstractSmartField;
import org.eclipse.scout.rt.platform.text.TEXTS;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;

import ch.ahoegger.docbox.shared.hr.employee.EmployeeLookupCall;

/**
 * <h3>{@link AbstractEmployeeSmartField}</h3>
 *
 * @author aho
 */
public abstract class AbstractEmployeeSmartField extends AbstractSmartField<BigDecimal> {

  @Override
  protected String getConfiguredLabel() {
    return TEXTS.get("Employee");
  }

  @Override
  protected Class<? extends ILookupCall<BigDecimal>> getConfiguredLookupCall() {
    return EmployeeLookupCall.class;
  }
}

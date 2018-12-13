package ch.ahoegger.docbox.client.templates;

import java.math.BigDecimal;
import java.util.stream.Collectors;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.ui.form.fields.IValueField;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.listbox.AbstractListBox;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.text.TEXTS;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;

import ch.ahoegger.docbox.shared.hr.employee.EmployeeLookupCall;
import ch.ahoegger.docbox.shared.hr.employer.EmployerLookupCall;
import ch.ahoegger.docbox.shared.template.AbstractLinkingBoxData;

/**
 * <h3>{@link AbstractLinkingGroupBox}</h3>
 *
 * @author aho
 */
@FormData(value = AbstractLinkingBoxData.class, defaultSubtypeSdkCommand = FormData.DefaultSubtypeSdkCommand.CREATE, sdkCommand = FormData.SdkCommand.CREATE)
public abstract class AbstractLinkingGroupBox extends AbstractGroupBox {
  @Override
  protected String getConfiguredLabel() {
    return TEXTS.get("Linking");
  }

  @Override
  protected int getConfiguredGridColumnCount() {
    return 2;
  }

  protected Class<? extends ILookupCall<BigDecimal>> getConfiguredLookupCallEmployers() {
    return EmployerLookupCall.class;
  }

  protected Class<? extends ILookupCall<BigDecimal>> getConfiguredLookupCallEmployees() {
    return EmployeeLookupCall.class;
  }

  protected void execPrepareLookupEmployers(ILookupCall<BigDecimal> call) {

  }

  protected void execPrepareLookupEmployees(ILookupCall<BigDecimal> call) {
    EmployeeLookupCall eeLookupCall = (EmployeeLookupCall) call;
    eeLookupCall.setEmployerIds(getEmployersBox().getValue());
  }

  protected void execChangedValueEmployers() {
  }

  protected void execChangedValueEmployees() {
  }

  public EmployersBox getEmployersBox() {
    return getFieldByClass(EmployersBox.class);
  }

  public EmployeesBox getEmployeesBox() {
    return getFieldByClass(EmployeesBox.class);
  }

  @Order(1000)
  public class EmployersBox extends AbstractListBox<BigDecimal> {
    @Override
    protected String getConfiguredLabel() {
      return TEXTS.get("Employer");
    }

    @Override
    public byte getLabelPosition() {
      return LABEL_POSITION_TOP;
    }

    @Override
    protected int getConfiguredGridW() {
      return 1;
    }

    @Override
    protected int getConfiguredGridH() {
      return 4;
    }

    @Override
    protected Class<? extends ILookupCall<BigDecimal>> getConfiguredLookupCall() {
      return getConfiguredLookupCallEmployers();

    }

    @Override
    protected void execPrepareLookup(ILookupCall<BigDecimal> call) {
      execPrepareLookupEmployers(call);

    }

    @Override
    protected void execChangedValue() {
      execChangedValueEmployers();
    }

  }

  @Order(2000)
  public class EmployeesBox extends AbstractListBox<BigDecimal> {
    @Override
    protected String getConfiguredLabel() {
      return TEXTS.get("Employee");
    }

    @Override
    public byte getLabelPosition() {
      return LABEL_POSITION_TOP;
    }

    @Override
    protected int getConfiguredGridW() {
      return 1;
    }

    @Override
    protected int getConfiguredGridH() {
      return 4;
    }

    @Override
    protected Class<? extends IValueField> getConfiguredMasterField() {
      return EmployersBox.class;
    }

    @Override
    protected Class<? extends ILookupCall<BigDecimal>> getConfiguredLookupCall() {
      return getConfiguredLookupCallEmployees();

    }

    @Override
    protected void execChangedMasterValue(Object newMasterValue) {
      loadListBoxData();
      getTable().uncheckRows(getTable().getRows().stream().filter(row -> !row.isEnabled())
          .collect(Collectors.toList()));
    }

    @Override
    protected void execPrepareLookup(ILookupCall<BigDecimal> call) {
      execPrepareLookupEmployees(call);

    }

    @Override
    protected void execChangedValue() {
      execChangedValueEmployees();
    }
  }

}

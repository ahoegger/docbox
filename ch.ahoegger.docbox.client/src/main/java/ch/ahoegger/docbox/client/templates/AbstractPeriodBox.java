package ch.ahoegger.docbox.client.templates;

import java.util.Date;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.ui.form.fields.datefield.AbstractDateField;
import org.eclipse.scout.rt.client.ui.form.fields.sequencebox.AbstractSequenceBox;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.text.TEXTS;

import ch.ahoegger.docbox.shared.template.AbstractPeriodBoxData;

/**
 * <h3>{@link AbstractPeriodBox}</h3>
 *
 * @author aho
 */
@FormData(value = AbstractPeriodBoxData.class, defaultSubtypeSdkCommand = FormData.DefaultSubtypeSdkCommand.CREATE, sdkCommand = FormData.SdkCommand.CREATE)
public abstract class AbstractPeriodBox extends AbstractSequenceBox {
  @Override
  protected String getConfiguredLabel() {
    return TEXTS.get("Period");
  }

  @Override
  protected boolean getConfiguredAutoCheckFromTo() {
    return true;
  }

  @Override
  protected int getConfiguredGridW() {
    return 2;
  }

  protected boolean getConfiguredMandatoryTo() {
    return false;
  }

  protected boolean getConfiguredMandatoryFrom() {
    return false;
  }

  protected void execFromChangedValue(Date value) {
  }

  protected void execToChangedValue(Date value) {
  }

  public ToField getToField() {
    return getFieldByClass(ToField.class);
  }

  public FromField getFromField() {
    return getFieldByClass(FromField.class);
  }

  @Order(1000)
  public class FromField extends AbstractDateField {
    @Override
    protected String getConfiguredLabel() {
      return TEXTS.get("from");
    }

    @Override
    protected boolean getConfiguredMandatory() {
      return getConfiguredMandatoryFrom();
    }

    @Override
    protected void execChangedValue() {
      execFromChangedValue(getValue());
    }
  }

  @Order(2000)
  public class ToField extends AbstractDateField {
    @Override
    protected String getConfiguredLabel() {
      return TEXTS.get("to");
    }

    @Override
    protected boolean getConfiguredMandatory() {
      return getConfiguredMandatoryTo();
    }

    @Override
    protected void execChangedValue() {
      execToChangedValue(getValue());
    }

  }

}

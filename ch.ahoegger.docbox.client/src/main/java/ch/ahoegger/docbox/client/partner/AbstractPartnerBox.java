package ch.ahoegger.docbox.client.partner;

import java.util.Date;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.ui.form.fields.datefield.AbstractDateField;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.shared.TEXTS;

import ch.ahoegger.docbox.shared.partner.AbstractPartnerBoxData;
import ch.ahoegger.docbox.shared.partner.IPartnerTable;
import ch.ahoegger.docbox.shared.validation.DateValidation;

/**
 * <h3>{@link AbstractPartnerBox}</h3>
 *
 * @author Andreas Hoegger
 */
@FormData(value = AbstractPartnerBoxData.class, defaultSubtypeSdkCommand = FormData.DefaultSubtypeSdkCommand.CREATE, sdkCommand = FormData.SdkCommand.CREATE)
public abstract class AbstractPartnerBox extends AbstractGroupBox {

  @Override
  protected boolean getConfiguredBorderVisible() {
    return false;
  }

  public DescriptionField getDescriptionField() {
    return getFieldByClass(DescriptionField.class);
  }

  public StartDateField getStartDateField() {
    return getFieldByClass(StartDateField.class);
  }

  public EndDateField getEndDateField() {
    return getFieldByClass(EndDateField.class);
  }

  public NameField getNameField() {
    return getFieldByClass(NameField.class);
  }

  @Order(1000)
  public class NameField extends AbstractStringField {
    @Override
    protected String getConfiguredLabel() {
      return TEXTS.get("Name");
    }

    @Override
    protected int getConfiguredMaxLength() {
      return IPartnerTable.NAME_LENGTH;
    }

    @Override
    protected boolean getConfiguredMandatory() {
      return true;
    }
  }

  @Order(2000)
  public class StartDateField extends AbstractDateField {
    @Override
    protected String getConfiguredLabel() {
      return TEXTS.get("from");
    }

    @Override
    protected Date execValidateValue(Date rawValue) {
      DateValidation.validateFromTo(rawValue, getEndDateField().getValue());
      return rawValue;
    }
  }

  @Order(4000)
  public class EndDateField extends AbstractDateField {
    @Override
    protected String getConfiguredLabel() {
      return TEXTS.get("to");
    }

    @Override
    protected Date execValidateValue(Date rawValue) {
      DateValidation.validateFromTo(getStartDateField().getValue(), rawValue);
      return rawValue;
    }
  }

  @Order(5000)
  public class DescriptionField extends AbstractStringField {
    @Override
    protected String getConfiguredLabel() {
      return TEXTS.get("Description");
    }

    @Override
    protected int getConfiguredMaxLength() {
      return IPartnerTable.DESCRIPTION_LENGTH;
    }

    @Override
    protected int getConfiguredGridW() {
      return 2;
    }

    @Override
    protected int getConfiguredGridH() {
      return 2;
    }

    @Override
    protected boolean getConfiguredMultilineText() {
      return true;
    }
  }

}

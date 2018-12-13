package ch.ahoegger.docbox.client.administration.hr.taxgroup;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.placeholder.AbstractPlaceholderField;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.status.IStatus;
import org.eclipse.scout.rt.platform.text.TEXTS;

import ch.ahoegger.docbox.client.administration.hr.taxgroup.TaxGroupForm.MainBox.CancelButton;
import ch.ahoegger.docbox.client.administration.hr.taxgroup.TaxGroupForm.MainBox.LinkingBox;
import ch.ahoegger.docbox.client.administration.hr.taxgroup.TaxGroupForm.MainBox.NameField;
import ch.ahoegger.docbox.client.administration.hr.taxgroup.TaxGroupForm.MainBox.OkButton;
import ch.ahoegger.docbox.client.administration.hr.taxgroup.TaxGroupForm.MainBox.PeriodBox;
import ch.ahoegger.docbox.client.administration.hr.taxgroup.TaxGroupForm.MainBox.PlaceholderField;
import ch.ahoegger.docbox.client.administration.hr.taxgroup.TaxGroupForm.MainBox.StatusField;
import ch.ahoegger.docbox.client.templates.AbstractLinkingGroupBox;
import ch.ahoegger.docbox.client.templates.AbstractPeriodBox;
import ch.ahoegger.docbox.client.templates.AbstractStatusField;
import ch.ahoegger.docbox.client.util.AbstractValidatableForm;
import ch.ahoegger.docbox.or.definition.table.ITaxGroupTable;
import ch.ahoegger.docbox.shared.administration.hr.taxgroup.TaxGroupFormData;
import ch.ahoegger.docbox.shared.administration.taxgroup.ITaxGroupService;
import ch.ahoegger.docbox.shared.util.LocalDateUtility;

@FormData(value = TaxGroupFormData.class, sdkCommand = FormData.SdkCommand.CREATE)
public class TaxGroupForm extends AbstractValidatableForm {

  private BigDecimal m_taxGroupId;

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("TaxGroup");
  }

  @Override
  protected void execStored() {
    getDesktop().dataChanged(ITaxGroupEntity.ENTITY_KEY);
  }

  public void startModify() {
    startInternalExclusive(new ModifyHandler());
  }

  public void startNew() {
    startInternal(new NewHandler());
  }

  @FormData
  public void setTaxGroupId(BigDecimal taxGroupId) {
    m_taxGroupId = taxGroupId;
  }

  @FormData
  public BigDecimal getTaxGroupId() {
    return m_taxGroupId;
  }

  public CancelButton getCancelButton() {
    return getFieldByClass(CancelButton.class);
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  public PlaceholderField getPlaceholderField() {
    return getFieldByClass(PlaceholderField.class);
  }

  public PeriodBox getPeriodBox() {
    return getFieldByClass(PeriodBox.class);
  }

  public LinkingBox getLinkingBox() {
    return getFieldByClass(LinkingBox.class);
  }

  public NameField getNameField() {
    return getFieldByClass(NameField.class);
  }

  @Override
  public StatusField getStatusField() {
    return getFieldByClass(StatusField.class);
  }

  public OkButton getOkButton() {
    return getFieldByClass(OkButton.class);
  }

  protected void updateName() {
    if (getPeriodBox().getFromField().getValue() != null) {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");
      getNameField().setValue(LocalDateUtility.toLocalDate(getPeriodBox().getFromField().getValue()).format(formatter));
    }
  }

  @Order(1000)
  public class MainBox extends AbstractGroupBox {
    @Override
    protected boolean getConfiguredBorderVisible() {
      return true;
    }

    @Override
    protected String getConfiguredBorderDecoration() {
      return BORDER_DECORATION_EMPTY;
    }

    @Order(1000)
    public class StatusField extends AbstractStatusField {

    }

    @Order(2000)
    public class NameField extends AbstractStringField {
      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("Name");
      }

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }

      @Override
      protected int getConfiguredMaxLength() {
        return ITaxGroupTable.NAME_LENGTH;
      }
    }

    @Order(3000)
    public class PlaceholderField extends AbstractPlaceholderField {
    }

    @Order(4000)
    public class PeriodBox extends AbstractPeriodBox {

      @Override
      protected void execFromChangedValue(Date value) {
        if (getFromField().getValue() != null) {
          getPeriodBox().getToField().setValue(LocalDateUtility.toDate(LocalDateUtility.toLocalDate(getFromField().getValue()).with(TemporalAdjusters.lastDayOfYear())));
        }
        updateName();
      }

      @Override
      protected void execToChangedValue(Date value) {
        updateName();
      }
    }

    @Order(5000)
    public class LinkingBox extends AbstractLinkingGroupBox {

    }

    @Order(100000)
    public class OkButton extends AbstractOkButton {
    }

    @Order(101000)
    public class CancelButton extends AbstractCancelButton {
    }
  }

  public class ModifyHandler extends AbstractFormHandler {

    @Override
    protected void execLoad() {
      ITaxGroupService service = BEANS.get(ITaxGroupService.class);
      TaxGroupFormData formData = new TaxGroupFormData();
      exportFormData(formData);
      formData = service.load(formData);
      importFormData(formData);

      getLinkingBox().setVisible(false);
    }

    @Override
    protected boolean execValidate() {
      TaxGroupFormData formData = new TaxGroupFormData();
      exportFormData(formData);
      IStatus status = BEANS.get(ITaxGroupService.class).validate(formData);
      getStatusField().setValidationStatus(status);
      return status.getSeverity() != IStatus.ERROR;
    }

    @Override
    protected void execStore() {
      ITaxGroupService service = BEANS.get(ITaxGroupService.class);
      TaxGroupFormData formData = new TaxGroupFormData();
      exportFormData(formData);
      service.store(formData);
    }
  }

  public class NewHandler extends AbstractFormHandler {

    @Override
    protected void execLoad() {
      ITaxGroupService service = BEANS.get(ITaxGroupService.class);
      TaxGroupFormData formData = new TaxGroupFormData();
      exportFormData(formData);
      formData = service.prepareCreate(formData);
      importFormData(formData);
      getLinkingBox().getEmployersBox().checkAllKeys();
      getLinkingBox().getEmployeesBox().checkAllKeys();
      updateName();
    }

    @Override
    protected void execPostLoad() {
      touch();
    }

    @Override
    protected boolean execValidate() {
      TaxGroupFormData formData = new TaxGroupFormData();
      exportFormData(formData);
      IStatus status = BEANS.get(ITaxGroupService.class).validate(formData);
      getStatusField().setValidationStatus(status);
      return status.getSeverity() != IStatus.ERROR;
    }

    @Override
    protected void execStore() {
      ITaxGroupService service = BEANS.get(ITaxGroupService.class);
      TaxGroupFormData formData = new TaxGroupFormData();
      exportFormData(formData);
      service.create(formData);
    }
  }
}

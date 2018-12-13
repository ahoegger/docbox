package ch.ahoegger.docbox.client.administration.hr.billing;

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
import org.eclipse.scout.rt.platform.classid.ClassId;
import org.eclipse.scout.rt.platform.status.IStatus;
import org.eclipse.scout.rt.platform.text.TEXTS;
import org.eclipse.scout.rt.platform.util.ObjectUtility;

import ch.ahoegger.docbox.client.administration.hr.billing.BillingCycleForm.MainBox.CancelButton;
import ch.ahoegger.docbox.client.administration.hr.billing.BillingCycleForm.MainBox.LinkingBox;
import ch.ahoegger.docbox.client.administration.hr.billing.BillingCycleForm.MainBox.NameField;
import ch.ahoegger.docbox.client.administration.hr.billing.BillingCycleForm.MainBox.OkButton;
import ch.ahoegger.docbox.client.administration.hr.billing.BillingCycleForm.MainBox.PeriodBox;
import ch.ahoegger.docbox.client.administration.hr.billing.BillingCycleForm.MainBox.PlaceHolderField;
import ch.ahoegger.docbox.client.administration.hr.billing.BillingCycleForm.MainBox.StatusField;
import ch.ahoegger.docbox.client.administration.hr.billing.BillingCycleForm.MainBox.TaxGroupField;
import ch.ahoegger.docbox.client.administration.hr.taxgroup.AbstractTaxGroupSmartField;
import ch.ahoegger.docbox.client.templates.AbstractLinkingGroupBox;
import ch.ahoegger.docbox.client.templates.AbstractPeriodBox;
import ch.ahoegger.docbox.client.templates.AbstractStatusField;
import ch.ahoegger.docbox.client.util.AbstractValidatableForm;
import ch.ahoegger.docbox.or.definition.table.IBillingCicleTable;
import ch.ahoegger.docbox.shared.administration.hr.billing.IBillingCycleService;
import ch.ahoegger.docbox.shared.administration.hr.billingcycle.BillingCycleFormData;
import ch.ahoegger.docbox.shared.util.LocalDateUtility;

/**
 * <h3>{@link BillingCycleForm}</h3>
 *
 * @author aho
 */
@FormData(value = BillingCycleFormData.class, sdkCommand = FormData.SdkCommand.CREATE)
public class BillingCycleForm extends AbstractValidatableForm {

  private BigDecimal m_billingCycleId;

  private String m_initialName;

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("BillingCycle");
  }

  @Override
  protected void execStored() {
    getDesktop().dataChanged(IBillingCycleEntity.ENTITY_KEY);
  }

  public void startNew() {
    startInternal(new NewHandler());
  }

  public void startModify() {
    startInternal(new ModifyHandler());
  }

  public OkButton getOkButton() {
    return getFieldByClass(OkButton.class);
  }

  public CancelButton getCancelButton() {
    return getFieldByClass(CancelButton.class);
  }

  public PeriodBox getPeriodBox() {
    return getFieldByClass(PeriodBox.class);
  }

  public PlaceHolderField getPlaceHolderField() {
    return getFieldByClass(PlaceHolderField.class);
  }

  public TaxGroupField getTaxGroupField() {
    return getFieldByClass(TaxGroupField.class);
  }

  @Override
  public StatusField getStatusField() {
    return getFieldByClass(StatusField.class);
  }

  public LinkingBox getLinkingBox() {
    return getFieldByClass(LinkingBox.class);
  }

  public NameField getNameField() {
    return getFieldByClass(NameField.class);
  }

  @FormData
  public void setBillingCycleId(BigDecimal billingCycleId) {
    m_billingCycleId = billingCycleId;
  }

  @FormData
  public BigDecimal getBillingCycleId() {
    return m_billingCycleId;
  }

  protected void updateName() {
    if (getPeriodBox().getFromField().getValue() != null && ObjectUtility.equals(m_initialName, getNameField().getValue())) {
      DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM yyyy", LocalDateUtility.DE_CH);
      m_initialName = LocalDateUtility.toLocalDate(getPeriodBox().getFromField().getValue()).format(monthFormatter);
      getNameField().setValue(m_initialName);
    }
  }

  @ClassId("b4202323-4693-4652-96c3-01bae449f969")
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
    public class TaxGroupField extends AbstractTaxGroupSmartField {
    }

    @Order(3000)
    @ClassId("56c3188b-cd41-44c7-bc76-f7cc7f3ced63")
    public class NameField extends AbstractStringField {
      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("Name");
      }

      @Override
      protected int getConfiguredMaxLength() {
        return IBillingCicleTable.NAME_LENGTH;
      }

    }

    @Order(4000)
    public class PlaceHolderField extends AbstractPlaceholderField {
    }

    @Order(5000)
    @ClassId("0580a816-e280-4a79-bff0-15fe84d81361")
    public class PeriodBox extends AbstractPeriodBox {

      @Override
      protected boolean getConfiguredMandatoryFrom() {
        return true;
      }

      @Override
      protected boolean getConfiguredMandatoryTo() {
        return true;
      }

      @Override
      protected void execFromChangedValue(Date value) {
        if (getFromField().getValue() != null) {
          getToField().setValue(LocalDateUtility.toDate(LocalDateUtility.toLocalDate(getFromField().getValue()).with(TemporalAdjusters.lastDayOfMonth())));
        }
        updateName();
      }

      @Override
      protected void execToChangedValue(Date value) {
        updateName();
      }

    }

    @Order(6000)
    public class LinkingBox extends AbstractLinkingGroupBox {

    }

    @Order(10000)
    @ClassId("d990569d-eed9-4e0d-9153-8dae371e0104")
    public class OkButton extends AbstractOkButton {
    }

    @Order(11000)
    @ClassId("c25a40d3-2fc2-45b3-86b8-cadb59507ed7")
    public class CancelButton extends AbstractCancelButton {
    }
  }

  public class NewHandler extends AbstractFormHandler {
    @Override
    protected void execLoad() {
      BillingCycleFormData formData = new BillingCycleFormData();
      exportFormData(formData);
      formData = BEANS.get(IBillingCycleService.class).prepareCreate(formData);
      importFormData(formData);
      updateName();
    }

    @Override
    protected void execPostLoad() {
      touch();
    }

    @Override
    protected void execStore() {
      BillingCycleFormData formData = new BillingCycleFormData();
      exportFormData(formData);
      formData = BEANS.get(IBillingCycleService.class).create(formData);
      importFormData(formData);
    }

    @Override
    protected boolean execValidate() {
      BillingCycleFormData formData = new BillingCycleFormData();
      exportFormData(formData);
      IStatus status = BEANS.get(IBillingCycleService.class).validateNew(formData);
      getStatusField().setValidationStatus(status);
      return status.getSeverity() != IStatus.ERROR;
    }
  }

  public class ModifyHandler extends AbstractFormHandler {
    @Override
    protected void execLoad() {
      BillingCycleFormData formData = new BillingCycleFormData();
      exportFormData(formData);
      formData = BEANS.get(IBillingCycleService.class).load(formData);
      importFormData(formData);
    }

    @Override
    protected boolean execValidate() {
      BillingCycleFormData formData = new BillingCycleFormData();
      exportFormData(formData);
      IStatus status = BEANS.get(IBillingCycleService.class).validate(formData);
      getStatusField().setValidationStatus(status);
      return status.getSeverity() != IStatus.ERROR;
    }

    @Override
    protected void execStore() {
      BillingCycleFormData formData = new BillingCycleFormData();
      exportFormData(formData);
      formData = BEANS.get(IBillingCycleService.class).store(formData);
      importFormData(formData);
    }
  }

}

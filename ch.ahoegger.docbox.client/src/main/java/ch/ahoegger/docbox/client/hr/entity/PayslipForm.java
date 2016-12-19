package ch.ahoegger.docbox.client.hr.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.dto.FormData.SdkCommand;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.datefield.AbstractDateField;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.client.ui.form.fields.tablefield.AbstractTableField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.status.IStatus;
import org.eclipse.scout.rt.platform.status.Status;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.shared.TEXTS;

import ch.ahoegger.docbox.client.document.IDocumentEntity;
import ch.ahoegger.docbox.client.hr.entity.PayslipForm.MainBox.CancelButton;
import ch.ahoegger.docbox.client.hr.entity.PayslipForm.MainBox.DateField;
import ch.ahoegger.docbox.client.hr.entity.PayslipForm.MainBox.EntitiesField;
import ch.ahoegger.docbox.client.hr.entity.PayslipForm.MainBox.OkButton;
import ch.ahoegger.docbox.client.hr.entity.PayslipForm.MainBox.TitleField;
import ch.ahoegger.docbox.shared.document.DocumentFormData;
import ch.ahoegger.docbox.shared.hr.entity.CreatePayslipPermission;
import ch.ahoegger.docbox.shared.hr.entity.EntitySearchFormData;
import ch.ahoegger.docbox.shared.hr.entity.EntityTablePageData;
import ch.ahoegger.docbox.shared.hr.entity.EntityTablePageData.EntityTableRowData;
import ch.ahoegger.docbox.shared.hr.entity.IEntityService;
import ch.ahoegger.docbox.shared.hr.entity.IPayslipService;
import ch.ahoegger.docbox.shared.hr.entity.PayslipFormData;
import ch.ahoegger.docbox.shared.util.LocalDateUtility;

@FormData(value = PayslipFormData.class, sdkCommand = FormData.SdkCommand.CREATE)
public class PayslipForm extends AbstractForm {
  private List<BigDecimal> m_entityIds;
  private BigDecimal m_partnerId;

  public PayslipForm() {
    setHandler(new PayslipForm.FormHandler());
  }

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("PaySlip");
  }

  @Override
  protected int getConfiguredDisplayHint() {
    return DISPLAY_HINT_DIALOG;
  }

  @Override
  protected void execStored() {
    getDesktop().dataChanged(IWorkItemEntity.WORK_ITEM_KEY, IDocumentEntity.ENTITY_KEY);
  }

  @FormData
  public List<BigDecimal> getEntityIds() {
    return m_entityIds;
  }

  @FormData
  public void setEntityIds(List<BigDecimal> entityIds) {
    m_entityIds = entityIds;
  }

  @FormData
  public void setPartnerId(BigDecimal partnerId) {
    m_partnerId = partnerId;
  }

  @FormData
  public BigDecimal getPartnerId() {
    return m_partnerId;
  }

  public CancelButton getCancelButton() {
    return getFieldByClass(CancelButton.class);
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  public EntitiesField getEntitiesField() {
    return getFieldByClass(EntitiesField.class);
  }

  public DateField getDateField() {
    return getFieldByClass(DateField.class);
  }

  public TitleField getTitleField() {
    return getFieldByClass(TitleField.class);
  }

  public OkButton getOkButton() {
    return getFieldByClass(OkButton.class);
  }

  @Order(1000)
  public class MainBox extends AbstractGroupBox {

    @Override
    protected String getConfiguredBorderDecoration() {
      return BORDER_DECORATION_EMPTY;
    }

    @Override
    protected boolean getConfiguredBorderVisible() {
      return true;
    }

    @Order(1000)
    public class TitleField extends AbstractStringField {
      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("Title");
      }

      @Override
      protected int getConfiguredMaxLength() {
        return 1024;
      }
    }

    @Order(2000)
    public class DateField extends AbstractDateField {
      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("Date");
      }
    }

    @Order(3000)
    @FormData(sdkCommand = SdkCommand.IGNORE)
    public class EntitiesField extends AbstractTableField<EntitiesField.Table> {

      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("Entity");
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

      @Override
      protected void execReloadTableData() {
        super.execReloadTableData();
      }

      public class Table extends AbstractEntityTable {
      }
    }

    @Order(100000)
    public class OkButton extends AbstractOkButton {
    }

    @Order(101000)
    public class CancelButton extends AbstractCancelButton {
    }
  }

  public class FormHandler extends AbstractFormHandler {

    @Override
    protected void execLoad() {

      // title
      LocalDate date = LocalDate.now();
      LocalDate.now().minusMonths(1);
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM", LocalDateUtility.DE_CH);
      getTitleField().setValue("Lohnabrechnung " + date.format(formatter));
      // date
      getDateField().setValue(LocalDateUtility.toDate(LocalDate.now()));
      // entities
      EntitySearchFormData searchData = new EntitySearchFormData();
      searchData.setEntityIds(m_entityIds);
      EntityTablePageData entityTableData = BEANS.get(IEntityService.class).getEntityTableData(searchData);
      getEntitiesField().getTable().importFromTableRowBeanData(CollectionUtility.arrayList(entityTableData.getRows()), EntityTableRowData.class);
      // partner
      BigDecimal partnerId = CollectionUtility.arrayList(entityTableData.getRows()).stream().map(r -> r.getPartnerId()).distinct().findFirst().orElse(null);
      setPartnerId(partnerId);
      if (partnerId == null) {
        getMainBox().addErrorStatus(new Status(TEXTS.get("Error_entitiesNotOfSamePartner"), IStatus.ERROR));
      }

      setEnabledPermission(new CreatePayslipPermission());
    }

    @Override
    protected void execPostLoad() {
      touch();
    }

    @Override
    protected void execStore() {
      IPayslipService service = BEANS.get(IPayslipService.class);
      PayslipFormData formData = new PayslipFormData();
      exportFormData(formData);
      DocumentFormData docData = service.create(formData);
      PayslipConfirmationForm form = new PayslipConfirmationForm();
      form.setDisplayParent(getDesktop());
      form.setDocumentId(docData.getDocumentId());
      form.start();
    }
  }

}

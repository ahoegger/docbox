package ch.ahoegger.docbox.client.hr.tax;

import java.math.BigDecimal;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.bigdecimalfield.AbstractBigDecimalField;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.datefield.AbstractDateField;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.placeholder.AbstractPlaceholderField;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.shared.TEXTS;

import ch.ahoegger.docbox.client.hr.tax.TaxGroupForm.MainBox.CancelButton;
import ch.ahoegger.docbox.client.hr.tax.TaxGroupForm.MainBox.EndDateField;
import ch.ahoegger.docbox.client.hr.tax.TaxGroupForm.MainBox.NameField;
import ch.ahoegger.docbox.client.hr.tax.TaxGroupForm.MainBox.OkButton;
import ch.ahoegger.docbox.client.hr.tax.TaxGroupForm.MainBox.PlaceholderField;
import ch.ahoegger.docbox.client.hr.tax.TaxGroupForm.MainBox.ReportGroupBox;
import ch.ahoegger.docbox.client.hr.tax.TaxGroupForm.MainBox.ReportGroupBox.BruttoWageField;
import ch.ahoegger.docbox.client.hr.tax.TaxGroupForm.MainBox.ReportGroupBox.NettoWageField;
import ch.ahoegger.docbox.client.hr.tax.TaxGroupForm.MainBox.ReportGroupBox.SocualInsuranceField;
import ch.ahoegger.docbox.client.hr.tax.TaxGroupForm.MainBox.ReportGroupBox.SourceTaxField;
import ch.ahoegger.docbox.client.hr.tax.TaxGroupForm.MainBox.ReportGroupBox.VacationExtraField;
import ch.ahoegger.docbox.client.hr.tax.TaxGroupForm.MainBox.ReportGroupBox.WorkHoursField;
import ch.ahoegger.docbox.client.hr.tax.TaxGroupForm.MainBox.StartDateField;
import ch.ahoegger.docbox.or.definition.table.ITaxGroupTable;
import ch.ahoegger.docbox.shared.hr.tax.ITaxGroupService;
import ch.ahoegger.docbox.shared.hr.tax.TaxGroupFormData;

@FormData(value = TaxGroupFormData.class, sdkCommand = FormData.SdkCommand.CREATE)
public class TaxGroupForm extends AbstractForm {

  private BigDecimal m_taxGroupId;
  private BigDecimal m_partnerId;

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

  public StartDateField getStartDateField() {
    return getFieldByClass(StartDateField.class);
  }

  public EndDateField getEndDateField() {
    return getFieldByClass(EndDateField.class);
  }

  public PlaceholderField getPlaceholderField() {
    return getFieldByClass(PlaceholderField.class);
  }

  public ReportGroupBox getReportGroupBox() {
    return getFieldByClass(ReportGroupBox.class);
  }

  public WorkHoursField getWorkHoursField() {
    return getFieldByClass(WorkHoursField.class);
  }

  public BruttoWageField getBruttoWageField() {
    return getFieldByClass(BruttoWageField.class);
  }

  public NettoWageField getNettoWageField() {
    return getFieldByClass(NettoWageField.class);
  }

  public SourceTaxField getSourceTaxField() {
    return getFieldByClass(SourceTaxField.class);
  }

  public SocualInsuranceField getSocualInsuranceField() {
    return getFieldByClass(SocualInsuranceField.class);
  }

  public VacationExtraField getVacationExtraField() {
    return getFieldByClass(VacationExtraField.class);
  }

  public NameField getNameField() {
    return getFieldByClass(NameField.class);
  }

  public OkButton getOkButton() {
    return getFieldByClass(OkButton.class);
  }

  @Order(1000)
  public class MainBox extends AbstractGroupBox {

    @Order(1000)
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

    @Order(1500)
    public class PlaceholderField extends AbstractPlaceholderField {
    }

    @Order(2000)
    public class StartDateField extends AbstractDateField {
      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("from");
      }

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }
    }

    @Order(3000)
    public class EndDateField extends AbstractDateField {
      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("to");
      }
    }

    @Order(4000)
    public class ReportGroupBox extends AbstractGroupBox {
      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("Report");
      }

      @Order(1000)
      public class WorkHoursField extends AbstractBigDecimalField {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("Hours");
        }

        @Override
        protected BigDecimal getConfiguredMinValue() {
          return BigDecimal.valueOf(0d);
        }

        @Override
        protected BigDecimal getConfiguredMaxValue() {
          return new BigDecimal("9999999999999999999");
        }

        @Override
        protected boolean getConfiguredEnabled() {
          return false;
        }

        @Override
        protected int getConfiguredDisabledStyle() {
          return DISABLED_STYLE_READ_ONLY;
        }
      }

      @Order(2000)
      public class BruttoWageField extends AbstractBigDecimalField {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("BruttoWage");
        }

        @Override
        protected BigDecimal getConfiguredMinValue() {
          return BigDecimal.valueOf(0d);
        }

        @Override
        protected BigDecimal getConfiguredMaxValue() {
          return new BigDecimal("9999999999999999999");
        }

        @Override
        protected boolean getConfiguredEnabled() {
          return false;
        }

        @Override
        protected int getConfiguredDisabledStyle() {
          return DISABLED_STYLE_READ_ONLY;
        }
      }

      @Order(3000)
      public class NettoWageField extends AbstractBigDecimalField {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("NettoWage");
        }

        @Override
        protected BigDecimal getConfiguredMinValue() {
          return BigDecimal.valueOf(0d);
        }

        @Override
        protected BigDecimal getConfiguredMaxValue() {
          return new BigDecimal("9999999999999999999");
        }

        @Override
        protected boolean getConfiguredEnabled() {
          return false;
        }

        @Override
        protected int getConfiguredDisabledStyle() {
          return DISABLED_STYLE_READ_ONLY;
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
          return new BigDecimal(-200d);
        }

        @Override
        protected BigDecimal getConfiguredMaxValue() {
          return new BigDecimal("9999999999999999999");
        }

        @Override
        protected boolean getConfiguredEnabled() {
          return false;
        }

        @Override
        protected int getConfiguredDisabledStyle() {
          return DISABLED_STYLE_READ_ONLY;
        }
      }

      @Order(5000)
      public class SocualInsuranceField extends AbstractBigDecimalField {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("SoucialInsuranceRate");
        }

        @Override
        protected BigDecimal getConfiguredMinValue() {
          return new BigDecimal(-200d);
        }

        @Override
        protected BigDecimal getConfiguredMaxValue() {
          return new BigDecimal("9999999999999999999");
        }

        @Override
        protected boolean getConfiguredEnabled() {
          return false;
        }

        @Override
        protected int getConfiguredDisabledStyle() {
          return DISABLED_STYLE_READ_ONLY;
        }
      }

      @Order(6000)
      public class VacationExtraField extends AbstractBigDecimalField {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("VacationExtra");
        }

        @Override
        protected BigDecimal getConfiguredMinValue() {
          return new BigDecimal(0d);
        }

        @Override
        protected BigDecimal getConfiguredMaxValue() {
          return new BigDecimal("9999999999999999999");
        }

        @Override
        protected boolean getConfiguredEnabled() {
          return false;
        }

        @Override
        protected int getConfiguredDisabledStyle() {
          return DISABLED_STYLE_READ_ONLY;
        }
      }

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

      // fields
      getReportGroupBox().setVisible(false);
    }

    @Override
    protected void execPostLoad() {
      touch();
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

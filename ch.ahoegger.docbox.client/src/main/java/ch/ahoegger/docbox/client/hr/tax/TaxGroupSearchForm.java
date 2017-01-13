package ch.ahoegger.docbox.client.hr.tax;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractSearchForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.util.TriState;
import org.eclipse.scout.rt.shared.TEXTS;

import ch.ahoegger.docbox.client.hr.tax.TaxGroupSearchForm.MainBox.CancelButton;
import ch.ahoegger.docbox.client.hr.tax.TaxGroupSearchForm.MainBox.OkButton;
import ch.ahoegger.docbox.shared.hr.tax.TaxGroupSearchFormData;

@FormData(value = TaxGroupSearchFormData.class, sdkCommand = FormData.SdkCommand.CREATE)
public class TaxGroupSearchForm extends AbstractSearchForm {
  private TriState m_hasEndDate;

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("TaxGroupSearch");
  }

  public void startModify() {
    startInternalExclusive(new ModifyHandler());
  }

  public void startNew() {
    startInternal(new NewHandler());
  }

  @FormData
  public void setHasEndDate(TriState hasEndDate) {
    m_hasEndDate = hasEndDate;
  }

  @FormData
  public TriState getHasEndDate() {
    return m_hasEndDate;
  }

  public CancelButton getCancelButton() {
    return getFieldByClass(CancelButton.class);
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  public OkButton getOkButton() {
    return getFieldByClass(OkButton.class);
  }

  @Order(1000)
  public class MainBox extends AbstractGroupBox {

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
    }

    @Override
    protected void execStore() {
    }
  }

  public class NewHandler extends AbstractFormHandler {

    @Override
    protected void execLoad() {
    }

    @Override
    protected void execStore() {
    }
  }
}

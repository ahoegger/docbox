package ch.ahoegger.docbox.client.hr.billing;

import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.labelfield.AbstractLabelField;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.classid.ClassId;
import org.eclipse.scout.rt.platform.text.TEXTS;

import ch.ahoegger.docbox.client.hr.billing.PayslipDetailForm.MainBox.TitleField;

/**
 * <h3>{@link PayslipDetailForm}</h3>
 *
 * @author aho
 */
public class PayslipDetailForm extends AbstractForm {

  public TitleField getTitleField() {
    return getFieldByClass(TitleField.class);
  }

  @Order(1000)
  @ClassId("4d18d0a3-809f-42a2-a296-5fa5d2bb4e12")
  public class MainBox extends AbstractGroupBox {

    @Order(1000)
    public class TitleField extends AbstractLabelField {
      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("Title");
      }

    }

  }

}

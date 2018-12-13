package ch.ahoegger.docbox.client.templates;

import org.eclipse.scout.rt.client.ui.form.fields.htmlfield.IHtmlField;
import org.eclipse.scout.rt.platform.status.IStatus;

/**
 * <h3>{@link IStatusField}</h3>
 *
 * @author aho
 */
public interface IStatusField extends IHtmlField {

  String PROP_VALIDATION_STATUS = "PROP_VALIDATION_STATUS";

  /**
   * @param validationStatus
   */
  void setValidationStatus(IStatus validationStatus);

  /**
   * @return
   */
  IStatus getValidationStatus();

}

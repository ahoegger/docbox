package ch.ahoegger.docbox.client.templates;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.dto.FormData.DefaultSubtypeSdkCommand;
import org.eclipse.scout.rt.client.dto.FormData.SdkCommand;
import org.eclipse.scout.rt.client.ui.form.fields.htmlfield.AbstractHtmlField;
import org.eclipse.scout.rt.platform.html.HTML;
import org.eclipse.scout.rt.platform.status.IStatus;

/**
 * <h3>{@link AbstractStatusField}</h3>
 *
 * @author aho
 */
@FormData(sdkCommand = SdkCommand.IGNORE, defaultSubtypeSdkCommand = DefaultSubtypeSdkCommand.IGNORE)
public abstract class AbstractStatusField extends AbstractHtmlField implements IStatusField {

  @Override
  protected boolean getConfiguredLabelVisible() {
    return false;
  }

  @Override
  protected boolean getConfiguredVisible() {
    return false;
  }

  @Override
  protected int getConfiguredGridW() {
    return 2;
  }

  @Override
  protected boolean getConfiguredGridUseUiHeight() {
    return true;
  }

  @Override
  protected String getConfiguredCssClass() {
    return "docbox-status-field";
  }

  @Override
  public IStatus getValidationStatus() {
    return (IStatus) propertySupport.getProperty(PROP_VALIDATION_STATUS);
  }

  @Override
  public void setValidationStatus(IStatus validationStatus) {
    if (getValidationStatus() != null) {
      getForm().getRootGroupBox().clearErrorStatus();
    }
    propertySupport.setProperty(PROP_VALIDATION_STATUS, validationStatus);
    if (validationStatus != null && validationStatus.getSeverity() == IStatus.ERROR) {
      getForm().getRootGroupBox().addErrorStatus(validationStatus);
    }
    handleValidationStatusChanged();
  }

  @SuppressWarnings("null")
  private void handleValidationStatusChanged() {
    IStatus validationStatus = getValidationStatus();
    boolean hasValidationStatus = validationStatus != null && validationStatus.getSeverity() != IStatus.OK;
    setVisible(hasValidationStatus);
    if (hasValidationStatus) {

      setValue(
          HTML.div(
              HTML.icon(computeIcon(validationStatus)).cssClass("icon font-icon"),
              HTML.div(validationStatus.getMessage()).cssClass("message"))
              .cssClass("docbox-status-box " + computeCssClass(validationStatus)).toHtml());
      System.out.println(getValue());
    }
    else {
      setValue(null);
      setCssClass(null);
    }
  }

  protected String computeIcon(IStatus status) {
    switch (status.getSeverity()) {
      case IStatus.ERROR:
      case IStatus.WARNING:
        return "font:icomoon \ue906";
      case IStatus.INFO:
        return "font:icomoon \ue905";
      default:
        return null;
    }
  }

  protected String computeCssClass(IStatus status) {
    switch (status.getSeverity()) {
      case IStatus.ERROR:
        return "error";
      case IStatus.WARNING:
        return "warning";
      case IStatus.INFO:
        return "info";
      default:
        return null;
    }
  }

}

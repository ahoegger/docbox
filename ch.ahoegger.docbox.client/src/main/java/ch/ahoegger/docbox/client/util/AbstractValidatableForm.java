package ch.ahoegger.docbox.client.util;

import java.beans.PropertyChangeListener;
import java.util.function.Consumer;

import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.FormEvent;
import org.eclipse.scout.rt.client.ui.form.FormListener;
import org.eclipse.scout.rt.client.ui.form.fields.IValueField;
import org.eclipse.scout.rt.platform.status.IStatus;
import org.eclipse.scout.rt.platform.status.Status;

import ch.ahoegger.docbox.client.templates.IStatusField;

/**
 * <h3>{@link AbstractValidatableForm}</h3>
 *
 * @author aho
 */
public abstract class AbstractValidatableForm extends AbstractForm {

  private PropertyChangeListener m_valueChangedListener = event -> validateInternal();
  private FormListener m_formListener = event -> handleFormChanged(event);

  @Override
  protected void execInitForm() {
    addFormListener(m_formListener);
    visit((Consumer<IValueField>) vf -> {
      if (vf instanceof IStatusField) {
        return;
      }
      vf.addPropertyChangeListener(IValueField.PROP_VALUE, m_valueChangedListener);
    }, IValueField.class);
  }

  /**
   * @param event
   * @return
   */
  private void handleFormChanged(FormEvent event) {
    if (event.getType() == FormEvent.TYPE_LOAD_COMPLETE) {
      validateInternal();
    }
  }

  @Override
  protected void execDisposeForm() {
    removeFormListener(m_formListener);
    visit((Consumer<IValueField>) vf -> {
      if (vf instanceof IStatusField) {
        return;
      }
      vf.removePropertyChangeListener(IValueField.PROP_VALUE, m_valueChangedListener);
    }, IValueField.class);
  }

  protected void validateInternal() {
    if (!this.isFormLoading()) {
      getStatusField().setValidationStatus(execValidateInput());
      getHandler().onValidate();
    }
  }

  /**
   * @deprecated use {@link AbstractFormHandler#execValidate} instead
   * @return
   */
  @Deprecated
  protected IStatus execValidateInput() {
    return Status.OK_STATUS;
  }

  protected abstract IStatusField getStatusField();

}

package ch.ahoegger.docbox.client.util;

import org.eclipse.scout.rt.client.ui.form.IFormHandler;

/**
 * <h3>{@link IValdidateFormHandler}</h3>
 *
 * @author aho
 */
public interface IValdidateFormHandler extends IFormHandler {

  void execValidate();
}

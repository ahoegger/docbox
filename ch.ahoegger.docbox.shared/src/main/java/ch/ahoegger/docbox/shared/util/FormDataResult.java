package ch.ahoegger.docbox.shared.util;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;

/**
 * <h3>{@link FormDataResult}</h3>
 *
 * @author aho
 */
public class FormDataResult<T extends AbstractFormData, V> {

  private T m_formData;
  private V m_value;

  public FormDataResult(T formData, V value) {
    m_formData = formData;
    m_value = value;
  }

  public V getValue() {
    return m_value;
  }

  public T getFormData() {
    return m_formData;
  }
}

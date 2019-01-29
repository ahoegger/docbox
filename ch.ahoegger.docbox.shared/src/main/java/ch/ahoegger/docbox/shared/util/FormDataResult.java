package ch.ahoegger.docbox.shared.util;

import java.io.Serializable;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;

/**
 * <h3>{@link FormDataResult}</h3>
 *
 * @author aho
 */
public class FormDataResult<T extends AbstractFormData, V> implements Serializable {
  private static final long serialVersionUID = 1L;

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

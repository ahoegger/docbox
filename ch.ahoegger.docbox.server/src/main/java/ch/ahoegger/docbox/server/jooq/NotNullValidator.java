package ch.ahoegger.docbox.server.jooq;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.scout.rt.platform.exception.ProcessingStatus;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.status.IStatus;
import org.eclipse.scout.rt.platform.status.MultiStatus;
import org.eclipse.scout.rt.platform.status.Status;
import org.jooq.Field;

/**
 * <h3>{@link NotNullValidator}</h3>
 *
 * @author aho
 */
public class NotNullValidator {

  private Map<String, Object /*value*/> m_values = new HashMap<>();

  public <T> NotNullValidator with(Field<T> fieldName, T value) {
    return with(fieldName.getName(), value);
  }

  public NotNullValidator with(String name, Object value) {
    m_values.put(name, value);
    return this;
  }

  public IStatus validate() {
    return m_values.entrySet()
        .stream()
        .map(e -> {
          if (e.getValue() == null) {
            return new Status(String.format("Field %s can not be null.", e.getKey()), IStatus.ERROR);
          }
          return Status.OK_STATUS;
        }).filter(status -> !status.isOK())
        .reduce(new MultiStatus(), (acc, status) -> {
          ((MultiStatus) acc).add(status);
          return acc;
        });
  }

  public void validateAndThrow() {
    IStatus status = validate();
    if (!status.isOK()) {
      throw new VetoException(new ProcessingStatus(status));
    }
  }
}

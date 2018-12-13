package ch.ahoegger.docbox.server.jooq;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.scout.rt.platform.exception.ProcessingStatus;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.status.IStatus;
import org.eclipse.scout.rt.platform.status.MultiStatus;
import org.eclipse.scout.rt.platform.status.Status;
import org.eclipse.scout.rt.platform.util.ObjectUtility;
import org.jooq.Field;
import org.jooq.Record;

/**
 * <h3>{@link UnmodifiableValidator}</h3>
 *
 * @author aho
 */
public class UnmodifiableValidator {

  private final Map<Field<?>, Object /*value*/> m_fieldValues = new HashMap<>();

  public UnmodifiableValidator with(Field<?> field, Object value) {
    m_fieldValues.put(field, value);
    return this;
  }

  public IStatus validate(Record rec) {
    return m_fieldValues.entrySet()
        .stream()
        .map(e -> {
          if (ObjectUtility.notEquals(rec.get(e.getKey()), e.getValue())) {
            return new Status(String.format("Unmodifiable field %s not equals [%s, %s].", e.getKey().getName(), rec.get(e.getKey()), e.getValue()), IStatus.ERROR);
          }
          return Status.OK_STATUS;
        })
        .filter(status -> !status.isOK())
        .reduce(new MultiStatus(), (acc, status) -> {
          ((MultiStatus) acc).add(status);
          return acc;
        });
  }

  public void validateAndThrow(Record rec) {
    IStatus status = validate(rec);
    if (!status.isOK()) {
      throw new VetoException(new ProcessingStatus(status));
    }
  }
}

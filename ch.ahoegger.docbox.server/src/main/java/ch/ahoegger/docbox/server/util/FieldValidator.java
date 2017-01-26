package ch.ahoegger.docbox.server.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.platform.status.IStatus;
import org.eclipse.scout.rt.platform.status.MultiStatus;
import org.eclipse.scout.rt.platform.status.Status;
import org.eclipse.scout.rt.platform.util.ObjectUtility;
import org.jooq.Field;
import org.jooq.Record;

/**
 * <h3>{@link FieldValidator}</h3>
 *
 * @author aho
 */
public class FieldValidator {

  private List<IFieldValidator<?, ?>> m_validators = new ArrayList<>();

  public void add(IFieldValidator<?, ?> validator) {
    m_validators.add(validator);
  }

  public IStatus validate(Record rec) {

    return m_validators.stream()
        .map(v -> v.validate(rec))
        .reduce(new MultiStatus(), (s1, s2) -> {
          ((MultiStatus) s1).add(s2);
          return s1;
        });
  }

  public static interface IFieldValidator<V1, V2> {

    IStatus validate(Record rec);

  }

  public static <FIELD extends Field<VALUE>, VALUE> IFieldValidator<FIELD, VALUE> unmodifiableValidator(FIELD field, VALUE value) {
    return new IFieldValidator<FIELD, VALUE>() {
      @Override
      public IStatus validate(Record rec) {

        VALUE dbValue = rec.get(field);
        if (ObjectUtility.notEquals(dbValue, value)) {
          return new Status(String.format("Unmodifiable field %s not equals [%s, %s].", field.getName(), dbValue, value), IStatus.ERROR);
        }
        return Status.OK_STATUS;
      }
    };

  }
}

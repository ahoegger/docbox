package ch.ahoegger.docbox.shared.validation;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.text.TEXTS;

/**
 * <h3>{@link DateValidation}</h3>
 *
 * @author Andreas Hoegger
 */
public class DateValidation {

  public static void validateFromTo(Date fromRaw, Date toRaw) throws VetoException {
    if (fromRaw == null) {
      if (toRaw == null) {
        return;
      }
      else {
        throw new VetoException(TEXTS.get("FromDateMustBeSet"));
      }
    }
    else if (toRaw == null) {
      return;
    }

    LocalDate from = fromRaw.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    LocalDate to = toRaw.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    if (to.isBefore(from)) {
      throw new VetoException(TEXTS.get("FromDateMustBeBeforeToDate"));
    }
  }
}

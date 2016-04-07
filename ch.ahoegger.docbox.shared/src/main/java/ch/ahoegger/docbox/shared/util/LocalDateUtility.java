package ch.ahoegger.docbox.shared.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * <h3>{@link LocalDateUtility}</h3>
 *
 * @author aho
 */
public class LocalDateUtility {

  public static Date toDate(LocalDate localDate) {
    return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
  }

  public static Date today() {
    return toDate(LocalDate.now());
  }
}

package ch.ahoegger.docbox.shared.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

/**
 * <h3>{@link LocalDateUtility}</h3>
 *
 * @author Andreas Hoegger
 */
public class LocalDateUtility {

  public static Locale DE_CH = new Locale("de", "CH");

  public static Date toDate(LocalDate localDate) {
    return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
  }

  public static Date today() {
    return toDate(LocalDate.now());
  }
}

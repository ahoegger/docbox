package ch.ahoegger.docbox.shared.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.function.BinaryOperator;

/**
 * <h3>{@link LocalDateUtility}</h3>
 *
 * @author Andreas Hoegger
 */
public class LocalDateUtility {

  public static Locale DE_CH = new Locale("de", "CH");

  public static DateTimeFormatter DATE_FORMATTER_ddMMyyyy = DateTimeFormatter.ofPattern("dd.MM.yyyy", DE_CH);
  public static DateTimeFormatter DATE_FORMATTER_yyyy = DateTimeFormatter.ofPattern("yyyy", DE_CH);

  public static DateTimeFormatter DATE_FORMATTER_MMMM = DateTimeFormatter.ofPattern("MMMM", DE_CH);

  public static BinaryOperator<LocalDateRange> DATE_RANGE_ACCUMULATOR = (accumulator, range) -> {
    return accumulator.combine(range);
  };

  public static LocalDateRange newDateRange(Date from, Date to) {
    return new LocalDateRange(toLocalDate(from), toLocalDate(to));
  }

  public static String format(Date date, DateTimeFormatter formatter) {
    if (date == null) {
      return null;
    }
    return toLocalDate(date).format(formatter);
  }

  public static Date toDate(LocalDate localDate) {
    if (localDate == null) {
      return null;
    }
    return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
  }

  public static Date today() {
    return toDate(LocalDate.now());
  }

  public static LocalDate toLocalDate(Date date) {
    if (date == null) {
      return null;
    }
    return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
  }

  public static boolean isBeforeOrEqual(LocalDate reference, LocalDate date) {
    return reference.isBefore(date) || reference.isEqual(date);
  }

  public static boolean isAfterOrEqual(LocalDate reference, LocalDate date) {
    return reference.isAfter(date) || reference.isEqual(date);
  }

  public static boolean isBetween(LocalDate from, LocalDate to, LocalDate date) {
    return from.isBefore(date) && to.isAfter(date);
  }

  public static boolean isBetweenOrEqual(LocalDate from, LocalDate to, LocalDate date) {
    return isBeforeOrEqual(from, date) && isAfterOrEqual(to, date);
  }

  public static boolean isBefore(Date reference, Date date) {
    LocalDate referenceDate = toLocalDate(reference);
    LocalDate localDate = toLocalDate(date);
    return referenceDate.isBefore(localDate);
  }

  public static boolean isBeforeOrEqual(Date reference, Date date) {
    LocalDate referenceDate = toLocalDate(reference);
    LocalDate localDate = toLocalDate(date);
    return isBeforeOrEqual(referenceDate, localDate);
  }

  public static boolean isAfter(Date reference, Date date) {
    LocalDate referenceDate = toLocalDate(reference);
    LocalDate localDate = toLocalDate(date);
    return referenceDate.isAfter(localDate);
  }

  public static boolean isAfterOrEqual(Date reference, Date date) {
    LocalDate referenceDate = toLocalDate(reference);
    LocalDate localDate = toLocalDate(date);
    return isAfterOrEqual(referenceDate, localDate);
  }

  public static boolean isBetween(Date from, Date to, Date date) {
    return isBetween(toLocalDate(from), toLocalDate(to), toLocalDate(date));
  }

  public static boolean isBetweenOrEqual(Date from, Date to, Date date) {
    return isBetweenOrEqual(toLocalDate(from), toLocalDate(to), toLocalDate(date));
  }

  public static class LocalDateRange {

    private LocalDate m_from;
    private LocalDate m_to;

    public LocalDateRange() {
      this(null, null);
    }

    public LocalDateRange(LocalDate from, LocalDate to) {
      m_from = from;
      m_to = to;
    }

    public LocalDateRange combine(LocalDateRange range2) {
      // from
      if (getFrom() == null) {
        m_from = range2.getFrom();
      }
      else if (range2.getFrom() != null) {
        if (getFrom().compareTo(range2.getFrom()) > 0) {
          m_from = range2.getFrom();
        }
      }
      // to
      if (getTo() == null) {
        m_to = range2.getTo();
      }
      else if (range2.getTo() != null) {
        if (getTo().compareTo(range2.getTo()) < 0) {
          m_to = range2.getTo();
        }
      }

      return this;
    }

    public LocalDate getFrom() {
      return m_from;
    }

    public LocalDate getTo() {
      return m_to;
    }

    public boolean isSet() {
      return m_from != null && m_to != null;
    }

  }

  /**
   * @param periodTo
   * @param periodTo2
   * @return
   */
  public static int compare(Date periodTo, Date periodTo2) {
    if (periodTo == null && periodTo2 == null) {
      return 0;
    }
    if (periodTo == null) {
      return -1;
    }
    if (periodTo2 == null) {
      return 1;
    }
    return toLocalDate(periodTo).compareTo(toLocalDate(periodTo2));
  }
}

package ch.ahoegger.docbox.server.backup.internal;

import java.text.SimpleDateFormat;

/**
 *
 */
public class DateColumn extends AbstractColumn {

  public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");

  /**
   * @param columnName
   * @param value
   */
  public DateColumn(String columnName) {
    super(columnName, IColumn.TYPE.DATE);
  }

  @Override
  public String formatValue(Object value) {
    if (value == null) {
      return "NULL";
    }
    return "DATE('" + DATE_FORMATTER.format(value) + "')";
  }
}

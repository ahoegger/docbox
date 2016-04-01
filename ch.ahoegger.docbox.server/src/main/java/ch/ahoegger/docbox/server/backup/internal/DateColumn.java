package ch.ahoegger.docbox.server.backup.internal;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.scout.rt.platform.util.TypeCastUtility;

/**
 *
 */
public class DateColumn extends AbstractColumn<Date> {

  public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");

  /**
   * @param columnName
   * @param value
   */
  public DateColumn(String columnName) {
    super(columnName, IColumn.TYPE.DATE);
  }

  @Override
  public String formatValueRaw(Object o) {
    return formatValue(TypeCastUtility.castValue(o, Date.class));
  }

  @Override
  public String formatValue(Date value) {
    if (value == null) {
      return "NULL";
    }
    return "DATE('" + DATE_FORMATTER.format(value) + "')";
  }
}

package ch.ahoegger.docbox.server.backup.internal;

import org.apache.commons.lang.StringEscapeUtils;
import org.eclipse.scout.rt.platform.util.TypeCastUtility;

/**
 *
 */
public class StringColumn extends AbstractColumn<String> {

  /**
   * @param columnName
   * @param value
   */
  public StringColumn(String columnName) {
    super(columnName, IColumn.TYPE.VARCHAR);
  }

  @Override
  public String formatValueRaw(Object o) {
    return formatValue(TypeCastUtility.castValue(o, String.class));
  }

  @Override
  public String formatValue(String value) {
    if (value == null) {
      return "NULL";
    }
    value = StringEscapeUtils.escapeSql(value);
    return "'" + value + "'";
  }
}

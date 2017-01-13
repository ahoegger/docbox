package ch.ahoegger.docbox.server.backup.internal;

import org.eclipse.scout.rt.platform.util.TypeCastUtility;

/**
 *
 */
public class ClobColumn extends AbstractColumn<String> {

  /**
   * @param columnName
   * @param value
   */
  public ClobColumn(String columnName) {
    super(columnName, IColumn.TYPE.CLOB);
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
    value = value.replace("'", "''");
    return "'" + value + "'";
  }
}

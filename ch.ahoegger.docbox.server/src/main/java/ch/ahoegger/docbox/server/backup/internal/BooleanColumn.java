package ch.ahoegger.docbox.server.backup.internal;

import org.eclipse.scout.rt.platform.util.TypeCastUtility;

/**
 *
 */
public class BooleanColumn extends AbstractColumn<Boolean> {

  /**
   * @param columnName
   * @param value
   */
  public BooleanColumn(String columnName) {
    super(columnName, IColumn.TYPE.BOOLEAN);
  }

  @Override
  public String formatValueRaw(Object o) {
    return formatValue(TypeCastUtility.castValue(o, Boolean.class));
  }

  @Override
  public String formatValue(Boolean value) {
    if (value != null) {
      return value.toString().toUpperCase();
    }
    return "NULL";
  }

}

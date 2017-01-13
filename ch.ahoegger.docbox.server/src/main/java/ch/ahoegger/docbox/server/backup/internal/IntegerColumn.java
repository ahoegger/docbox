package ch.ahoegger.docbox.server.backup.internal;

import org.eclipse.scout.rt.platform.util.TypeCastUtility;

/**
 *
 */
public class IntegerColumn extends AbstractColumn<Integer> {

  /**
   * @param columnName
   * @param value
   */
  public IntegerColumn(String columnName) {
    super(columnName, IColumn.TYPE.INTEGER);
  }

  @Override
  public String formatValueRaw(Object o) {
    return formatValue(TypeCastUtility.castValue(o, Integer.class));
  }
}

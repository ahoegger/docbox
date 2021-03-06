package ch.ahoegger.docbox.server.backup.internal;

import org.eclipse.scout.rt.platform.util.TypeCastUtility;

/**
 *
 */
public class DoubleColumn extends AbstractColumn<Double> {

  /**
   * @param columnName
   * @param value
   */
  public DoubleColumn(String columnName) {
    super(columnName, IColumn.TYPE.DOUBLE);
  }

  @Override
  public String formatValueRaw(Object o) {
    return formatValue(TypeCastUtility.castValue(o, Double.class));
  }

}

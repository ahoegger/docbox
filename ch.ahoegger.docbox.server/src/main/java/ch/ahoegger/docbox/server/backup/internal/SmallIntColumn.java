package ch.ahoegger.docbox.server.backup.internal;

import org.eclipse.scout.rt.platform.util.TypeCastUtility;

/**
 *
 */
public class SmallIntColumn extends AbstractColumn<Integer> {

  /**
   * @param columnName
   * @param value
   */
  public SmallIntColumn(String columnName) {
    super(columnName, IColumn.TYPE.SMALLINT);
  }

  @Override
  public String formatValueRaw(Object o) {
    return formatValue(TypeCastUtility.castValue(o, Integer.class));
  }
}

package ch.ahoegger.docbox.server.backup.internal;

import java.math.BigDecimal;

import org.eclipse.scout.rt.platform.util.TypeCastUtility;

/**
 *
 */
public class DecimalColumn extends AbstractColumn<BigDecimal> {

  @Override
  public String formatValueRaw(Object o) {
    return formatValue(TypeCastUtility.castValue(o, BigDecimal.class));
  }

  /**
   * @param columnName
   * @param value
   */
  public DecimalColumn(String columnName) {
    super(columnName, IColumn.TYPE.DECIMAL);
  }

}

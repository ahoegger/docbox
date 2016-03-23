package ch.ahoegger.docbox.server.backup.internal;

/**
 *
 */
public class DecimalColumn extends AbstractColumn {

  /**
   * @param columnName
   * @param value
   */
  public DecimalColumn(String columnName) {
    super(columnName, IColumn.TYPE.DECIMAL);
  }

}

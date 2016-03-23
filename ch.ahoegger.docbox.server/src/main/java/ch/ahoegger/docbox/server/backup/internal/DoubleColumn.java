package ch.ahoegger.docbox.server.backup.internal;

/**
 *
 */
public class DoubleColumn extends AbstractColumn {

  /**
   * @param columnName
   * @param value
   */
  public DoubleColumn(String columnName) {
    super(columnName, IColumn.TYPE.DOUBLE);
  }

}

package ch.ahoegger.docbox.server.backup.internal;

/**
 *
 */
public class StringColumn extends AbstractColumn {

  /**
   * @param columnName
   * @param value
   */
  public StringColumn(String columnName) {
    super(columnName, IColumn.TYPE.VARCHAR);
  }

  @Override
  public String formatValue(Object value) {
    if (value == null) {
      return "NULL";
    }
    return "'" + value + "'";
  }
}

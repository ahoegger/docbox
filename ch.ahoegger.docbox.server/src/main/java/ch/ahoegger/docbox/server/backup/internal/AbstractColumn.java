package ch.ahoegger.docbox.server.backup.internal;

/**
 *
 */
public class AbstractColumn implements IColumn {

  private String m_columnName;
  private final TYPE m_type;

  public AbstractColumn(String columnName, IColumn.TYPE type) {
    m_columnName = columnName;
    m_type = type;
  }

  @Override
  public String getColumnName() {
    return m_columnName;
  }

  @Override
  public TYPE getType() {
    return m_type;
  }

  @Override
  public String formatValue(Object value) {
    if (value != null) {
      return value.toString();
    }
    return "NULL";
  }
}

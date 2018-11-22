package ch.ahoegger.docbox.server.database.migration;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * <h3>{@link TableDescription}</h3>
 *
 * @author aho
 */
public class TableDescription implements ITableDescription {

  private String m_tableName;
  private String m_tableId;
  private Set<String> m_columns;

  TableDescription withTableId(String tableId) {
    m_tableId = tableId;
    return this;
  }

  public String getTableId() {
    return m_tableId;
  }

  TableDescription withTableName(String tableName) {
    m_tableName = tableName;
    return this;
  }

  @Override
  public String getTableName() {
    return m_tableName;
  }

  TableDescription withColumns(Set<String> columns) {
    m_columns = columns;
    return this;
  }

  @Override
  public Set<String> getColumns() {
    return m_columns;
  }

  @Override
  public boolean hasColumn(String columnName) {
    return m_columns.contains(columnName);
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("Table: '").append(getTableName()).append("(").append(getTableId()).append(")' with columns [");
    if (m_columns != null) {
      builder.append(m_columns.stream().collect(Collectors.joining(", ")));
    }
    builder.append("]");
    return builder.toString();
  }

}

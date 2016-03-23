package ch.ahoegger.docbox.server.backup.internal;

import java.util.List;

/**
 *
 */
public class StatementFactory {

  public static String createInsertStatement(String tableName, List<IColumn> columns, Object[] row) {
    StringBuilder columnBuilder = new StringBuilder("(");
    StringBuilder valueBuilder = new StringBuilder("(");
    for (int i = 0; i < columns.size(); i++) {
      IColumn column = columns.get(i);
      if (i > 0) {
        columnBuilder.append(", ");
        valueBuilder.append(", ");
      }
      columnBuilder.append(column.getColumnName());
      valueBuilder.append(column.formatValue(row[i]));
    }
    columnBuilder.append(")");
    valueBuilder.append(")");
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("INSERT INTO ").append(tableName);
    statementBuilder.append(" ").append(columnBuilder.toString()).append(" VALUES ").append(valueBuilder.toString());
    statementBuilder.append(";");
    return statementBuilder.toString();
  }

  public static String createDeleteTableStatement(String tableName) {
    return "DELETE FROM " + tableName + ";";
  }
}

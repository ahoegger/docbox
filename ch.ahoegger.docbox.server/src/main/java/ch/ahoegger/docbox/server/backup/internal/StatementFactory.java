package ch.ahoegger.docbox.server.backup.internal;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 *
 */
public class StatementFactory {

  public static String createInsertStatement(String tableName, List<IColumn> columns, Object[][] rows) {
    StringBuilder columnBuilder = new StringBuilder("(");
    for (int i = 0; i < columns.size(); i++) {
      IColumn<?> column = columns.get(i);
      if (i > 0) {
        columnBuilder.append(", ");
      }
      columnBuilder.append(column.getColumnName());
    }
    columnBuilder.append(")");

    String values = Arrays.stream(rows)
        .map(r -> (Object[]) r)
        .map(row -> {
          StringBuilder valRow = new StringBuilder("  (");
          valRow.append(
              IntStream.range(0, row.length)
                  .mapToObj(i -> columns.get(i).formatValueRaw(row[i]))
                  .collect(Collectors.joining(", ")));
          valRow.append(")");
          return valRow.toString();

        }).collect(Collectors.joining(",\r\n"));

    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("INSERT INTO ").append(tableName);
    statementBuilder.append(" \r\n  ").append(columnBuilder.toString()).append(" VALUES \r\n").append(values);
    statementBuilder.append(";");
    return statementBuilder.toString();
  }

  public static String createInsertStatement(String tableName, List<IColumn> columns, Object[] row) {
    StringBuilder columnBuilder = new StringBuilder("(");
    StringBuilder valueBuilder = new StringBuilder("(");
    for (int i = 0; i < columns.size(); i++) {
      IColumn<?> column = columns.get(i);
      if (i > 0) {
        columnBuilder.append(", ");
        valueBuilder.append(", ");
      }
      columnBuilder.append(column.getColumnName());
      valueBuilder.append(column.formatValueRaw(row[i]));
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

package ch.ahoegger.docbox.server.database;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <h3>{@link SqlFramentBuilder}</h3>
 *
 * @author aho
 */
public class SqlFramentBuilder {

  public static final String WHERE_DEFAULT = "WHERE 1 = 1";

  public static String columns(String... columns) {
    Stream<String> columnStream = Stream.of(columns);
    return columnStream.collect(Collectors.joining(", "));
  }

  public static String whereStringContains(String column, String text) {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("UPPER(").append(column).append(") LIKE UPPER('");
    statementBuilder.append("%").append(text).append("%')");
    return statementBuilder.toString();
  }
}

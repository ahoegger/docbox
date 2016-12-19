package ch.ahoegger.docbox.shared.util;

import java.text.NumberFormat;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.scout.rt.platform.util.NumberUtility;

/**
 * <h3>{@link SqlFramentBuilder}</h3>
 *
 * @author Andreas Hoegger
 */
public class SqlFramentBuilder {

  public static final String WHERE_DEFAULT = "WHERE 1 = 1";

  public static String columns(String... columns) {
    Stream<String> columnStream = Stream.of(columns);
    return columnStream.collect(Collectors.joining(", "));
  }

  public static String columnsAliased(String alias, String... columns) {
    Stream<String> columnStream = Stream.of(columns);
    return columnStream.map(c -> alias + "." + c).collect(Collectors.joining(", "));
  }

  public static String whereStringContains(String tableAlias, String column, String text) {
    return whereStringContains(tableAlias + "." + column, text);
  }

  public static String whereStringContains(String column, String text) {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("UPPER(").append(column).append(") LIKE UPPER('");
    statementBuilder.append("%").append(text).append("%')");
    return statementBuilder.toString();
  }

  public static String where(String tableAlias, String column, Number number) {
    return where(tableAlias + "." + column, number);
  }

  public static String where(String column, Number number) {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append(column);
    if (number == null) {
      statementBuilder.append(" IS NULL");
    }
    else {
      NumberFormat.getInstance().setGroupingUsed(false);
      statementBuilder.append(" = ").append(NumberUtility.format(number));

    }
    return statementBuilder.toString();
  }
}

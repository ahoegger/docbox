package ch.ahoegger.docbox.shared.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <h3>{@link SqlFramentBuilder}</h3>
 *
 * @author Andreas Hoegger
 */
public class SqlFramentBuilder {

  public static final String WHERE_DEFAULT = "WHERE 1 = 1";

  private static final NumberFormat m_idFormat;
  static {
    m_idFormat = NumberFormat.getInstance();
    m_idFormat.setMaximumFractionDigits(0);
    m_idFormat.setGroupingUsed(false);

  }

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

  public static String whereIn(String tableAlias, String column, Set<BigDecimal> ids) {
    return whereIn(tableAlias + "." + column, ids);
  }

  public static String whereIn(String column, Set<BigDecimal> ids) {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append(column).append(" IN (")
        .append(
            ids.stream()
                .filter(key -> key != null)
                .map(key -> m_idFormat.format(key))
                .collect(Collectors.joining(", ")))
        .append(")");
    return statementBuilder.toString();
  }

}

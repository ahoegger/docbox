package ch.ahoegger.docbox.server.database;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <h3>{@link StatementBuilder}</h3>
 *
 * @author Andreas Hoegger
 */
public class StatementBuilder {
  private StringBuilder m_sqlBuilder = new StringBuilder();

  public StatementBuilder select() {
    m_sqlBuilder.append("SELECT ");
    return this;
  }

  public StatementBuilder columns(String... columns) {
    Stream<String> columnStream = Stream.of(columns);
    m_sqlBuilder.append(columnStream.collect(Collectors.joining(", ")));
    return this;
  }

  public String getStatement() {
    return m_sqlBuilder.toString();
  }

}

package ch.ahoegger.docbox.server.or.generator.table;

import java.sql.Connection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.scout.rt.platform.ApplicationScoped;

/**
 * <h3>{@link ITableStatement}</h3>
 *
 * @author Andreas Hoegger
 */
@ApplicationScoped
public interface ITableStatement {

  String getCreateTable();

  public static String columns(String... columns) {
    Stream<String> columnStream = Stream.of(columns);
    return columnStream.collect(Collectors.joining(", "));
  }

  /**
   * @param connection
   */
  void createTable(Connection connection);

  /**
   * @param connection
   */
  void deleteTable(Connection connection);

  /**
   * @param connection
   */
  void dropTable(Connection connection);
}

package ch.ahoegger.docbox.server.database.migration;

import java.util.Set;

/**
 * <h3>{@link ITableDescription}</h3>
 *
 * @author aho
 */
public interface ITableDescription {

  /**
   * @return
   */
  String getTableName();

  /**
   * @param columnName
   * @return
   */
  boolean hasColumn(String columnName);

  /**
   * @return
   */
  Set<String> getColumns();

}

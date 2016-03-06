package ch.ahoegger.docbox.server.database.initialization;

import org.eclipse.scout.rt.platform.Bean;

import ch.ahoegger.docbox.server.database.IDocboxSqlService;

/**
 * <h3>{@link ITableTask}</h3>
 *
 * @author aho
 */
@Bean
public interface ITableTask {

  String getCreateStatement();

  void createTable(IDocboxSqlService sqlService);

  void createRows(IDocboxSqlService sqlService);
}

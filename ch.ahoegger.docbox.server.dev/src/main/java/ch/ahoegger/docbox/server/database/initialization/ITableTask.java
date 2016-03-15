package ch.ahoegger.docbox.server.database.initialization;

import org.eclipse.scout.rt.platform.Bean;
import org.eclipse.scout.rt.server.jdbc.ISqlService;

/**
 * <h3>{@link ITableTask}</h3>
 *
 * @author aho
 */
@Bean
public interface ITableTask {

  String getCreateStatement();

  void createTable(ISqlService sqlService);

  void createRows(ISqlService sqlService);
}

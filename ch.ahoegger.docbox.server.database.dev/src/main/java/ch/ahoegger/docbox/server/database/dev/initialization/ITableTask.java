package ch.ahoegger.docbox.server.database.dev.initialization;

import org.eclipse.scout.rt.platform.Bean;
import org.eclipse.scout.rt.server.jdbc.ISqlService;

/**
 * <h3>{@link ITableTask}</h3>
 *
 * @author Andreas Hoegger
 */
@Bean
public interface ITableTask {

  String getCreateStatement();

  void createTable(ISqlService sqlService);

  void deleteTable(ISqlService sqlService);

  void dropTable(ISqlService sqlService);
}

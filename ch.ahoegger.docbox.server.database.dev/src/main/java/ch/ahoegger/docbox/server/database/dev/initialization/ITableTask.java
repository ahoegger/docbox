package ch.ahoegger.docbox.server.database.dev.initialization;

import org.eclipse.scout.rt.server.jdbc.ISqlService;

import ch.ahoegger.docbox.server.or.generator.table.ITableStatement;

/**
 * <h3>{@link ITableTask}</h3>
 *
 * @author Andreas Hoegger
 */
public interface ITableTask extends ITableStatement {

  void createTable(ISqlService sqlService);

  void deleteTable(ISqlService sqlService);

  void dropTable(ISqlService sqlService);
}

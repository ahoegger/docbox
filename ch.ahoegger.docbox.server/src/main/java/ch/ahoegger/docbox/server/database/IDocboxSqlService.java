package ch.ahoegger.docbox.server.database;

import org.eclipse.scout.rt.server.jdbc.ISqlService;

/**
 * <h3>{@link IDocboxSqlService}</h3>
 *
 * @author aho
 */
public interface IDocboxSqlService extends ISqlService {

  /**
   * @return
   */
  Long getSequenceNextval();

}

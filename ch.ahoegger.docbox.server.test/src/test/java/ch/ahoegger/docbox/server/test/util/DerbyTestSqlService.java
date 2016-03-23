package ch.ahoegger.docbox.server.test.util;

import org.eclipse.scout.rt.platform.CreateImmediately;
import org.eclipse.scout.rt.platform.Replace;

import ch.ahoegger.docbox.server.database.DerbyDevSqlService;

/**
 * <h3>{@link DerbyTestSqlService}</h3>
 *
 * @author aho
 */
@CreateImmediately
@Replace
public class DerbyTestSqlService extends DerbyDevSqlService {

  @Override
  protected String getConfiguredJdbcMappingName() {
    // in memory
    return "jdbc:derby:memory:contacts-database;create=true";
  }
}

package ch.ahoegger.docbox.server.test.util;

import org.eclipse.scout.rt.platform.Replace;

import ch.ahoegger.docbox.server.database.DerbySqlService;

/**
 * <h3>{@link DerbyTestSqlService}</h3>
 *
 * @author Andreas Hoegger
 */
@Replace
public class DerbyTestSqlService extends DerbySqlService {

  @Override
  protected String getConfiguredJdbcMappingName() {
    // in memory
    return "jdbc:derby:memory:contacts-database;create=true";
  }

}

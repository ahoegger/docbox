package ch.ahoegger.docbox.server.app.dev;

import org.eclipse.scout.rt.platform.Replace;
import org.eclipse.scout.rt.platform.config.CONFIG;
import org.eclipse.scout.rt.platform.util.StringUtility;

import ch.ahoegger.docbox.server.database.DerbySqlService;

/**
 * <h3>{@link DevDerbySqlService}</h3>
 *
 * @author Andreas Hoegger
 */
@Replace
public class DevDerbySqlService extends DerbySqlService {

  @Override
  protected String getConfiguredJdbcMappingName() {
    if (StringUtility.isNullOrEmpty(CONFIG.getPropertyValue(DatabaseLocationProperty.class))) {
      // in memory
      return "jdbc:derby:memory:contacts-database;create=true";
    }
    return super.getConfiguredJdbcMappingName();
  }

}

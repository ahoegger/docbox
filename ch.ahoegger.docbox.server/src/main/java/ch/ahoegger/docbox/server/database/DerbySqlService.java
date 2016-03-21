package ch.ahoegger.docbox.server.database;

import org.eclipse.scout.rt.platform.config.AbstractStringConfigProperty;
import org.eclipse.scout.rt.platform.config.CONFIG;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.server.jdbc.derby.AbstractDerbySqlService;
import org.eclipse.scout.rt.server.jdbc.style.ISqlStyle;

import ch.ahoegger.docbox.shared.ISequenceTable;

/**
 * <h3>{@link DerbySqlService}</h3>
 *
 * @author Andreas Hoegger
 */
public class DerbySqlService extends AbstractDerbySqlService {

  @Override
  protected String getConfiguredSequenceColumnName() {
    return ISequenceTable.LAST_VAL;
  }

  @Override
  protected Class<? extends ISqlStyle> getConfiguredSqlStyle() {
    return DerbySqlStyleEx.class;
  }

  @Override
  protected String getConfiguredJdbcMappingName() {
    String databaseLocation = CONFIG.getPropertyValue(DatabaseLocationProperty.class);
    if (StringUtility.hasText(databaseLocation)) {
      return "jdbc:derby:" + databaseLocation;
    }
    else {
      throw new ProcessingException("Database location is not set!");
    }
  }

  public static class DatabaseLocationProperty extends AbstractStringConfigProperty {

    @Override
    public String getKey() {
      return "docbox.server.databaseLocation";
    }
  }
}

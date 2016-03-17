package ch.ahoegger.docbox.server.database;

import org.eclipse.scout.rt.server.jdbc.derby.AbstractDerbySqlService;
import org.eclipse.scout.rt.server.jdbc.style.ISqlStyle;

import ch.ahoegger.docbox.shared.ISequenceTable;

/**
 * <h3>{@link AbstractDocboxSqlService}</h3>
 *
 * @author Andreas Hoegger
 */
public class AbstractDocboxSqlService extends AbstractDerbySqlService {

  @Override
  protected String getConfiguredSequenceColumnName() {
    return ISequenceTable.LAST_VAL;
  }

  @Override
  protected Class<? extends ISqlStyle> getConfiguredSqlStyle() {
    return DerbySqlStyleEx.class;
  }
}

package ch.ahoegger.docbox.server.database;

import java.sql.Types;

import org.eclipse.scout.rt.server.jdbc.SqlBind;
import org.eclipse.scout.rt.server.jdbc.derby.DerbySqlStyle;

/**
 * <h3>{@link DerbySqlStyleEx}</h3>
 *
 * @author aho
 */
public class DerbySqlStyleEx extends DerbySqlStyle {

  private static final long serialVersionUID = 1L;

  @Override
  public SqlBind buildBindFor(Object o, Class nullType) {
    if (o != null && Short.class.isAssignableFrom(o.getClass())) {
      return new SqlBind(Types.SMALLINT, o);
    }
    return super.buildBindFor(o, nullType);
  }
}

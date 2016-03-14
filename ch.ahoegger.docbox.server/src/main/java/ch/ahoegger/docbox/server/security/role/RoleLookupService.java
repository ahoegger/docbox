package ch.ahoegger.docbox.server.security.role;

import java.math.BigDecimal;

import org.eclipse.scout.rt.server.jdbc.lookup.AbstractSqlLookupService;

import ch.ahoegger.docbox.server.database.SqlFramentBuilder;
import ch.ahoegger.docbox.shared.security.role.IRoleLookupService;

/**
 * <h3>{@link RoleLookupService}</h3>
 *
 * @author aho
 */
public class RoleLookupService extends AbstractSqlLookupService<BigDecimal> implements IRoleLookupService, IRoleTable {

  @Override
  protected String getConfiguredSqlSelect() {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("SELECT ").append(SqlFramentBuilder.columns(ROLE_NR, NAME));
    statementBuilder.append(" FROM ").append(TABLE_NAME);
    statementBuilder.append(" ").append(SqlFramentBuilder.WHERE_DEFAULT).append(" ");
    statementBuilder.append("<key>").append("AND ").append(ROLE_NR).append(" = :key").append("</key>");
    statementBuilder.append("<text>").append("AND UPPER(").append(NAME).append(") LIKE UPPER(:text||'%')").append("</text>");
    return statementBuilder.toString();
  }

}

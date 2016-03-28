package ch.ahoegger.docbox.server.administration.user;

import org.eclipse.scout.rt.server.jdbc.lookup.AbstractSqlLookupService;

import ch.ahoegger.docbox.shared.administration.user.IUserLookupService;
import ch.ahoegger.docbox.shared.administration.user.IUserTable;
import ch.ahoegger.docbox.shared.util.SqlFramentBuilder;

/**
 * <h3>{@link UserLookupService}</h3>
 *
 * @author Andreas Hoegger
 */
public class UserLookupService extends AbstractSqlLookupService<String> implements IUserLookupService, IUserTable {

  @Override
  protected String getConfiguredSqlSelect() {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("SELECT ").append(USERNAME).append(", ").append(FIRSTNAME).append("||' '||").append(NAME).append("||").append("' ('||").append(USERNAME).append("||')' AS DISPLAY_NAME");
    statementBuilder.append(" FROM ").append(TABLE_NAME);
    statementBuilder.append(" ").append(SqlFramentBuilder.WHERE_DEFAULT).append(" ");
    statementBuilder.append("<key>").append("AND ").append(USERNAME).append(" = :key").append("</key>");
    statementBuilder.append("<text>").append("AND UPPER(").append("DISPLAY_NAME").append(") LIKE UPPER(:text||'%')").append("</text>");
    return statementBuilder.toString();
  }

}

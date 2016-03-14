package ch.ahoegger.docbox.server.administration.user;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.scout.rt.platform.ApplicationScoped;
import org.eclipse.scout.rt.platform.holders.NVPair;
import org.eclipse.scout.rt.server.jdbc.SQL;

/**
 * <h3>{@link RoleService}</h3>
 *
 * @author aho
 */
@ApplicationScoped
public class UserRoleService implements IUserRoleTable {

  public Set<BigDecimal> getRoleIds(String username) {
    Set<BigDecimal> roleIds = new HashSet<BigDecimal>();
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("SELECT ").append(ROLE_NR).append(" FROM ").append(TABLE_NAME);
    statementBuilder.append(" WHERE ").append(USERNAME).append(" = :username");
    Object[][] rawResult = SQL.select(statementBuilder.toString(),
        new NVPair("username", username));
    for (Object[] o : rawResult) {
      roleIds.add((BigDecimal) o[0]);
    }
    return roleIds;
  }
}

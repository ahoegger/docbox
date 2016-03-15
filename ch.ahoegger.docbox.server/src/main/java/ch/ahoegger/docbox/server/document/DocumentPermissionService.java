package ch.ahoegger.docbox.server.document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.eclipse.scout.rt.platform.holders.NVPair;
import org.eclipse.scout.rt.platform.util.TypeCastUtility;
import org.eclipse.scout.rt.server.jdbc.SQL;

import ch.ahoegger.docbox.server.database.SqlFramentBuilder;
import ch.ahoegger.docbox.shared.document.IDocumentPermissionTable;
import ch.ahoegger.docbox.shared.security.permission.IPermissionService;

/**
 * <h3>{@link DocumentPermissionService}</h3>
 *
 * @author aho
 */
public class DocumentPermissionService implements IPermissionService, IDocumentPermissionTable {

  @Override
  public boolean hasReadAccess(String userId, Long entityId) {
    List<Integer> permission = new ArrayList<Integer>();
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("SELECT ").append(SqlFramentBuilder.columns(PERMISSION, USERNAME, DOCUMENT_NR)).append(" FROM ").append(TABLE_NAME);
    statementBuilder.append(" WHERE 1 = 1");
    statementBuilder.append(" AND ").append(USERNAME).append(" LIKE :userId");
    statementBuilder.append(" AND ").append(DOCUMENT_NR).append(" = :entityId");
//    statementBuilder.append(" INTO :permission");
//    SQL.selectInto(statementBuilder.toString(), new NVPair("userId", userId), new NVPair("entityId", entityId), new NVPair("permission", permission));
    Object[][] result = SQL.select(statementBuilder.toString(), new NVPair("userId", userId), new NVPair("entityId", entityId), new NVPair("permission", permission));
    if (result.length > 0) {
      return ((Long) result[0][0] >= PERMISSION_READ);
    }
//    if (permission.size() > 0) {
//      return (permission.get(0).shortValue() & PERMISSION_READ) != 0;
//    }
    return false;
  }

  @Override
  public boolean hasWriteAccess(Long entityId) {
    return false;
  }

  public Map<String, Integer> getPermissions(Long documentId) {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("SELECT ").append(SqlFramentBuilder.columns(USERNAME, PERMISSION)).append(" FROM ").append(TABLE_NAME);
    statementBuilder.append(" WHERE ").append(DOCUMENT_NR).append(" = :documentId");

    Object[][] rawResult = SQL.select(statementBuilder.toString(),
        new NVPair("documentId", documentId));
    return Arrays.stream(rawResult).collect(Collectors.toMap(
        row -> (String) TypeCastUtility.castValue(row[0], String.class),
        row -> (Integer) TypeCastUtility.castValue(row[1], Integer.class),
        (p1, p2) -> Math.max(p1, p2)));
  }

  public void createDocumentPermissions(Long documentId, Map<String, Integer> permissions) {
    for (Entry<String, Integer> permission : permissions.entrySet()) {
      StringBuilder statementBuilder = new StringBuilder();
      statementBuilder.append("INSERT INTO ").append(TABLE_NAME).append(" (");
      statementBuilder.append(SqlFramentBuilder.columns(DOCUMENT_NR, USERNAME, PERMISSION));
      statementBuilder.append(") VALUES (");
      statementBuilder.append(":documentId, :username, :permission");
      statementBuilder.append(")");
      SQL.insert(statementBuilder.toString(),
          new NVPair("documentId", documentId),
          new NVPair("username", permission.getKey()),
          new NVPair("permission", permission.getValue()));
    }
  }
}

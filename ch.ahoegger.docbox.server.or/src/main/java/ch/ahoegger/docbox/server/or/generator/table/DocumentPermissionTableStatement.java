package ch.ahoegger.docbox.server.or.generator.table;

import ch.ahoegger.docbox.or.definition.table.IDocumentPermissionTable;

/**
 * <h3>{@link DocumentPermissionTableStatement}</h3>
 *
 * @author Andreas Hoegger
 */
public class DocumentPermissionTableStatement implements ITableStatement, IDocumentPermissionTable {
  @Override
  public String getCreateTable() {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("CREATE TABLE ").append(TABLE_NAME).append(" ( ");
    statementBuilder.append(USERNAME).append(" VARCHAR(").append(USERNAME_LENGTH).append(") NOT NULL, ");
    statementBuilder.append(DOCUMENT_NR).append(" BIGINT NOT NULL, ");
    statementBuilder.append(PERMISSION).append(" INT NOT NULL, ");
    statementBuilder.append("PRIMARY KEY (").append(ITableStatement.columns(USERNAME, DOCUMENT_NR))
        .append(")");
    statementBuilder.append(" )");
    return statementBuilder.toString();
  }

}

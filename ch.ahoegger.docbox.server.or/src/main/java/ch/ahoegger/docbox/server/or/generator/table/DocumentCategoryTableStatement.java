package ch.ahoegger.docbox.server.or.generator.table;

import ch.ahoegger.docbox.or.definition.table.IDocumentCategoryTable;

/**
 * <h3>{@link DocumentCategoryTableStatement}</h3>
 *
 * @author Andreas Hoegger
 */
public class DocumentCategoryTableStatement implements ITableStatement, IDocumentCategoryTable {

  @Override
  public String getCreateTable() {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("CREATE TABLE ").append(TABLE_NAME).append(" (");
    statementBuilder.append(DOCUMENT_NR).append(" BIGINT NOT NULL, ");
    statementBuilder.append(CATEGORY_NR).append(" BIGINT NOT NULL, ");
    statementBuilder.append("PRIMARY KEY (").append(ITableStatement.columns(DOCUMENT_NR, CATEGORY_NR))
        .append(")");
    statementBuilder.append(")");
    return statementBuilder.toString();
  }

}

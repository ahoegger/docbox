package ch.ahoegger.docbox.server.or.generator.table;

import ch.ahoegger.docbox.or.definition.table.IDocumentOcrTable;

/**
 * <h3>{@link DocumentOcrTableStatement}</h3>
 *
 * @author Andreas Hoegger
 */
public class DocumentOcrTableStatement implements ITableStatement, IDocumentOcrTable {

  @Override
  public String getCreateTable() {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("CREATE TABLE ").append(TABLE_NAME).append(" (");
    statementBuilder.append(DOCUMENT_NR).append(" BIGINT NOT NULL, ");
    statementBuilder.append(TEXT).append(" CLOB(").append(TEXT_LENGHT).append(") , ");
    statementBuilder.append(OCR_SCANNED).append(" BOOLEAN NOT NULL, ");
    statementBuilder.append(PARSE_FAILED).append(" BOOLEAN NOT NULL, ");
    statementBuilder.append("PRIMARY KEY (").append(ITableStatement.columns(DOCUMENT_NR)).append(")");
    statementBuilder.append(")");
    return statementBuilder.toString();
  }
}

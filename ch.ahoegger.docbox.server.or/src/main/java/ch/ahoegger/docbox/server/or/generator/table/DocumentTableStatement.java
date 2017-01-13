package ch.ahoegger.docbox.server.or.generator.table;

import ch.ahoegger.docbox.or.definition.table.IDocumentTable;

/**
 * <h3>{@link DocumentTableStatement}</h3>
 *
 * @author Andreas Hoegger
 */
public class DocumentTableStatement implements ITableStatement, IDocumentTable {

  @Override
  public String getCreateTable() {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("CREATE TABLE ").append(TABLE_NAME).append(" ( ");
    statementBuilder.append(DOCUMENT_NR).append(" BIGINT NOT NULL, ");
    statementBuilder.append(ABSTRACT).append(" VARCHAR(").append(ABSTRACT_LENGTH).append(") NOT NULL, ");
    statementBuilder.append(DOCUMENT_DATE).append(" DATE NOT NULL, ");
    statementBuilder.append(INSERT_DATE).append(" DATE NOT NULL, ");
    statementBuilder.append(VALID_DATE).append(" DATE, ");
    statementBuilder.append(DOCUMENT_URL).append(" VARCHAR(").append(DOCUMENT_URL_LENGTH).append("), ");
    statementBuilder.append(ORIGINAL_STORAGE).append(" VARCHAR(").append(ORIGINAL_STORAGE_LENGTH).append("), ");
    statementBuilder.append(CONVERSATION_NR).append(" BIGINT, ");
    statementBuilder.append(PARSE_OCR).append(" BOOLEAN NOT NULL, ");
    statementBuilder.append("PRIMARY KEY (").append(DOCUMENT_NR).append(")");
    statementBuilder.append(" )");
    return statementBuilder.toString();
  }

}

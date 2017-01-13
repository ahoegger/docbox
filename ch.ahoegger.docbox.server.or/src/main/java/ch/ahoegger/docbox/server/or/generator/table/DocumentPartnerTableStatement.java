package ch.ahoegger.docbox.server.or.generator.table;

import ch.ahoegger.docbox.or.definition.table.IDocumentPartnerTable;

/**
 * <h3>{@link DocumentPartnerTableStatement}</h3>
 *
 * @author Andreas Hoegger
 */
public class DocumentPartnerTableStatement implements ITableStatement, IDocumentPartnerTable {

  @Override
  public String getCreateTable() {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("CREATE TABLE ").append(TABLE_NAME).append(" (");
    statementBuilder.append(DOCUMENT_NR).append(" BIGINT NOT NULL, ");
    statementBuilder.append(PARTNER_NR).append(" BIGINT NOT NULL, ");
    statementBuilder.append("PRIMARY KEY (").append(ITableStatement.columns(DOCUMENT_NR, PARTNER_NR)).append(")");
    statementBuilder.append(")");
    return statementBuilder.toString();
  }

}

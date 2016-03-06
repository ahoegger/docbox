package ch.ahoegger.docbox.server.document;

import org.eclipse.scout.rt.platform.holders.NVPair;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.server.jdbc.SQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.server.database.SqlFramentBuilder;
import ch.ahoegger.docbox.shared.document.DocumentFormData;
import ch.ahoegger.docbox.shared.document.DocumentSearchFormData;
import ch.ahoegger.docbox.shared.document.DocumentTableData;
import ch.ahoegger.docbox.shared.document.IDocumentService;

/**
 * <h3>{@link DocumentService}</h3>
 *
 * @author aho
 */
public class DocumentService implements IDocumentService, IDocumentTable {
  private static final Logger LOG = LoggerFactory.getLogger(DocumentService.class);

  @Override
  public DocumentTableData getTableData(DocumentSearchFormData formData) {
    StringBuilder sqlBuilder = new StringBuilder();
    sqlBuilder.append("SELECT ");
    sqlBuilder.append(DOCUMENT_NR).append(", ");
    sqlBuilder.append(ABSTRACT).append(" ");
    sqlBuilder.append("FROM ").append(TABLE_NAME);
    sqlBuilder.append(" WHERE 1 = 1");
    if (StringUtility.hasText(formData.getAbstract().getValue())) {
      sqlBuilder.append(" AND ").append(SqlFramentBuilder.whereStringContains(ABSTRACT, formData.getAbstract().getValue()));
    }
    sqlBuilder.append(" INTO ");
    sqlBuilder.append(":{td.documentId}, ");
    sqlBuilder.append(":{td.abstract} ");
    DocumentTableData tableData = new DocumentTableData();
    SQL.selectInto(sqlBuilder.toString(), new NVPair("td", tableData));
    return tableData;
  }

  @Override
  public void store(DocumentFormData formData) {
    LOG.debug("Store document.");

  }
}

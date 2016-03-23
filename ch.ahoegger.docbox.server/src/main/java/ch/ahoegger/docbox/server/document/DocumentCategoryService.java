package ch.ahoegger.docbox.server.document;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.scout.rt.platform.ApplicationScoped;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.holders.NVPair;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.platform.util.TypeCastUtility;
import org.eclipse.scout.rt.server.jdbc.SQL;
import org.eclipse.scout.rt.shared.servicetunnel.RemoteServiceAccessDenied;

import ch.ahoegger.docbox.server.database.SqlFramentBuilder;
import ch.ahoegger.docbox.shared.backup.IBackupService;
import ch.ahoegger.docbox.shared.document.IDocumentCategoryTable;

/**
 * <h3>{@link DocumentCategoryService}</h3>
 *
 * @author Andreas Hoegger
 */
@ApplicationScoped
public class DocumentCategoryService implements IDocumentCategoryTable {

  public Set<BigDecimal> getCategoryIds(Long documentId) {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("SELECT ").append(CATEGORY_NR).append(" FROM ").append(TABLE_NAME);
    statementBuilder.append(" WHERE ").append(DOCUMENT_NR).append(" = :documentId");

    Object[][] rawResult = SQL.select(statementBuilder.toString(),
        new NVPair("documentId", documentId));
    return Arrays.stream(rawResult).map(row -> TypeCastUtility.castValue(row[0], BigDecimal.class)).collect(Collectors.toSet());
  }

  public void createDocumentCategories(Long documentId, Set<BigDecimal> categoryIds) {
    if (CollectionUtility.hasElements(categoryIds)) {
      for (BigDecimal categoryId : categoryIds) {
        StringBuilder statementBuilder = new StringBuilder();
        statementBuilder.append("INSERT INTO ").append(TABLE_NAME).append(" (");
        statementBuilder.append(SqlFramentBuilder.columns(DOCUMENT_NR, CATEGORY_NR));
        statementBuilder.append(") VALUES (");
        statementBuilder.append(":documentId, :categoryId");
        statementBuilder.append(")");
        SQL.insert(statementBuilder.toString(),
            new NVPair("documentId", documentId),
            new NVPair("categoryId", categoryId));
      }

      // notify backup needed
      BEANS.get(IBackupService.class).notifyModification();

    }
  }

  public void deleteByCategoryId(Long categoryId) {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("DELETE FROM ").append(TABLE_NAME).append(" WHERE ").append(CATEGORY_NR).append(" = :categoryId");
    SQL.delete(statementBuilder.toString(), new NVPair("categoryId", categoryId));

    // notify backup needed
    BEANS.get(IBackupService.class).notifyModification();

  }

  /**
   * @param documentId
   * @param value
   */
  @RemoteServiceAccessDenied
  public void updateDocumentCategories(Long documentId, Set<BigDecimal> categoryIds) {
    deleteByDocumentId(documentId);
    createDocumentCategories(documentId, categoryIds);
  }

  @RemoteServiceAccessDenied
  public void deleteByDocumentId(Long documentId) {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("DELETE FROM ").append(TABLE_NAME).append(" WHERE ").append(DOCUMENT_NR).append(" = :documentId");
    SQL.delete(statementBuilder.toString(), new NVPair("documentId", documentId));

    // notify backup needed
    BEANS.get(IBackupService.class).notifyModification();

  }
}

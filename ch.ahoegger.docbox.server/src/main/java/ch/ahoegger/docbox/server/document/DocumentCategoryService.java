package ch.ahoegger.docbox.server.document;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Set;
import java.util.stream.Collectors;

import org.ch.ahoegger.docbox.server.or.app.tables.DocumentCategory;
import org.ch.ahoegger.docbox.server.or.app.tables.records.DocumentCategoryRecord;
import org.eclipse.scout.rt.platform.ApplicationScoped;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.server.jdbc.SQL;
import org.eclipse.scout.rt.shared.servicetunnel.RemoteServiceAccessDenied;
import org.jooq.InsertValuesStep2;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import ch.ahoegger.docbox.or.definition.table.IDocumentCategoryTable;
import ch.ahoegger.docbox.shared.backup.IBackupService;

/**
 * <h3>{@link DocumentCategoryService}</h3>
 *
 * @author Andreas Hoegger
 */
@ApplicationScoped
public class DocumentCategoryService implements IDocumentCategoryTable {

  public Set<BigDecimal> getCategoryIds(BigDecimal documentId) {
    DocumentCategory dc = DocumentCategory.DOCUMENT_CATEGORY;

    return DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .select(dc.CATEGORY_NR)
        .from(dc)
        .where(dc.DOCUMENT_NR.eq(documentId))
        .fetch()
        .stream()
        .map(rec -> rec.get(dc.CATEGORY_NR))
        .collect(Collectors.toSet());
  }

  public void createDocumentCategories(BigDecimal documentId, Set<BigDecimal> categoryIds) {
    if (categoryIds == null) {
      return;
    }
    DocumentCategory dc = DocumentCategory.DOCUMENT_CATEGORY;
    InsertValuesStep2<DocumentCategoryRecord, BigDecimal, BigDecimal> insertInto = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .insertInto(dc, dc.DOCUMENT_NR, dc.CATEGORY_NR);

    categoryIds.forEach(catId -> {
      insertInto.values(documentId, catId);
    });

    insertInto.execute();

    // notify backup needed
    BEANS.get(IBackupService.class).notifyModification();
  }

  public void deleteByCategoryId(BigDecimal categoryId) {
    DocumentCategory dc = DocumentCategory.DOCUMENT_CATEGORY;

    DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .delete(dc)
        .where(dc.CATEGORY_NR.eq(categoryId))
        .execute();

    // notify backup needed
    BEANS.get(IBackupService.class).notifyModification();

  }

  /**
   * @param documentId
   * @param value
   */
  @RemoteServiceAccessDenied
  public void updateDocumentCategories(BigDecimal documentId, Set<BigDecimal> categoryIds) {
    deleteByDocumentId(documentId);
    createDocumentCategories(documentId, categoryIds);
  }

  @RemoteServiceAccessDenied
  public void deleteByDocumentId(BigDecimal documentId) {
    DocumentCategory dc = DocumentCategory.DOCUMENT_CATEGORY;

    DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .delete(dc)
        .where(dc.DOCUMENT_NR.eq(documentId))
        .execute();

    // notify backup needed
    BEANS.get(IBackupService.class).notifyModification();

  }

  @RemoteServiceAccessDenied
  public int insert(Connection connection, BigDecimal documentId, BigDecimal categoryId) {
    return DSL.using(connection, SQLDialect.DERBY)
        .executeInsert(toRecord(documentId, categoryId));
  }

  protected DocumentCategoryRecord toRecord(BigDecimal documentId, BigDecimal categoryId) {
    return new DocumentCategoryRecord()
        .with(DocumentCategory.DOCUMENT_CATEGORY.CATEGORY_NR, categoryId)
        .with(DocumentCategory.DOCUMENT_CATEGORY.DOCUMENT_NR, documentId);
  }
}

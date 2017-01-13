package ch.ahoegger.docbox.server.ocr;

import java.math.BigDecimal;

import org.ch.ahoegger.docbox.server.or.app.tables.DocumentOcr;
import org.ch.ahoegger.docbox.server.or.app.tables.records.DocumentOcrRecord;
import org.eclipse.scout.rt.platform.ApplicationScoped;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.server.jdbc.SQL;
import org.eclipse.scout.rt.shared.servicetunnel.RemoteServiceAccessDenied;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.shared.backup.IBackupService;
import ch.ahoegger.docbox.shared.document.ocr.DocumentOcrFormData;
import ch.ahoegger.docbox.shared.ocr.IDocumentOcrService;

/**
 * <h3>{@link DocumentOcrService}</h3>
 *
 * @author Andreas Hoegger
 */
@ApplicationScoped
public class DocumentOcrService implements IDocumentOcrService {
  private static final Logger LOG = LoggerFactory.getLogger(DocumentOcrService.class);

  @RemoteServiceAccessDenied
  public void create(BigDecimal documentId, OcrParseResult parseResult) {

    DocumentOcr docOcr = DocumentOcr.DOCUMENT_OCR.as("DOC_OCR");
    int rowCount = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .newRecord(docOcr)
        .with(docOcr.DOCUMENT_NR, documentId)
        .with(docOcr.OCR_SCANNED, parseResult.isOcrParsed())
        .with(docOcr.PARSE_FAILED, false)
        .with(docOcr.TEXT, parseResult.getText())
        .insert();

    if (rowCount == 1) {
      // notify backup needed
      BEANS.get(IBackupService.class).notifyModification();
    }
  }

  @Override
  public DocumentOcrFormData load(DocumentOcrFormData formData) {
    DocumentOcr docOcr = DocumentOcr.DOCUMENT_OCR.as("DOC_OCR");
    return DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .fetch(docOcr, docOcr.DOCUMENT_NR.eq(formData.getDocumentId()))
        .stream()
        .map(rec -> {
          DocumentOcrFormData fd = new DocumentOcrFormData();
          fd.setDocumentId(rec.get(docOcr.DOCUMENT_NR));
          fd.getOcrParsed().setValue(rec.get(docOcr.OCR_SCANNED));
          fd.getNotParsable().setValue(rec.get(docOcr.PARSE_FAILED));
          fd.getText().setValue(rec.get(docOcr.TEXT));
          return fd;
        })
        .findFirst()
        .orElse(null);
  }

  /**
   * @param documentId
   * @return
   */
  public Boolean exists(BigDecimal documentId) {
    DocumentOcrFormData fd = new DocumentOcrFormData();
    fd.setDocumentId(documentId);
    return load(fd) != null;
  }

  /**
   * @param documentId
   * @param value
   */
  public boolean delete(BigDecimal documentId) {

    DocumentOcr docOcr = DocumentOcr.DOCUMENT_OCR.as("DOC_OCR");

    DocumentOcrRecord rec = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .fetchOne(docOcr, docOcr.DOCUMENT_NR.eq(documentId));
    if (rec == null) {
      LOG.warn("Try to delete not existing record with id '{}'!", documentId);
      return false;
    }
    if (rec.delete() != 1) {
      LOG.error("Could not delete record with id '{}'!", documentId);
      return false;
    }

    // notify backup needed
    BEANS.get(IBackupService.class).notifyModification();
    return true;
  }

}

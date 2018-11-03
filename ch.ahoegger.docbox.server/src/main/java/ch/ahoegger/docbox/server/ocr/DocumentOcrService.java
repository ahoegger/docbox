package ch.ahoegger.docbox.server.ocr;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Optional;

import org.ch.ahoegger.docbox.server.or.app.tables.DocumentOcr;
import org.ch.ahoegger.docbox.server.or.app.tables.records.DocumentOcrRecord;
import org.eclipse.scout.rt.platform.ApplicationScoped;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingStatus;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.status.IStatus;
import org.eclipse.scout.rt.server.jdbc.SQL;
import org.eclipse.scout.rt.shared.servicetunnel.RemoteServiceAccessDenied;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.server.util.FieldValidator;
import ch.ahoegger.docbox.shared.backup.IBackupService;
import ch.ahoegger.docbox.shared.document.OcrResultGroupBoxData;
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
  public void updateOrCreate(BigDecimal documentId, OcrParseResult parseResult) {
    boolean create = false;
    OcrResultGroupBoxData fd = new OcrResultGroupBoxData();
    fd.setDocumentId(documentId);
    fd = load(fd);
    if (fd == null) {
      fd = new OcrResultGroupBoxData();
      fd.setDocumentId(documentId);
      create = true;
    }
    fd.getParseCount().setValue(Optional.ofNullable(fd.getParseCount().getValue()).map(c -> c + 1).orElse(1));
    fd.getOcrParsed().setValue(parseResult.isOcrParsed());
    fd.getParsedText().setValue(parseResult.getText());
    fd.getParseFailedReason().setValue(Optional.ofNullable(parseResult.getParseError()).map(r -> r.toString()).orElse(null));

    if (create) {
      create(fd);
    }
    else {
      storeInternal(fd, new FieldValidator());
    }
  }

  protected OcrResultGroupBoxData create(OcrResultGroupBoxData formData) {
    DocumentOcr docOcr = DocumentOcr.DOCUMENT_OCR.as("DOC_OCR");

    int rowCount = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .newRecord(docOcr)
        .with(docOcr.DOCUMENT_NR, formData.getDocumentId())
        .with(docOcr.FAILED_REASON, formData.getParseFailedReason().getValue())
        .with(docOcr.OCR_SCANNED, formData.getOcrParsed().getValue())
        .with(docOcr.PARSE_COUNT, formData.getParseCount().getValue())
        .with(docOcr.TEXT, formData.getParsedText().getValue())
        .insert();
    if (rowCount == 1) {
      // notify backup needed
      BEANS.get(IBackupService.class).notifyModification();
      return formData;
    }
    return null;
  }

  @Override
  public OcrResultGroupBoxData load(OcrResultGroupBoxData formData) {
    DocumentOcr docOcr = DocumentOcr.DOCUMENT_OCR.as("DOC_OCR");
    return DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .fetch(docOcr, docOcr.DOCUMENT_NR.eq(formData.getDocumentId()))
        .stream()
        .map(rec -> {
          OcrResultGroupBoxData fd = new OcrResultGroupBoxData();
          fd.setDocumentId(rec.get(docOcr.DOCUMENT_NR));
          fd.getOcrParsed().setValue(rec.get(docOcr.OCR_SCANNED));
          fd.getParseCount().setValue(rec.get(docOcr.PARSE_COUNT));
          fd.getParseFailedReason().setValue(rec.get(docOcr.FAILED_REASON));
          fd.getParsedText().setValue(rec.get(docOcr.TEXT));
          return fd;
        })
        .findFirst()
        .orElse(null);
  }

  @Override
  public OcrResultGroupBoxData store(OcrResultGroupBoxData formData) {

    DocumentOcr e = DocumentOcr.DOCUMENT_OCR;
    // validations
    FieldValidator validator = new FieldValidator();
    validator.add(FieldValidator.unmodifiableValidator(e.FAILED_REASON, formData.getParseFailedReason().getValue()));
    validator.add(FieldValidator.unmodifiableValidator(e.PARSE_COUNT, formData.getParseCount().getValue()));
    validator.add(FieldValidator.unmodifiableValidator(e.OCR_SCANNED, formData.getOcrParsed().getValue()));
    return storeInternal(formData, validator);

  }

  @RemoteServiceAccessDenied
  protected OcrResultGroupBoxData storeInternal(OcrResultGroupBoxData formData, FieldValidator validator) {
    DocumentOcr e = DocumentOcr.DOCUMENT_OCR;
    DocumentOcrRecord rec = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .fetchOne(e, e.DOCUMENT_NR.eq(formData.getDocumentId()));

    if (rec == null) {
      return null;
    }
    IStatus validateStatus = validator.validate(rec);
    if (!validateStatus.isOK()) {
      throw new VetoException(new ProcessingStatus(validateStatus));
    }

    int rowCount = rec.with(e.FAILED_REASON, formData.getParseFailedReason().getValue())
        .with(e.OCR_SCANNED, formData.getOcrParsed().getValue())
        .with(e.PARSE_COUNT, formData.getParseCount().getValue())
        .with(e.TEXT, formData.getParsedText().getValue())
        .update();

    if (rowCount == 1) {
      // notify backup needed
      BEANS.get(IBackupService.class).notifyModification();

    }
    return formData;
  }

  /**
   * @param documentId
   * @return
   */
  public Boolean exists(BigDecimal documentId) {
    OcrResultGroupBoxData fd = new OcrResultGroupBoxData();
    fd.setDocumentId(documentId);
    return load(fd) != null && fd.getParseFailedReason().getValue() == null;
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

  @RemoteServiceAccessDenied
  public int insert(Connection connection, BigDecimal documentId, String text, boolean parsed, int parseCount, String parseFailedReason) {
    return DSL.using(connection, SQLDialect.DERBY)
        .executeInsert(toRecord(documentId, text, parsed, parseCount, parseFailedReason));
  }

  protected DocumentOcrRecord toRecord(BigDecimal documentId, String text, boolean parsed, int parseCount, String parseFailedReason) {
    return new DocumentOcrRecord()
        .with(DocumentOcr.DOCUMENT_OCR.DOCUMENT_NR, documentId)
        .with(DocumentOcr.DOCUMENT_OCR.OCR_SCANNED, parsed)
        .with(DocumentOcr.DOCUMENT_OCR.PARSE_COUNT, parseCount)
        .with(DocumentOcr.DOCUMENT_OCR.FAILED_REASON, parseFailedReason)
        .with(DocumentOcr.DOCUMENT_OCR.TEXT, text);
  }

}

package ch.ahoegger.docbox.server.ocr;

import org.eclipse.scout.rt.platform.ApplicationScoped;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.holders.BooleanHolder;
import org.eclipse.scout.rt.platform.holders.NVPair;
import org.eclipse.scout.rt.server.jdbc.SQL;
import org.eclipse.scout.rt.shared.servicetunnel.RemoteServiceAccessDenied;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.shared.backup.IBackupService;
import ch.ahoegger.docbox.shared.document.ocr.DocumentOcrFormData;
import ch.ahoegger.docbox.shared.ocr.IDocumentOcrService;
import ch.ahoegger.docbox.shared.ocr.IDocumentOcrTable;
import ch.ahoegger.docbox.shared.util.SqlFramentBuilder;

/**
 * <h3>{@link DocumentOcrService}</h3>
 *
 * @author Andreas Hoegger
 */
@ApplicationScoped
public class DocumentOcrService implements IDocumentOcrService, IDocumentOcrTable {
  @SuppressWarnings("unused")
  private static final Logger LOG = LoggerFactory.getLogger(DocumentOcrService.class);

  @RemoteServiceAccessDenied
  public void create(Long documentId, OcrParseResult parseResult) {
    String text = null;
    boolean ocrParsed = false;
    boolean notParsable = false;
    text = parseResult.getText();
    ocrParsed = parseResult.isOcrParsed();
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("INSERT INTO ").append(TABLE_NAME).append(" (");
    statementBuilder.append(SqlFramentBuilder.columns(DOCUMENT_NR, TEXT, OCR_SCANNED, PARSE_FAILED));
    statementBuilder.append(") VALUES (");
    statementBuilder.append(":documentId, :text, :ocrParsed, :notParsable");
    statementBuilder.append(")");
    SQL.insert(statementBuilder.toString(),
        new NVPair("documentId", documentId),
        new NVPair("text", text),
        new NVPair("ocrParsed", ocrParsed),
        new NVPair("notParsable", notParsable));

    // notify backup needed
    BEANS.get(IBackupService.class).notifyModification();
  }

  @Override
  public DocumentOcrFormData load(DocumentOcrFormData formData) {
    BooleanHolder exists = new BooleanHolder();
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("SELECT TRUE, ").append(SqlFramentBuilder.columns(SqlFramentBuilder.columns(TEXT, OCR_SCANNED, PARSE_FAILED)));
    statementBuilder.append(" FROM ").append(TABLE_NAME);
    statementBuilder.append(" WHERE ").append(DOCUMENT_NR).append(" = :documentId");
    statementBuilder.append(" INTO :exists,  :text, :ocrParsed, :notParsable");
    SQL.selectInto(statementBuilder.toString(),
        new NVPair("exists", exists),
        formData);
    if (exists.getValue() == null) {
      return null;
    }
    return formData;
  }

  /**
   * @param documentId
   * @return
   */
  public Boolean exists(Long documentId) {
    BooleanHolder exists = new BooleanHolder();
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("SELECT TRUE ");
    statementBuilder.append(" FROM ").append(TABLE_NAME);
    statementBuilder.append(" WHERE ").append(DOCUMENT_NR).append(" = :documentId");
    statementBuilder.append(" INTO :exists");
    SQL.selectInto(statementBuilder.toString(),
        new NVPair("documentId", documentId),
        new NVPair("exists", exists));
    if (exists.getValue() == null) {
      return Boolean.FALSE;
    }
    return Boolean.TRUE;
  }

  /**
   * @param documentId
   * @param value
   */
  public void delete(Long documentId) {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("DELETE FROM ").append(TABLE_NAME).append(" WHERE ").append(DOCUMENT_NR).append(" = :documentId");
    SQL.delete(statementBuilder.toString(), new NVPair("documentId", documentId));

    // notify backup needed
    BEANS.get(IBackupService.class).notifyModification();
  }

}

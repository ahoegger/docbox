package ch.ahoegger.docbox.server.ocr;

import java.math.BigDecimal;
import java.security.AccessController;
import java.util.concurrent.Callable;

import javax.security.auth.Subject;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.job.IFuture;
import org.eclipse.scout.rt.platform.job.Jobs;
import org.eclipse.scout.rt.platform.resource.BinaryResource;
import org.eclipse.scout.rt.platform.transaction.TransactionScope;
import org.eclipse.scout.rt.platform.util.concurrent.IRunnable;
import org.eclipse.scout.rt.server.clientnotification.ClientNotificationRegistry;
import org.eclipse.scout.rt.server.context.ServerRunContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.shared.document.ocr.DocumentParsedNotification;
import ch.ahoegger.docbox.shared.document.store.IDocumentStoreService;

/**
 * <h3>{@link ParseDocumentRunnable}</h3>
 *
 * @author Andreas Hoegger
 */
public class ParseDocumentRunnable implements IRunnable {

  private static final Logger LOG = LoggerFactory.getLogger(ParseDocumentRunnable.class);

  private IOcrParser m_parser;

  private OcrParseService m_parseService;

  ParseDocumentRunnable(OcrParseService parseService) {
    m_parseService = parseService;
    m_parser = BEANS.get(OcrParserProvider.class).getParser();
    LOG.debug("Using '{}' as ocr parser.", m_parser.getClass().getName());
  }

  @Override
  public void run() throws Exception {
    ParseDescription description = m_parseService.popNextDescription();
    while (description != null) {
      BinaryResource resource = getBinaryResource(description.getDocumentId()).awaitDoneAndGet();

      OcrParseResult parseResult = m_parser.parsePdf(resource, description.getLanguage());
      // store
      storeResult(description.getDocumentId(), parseResult).awaitDone();
      description = m_parseService.popNextDescription();
    }
  }

  protected IFuture<Void> storeResult(BigDecimal documentId, OcrParseResult result) {

    return Jobs.schedule(new IRunnable() {

      @Override
      public void run() throws Exception {
        BEANS.get(DocumentOcrService.class).updateOrCreate(documentId, result);
        BEANS.get(ClientNotificationRegistry.class).putForAllSessions(new DocumentParsedNotification().withDocumentId(documentId));

      }
    }, Jobs.newInput().withRunContext(
        ServerRunContexts.empty()
            .withSubject(Subject.getSubject(AccessController.getContext()))
            .withTransactionScope(TransactionScope.REQUIRES_NEW)));

  }

  protected IFuture<BinaryResource> getBinaryResource(final BigDecimal documentId) {
    return Jobs.schedule(new Callable<BinaryResource>() {
      @Override
      public BinaryResource call() throws Exception {
        return BEANS.get(IDocumentStoreService.class).getDocument(documentId);
      }
    }, Jobs.newInput().withRunContext(
        ServerRunContexts.empty()
            .withSubject(Subject.getSubject(AccessController.getContext()))
            .withTransactionScope(TransactionScope.REQUIRES_NEW)));

  }

}

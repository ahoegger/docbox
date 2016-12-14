package ch.ahoegger.docbox.server.ocr;

import java.security.AccessController;
import java.util.concurrent.Callable;

import javax.security.auth.Subject;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.context.RunContexts;
import org.eclipse.scout.rt.platform.job.IFuture;
import org.eclipse.scout.rt.platform.job.Jobs;
import org.eclipse.scout.rt.platform.resource.BinaryResource;
import org.eclipse.scout.rt.platform.transaction.TransactionScope;
import org.eclipse.scout.rt.platform.util.concurrent.IRunnable;
import org.eclipse.scout.rt.server.context.ServerRunContexts;

import ch.ahoegger.docbox.shared.document.store.IDocumentStoreService;

/**
 * <h3>{@link ParseDocumentJob}</h3>
 *
 * @author aho
 */
public class ParseDocumentJob {

  private Long m_documentId;

  public ParseDocumentJob(Long documentId) {
    m_documentId = documentId;
  }

  public Long getDocumentId() {
    return m_documentId;
  }

  public IFuture<String> schedule() {
    return Jobs.schedule(new Callable<String>() {
      @Override
      public String call() throws Exception {
        BinaryResource resource = getBinaryResource(getDocumentId()).awaitDoneAndGet();
        OcrParseResult parseResult = BEANS.get(OcrParseService.class).parsePdf(resource);
        if (parseResult != null) {
          persist(getDocumentId(), parseResult);
          return parseResult.getText();
        }
        return null;
      }
    }, Jobs.newInput().withRunContext(RunContexts.empty().withSubject(Subject.getSubject(AccessController.getContext()))));
  }

  protected IFuture<BinaryResource> getBinaryResource(final Long documentId) {
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

  protected IFuture<Void> persist(Long documentId, OcrParseResult result) {
    return Jobs.schedule(new IRunnable() {

      @Override
      public void run() throws Exception {
        BEANS.get(DocumentOcrService.class).create(documentId, result);

      }
    }, Jobs.newInput().withRunContext(
        ServerRunContexts.empty()
            .withSubject(Subject.getSubject(AccessController.getContext()))
            .withTransactionScope(TransactionScope.REQUIRES_NEW)));

  }

}

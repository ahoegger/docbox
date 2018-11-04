package ch.ahoegger.docbox.server.ocr;

import java.security.AccessController;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import javax.security.auth.Subject;

import org.eclipse.scout.rt.platform.context.RunContexts;
import org.eclipse.scout.rt.platform.job.IFuture;
import org.eclipse.scout.rt.platform.job.Jobs;
import org.eclipse.scout.rt.platform.util.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <h3>{@link OcrParseService}</h3>
 *
 * @author Andreas Hoegger
 */
public class OcrParseService implements IOcrParseService {

  private static final Logger LOG = LoggerFactory.getLogger(OcrParseService.class);

  private LinkedList<ParseDescription> m_pendingDescriptions = new LinkedList<>();

  private Object PROCESS_OCR_LOCK = new Object();
  private IFuture<Void> m_ocrParseFeature = null;

  @Override
  public IFuture<Void> schedule(ParseDescription parseDescription) {
    Assertions.assertNotNull(parseDescription);
    synchronized (PROCESS_OCR_LOCK) {
      m_pendingDescriptions.add(parseDescription);
      // start parse job if not running
      if (m_ocrParseFeature == null) {
        m_ocrParseFeature = startJob();
      }
      return m_ocrParseFeature;
    }
  }

  Object getProcessOcrLock() {
    return PROCESS_OCR_LOCK;
  }

  ParseDescription popNextDescription() {
    synchronized (PROCESS_OCR_LOCK) {
      ParseDescription desc = m_pendingDescriptions.poll();
      if (desc == null) {
        m_ocrParseFeature = null;
        LOG.debug("End parse document job.");
      }
      return desc;
    }
  }

  private IFuture<Void> startJob() {
    LOG.debug("Start parse document job.");
    return Jobs.schedule(new ParseDocumentRunnable(this),
        Jobs.newInput()
            .withExecutionTrigger(Jobs.newExecutionTrigger().withStartIn(300, TimeUnit.MILLISECONDS))
            .withRunContext(RunContexts.empty().withSubject(Subject.getSubject(AccessController.getContext()))));

  }

  @Override
  public IFuture<Void> getCurrentParsingFeature() {
    synchronized (PROCESS_OCR_LOCK) {
      return m_ocrParseFeature;
    }
  }

}

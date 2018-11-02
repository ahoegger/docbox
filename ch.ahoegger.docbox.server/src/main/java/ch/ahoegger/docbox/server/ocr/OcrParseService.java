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
      }
      return desc;
    }
  }

  private IFuture<Void> startJob() {
    return Jobs.schedule(new ParseDocumentRunnable(this),
        Jobs.newInput()
            .withExecutionTrigger(Jobs.newExecutionTrigger().withStartIn(2, TimeUnit.SECONDS))
            .withRunContext(RunContexts.empty().withSubject(Subject.getSubject(AccessController.getContext()))));

  }

  @Override
  public IFuture<Void> getCurrentParsingFeature() {
    synchronized (PROCESS_OCR_LOCK) {
      return m_ocrParseFeature;
    }
  }

//  public static class OcrParserProperty extends AbstractConfigProperty<Class<? extends IOcrParser>, String> implements IStartupValidatableBean {
//    private static final Logger LOG = LoggerFactory.getLogger(OcrParserProperty.class);
//
//    public static final String BUILD_REPLACEMENT_VAR = "${docbox.ocr.parser}";
//
//    @Override
//    public String getKey() {
//      return "docbox.ocr.parser";
//    }
//
//    @Override
//    public Class<? extends IOcrParser> getDefaultValue() {
//      return ShellScriptOcrParser.class;
//    }
//
//    @SuppressWarnings("unchecked")
//    @Override
//    protected Class<? extends IOcrParser> parse(String value) {
//      try {
//        return (Class<? extends IOcrParser>) Class.forName(value);
//      }
//      catch (ClassNotFoundException e) {
//        throw new ProcessingException(new ProcessingStatus(String.format("Cold not parse '%s' property (see '%s' for details).", getKey(), OcrParserProperty.class.getName()), e, 0, IStatus.ERROR));
//      }
//    }
//
//    @Override
//    public String description() {
//      return "The class of the OCR parser.";
//    }
//
//    @Override
//    public boolean validate() {
//      try {
//        getValue();
//      }
//      catch (Exception e) {
//        LOG.error("ConfigProperty: '{}' does not exist.", getKey(), e);
//        return false;
//      }
//      return true;
//    }
//  }
}

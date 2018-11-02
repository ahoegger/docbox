package ch.ahoegger.docbox.server.ocr;

import java.util.LinkedList;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.config.AbstractConfigProperty;
import org.eclipse.scout.rt.platform.config.CONFIG;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.ProcessingStatus;
import org.eclipse.scout.rt.platform.resource.BinaryResource;
import org.eclipse.scout.rt.platform.status.IStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.server.ocr.unix.ShellScriptOcrParser;
import ch.ahoegger.docbox.shared.validation.IStartupValidatableBean;

/**
 * <h3>{@link OcrParseService}</h3>
 *
 * @author Andreas Hoegger
 */
public class OcrParseService implements IOcrParseService {

  private IOcrParser m_parser;
  private LinkedList<ParseDescription> m_pendingDescriptions = new LinkedList<>();

  public OcrParseService() {
    m_parser = BEANS.get(CONFIG.getPropertyValue(OcrParserProperty.class));
  }

  /**
   * delegate to parser
   */
  @Override
  public OcrParseResult parsePdf(BinaryResource pdfResource, String language) {
    return m_parser.parsePdf(pdfResource, language);
  }

  @Override
  public void schedule(ParseDescription parseDescription) {
    synchronized (m_pendingDescriptions) {
      m_pendingDescriptions.add(parseDescription);
    }
  }

  public ParseDescription popNextDescription() {
    synchronized (m_pendingDescriptions) {
      return m_pendingDescriptions.poll();
    }
  }

  public static class OcrParserProperty extends AbstractConfigProperty<Class<? extends IOcrParser>, String> implements IStartupValidatableBean {
    private static final Logger LOG = LoggerFactory.getLogger(OcrParserProperty.class);

    public static final String BUILD_REPLACEMENT_VAR = "${docbox.ocr.parser}";

    @Override
    public String getKey() {
      return "docbox.ocr.parser";
    }

    @Override
    public Class<? extends IOcrParser> getDefaultValue() {
      return ShellScriptOcrParser.class;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Class<? extends IOcrParser> parse(String value) {
      try {
        return (Class<? extends IOcrParser>) Class.forName(value);
      }
      catch (ClassNotFoundException e) {
        throw new ProcessingException(new ProcessingStatus(String.format("Cold not parse '%s' property (see '%s' for details).", getKey(), OcrParserProperty.class.getName()), e, 0, IStatus.ERROR));
      }
    }

    @Override
    public String description() {
      return "The class of the OCR parser.";
    }

    @Override
    public boolean validate() {
      try {
        getValue();
      }
      catch (Exception e) {
        LOG.error("ConfigProperty: '{}' does not exist.", getKey(), e);
        return false;
      }
      return true;
    }
  }
}

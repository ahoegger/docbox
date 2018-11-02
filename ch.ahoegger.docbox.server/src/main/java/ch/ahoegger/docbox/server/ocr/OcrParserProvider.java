package ch.ahoegger.docbox.server.ocr;

import javax.annotation.PostConstruct;

import org.eclipse.scout.rt.platform.ApplicationScoped;
import org.eclipse.scout.rt.platform.BEANS;

/**
 * <h3>{@link OcrParserProvider}</h3>
 *
 * @author aho
 */
@ApplicationScoped
public class OcrParserProvider {

  private IOcrParser m_parser;

  @PostConstruct
  protected void init() {
    for (IOcrParser parser : BEANS.all(IOcrParser.class)) {
      if (parser.isActive()) {
        m_parser = parser;
        break;
      }
    }
  }

  public IOcrParser getParser() {
    return m_parser;
  }

}

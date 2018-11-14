package ch.ahoegger.docbox.server.ocr;

import org.eclipse.scout.rt.platform.ApplicationScoped;
import org.eclipse.scout.rt.platform.resource.BinaryResource;

/**
 * <h3>{@link IOcrParser}</h3>
 *
 * @author Andreas Hoegger
 */
@ApplicationScoped
public interface IOcrParser {

  OcrParseResult parsePdf(BinaryResource pdfResource, String language);

  /**
   * @return
   */
  boolean isActive();
}

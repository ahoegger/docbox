package ch.ahoegger.docbox.server.ocr;

import org.eclipse.scout.rt.platform.Bean;
import org.eclipse.scout.rt.platform.resource.BinaryResource;

/**
 * <h3>{@link IOcrParser}</h3>
 *
 * @author aho
 */
@Bean
public interface IOcrParser {

  OcrParseResult parsePdf(BinaryResource pdfResource, String language);
}

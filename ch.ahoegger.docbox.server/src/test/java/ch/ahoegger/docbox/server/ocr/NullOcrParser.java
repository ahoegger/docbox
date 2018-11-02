package ch.ahoegger.docbox.server.ocr;

import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.resource.BinaryResource;

/**
 * <h3>{@link NullOcrParser}</h3>
 *
 * @author aho
 */
@Order(5000)
public class NullOcrParser implements IOcrParser {

  @Override
  public OcrParseResult parsePdf(BinaryResource pdfResource, String language) {
    return new OcrParseResult().withText("parsed").withOcrParsed(true);
  }

  @Override
  public boolean isActive() {
    return true;
  }
}

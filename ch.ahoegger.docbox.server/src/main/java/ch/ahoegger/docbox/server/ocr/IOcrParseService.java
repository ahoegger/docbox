package ch.ahoegger.docbox.server.ocr;

import org.eclipse.scout.rt.platform.ApplicationScoped;
import org.eclipse.scout.rt.platform.job.IFuture;

/**
 * <h3>{@link IOcrParseService}</h3>
 *
 * @author aho
 */
@ApplicationScoped
public interface IOcrParseService {

  IFuture<Void> schedule(ParseDescription parseDescription);

  IFuture<Void> getCurrentParsingFeature();

}

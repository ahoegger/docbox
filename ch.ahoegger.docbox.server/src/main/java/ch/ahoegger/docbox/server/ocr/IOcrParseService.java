package ch.ahoegger.docbox.server.ocr;

import org.eclipse.scout.rt.platform.ApplicationScoped;
import org.eclipse.scout.rt.platform.job.IFuture;
import org.eclipse.scout.rt.shared.servicetunnel.RemoteServiceAccessDenied;

/**
 * <h3>{@link IOcrParseService}</h3>
 *
 * @author Andreas Hoegger
 */
@ApplicationScoped
public interface IOcrParseService {

  @RemoteServiceAccessDenied
  IFuture<Void> schedule(ParseDescription parseDescription);

  @RemoteServiceAccessDenied
  IFuture<Void> getCurrentParsingFeature();

}

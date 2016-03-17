package ch.ahoegger.docbox.ui.html;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.scout.rt.platform.config.CONFIG;
import org.eclipse.scout.rt.ui.html.res.loader.IResourceLoader;
import org.eclipse.scout.rt.ui.html.res.loader.IResourceLoaderFactory;

import ch.ahoegger.docbox.client.document.DocumentLinkProperties.DocumentLinkURI;

/**
 * <h3>{@link PdfResourceLoaderFactory}</h3>
 *
 * @author Andreas Hoegger
 */
public class PdfResourceLoaderFactory implements IResourceLoaderFactory {

  @Override
  public IResourceLoader createResourceLoader(HttpServletRequest req, String resourcePath) {
    if (resourcePath.matches("^/" + CONFIG.getPropertyValue(DocumentLinkURI.class))) {
      return new PdfResourceLoader(req);
    }
    return null;
  }

}

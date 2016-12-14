package ch.ahoegger.docbox.ui.html;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.scout.rt.platform.Replace;
import org.eclipse.scout.rt.platform.config.CONFIG;
import org.eclipse.scout.rt.ui.html.res.loader.IResourceLoader;
import org.eclipse.scout.rt.ui.html.res.loader.ResourceLoaders;

import ch.ahoegger.docbox.client.document.DocumentLinkProperties.DocumentLinkURI;

/**
 * <h3>{@link DocboxResourceLoaders}</h3>
 *
 * @author aho
 */
@Replace
public class DocboxResourceLoaders extends ResourceLoaders {

  @Override
  public IResourceLoader create(HttpServletRequest req, String resourcePath) {
    if (resourcePath.matches("^/" + CONFIG.getPropertyValue(DocumentLinkURI.class))) {
      return new PdfResourceLoader(req);
    }
    return super.create(req, resourcePath);
  }
}

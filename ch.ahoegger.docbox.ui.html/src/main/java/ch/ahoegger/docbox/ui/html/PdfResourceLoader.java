package ch.ahoegger.docbox.ui.html;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.config.CONFIG;
import org.eclipse.scout.rt.platform.resource.BinaryResource;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.ui.html.cache.HttpCacheKey;
import org.eclipse.scout.rt.ui.html.cache.HttpCacheObject;
import org.eclipse.scout.rt.ui.html.cache.IHttpCacheControl;
import org.eclipse.scout.rt.ui.html.res.loader.AbstractResourceLoader;

import ch.ahoegger.docbox.client.document.DocumentLinkProperties.DocumentLinkDocumentIdParamName;
import ch.ahoegger.docbox.shared.document.DocumentFormData;
import ch.ahoegger.docbox.shared.document.IDocumentService;
import ch.ahoegger.docbox.shared.document.store.IFileService;

/**
 * <h3>{@link PdfResourceLoader}</h3>
 *
 * @author aho
 */
public class PdfResourceLoader extends AbstractResourceLoader {

  /**
   * @param req
   */
  public PdfResourceLoader(HttpServletRequest req) {
    super(req);
  }

  @Override
  public HttpCacheObject loadResource(HttpCacheKey cacheKey) throws IOException {
    String documentIdParameter = getRequest().getParameter(CONFIG.getPropertyValue(DocumentLinkDocumentIdParamName.class));
    if (StringUtility.hasText(documentIdParameter)) {
      long documentId = Long.parseLong(documentIdParameter);
      DocumentFormData formData = new DocumentFormData();
      formData.setDocumentId(documentId);
      formData = BEANS.get(IDocumentService.class).load(formData);

      //
      BinaryResource resource = BEANS.get(IFileService.class).get(formData.getDocumentPath());
      return new HttpCacheObject(cacheKey, true, IHttpCacheControl.MAX_AGE_4_HOURS, resource);
    }
    return null;
  }

}

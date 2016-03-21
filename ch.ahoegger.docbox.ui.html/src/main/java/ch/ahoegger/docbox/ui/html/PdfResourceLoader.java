package ch.ahoegger.docbox.ui.html;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.config.CONFIG;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.resource.BinaryResource;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;
import org.eclipse.scout.rt.shared.services.common.security.IAccessControlService;
import org.eclipse.scout.rt.ui.html.cache.HttpCacheKey;
import org.eclipse.scout.rt.ui.html.cache.HttpCacheObject;
import org.eclipse.scout.rt.ui.html.res.loader.AbstractResourceLoader;

import ch.ahoegger.docbox.client.document.DocumentLinkProperties.DocumentLinkDocumentIdParamName;
import ch.ahoegger.docbox.shared.document.store.IDocumentStoreService;
import ch.ahoegger.docbox.shared.security.permission.EntityReadPermission;

/**
 * <h3>{@link PdfResourceLoader}</h3>
 *
 * @author Andreas Hoegger
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

      IAccessControlService accessControlService = BEANS.get(IAccessControlService.class);
      accessControlService.getUserIdOfCurrentSubject();

      long documentId = Long.parseLong(documentIdParameter);
      if (!ACCESS.check(new EntityReadPermission(documentId))) {
//      if (!ACCESS.check(new EntityReadPermission(formData.getDocumentId()))) {
        throw new VetoException("Access denied");
      }
      BinaryResource resource = BEANS.get(IDocumentStoreService.class).getDocument(documentId);
      return new HttpCacheObject(cacheKey, false, 0, resource);
    }
    return null;
  }

}

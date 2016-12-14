package ch.ahoegger.docbox.server.app.dev;

import java.io.File;

import org.eclipse.scout.rt.platform.Replace;
import org.eclipse.scout.rt.platform.config.CONFIG;
import org.eclipse.scout.rt.platform.util.IOUtility;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.server.document.store.DocumentStoreService;

/**
 * <h3>{@link DevDocumentStoreService}</h3>
 *
 * @author Andreas Hoegger
 */
@Replace
public class DevDocumentStoreService extends DocumentStoreService {
  @SuppressWarnings("unused")
  private static final Logger LOG = LoggerFactory.getLogger(DevDocumentStoreService.class);

  @Override
  protected String getConfiguredDocumentStoreLocation() {
    String docStoreLocation = CONFIG.getPropertyValue(DocumentStoreLocationProperty.class);
    if (StringUtility.isNullOrEmpty(docStoreLocation)) {
      File workingDir = IOUtility.createTempDirectory("docbox");
      workingDir.deleteOnExit();
      return workingDir.getAbsolutePath();
    }
    return super.getConfiguredDocumentStoreLocation();
  }

}

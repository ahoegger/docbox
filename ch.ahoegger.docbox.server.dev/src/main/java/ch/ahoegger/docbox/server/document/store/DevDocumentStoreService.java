package ch.ahoegger.docbox.server.document.store;

import java.io.File;

import org.eclipse.scout.rt.platform.Replace;
import org.eclipse.scout.rt.platform.util.IOUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <h3>{@link DevDocumentStoreService}</h3>
 *
 * @author aho
 */
@Replace
public class DevDocumentStoreService extends DocumentStoreService {
  private static final Logger LOG = LoggerFactory.getLogger(DevDocumentStoreService.class);

  @Override
  protected String getConfiguredDocumentStoreLocation() {
    File workingDir = IOUtility.createTempDirectory("docbox");
    workingDir.deleteOnExit();
    return workingDir.getAbsolutePath();
  }

}

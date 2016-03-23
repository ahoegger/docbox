package ch.ahoegger.docbox.server.test.util;

import java.io.File;

import org.eclipse.scout.rt.platform.Replace;
import org.eclipse.scout.rt.platform.util.IOUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.server.document.store.DevDocumentStoreService;

/**
 * <h3>{@link TestDocumentStoreService}</h3>
 *
 * @author Andreas Hoegger
 */
@Replace
public class TestDocumentStoreService extends DevDocumentStoreService {
  private static final Logger LOG = LoggerFactory.getLogger(TestDocumentStoreService.class);

  @Override
  protected String getConfiguredDocumentStoreLocation() {
    File workingDir = IOUtility.createTempDirectory("docbox");
    workingDir.deleteOnExit();
    return workingDir.getAbsolutePath();
  }

}

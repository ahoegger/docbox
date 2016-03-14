package ch.ahoegger.docbox.server.document.store;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.annotation.PostConstruct;

import org.eclipse.scout.rt.platform.Replace;
import org.eclipse.scout.rt.platform.resource.BinaryResource;
import org.eclipse.scout.rt.platform.util.IOUtility;
import org.eclipse.scout.rt.shared.services.common.file.RemoteFile;
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

  @PostConstruct
  void init() {
    File file = new File("abc.txt");
    System.out.println(file.getAbsolutePath());
  }

  @Override
  public File store(RemoteFile remoteFile) {

    return null;
  }

  @Override
  public BinaryResource getDocument(String path) {
    BinaryResource result = null;
    URL resource = getClass().getResource(path);
    try {
      result = new BinaryResource(path, IOUtility.readFromUrl(resource));
    }
    catch (IOException e) {
      LOG.error("Could not resolve file '" + path + "'.", e);
    }
    return result;
  }

}

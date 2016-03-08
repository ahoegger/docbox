package ch.ahoegger.docbox.server.document.store;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.annotation.PostConstruct;

import org.eclipse.scout.rt.platform.CreateImmediately;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.resource.BinaryResource;
import org.eclipse.scout.rt.platform.util.IOUtility;
import org.eclipse.scout.rt.shared.services.common.file.RemoteFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.shared.document.store.IFileService;

/**
 * <h3>{@link DevFileService}</h3>
 *
 * @author aho
 */
@Order(-10)
@CreateImmediately
public class DevFileService implements IFileService {
  private static final Logger LOG = LoggerFactory.getLogger(DevFileService.class);

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
  public BinaryResource get(String path) {
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

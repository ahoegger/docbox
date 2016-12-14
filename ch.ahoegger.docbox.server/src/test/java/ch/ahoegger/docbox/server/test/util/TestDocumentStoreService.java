package ch.ahoegger.docbox.server.test.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.eclipse.scout.rt.platform.Replace;
import org.eclipse.scout.rt.platform.util.IOUtility;
import org.eclipse.scout.rt.platform.util.ObjectUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.server.document.store.DocumentStoreService;

/**
 * <h3>{@link TestDocumentStoreService}</h3>
 *
 * @author Andreas Hoegger
 */
@Replace
public class TestDocumentStoreService extends DocumentStoreService {
  @SuppressWarnings("unused")
  private static final Logger LOG = LoggerFactory.getLogger(TestDocumentStoreService.class);

  @Override
  protected String getConfiguredDocumentStoreLocation() {
    File workingDir = IOUtility.createTempDirectory("docbox");
    workingDir.deleteOnExit();
    return workingDir.getAbsolutePath();
  }

  public void clearStore() throws IOException {
    synchronized (IO_LOCK) {
      Path documentStorePath = getDocumentStoreDirectory().toPath();
      Files.walkFileTree(documentStorePath, new SimpleFileVisitor<Path>() {
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
          if (ObjectUtility.notEquals(file, documentStorePath)) {
            Files.delete(file);
          }
          return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException e)
            throws IOException {
          if (e == null) {
            if (ObjectUtility.notEquals(dir, documentStorePath)) {
              Files.delete(dir);
            }
            return FileVisitResult.CONTINUE;
          }
          else {
            // directory iteration failed
            throw e;
          }
        }
      });
    }
  }
}

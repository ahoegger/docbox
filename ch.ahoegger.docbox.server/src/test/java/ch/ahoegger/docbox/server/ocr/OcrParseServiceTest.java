package ch.ahoegger.docbox.server.ocr;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.resource.BinaryResource;
import org.eclipse.scout.rt.platform.util.IOUtility;
import org.eclipse.scout.rt.testing.platform.runner.PlatformTestRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.shared.ocr.OcrLanguageCodeType;

/**
 * <h3>{@link OcrParseServiceTest}</h3>
 *
 * @author Andreas Hoegger
 */
@RunWith(PlatformTestRunner.class)
public class OcrParseServiceTest {
  private static final Logger LOG = LoggerFactory.getLogger(OcrParseServiceTest.class);

  @Test
  public void testPdfParse() throws Exception {
    BinaryResource resouce = getResourceFromClassLoader(Paths.get("devDocuments", "withoutTextInfo.pdf"));
    OcrParseResult parseResult = BEANS.get(OcrParseService2.class).parsePdf(resouce, OcrLanguageCodeType.GermanCode.ID);
    Assert.assertTrue(parseResult.isOcrParsed());
    String text = parseResult.getText().toLowerCase();
    LOG.info("parsed text: {}", text);

    Assert.assertFalse("Working direcotry is propperly removed.", Files.exists(parseResult.getWorkingDirectory()));
    Assert.assertTrue(text.contains("einfacher demo"));
    Assert.assertTrue(text.contains("industrie"));
    Assert.assertTrue(text.contains("demo-text"));
    Assert.assertTrue(text.contains("schriftsteller"));
    Assert.assertTrue(text.contains("wörter"));
    Assert.assertTrue(text.contains("musterbuch"));
  }

  private BinaryResource getResourceFromClassLoader(Path path) throws IOException {
    InputStream in = null;
    try {
      URL resource = Test.class.getClassLoader().getResource(path.toString());
      in = resource.openStream();
      return new BinaryResource(path.getFileName().toString(), IOUtility.readBytes(in));
    }
    finally {
      try {
        if (in != null) {
          in.close();
        }
      }
      catch (IOException e) {
        // void
      }
    }
  }

  @Test
  public void testPdfReadable() throws Exception {
    BinaryResource resouce = getResourceFromClassLoader(Paths.get("devDocuments", "withTextInfo.pdf"));
    OcrParseResult parseResult = BEANS.get(OcrParseService2.class).parsePdf(resouce, OcrLanguageCodeType.GermanCode.ID);
    Assert.assertFalse(parseResult.isOcrParsed());
    String text = parseResult.getText();
    Assert.assertTrue(text.contains("Maecenas sodales molestie volutpat. Curabitur diam libero, tincidunt vel enim non, varius lacinia"));
  }

  @Test
  public void testMultiPageOcr() throws Exception {
    BinaryResource resouce = getResourceFromClassLoader(Paths.get("devDocuments", "multipPateWithoutTextInfo.pdf"));
    OcrParseResult parseResult = BEANS.get(OcrParseService2.class).parsePdf(resouce, OcrLanguageCodeType.EnglishCode.ID);
    Assert.assertTrue(parseResult.isOcrParsed());
    String text = parseResult.getText();
    Assert.assertTrue(text.contains(" Sections 2(a) and 2(b) above"));
    Assert.assertTrue(text.contains("ACCEPTANCE OF THIS AGREEMENT"));
    Assert.assertFalse("Working direcotry is propperly removed.", Files.exists(parseResult.getWorkingDirectory()));
  }
}

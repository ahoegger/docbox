package ch.ahoegger.docbox.server.ocr;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.config.CONFIG;
import org.eclipse.scout.rt.testing.platform.runner.PlatformTestRunner;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.server.ocr.OcrParseService.TessdataDirectoryProperty;
import ch.ahoegger.docbox.server.test.util.OS;

/**
 * <h3>{@link OcrParseServiceTest}</h3>
 *
 * @author aho
 */
@RunWith(PlatformTestRunner.class)
public class OcrParseServiceTest {
  private static final Logger LOG = LoggerFactory.getLogger(OcrParseServiceTest.class);

  private static Path tessdataDirectory;

  @BeforeClass
  public static void beforeClass() {
    if (OS.isWindows()) {
      tessdataDirectory = Paths.get("C:/tesseract/tessdata");
    }
    else {
      tessdataDirectory = CONFIG.getPropertyValue(TessdataDirectoryProperty.class);
    }
  }

  @Test
  public void testPdfParse() throws Exception {
    URL resource = Test.class.getClassLoader().getResource("devDocuments/withoutTextInfo.pdf");
    InputStream is = null;
    try {
      is = resource.openStream();
      OcrParseResult parseResult = BEANS.get(OcrParseService.class).parsePdf(is, tessdataDirectory);
      Assert.assertTrue(parseResult.isOcrParsed());
      String text = parseResult.getText();
      LOG.info("parsed text: {}", text);
      System.out.println(text);

      Assert.assertFalse("Working direcotry is propperly removed.", Files.exists(parseResult.getWorkingDirectory()));
      Assert.assertTrue(text.contains("einfacher Demo-Text"));
      Assert.assertTrue(text.contains("Industrie"));
      Assert.assertTrue(text.contains("Demo-Text"));
      Assert.assertTrue(text.contains("1500"));
      Assert.assertTrue(text.contains("Schriftsteller"));
      Assert.assertTrue(text.contains("Wörter"));
      Assert.assertTrue(text.contains("Musterbuch"));
    }
    finally {
      if (is != null) {
        is.close();
      }
    }
  }

  @Test
  public void testPdfReadable() throws Exception {
    URL resource = Test.class.getClassLoader().getResource("devDocuments/withTextInfo.pdf");
    InputStream is = null;
    try {
      is = resource.openStream();
      OcrParseResult parseResult = BEANS.get(OcrParseService.class).parsePdf(is, tessdataDirectory);
      Assert.assertFalse(parseResult.isOcrParsed());
      String text = parseResult.getText();
      Assert.assertFalse("Working direcotry is propperly removed.", Files.exists(parseResult.getWorkingDirectory()));
      Assert.assertTrue(text.contains("Maecenas sodales molestie volutpat. Curabitur diam libero, tincidunt vel enim non, varius lacinia"));
    }
    finally {
      if (is != null) {
        is.close();
      }
    }
  }

  @Test
  public void testMultiPageOcr() throws Exception {
    URL resource = Test.class.getClassLoader().getResource("devDocuments/multipPateWithoutTextInfo.pdf");
    InputStream is = null;
    try {
      is = resource.openStream();
      OcrParseResult parseResult = BEANS.get(OcrParseService.class).parsePdf(is, tessdataDirectory);
      Assert.assertTrue(parseResult.isOcrParsed());
      String text = parseResult.getText();
      Assert.assertTrue(text.contains(" Sections 2(a) and 2(b) above"));
      Assert.assertTrue(text.contains("ACCEPTANCE OF THIS AGREEMENT"));
      Assert.assertFalse("Working direcotry is propperly removed.", Files.exists(parseResult.getWorkingDirectory()));
    }
    finally {
      if (is != null) {
        is.close();
      }
    }
  }
}

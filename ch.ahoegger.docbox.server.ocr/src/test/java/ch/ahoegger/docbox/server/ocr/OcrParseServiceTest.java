package ch.ahoegger.docbox.server.ocr;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.testing.platform.runner.PlatformTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * <h3>{@link OcrParseServiceTest}</h3>
 *
 * @author aho
 */
@RunWith(PlatformTestRunner.class)
public class OcrParseServiceTest {

  @Test
  public void testParseTif() throws Exception {
    URL resource = Test.class.getClassLoader().getResource("devDocuments/sampleText.pdf");
    InputStream is = null;
    try {
      is = resource.openStream();
      List<Path> convertToTif = BEANS.get(OcrParseService.class).pdfToTif(is);
      convertToTif.forEach(System.out::println);
      String text = BEANS.get(OcrParseService.class).computeText(convertToTif);
      System.out.println(text);
    }
    finally {
      if (is != null) {
        is.close();
      }
    }

  }
}

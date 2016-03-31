package ch.ahoegger.docbox.server.ocr;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.testing.platform.runner.PlatformTestRunner;
import org.junit.Assert;
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
    URL resource = Test.class.getClassLoader().getResource("devDocuments/simple.pdf");
    InputStream is = null;
    try {
      is = resource.openStream();
      List<Path> convertToTif = BEANS.get(OcrParseService.class).pdfToTif(is);
      convertToTif.forEach(System.out::println);
      String text = BEANS.get(OcrParseService.class).computeText(convertToTif);
      System.out.println(text);
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

  public static void main(String[] args) {
    for (Entry<Object, Object> e : System.getProperties().entrySet()) {
      System.out.println(e.getKey() + " - " + e.getValue());
    }
    System.out.println(System.getProperty("os.name"));
  }
}

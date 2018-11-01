package ch.ahoegger.docbox.server.ocr;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.text.PDFTextStripper;
import org.eclipse.scout.rt.platform.ApplicationScoped;
import org.eclipse.scout.rt.platform.resource.BinaryResource;
import org.eclipse.scout.rt.platform.util.IOUtility;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.server.ocr.OcrParseResult.ParseError;

/**
 * <h3>{@link OcrParseService2}</h3>
 *
 * @author aho
 */
@ApplicationScoped
public class OcrParseService2 {
  private static final Logger LOG = LoggerFactory.getLogger(OcrParseService2.class);

  public OcrParseResult parsePdf(BinaryResource pdfResource, String language) {
    return parsePdf(new ByteArrayInputStream(pdfResource.getContent()), pdfResource.getFilename(), language);
  }

  public OcrParseResult parsePdf(InputStream input, String filename, String language) {
    OcrParseResult result = new OcrParseResult();
    // try to read meta text of pdf
    try {
      String content = readPdfMetaText(input);
      if (StringUtility.hasText(content)) {
        result.withText(content).withOcrParsed(false);
        return result;
      }
    }
    catch (Exception e) {
      LOG.error("Could not read meta text of file '{}'.", e);
    }
    finally {
      if (input != null) {
        try {
          input.close();
        }
        catch (IOException e) {
          // void
        }
      }
    }

    // try to parse using tesseract

    Path tempDir = null;
    try {
      tempDir = Files.createTempDirectory("ocrWorkingDir");
      result.withWorkingDirectory(tempDir);
      Path inputPath = tempDir.resolve("input.pdf");
      inputPath = Files.createFile(inputPath);
      Files.copy(
          input,
          inputPath,
          StandardCopyOption.REPLACE_EXISTING);

      if (startAndWait(tempDir, language)) {
        LOG.debug("Successfully parsed file: {}", filename);
        if (LOG.isTraceEnabled()) {
          String content = new String(Files.readAllBytes(tempDir.resolve("output.txt")), Charset.forName("UTF-8"));
          LOG.trace("Parsed text: {}", content);
          result.withOcrParsed(true).withText(content);
        }

      }
      else {
        result.withParseError(ParseError.CouldNotParseText);
      }

    }
    catch (Exception e) {
      LOG.error("Could not parse file '{}'.", e);
    }
    finally {
      if (tempDir != null) {
        IOUtility.deleteDirectory(tempDir.toFile());
      }
    }
    return result;

  }

  private String readPdfMetaText(InputStream in) throws InvalidPasswordException, IOException {
    PDDocument pddoc = PDDocument.load(in);
    // try to get text straight
    String content = getTextOfPdf(pddoc);
    return content;
  }

  protected synchronized String getTextOfPdf(PDDocument doc) throws IOException {
    PDFTextStripper textStripper = new PDFTextStripper();
    String content = textStripper.getText(doc);
    return content;
  }

  private boolean startAndWait(Path workingDir, String language) throws IOException, InterruptedException {
    LOG.debug("Start tesseract-pdfToText with working directory: {}", workingDir.toString());
    ProcessBuilder processBuilder = new ProcessBuilder("/opt/pdfToText/tesseract-pdfToText.sh", workingDir.toAbsolutePath().toString(), language);
    Process p = processBuilder.start();
    return p.waitFor() == 0;
  }
}

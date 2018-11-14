package ch.ahoegger.docbox.server.ocr.unix;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.config.AbstractConfigProperty;
import org.eclipse.scout.rt.platform.config.CONFIG;
import org.eclipse.scout.rt.platform.resource.BinaryResource;
import org.eclipse.scout.rt.platform.util.IOUtility;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.server.ocr.IOcrParser;
import ch.ahoegger.docbox.server.ocr.OcrParseResult;
import ch.ahoegger.docbox.server.ocr.OcrParseResult.ParseError;
import ch.ahoegger.docbox.server.ocr.OcrParserProvider;
import ch.ahoegger.docbox.server.util.OS;
import ch.ahoegger.docbox.shared.validation.IStartupValidatableBean;

/**
 * <h3>{@link ShellScriptOcrParser}</h3>
 *
 * @author Andreas Hoegger
 */
@Order(100)
public class ShellScriptOcrParser implements IOcrParser {
  private static final Logger LOG = LoggerFactory.getLogger(ShellScriptOcrParser.class);

  @Override
  public boolean isActive() {
    return OS.isUnix();
  }

  @Override
  public OcrParseResult parsePdf(BinaryResource pdfResource, String language) {
    LOG.debug("Start parsing file: {}", pdfResource.getFilename());
    OcrParseResult result = readPdfMetaText(pdfResource);
    if (result == null) {
      // try to parse
      result = parsePdfTesseract(pdfResource, language);
    }
    return result;
  }

  private OcrParseResult readPdfMetaText(BinaryResource pdfResource) {
    OcrParseResult result = new OcrParseResult();
    InputStream in = null;
    PDDocument pddoc = null;
    // try to read meta text of pdf
    try {
      in = new ByteArrayInputStream(pdfResource.getContent());
      pddoc = PDDocument.load(in);
      // try to get text straight
      String content = getTextOfPdf(pddoc);
      if (StringUtility.hasText(content)) {
        result.withText(content).withOcrParsed(false);
        LOG.debug("Found text in pdf meta data: {}", pdfResource.getFilename());
        return result;
      }
    }
    catch (Exception e) {
      LOG.error(String.format("Could not read meta text of file '%s'.", pdfResource.getFilename()), e);
    }
    finally {
      if (pddoc != null) {
        try {
          pddoc.close();
        }
        catch (IOException e) {
          // void
        }
      }
      if (in != null) {
        try {
          in.close();
        }
        catch (IOException e) {
          // void
        }
      }
    }
    return null;
  }

  protected synchronized String getTextOfPdf(PDDocument doc) throws IOException {
    PDFTextStripper textStripper = new PDFTextStripper();
    String content = textStripper.getText(doc);
    return content;
  }

  private OcrParseResult parsePdfTesseract(BinaryResource pdfResource, String language) {
    OcrParseResult result = new OcrParseResult();
    Path tempDir = null;
    InputStream in = null;
    try {
      tempDir = Files.createTempDirectory("ocrWorkingDir").toAbsolutePath();
      result.withWorkingDirectory(tempDir);
      Path inputPath = tempDir.resolve("input.pdf");
      inputPath = Files.createFile(inputPath);
      in = new ByteArrayInputStream(pdfResource.getContent());
      Files.copy(
          in,
          inputPath,
          StandardCopyOption.REPLACE_EXISTING);

      if (startAndWait(tempDir, language)) {
        LOG.debug("Successfully parsed file: {}", pdfResource.getFilename());
        String content = new String(Files.readAllBytes(tempDir.resolve("output.txt")), Charset.forName("UTF-8"));
        if (LOG.isTraceEnabled()) {
          LOG.trace("Parsed text: {}", content);
        }
        result.withOcrParsed(true).withText(content);
      }
      else {
        result.withParseError(ParseError.CouldNotParseText);
      }

    }
    catch (Exception e) {
      result.withParseError(ParseError.CouldNotParseText);
      LOG.error(String.format("Could not parse file '%s'.", pdfResource.getFilename()), e);
    }
    finally {
      if (tempDir != null) {
        IOUtility.deleteDirectory(tempDir.toFile());
      }
      if (in != null) {
        try {
          in.close();
        }
        catch (IOException e) {
          // void
        }
      }
    }
    return result;
  }

  private boolean startAndWait(Path workingDir, String language) throws IOException, InterruptedException {
    LOG.trace("Start tesseract-pdfToText with working directory: {}", workingDir.toString());
    ProcessBuilder processBuilder = new ProcessBuilder(CONFIG.getPropertyValue(TesseractPdfToTextProperty.class).toString(), workingDir.toAbsolutePath().toString(), language);
    Process p = processBuilder.start();
    LOG.debug("tesseract errors: ", IOUtility.readStringUTF8(p.getErrorStream()));
    LOG.debug("tesseract stdOut: ", IOUtility.readStringUTF8(p.getInputStream()));
    return p.waitFor() == 0;
  }

  public static class TesseractPdfToTextProperty extends AbstractConfigProperty<Path, String> implements IStartupValidatableBean {

    public static final String BUILD_REPLACEMENT_VAR = "${docbox.tesserarct.tesseract-pdfToText}";

    @Override
    public String getKey() {
      return "docbox.tesserarct.tesseract-pdfToText";
    }

    @Override
    public Path getDefaultValue() {
      if (OS.isWindows()) {
        return Paths.get("NOT SUPPORTED!");
      }
      return Paths.get("/opt/pdfToText/tesseract-pdfToText.sh");
    }

    @Override
    protected Path parse(String value) {
      if (BUILD_REPLACEMENT_VAR.equals(value)) {
        return getDefaultValue();
      }
      else {
        return Paths.get(value);
      }
    }

    @Override
    public String description() {
      return "The path to the tesseract-pdfToText.sh script.";
    }

    @Override
    public boolean validate() {
      if (BEANS.get(OcrParserProvider.class).getParser().getClass() == ShellScriptOcrParser.class) {
//      if (CONFIG.getPropertyValue(OcrParserProperty.class) == ShellScriptOcrParser.class) {
        Path value = getValue();
        if (Files.notExists(value) || Files.isDirectory(value)) {
          LOG.error("ConfigProperty: '{}' with value '{}' does not exist.", getKey(), value.toString());
          return false;
        }
      }
      return true;
    }
  }
}

package ch.ahoegger.docbox.javacpp.tesseract;

import static org.bytedeco.javacpp.lept.pixDestroy;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.lept;
import org.bytedeco.javacpp.lept.PIX;
import org.bytedeco.javacpp.tesseract.TessBaseAPI;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.config.AbstractConfigProperty;
import org.eclipse.scout.rt.platform.config.CONFIG;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.ProcessingStatus;
import org.eclipse.scout.rt.platform.resource.BinaryResource;
import org.eclipse.scout.rt.platform.status.IStatus;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.platform.util.IOUtility;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.server.ocr.IOcrParser;
import ch.ahoegger.docbox.server.ocr.OcrParseResult;
import ch.ahoegger.docbox.server.ocr.OcrParseResult.ParseError;
import ch.ahoegger.docbox.server.ocr.OcrParserProvider;
import ch.ahoegger.docbox.server.util.OS;
import ch.ahoegger.docbox.shared.ocr.OcrLanguageCodeType;
import ch.ahoegger.docbox.shared.validation.IStartupValidatableBean;

/**
 * <h3>{@link CppTesseractOcrParser}</h3>
 *
 * @author aho
 */
@Order(1000)
public class CppTesseractOcrParser implements IOcrParser {

  private static final Logger LOG = LoggerFactory.getLogger(CppTesseractOcrParser.class);
  private static final Object LOCK = new Object();

  @Override
  public boolean isActive() {
    return true;
  }

  @Override
  public OcrParseResult parsePdf(BinaryResource pdfResource, String language) {
    OcrParseResult result = readPdfMetaText(pdfResource);
    if (result == null) {
      // try to parse
      result = parsePdfTesseract(pdfResource, language);
    }
    return result;
  }

  protected OcrParseResult readPdfMetaText(BinaryResource pdfResource) {
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

  protected OcrParseResult parsePdfTesseract(BinaryResource pdfResource, String language) {
    synchronized (LOCK) {

      Path workingDirectory = null;
      PDDocument pddoc = null;
      InputStream inputStream = null;
      try {
        OcrParseResult result = new OcrParseResult();
        workingDirectory = Files.createTempDirectory("ocrWorkingDir").toAbsolutePath();
        result.withWorkingDirectory(workingDirectory);
        List<Path> tifFiles = CollectionUtility.emptyArrayList();
        inputStream = new ByteArrayInputStream(pdfResource.getContent());
        pddoc = PDDocument.load(inputStream);
        tifFiles = pdfToTif(pddoc, workingDirectory);
        pddoc.close();
        pddoc = null;
        String text = computeText(tifFiles, language);
        if (StringUtility.hasText(text)) {
          result.withText(text).withOcrParsed(true);
        }
        else {
          result.withParseError(ParseError.CouldNotParseText);
        }
        return result;
      }
      catch (IOException e) {
        throw new ProcessingException(new ProcessingStatus("Could not read text form pdf.", e, 0, IStatus.ERROR));
      }
      finally {
        if (inputStream != null) {
          try {
            inputStream.close();
          }
          catch (IOException e) {
            // void
          }
        }
        if (pddoc != null) {
          try {
            pddoc.close();
          }
          catch (IOException e) {
            LOG.warn("Could not close pdd document.", e);
          }
        }
        // delete working dir
        if (workingDirectory != null) {
          if (workingDirectory != null) {
            IOUtility.deleteDirectory(workingDirectory.toFile());
          }
        }
      }
    }
  }

  protected List<Path> pdfToTif(PDDocument pddoc, Path workingDirectory) throws IOException {
    List<Path> tiffPaths = new ArrayList<>();
    PDFRenderer pdfRenderer = new PDFRenderer(pddoc);

    for (int i = 0; i < pddoc.getPages().getCount(); i++) {
      OutputStream os = null;
      FileChannel channel = null;
      BufferedImage img = null;
      try {
        Path tifPath = Files.createFile(workingDirectory.resolve("TIFF_" + i + ".tiff"));
        tiffPaths.add(tifPath);
        channel = FileChannel.open(tifPath, StandardOpenOption.WRITE);
        os = Channels.newOutputStream(channel);
        // suffix in filename will be used as the file format
        img = pdfRenderer.renderImageWithDPI(i, 200, ImageType.RGB);
        ImageIOUtil.writeImage(img, "tiff", os);
      }
      finally {
        if (img != null) {
          img.flush();
        }
        if (channel != null) {
          channel.force(false);
          channel.close();
        }
      }
    }
    return tiffPaths;
  }

  protected String computeText(List<Path> tifPaths, String language) throws IOException {
    if (language == null) {
      language = OcrLanguageCodeType.GermanCode.ID;
    }
    synchronized (LOCK) {
      TessBaseAPI api = null;
      PIX image = null;
      BytePointer outText = null;

      try {
        StringBuilder result = new StringBuilder();
        api = new TessBaseAPI();

        if (api.Init(CONFIG.getPropertyValue(CppTesseractDataDirectoryProperty.class).toString(), language) != 0) {
          throw new ProcessingException(new ProcessingStatus("Could not initialize tesseract.", IStatus.ERROR));
        }
        for (Path tifPath : tifPaths) {
          // Open input image with leptonica library
          // Get OCR result
          try {

            image = lept.pixRead(tifPath.toString());
            api.SetImage(image);

            outText = api.GetUTF8Text();
            if (outText != null) {
              result.append(outText.getString());
            }
          }
          finally {
            if (outText != null) {
              outText.setNull();
              outText.deallocate();
            }
            pixDestroy(image);
            TessBaseAPI.ClearPersistentCache();
            TessBaseAPI.deallocateReferences();
          }
        }
        return result.toString();
      }
      finally {
        if (api != null) {
          api.Clear();
          api.End();

          try {
            api.close();
          }
          catch (Exception e) {
            throw new ProcessingException(new ProcessingStatus("Could not close api tesseract.", e, 0, IStatus.ERROR));
          }
        }
      }
    }

  }

  public static class CppTesseractDataDirectoryProperty extends AbstractConfigProperty<Path, String> implements IStartupValidatableBean {

    public static final String BUILD_REPLACEMENT_VAR = "${docbox.tesserarct.tessdata}";

    @Override
    public String getKey() {
      return "docbox.tesserarct.tessdata";
    }

    @Override
    public Path getDefaultValue() {
      if (OS.isWindows()) {
        return Paths.get("C:/tesseract/tessdata-3.04.00/tessdata");
      }
      return Paths.get("/var/lib/tesseract/tessdata-3.04.00/tessdata");
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
      return "The system path to the tessdata files.";
    }

    @Override
    public boolean validate() {
      if (BEANS.get(OcrParserProvider.class).getParser().getClass() == CppTesseractOcrParser.class) {
//      if (CONFIG.getPropertyValue(OcrParserProperty.class) == CppTesseractOcrParser.class) {
        Path value = CONFIG.getPropertyValue(CppTesseractDataDirectoryProperty.class);
        if (Files.notExists(value, LinkOption.NOFOLLOW_LINKS)) {
          LOG.error("ConfigProperty: '{}' with value '{}' does not exist.", getKey(), value.toString());
          return false;
        }
      }
      return true;
    }
  }

}

package ch.ahoegger.docbox.server.ocr;

import static org.bytedeco.javacpp.lept.pixDestroy;
import static org.bytedeco.javacpp.lept.pixRead;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.lept.PIX;
import org.bytedeco.javacpp.tesseract.TessBaseAPI;
import org.eclipse.scout.rt.platform.ApplicationScoped;
import org.eclipse.scout.rt.platform.config.AbstractConfigProperty;
import org.eclipse.scout.rt.platform.config.AbstractStringConfigProperty;
import org.eclipse.scout.rt.platform.config.CONFIG;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.ProcessingStatus;
import org.eclipse.scout.rt.platform.resource.BinaryResource;
import org.eclipse.scout.rt.platform.status.IStatus;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <h3>{@link OcrParseService}</h3>
 *
 * @author aho
 */
@ApplicationScoped
public class OcrParseService {
  private static final Logger LOG = LoggerFactory.getLogger(OcrParseService.class);

  public OcrParseResult parsePdf(BinaryResource pdfResource) {
    // try to get content
    ByteArrayInputStream in = null;
    try {
      in = new ByteArrayInputStream(pdfResource.getContent());
      return parsePdf(in);
    }
    finally {
      if (in != null) {
        try {
          in.close();
        }
        catch (IOException e) {
          LOG.warn("Could not close input stram of binary resource '" + pdfResource + "'.", e);
        }
      }
    }
  }

  public OcrParseResult parsePdf(InputStream pdfInputStream) {
    return parsePdf(pdfInputStream, CONFIG.getPropertyValue(TessdataDirectoryProperty.class));
  }

  public synchronized OcrParseResult parsePdf(InputStream pdfInputStream, Path tessdataDirectory) {
    Path workingDirectory = null;
    PDDocument pddoc = null;
    try {
      OcrParseResult result = new OcrParseResult();
      workingDirectory = Files.createTempDirectory("ocrWorkingDir").toAbsolutePath();
      result.setWorkingDirectory(workingDirectory);
      List<Path> tifFiles = CollectionUtility.emptyArrayList();
      pddoc = PDDocument.load(pdfInputStream);
      // try to get text straight
      String content = getTextOfPdf(pddoc);
      if (StringUtility.hasText(content)) {
        result.setText(content);
        result.setOcrParsed(false);
      }
      else {
        tifFiles = pdfToTif(pddoc, workingDirectory);
        result.setText(computeText(tifFiles, tessdataDirectory));
        result.setOcrParsed(true);
      }
      return result;
    }
    catch (IOException e) {
      throw new ProcessingException(new ProcessingStatus("Could not read text form pdf.", e, 0, IStatus.ERROR));
    }
    finally {
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
        try {
          Files.walkFileTree(workingDirectory, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path filePath, BasicFileAttributes attrs) throws IOException {
              Files.delete(filePath);
              return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
              Files.delete(dir);
              return FileVisitResult.CONTINUE;
            }
          });
        }
        catch (IOException e) {
          LOG.warn("Could not delete working directory '" + workingDirectory + "'.", e);
          // void
        }
      }
    }
  }

  protected synchronized String getTextOfPdf(PDDocument doc) throws IOException {
    PDFTextStripper textStripper = new PDFTextStripper();
    String content = textStripper.getText(doc);
    return content;
  }

  protected synchronized List<Path> pdfToTif(PDDocument pddoc, Path workingDirectory) throws IOException {
    List<Path> tiffPaths = new ArrayList<>();
    OutputStream os = null;
    try {

      PDFRenderer pdfRenderer = new PDFRenderer(pddoc);
      for (int i = 0; i < pddoc.getPages().getCount(); i++) {
        Path tifPath = Files.createFile(workingDirectory.resolve("TIFF_" + i + ".tiff"));
        tiffPaths.add(tifPath);
        FileChannel channel = FileChannel.open(tifPath, StandardOpenOption.WRITE);
        os = Channels.newOutputStream(channel);
        // suffix in filename will be used as the file format
        ImageIOUtil.writeImage(pdfRenderer.renderImageWithDPI(i, 300, ImageType.RGB), "tiff", os, 300);
      }
    }
    finally {
      if (os != null) {
        os.close();
      }
    }
    return tiffPaths;
  }

  protected synchronized String computeText(List<Path> tifPaths, Path tessdataDirectory) throws IOException {
    TessBaseAPI api = null;
    PIX image = null;
    BytePointer outText = null;
    try {
      api = new TessBaseAPI();
      if (api.Init(tessdataDirectory.toString(), CONFIG.getPropertyValue(TesseractLanguageProperty.class)) != 0) {
        throw new ProcessingException(new ProcessingStatus("Could not initialize tesseract.", IStatus.ERROR));
      }

      StringBuilder result = new StringBuilder();
      for (Path tifPath : tifPaths) {
        // Open input image with leptonica library
        image = pixRead(tifPath.toString());
        api.SetImage(image);
        // Get OCR result
        outText = api.GetUTF8Text();
        result.append(outText.getString());
      }
      return result.toString();
    }
    finally {
      // Destroy used object and release memory
      if (outText != null) {
        outText.deallocate();
        try {
          outText.close();
        }
        catch (Exception e) {
          // void
          LOG.warn("Could not close tesseract outText.", e);
        }
      }
      if (api != null) {
        api.End();
        try {
          api.close();
        }
        catch (Exception e) {
          // void
          LOG.warn("Could not close tesseract api.", e);
        }
      }
      pixDestroy(image);
    }

  }

  public static class TessdataDirectoryProperty extends AbstractConfigProperty<Path> {

    public static final String BUILD_REPLACEMENT_VAR = "${docbox.tesserarct.tessdata}";

    @Override
    public String getKey() {
      return "docbox.tesserarct.tessdata";
    }

    @Override
    protected Path getDefaultValue() {
      return Paths.get("C:/tesseract/tessdata");
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
  }

  public static class TesseractLanguageProperty extends AbstractStringConfigProperty {

    @Override
    public String getKey() {
      return "docbox.tesserarct.language";
    }

    @Override
    protected String getDefaultValue() {
      return "deu";
    }

  }
}

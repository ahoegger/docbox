package ch.ahoegger.docbox.server.ocr;

import static org.bytedeco.javacpp.lept.pixDestroy;

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
import org.bytedeco.javacpp.lept;
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
import org.eclipse.scout.rt.platform.util.FileUtility;
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
    LOG.info("About to parse file {} .", pdfResource.getFilename());
    if (!"pdf".equalsIgnoreCase(FileUtility.getFileExtension(pdfResource.getFilename()))) {
      LOG.warn("File {} is not a parsable file. Parsable files are [*.pdf].", pdfResource.getFilename());
      return null;
    }
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

  public OcrParseResult parsePdf(InputStream pdfInputStream, Path tessdataDirectory) {
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
        pddoc.close();
        pddoc = null;
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
        }
      }
    }
  }

  protected String getTextOfPdf(PDDocument doc) throws IOException {
    PDFTextStripper textStripper = new PDFTextStripper();
    String content = textStripper.getText(doc);
    return content;
  }

  protected synchronized List<Path> pdfToTif(PDDocument pddoc, Path workingDirectory) throws IOException {
    List<Path> tiffPaths = new ArrayList<>();

    PDFRenderer pdfRenderer = new PDFRenderer(pddoc);
    for (int i = 0; i < pddoc.getPages().getCount(); i++) {
      OutputStream os = null;
      FileChannel channel = null;
      try {
        Path tifPath = Files.createFile(workingDirectory.resolve("TIFF_" + i + ".tiff"));
        tiffPaths.add(tifPath);
        channel = FileChannel.open(tifPath, StandardOpenOption.WRITE);
        os = Channels.newOutputStream(channel);
        // suffix in filename will be used as the file format
        ImageIOUtil.writeImage(pdfRenderer.renderImageWithDPI(i, 300, ImageType.RGB), "tiff", os, 300);
      }
      finally {
        if (channel != null) {
          channel.close();
        }
        if (os != null) {
          os.flush();
          os.close();
        }
      }
    }
    return tiffPaths;
  }

  protected String computeText(List<Path> tifPaths, Path tessdataDirectory) throws IOException {
    TessBaseAPI api = null;
    PIX image = null;
    BytePointer outText = null;

    StringBuilder result = new StringBuilder();
    for (Path tifPath : tifPaths) {
      // Open input image with leptonica library
      // Get OCR result
      try {
        api = new TessBaseAPI();
        if (api.Init(tessdataDirectory.toString(), CONFIG.getPropertyValue(TesseractLanguageProperty.class)) != 0) {
          throw new ProcessingException(new ProcessingStatus("Could not initialize tesseract.", IStatus.ERROR));
        }
        image = lept.pixRead(tifPath.toString());
        api.SetImage(image);
        outText = api.GetUTF8Text();
        if (outText != null) {
          result.append(outText.getString("UTF8"));
//          result.append(outText.getString());
        }
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
        if (outText != null) {
          outText.deallocate();
        }
        pixDestroy(image);

      }

    }
    return result.toString();

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
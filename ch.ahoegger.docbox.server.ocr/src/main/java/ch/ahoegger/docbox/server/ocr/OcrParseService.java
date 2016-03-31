package ch.ahoegger.docbox.server.ocr;

import static org.bytedeco.javacpp.lept.pixDestroy;
import static org.bytedeco.javacpp.lept.pixRead;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.Buffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
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
import org.eclipse.scout.rt.platform.status.IStatus;
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

  public String parsePdf(InputStream pdfInputStream) {
    String parsedContent;
    try {
      List<Path> tifs = pdfToTif(pdfInputStream);
      parsedContent = computeText(tifs);
    }
    catch (IOException e) {
      throw new ProcessingException(new ProcessingStatus("Could not read text form pdf.", e, 0, IStatus.ERROR));
    }
    return parsedContent;
  }

  protected List<Path> savePdfAsTiff(Path pdfFilePath) throws IOException {
    InputStream inputStream = null;
    try {
      inputStream = Channels.newInputStream(FileChannel.open(pdfFilePath, StandardOpenOption.READ));
      return pdfToTif(inputStream);

    }
    finally {
      if (inputStream != null) {
        inputStream.close();
      }
    }

  }

  protected List<Path> pdfToTif(InputStream pdfInput) throws IOException {

    List<Path> tiffPaths = new ArrayList<>();
    OutputStream os = null;
    try {
      PDDocument pddoc = null;
      pddoc = PDDocument.load(pdfInput);
      PDFRenderer pdfRenderer = new PDFRenderer(pddoc);
      Path workingDirectory = getWorkingDirectory();
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

  protected Path getWorkingDirectory() throws IOException {
    Path p = CONFIG.getPropertyValue(OcrWorkingDirectoryProperty.class);
    if (!Files.exists(p)) {
      Files.createDirectories(p);
    }
    return p;
  }

  protected String computeText(List<Path> tifPaths) throws IOException {
//    Path textPath = Files.createFile(getWorkingDirectory().resolve("TEXT.txt"));
//    OutputStream os = null;
    TessBaseAPI api = null;
    PIX image = null;
    BytePointer outText = null;
    try {
      api = new TessBaseAPI();
      // Initialize tesseract-ocr with English, without specifying tessdata path
//    BytePointer bp = new BytePointer(Test.class.getResource(name))

      // TODO make language configurable
      if (api.Init(CONFIG.getPropertyValue(TessdataDirectoryProperty.class).toString(), CONFIG.getPropertyValue(TesseractLanguageProperty.class)) != 0) {
        System.err.println("Could not initialize tesseract.");
        System.exit(1);
      }
//      FileChannel channel = FileChannel.open(textPath, StandardOpenOption.WRITE);
//      os = Channels.newOutputStream(channel);
      Buffer buffer = api.asBuffer();

      StringBuilder result = new StringBuilder();
      for (Path tifPath : tifPaths) {
        // Open input image with leptonica library
        image = pixRead(tifPath.toString());
        api.SetImage(image);
        // Get OCR result

        outText = api.GetUTF8Text();
        result.append(outText.getString());
//        os.write(outText.getStringBytes());
//        System.out.println("OCR output:\n" + outText.getString());
      }
      return result.toString();
    }
    finally {
//      os.flush();
//      os.close();
      // Destroy used object and release memory
      api.End();
      outText.deallocate();
      pixDestroy(image);
    }

  }

  /**
   */
  public static class OcrWorkingDirectoryProperty extends AbstractConfigProperty<Path> {

    @Override
    public String getKey() {
      return "docbox.workingFolder";
    }

    @Override
    protected Path getDefaultValue() {
      try {
        return Files.createTempDirectory("ocrWorkingDir").toAbsolutePath();
      }
      catch (IOException e) {
        throw new ProcessingException(new ProcessingStatus("Could not create working directory for OCR.", e, 0, IStatus.ERROR));
      }
    }

    @Override
    protected Path parse(String value) {
      return Paths.get(value);
    }
  }

  public static class TessdataDirectoryProperty extends AbstractConfigProperty<Path> {

    @Override
    public String getKey() {
      return "docbox.tesserarct.tessdata";
    }

    @Override
    protected Path getDefaultValue() {
      return Paths.get("/var/lib/tesseract/tessdata");
    }

    @Override
    protected Path parse(String value) {
      return Paths.get(value);
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

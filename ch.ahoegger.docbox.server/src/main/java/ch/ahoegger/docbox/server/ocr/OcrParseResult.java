package ch.ahoegger.docbox.server.ocr;

import java.nio.file.Path;

import org.eclipse.scout.rt.platform.util.StringUtility;

/**
 * <h3>{@link OcrParseResult}</h3>
 *
 * @author aho
 */
public class OcrParseResult {
  private String m_text;
  private Path m_workingDirectory;
  private boolean m_ocrParsed;

  public void setText(String text) {
    m_text = text;
  }

  public String getText() {
    return m_text;
  }

  public boolean hasText() {
    return StringUtility.hasText(getText());
  }

  public void setOcrParsed(boolean ocrParsed) {
    m_ocrParsed = ocrParsed;
  }

  public boolean isOcrParsed() {
    return m_ocrParsed;
  }

  public void setWorkingDirectory(Path workingDirectory) {
    m_workingDirectory = workingDirectory;
  }

  public Path getWorkingDirectory() {
    return m_workingDirectory;
  }
}

package ch.ahoegger.docbox.server.ocr;

import java.nio.file.Path;

/**
 * <h3>{@link OcrParseResult}</h3>
 *
 * @author Andreas Hoegger
 */
public class OcrParseResult {

  public static enum ParseError {
    CouldNotParseText
  }

  private String m_text;
  private Path m_workingDirectory;
  private boolean m_ocrParsed;
  private ParseError m_parseError;

  public OcrParseResult withText(String text) {
    m_text = text;
    return this;
  }

  public String getText() {
    return m_text;
  }

  public OcrParseResult withOcrParsed(boolean ocrParsed) {
    m_ocrParsed = ocrParsed;
    return this;
  }

  public boolean isOcrParsed() {
    return m_ocrParsed;
  }

  public OcrParseResult withWorkingDirectory(Path workingDirectory) {
    m_workingDirectory = workingDirectory;
    return this;
  }

  public Path getWorkingDirectory() {
    return m_workingDirectory;
  }

  public ParseError getParseError() {
    return m_parseError;
  }

  public OcrParseResult withParseError(ParseError parseError) {
    m_parseError = parseError;
    return this;
  }
}

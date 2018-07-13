package ch.ahoegger.docbox.shared.ocr;

import java.math.BigDecimal;

import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.text.TEXTS;
import org.eclipse.scout.rt.platform.util.ObjectUtility;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCode;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCodeType;

/**
 * <h3>{@link OcrLanguageCodeType}</h3>
 *
 * @author Andreas Hoegger
 */
public class OcrLanguageCodeType extends AbstractCodeType<BigDecimal, String> {

  private static final long serialVersionUID = 1L;
  public static final BigDecimal ID = BigDecimal.valueOf(100);

  @Override
  protected String getConfiguredText() {
    return TEXTS.get("Language");
  }

  @Override
  public BigDecimal getId() {
    return ID;
  }

  @Order(1000)
  public static class GermanCode extends AbstractCode<String> {
    private static final long serialVersionUID = 1L;
    public static final String ID = "deu";

    @Override
    protected String getConfiguredText() {
      return TEXTS.get("german");
    }

    @Override
    public String getId() {
      return ID;
    }

    public static boolean isEqual(BigDecimal value) {
      return ObjectUtility.equals(ID, value);
    }
  }

  @Order(1000)
  public static class EnglishCode extends AbstractCode<String> {
    private static final long serialVersionUID = 1L;
    public static final String ID = "eng";

    @Override
    protected String getConfiguredText() {
      return TEXTS.get("english");
    }

    @Override
    public String getId() {
      return ID;
    }

    public static boolean isEqual(BigDecimal value) {
      return ObjectUtility.equals(ID, value);
    }
  }

}

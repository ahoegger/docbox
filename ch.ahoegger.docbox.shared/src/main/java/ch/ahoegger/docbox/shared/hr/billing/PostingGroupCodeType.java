package ch.ahoegger.docbox.shared.hr.billing;

import java.math.BigDecimal;

import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.text.TEXTS;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCode;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCodeType;

/**
 * <h3>{@link PostingGroupCodeType}</h3>
 *
 * @author Andreas Hoegger
 */
public class PostingGroupCodeType extends AbstractCodeType<BigDecimal, BigDecimal> {

  private static final long serialVersionUID = 1L;
  public static final BigDecimal ID = BigDecimal.valueOf(100L);

  @Override
  protected String getConfiguredText() {
    return TEXTS.get("EntityType");
  }

  @Override
  public BigDecimal getId() {
    return ID;
  }

  @Order(1000)
  public static class UnbilledCode extends AbstractCode<BigDecimal> {
    private static final long serialVersionUID = 1L;
    public static final BigDecimal ID = BigDecimal.valueOf(-1);

    @Override
    protected String getConfiguredText() {
      return TEXTS.get("Unbilled");
    }

    @Override
    public BigDecimal getId() {
      return ID;
    }
  }
}

package ch.ahoegger.docbox.shared.hr.tax;

import java.math.BigDecimal;

import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.text.TEXTS;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCode;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCodeType;

import ch.ahoegger.docbox.shared.IHolyIds;

/**
 * <h3>{@link TaxCodeType}</h3>
 *
 * @author aho
 */
public class TaxCodeType extends AbstractCodeType<BigDecimal, BigDecimal> {
  private static final long serialVersionUID = 1L;
  public static final BigDecimal ID = BigDecimal.valueOf(IHolyIds.TAX_CODE_TYPE_ID);

  @Override
  protected String getConfiguredText() {
    return TEXTS.get("TaxType");
  }

  @Override
  public BigDecimal getId() {
    return ID;
  }

  @Order(1000)
  public static class SourceTax extends AbstractCode<BigDecimal> {
    private static final long serialVersionUID = 1L;
    public static final BigDecimal ID = BigDecimal.valueOf(IHolyIds.SOURCE_TAX_ID);

    @Override
    protected String getConfiguredText() {
      return TEXTS.get("SourceTax");
    }

    @Override
    public BigDecimal getId() {
      return ID;
    }
  }

  @Order(2000)
  public static class RegularTax extends AbstractCode<BigDecimal> {
    private static final long serialVersionUID = 1L;
    public static final BigDecimal ID = BigDecimal.valueOf(IHolyIds.REGULAR_TAX_ID);

    @Override
    protected String getConfiguredText() {
      return TEXTS.get("RegularTax");
    }

    @Override
    public BigDecimal getId() {
      return ID;
    }
  }

}

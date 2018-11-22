package ch.ahoegger.docbox.shared.hr;

import java.math.BigDecimal;

import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.text.TEXTS;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCode;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCodeType;

import ch.ahoegger.docbox.shared.IHolyIds;

/**
 * <h3>{@link StatementCodeType}</h3>
 *
 * @author aho
 */
public class StatementCodeType extends AbstractCodeType<BigDecimal, BigDecimal> {

  private static final long serialVersionUID = 1L;
  public static final BigDecimal ID = BigDecimal.valueOf(IHolyIds.STATEMENT_CODE_TYPE_ID);

  @Override
  protected String getConfiguredText() {
    return TEXTS.get("StatementType");
  }

  @Override
  public BigDecimal getId() {
    return ID;
  }

  @Order(1000)
  public static class StatementMonthlyCode extends AbstractCode<BigDecimal> {
    private static final long serialVersionUID = 1L;
    public static final BigDecimal ID = BigDecimal.valueOf(IHolyIds.STATEMENT_MONTHLY_CODE_ID);

    @Override
    protected String getConfiguredText() {
      return TEXTS.get("Monthly");
    }

    @Override
    public BigDecimal getId() {
      return ID;
    }
  }

  @Order(1000)
  public static class StatementYearlyCode extends AbstractCode<BigDecimal> {
    private static final long serialVersionUID = 1L;
    public static final BigDecimal ID = BigDecimal.valueOf(IHolyIds.STATEMENT_YEARLY_CODE_ID);

    @Override
    protected String getConfiguredText() {
      return TEXTS.get("Yearly");
    }

    @Override
    public BigDecimal getId() {
      return ID;
    }
  }
}

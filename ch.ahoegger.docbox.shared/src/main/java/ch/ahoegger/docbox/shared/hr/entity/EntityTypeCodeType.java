package ch.ahoegger.docbox.shared.hr.entity;

import java.math.BigDecimal;

import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.util.ObjectUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCode;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCodeType;

/**
 * <h3>{@link EntityTypeCodeType}</h3>
 *
 * @author Andreas Hoegger
 */
public class EntityTypeCodeType extends AbstractCodeType<BigDecimal, BigDecimal> {

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
  public static class WorkCode extends AbstractCode<BigDecimal> {
    private static final long serialVersionUID = 1L;
    public static final BigDecimal ID = BigDecimal.valueOf(101L);

    @Override
    protected String getConfiguredText() {
      return TEXTS.get("Work");
    }

    @Override
    public BigDecimal getId() {
      return ID;
    }

    public static boolean isEqual(BigDecimal value) {
      return ObjectUtility.equals(ID, value);
    }
  }

  @Order(1000)
  public static class ExpenseCode extends AbstractCode<BigDecimal> {
    private static final long serialVersionUID = 1L;
    public static final BigDecimal ID = BigDecimal.valueOf(100L);

    @Override
    protected String getConfiguredText() {
      return TEXTS.get("Expense");
    }

    @Override
    public BigDecimal getId() {
      return ID;
    }

    public static boolean isEqual(BigDecimal value) {
      return ObjectUtility.equals(ID, value);
    }
  }

}

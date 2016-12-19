package ch.ahoegger.docbox.shared.hr.entity;

import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCode;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCodeType;

/**
 * <h3>{@link EntityTypeCodeType}</h3>
 *
 * @author Andreas Hoegger
 */
public class EntityTypeCodeType extends AbstractCodeType<Long, Long> {

  private static final long serialVersionUID = 1L;
  public static final long ID = 100L;

  @Override
  protected String getConfiguredText() {
    return TEXTS.get("EntityType");
  }

  @Override
  public Long getId() {
    return ID;
  }

  @Order(1000)
  public static class WorkCode extends AbstractCode<Long> {
    private static final long serialVersionUID = 1L;
    public static final long ID = 101L;

    @Override
    protected String getConfiguredText() {
      return TEXTS.get("Work");
    }

    @Override
    public Long getId() {
      return ID;
    }
  }

  @Order(1000)
  public static class ExpenseCode extends AbstractCode<Long> {
    private static final long serialVersionUID = 1L;
    public static final long ID = 100L;

    @Override
    protected String getConfiguredText() {
      return TEXTS.get("Expense");
    }

    @Override
    public Long getId() {
      return ID;
    }
  }

}

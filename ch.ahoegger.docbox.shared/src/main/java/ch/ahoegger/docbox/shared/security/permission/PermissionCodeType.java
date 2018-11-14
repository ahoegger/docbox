package ch.ahoegger.docbox.shared.security.permission;

import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.text.TEXTS;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCode;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCodeType;

/**
 * <h3>{@link PermissionCodeType}</h3>
 *
 * @author Andreas Hoegger
 */
public class PermissionCodeType extends AbstractCodeType<Integer, Integer> {

  private static final long serialVersionUID = 1L;
  public static final int ID = 0;

  @Override
  public Integer getId() {
    return ID;
  }

  @Order(0)
  public static class NoneCode extends AbstractCode<Integer> {
    private static final long serialVersionUID = 1L;
    public static final int ID = 0;

    @Override
    protected String getConfiguredText() {
      return TEXTS.get("None");
    }

    @Override
    public Integer getId() {
      return ID;
    }

    @Override
    protected boolean getConfiguredActive() {
      return false;
    }
  }

  @Order(1000)
  public static class ReadCode extends AbstractCode<Integer> {
    private static final long serialVersionUID = 1L;
    public static final int ID = 1;

    @Override
    protected String getConfiguredText() {
      return TEXTS.get("Read");
    }

    @Override
    public Integer getId() {
      return ID;
    }
  }

  @Order(2000)
  public static class WriteCode extends AbstractCode<Integer> {
    private static final long serialVersionUID = 1L;
    public static final int ID = 2;

    @Override
    protected String getConfiguredText() {
      return TEXTS.get("write");
    }

    @Override
    public Integer getId() {
      return ID;
    }
  }

  @Order(3000)
  public static class OwnerCode extends AbstractCode<Integer> {
    private static final long serialVersionUID = 1L;
    public static final int ID = 3;

    @Override
    protected String getConfiguredText() {
      return TEXTS.get("Owner");
    }

    @Override
    public Integer getId() {
      return ID;
    }
  }

}

package ch.ahoegger.docbox.client.hr.entity;

import java.math.BigDecimal;

import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractBigDecimalColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractDateColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractSmartColumn;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;

import ch.ahoegger.docbox.shared.hr.entity.EntityTypeCodeType;
import ch.ahoegger.docbox.shared.hr.entity.IEntityTable;

/**
 * <h3>{@link AbstractEntityTable}</h3>
 *
 * @author Andreas Hoegger
 */
public abstract class AbstractEntityTable extends AbstractTable {

  public DateColumn getDateColumn() {
    return getColumnSet().getColumnByClass(DateColumn.class);
  }

  public BilledColumn getBilledColumn() {
    return getColumnSet().getColumnByClass(BilledColumn.class);
  }

  public AmountColumn getAmountColumn() {
    return getColumnSet().getColumnByClass(AmountColumn.class);
  }

  public EntityTypeColumn getEntityTypeColumn() {
    return getColumnSet().getColumnByClass(EntityTypeColumn.class);
  }

  public PartnerIdColumn getPartnerIdColumn() {
    return getColumnSet().getColumnByClass(PartnerIdColumn.class);
  }

  public HoursColumn getHoursColumn() {
    return getColumnSet().getColumnByClass(HoursColumn.class);
  }

  public EnityIdColumn getEnityIdColumn() {
    return getColumnSet().getColumnByClass(EnityIdColumn.class);
  }

  @Order(1000)
  public class EnityIdColumn extends AbstractBigDecimalColumn {
    @Override
    protected boolean getConfiguredDisplayable() {
      return false;
    }
  }

  @Order(1500)
  public class PartnerIdColumn extends AbstractBigDecimalColumn {

    @Override
    protected boolean getConfiguredDisplayable() {
      return false;
    }
  }

  @Order(2000)
  public class DateColumn extends AbstractDateColumn {
    @Override
    protected String getConfiguredHeaderText() {
      return TEXTS.get("Date");
    }

    @Override
    public int getSortIndex() {
      return 1;
    }

    @Override
    protected int getConfiguredWidth() {
      return 120;
    }
  }

  @Order(2500)
  public class EntityTypeColumn extends AbstractSmartColumn<Long> {
    @Override
    protected String getConfiguredHeaderText() {
      return TEXTS.get("EntityType");
    }

    @Override
    protected Class<? extends ICodeType<?, Long>> getConfiguredCodeType() {
      return EntityTypeCodeType.class;
    }

    @Override
    protected int getConfiguredWidth() {
      return 150;
    }
  }

  @Order(3000)
  public class HoursColumn extends AbstractBigDecimalColumn {
    @Override
    protected String getConfiguredHeaderText() {
      return TEXTS.get("Hours");
    }

    @Override
    protected BigDecimal getConfiguredMinValue() {
      return IEntityTable.HOURS_MIN;
    }

    @Override
    protected BigDecimal getConfiguredMaxValue() {
      return IEntityTable.HOURS_MAX;
    }

    @Override
    protected int getConfiguredWidth() {
      return 100;
    }

  }

  @Order(3500)
  public class AmountColumn extends AbstractBigDecimalColumn {
    @Override
    protected String getConfiguredHeaderText() {
      return TEXTS.get("Amount");
    }

    @Override
    protected int getConfiguredWidth() {
      return 100;
    }

    @Override
    protected BigDecimal getConfiguredMinValue() {
      return IEntityTable.AMOUNT_MIN;
    }

    @Override
    protected BigDecimal getConfiguredMaxValue() {
      return IEntityTable.AMOUNT_MIN;
    }
  }

  @Order(4000)
  public class BilledColumn extends AbstractDateColumn {
    @Override
    protected String getConfiguredHeaderText() {
      return TEXTS.get("Billed");
    }

    @Override
    protected int getConfiguredWidth() {
      return 120;
    }
  }
}

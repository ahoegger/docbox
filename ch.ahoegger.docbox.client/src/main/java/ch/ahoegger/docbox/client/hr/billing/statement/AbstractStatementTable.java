package ch.ahoegger.docbox.client.hr.billing.statement;

import java.math.BigDecimal;

import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractBigDecimalColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractDateColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractSmartColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.text.TEXTS;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;

/**
 * <h3>{@link AbstractStatementTable}</h3>
 *
 * @author aho
 */
public abstract class AbstractStatementTable extends AbstractTable {

  public DocumentIdColumn getDocumentIdColumn() {
    return getColumnSet().getColumnByClass(DocumentIdColumn.class);
  }

  public TaxTypeColumn getTaxTypeColumn() {
    return getColumnSet().getColumnByClass(TaxTypeColumn.class);
  }

  public AccountNumberColumn getAccountNumberColumn() {
    return getColumnSet().getColumnByClass(AccountNumberColumn.class);
  }

  public HourlyWageColumn getHourlyWageColumn() {
    return getColumnSet().getColumnByClass(HourlyWageColumn.class);
  }

  public SourceTaxRateColumn getSourceTaxRateColumn() {
    return getColumnSet().getColumnByClass(SourceTaxRateColumn.class);
  }

  public WorkingHoursColumn getWorkingHoursColumn() {
    return getColumnSet().getColumnByClass(WorkingHoursColumn.class);
  }

  public WageColumn getWageColumn() {
    return getColumnSet().getColumnByClass(WageColumn.class);
  }

  public BruttoColumn getBruttoColumn() {
    return getColumnSet().getColumnByClass(BruttoColumn.class);
  }

  public PayoutColumn getPayoutColumn() {
    return getColumnSet().getColumnByClass(PayoutColumn.class);
  }

  public SourceTaxColumn getSourceTaxColumn() {
    return getColumnSet().getColumnByClass(SourceTaxColumn.class);
  }

  public SocialInsuranceTaxColumn getSocialInsuranceTaxColumn() {
    return getColumnSet().getColumnByClass(SocialInsuranceTaxColumn.class);
  }

  public VacationExtraColumn getVacationExtraColumn() {
    return getColumnSet().getColumnByClass(VacationExtraColumn.class);
  }

  public ExpensesColumn getExpensesColumn() {
    return getColumnSet().getColumnByClass(ExpensesColumn.class);
  }

  public VacationExtraRateColumn getVacationExtraRateColumn() {
    return getColumnSet().getColumnByClass(VacationExtraRateColumn.class);
  }

  public PensionsFundColumn getPensionsFundColumn() {
    return getColumnSet().getColumnByClass(PensionsFundColumn.class);
  }

  public NettoColumn getNettoColumn() {
    return getColumnSet().getColumnByClass(NettoColumn.class);
  }

  public SocialInsuranceRateColumn getSocialInsuranceRateColumn() {
    return getColumnSet().getColumnByClass(SocialInsuranceRateColumn.class);
  }

  public StatementDateColumn getStatementDateColumn() {
    return getColumnSet().getColumnByClass(StatementDateColumn.class);
  }

  public StatementIdColumn getStatementIdColumn() {
    return getColumnSet().getColumnByClass(StatementIdColumn.class);
  }

  @Order(50000)
  public class StatementIdColumn extends AbstractBigDecimalColumn {
    @Override
    protected boolean getConfiguredDisplayable() {
      return false;
    }
  }

  @Order(51000)
  public class DocumentIdColumn extends AbstractBigDecimalColumn {
    @Override
    protected boolean getConfiguredDisplayable() {
      return false;
    }
  }

  @Order(52000)
  public class TaxTypeColumn extends AbstractSmartColumn<BigDecimal> {
    @Override
    protected String getConfiguredHeaderText() {
      return TEXTS.get("TaxType");
    }

    @Override
    protected Class<? extends ILookupCall<BigDecimal>> getConfiguredLookupCall() {
      // TODO
      return null;
    }

    @Override
    protected int getConfiguredWidth() {
      return 100;
    }
  }

  @Order(53000)
  public class StatementDateColumn extends AbstractDateColumn {
    @Override
    protected String getConfiguredHeaderText() {
      return TEXTS.get("Date");
    }

    @Override
    protected int getConfiguredWidth() {
      return 100;
    }
  }

  @Order(54000)
  public class AccountNumberColumn extends AbstractStringColumn {
    @Override
    protected String getConfiguredHeaderText() {
      return TEXTS.get("AccountNumber");
    }

    @Override
    protected int getConfiguredWidth() {
      return 100;
    }

    @Override
    protected boolean getConfiguredVisible() {
      return false;
    }
  }

  @Order(55000)
  public class HourlyWageColumn extends AbstractBigDecimalColumn {
    @Override
    protected String getConfiguredHeaderText() {
      return TEXTS.get("HourlyWage");
    }

    @Override
    protected int getConfiguredWidth() {
      return 100;
    }
  }

  @Order(56000)
  public class SocialInsuranceRateColumn extends AbstractBigDecimalColumn {
    @Override
    protected String getConfiguredHeaderText() {
      return TEXTS.get("SocialSecurityTaxRate");
    }

    @Override
    protected int getConfiguredWidth() {
      return 100;
    }

    @Override
    protected boolean getConfiguredVisible() {
      return false;
    }
  }

  @Order(57000)
  public class SourceTaxRateColumn extends AbstractBigDecimalColumn {
    @Override
    protected String getConfiguredHeaderText() {
      return TEXTS.get("SourceTaxRate");
    }

    @Override
    protected int getConfiguredWidth() {
      return 100;
    }

    @Override
    protected boolean getConfiguredVisible() {
      return false;
    }
  }

  @Order(57500)
  public class VacationExtraRateColumn extends AbstractBigDecimalColumn {
    @Override
    protected String getConfiguredHeaderText() {
      return TEXTS.get("VacationExtraRate");
    }

    @Override
    protected int getConfiguredWidth() {
      return 100;
    }

    @Override
    protected boolean getConfiguredVisible() {
      return false;
    }
  }

  @Order(58000)
  public class WorkingHoursColumn extends AbstractBigDecimalColumn {
    @Override
    protected String getConfiguredHeaderText() {
      return TEXTS.get("Hours");
    }

    @Override
    protected int getConfiguredWidth() {
      return 100;
    }
  }

  @Order(59000)
  public class WageColumn extends AbstractBigDecimalColumn {
    @Override
    protected String getConfiguredHeaderText() {
      return TEXTS.get("Wage");
    }

    @Override
    protected String getConfiguredHeaderTooltipText() {
      return TEXTS.get("WageTooltip");
    }

    @Override
    protected int getConfiguredWidth() {
      return 100;
    }

    @Override
    protected boolean getConfiguredVisible() {
      return false;
    }
  }

  @Order(60000)
  public class BruttoColumn extends AbstractBigDecimalColumn {
    @Override
    protected String getConfiguredHeaderText() {
      return TEXTS.get("BruttoWage");
    }

    @Override
    protected int getConfiguredWidth() {
      return 100;
    }
  }

  @Order(61000)
  public class NettoColumn extends AbstractBigDecimalColumn {
    @Override
    protected String getConfiguredHeaderText() {
      return TEXTS.get("NettoWage");
    }

    @Override
    protected int getConfiguredWidth() {
      return 100;
    }

    @Override
    protected boolean getConfiguredVisible() {
      return false;
    }
  }

  @Order(62000)
  public class PayoutColumn extends AbstractBigDecimalColumn {
    @Override
    protected String getConfiguredHeaderText() {
      return TEXTS.get("Payout");
    }

    @Override
    protected int getConfiguredWidth() {
      return 100;
    }
  }

  @Order(63000)
  public class SourceTaxColumn extends AbstractBigDecimalColumn {
    @Override
    protected String getConfiguredHeaderText() {
      return TEXTS.get("SourceTax");
    }

    @Override
    protected int getConfiguredWidth() {
      return 100;
    }

    @Override
    protected boolean getConfiguredVisible() {
      return false;
    }
  }

  @Order(63500)
  public class PensionsFundColumn extends AbstractBigDecimalColumn {
    @Override
    protected String getConfiguredHeaderText() {
      return TEXTS.get("PensionsFund");
    }

    @Override
    protected int getConfiguredWidth() {
      return 120;
    }
  }

  @Order(64000)
  public class SocialInsuranceTaxColumn extends AbstractBigDecimalColumn {
    @Override
    protected String getConfiguredHeaderText() {
      return TEXTS.get("SocialSecurityTax");
    }

    @Override
    protected int getConfiguredWidth() {
      return 100;
    }

    @Override
    protected boolean getConfiguredVisible() {
      return false;
    }
  }

  @Order(65000)
  public class VacationExtraColumn extends AbstractBigDecimalColumn {
    @Override
    protected String getConfiguredHeaderText() {
      return TEXTS.get("VacationExtra");
    }

    @Override
    protected int getConfiguredWidth() {
      return 100;
    }

    @Override
    protected boolean getConfiguredVisible() {
      return false;
    }
  }

  @Order(66000)
  public class ExpensesColumn extends AbstractBigDecimalColumn {
    @Override
    protected String getConfiguredHeaderText() {
      return TEXTS.get("Expenses");
    }

    @Override
    protected int getConfiguredWidth() {
      return 100;
    }
  }

}

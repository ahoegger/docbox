package org.ch.ahoegger.docbox.jasper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

import org.eclipse.scout.rt.platform.BEANS;
import org.junit.Assert;
import org.junit.Test;

/**
 * <h3>{@link WageReportServiceTest}</h3>
 *
 * @author Andreas Hoegger
 */
public class WageReportServiceTest {

  @Test
  public void createReport() {
    NumberFormat formatFloat2FractionDigits = NumberFormat.getInstance(new Locale("de", "CH"));
    formatFloat2FractionDigits.setMaximumFractionDigits(2);
    formatFloat2FractionDigits.setMinimumFractionDigits(2);
    BigDecimal financeRound = BEANS.get(WageReportService.class).financeRound(BigDecimal.valueOf(175.67), BigDecimal.valueOf(0.05), RoundingMode.UP);
    Assert.assertEquals(financeRound.doubleValue(), 175.70, 0.0000000001);

  }

}

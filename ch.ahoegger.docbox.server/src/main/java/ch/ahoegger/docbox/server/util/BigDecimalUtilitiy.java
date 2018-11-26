package ch.ahoegger.docbox.server.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * <h3>{@link BigDecimalUtilitiy}</h3>
 *
 * @author aho
 */
public class BigDecimalUtilitiy {

  public static BigDecimal financeRound(BigDecimal value, BigDecimal increment,
      RoundingMode roundingMode) {
    if (increment.signum() == 0) {
      // 0 increment does not make much sense, but prevent division by 0
      return value;
    }
    else {
      BigDecimal divided = value.divide(increment, 0, roundingMode);
      BigDecimal result = divided.multiply(increment);
      return result;
    }
  }
}

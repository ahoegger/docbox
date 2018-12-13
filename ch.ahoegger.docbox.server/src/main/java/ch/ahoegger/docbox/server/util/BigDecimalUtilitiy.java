package ch.ahoegger.docbox.server.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

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

  public static BigDecimal orZero(BigDecimal n) {
    return Optional.ofNullable(n).orElse(BigDecimal.ZERO);
  }

  public static BigDecimal nullSafeAdd(BigDecimal n1, BigDecimal n2) {
    return Optional.ofNullable(n1).orElse(BigDecimal.ZERO).add(Optional.ofNullable(n2).orElse(BigDecimal.ZERO));
  }

  public static boolean sameNumber(BigDecimal n1, BigDecimal n2) {
    if (n1 == null && n2 == null) {
      return true;
    }
    if (n1 == null) {
      return false;
    }
    if (n2 == null) {
      return false;
    }
    return n1.compareTo(n2) == 0;
  }

}

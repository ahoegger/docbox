package org.ch.ahoegger.docbox.jasper;

import java.math.BigDecimal;

import org.eclipse.scout.rt.platform.config.AbstractConfigProperty;
import org.eclipse.scout.rt.platform.util.StringUtility;

/**
 * <h3>{@link AbstractProcentageProperty}</h3>
 *
 * @author Andreas Hoegger
 */
public abstract class AbstractProcentageProperty extends AbstractConfigProperty<BigDecimal> {

  @Override
  protected BigDecimal parse(String value) {
    if (!StringUtility.hasText(value)) {
      return null;
    }
    return new BigDecimal(value);
  }
}

package ch.ahoegger.docbox.shared.util;

import java.util.Locale;

import org.eclipse.scout.rt.platform.Replace;
import org.eclipse.scout.rt.platform.nls.NlsLocale;
import org.eclipse.scout.rt.platform.util.date.DateFormatProvider;

/**
 * <h3>{@link DocboxDateFormatProvider}</h3>
 *
 * @author Andreas Hoegger
 */
@Replace
public class DocboxDateFormatProvider extends DateFormatProvider {

  /**
   * <pre>
   * en-ch    "dd.MM.yyyy"
   * </pre>
   */
  @Override
  protected String getIsolatedDateFormatPattern(Locale locale) {
    if (locale == null) {
      locale = NlsLocale.get();
    }
    if ("ch".equalsIgnoreCase(locale.getCountry())) {
      return "dd.MM.yyyy";
    }

    return super.getIsolatedDateFormatPattern(locale);
  }
}

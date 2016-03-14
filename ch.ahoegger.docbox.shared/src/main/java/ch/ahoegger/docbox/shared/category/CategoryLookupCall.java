package ch.ahoegger.docbox.shared.category;

import java.math.BigDecimal;

import org.eclipse.scout.rt.shared.services.lookup.ILookupService;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;

/**
 * <h3>{@link CategoryLookupCall}</h3>
 *
 * @author aho
 */
public class CategoryLookupCall extends LookupCall<BigDecimal> {

  private static final long serialVersionUID = 1L;

  @Override
  protected Class<? extends ILookupService<BigDecimal>> getConfiguredService() {
    return ICategoryLookupService.class;
  }
}

package ch.ahoegger.docbox.shared.validation;

import org.eclipse.scout.rt.platform.Bean;

/**
 * <h3>{@link IStartupValidatableBean}</h3>
 *
 * @author Andreas Hoegger
 */
@Bean
public interface IStartupValidatableBean {

  boolean validate();
}

package ch.ahoegger.docbox.shared.validation;

import org.eclipse.scout.rt.platform.Bean;

/**
 * <h3>{@link IStartupValidatableBean}</h3>
 *
 * @author aho
 */
@Bean
public interface IStartupValidatableBean {

  boolean validate();
}

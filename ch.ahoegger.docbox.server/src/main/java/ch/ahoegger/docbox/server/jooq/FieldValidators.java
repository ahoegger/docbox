package ch.ahoegger.docbox.server.jooq;

/**
 * <h3>{@link FieldValidators}</h3>
 *
 * @author aho
 */
public final class FieldValidators {
  private FieldValidators() {

  }

  public static NotNullValidator notNullValidator() {
    return new NotNullValidator();
  }

  public static UnmodifiableValidator unmodifiableValidator() {
    return new UnmodifiableValidator();
  }

}

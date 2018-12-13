package ch.ahoegger.docbox.server.or.generator.converter;

import org.jooq.Converter;

import ch.ahoegger.docbox.server.or.generator.Version;

/**
 * <h3>{@link VersionConverter}</h3>
 *
 * @author Andreas Hoegger
 */
public class VersionConverter implements Converter<String, Version> {
  private static final long serialVersionUID = 1L;

  @Override
  public Class<String> fromType() {
    return String.class;
  }

  @Override
  public Version from(String databaseObject) {
    return Version.parse(databaseObject);
  }

  @Override
  public Class<Version> toType() {
    return Version.class;
  }

  @Override
  public String to(Version userObject) {
    return userObject.toString();
  }
}

package ch.ahoegger.docbox.server.security;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import org.eclipse.scout.rt.platform.ApplicationScoped;
import org.eclipse.scout.rt.platform.holders.NVPair;
import org.eclipse.scout.rt.platform.security.SecurityUtility;
import org.eclipse.scout.rt.server.jdbc.SQL;
import org.eclipse.scout.rt.shared.servicetunnel.RemoteServiceAccessDenied;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.shared.administration.user.IUserTable;

/**
 * <h3>{@link SecurityService}</h3>
 *
 * @author aho
 */
@ApplicationScoped
public class SecurityService implements IUserTable {
  private static final Logger LOG = LoggerFactory.getLogger(SecurityService.class);

  private static final byte[] SALT = "[B@484b61fc".getBytes();

  public boolean authenticate(String username, final char[] passwordPlainText) {
    String passwordPlain = new String(passwordPlainText);

    StringBuilder statementBuilder = new StringBuilder()
        .append("SELECT ").append(PASSWORD).append(" FROM ").append(TABLE_NAME)
        .append(" WHERE ").append(ACTIVE)
        .append(" AND ").append(USERNAME).append(" = :username");

    Object[][] result = SQL.select(statementBuilder.toString(), new NVPair("username", username));

    if (result.length == 1) {
      if ("empty".equalsIgnoreCase(passwordPlain)) {
        LOG.warn("Authenicate user({}) with empty password. Do not use hash compare.", username);
        final boolean authenticated = result[0][0].equals(passwordPlain);
        LOG.warn("Authenicate user({}) with empty password. Do not use hash compare. Login successful:'{}'", username, authenticated);
        return authenticated;
      }
      else {
        String pwHash = new String(createPasswordHash(passwordPlainText));
        final boolean authenicated = result[0][0].equals(pwHash);
        if (LOG.isInfoEnabled()) {
          LOG.warn("Authenicate user({}). Login successful:'{}'", username, authenicated);
        }
        return authenicated;
      }
    }
    return false;

  }

  @RemoteServiceAccessDenied
  public byte[] createPasswordHash(final char[] password) {
    return SecurityUtility.hash(toBytes(password), SALT);
  }

  @RemoteServiceAccessDenied
  private byte[] toBytes(final char[] password) {
    try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
      final OutputStreamWriter writer = new OutputStreamWriter(os, StandardCharsets.UTF_16);
      writer.write(password);
      writer.flush();
      return os.toByteArray();
    }
    catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }
}

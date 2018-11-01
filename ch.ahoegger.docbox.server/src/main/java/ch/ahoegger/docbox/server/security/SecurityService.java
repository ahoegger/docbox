package ch.ahoegger.docbox.server.security;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import org.ch.ahoegger.docbox.server.or.app.tables.DocboxUser;
import org.eclipse.scout.rt.platform.ApplicationScoped;
import org.eclipse.scout.rt.platform.security.SecurityUtility;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.server.jdbc.SQL;
import org.eclipse.scout.rt.shared.servicetunnel.RemoteServiceAccessDenied;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.or.definition.table.IUserTable;

/**
 * <h3>{@link SecurityService}</h3>
 *
 * @author Andreas Hoegger
 */
@ApplicationScoped
public class SecurityService implements IUserTable {
  private static final Logger LOG = LoggerFactory.getLogger(SecurityService.class);

  private static final byte[] SALT = "[B@484b61fc".getBytes();

  public boolean authenticate(String username, final char[] passwordPlainText) {
    String passwordPlain = new String(passwordPlainText);

    DocboxUser t = DocboxUser.DOCBOX_USER;

    String dbPass = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .select(t.PASSWORD)
        .from(t)
        .where(t.ACTIVE)
        .and(t.USERNAME.eq(username))
        .fetch()
        .stream()
        .map(rec -> rec.get(t.PASSWORD))
        .findFirst()
        .orElse(null);

    if (StringUtility.hasText(dbPass)) {
      if ("empty".equalsIgnoreCase(passwordPlain)) {
        LOG.warn("Authenicate user({}) with empty password. Do not use hash compare.", username);
        final boolean authenticated = dbPass.equals(passwordPlain);
        LOG.warn("Authenicate user({}) with empty password. Do not use hash compare. Login successful:'{}'", username, authenticated);
        return authenticated;
      }
      else {
        String pwHash = new String(createPasswordHash(passwordPlainText));
        final boolean authenicated = dbPass.equals(pwHash);
        if (LOG.isInfoEnabled()) {
          LOG.info("Authenicate user({}). Login successful:'{}'", username, authenicated);
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

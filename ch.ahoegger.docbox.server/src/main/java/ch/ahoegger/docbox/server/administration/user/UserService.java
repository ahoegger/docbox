package ch.ahoegger.docbox.server.administration.user;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.holders.NVPair;
import org.eclipse.scout.rt.platform.security.SecurityUtility;
import org.eclipse.scout.rt.server.jdbc.SQL;
import org.eclipse.scout.rt.shared.servicetunnel.RemoteServiceAccessDenied;

import ch.ahoegger.docbox.server.database.IDocboxSqlService;
import ch.ahoegger.docbox.shared.administration.user.IUserService;
import ch.ahoegger.docbox.shared.administration.user.UserTablePageData;

/**
 * <h3>{@link UserService}</h3>
 *
 * @author aho
 */
public class UserService implements IUserService {
  private static final byte[] SALT = "[B@484b61fc".getBytes();

  @Override
  public UserTablePageData getUserTableData() {
    UserTablePageData pageData = new UserTablePageData();

    // get data from db

    IDocboxSqlService sqlService = BEANS.get(IDocboxSqlService.class);

    StringBuilder sqlSelect = new StringBuilder("SELECT   USER_NR, "
        + "         NAME, "
        + "         FIRSTNAME "
        + "FROM     DOCBOX_USER "
        + "INTO     :{page.userId}, "
        + "         :{page.name}, "
        + "         :{page.firstname}");

    sqlService.selectInto(sqlSelect.toString(), new NVPair("page", pageData));
    return pageData;

  }

  @Override
  public boolean authenticate(String username, final char[] passwordPlainText) {
    String pwHash = new String(createPasswordHash(passwordPlainText));

    StringBuilder sqlBuilder = new StringBuilder();
    sqlBuilder.append("SELECT PASSWORD FROM DOCBOX_USER WHERE 1 = 1");
    sqlBuilder.append(" AND USERNAME = :username");
    Object[][] result = SQL.select(sqlBuilder.toString(), new NVPair("username", username));
    if (result.length == 1) {
      return result[0][0].equals(pwHash);
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

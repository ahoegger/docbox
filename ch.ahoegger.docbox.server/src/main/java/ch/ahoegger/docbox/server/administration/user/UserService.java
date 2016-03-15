package ch.ahoegger.docbox.server.administration.user;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Set;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.holders.NVPair;
import org.eclipse.scout.rt.platform.security.SecurityUtility;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.eclipse.scout.rt.server.jdbc.SQL;
import org.eclipse.scout.rt.shared.servicetunnel.RemoteServiceAccessDenied;

import ch.ahoegger.docbox.server.database.SqlFramentBuilder;
import ch.ahoegger.docbox.shared.administration.user.IUserService;
import ch.ahoegger.docbox.shared.administration.user.IUserTable;
import ch.ahoegger.docbox.shared.administration.user.UserFormData;
import ch.ahoegger.docbox.shared.administration.user.UserTablePageData;

/**
 * <h3>{@link UserService}</h3>
 *
 * @author aho
 */
public class UserService implements IUserService, IUserTable {
  private static final byte[] SALT = "[B@484b61fc".getBytes();

  @Override
  public UserTablePageData getUserTableData() {
    UserTablePageData pageData = new UserTablePageData();

    // get data from db

    ISqlService sqlService = BEANS.get(ISqlService.class);

    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("SELECT ").append(SqlFramentBuilder.columns(USERNAME, NAME, FIRSTNAME)).append(" FROM ").append(TABLE_NAME);
    statementBuilder.append(" INTO ").append(":{page.username},  :{page.name},   :{page.firstname}");

    sqlService.selectInto(statementBuilder.toString(), new NVPair("page", pageData));
    return pageData;
  }

  @Override
  public UserFormData load(UserFormData formData) {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("SELECT ").append(SqlFramentBuilder.columns(FIRSTNAME, NAME, USERNAME, INSERT_DATE, VALID_DATE));
    statementBuilder.append(" FROM ").append(TABLE_NAME).append(" WHERE ").append(USERNAME).append(" = :username");
    statementBuilder.append(" INTO ").append(":firstname, :name, :username, :insertDate, :validDate");
    SQL.selectInto(statementBuilder.toString(), formData);

    // roles
    Set<BigDecimal> roleIds = BEANS.get(UserRoleService.class).getRoleIds(formData.getUsername().getValue());
    formData.getRoleBox().setValue(roleIds);

    return formData;
  }

  @Override
  public boolean authenticate(String username, final char[] passwordPlainText) {
    String pwHash = new String(createPasswordHash(passwordPlainText));

    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("SELECT ").append(PASSWORD).append(" FROM ").append(TABLE_NAME);
    statementBuilder.append(" WHERE ").append(USERNAME).append(" = :username");
    Object[][] result = SQL.select(statementBuilder.toString(), new NVPair("username", username));
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

package ch.ahoegger.docbox.server.administration.user;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.holders.BooleanHolder;
import org.eclipse.scout.rt.platform.holders.NVPair;
import org.eclipse.scout.rt.platform.security.SecurityUtility;
import org.eclipse.scout.rt.platform.util.TypeCastUtility;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.eclipse.scout.rt.server.jdbc.SQL;
import org.eclipse.scout.rt.shared.servicetunnel.RemoteServiceAccessDenied;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.server.database.SqlFramentBuilder;
import ch.ahoegger.docbox.shared.administration.user.IUserService;
import ch.ahoegger.docbox.shared.administration.user.IUserTable;
import ch.ahoegger.docbox.shared.administration.user.UserFormData;
import ch.ahoegger.docbox.shared.administration.user.UserTablePageData;

/**
 * <h3>{@link UserService}</h3>
 *
 * @author Andreas Hoegger
 */
public class UserService implements IUserService, IUserTable {
  private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

  private static final byte[] SALT = "[B@484b61fc".getBytes();

  @Override
  public UserTablePageData getUserTableData() {
    UserTablePageData pageData = new UserTablePageData();

    // get data from db

    ISqlService sqlService = BEANS.get(ISqlService.class);

    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("SELECT ").append(SqlFramentBuilder.columns(USERNAME, NAME, FIRSTNAME, ACTIVE, ADMINISTRATOR)).append(" FROM ").append(TABLE_NAME);
    statementBuilder.append(" INTO ").append(":{page.username}, :{page.name}, :{page.firstname}, :{page.active}, :{page.administrator}");

    sqlService.selectInto(statementBuilder.toString(), new NVPair("page", pageData));
    return pageData;
  }

  @Override
  public UserFormData load(UserFormData formData) {
    BooleanHolder exists = new BooleanHolder();

    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("SELECT TRUE, ").append(SqlFramentBuilder.columns(FIRSTNAME, NAME, USERNAME, ACTIVE, ADMINISTRATOR));
    statementBuilder.append(" FROM ").append(TABLE_NAME).append(" WHERE ").append(USERNAME).append(" = :username");
    statementBuilder.append(" INTO ").append(":exists, :firstname, :name, :username, :active, :administrator");
    SQL.selectInto(statementBuilder.toString(),
        new NVPair("exists", exists),
        formData);

    if (exists.getValue() == null) {
      return null;
    }

    return formData;
  }

  @Override
  public UserFormData store(UserFormData formData) {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("UPDATE ").append(TABLE_NAME).append(" SET ")
        .append(NAME).append("= :name, ")
        .append(FIRSTNAME).append("= :firstname, ")
        .append(ACTIVE).append("= :active ");
    if (formData.getPassword().isValueSet()) {
      statementBuilder.append(", ").append(PASSWORD).append("= :password");
      formData.getPassword().setValue(new String(createPasswordHash(formData.getPassword().getValue().toCharArray())));
    }
    statementBuilder.append(" WHERE ").append(USERNAME).append(" = :username");
    SQL.update(statementBuilder.toString(), formData);
    formData.getPassword().setValue(null);
    return formData;
  }

  @Override
  public UserFormData prepareCreate(UserFormData formData) {
    formData.getActive().setValue(true);
    formData.getAdministrator().setValue(false);
    return formData;
  }

  @Override
  public UserFormData create(UserFormData formData) {
    formData.getPassword().setValue(new String(createPasswordHash(formData.getPassword().getValue().toCharArray())));
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("INSERT INTO ").append(TABLE_NAME);
    statementBuilder.append(" (").append(SqlFramentBuilder.columns(USERNAME, NAME, FIRSTNAME, ACTIVE, PASSWORD, ADMINISTRATOR)).append(")");
    statementBuilder.append(" VALUES (:username, :name, :firstname, :active, :password, :administrator)");
    SQL.insert(statementBuilder.toString(), formData);
    formData.getPassword().setValue(null);
    return formData;
  }

  @Override
  public void delete(String username) {
    // load
    UserFormData fd = new UserFormData();
    fd.getUsername().setValue(username);
    fd = load(fd);
    if (fd.getAdministrator().getValue()) {
      // do not allow to delete last admin
      StringBuilder statementBuilder = new StringBuilder();
      statementBuilder.append("SELECT COUNT(1) FROM ").append(TABLE_NAME)
          .append(" WHERE ").append(USERNAME).append(" = :username");
      Object[][] rawResult = SQL.select(statementBuilder.toString(), new NVPair("username", username));
      if (Arrays.stream(rawResult).map(row -> TypeCastUtility.castValue(row[0], Integer.class)).findFirst().get() < 2) {
        throw new ProcessingException("Can not delete last administrator.");
      }
    }
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("DELETE FROM ").append(TABLE_NAME).append(" WHERE ").append(USERNAME).append(" = :username");

    int deletedRows = SQL.delete(statementBuilder.toString(), new NVPair("username", username));
    if (deletedRows != 1) {
      LOG.warn("Deleted {} rows for '{}' username.", deletedRows, username);
    }

  }

  @Override
  public boolean authenticate(String username, final char[] passwordPlainText) {
    String pwHash = new String(createPasswordHash(passwordPlainText));

    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("SELECT ").append(PASSWORD).append(" FROM ").append(TABLE_NAME);
    statementBuilder.append(" WHERE ").append(ACTIVE)
        .append(" AND ").append(USERNAME).append(" = :username");
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

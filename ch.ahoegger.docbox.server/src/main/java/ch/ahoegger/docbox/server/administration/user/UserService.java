package ch.ahoegger.docbox.server.administration.user;

import java.sql.Connection;
import java.util.Optional;

import org.ch.ahoegger.docbox.server.or.app.tables.DefaultPermissionTable;
import org.ch.ahoegger.docbox.server.or.app.tables.DocboxUser;
import org.ch.ahoegger.docbox.server.or.app.tables.records.DocboxUserRecord;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.ProcessingStatus;
import org.eclipse.scout.rt.platform.status.IStatus;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.server.jdbc.SQL;
import org.eclipse.scout.rt.shared.servicetunnel.RemoteServiceAccessDenied;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.server.ServerSession;
import ch.ahoegger.docbox.server.security.SecurityService;
import ch.ahoegger.docbox.server.security.permission.DefaultPermissionService;
import ch.ahoegger.docbox.shared.administration.user.IUserService;
import ch.ahoegger.docbox.shared.administration.user.UserFormData;
import ch.ahoegger.docbox.shared.administration.user.UserTablePageData;
import ch.ahoegger.docbox.shared.administration.user.UserTablePageData.UserTableRowData;
import ch.ahoegger.docbox.shared.backup.IBackupService;
import ch.ahoegger.docbox.shared.security.permission.PermissionCodeType;

/**
 * <h3>{@link UserService}</h3>
 *
 * @author Andreas Hoegger
 */
public class UserService implements IUserService, IUser {
  private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

  @Override
  public UserTablePageData getUserTableData() {
    UserTablePageData pageData = new UserTablePageData();
    DocboxUser user = DocboxUser.DOCBOX_USER;

    DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .select(user.USERNAME, user.NAME, user.FIRSTNAME, user.ACTIVE, user.ADMINISTRATOR)
        .from(user)
        .fetch().forEach(rec -> {
          UserTableRowData row = pageData.addRow();
          row.setUsername(rec.get(user.USERNAME));
          row.setName(rec.get(user.NAME));
          row.setFirstname(rec.get(user.FIRSTNAME));
          row.setActive(rec.get(user.ACTIVE));
          row.setAdministrator(rec.get(user.ADMINISTRATOR));
        });

    return pageData;
  }

  @Override
  public UserFormData load(UserFormData formData) {

    DocboxUser user = DocboxUser.DOCBOX_USER.as("U");
    DefaultPermissionTable defaultPermission = DefaultPermissionTable.DEFAULT_PERMISSION_TABLE.as("DEF_PER");

    Field<String> displayNameField = IUser.createDisplayNameForAlias(user);
    return DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .select(user.FIRSTNAME, user.NAME, user.USERNAME, user.ACTIVE, user.ADMINISTRATOR, displayNameField)
        .select(defaultPermission.PERMISSION)
        .from(user)
        .leftOuterJoin(defaultPermission)
        .on(user.USERNAME.eq(defaultPermission.USERNAME))
        .where(user.USERNAME.eq(formData.getUsername().getValue()))
        .fetch()
        .map(rec -> {
          UserFormData res = (UserFormData) formData.deepCopy();
          res.getFirstname().setValue(rec.get(user.FIRSTNAME));
          res.getName().setValue(rec.get(user.NAME));
          res.getUsername().setValue(rec.get(user.USERNAME));
          res.setDisplayName(rec.get(displayNameField));
          res.getActive().setValue(rec.get(user.ACTIVE));
          res.getAdministrator().setValue(rec.get(user.ADMINISTRATOR));
          res.getDefaultPermission().setValue(rec.get(defaultPermission.PERMISSION));
          return res;
        })
        .stream()
        .findFirst()
        .orElse(null);

  }

  @Override
  public UserFormData store(UserFormData formData) {

    DocboxUser user = DocboxUser.DOCBOX_USER.as("U");

    DocboxUserRecord rec = new DocboxUserRecord()
        .with(user.USERNAME, formData.getUsername().getValue())
        .with(user.ACTIVE, formData.getActive().getValue())
        .with(user.ADMINISTRATOR, formData.getAdministrator().getValue())
        .with(user.FIRSTNAME, formData.getFirstname().getValue())
        .with(user.NAME, formData.getName().getValue());
    if (formData.getChangePassword().isValueSet() && formData.getChangePassword().getValue()) {
      rec.setPassword(new String(BEANS.get(SecurityService.class).createPasswordHash(formData.getPassword().getValue().toCharArray())));
    }

    if (DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .executeUpdate(rec) < 1) {
      return null;
    }
    formData.getPassword().setValue(null);

    BEANS.get(DefaultPermissionService.class).updateDefaultPermission(formData.getUsername().getValue(), formData.getDefaultPermission().getValue());

    // notify backup needed
    BEANS.get(IBackupService.class).notifyModification();

    return formData;
  }

  @Override
  public UserFormData prepareCreate(UserFormData formData) {
    formData.getActive().setValue(true);
    formData.getAdministrator().setValue(false);
    formData.getDefaultPermission().setValue(PermissionCodeType.ReadCode.ID);
    return formData;
  }

  @Override
  public UserFormData create(UserFormData formData) {

    DocboxUser user = DocboxUser.DOCBOX_USER;

    int insertedRows = DSL.using(SQL.getConnection(), SQLDialect.DERBY).newRecord(user)
        .with(user.USERNAME, formData.getUsername().getValue())
        .with(user.ACTIVE, formData.getActive().getValue())
        .with(user.ADMINISTRATOR, formData.getAdministrator().getValue())
        .with(user.FIRSTNAME, formData.getFirstname().getValue())
        .with(user.NAME, formData.getName().getValue())
        .with(user.PASSWORD, new String(BEANS.get(SecurityService.class).createPasswordHash(formData.getPassword().getValue().toCharArray())))
        .insert();

    if (insertedRows < 1) {
      return null;
    }
    formData.getPassword().setValue(null);

    BEANS.get(DefaultPermissionService.class).createDefaultPermission(formData.getUsername().getValue(), formData.getDefaultPermission().getValue());

    // notify backup needed
    BEANS.get(IBackupService.class).notifyModification();

    return formData;
  }

  @Override
  public boolean delete(String username) {

    if (StringUtility.equalsIgnoreCase(username, ServerSession.get().getUserId())) {
      throw new ProcessingException(new ProcessingStatus("Deletion of myself is not allowed.", IStatus.ERROR));
    }

    DocboxUser user = DocboxUser.DOCBOX_USER;

    DSLContext dsl = DSL.using(SQL.getConnection(), SQLDialect.DERBY);
    // load
    UserFormData fd = new UserFormData();
    fd.getUsername().setValue(username);
    fd = load(fd);
    if (fd.getAdministrator().getValue()) {
      // do not allow to delete last admin
      int adminCount = dsl.select(DSL.count())
          .from(user)
          .where(user.ADMINISTRATOR)
          .stream()
          .map(rec -> rec.get(0, Integer.class))
          .findFirst()
          .orElse(-1);
      if (adminCount < 2) {
        throw new ProcessingException("Can not delete last administrator.");
      }

    }

    DocboxUserRecord rec = dsl
        .fetchOne(user, user.USERNAME.eq(username));
    if (rec == null) {
      LOG.warn("Try to delete not existing record with id '{}'!", username);
      return false;
    }
    if (rec.delete() != 1) {
      LOG.error("Could not delete record with id '{}'!", username);
      return false;
    }

    BEANS.get(DefaultPermissionService.class).deleteByUsername(username);

    BEANS.get(IBackupService.class).notifyModification();
    return true;

  }

  @RemoteServiceAccessDenied
  public int insert(Connection connection, String name, String firstname, String username, String password,
      boolean active, boolean administrator) {
    return DSL.using(connection, SQLDialect.DERBY)
        .executeInsert(mapToRecord(new DocboxUserRecord(), name, firstname, username, password, active, administrator));
  }

  protected DocboxUserRecord mapToRecord(DocboxUserRecord rec, UserFormData fd) {
    if (fd == null) {
      return null;
    }
    return mapToRecord(rec, fd.getName().getValue(), fd.getFirstname().getValue(), fd.getUsername().getValue(),
        Optional.ofNullable(fd.getPassword().getValue())
            .map(plain -> new String(BEANS.get(SecurityService.class).createPasswordHash(plain.toCharArray())))
            .orElse(null),
        fd.getActive().getValue(), fd.getAdministrator().getValue());
  }

  protected DocboxUserRecord mapToRecord(DocboxUserRecord rec, String name, String firstname, String username, String password,
      boolean active, boolean administrator) {
    return rec
        .with(DocboxUser.DOCBOX_USER.ACTIVE, active)
        .with(DocboxUser.DOCBOX_USER.ADMINISTRATOR, administrator)
        .with(DocboxUser.DOCBOX_USER.FIRSTNAME, firstname)
        .with(DocboxUser.DOCBOX_USER.NAME, name)
        .with(DocboxUser.DOCBOX_USER.PASSWORD, password)
        .with(DocboxUser.DOCBOX_USER.USERNAME, username);

  }

}

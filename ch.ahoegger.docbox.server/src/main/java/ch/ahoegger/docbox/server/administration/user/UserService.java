package ch.ahoegger.docbox.server.administration.user;

import org.ch.ahoegger.docbox.server.or.app.tables.DefaultPermissionTable;
import org.ch.ahoegger.docbox.server.or.app.tables.DocboxUser;
import org.ch.ahoegger.docbox.server.or.app.tables.records.DocboxUserRecord;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.ProcessingStatus;
import org.eclipse.scout.rt.platform.status.IStatus;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.server.jdbc.SQL;
import org.jooq.DSLContext;
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
public class UserService implements IUserService {
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

    return DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .select(user.FIRSTNAME, user.NAME, user.USERNAME, user.ACTIVE, user.ADMINISTRATOR)
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

}

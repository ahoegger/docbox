package ch.ahoegger.docbox.server.administrator.user;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.junit.Assert;
import org.junit.Test;

import ch.ahoegger.docbox.server.administration.user.UserService;
import ch.ahoegger.docbox.server.database.dev.initialization.UserTableTask;
import ch.ahoegger.docbox.server.security.SecurityService;
import ch.ahoegger.docbox.server.test.util.AbstractTestWithDatabase;
import ch.ahoegger.docbox.server.test.util.DocboxAssert;
import ch.ahoegger.docbox.shared.administration.user.IUserService;
import ch.ahoegger.docbox.shared.administration.user.UserFormData;
import ch.ahoegger.docbox.shared.document.IDocumentPermissionTable;

/**
 * <h3>{@link HelloWorldFormServiceTest}</h3>
 *
 * @author Andreas Hoegger
 */

public class UserServiceTest extends AbstractTestWithDatabase {
  private final String userId01 = SUBJECT_NAME;
  private final String userId02 = "username02";
  private final String userId03 = "username03";
  private final String userId04 = "username04";
  private final String userId05 = "username05";

  @Override
  public void setupDb() throws Exception {
    super.setupDb();
    ISqlService sqlService = BEANS.get(ISqlService.class);
    BEANS.get(UserTableTask.class).insertUser(sqlService, "adminstrator", "first.admin", userId01, "secret", true, true);
    BEANS.get(UserTableTask.class).insertUser(sqlService, "name02", "firstname02", userId02, passwordHash("secret02"), true, false);
    BEANS.get(UserTableTask.class).insertUser(sqlService, "name03", "firstname03", userId03, passwordHash("secret03"), false, false);
    BEANS.get(UserTableTask.class).insertUser(sqlService, "name04", "firstname04", userId04, passwordHash("secret04"), true, false);
    BEANS.get(UserTableTask.class).insertUser(sqlService, "name05", "firstname05", userId05, passwordHash("secret05"), true, false);
  }

  @Test
  public void testCreateUser() {
    IUserService userService = BEANS.get(IUserService.class);

    UserFormData fd1 = new UserFormData();
    fd1 = userService.prepareCreate(fd1);
    fd1.getUsername().setValue("username");
    fd1.getPassword().setValue("secret");
    fd1.getName().setValue("name");
    fd1.getFirstname().setValue("firstname");
    fd1.getAdministrator().setValue(false);
    fd1.getDefaultPermission().setValue(IDocumentPermissionTable.PERMISSION_WRITE);
    fd1 = userService.create(fd1);

    UserFormData fd2 = new UserFormData();
    fd2.getUsername().setValue("username");
    fd2 = userService.load(fd2);

    DocboxAssert.assertEquals(fd1, fd2);
  }

  @Test
  public void testModifyUser() {
    IUserService userService = BEANS.get(IUserService.class);

    UserFormData fd1 = new UserFormData();
    fd1.getUsername().setValue(userId04);
    fd1 = userService.load(fd1);

    fd1.getName().setValue("modified.name");
    fd1.getFirstname().setValue("modified.firstname");
    fd1.getActive().setValue(false);
    fd1.getAdministrator().setValue(true);
    fd1.getDefaultPermission().setValue(IDocumentPermissionTable.PERMISSION_OWNER);

    userService.store(fd1);

    UserFormData fd2 = new UserFormData();
    fd2.getUsername().setValue(userId04);
    fd2 = userService.load(fd2);

    DocboxAssert.assertEquals(fd1, fd2);

  }

  @Test
  public void testModifyUserPassword() {
    UserService userService = BEANS.get(UserService.class);
    Assert.assertTrue(BEANS.get(SecurityService.class).authenticate(userId05, "secret05".toCharArray()));

    UserFormData fd1 = new UserFormData();
    fd1.getUsername().setValue(userId05);
    fd1 = userService.load(fd1);

    fd1.getChangePassword().setValue(true);
    fd1.getPassword().setValue("1234abc");
    userService.store(fd1);

    // try to login
    Assert.assertFalse(BEANS.get(SecurityService.class).authenticate(userId05, "secret05".toCharArray()));
    Assert.assertTrue(BEANS.get(SecurityService.class).authenticate(userId05, "1234abc".toCharArray()));

  }

  @Test
  public void testDelete() {
    IUserService userService = BEANS.get(IUserService.class);
    userService.delete(userId02);

    UserFormData fd1 = new UserFormData();
    fd1.getUsername().setValue(userId02);
    fd1 = userService.load(fd1);

  }

  @Test(expected = ProcessingException.class)
  public void testDeleteLastAdmin() {
    IUserService userService = BEANS.get(IUserService.class);
    userService.delete("admin");
  }

  @Test
  public void testLogin() {
    Assert.assertTrue(BEANS.get(SecurityService.class).authenticate(userId02, "secret02".toCharArray()));
    Assert.assertFalse(BEANS.get(SecurityService.class).authenticate(userId03, "secret03".toCharArray()));
  }
}

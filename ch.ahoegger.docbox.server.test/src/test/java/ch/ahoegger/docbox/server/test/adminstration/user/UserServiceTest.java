package ch.ahoegger.docbox.server.test.adminstration.user;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.ahoegger.docbox.server.administration.user.UserService;
import ch.ahoegger.docbox.server.database.initialization.UserTableTask;
import ch.ahoegger.docbox.server.test.util.AbstractTestWithDatabase;
import ch.ahoegger.docbox.server.test.util.DocboxAssert;
import ch.ahoegger.docbox.shared.administration.user.IUserService;
import ch.ahoegger.docbox.shared.administration.user.UserFormData;

/**
 * <h3>{@link HelloWorldFormServiceTest}</h3>
 *
 * @author Andreas Hoegger
 */

public class UserServiceTest extends AbstractTestWithDatabase {

  @BeforeClass
  public static void createTestRows() {
    ISqlService sqlService = BEANS.get(ISqlService.class);
    BEANS.get(UserTableTask.class).createUser(sqlService, "adminstrator", "first.admin", "admin", "secret", true, true);
    BEANS.get(UserTableTask.class).createUser(sqlService, "semil", "borak", "semil.borak", "secret", true, false);
    BEANS.get(UserTableTask.class).createUser(sqlService, "inactive.name", "inactive.firstname", "inactive.username", "inactive.secret", false, false);
    BEANS.get(UserTableTask.class).createUser(sqlService, "modify.name", "modify.firstname", "modify.username", "modify.secret", true, false);
    BEANS.get(UserTableTask.class).createUser(sqlService, "modifyPwd.name", "modifyPwd.firstname", "modifyPwd.username", "modifyPwd.secret", true, false);
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
    fd1.getUsername().setValue("modify.username");
    fd1 = userService.load(fd1);

    fd1.getName().setValue("modified.name");
    fd1.getFirstname().setValue("modified.firstname");
    fd1.getActive().setValue(false);
    fd1.getAdministrator().setValue(true);

    userService.store(fd1);

    UserFormData fd2 = new UserFormData();
    fd2.getUsername().setValue("modify.username");
    fd2 = userService.load(fd2);

    DocboxAssert.assertEquals(fd1, fd2);

  }

  @Test
  public void testModifyUserPassword() {
    UserService userService = BEANS.get(UserService.class);
    Assert.assertTrue(userService.authenticate("modifyPwd.username", "modifyPwd.secret".toCharArray()));

    UserFormData fd1 = new UserFormData();
    fd1.getUsername().setValue("modifyPwd.username");
    fd1 = userService.load(fd1);

    fd1.getChangePassword().setValue(true);
    fd1.getPassword().setValue("1234abc");
    userService.store(fd1);

    // try to login
    Assert.assertFalse(userService.authenticate("modifyPwd.username", "modifyPwd.secret".toCharArray()));
    Assert.assertTrue(userService.authenticate("modifyPwd.username", "1234abc".toCharArray()));

  }

  @Test
  public void testDelete() {
    IUserService userService = BEANS.get(IUserService.class);
    userService.delete("semil.borak");

    UserFormData fd1 = new UserFormData();
    fd1.getUsername().setValue("semil.borak");
    fd1 = userService.load(fd1);

  }

  @Test(expected = ProcessingException.class)
  public void testDeleteLastAdmin() {
    IUserService userService = BEANS.get(IUserService.class);
    userService.delete("admin");
  }

  @Test
  public void testLogin() {
    UserService userService = BEANS.get(UserService.class);
    Assert.assertTrue(userService.authenticate("semil.borak", "secret".toCharArray()));
    Assert.assertFalse(userService.authenticate("inactive.username", "inactive.secret".toCharArray()));
  }
}

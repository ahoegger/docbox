package ch.ahoegger.docbox.server.test.adminstration.user;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.ahoegger.docbox.server.administration.user.UserRoleService;
import ch.ahoegger.docbox.server.administration.user.UserService;
import ch.ahoegger.docbox.server.database.initialization.RoleTableTask;
import ch.ahoegger.docbox.server.database.initialization.UserRoleTableTask;
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
  public static final String SUBJECT_NAME = "admin";

  @BeforeClass
  public static void createTestRows() {
    ISqlService sqlService = BEANS.get(ISqlService.class);
    BEANS.get(UserTableTask.class).createUser(sqlService, "semil", "borak", "semil.borak", "secret", true);
    BEANS.get(UserTableTask.class).createUser(sqlService, "inactive.name", "inactive.firstname", "inactive.username", "inactive.secret", false);
    BEANS.get(RoleTableTask.class).createRole(sqlService, 333l, "role01");
    BEANS.get(UserRoleTableTask.class).createUserRole(sqlService, 333l, "semil.borak");
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
    fd1.getRoleBox().setValue(CollectionUtility.emptyHashSet());

    fd1 = userService.create(fd1);

    UserFormData fd2 = new UserFormData();
    fd2.getUsername().setValue("username");
    fd2 = userService.load(fd2);

    DocboxAssert.assertEquals(fd1, fd2);
  }

  @Test
  public void testCascadeDelete() {
    IUserService userService = BEANS.get(IUserService.class);
    userService.delete("semil.borak");

    UserFormData fd1 = new UserFormData();
    fd1.getUsername().setValue("semil.borak");
    fd1 = userService.load(fd1);

    Assert.assertNull(fd1);
    Assert.assertTrue(BEANS.get(UserRoleService.class).getRoleIds("semil.borak").isEmpty());
  }

  @Test
  public void testLogin() {
    UserService userService = BEANS.get(UserService.class);
    Assert.assertTrue(userService.authenticate("semil.borak", "secret".toCharArray()));
    Assert.assertFalse(userService.authenticate("inactive.username", "inactive.secret".toCharArray()));
  }
}

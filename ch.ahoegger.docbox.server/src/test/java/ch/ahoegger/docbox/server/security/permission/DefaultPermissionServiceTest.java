package ch.ahoegger.docbox.server.security.permission;

import java.util.Map;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.junit.Assert;
import org.junit.Test;

import ch.ahoegger.docbox.server.test.util.AbstractTestWithDatabase;
import ch.ahoegger.docbox.shared.security.permission.PermissionCodeType.OwnerCode;
import ch.ahoegger.docbox.shared.security.permission.PermissionCodeType.ReadCode;
import ch.ahoegger.docbox.shared.security.permission.PermissionCodeType.WriteCode;

/**
 * <h3>{@link DefaultPermissionServiceTest}</h3>
 *
 * @author aho
 */
public class DefaultPermissionServiceTest extends AbstractTestWithDatabase {

  private static final String user01 = "user01";
  private static final String user02 = "user02";
  private static final String user03 = "user03";

  @Override
  public void setupDb() throws Exception {
    super.setupDb();

    ISqlService sqlService = BEANS.get(ISqlService.class);

    BEANS.get(DefaultPermissionService.class).insertRow(sqlService.getConnection(), user01, ReadCode.ID);
    BEANS.get(DefaultPermissionService.class).insertRow(sqlService.getConnection(), user02, WriteCode.ID);
    BEANS.get(DefaultPermissionService.class).insertRow(sqlService.getConnection(), user03, OwnerCode.ID);
  }

  @Test
  public void testGet() {
    Map<String, Integer> defaultPermissions = BEANS.get(DefaultPermissionService.class).getDefaultPermissions();
    Assert.assertEquals(3, defaultPermissions.size());
    Assert.assertEquals(Integer.valueOf(ReadCode.ID), defaultPermissions.get(user01));
    Assert.assertEquals(Integer.valueOf(WriteCode.ID), defaultPermissions.get(user02));
    Assert.assertEquals(Integer.valueOf(OwnerCode.ID), defaultPermissions.get(user03));
  }

  @Test
  public void testUpdate() {
    BEANS.get(DefaultPermissionService.class).updateDefaultPermission(user02, ReadCode.ID);
    Map<String, Integer> defaultPermissions = BEANS.get(DefaultPermissionService.class).getDefaultPermissions();
    Assert.assertEquals(3, defaultPermissions.size());
    Assert.assertEquals(Integer.valueOf(ReadCode.ID), defaultPermissions.get(user01));
    Assert.assertEquals(Integer.valueOf(ReadCode.ID), defaultPermissions.get(user02));
    Assert.assertEquals(Integer.valueOf(OwnerCode.ID), defaultPermissions.get(user03));
  }

  @Test
  public void testDelete() {
    BEANS.get(DefaultPermissionService.class).deleteByUsername(user02);
    Map<String, Integer> defaultPermissions = BEANS.get(DefaultPermissionService.class).getDefaultPermissions();
    Assert.assertEquals(2, defaultPermissions.size());
    Assert.assertEquals(Integer.valueOf(ReadCode.ID), defaultPermissions.get(user01));
    Assert.assertEquals(Integer.valueOf(OwnerCode.ID), defaultPermissions.get(user03));
  }
}

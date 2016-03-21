package ch.ahoegger.docbox.server.test.document;

import java.io.IOException;
import java.util.Calendar;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.ahoegger.docbox.server.database.initialization.DocumentPermissionTableTask;
import ch.ahoegger.docbox.server.database.initialization.DocumentTableTask;
import ch.ahoegger.docbox.server.database.initialization.UserTableTask;
import ch.ahoegger.docbox.server.test.util.AbstractTestWithDatabase;
import ch.ahoegger.docbox.shared.document.DocumentSearchFormData;
import ch.ahoegger.docbox.shared.document.DocumentTableData;
import ch.ahoegger.docbox.shared.document.IDocumentPermissionTable;
import ch.ahoegger.docbox.shared.document.IDocumentService;

/**
 * <h3>{@link DocumentService_WithNoPermission}</h3>
 *
 * @author aho
 */
public class DocumentService_WithNoPermission extends AbstractTestWithDatabase {

  public static final String NO_ACCESS_USER_NAME = "noaccessuser";

  public static final String ONE_DOC_USER_NAME = "onedocusername";

  @BeforeClass
  public static void createTestRows() throws IOException {

    ISqlService sqlService = BEANS.get(ISqlService.class);

    BEANS.get(UserTableTask.class).createUser(sqlService, "admin", "manager", AbstractTestWithDatabase.SUBJECT_NAME, "secret", true, true);
    BEANS.get(UserTableTask.class).createUser(sqlService, "name", "firstname", NO_ACCESS_USER_NAME, "secret", true, false);
    BEANS.get(UserTableTask.class).createUser(sqlService, "name1", "firstname1", ONE_DOC_USER_NAME, "secret", true, false);

    Calendar cal = Calendar.getInstance();
    cal.set(1982, 04, 20);
    BEANS.get(DocumentTableTask.class).createDocumentRow(sqlService, 100, "Cats Document", cal.getTime(), cal.getTime(), cal.getTime(), "2016_03_08_124640.pdf", null, null);
    cal.set(1982, 04, 21);
    BEANS.get(DocumentTableTask.class).createDocumentRow(sqlService, 101, "Abstract Document", cal.getTime(), cal.getTime(), cal.getTime(), "2016_03_08_124640.pdf", null, null);
    cal.set(1982, 04, 22);
    BEANS.get(DocumentTableTask.class).createDocumentRow(sqlService, 102, "Dogs Document", cal.getTime(), cal.getTime(), cal.getTime(), "2016_03_08_124640.pdf", null, null);

    // permissions
    BEANS.get(DocumentPermissionTableTask.class).createDocumentPermissionRow(sqlService, ONE_DOC_USER_NAME, 102, IDocumentPermissionTable.PERMISSION_WRITE);
  }

  @Test
  public void testFindAllAdminPermission() {
    IDocumentService service = BEANS.get(IDocumentService.class);
    DocumentSearchFormData sd = new DocumentSearchFormData();
    DocumentTableData tableData = service.getTableData(sd);
    Assert.assertEquals(3, tableData.getRowCount());
  }

  @Test
  @RunWithSubject(DocumentService_WithNoPermission.NO_ACCESS_USER_NAME)
  public void testFindAllNoAccess() {
    IDocumentService service = BEANS.get(IDocumentService.class);
    DocumentSearchFormData sd = new DocumentSearchFormData();
    DocumentTableData tableData = service.getTableData(sd);
    Assert.assertEquals(0, tableData.getRowCount());
  }

  @Test
  @RunWithSubject(DocumentService_WithNoPermission.ONE_DOC_USER_NAME)
  public void testFindAllOneDocAccess() {
    IDocumentService service = BEANS.get(IDocumentService.class);
    DocumentSearchFormData sd = new DocumentSearchFormData();
    DocumentTableData tableData = service.getTableData(sd);
    Assert.assertEquals(1, tableData.getRowCount());
  }

}

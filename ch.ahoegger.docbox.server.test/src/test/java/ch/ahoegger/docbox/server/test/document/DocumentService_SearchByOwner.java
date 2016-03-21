package ch.ahoegger.docbox.server.test.document;

import java.io.IOException;
import java.util.Calendar;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
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
 * <h3>{@link DocumentService_SearchByOwner}</h3>
 *
 * @author aho
 */
public class DocumentService_SearchByOwner extends AbstractTestWithDatabase {

  @BeforeClass
  public static void createTestRows() throws IOException {

    ISqlService sqlService = BEANS.get(ISqlService.class);

    BEANS.get(UserTableTask.class).createUser(sqlService, "name", "firstname", "admin", "secret", true, true);
    BEANS.get(UserTableTask.class).createUser(sqlService, "name", "firstname", "user01", "secret", true, true);

    Calendar cal = Calendar.getInstance();
    DateUtility.truncCalendar(cal);
    cal.add(Calendar.DAY_OF_MONTH, -110);
    BEANS.get(DocumentTableTask.class).createDocumentRow(sqlService, 100, "doc 01", cal.getTime(), cal.getTime(), null, "2016_03_08_124640.pdf", null, null);
    cal = Calendar.getInstance();
    DateUtility.truncCalendar(cal);
    BEANS.get(DocumentTableTask.class).createDocumentRow(sqlService, 101, "doc 02", cal.getTime(), cal.getTime(), null, "2016_03_08_124640.pdf", null, null);
    cal = Calendar.getInstance();
    DateUtility.truncCalendar(cal);
    cal.add(Calendar.DAY_OF_WEEK, -1);
    BEANS.get(DocumentTableTask.class).createDocumentRow(sqlService, 102, "doc 03", cal.getTime(), cal.getTime(), null, "2016_03_08_124640.pdf", null, null);
    cal = Calendar.getInstance();
    DateUtility.truncCalendar(cal);
    cal.add(Calendar.DAY_OF_WEEK, -20);
    BEANS.get(DocumentTableTask.class).createDocumentRow(sqlService, 103, "doc 04", cal.getTime(), cal.getTime(), null, "2016_03_08_124640.pdf", null, null);
    BEANS.get(DocumentTableTask.class).createDocumentRow(sqlService, 104, "doc 05", cal.getTime(), cal.getTime(), null, "2016_03_08_124640.pdf", null, null);

    // permissions
    BEANS.get(DocumentPermissionTableTask.class).createDocumentPermissionRow(sqlService, "admin", 100, IDocumentPermissionTable.PERMISSION_OWNER);
    BEANS.get(DocumentPermissionTableTask.class).createDocumentPermissionRow(sqlService, "user01", 101, IDocumentPermissionTable.PERMISSION_OWNER);
    BEANS.get(DocumentPermissionTableTask.class).createDocumentPermissionRow(sqlService, "user01", 104, IDocumentPermissionTable.PERMISSION_OWNER);
  }

  @Test
  public void testFindByOwnerUser01() {
    IDocumentService service = BEANS.get(IDocumentService.class);
    DocumentSearchFormData sd = new DocumentSearchFormData();
    sd.getOwner().setValue("user01");
    DocumentTableData tableData = service.getTableData(sd);
    Assert.assertEquals(2, tableData.getRowCount());
    Assert.assertEquals(new Long(101), tableData.getRows()[0].getDocumentId());
    Assert.assertEquals(new Long(104), tableData.getRows()[1].getDocumentId());
  }

  @Test
  public void testFindByOwnerAdmin() {
    IDocumentService service = BEANS.get(IDocumentService.class);
    DocumentSearchFormData sd = new DocumentSearchFormData();
    sd.getOwner().setValue("admin");
    DocumentTableData tableData = service.getTableData(sd);
    Assert.assertEquals(1, tableData.getRowCount());
    Assert.assertEquals(new Long(100), tableData.getRows()[0].getDocumentId());
  }

  @Test
  public void testFindByOwnerNull() {
    IDocumentService service = BEANS.get(IDocumentService.class);
    DocumentSearchFormData sd = new DocumentSearchFormData();
    sd.getOwner().setValue(null);
    DocumentTableData tableData = service.getTableData(sd);
    Assert.assertEquals(5, tableData.getRowCount());
    Assert.assertEquals(new Long(100), tableData.getRows()[0].getDocumentId());
    Assert.assertEquals(new Long(101), tableData.getRows()[1].getDocumentId());
    Assert.assertEquals(new Long(102), tableData.getRows()[2].getDocumentId());
    Assert.assertEquals(new Long(103), tableData.getRows()[3].getDocumentId());
    Assert.assertEquals(new Long(104), tableData.getRows()[4].getDocumentId());
  }

}

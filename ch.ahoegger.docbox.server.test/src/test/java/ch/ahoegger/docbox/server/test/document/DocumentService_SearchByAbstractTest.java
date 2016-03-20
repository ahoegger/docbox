package ch.ahoegger.docbox.server.test.document;

import java.io.IOException;
import java.util.Calendar;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.ahoegger.docbox.server.database.initialization.DocumentTableTask;
import ch.ahoegger.docbox.server.database.initialization.UserTableTask;
import ch.ahoegger.docbox.server.test.util.AbstractTestWithDatabase;
import ch.ahoegger.docbox.shared.document.DocumentSearchFormData;
import ch.ahoegger.docbox.shared.document.DocumentTableData;
import ch.ahoegger.docbox.shared.document.IDocumentService;

/**
 * <h3>{@link DocumentService_SearchByAbstractTest}</h3>
 *
 * @author aho
 */
public class DocumentService_SearchByAbstractTest extends AbstractTestWithDatabase {

  @BeforeClass
  public static void createTestRows() throws IOException {

    ISqlService sqlService = BEANS.get(ISqlService.class);

    BEANS.get(UserTableTask.class).createUser(sqlService, "name", "firstname", "admin", "secret", true, true);

    Calendar cal = Calendar.getInstance();
    cal.set(1982, 04, 20);
    BEANS.get(DocumentTableTask.class).createDocumentRow(sqlService, 2000, "Cats Document", cal.getTime(), cal.getTime(), cal.getTime(), "2016_03_08_124640.pdf", null, null);
    cal.set(1982, 04, 21);
    BEANS.get(DocumentTableTask.class).createDocumentRow(sqlService, 2001, "Abstract Document", cal.getTime(), cal.getTime(), cal.getTime(), "2016_03_08_124640.pdf", null, null);
    cal.set(1982, 04, 22);
    BEANS.get(DocumentTableTask.class).createDocumentRow(sqlService, 2002, "Dogs Document", cal.getTime(), cal.getTime(), cal.getTime(), "2016_03_08_124640.pdf", null, null);
    cal.set(1982, 04, 23);
    BEANS.get(DocumentTableTask.class).createDocumentRow(sqlService, 2003, "All fish are wet", cal.getTime(), cal.getTime(), cal.getTime(), "2016_03_08_124640.pdf", null, null);
  }

  @Test
  public void testFind_cats() {
    IDocumentService service = BEANS.get(IDocumentService.class);
    DocumentSearchFormData sd = new DocumentSearchFormData();
    sd.getAbstract().setValue("cats");
    DocumentTableData tableData = service.getTableData(sd);
    Assert.assertEquals(1, tableData.getRowCount());
    Assert.assertEquals(new Long(2000), tableData.getRows()[0].getDocumentId());
  }

  @Test
  public void testFind_document() {
    IDocumentService service = BEANS.get(IDocumentService.class);
    DocumentSearchFormData sd = new DocumentSearchFormData();
    sd.getAbstract().setValue("document");
    DocumentTableData tableData = service.getTableData(sd);
    Assert.assertEquals(3, tableData.getRowCount());
    Assert.assertEquals(new Long(2000), tableData.getRows()[0].getDocumentId());
    Assert.assertEquals(new Long(2001), tableData.getRows()[1].getDocumentId());
    Assert.assertEquals(new Long(2002), tableData.getRows()[2].getDocumentId());
  }
}

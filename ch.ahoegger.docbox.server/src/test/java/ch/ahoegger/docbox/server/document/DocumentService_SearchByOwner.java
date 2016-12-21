package ch.ahoegger.docbox.server.document;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.stream.Collectors;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.junit.Assert;
import org.junit.Test;

import ch.ahoegger.docbox.server.database.dev.initialization.DocumentPermissionTableTask;
import ch.ahoegger.docbox.server.database.dev.initialization.DocumentTableTask;
import ch.ahoegger.docbox.server.database.dev.initialization.UserTableTask;
import ch.ahoegger.docbox.server.test.util.AbstractTestWithDatabase;
import ch.ahoegger.docbox.server.test.util.IdGenerateService;
import ch.ahoegger.docbox.shared.document.DocumentSearchFormData;
import ch.ahoegger.docbox.shared.document.DocumentTableData;
import ch.ahoegger.docbox.shared.document.IDocumentPermissionTable;
import ch.ahoegger.docbox.shared.document.IDocumentService;

/**
 * <h3>{@link DocumentService_SearchByOwner}</h3>
 *
 * @author Andreas Hoegger
 */
public class DocumentService_SearchByOwner extends AbstractTestWithDatabase {

  private static final String username01 = SUBJECT_NAME;
  private static final String username02 = "username02";

  private static final BigDecimal documentId01 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private static final BigDecimal documentId02 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private static final BigDecimal documentId03 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private static final BigDecimal documentId04 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private static final BigDecimal documentId05 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();

  @Override
  public void setupDb() throws Exception {
    super.setupDb();

    ISqlService sqlService = BEANS.get(ISqlService.class);

    BEANS.get(UserTableTask.class).insertUser(sqlService, "name01", "firstname01", username01, "secret", true, true);
    BEANS.get(UserTableTask.class).insertUser(sqlService, "name02", "firstname02", username02, "secret", true, true);

    Calendar cal = Calendar.getInstance();
    DateUtility.truncCalendar(cal);
    cal.add(Calendar.DAY_OF_MONTH, -110);
    BEANS.get(DocumentTableTask.class).createDocumentRow(sqlService, documentId01, "doc 01", cal.getTime(), cal.getTime(), null, "2016_03_08_124640.pdf", null, null, false);
    cal = Calendar.getInstance();
    DateUtility.truncCalendar(cal);
    BEANS.get(DocumentTableTask.class).createDocumentRow(sqlService, documentId02, "doc 02", cal.getTime(), cal.getTime(), null, "2016_03_08_124640.pdf", null, null, false);
    cal = Calendar.getInstance();
    DateUtility.truncCalendar(cal);
    cal.add(Calendar.DAY_OF_WEEK, -1);
    BEANS.get(DocumentTableTask.class).createDocumentRow(sqlService, documentId03, "doc 03", cal.getTime(), cal.getTime(), null, "2016_03_08_124640.pdf", null, null, false);
    cal = Calendar.getInstance();
    DateUtility.truncCalendar(cal);
    cal.add(Calendar.DAY_OF_WEEK, -20);
    BEANS.get(DocumentTableTask.class).createDocumentRow(sqlService, documentId04, "doc 04", cal.getTime(), cal.getTime(), null, "2016_03_08_124640.pdf", null, null, false);
    BEANS.get(DocumentTableTask.class).createDocumentRow(sqlService, documentId05, "doc 05", cal.getTime(), cal.getTime(), null, "2016_03_08_124640.pdf", null, null, false);

    // permissions
    BEANS.get(DocumentPermissionTableTask.class).createDocumentPermissionRow(sqlService, username01, documentId01, IDocumentPermissionTable.PERMISSION_OWNER);
    BEANS.get(DocumentPermissionTableTask.class).createDocumentPermissionRow(sqlService, username02, documentId02, IDocumentPermissionTable.PERMISSION_OWNER);
    BEANS.get(DocumentPermissionTableTask.class).createDocumentPermissionRow(sqlService, username02, documentId05, IDocumentPermissionTable.PERMISSION_OWNER);
  }

  @Test
  public void testFindByOwnerAdmin() {
    IDocumentService service = BEANS.get(IDocumentService.class);
    DocumentSearchFormData sd = new DocumentSearchFormData();
    sd.getOwner().setValue(username01);
    DocumentTableData tableData = service.getTableData(sd);

    Assert.assertEquals(CollectionUtility.arrayList(documentId01),
        Arrays.stream(tableData.getRows()).map(row -> row.getDocumentId())
            .sorted()
            .collect(Collectors.toList()));

  }

  @Test
  public void testFindByOwnerUser02() {
    IDocumentService service = BEANS.get(IDocumentService.class);
    DocumentSearchFormData sd = new DocumentSearchFormData();
    sd.getOwner().setValue(username02);
    DocumentTableData tableData = service.getTableData(sd);

    Assert.assertEquals(CollectionUtility.arrayList(documentId02, documentId05),
        Arrays.stream(tableData.getRows()).map(row -> row.getDocumentId())
            .sorted()
            .collect(Collectors.toList()));
  }

  @Test
  public void testFindByOwnerNull() {
    IDocumentService service = BEANS.get(IDocumentService.class);
    DocumentSearchFormData sd = new DocumentSearchFormData();
    sd.getOwner().setValue(null);
    DocumentTableData tableData = service.getTableData(sd);

    Assert.assertEquals(CollectionUtility.arrayList(documentId01, documentId02, documentId03, documentId04, documentId05),
        Arrays.stream(tableData.getRows()).map(row -> row.getDocumentId())
            .sorted()
            .collect(Collectors.toList()));

  }

}

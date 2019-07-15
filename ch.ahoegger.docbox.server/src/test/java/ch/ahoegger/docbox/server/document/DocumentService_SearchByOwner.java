package ch.ahoegger.docbox.server.document;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Arrays;
import java.util.Calendar;
import java.util.stream.Collectors;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.junit.Assert;
import org.junit.Test;

import ch.ahoegger.docbox.server.administration.user.UserService;
import ch.ahoegger.docbox.server.test.util.AbstractTestWithDatabase;
import ch.ahoegger.docbox.server.test.util.IdGenerateService;
import ch.ahoegger.docbox.server.test.util.TestDataGenerator;
import ch.ahoegger.docbox.shared.document.DocumentSearchFormData;
import ch.ahoegger.docbox.shared.document.DocumentTableData;
import ch.ahoegger.docbox.shared.document.IDocumentService;
import ch.ahoegger.docbox.shared.security.permission.PermissionCodeType;

/**
 * <h3>{@link DocumentService_SearchByOwner}</h3>
 *
 * @author Andreas Hoegger
 */
public class DocumentService_SearchByOwner extends AbstractTestWithDatabase {

//  private static final String username01 = SUBJECT_NAME;
  private static final String admin02 = "admin02";

  private static final BigDecimal documentId01 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private static final BigDecimal documentId02 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private static final BigDecimal documentId03 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private static final BigDecimal documentId04 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private static final BigDecimal documentId05 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();

  @Override
  protected void execSetupDb(Connection connection, TestDataGenerator testDataGenerator) throws Exception {

//    BEANS.get(UserService.class).insert(connection, "name01", "firstname01", username01, "secret", true, true);
    BEANS.get(UserService.class).insert(connection, "name02", "firstname02", admin02, "secret", true, true);

    Calendar cal = Calendar.getInstance();
    DateUtility.truncCalendar(cal);
    cal.add(Calendar.DAY_OF_MONTH, -110);
    BEANS.get(DocumentService.class).insert(connection, documentId01, "doc 01", cal.getTime(), cal.getTime(), null, "2016_03_08_124640.pdf", null, null, false, null);
    cal = Calendar.getInstance();
    DateUtility.truncCalendar(cal);
    BEANS.get(DocumentService.class).insert(connection, documentId02, "doc 02", cal.getTime(), cal.getTime(), null, "2016_03_08_124640.pdf", null, null, false, null);
    cal = Calendar.getInstance();
    DateUtility.truncCalendar(cal);
    cal.add(Calendar.DAY_OF_WEEK, -1);
    BEANS.get(DocumentService.class).insert(connection, documentId03, "doc 03", cal.getTime(), cal.getTime(), null, "2016_03_08_124640.pdf", null, null, false, null);
    cal = Calendar.getInstance();
    DateUtility.truncCalendar(cal);
    cal.add(Calendar.DAY_OF_WEEK, -20);
    BEANS.get(DocumentService.class).insert(connection, documentId04, "doc 04", cal.getTime(), cal.getTime(), null, "2016_03_08_124640.pdf", null, null, false, null);
    BEANS.get(DocumentService.class).insert(connection, documentId05, "doc 05", cal.getTime(), cal.getTime(), null, "2016_03_08_124640.pdf", null, null, false, null);

    // permissions
    BEANS.get(DocumentPermissionService.class).insert(connection, ADMIN, documentId01, PermissionCodeType.OwnerCode.ID);
    BEANS.get(DocumentPermissionService.class).insert(connection, admin02, documentId02, PermissionCodeType.OwnerCode.ID);
    BEANS.get(DocumentPermissionService.class).insert(connection, admin02, documentId05, PermissionCodeType.OwnerCode.ID);
  }

  @Test
  public void testFindByOwnerAdmin() {
    IDocumentService service = BEANS.get(IDocumentService.class);
    DocumentSearchFormData sd = new DocumentSearchFormData();
    sd.getOwner().setValue(ADMIN);
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
    sd.getOwner().setValue(admin02);
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

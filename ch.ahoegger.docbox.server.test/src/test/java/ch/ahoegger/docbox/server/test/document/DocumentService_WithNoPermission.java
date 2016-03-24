package ch.ahoegger.docbox.server.test.document;

import java.util.Arrays;
import java.util.Calendar;
import java.util.stream.Collectors;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.Assert;
import org.junit.Test;

import ch.ahoegger.docbox.server.database.initialization.DocumentPermissionTableTask;
import ch.ahoegger.docbox.server.database.initialization.DocumentTableTask;
import ch.ahoegger.docbox.server.database.initialization.UserTableTask;
import ch.ahoegger.docbox.server.test.util.AbstractTestWithDatabase;
import ch.ahoegger.docbox.server.test.util.IdGenerateService;
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

  private static final String username01 = SUBJECT_NAME;
  private static final String username02 = "username02";
  private static final String username03 = "username03";
  private static final String username04 = "username04";

  private static final Long documentId01 = BEANS.get(IdGenerateService.class).getNextId();
  private static final Long documentId02 = BEANS.get(IdGenerateService.class).getNextId();
  private static final Long documentId03 = BEANS.get(IdGenerateService.class).getNextId();

  @Override
  public void setupDb() throws Exception {
    super.setupDb();

    ISqlService sqlService = BEANS.get(ISqlService.class);

    BEANS.get(UserTableTask.class).createUser(sqlService, "admin", "manager", username01, "secret", true, true);
    BEANS.get(UserTableTask.class).createUser(sqlService, "name02", "firstname02", username02, "secret", true, false);
    BEANS.get(UserTableTask.class).createUser(sqlService, "name03", "firstname03", username03, "secret", true, false);
    BEANS.get(UserTableTask.class).createUser(sqlService, "name04", "firstname04", username04, "secret", true, false);

    Calendar cal = Calendar.getInstance();
    cal.set(1983, 04, 20);
    BEANS.get(DocumentTableTask.class).createDocumentRow(sqlService, documentId01, "Cats Document", cal.getTime(), cal.getTime(), cal.getTime(), "2016_03_08_124640.pdf", null, null);
    cal.set(1983, 04, 21);
    BEANS.get(DocumentTableTask.class).createDocumentRow(sqlService, documentId02, "Abstract Document", cal.getTime(), cal.getTime(), cal.getTime(), "2016_03_08_124640.pdf", null, null);
    cal.set(1983, 04, 22);
    BEANS.get(DocumentTableTask.class).createDocumentRow(sqlService, documentId03, "Dogs Document", cal.getTime(), cal.getTime(), cal.getTime(), "2016_03_08_124640.pdf", null, null);

    // permissions
    BEANS.get(DocumentPermissionTableTask.class).createDocumentPermissionRow(sqlService, username03, documentId03, IDocumentPermissionTable.PERMISSION_WRITE);
    BEANS.get(DocumentPermissionTableTask.class).createDocumentPermissionRow(sqlService, username04, documentId02, IDocumentPermissionTable.PERMISSION_READ);
    BEANS.get(DocumentPermissionTableTask.class).createDocumentPermissionRow(sqlService, username04, documentId03, IDocumentPermissionTable.PERMISSION_WRITE);
  }

  @Test
  public void testFindAllAdminPermission() {
    IDocumentService service = BEANS.get(IDocumentService.class);
    DocumentSearchFormData sd = new DocumentSearchFormData();
    DocumentTableData tableData = service.getTableData(sd);
    Assert.assertEquals(3, tableData.getRowCount());

    Assert.assertEquals(CollectionUtility.arrayList(documentId01, documentId02, documentId03),
        Arrays.stream(tableData.getRows()).map(row -> row.getDocumentId())
            .sorted()
            .collect(Collectors.toList()));
  }

  @Test
  @RunWithSubject(DocumentService_WithNoPermission.username02)
  public void testFindAllNoAccess() {
    IDocumentService service = BEANS.get(IDocumentService.class);
    DocumentSearchFormData sd = new DocumentSearchFormData();
    DocumentTableData tableData = service.getTableData(sd);

    Assert.assertEquals(CollectionUtility.arrayList(),
        Arrays.stream(tableData.getRows()).map(row -> row.getDocumentId())
            .sorted()
            .collect(Collectors.toList()));
  }

  @Test
  @RunWithSubject(DocumentService_WithNoPermission.username03)
  public void testFindAllOneDocAccess() {
    IDocumentService service = BEANS.get(IDocumentService.class);
    DocumentSearchFormData sd = new DocumentSearchFormData();
    DocumentTableData tableData = service.getTableData(sd);

    Assert.assertEquals(CollectionUtility.arrayList(documentId03),
        Arrays.stream(tableData.getRows()).map(row -> row.getDocumentId())
            .sorted()
            .collect(Collectors.toList()));
  }

  @Test
  @RunWithSubject(DocumentService_WithNoPermission.username04)
  public void testFindForUser04() {
    IDocumentService service = BEANS.get(IDocumentService.class);
    DocumentSearchFormData sd = new DocumentSearchFormData();
    DocumentTableData tableData = service.getTableData(sd);

    Assert.assertEquals(CollectionUtility.arrayList(documentId02, documentId03),
        Arrays.stream(tableData.getRows()).map(row -> row.getDocumentId())
            .sorted()
            .collect(Collectors.toList()));
  }

}

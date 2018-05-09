package ch.ahoegger.docbox.server.document;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Arrays;
import java.util.Calendar;
import java.util.stream.Collectors;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.Assert;
import org.junit.Test;

import ch.ahoegger.docbox.server.administration.user.UserService;
import ch.ahoegger.docbox.server.ocr.DocumentOcrService;
import ch.ahoegger.docbox.server.test.util.AbstractTestWithDatabase;
import ch.ahoegger.docbox.server.test.util.IdGenerateService;
import ch.ahoegger.docbox.shared.document.DocumentFormData;
import ch.ahoegger.docbox.shared.document.DocumentSearchFormData;
import ch.ahoegger.docbox.shared.document.DocumentTableData;
import ch.ahoegger.docbox.shared.document.IDocumentService;
import ch.ahoegger.docbox.shared.security.permission.PermissionCodeType;

/**
 * <h3>{@link DocumentService_WithNoPermission}</h3>
 *
 * @author Andreas Hoegger
 */
public class DocumentService_WithNoPermission extends AbstractTestWithDatabase {

  private static final String username02 = "username02";
  private static final String username03 = "username03";
  private static final String username04 = "username04";

  private static final BigDecimal documentId01 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private static final BigDecimal documentId02 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private static final BigDecimal documentId03 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();

  @Override
  protected void execSetupDb(Connection connection) throws Exception {

    BEANS.get(UserService.class).insert(connection, "name02", "firstname02", username02, "secret", true, false);
    BEANS.get(UserService.class).insert(connection, "name03", "firstname03", username03, "secret", true, false);
    BEANS.get(UserService.class).insert(connection, "name04", "firstname04", username04, "secret", true, false);

    Calendar cal = Calendar.getInstance();
    cal.set(1983, 04, 20);
    BEANS.get(DocumentService.class).insert(connection, documentId01, "Cats Document", cal.getTime(), cal.getTime(), cal.getTime(), "2016_03_08_124640.pdf", null, null, false, null);
    cal.set(1983, 04, 21);
    BEANS.get(DocumentService.class).insert(connection, documentId02, "Abstract Document", cal.getTime(), cal.getTime(), cal.getTime(), "2016_03_08_124640.pdf", null, null, false, null);
    cal.set(1983, 04, 22);
    BEANS.get(DocumentService.class).insert(connection, documentId03, "Dogs Document", cal.getTime(), cal.getTime(), cal.getTime(), "2016_03_08_124640.pdf", null, null, false, null);

    // ocr
    BEANS.get(DocumentOcrService.class).insert(connection, documentId02, "Abstract Document", true, 1, null);

    // permissions
    BEANS.get(DocumentPermissionService.class).insert(connection, username03, documentId03, PermissionCodeType.WriteCode.ID);
    BEANS.get(DocumentPermissionService.class).insert(connection, username04, documentId02, PermissionCodeType.ReadCode.ID);
    BEANS.get(DocumentPermissionService.class).insert(connection, username04, documentId03, PermissionCodeType.WriteCode.ID);
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
  @RunWithSubject(USER)
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

  @Test(expected = VetoException.class)
  @RunWithSubject(username04)
  public void testDeleteWithoutPermission() {
    IDocumentService service = BEANS.get(IDocumentService.class);
    try {
      service.delete(documentId02);

    }
    catch (Exception e) {
      DocumentFormData fd = new DocumentFormData();
      fd.setDocumentId(documentId02);
      Assert.assertNotNull(service.load(fd));

      Assert.assertTrue(BEANS.get(DocumentOcrService.class).exists(documentId02));
      throw e;
    }
    Assert.fail("Should en with exception");

  }

}

package ch.ahoegger.docbox.server.document;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.stream.Collectors;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.Assert;
import org.junit.Test;

import ch.ahoegger.docbox.server.administration.user.UserService;
import ch.ahoegger.docbox.server.test.util.AbstractTestWithDatabase;
import ch.ahoegger.docbox.server.test.util.IdGenerateService;
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

  private static final String username01 = SUBJECT_NAME;
  private static final String username02 = "username02";
  private static final String username03 = "username03";
  private static final String username04 = "username04";

  private static final BigDecimal documentId01 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private static final BigDecimal documentId02 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private static final BigDecimal documentId03 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();

  @Override
  public void setupDb() throws Exception {
    super.setupDb();

    ISqlService sqlService = BEANS.get(ISqlService.class);

    BEANS.get(UserService.class).insert(sqlService.getConnection(), "admin", "manager", username01, "secret", true, true);
    BEANS.get(UserService.class).insert(sqlService.getConnection(), "name02", "firstname02", username02, "secret", true, false);
    BEANS.get(UserService.class).insert(sqlService.getConnection(), "name03", "firstname03", username03, "secret", true, false);
    BEANS.get(UserService.class).insert(sqlService.getConnection(), "name04", "firstname04", username04, "secret", true, false);

    Calendar cal = Calendar.getInstance();
    cal.set(1983, 04, 20);
    BEANS.get(DocumentService.class).insert(sqlService.getConnection(), documentId01, "Cats Document", cal.getTime(), cal.getTime(), cal.getTime(), "2016_03_08_124640.pdf", null, null, false, null);
    cal.set(1983, 04, 21);
    BEANS.get(DocumentService.class).insert(sqlService.getConnection(), documentId02, "Abstract Document", cal.getTime(), cal.getTime(), cal.getTime(), "2016_03_08_124640.pdf", null, null, false, null);
    cal.set(1983, 04, 22);
    BEANS.get(DocumentService.class).insert(sqlService.getConnection(), documentId03, "Dogs Document", cal.getTime(), cal.getTime(), cal.getTime(), "2016_03_08_124640.pdf", null, null, false, null);

    // permissions
    BEANS.get(DocumentPermissionService.class).insert(sqlService.getConnection(), username03, documentId03, PermissionCodeType.WriteCode.ID);
    BEANS.get(DocumentPermissionService.class).insert(sqlService.getConnection(), username04, documentId02, PermissionCodeType.ReadCode.ID);
    BEANS.get(DocumentPermissionService.class).insert(sqlService.getConnection(), username04, documentId03, PermissionCodeType.WriteCode.ID);
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

package ch.ahoegger.docbox.server.document;

import java.util.Arrays;
import java.util.Calendar;
import java.util.stream.Collectors;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.junit.Assert;
import org.junit.Test;

import ch.ahoegger.docbox.server.database.dev.initialization.DocumentTableTask;
import ch.ahoegger.docbox.server.database.dev.initialization.UserTableTask;
import ch.ahoegger.docbox.server.test.util.AbstractTestWithDatabase;
import ch.ahoegger.docbox.server.test.util.IdGenerateService;
import ch.ahoegger.docbox.shared.document.DocumentSearchFormData;
import ch.ahoegger.docbox.shared.document.DocumentTableData;
import ch.ahoegger.docbox.shared.document.IDocumentService;

/**
 * <h3>{@link DocumentService_SearchByAbstractTest}</h3>
 *
 * @author aho
 */
public class DocumentService_SearchByAbstractTest extends AbstractTestWithDatabase {

  private static final String username01 = SUBJECT_NAME;

  private static final Long documentId01 = BEANS.get(IdGenerateService.class).getNextId();
  private static final Long documentId02 = BEANS.get(IdGenerateService.class).getNextId();
  private static final Long documentId03 = BEANS.get(IdGenerateService.class).getNextId();
  private static final Long documentId04 = BEANS.get(IdGenerateService.class).getNextId();

  @Override
  public void setupDb() throws Exception {
    super.setupDb();

    ISqlService sqlService = BEANS.get(ISqlService.class);

    BEANS.get(UserTableTask.class).insertUser(sqlService, "name", "firstname", username01, "secret", true, true);

    Calendar cal = Calendar.getInstance();
    cal.set(1982, 04, 20);
    BEANS.get(DocumentTableTask.class).createDocumentRow(sqlService, documentId01, "Cats Document", cal.getTime(), cal.getTime(), cal.getTime(), "2016_03_08_124640.pdf", null, null, false);
    cal.set(1982, 04, 21);
    BEANS.get(DocumentTableTask.class).createDocumentRow(sqlService, documentId02, "Abstract Document", cal.getTime(), cal.getTime(), cal.getTime(), "2016_03_08_124640.pdf", null, null, false);
    cal.set(1982, 04, 22);
    BEANS.get(DocumentTableTask.class).createDocumentRow(sqlService, documentId03, "Dogs Document", cal.getTime(), cal.getTime(), cal.getTime(), "2016_03_08_124640.pdf", null, null, false);
    cal.set(1982, 04, 23);
    BEANS.get(DocumentTableTask.class).createDocumentRow(sqlService, documentId04, "All fish are wet", cal.getTime(), cal.getTime(), cal.getTime(), "2016_03_08_124640.pdf", null, null, false);
  }

  @Test
  public void testFind_cats() {
    IDocumentService service = BEANS.get(IDocumentService.class);
    DocumentSearchFormData sd = new DocumentSearchFormData();
    sd.getAbstract().setValue("cats");
    DocumentTableData tableData = service.getTableData(sd);

    Assert.assertEquals(CollectionUtility.arrayList(documentId01),
        Arrays.stream(tableData.getRows()).map(row -> row.getDocumentId())
            .sorted()
            .collect(Collectors.toList()));
  }

  @Test
  public void testFind_document() {
    IDocumentService service = BEANS.get(IDocumentService.class);
    DocumentSearchFormData sd = new DocumentSearchFormData();
    sd.getAbstract().setValue("document");
    DocumentTableData tableData = service.getTableData(sd);

    Assert.assertEquals(CollectionUtility.arrayList(documentId01, documentId02, documentId03),
        Arrays.stream(tableData.getRows()).map(row -> row.getDocumentId())
            .sorted()
            .collect(Collectors.toList()));
  }

}

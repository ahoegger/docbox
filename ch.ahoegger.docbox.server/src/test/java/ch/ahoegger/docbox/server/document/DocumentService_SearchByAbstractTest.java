package ch.ahoegger.docbox.server.document;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Arrays;
import java.util.Calendar;
import java.util.stream.Collectors;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.junit.Assert;
import org.junit.Test;

import ch.ahoegger.docbox.server.test.util.AbstractTestWithDatabase;
import ch.ahoegger.docbox.server.test.util.IdGenerateService;
import ch.ahoegger.docbox.shared.document.DocumentSearchFormData;
import ch.ahoegger.docbox.shared.document.DocumentTableData;
import ch.ahoegger.docbox.shared.document.IDocumentService;

/**
 * <h3>{@link DocumentService_SearchByAbstractTest}</h3>
 *
 * @author Andreas Hoegger
 */
public class DocumentService_SearchByAbstractTest extends AbstractTestWithDatabase {

  private static final BigDecimal documentId01 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private static final BigDecimal documentId02 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private static final BigDecimal documentId03 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private static final BigDecimal documentId04 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();

  @Override
  protected void execSetupDb(Connection connection) throws Exception {

    Calendar cal = Calendar.getInstance();
    cal.set(1982, 04, 20);
    BEANS.get(DocumentService.class).insert(connection, documentId01, "Cats Document", cal.getTime(), cal.getTime(), cal.getTime(), "2016_03_08_124640.pdf", null, null, false, null);
    cal.set(1982, 04, 21);
    BEANS.get(DocumentService.class).insert(connection, documentId02, "Abstract Document", cal.getTime(), cal.getTime(), cal.getTime(), "2016_03_08_124640.pdf", null, null, false, null);
    cal.set(1982, 04, 22);
    BEANS.get(DocumentService.class).insert(connection, documentId03, "Dogs Document", cal.getTime(), cal.getTime(), cal.getTime(), "2016_03_08_124640.pdf", null, null, false, null);
    cal.set(1982, 04, 23);
    BEANS.get(DocumentService.class).insert(connection, documentId04, "All fish are wet", cal.getTime(), cal.getTime(), cal.getTime(), "2016_03_08_124640.pdf", null, null, false, null);
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

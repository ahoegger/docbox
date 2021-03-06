package ch.ahoegger.docbox.server.document;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Arrays;
import java.util.Calendar;
import java.util.stream.Collectors;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.platform.util.TriState;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.junit.Assert;
import org.junit.Test;

import ch.ahoegger.docbox.server.test.util.AbstractTestWithDatabase;
import ch.ahoegger.docbox.server.test.util.IdGenerateService;
import ch.ahoegger.docbox.server.test.util.TestDataGenerator;
import ch.ahoegger.docbox.shared.document.DocumentSearchFormData;
import ch.ahoegger.docbox.shared.document.DocumentTableData;
import ch.ahoegger.docbox.shared.document.IDocumentService;

/**
 * <h3>{@link DocumentService_SearchByActiveState}</h3>
 *
 * @author Andreas Hoegger
 */
public class DocumentService_SearchByActiveState extends AbstractTestWithDatabase {

  private static final BigDecimal documentId01 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private static final BigDecimal documentId02 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private static final BigDecimal documentId03 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private static final BigDecimal documentId04 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private static final BigDecimal documentId05 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();

  @Override
  protected void execSetupDb(Connection connection, TestDataGenerator testDataGenerator) throws Exception {

    Calendar cal = Calendar.getInstance();
    DateUtility.truncCalendar(cal);
    cal.add(Calendar.DAY_OF_MONTH, +1);
    BEANS.get(DocumentService.class).insert(connection, documentId01, "doc 01", cal.getTime(), cal.getTime(), cal.getTime(), "2016_03_08_124640.pdf", null, null, false, null);
    cal = Calendar.getInstance();
    DateUtility.truncCalendar(cal);
    BEANS.get(DocumentService.class).insert(connection, documentId02, "doc 02", cal.getTime(), cal.getTime(), cal.getTime(), "2016_03_08_124640.pdf", null, null, false, null);
    cal = Calendar.getInstance();
    DateUtility.truncCalendar(cal);
    cal.add(Calendar.DAY_OF_WEEK, 1);
    BEANS.get(DocumentService.class).insert(connection, documentId03, "doc 03", cal.getTime(), cal.getTime(), cal.getTime(), "2016_03_08_124640.pdf", null, null, false, null);
    cal = Calendar.getInstance();
    DateUtility.truncCalendar(cal);
    cal.add(Calendar.DAY_OF_WEEK, -1);
    BEANS.get(DocumentService.class).insert(connection, documentId04, "doc 04", cal.getTime(), cal.getTime(), cal.getTime(), "2016_03_08_124640.pdf", null, null, false, null);
    BEANS.get(DocumentService.class).insert(connection, documentId05, "doc 05", cal.getTime(), cal.getTime(), null, "2016_03_08_124640.pdf", null, null, false, null);
  }

  @Test
  public void testFindActive() {
    IDocumentService service = BEANS.get(IDocumentService.class);
    DocumentSearchFormData sd = new DocumentSearchFormData();
    sd.getActiveBox().setValue(TriState.TRUE);
    DocumentTableData tableData = service.getTableData(sd);

    Assert.assertEquals(CollectionUtility.arrayList(documentId01, documentId02, documentId03, documentId05),
        Arrays.stream(tableData.getRows()).map(row -> row.getDocumentId())
            .sorted()
            .collect(Collectors.toList()));
  }

  @Test
  public void testFindInactive() {
    IDocumentService service = BEANS.get(IDocumentService.class);
    DocumentSearchFormData sd = new DocumentSearchFormData();
    sd.getActiveBox().setValue(TriState.FALSE);
    DocumentTableData tableData = service.getTableData(sd);

    Assert.assertEquals(CollectionUtility.arrayList(documentId04),
        Arrays.stream(tableData.getRows()).map(row -> row.getDocumentId())
            .sorted()
            .collect(Collectors.toList()));
  }

  @Test
  public void testFindAll() {
    IDocumentService service = BEANS.get(IDocumentService.class);
    DocumentSearchFormData sd = new DocumentSearchFormData();
    sd.getActiveBox().setValue(TriState.UNDEFINED);
    DocumentTableData tableData = service.getTableData(sd);

    Assert.assertEquals(CollectionUtility.arrayList(documentId01, documentId02, documentId03, documentId04, documentId05),
        Arrays.stream(tableData.getRows()).map(row -> row.getDocumentId())
            .sorted()
            .collect(Collectors.toList()));
  }

}

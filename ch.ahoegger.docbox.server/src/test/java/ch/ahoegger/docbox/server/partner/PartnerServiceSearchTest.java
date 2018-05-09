package ch.ahoegger.docbox.server.partner;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.junit.Assert;
import org.junit.Test;

import ch.ahoegger.docbox.server.document.DocumentPartnerService;
import ch.ahoegger.docbox.server.test.util.AbstractTestWithDatabase;
import ch.ahoegger.docbox.server.test.util.IdGenerateService;
import ch.ahoegger.docbox.shared.partner.IPartnerService;
import ch.ahoegger.docbox.shared.partner.PartnerSearchFormData;
import ch.ahoegger.docbox.shared.util.LocalDateUtility;

/**
 * <h3>{@link PartnerServiceSearchTest}</h3>
 *
 * @author aho
 */
public class PartnerServiceSearchTest extends AbstractTestWithDatabase {

  private static final BigDecimal partnerId01 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private static final BigDecimal partnerId02 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private static final BigDecimal partnerId03 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();

  private static final BigDecimal documentId01 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private static final BigDecimal documentId02 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private static final BigDecimal documentId03 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();

  @Override
  protected void execSetupDb(Connection connection) throws Exception {
    LocalDate today = LocalDate.now();

    BEANS.get(PartnerService.class).insert(connection, partnerId01, "partner01", "some notes",
        LocalDateUtility.toDate(today.minusDays(20)),
        null);

    // till yesterday
    BEANS.get(PartnerService.class).insert(connection, partnerId02, "partner02", "some notes",
        LocalDateUtility.toDate(today.minusDays(10)),
        LocalDateUtility.toDate(today.minusDays(1)));

    // till today
    BEANS.get(PartnerService.class).insert(connection, partnerId03, "partner03", "some notes",
        LocalDateUtility.toDate(today.minusDays(10)),
        LocalDateUtility.toDate(today));

    BEANS.get(DocumentPartnerService.class).insert(connection, documentId01, partnerId01);
    BEANS.get(DocumentPartnerService.class).insert(connection, documentId01, partnerId02);
    BEANS.get(DocumentPartnerService.class).insert(connection, documentId02, partnerId03);
  }

  @Test
  public void testFindByDocumentId1() {
    PartnerSearchFormData fd = new PartnerSearchFormData();
    fd.setDocumentId(documentId01);
    Assert.assertEquals(CollectionUtility.arrayList(partnerId01, partnerId02),
        Arrays.stream(BEANS.get(IPartnerService.class).getTableData(fd).getRows())
            .map(row -> row.getPartnerId())
            .sorted()
            .collect(Collectors.toList()));
  }

  @Test
  public void testFindByDocumentId2() {
    PartnerSearchFormData fd = new PartnerSearchFormData();
    fd.setDocumentId(documentId02);
    Assert.assertEquals(CollectionUtility.arrayList(partnerId03),
        Arrays.stream(BEANS.get(IPartnerService.class).getTableData(fd).getRows())
            .map(row -> row.getPartnerId())
            .sorted()
            .collect(Collectors.toList()));
  }

  @Test
  public void testFindByDocumentId3() {
    PartnerSearchFormData fd = new PartnerSearchFormData();
    fd.setDocumentId(documentId03);
    Assert.assertEquals(CollectionUtility.arrayList(),
        Arrays.stream(BEANS.get(IPartnerService.class).getTableData(fd).getRows())
            .map(row -> row.getPartnerId())
            .sorted()
            .collect(Collectors.toList()));
  }

}

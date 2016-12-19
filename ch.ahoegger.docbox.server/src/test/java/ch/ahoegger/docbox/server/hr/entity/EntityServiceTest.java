package ch.ahoegger.docbox.server.hr.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.junit.Assert;
import org.junit.Test;

import ch.ahoegger.docbox.server.database.dev.initialization.DocumentTableTask;
import ch.ahoegger.docbox.server.database.dev.initialization.EntityTableTask;
import ch.ahoegger.docbox.server.database.dev.initialization.PartnerTableTask;
import ch.ahoegger.docbox.server.database.dev.initialization.PostingGroupTableTask;
import ch.ahoegger.docbox.server.test.util.AbstractTestWithDatabase;
import ch.ahoegger.docbox.server.test.util.IdGenerateService;
import ch.ahoegger.docbox.shared.hr.entity.EntitySearchFormData;
import ch.ahoegger.docbox.shared.hr.entity.EntityTablePageData;
import ch.ahoegger.docbox.shared.hr.entity.EntityTypeCodeType;
import ch.ahoegger.docbox.shared.hr.entity.IEntityService;
import ch.ahoegger.docbox.shared.util.LocalDateUtility;

public class EntityServiceTest extends AbstractTestWithDatabase {

  private Long partnerId01 = BEANS.get(IdGenerateService.class).getNextId();
  private Long documentId01 = BEANS.get(IdGenerateService.class).getNextId();
  private Long postingGroupId01 = BEANS.get(IdGenerateService.class).getNextId();
  private Long entityId01 = BEANS.get(IdGenerateService.class).getNextId();
  private Long entityId02 = BEANS.get(IdGenerateService.class).getNextId();

  @Override
  public void setupDb() throws Exception {
    super.setupDb();

    ISqlService sqlService = BEANS.get(ISqlService.class);
    BEANS.get(PartnerTableTask.class).createPartnerRow(sqlService, partnerId01, "patnerName01", "desc01", LocalDateUtility.today(), null);

    BEANS.get(DocumentTableTask.class).createDocumentRow(sqlService, documentId01, "All fish are wet", LocalDateUtility.toDate(LocalDate.now().minusDays(3)), LocalDateUtility.today(), null, "2016_03_08_124640.pdf", null, null, false);

    BEANS.get(PostingGroupTableTask.class).createRow(sqlService, postingGroupId01, partnerId01, documentId01, "August 2016", LocalDateUtility.today(), BigDecimal.valueOf(234.9), BigDecimal.valueOf(232.1), BigDecimal.valueOf(-10.0),
        BigDecimal.valueOf(-4.5), BigDecimal.valueOf(5.30));

    BEANS.get(EntityTableTask.class).createEntityRow(sqlService, entityId01, partnerId01, postingGroupId01, EntityTypeCodeType.WorkCode.ID, LocalDateUtility.today(), BigDecimal.valueOf(3.25), null, "Work01", null);

    BEANS.get(EntityTableTask.class).createEntityRow(sqlService, entityId02, partnerId01, null, EntityTypeCodeType.WorkCode.ID, LocalDateUtility.today(), BigDecimal.valueOf(3.25), null, "Work01", null);
  }

  @Test
  public void testUnbiled() {
    EntitySearchFormData searchFd = new EntitySearchFormData();
    searchFd.getPartnerId().setValue(BigDecimal.valueOf(partnerId01));
    searchFd.setPostingGroupId(null);
    EntityTablePageData entityTableData = BEANS.get(IEntityService.class).getEntityTableData(searchFd);
    Assert.assertEquals(1, entityTableData.getRowCount());
  }

  // TODO [aho] add test cases
}

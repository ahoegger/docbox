package ch.ahoegger.docbox.server.hr.billing;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.junit.Assert;
import org.junit.Test;

import ch.ahoegger.docbox.server.hr.entity.EntityService;
import ch.ahoegger.docbox.server.test.util.AbstractTestWithDatabase;
import ch.ahoegger.docbox.server.test.util.IdGenerateService;
import ch.ahoegger.docbox.shared.hr.billing.PostingGroupCodeType.UnbilledCode;
import ch.ahoegger.docbox.shared.hr.entity.EntityTablePageData.EntityTableRowData;
import ch.ahoegger.docbox.shared.hr.entity.EntityTypeCodeType.WorkCode;
import ch.ahoegger.docbox.shared.util.LocalDateUtility;

/**
 * <h3>{@link PostingGroupServiceTest_FindUnbilld}</h3>
 *
 * @author aho
 */
public class PostingGroupServiceTest_FindUnbilld extends AbstractTestWithDatabase {

  private BigDecimal entityId01 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private BigDecimal entityId02 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private BigDecimal entityId03 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private BigDecimal entityId04 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private BigDecimal entityId05 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();

  @Override
  protected void execSetupDb(Connection connection) throws Exception {
    // create entities
    BEANS.get(EntityService.class).insert(connection, entityId01, BigDecimal.valueOf(5), UnbilledCode.ID, WorkCode.ID, LocalDateUtility.toDate(LocalDate.of(2016, 3, 31)), BigDecimal.valueOf(2), null, "desc");
    BEANS.get(EntityService.class).insert(connection, entityId02, BigDecimal.valueOf(5), UnbilledCode.ID, WorkCode.ID, LocalDateUtility.toDate(LocalDate.of(2016, 4, 1)), BigDecimal.valueOf(2), null, "desc");
    BEANS.get(EntityService.class).insert(connection, entityId03, BigDecimal.valueOf(5), UnbilledCode.ID, WorkCode.ID, LocalDateUtility.toDate(LocalDate.of(2016, 4, 13)), BigDecimal.valueOf(2), null, "desc");
    BEANS.get(EntityService.class).insert(connection, entityId04, BigDecimal.valueOf(5), UnbilledCode.ID, WorkCode.ID, LocalDateUtility.toDate(LocalDate.of(2016, 4, 30)), BigDecimal.valueOf(2), null, "desc");
    BEANS.get(EntityService.class).insert(connection, entityId05, BigDecimal.valueOf(5), UnbilledCode.ID, WorkCode.ID, LocalDateUtility.toDate(LocalDate.of(2016, 5, 1)), BigDecimal.valueOf(2), null, "desc");

  }

  @Test
  public void testSearchApril() {
    List<EntityTableRowData> unbilledEntities = BEANS.get(PostingGroupService.class).getUnbilledEntities(LocalDateUtility.toDate(LocalDate.of(2016, 4, 1)), LocalDateUtility.toDate(LocalDate.of(2016, 4, 30)), BigDecimal.valueOf(5));

    Assert.assertEquals(CollectionUtility.arrayList(entityId02, entityId03, entityId04),
        unbilledEntities
            .stream().map(e -> e.getEnityId())
            .sorted()
            .collect(Collectors.toList()));

  }
}

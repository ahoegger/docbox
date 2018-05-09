package ch.ahoegger.docbox.server.hr.billing;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDate;

import org.eclipse.scout.rt.platform.BEANS;
import org.junit.Assert;
import org.junit.Test;

import ch.ahoegger.docbox.server.hr.employee.EmployeeService;
import ch.ahoegger.docbox.server.hr.entity.EntityService;
import ch.ahoegger.docbox.server.hr.tax.TaxGroupService;
import ch.ahoegger.docbox.server.partner.PartnerService;
import ch.ahoegger.docbox.server.test.util.AbstractTestWithDatabase;
import ch.ahoegger.docbox.server.test.util.IdGenerateService;
import ch.ahoegger.docbox.shared.hr.billing.PostingGroupCodeType.UnbilledCode;
import ch.ahoegger.docbox.shared.hr.billing.PostingGroupFormData;
import ch.ahoegger.docbox.shared.hr.entity.EntityTypeCodeType.WorkCode;
import ch.ahoegger.docbox.shared.util.LocalDateUtility;

/**
 * <h3>{@link PostingGroupServiceTest_PrepareCreate}</h3>
 *
 * @author aho
 */
public class PostingGroupServiceTest_PrepareCreate extends AbstractTestWithDatabase {

  private static LocalDate today = LocalDate.now();

  private BigDecimal partnerId01 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();

  private BigDecimal taxGroupId01 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private BigDecimal taxGroupId02 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private BigDecimal taxGroupId03 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();

  private BigDecimal entityId01 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private BigDecimal entityId02 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private BigDecimal entityId03 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private BigDecimal entityId04 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private BigDecimal entityId05 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();

  @Override
  protected void execSetupDb(Connection connection) throws Exception {
    // partner
    BEANS.get(PartnerService.class).insert(connection, partnerId01, "Homer Simpson", null, LocalDateUtility.toDate(today.minusDays(20)), null);

    // employee
    BEANS.get(EmployeeService.class).insert(connection, partnerId01, "Homer", "Simpson", "Nashvill Street 12a", "Santa Barbara CA-90051", "ahv123564789", "iban987654321",
        LocalDateUtility.toDate(LocalDate.of(1972, 12, 31)), BigDecimal.valueOf(26.30),
        BigDecimal.valueOf(6.225), BigDecimal.valueOf(5.0), BigDecimal.valueOf(8.33),
        "Master Bob & Minor Molar", "Mountainview 12", "CA-90153 Santa Tropee", "master.bob@blu.com", "5445621236");

    // create Tax groups
    BEANS.get(TaxGroupService.class).insert(connection, taxGroupId01, "", LocalDateUtility.toDate(today.minusYears(1).withDayOfYear(1)),
        LocalDateUtility.toDate(today.minusYears(1).withDayOfYear(today.minusYears(1).lengthOfYear())));
    BEANS.get(TaxGroupService.class).insert(connection, taxGroupId02, "", LocalDateUtility.toDate(today.withDayOfYear(1)),
        LocalDateUtility.toDate(today.withDayOfYear(today.lengthOfYear())));
    BEANS.get(TaxGroupService.class).insert(connection, taxGroupId03, "", LocalDateUtility.toDate(today.plusYears(1).withDayOfYear(1)),
        LocalDateUtility.toDate(today.plusYears(1).withDayOfYear(today.plusYears(1).lengthOfYear())));

    // create entities
    BEANS.get(EntityService.class).insert(connection, entityId01, partnerId01, UnbilledCode.ID, WorkCode.ID, LocalDateUtility.toDate(LocalDate.of(today.getYear(), 3, 31)), BigDecimal.valueOf(2), null, "desc");
    BEANS.get(EntityService.class).insert(connection, entityId02, partnerId01, UnbilledCode.ID, WorkCode.ID, LocalDateUtility.toDate(LocalDate.of(today.getYear(), 4, 1)), BigDecimal.valueOf(2), null, "desc");
    BEANS.get(EntityService.class).insert(connection, entityId03, partnerId01, UnbilledCode.ID, WorkCode.ID, LocalDateUtility.toDate(LocalDate.of(today.getYear(), 4, 13)), BigDecimal.valueOf(2), null, "desc");
    BEANS.get(EntityService.class).insert(connection, entityId04, partnerId01, UnbilledCode.ID, WorkCode.ID, LocalDateUtility.toDate(LocalDate.of(today.getYear(), 4, 30)), BigDecimal.valueOf(2), null, "desc");
    BEANS.get(EntityService.class).insert(connection, entityId05, partnerId01, UnbilledCode.ID, WorkCode.ID, LocalDateUtility.toDate(LocalDate.of(today.getYear(), 5, 1)), BigDecimal.valueOf(2), null, "desc");

  }

  @Test
  public void testPrepareCreate() {
    PostingGroupFormData data = new PostingGroupFormData();
    data.getFrom().setValue(LocalDateUtility.toDate(LocalDate.of(today.getYear(), 4, 1)));
    data.getTo().setValue(LocalDateUtility.toDate(LocalDate.of(today.getYear(), 4, 30)));
    data.getPartner().setValue(partnerId01);
    data = BEANS.get(PostingGroupService.class).prepareCreate(data);
    Assert.assertEquals(3, data.getPostingCalculationBox().getEntities().getRowCount());
    Assert.assertEquals(taxGroupId02, data.getTaxGroup().getValue());
  }
}

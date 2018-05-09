package ch.ahoegger.docbox.server.hr.tax;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDate;

import org.eclipse.scout.rt.platform.BEANS;
import org.junit.Test;

import ch.ahoegger.docbox.server.test.util.AbstractTestWithDatabase;
import ch.ahoegger.docbox.server.test.util.DocboxAssert;
import ch.ahoegger.docbox.server.test.util.IdGenerateService;
import ch.ahoegger.docbox.shared.hr.tax.ITaxGroupService;
import ch.ahoegger.docbox.shared.hr.tax.TaxGroupFormData;
import ch.ahoegger.docbox.shared.util.LocalDateUtility;

public class TaxGroupServiceTest extends AbstractTestWithDatabase {

  private BigDecimal taxGroupId = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();

  @Override
  protected void execSetupDb(Connection connection) throws Exception {

    BEANS.get(TaxGroupService.class).createRow(connection, taxGroupId, "abc", LocalDateUtility.today(), null);

  }

  @Test
  public void testCreate() {
    TaxGroupFormData fd = new TaxGroupFormData();
    fd.getName().setValue("2000");
    fd.getStartDate().setValue(LocalDateUtility.today());
    fd = BEANS.get(ITaxGroupService.class).create(fd);

    TaxGroupFormData fd2 = new TaxGroupFormData();
    fd2.setTaxGroupId(fd.getTaxGroupId());
    fd2 = BEANS.get(ITaxGroupService.class).load(fd2);
    DocboxAssert.assertEquals(fd, fd2);

  }

  @Test
  public void testModify() {
    TaxGroupFormData fd = new TaxGroupFormData();
    fd.setTaxGroupId(taxGroupId);
    fd = BEANS.get(ITaxGroupService.class).load(fd);
    fd.getName().setValue("def");
    fd.getEndDate().setValue(LocalDateUtility.toDate(LocalDate.now().plusDays(2)));
    BEANS.get(ITaxGroupService.class).store(fd);

    TaxGroupFormData fd2 = new TaxGroupFormData();
    fd2.setTaxGroupId(taxGroupId);
    fd2 = BEANS.get(ITaxGroupService.class).load(fd2);
    DocboxAssert.assertEquals(fd, fd2);
  }
}

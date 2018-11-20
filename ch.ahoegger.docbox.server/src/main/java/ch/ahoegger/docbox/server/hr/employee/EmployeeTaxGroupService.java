package ch.ahoegger.docbox.server.hr.employee;

import java.math.BigDecimal;

import org.ch.ahoegger.docbox.server.or.app.tables.EmployeeTaxGroup;
import org.ch.ahoegger.docbox.server.or.app.tables.records.EmployeeTaxGroupRecord;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.server.jdbc.SQL;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import ch.ahoegger.docbox.shared.backup.IBackupService;
import ch.ahoegger.docbox.shared.hr.employee.IEmployeeTaxGroupService;
import ch.ahoegger.docbox.shared.hr.tax.EmployeeTaxGroupFormData;

/**
 * <h3>{@link EmployeeTaxGroupService}</h3>
 *
 * @author aho
 */
public class EmployeeTaxGroupService implements IEmployeeTaxGroupService {

  @Override
  public EmployeeTaxGroupFormData create(EmployeeTaxGroupFormData formData) {
    if (formData.getPartnerId() == null) {
      throw new VetoException("Partner id can not be null.");
    }

    EmployeeTaxGroup e = EmployeeTaxGroup.EMPLOYEE_TAX_GROUP.as("ETG");
    int rowCount = mapToRecord(DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .newRecord(e), formData).insert();

    if (rowCount == 1) {
      // notify backup needed
      BEANS.get(IBackupService.class).notifyModification();
      return formData;
    }
    return null;
  }

  protected EmployeeTaxGroupRecord mapToRecord(EmployeeTaxGroupRecord rec, EmployeeTaxGroupFormData fd) {
    if (fd == null) {
      return null;
    }
    return mapToRecord(rec, fd.getPartnerId(), fd.getTaxGroup().getValue());

  }

  protected EmployeeTaxGroupRecord mapToRecord(EmployeeTaxGroupRecord rec, BigDecimal partnerId, BigDecimal taxGroupId) {
    EmployeeTaxGroup e = EmployeeTaxGroup.EMPLOYEE_TAX_GROUP;
    return rec
        .with(e.PARTNER_NR, partnerId)
        .with(e.TAX_GROUP_NR, taxGroupId);
  }
}

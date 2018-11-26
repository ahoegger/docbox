package ch.ahoegger.docbox.server.hr.employer;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.ch.ahoegger.docbox.server.or.app.tables.Employer;
import org.ch.ahoegger.docbox.server.or.app.tables.records.EmployerRecord;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.Assertions;
import org.eclipse.scout.rt.server.jdbc.SQL;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import ch.ahoegger.docbox.or.definition.table.ISequenceTable;
import ch.ahoegger.docbox.shared.backup.IBackupService;
import ch.ahoegger.docbox.shared.hr.IAddressService;
import ch.ahoegger.docbox.shared.hr.employer.EmployerFormData;
import ch.ahoegger.docbox.shared.hr.employer.EmployerTablePageData;
import ch.ahoegger.docbox.shared.hr.employer.EmployerTablePageData.EmployerTableRowData;
import ch.ahoegger.docbox.shared.hr.employer.IEmployerService;

/**
 * <h3>{@link EmployerService}</h3>
 *
 * @author aho
 */
public class EmployerService implements IEmployerService {

  @Override
  public EmployerTablePageData getTableData() {
    Employer table = Employer.EMPLOYER;
    List<EmployerTableRowData> rows = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .select(table.EMPLOYER_NR, table.NAME, table.ADDRESS_NR, table.EMAIL, table.PHONE)
        .from(table)
        .fetch()
        .stream()
        .map(rec -> {
          EmployerTableRowData rd = new EmployerTableRowData();
          rd.setEmployerId(rec.get(table.EMPLOYER_NR));
          rd.setName(rec.get(table.NAME));
          return rd;
        })
        .collect(Collectors.toList());

    EmployerTablePageData pageData = new EmployerTablePageData();
    pageData.setRows(rows.toArray(new EmployerTableRowData[0]));
    return pageData;
  }

  @Override
  public EmployerFormData prepareCreate(EmployerFormData formData) {
    return formData;
  }

  @Override
  public EmployerFormData create(EmployerFormData formData) {
    // create address
    BEANS.get(IAddressService.class).create(formData.getAddressBox());
    formData.setEmployerId(new BigDecimal(SQL.getSequenceNextval(ISequenceTable.TABLE_NAME)));
    int rowCount = insert(formData);
    if (rowCount == 1) {
      // notify backup needed
      BEANS.get(IBackupService.class).notifyModification();
      return formData;
    }
    return null;
  }

  @Override
  public EmployerFormData load(EmployerFormData formData) {
    Assertions.assertNotNull(formData.getEmployerId());
    Employer emp = Employer.EMPLOYER;
    formData = mapToFormData(formData, DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .fetchOne(emp, emp.EMPLOYER_NR.eq(formData.getEmployerId())));
    // load address
    if (formData != null) {
      BEANS.get(IAddressService.class).load(formData.getAddressBox());
    }
    return formData;
  }

  @Override
  public EmployerFormData store(EmployerFormData formData) {
    Assertions.assertNotNull(formData.getEmployerId());
    // store address
    BEANS.get(IAddressService.class).store(formData.getAddressBox());

    Employer emp = Employer.EMPLOYER;
    EmployerRecord record = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .fetchOne(emp, emp.EMPLOYER_NR.eq(formData.getEmployerId()));

    if (record == null) {
      return null;
    }

    int rowCount = mapToRecord(record, formData).update();

    if (rowCount == 1) {
      // notify backup needed
      BEANS.get(IBackupService.class).notifyModification();

      return formData;
    }
    return null;
  }

  public int insert(EmployerFormData formData) {
    Employer emp = Employer.EMPLOYER;
    return mapToRecord(DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .newRecord(emp), formData).insert();

  }

  protected EmployerRecord mapToRecord(EmployerRecord rec, EmployerFormData formData) {
    Employer e = Employer.EMPLOYER;
    return rec
        .with(e.EMPLOYER_NR, formData.getEmployerId())
        .with(e.NAME, formData.getName().getValue())
        .with(e.ADDRESS_NR, formData.getAddressBox().getAddressId())
        .with(e.EMAIL, formData.getEmail().getValue())
        .with(e.PHONE, formData.getPhone().getValue());
  }

  protected EmployerFormData mapToFormData(EmployerFormData formData, EmployerRecord rec) {
    if (rec == null) {
      return null;
    }
    formData.setEmployerId(rec.getEmployerNr());
    formData.getAddressBox().setAddressId(rec.getAddressNr());
    formData.getName().setValue(rec.getName());
    formData.getEmail().setValue(rec.getEmail());
    formData.getPhone().setValue(rec.getPhone());
    return formData;
  }
}

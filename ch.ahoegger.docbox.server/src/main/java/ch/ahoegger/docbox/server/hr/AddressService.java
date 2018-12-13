package ch.ahoegger.docbox.server.hr;

import java.math.BigDecimal;
import java.sql.Connection;

import org.ch.ahoegger.docbox.server.or.app.tables.Address;
import org.ch.ahoegger.docbox.server.or.app.tables.records.AddressRecord;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.Assertions;
import org.eclipse.scout.rt.server.jdbc.SQL;
import org.eclipse.scout.rt.shared.servicetunnel.RemoteServiceAccessDenied;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import ch.ahoegger.docbox.or.definition.table.ISequenceTable;
import ch.ahoegger.docbox.shared.backup.IBackupService;
import ch.ahoegger.docbox.shared.hr.IAddressService;
import ch.ahoegger.docbox.shared.hr.employer.EmployerFormData.AddressBox;
import ch.ahoegger.docbox.shared.template.AbstractAddressBoxData;

/**
 * <h3>{@link AddressService}</h3>
 *
 * @author aho
 */
public class AddressService implements IAddressService {

  @Override
  public AddressBox prepareCreate(AddressBox formData) {
    return null;
  }

  @Override
  public AbstractAddressBoxData create(AbstractAddressBoxData formData) {
    formData.setAddressId(new BigDecimal(SQL.getSequenceNextval(ISequenceTable.TABLE_NAME)));
    int rowCount = insert(formData);
    if (rowCount == 1) {
      // notify backup needed
      BEANS.get(IBackupService.class).notifyModification();
      return formData;
    }
    return null;
  }

  @Override
  public AbstractAddressBoxData load(AbstractAddressBoxData formData) {
    Assertions.assertNotNull(formData.getAddressId());
    Address table = Address.ADDRESS;
    mapToFormData(formData, DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .fetchOne(table, table.ADDRESS_NR.eq(formData.getAddressId())));
    return formData;
  }

  @Override
  public AbstractAddressBoxData store(AbstractAddressBoxData formData) {
    Assertions.assertNotNull(formData.getAddressId());
    Address table = Address.ADDRESS;
    AddressRecord record = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .fetchOne(table, table.ADDRESS_NR.eq(formData.getAddressId()));

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

  @RemoteServiceAccessDenied
  public int insert(AbstractAddressBoxData formData) {
    return insert(SQL.getConnection(), formData);
  }

  @RemoteServiceAccessDenied
  public int insert(Connection connection, AbstractAddressBoxData formData) {
    Address table = Address.ADDRESS;
    return mapToRecord(DSL.using(connection, SQLDialect.DERBY)
        .newRecord(table), formData).insert();

  }

  protected AddressRecord mapToRecord(AddressRecord rec, AbstractAddressBoxData formData) {
    Address e = Address.ADDRESS;
    return rec
        .with(e.ADDRESS_NR, formData.getAddressId())
        .with(e.LINE_1, formData.getLine1().getValue())
        .with(e.LINE_2, formData.getLine2().getValue())
        .with(e.PLZ, formData.getPlz().getValue())
        .with(e.CITY, formData.getCity().getValue());
  }

  protected AbstractAddressBoxData mapToFormData(AbstractAddressBoxData formData, AddressRecord rec) {
    if (rec == null) {
      return null;
    }
    formData.setAddressId(rec.getAddressNr());
    formData.getLine1().setValue(rec.getLine_1());
    formData.getLine2().setValue(rec.getLine_2());
    formData.getPlz().setValue(rec.getPlz());
    formData.getCity().setValue(rec.getCity());
    return formData;
  }

}

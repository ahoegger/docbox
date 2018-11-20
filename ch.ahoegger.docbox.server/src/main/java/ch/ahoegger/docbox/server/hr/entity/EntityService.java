package ch.ahoegger.docbox.server.hr.entity;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.ch.ahoegger.docbox.server.or.app.tables.Entity;
import org.ch.ahoegger.docbox.server.or.app.tables.records.EntityRecord;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingStatus;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.status.IStatus;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.platform.util.ObjectUtility;
import org.eclipse.scout.rt.server.jdbc.SQL;
import org.eclipse.scout.rt.shared.servicetunnel.RemoteServiceAccessDenied;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.or.definition.table.ISequenceTable;
import ch.ahoegger.docbox.server.util.FieldValidator;
import ch.ahoegger.docbox.shared.backup.IBackupService;
import ch.ahoegger.docbox.shared.hr.billing.PayslipAccountingCodeType.UnbilledCode;
import ch.ahoegger.docbox.shared.hr.entity.EntityFormData;
import ch.ahoegger.docbox.shared.hr.entity.EntitySearchFormData;
import ch.ahoegger.docbox.shared.hr.entity.EntityTablePageData;
import ch.ahoegger.docbox.shared.hr.entity.EntityTablePageData.EntityTableRowData;
import ch.ahoegger.docbox.shared.hr.entity.IEntityService;
import ch.ahoegger.docbox.shared.util.LocalDateUtility;

public class EntityService implements IEntityService {
  private static final Logger LOG = LoggerFactory.getLogger(EntityService.class);

  @Override
  public EntityTablePageData getEntityTableData(EntitySearchFormData formData) {

    Entity e = Entity.ENTITY.as("ENT");

    Condition condition = DSL.trueCondition();

    // search partnerId
    if (formData.getPartnerId().getValue() != null) {
      condition = condition.and(e.PARTNER_NR.eq(formData.getPartnerId().getValue()));
    }

    // search payslipAccountingId
    if (formData.getPayslipAccountingId() == null) {
      condition = condition.and(e.PAYSLIP_ACCOUNTING_NR.isNull());
    }
    else {
      condition = condition.and(e.PAYSLIP_ACCOUNTING_NR.eq(formData.getPayslipAccountingId()));
    }

    // entity date from
    if (formData.getEntityDateFrom().getValue() != null) {
      condition = condition.and(e.ENTITY_DATE.ge(formData.getEntityDateFrom().getValue()));
    }
    // entity date to
    if (formData.getEntityDateTo().getValue() != null) {
      condition = condition.and(e.ENTITY_DATE.le(formData.getEntityDateTo().getValue()));
    }

    if (CollectionUtility.hasElements(formData.getEntityIds())) {
      condition = condition.and(e.ENTITY_NR.in(formData.getEntityIds()));
    }

    List<EntityTableRowData> rows = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .select(e.ENTITY_NR, e.PARTNER_NR, e.PAYSLIP_ACCOUNTING_NR, e.ENTITY_TYPE, e.ENTITY_DATE, e.WORKING_HOURS, e.EXPENSE_AMOUNT, e.DESCRIPTION)
        .from(e)
        .where(condition)
        .orderBy(e.ENTITY_DATE)
        .fetch()
        .stream()
        .map(rec -> {
          EntityTableRowData rd = new EntityTableRowData();
          rd.setEnityId(rec.get(e.ENTITY_NR));
          rd.setPartnerId(rec.get(e.PARTNER_NR));
          rd.setPayslipAccountingId(rec.get(e.PAYSLIP_ACCOUNTING_NR));
          rd.setEntityType(rec.get(e.ENTITY_TYPE));
          rd.setDate(rec.get(e.ENTITY_DATE));
          rd.setHours(rec.get(e.WORKING_HOURS));
          rd.setAmount(rec.get(e.EXPENSE_AMOUNT));
          rd.setText(rec.get(e.DESCRIPTION));
          return rd;
        })
        .collect(Collectors.toList());

    EntityTablePageData result = new EntityTablePageData();
    result.setRows(rows.toArray(new EntityTableRowData[0]));
    return result;
  }

  @Override
  public EntityFormData prepareCreate(EntityFormData formData) {
    Entity e = Entity.ENTITY.as("ENT");

    Condition condition = DSL.trueCondition();
    // alias
    Field<Date> maxDate = DSL.max(e.ENTITY_DATE).as("MAX_DATE");
    // search partnerId
    if (formData.getPartnerId() != null) {
      condition = condition.and(e.PARTNER_NR.eq(formData.getPartnerId()));
    }

    LocalDate today = LocalDate.now();
    Date date = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .select(maxDate)
        .from(e)
        .where(condition)
        .fetch()
        .stream()
        .findFirst()
        .map(rec -> rec.get(maxDate))
        .orElse(LocalDateUtility.toDate(today));
    formData.getEntityDate().setValue(date);
    formData.setPayslipAccountingId(UnbilledCode.ID);
    return formData;
  }

  @Override
  public EntityFormData create(EntityFormData formData) {
    if (formData.getPayslipAccountingId() == null) {
      throw new VetoException("Payslip accounting id can not be null.");
    }
    formData.setEntityId(new BigDecimal(SQL.getSequenceNextval(ISequenceTable.TABLE_NAME)));

    Entity e = Entity.ENTITY.as("ENT");
    int rowCount = mapToRecord(DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .newRecord(e), formData).insert();

    if (rowCount == 1) {
      // notify backup needed
      BEANS.get(IBackupService.class).notifyModification();
      return formData;
    }
    return null;
  }

  @Override
  public EntityFormData load(EntityFormData formData) {
    Entity e = Entity.ENTITY.as("ENT");
    return toFormData(DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .fetchOne(e, e.ENTITY_NR.eq(formData.getEntityId())));

  }

  @Override
  public EntityFormData store(EntityFormData formData) {
    if (ObjectUtility.notEquals(UnbilledCode.ID, formData.getPayslipAccountingId())) {
      throw new VetoException("Access denied.");
    }
    Entity e = Entity.ENTITY;
    FieldValidator validator = new FieldValidator();
    validator.add(FieldValidator.unmodifiableValidator(e.ENTITY_TYPE, formData.getEntityType()));
    validator.add(FieldValidator.unmodifiableValidator(e.PARTNER_NR, formData.getPartnerId()));
    validator.add(FieldValidator.unmodifiableValidator(e.PAYSLIP_ACCOUNTING_NR, formData.getPayslipAccountingId()));
    EntityRecord entity = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .fetchOne(e, e.ENTITY_NR.eq(formData.getEntityId()));

    if (entity == null) {
      return null;
    }

    IStatus validateStatus = validator.validate(entity);
    if (!validateStatus.isOK()) {
      throw new VetoException(new ProcessingStatus(validateStatus));
    }

    int rowCount = mapToRecord(entity, formData).update();

    if (rowCount == 1) {
      // notify backup needed
      BEANS.get(IBackupService.class).notifyModification();

      return formData;
    }
    return null;

  }

  @Override
  public boolean delete(BigDecimal entityId) {
    Entity e = Entity.ENTITY.as("ent");

    EntityRecord rec = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .fetchOne(e, e.ENTITY_NR.eq(entityId));
    if (rec == null) {
      LOG.warn("Try to delete not existing record with id '{}'!", entityId);
      return false;
    }
    if (rec.delete() != 1) {
      LOG.error("Could not delete record with id '{}'!", entityId);
      return false;
    }

    // notify backup needed
    BEANS.get(IBackupService.class).notifyModification();
    return true;
  }

  public void updateGroupId(Set<BigDecimal> entityIds, BigDecimal groupId) {
    Entity e = Entity.ENTITY.as("ENT");

    Condition condition = DSL.trueCondition();
    if (CollectionUtility.hasElements(entityIds)) {
      condition = e.ENTITY_NR.in(entityIds);
    }

    DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .update(e)
        .set(e.PAYSLIP_ACCOUNTING_NR, groupId)
        .where(condition)
        .execute();

    // notify backup needed
    BEANS.get(IBackupService.class).notifyModification();
  }

  @RemoteServiceAccessDenied
  public int insert(Connection connection, BigDecimal entityId, BigDecimal partnerId, BigDecimal payslipAccountingId, BigDecimal entityType, Date entityDate, BigDecimal hours, BigDecimal amount, String desc) {
    return DSL.using(connection, SQLDialect.DERBY)
        .executeInsert(mapToRecord(new EntityRecord(), entityId, partnerId, payslipAccountingId, entityType, entityDate, hours, amount, desc));
  }

  protected EntityRecord toRecord(EntityFormData fd) {
    if (fd == null) {
      return null;
    }
    return mapToRecord(new EntityRecord(), fd);

  }

  protected EntityRecord mapToRecord(EntityRecord rec, EntityFormData fd) {
    if (fd == null) {
      return null;
    }
    return mapToRecord(rec, fd.getEntityId(), fd.getPartnerId(), fd.getPayslipAccountingId(), fd.getEntityType(), fd.getEntityDate().getValue(), fd.getWorkHours().getValue(), fd.getExpenseAmount().getValue(), fd.getText().getValue());

  }

  protected EntityRecord mapToRecord(EntityRecord rec, BigDecimal entityId, BigDecimal partnerId, BigDecimal payslipAccountingId, BigDecimal entityType, Date entityDate, BigDecimal hours, BigDecimal amount, String desc) {
    Entity e = Entity.ENTITY;
    return rec
        .with(e.EXPENSE_AMOUNT, amount)
        .with(e.DESCRIPTION, desc)
        .with(e.ENTITY_DATE, entityDate)
        .with(e.ENTITY_NR, entityId)
        .with(e.ENTITY_TYPE, entityType)
        .with(e.WORKING_HOURS, hours)
        .with(e.PARTNER_NR, partnerId)
        .with(e.PAYSLIP_ACCOUNTING_NR, payslipAccountingId);
  }

  protected EntityFormData toFormData(EntityRecord rec) {
    if (rec == null) {
      return null;
    }
    EntityFormData fd = new EntityFormData();
    fd.getExpenseAmount().setValue(rec.getExpenseAmount());
    fd.getText().setValue(rec.getDescription());
    fd.getEntityDate().setValue(rec.getEntityDate());
    fd.setEntityId(rec.getEntityNr());
    fd.setEntityType(rec.getEntityType());
    fd.getWorkHours().setValue(rec.getWorkingHours());
    fd.setPartnerId(rec.getPartnerNr());
    fd.setPayslipAccountingId(rec.getPayslipAccountingNr());
    return fd;
  }

}

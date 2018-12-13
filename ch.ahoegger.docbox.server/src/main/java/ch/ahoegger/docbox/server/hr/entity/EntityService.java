package ch.ahoegger.docbox.server.hr.entity;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.ch.ahoegger.docbox.server.or.app.tables.BillingCycle;
import org.ch.ahoegger.docbox.server.or.app.tables.EmployeeTaxGroup;
import org.ch.ahoegger.docbox.server.or.app.tables.Entity;
import org.ch.ahoegger.docbox.server.or.app.tables.Payslip;
import org.ch.ahoegger.docbox.server.or.app.tables.records.EntityRecord;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingStatus;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.status.IStatus;
import org.eclipse.scout.rt.platform.status.MultiStatus;
import org.eclipse.scout.rt.platform.status.Status;
import org.eclipse.scout.rt.platform.text.TEXTS;
import org.eclipse.scout.rt.platform.util.Assertions;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.server.jdbc.SQL;
import org.eclipse.scout.rt.shared.servicetunnel.RemoteServiceAccessDenied;
import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.Record3;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.or.definition.table.ISequenceTable;
import ch.ahoegger.docbox.server.administration.hr.billing.BillingCycleLookupService;
import ch.ahoegger.docbox.server.administration.hr.billing.BillingCycleService;
import ch.ahoegger.docbox.server.hr.billing.payslip.PayslipService;
import ch.ahoegger.docbox.server.util.FieldValidator;
import ch.ahoegger.docbox.shared.administration.hr.billingcycle.BillingCycleFormData;
import ch.ahoegger.docbox.shared.backup.IBackupService;
import ch.ahoegger.docbox.shared.hr.billing.payslip.PayslipFormData;
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
    Payslip payslip = Payslip.PAYSLIP;
    EmployeeTaxGroup empTaxGrp = EmployeeTaxGroup.EMPLOYEE_TAX_GROUP;

    Condition condition = DSL.trueCondition();

    // search partnerId
    if (formData.getPartnerId().getValue() != null) {
      condition = condition.and(empTaxGrp.EMPLOYEE_NR.eq(formData.getPartnerId().getValue()));
    }

    // search PayslipId
    if (formData.getPayslipId() != null) {
      condition = condition.and(e.PAYSLIP_NR.eq(formData.getPayslipId()));
    }

    // entity date from
    if (formData.getEntityDateFrom().getValue() != null) {
      condition = condition.and(e.ENTITY_DATE.ge(formData.getEntityDateFrom().getValue()));
    }
    // entity date to
    if (formData.getEntityDateTo().getValue() != null) {
      condition = condition.and(e.ENTITY_DATE.le(formData.getEntityDateTo().getValue()));
    }

    List<EntityTableRowData> rows = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .select(e.ENTITY_NR, e.PAYSLIP_NR, e.ENTITY_TYPE, e.ENTITY_DATE, e.WORKING_HOURS, e.EXPENSE_AMOUNT, e.DESCRIPTION, payslip.STATEMENT_NR, empTaxGrp.EMPLOYEE_NR)
        .from(e)
        .leftOuterJoin(payslip).on(e.PAYSLIP_NR.eq(payslip.PAYSLIP_NR))
        .leftOuterJoin(empTaxGrp).on(payslip.EMPLOYEE_TAX_GROUP_NR.eq(empTaxGrp.EMPLOYEE_TAX_GROUP_NR))
        .where(condition)
        .orderBy(e.ENTITY_DATE)
        .fetch()
        .stream()
        .map(rec -> {
          EntityTableRowData rd = new EntityTableRowData();
          rd.setEnityId(rec.get(e.ENTITY_NR));
          rd.setPayslipId(rec.get(e.PAYSLIP_NR));
          rd.setEntityType(rec.get(e.ENTITY_TYPE));
          rd.setDate(rec.get(e.ENTITY_DATE));
          rd.setHours(rec.get(e.WORKING_HOURS));
          rd.setAmount(rec.get(e.EXPENSE_AMOUNT));
          rd.setText(rec.get(e.DESCRIPTION));
          rd.setPayslipId(rec.get(payslip.STATEMENT_NR));
          return rd;
        })
        .collect(Collectors.toList());

    EntityTablePageData result = new EntityTablePageData();
    result.setRows(rows.toArray(new EntityTableRowData[0]));
    return result;
  }

  @Override
  public EntityFormData prepareCreate(EntityFormData formData) {
    Assertions.assertNotNull(formData.getPayslipId());
    validatePayslipRelation(formData, new MultiStatus(), true);
    PayslipFormData payslipData = new PayslipFormData();
    payslipData.setPayslipId(formData.getPayslipId());
    payslipData = BEANS.get(PayslipService.class).load(payslipData);

    BillingCycleFormData billingCycleData = new BillingCycleFormData();
    billingCycleData.setBillingCycleId(payslipData.getBillingCycle().getValue());
    billingCycleData = BEANS.get(BillingCycleService.class).load(billingCycleData);

    EntitySearchFormData entitySearchData = new EntitySearchFormData();
    entitySearchData.setPayslipId(formData.getPayslipId());
    List<EntityTableRowData> entities = CollectionUtility.arrayList(getEntityTableData(entitySearchData).getRows());
    if (entities.size() > 0) {
      // find max
      formData.getEntityDate().setValue(LocalDateUtility.toDate(entities.stream()
          .map(e -> LocalDateUtility.toLocalDate(e.getDate()))
          .max(Comparator.naturalOrder()).get()));
    }
    else {
      formData.getEntityDate().setValue(billingCycleData.getPeriodBox().getFrom().getValue());
    }
    return formData;
  }

  @Override
  public IStatus validate(EntityFormData formData) {
    MultiStatus status = new MultiStatus();
    validatePayslipRelation(formData, status, false);
    return status;
  }

  @Override
  public EntityFormData create(EntityFormData formData) {
    if (formData.getPayslipId() == null) {
      throw new VetoException("Payslip accounting id can not be null.");
    }
    validatePayslipRelation(formData, new MultiStatus(), true);
    BillingCycleFormData billingCycleData = BEANS.get(BillingCycleService.class).getBillingCycleByPayslipId(formData.getPayslipId());
    if (!LocalDateUtility.isBetweenOrEqual(billingCycleData.getPeriodBox().getFrom().getValue(), billingCycleData.getPeriodBox().getTo().getValue(), formData.getEntityDate().getValue())) {
      throw new VetoException(TEXTS.get("Validate_DateNotInPeriod", TEXTS.get("PaySlip")));
    }

    formData.setEntityId(new BigDecimal(SQL.getSequenceNextval(ISequenceTable.TABLE_NAME)));

    if (insert(SQL.getConnection(), formData) == 1) {
      // notify backup needed
      BEANS.get(IBackupService.class).notifyModification();
      return formData;
    }
    return null;
  }

  @Override
  public EntityFormData load(EntityFormData formData) {
    Entity e = Entity.ENTITY.as("ENT");
    return toFormData(
        DSL.using(SQL.getConnection(), SQLDialect.DERBY)
            .select(e.fields())
            .from(e)
            .where(e.ENTITY_NR.eq(formData.getEntityId()))
            .fetchOne(),
        formData);
  }

  @Override
  public EntityFormData store(EntityFormData formData) {
    validatePayslipRelation(formData, new MultiStatus(), true);
    Entity e = Entity.ENTITY;
    FieldValidator validator = new FieldValidator();
    validator.add(FieldValidator.unmodifiableValidator(e.ENTITY_TYPE, formData.getEntityType()));
    validator.add(FieldValidator.unmodifiableValidator(e.PAYSLIP_NR, formData.getPayslipId()));
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
    EntityFormData formData = new EntityFormData();
    formData.setEntityId(entityId);
    formData = load(formData);
    validatePayslipRelation(formData, new MultiStatus(), true);

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
        .set(e.PAYSLIP_NR, groupId)
        .where(condition)
        .execute();

    // notify backup needed
    BEANS.get(IBackupService.class).notifyModification();
  }

//  entityId01, BigDecimal.valueOf(5), UnbilledCode.ID, WorkCode.ID, LocalDateUtility.toDate(LocalDate.of(2016, 3, 31)), BigDecimal.valueOf(2), null, "desc");
  public int insert(Connection connection, BigDecimal entityId, BigDecimal payslipId, BigDecimal entityType, Date date, BigDecimal workingHours, BigDecimal expense, String text) {
    EntityFormData fd = new EntityFormData();
    fd.setEntityId(entityId);
    fd.setPayslipId(payslipId);
    fd.setEntityType(entityType);
    fd.getEntityDate().setValue(date);
    fd.getWorkHours().setValue(workingHours);
    fd.getExpenseAmount().setValue(expense);
    fd.getText().setValue(text);
    return insert(connection, fd);
  }

  @RemoteServiceAccessDenied
  public int insert(Connection connection, EntityFormData formData) {
    Entity table = Entity.ENTITY;
    return mapToRecord(DSL.using(connection, SQLDialect.DERBY)
        .newRecord(table), formData).insert();
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
    Entity e = Entity.ENTITY;
    return rec
        .with(e.EXPENSE_AMOUNT, fd.getExpenseAmount().getValue())
        .with(e.DESCRIPTION, fd.getText().getValue())
        .with(e.ENTITY_DATE, fd.getEntityDate().getValue())
        .with(e.ENTITY_NR, fd.getEntityId())
        .with(e.ENTITY_TYPE, fd.getEntityType())
        .with(e.WORKING_HOURS, fd.getWorkHours().getValue())
        .with(e.PAYSLIP_NR, fd.getPayslipId());
  }

  protected EntityFormData toFormData(Record rec, EntityFormData fd) {
    if (rec == null) {
      return null;
    }
    Entity e = Entity.ENTITY.as("ENT");

    fd.getExpenseAmount().setValue(rec.get(e.EXPENSE_AMOUNT));
    fd.getText().setValue(rec.get(e.DESCRIPTION));
    fd.getEntityDate().setValue(rec.get(e.ENTITY_DATE));
    fd.setEntityId(rec.get(e.ENTITY_NR));
    fd.setEntityType(rec.get(e.ENTITY_TYPE));
    fd.getWorkHours().setValue(rec.get(e.WORKING_HOURS));
    fd.setPayslipId(rec.get(e.PAYSLIP_NR));
    return fd;
  }

  /**
   * @param formData
   * @param status
   */
  protected void validatePayslipRelation(EntityFormData formData, MultiStatus status, boolean throwVetoException) {

    Payslip payslip = Payslip.PAYSLIP;
    BillingCycle billingCycle = BillingCycle.BILLING_CYCLE;

    Record3<BigDecimal, Date, Date> record = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .select(billingCycle.BILLING_CYCLE_NR, billingCycle.START_DATE, billingCycle.END_DATE)
        .from(payslip)
        .innerJoin(billingCycle).on(payslip.BILLING_CYCLE_NR.eq(billingCycle.BILLING_CYCLE_NR))
        .where(payslip.PAYSLIP_NR.eq(formData.getPayslipId()))
        .fetchOne();
    if (formData.getEntityDate().getValue() != null) {
      if (!LocalDateUtility.isBetweenOrEqual(record.get(billingCycle.START_DATE), record.get(billingCycle.END_DATE), formData.getEntityDate().getValue())) {
        status.add(new Status(
            new StringBuilder()
                .append("Entity date is not in payslip '")
                .append(BEANS.get(BillingCycleLookupService.class).getName(record.get(billingCycle.BILLING_CYCLE_NR)))
                .append("' range.")
                .toString(),
            IStatus.WARNING));
      }
    }
    IStatus finalizedStatus = BEANS.get(PayslipService.class).isFinalized(formData.getPayslipId());
    if (!finalizedStatus.isOK()) {
      throw new VetoException(new ProcessingStatus(finalizedStatus));
    }
  }

}

package ch.ahoegger.docbox.server.hr.billing;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.ch.ahoegger.docbox.jasper.WageReportService;
import org.ch.ahoegger.docbox.jasper.bean.WageCalculation;
import org.ch.ahoegger.docbox.server.or.app.tables.EmployeeTaxGroup;
import org.ch.ahoegger.docbox.server.or.app.tables.PayslipAccounting;
import org.ch.ahoegger.docbox.server.or.app.tables.records.PayslipAccountingRecord;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingStatus;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.resource.BinaryResource;
import org.eclipse.scout.rt.platform.status.IStatus;
import org.eclipse.scout.rt.platform.text.TEXTS;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.server.jdbc.SQL;
import org.eclipse.scout.rt.shared.servicetunnel.RemoteServiceAccessDenied;
import org.jooq.Condition;
import org.jooq.SQLDialect;
import org.jooq.TableLike;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.or.definition.table.ISequenceTable;
import ch.ahoegger.docbox.server.document.DocumentService;
import ch.ahoegger.docbox.server.hr.employee.EmployeeService;
import ch.ahoegger.docbox.server.hr.entity.EntityService;
import ch.ahoegger.docbox.server.util.FieldValidator;
import ch.ahoegger.docbox.shared.administration.taxgroup.ITaxGroupService;
import ch.ahoegger.docbox.shared.backup.IBackupService;
import ch.ahoegger.docbox.shared.document.DocumentFormData;
import ch.ahoegger.docbox.shared.document.DocumentFormData.Partners.PartnersRowData;
import ch.ahoegger.docbox.shared.hr.billing.AbstractPayslipAccountingBoxData.Entities.EntitiesRowData;
import ch.ahoegger.docbox.shared.hr.billing.IPayslipAccountingService;
import ch.ahoegger.docbox.shared.hr.billing.PayslipAccountingCodeType.UnbilledCode;
import ch.ahoegger.docbox.shared.hr.billing.PayslipAccountingFormData;
import ch.ahoegger.docbox.shared.hr.billing.PayslipAccountingSearchFormData;
import ch.ahoegger.docbox.shared.hr.billing.PayslipAccountingTableData;
import ch.ahoegger.docbox.shared.hr.billing.PayslipAccountingTableData.PayslipAccountingTableRowData;
import ch.ahoegger.docbox.shared.hr.employee.EmployeeFormData;
import ch.ahoegger.docbox.shared.hr.employee.IEmployeeService;
import ch.ahoegger.docbox.shared.hr.entity.EntitySearchFormData;
import ch.ahoegger.docbox.shared.hr.entity.EntityTablePageData;
import ch.ahoegger.docbox.shared.hr.entity.EntityTablePageData.EntityTableRowData;
import ch.ahoegger.docbox.shared.hr.entity.IEntityService;
import ch.ahoegger.docbox.shared.hr.tax.TaxGroupSearchFormData;
import ch.ahoegger.docbox.shared.util.LocalDateUtility;

/**
 * <h3>{@link PayslipAccountingService}</h3>
 *
 * @author Andreas Hoegger
 */
public class PayslipAccountingService implements IPayslipAccountingService {
  private static final Logger LOG = LoggerFactory.getLogger(PayslipAccountingService.class);

  @Override
  public PayslipAccountingTableData getTableData(PayslipAccountingSearchFormData formData) {
    Condition condition = DSL.trueCondition();
    PayslipAccounting pg = PayslipAccounting.PAYSLIP_ACCOUNTING.as("PG");
    EmployeeTaxGroup etg = EmployeeTaxGroup.EMPLOYEE_TAX_GROUP.as("ETG");
    TableLike<?> table = pg;

    if (formData.getPartner().getValue() != null) {
      condition = condition.and(pg.PARTNER_NR.eq(formData.getPartner().getValue()));
      table = pg.innerJoin(etg).on(etg.PARTNER_NR.eq(pg.PARTNER_NR));
    }
    if (formData.getTaxGroupId() != null) {
      condition = condition.and(pg.TAX_GROUP_NR.eq(formData.getTaxGroupId()));
    }
    // startDate from
    if (formData.getStartDateFrom().getValue() != null) {
      condition = condition.and(pg.START_DATE.ge(formData.getStartDateFrom().getValue()));
    }
    // startDate to
    if (formData.getStartDateTo().getValue() != null) {
      condition = condition.and(pg.START_DATE.le(formData.getStartDateTo().getValue()));
    }

    // endDate from
    if (formData.getEndDateFrom().getValue() != null) {
      condition = condition.and(pg.END_DATE.ge(formData.getEndDateFrom().getValue()));
    }
    // endDate to
    if (formData.getEndDateTo().getValue() != null) {
      condition = condition.and(pg.END_DATE.le(formData.getEndDateTo().getValue()));
    }

    List<PayslipAccountingTableRowData> rows = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .select(pg.PAYSLIP_ACCOUNTING_NR, pg.PARTNER_NR, pg.DOCUMENT_NR, pg.NAME, pg.START_DATE, pg.END_DATE, pg.STATEMENT_DATE, pg.WORKING_HOURS, pg.BRUTTO_WAGE, pg.NETTO_WAGE, pg.SOURCE_TAX, pg.SOCIAL_SECURITY_TAX, pg.VACATION_EXTRA,
            pg.TAX_GROUP_NR)
        .from(table)
        .where(condition)
        .fetch()
        .stream()
        .map(rec -> {
          PayslipAccountingTableRowData rd = new PayslipAccountingTableRowData();
          rd.setSortGroup(BigDecimal.valueOf(1));
          rd.setId(rec.get(pg.PAYSLIP_ACCOUNTING_NR));
          rd.setName(rec.get(pg.NAME));
          rd.setStartDate(rec.get(pg.START_DATE));
          rd.setEndDate(rec.get(pg.END_DATE));
          rd.setDate(rec.get(pg.STATEMENT_DATE));
          rd.setWorkingHours(rec.get(pg.WORKING_HOURS));
          rd.setBruttoWage(rec.get(pg.BRUTTO_WAGE));
          rd.setSourceTax(rec.get(pg.SOURCE_TAX));
          rd.setSocialSecurityTax(rec.get(pg.SOCIAL_SECURITY_TAX));
          rd.setVacationExtra(rec.get(pg.VACATION_EXTRA));
          rd.setNettoWage(rec.get(pg.NETTO_WAGE));
          rd.setTaxGroup(rec.get(pg.TAX_GROUP_NR));
          rd.setPartnerId(rec.get(pg.PARTNER_NR));
          rd.setDocumentId(rec.get(pg.DOCUMENT_NR));
          return rd;
        })
        .collect(Collectors.toList());

    // unbilled row
    if (formData.getIncludeUnbilled() != null && formData.getIncludeUnbilled().booleanValue()) {
      PayslipAccountingTableRowData unbilledRow = new PayslipAccountingTableRowData();
      unbilledRow.setId(UnbilledCode.ID);
      unbilledRow.setSortGroup(BigDecimal.valueOf(2));
      unbilledRow.setPartnerId(formData.getPartner().getValue());
      unbilledRow.setName(TEXTS.get("Unbilled"));
      rows.add(unbilledRow);

    }
    PayslipAccountingTableData tableData = new PayslipAccountingTableData();
    tableData.setRows(rows.toArray(new PayslipAccountingTableRowData[0]));
    return tableData;
  }

  @Override
  public PayslipAccountingFormData prepareCreate(PayslipAccountingFormData formData) {
    LocalDate today = LocalDate.now();
    formData.getDate().setValue(LocalDateUtility.toDate(today));

    if (formData.getFrom().getValue() == null && formData.getTo().getValue() == null) {
      DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM yyyy", LocalDateUtility.DE_CH);
      if (today.getDayOfMonth() < 20) {
        formData.getTitle().setValue(today.minusMonths(1).format(monthFormatter));
        formData.getDocumentAbstract().setValue(TEXTS.get("PayslipTitle", today.minusMonths(1).format(monthFormatter)));
        formData.getFrom().setValue(LocalDateUtility.toDate(today.minusMonths(1).withDayOfMonth(1)));
        formData.getTo().setValue(LocalDateUtility.toDate(today.minusMonths(1).withDayOfMonth(today.minusMonths(1).lengthOfMonth())));
      }
      else {
        formData.getTitle().setValue(today.format(monthFormatter));
        formData.getDocumentAbstract().setValue(TEXTS.get("PayslipTitle", today.format(monthFormatter)));
        formData.getFrom().setValue(LocalDateUtility.toDate(today.withDayOfMonth(1)));
        formData.getTo().setValue(LocalDateUtility.toDate(today.withDayOfMonth(today.lengthOfMonth())));
      }
    }

    TaxGroupSearchFormData taxGroupData = new TaxGroupSearchFormData();
    Date fromDate = formData.getFrom().getValue();
    Date toDate = formData.getTo().getValue();
    BigDecimal taxGroupId = Arrays.stream(BEANS.get(ITaxGroupService.class).getTaxGroupTableData(taxGroupData).getRows())
        .filter(row -> Optional.ofNullable(row.getStartDate()).filter(sd -> sd.before(fromDate) || sd.equals(fromDate)).isPresent())
        .filter(row -> Optional.ofNullable(row.getEndDate()).filter(ed -> ed.after(toDate) || ed.equals(toDate)).isPresent())
        .map(row -> row.getTaxGroupId())
        .findFirst().orElse(null);

    formData.getTaxGroup().setValue(taxGroupId);

    return calculateWage(formData);
  }

  @Override
  public PayslipAccountingFormData create(PayslipAccountingFormData formData) {

    // create document
    EmployeeFormData employeeData = new EmployeeFormData();
    employeeData.setPartnerId(formData.getPartner().getValue());
    employeeData = BEANS.get(EmployeeService.class).load(employeeData);

    List<EntityTableRowData> unbilledEntities = getUnbilledEntities(formData.getFrom().getValue(), formData.getTo().getValue(), formData.getPartner().getValue());

    WageCalculation wageData = BEANS.get(WageCalculationService.class).calculateWage(unbilledEntities, employeeData.getEmploymentBox().getHourlyWage().getValue(),
        employeeData.getEmploymentBox().getSocialInsuranceRate().getValue(), employeeData.getEmploymentBox().getSourceTaxRate().getValue(), employeeData.getEmploymentBox().getVacationExtraRate().getValue());
    byte[] docContent = BEANS.get(WageReportService.class).createMonthlyReport(formData.getTitle().getValue(), employeeData.getEmployeeBox().getFirstName().getValue() + " " + employeeData.getEmployeeBox().getLastName().getValue(),
        employeeData.getEmployeeBox().getAddressLine1().getValue(), employeeData.getEmployeeBox().getAddressLine2().getValue(), formData.getDate().getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
        employeeData.getEmployeeBox().getAccountNumber().getValue(), employeeData.getEmploymentBox().getHourlyWage().getValue(), wageData,
        employeeData.getEmployerBox().getAddressLine1().getValue(), employeeData.getEmployerBox().getAddressLine2().getValue(), employeeData.getEmployerBox().getAddressLine3().getValue(), employeeData.getEmployerBox().getEmail().getValue(),
        employeeData.getEmployerBox().getPhone().getValue());

    DocumentFormData documentData = new DocumentFormData();
    DocumentService documentService = BEANS.get(DocumentService.class);
    documentData = documentService.prepareCreate(documentData);
    documentData.getDocumentDate().setValue(LocalDateUtility.today());
    documentData.getDocument().setValue(new BinaryResource("payslip.pdf", docContent));
    PartnersRowData partnerRow = documentData.getPartners().addRow();
    partnerRow.setPartner(formData.getPartner().getValue());
    documentData.getAbstract().setValue(formData.getDocumentAbstract().getValue());
    documentData.getCategoriesBox().setValue(CollectionUtility.emptyTreeSet());
    documentData = documentService.create(documentData);
    formData.setDocumentId(documentData.getDocumentId());

    // create payslip
    BigDecimal payslipAccountingId = BigDecimal.valueOf(SQL.getSequenceNextval(ISequenceTable.TABLE_NAME));

    formData.setPayslipAccountingId(payslipAccountingId);

    PayslipAccounting pg = PayslipAccounting.PAYSLIP_ACCOUNTING;
    int rowCount = mapToRecord(DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .newRecord(pg), formData).insert();

    if (rowCount == 1) {

      // update entities
      BEANS.get(EntityService.class).updateGroupId(unbilledEntities.stream().map(e -> e.getEnityId()).collect(Collectors.toSet()), payslipAccountingId);

      // notify backup needed
      BEANS.get(IBackupService.class).notifyModification();
      return formData;
    }
    return null;
  }

  @Override
  public PayslipAccountingFormData calculateWage(PayslipAccountingFormData formData) {
    EmployeeFormData employeeData = new EmployeeFormData();
    employeeData.setPartnerId(formData.getPartner().getValue());
    employeeData = BEANS.get(IEmployeeService.class).load(employeeData);

    List<EntityTableRowData> unbilledEntities = getUnbilledEntities(formData.getFrom().getValue(), formData.getTo().getValue(), formData.getPartner().getValue());
    WageCalculation wageCalc = BEANS.get(WageCalculationService.class).calculateWage(unbilledEntities,
        employeeData.getEmploymentBox().getHourlyWage().getValue(), employeeData.getEmploymentBox().getSocialInsuranceRate().getValue(), employeeData.getEmploymentBox().getSourceTaxRate().getValue(),
        employeeData.getEmploymentBox().getVacationExtraRate().getValue());

    formData.getPayslipCalculationBox().getBruttoWage().setValue(wageCalc.getBruttoWage());
    formData.getPayslipCalculationBox().getNettoWage().setValue(wageCalc.getNettoWage());
    formData.getPayslipCalculationBox().getWorkingHours().setValue(wageCalc.getHoursTotal());
    formData.getPayslipCalculationBox().getSocialSecurityTax().setValue(wageCalc.getSocialSecuityTax());
    formData.getPayslipCalculationBox().getSourceTax().setValue(wageCalc.getSourceTax());
    formData.getPayslipCalculationBox().getVacationExtra().setValue(wageCalc.getVacationExtra());

    List<EntitiesRowData> entityRows = unbilledEntities.stream().map(row -> {
      EntitiesRowData rd = new EntitiesRowData();
      rd.setAmount(row.getAmount());
      rd.setPayslipAccountingId(row.getPayslipAccountingId());
      rd.setDate(row.getDate());
      rd.setEnityId(row.getEnityId());
      rd.setEntityType(row.getEntityType());
      rd.setHours(row.getHours());
      rd.setPartnerId(row.getPartnerId());
      return rd;
    }).collect(Collectors.toList());
    formData.getPayslipCalculationBox().getEntities().setRows(entityRows.toArray(new EntitiesRowData[0]));
    return formData;
  }

  @Override
  public PayslipAccountingFormData load(PayslipAccountingFormData formData) {
    PayslipAccounting pg = PayslipAccounting.PAYSLIP_ACCOUNTING;
    formData = toFormData(DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .fetchOne(pg, pg.PAYSLIP_ACCOUNTING_NR.eq(formData.getPayslipAccountingId())));
    EntitySearchFormData entitySearchData = new EntitySearchFormData();
    entitySearchData.setPayslipAccountingId(formData.getPayslipAccountingId());
    List<EntitiesRowData> entityRows = CollectionUtility.arrayList(BEANS.get(EntityService.class).getEntityTableData(entitySearchData).getRows()).stream()
        .map(row -> {
          EntitiesRowData rd = new EntitiesRowData();
          rd.setAmount(row.getAmount());
          rd.setPayslipAccountingId(row.getPayslipAccountingId());
          rd.setDate(row.getDate());
          rd.setEnityId(row.getEnityId());
          rd.setEntityType(row.getEntityType());
          rd.setHours(row.getHours());
          rd.setPartnerId(row.getPartnerId());
          return rd;
        }).collect(Collectors.toList());

    formData.getPayslipCalculationBox().getEntities().setRows(entityRows.toArray(new EntitiesRowData[0]));
    return formData;
  }

  @Override
  public PayslipAccountingFormData store(PayslipAccountingFormData formData) {
    PayslipAccounting pg = PayslipAccounting.PAYSLIP_ACCOUNTING;

    FieldValidator validator = new FieldValidator();
    validator.add(FieldValidator.unmodifiableValidator(pg.BRUTTO_WAGE, formData.getPayslipCalculationBox().getBruttoWage().getValue()));
    validator.add(FieldValidator.unmodifiableValidator(pg.DOCUMENT_NR, formData.getDocumentId()));
    validator.add(FieldValidator.unmodifiableValidator(pg.END_DATE, formData.getTo().getValue()));
    validator.add(FieldValidator.unmodifiableValidator(pg.NAME, formData.getTitle().getValue()));
    validator.add(FieldValidator.unmodifiableValidator(pg.NETTO_WAGE, formData.getPayslipCalculationBox().getNettoWage().getValue()));
    validator.add(FieldValidator.unmodifiableValidator(pg.PARTNER_NR, formData.getPartner().getValue()));
    validator.add(FieldValidator.unmodifiableValidator(pg.PAYSLIP_ACCOUNTING_NR, formData.getPayslipAccountingId()));
    validator.add(FieldValidator.unmodifiableValidator(pg.SOCIAL_SECURITY_TAX, formData.getPayslipCalculationBox().getSocialSecurityTax().getValue()));
    validator.add(FieldValidator.unmodifiableValidator(pg.START_DATE, formData.getFrom().getValue()));
    validator.add(FieldValidator.unmodifiableValidator(pg.STATEMENT_DATE, formData.getDate().getValue()));
    validator.add(FieldValidator.unmodifiableValidator(pg.VACATION_EXTRA, formData.getPayslipCalculationBox().getVacationExtra().getValue()));
    validator.add(FieldValidator.unmodifiableValidator(pg.WORKING_HOURS, formData.getPayslipCalculationBox().getWorkingHours().getValue()));

    PayslipAccountingRecord payslipAccounting = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .fetchOne(pg, pg.PAYSLIP_ACCOUNTING_NR.eq(formData.getPayslipAccountingId()));

    if (payslipAccounting == null) {
      return null;
    }

    IStatus validateStatus = validator.validate(payslipAccounting);
    if (!validateStatus.isOK()) {
      throw new VetoException(new ProcessingStatus(validateStatus));
    }

    int rowCount = mapToRecord(payslipAccounting, formData).update();

    if (rowCount == 1) {
      // notify backup needed
      BEANS.get(IBackupService.class).notifyModification();

      return formData;
    }
    return null;

  }

  @Override
  public boolean delete(BigDecimal payslipAccountingId) {

    PayslipAccounting pg = PayslipAccounting.PAYSLIP_ACCOUNTING;

    PayslipAccountingRecord rec = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .fetchOne(pg, pg.PAYSLIP_ACCOUNTING_NR.eq(payslipAccountingId));
    if (rec == null) {
      LOG.warn("Try to delete not existing record with id '{}'!", payslipAccountingId);
      return false;
    }
    if (rec.delete() != 1) {
      LOG.error("Could not delete record with id '{}'!", payslipAccountingId);
      return false;
    }

    BEANS.get(DocumentService.class).delete(rec.getDocumentNr());

    // update entities
    EntitySearchFormData entitySearchData = new EntitySearchFormData();
    entitySearchData.getPartnerId().setValue(rec.getPartnerNr());
    entitySearchData.setPayslipAccountingId(payslipAccountingId);
    EntityTablePageData entityTableData = BEANS.get(IEntityService.class).getEntityTableData(entitySearchData);

    BEANS.get(EntityService.class).updateGroupId(CollectionUtility.arrayList(entityTableData.getRows()).stream().map(e -> e.getEnityId()).collect(Collectors.toSet()), UnbilledCode.ID);

    // notify backup needed
    BEANS.get(IBackupService.class).notifyModification();
    return true;
  }

  protected List<EntityTableRowData> getUnbilledEntities(Date from, Date to, BigDecimal partnerId) {
    EntitySearchFormData entitySearchData = new EntitySearchFormData();
    entitySearchData.getEntityDateFrom().setValue(from);
    entitySearchData.getEntityDateTo().setValue(to);
    entitySearchData.getPartnerId().setValue(partnerId);
    entitySearchData.setPayslipAccountingId(UnbilledCode.ID);
    EntityTablePageData entityTableData = BEANS.get(IEntityService.class).getEntityTableData(entitySearchData);
    return CollectionUtility.arrayList(entityTableData.getRows());
  }

  @RemoteServiceAccessDenied
  public int insert(Connection connection, BigDecimal payslipAccountingId, BigDecimal partnerId, BigDecimal taxGroupId, BigDecimal documentId, String name,
      Date startDate, Date endDate, Date statementDate, BigDecimal workingHours, BigDecimal bruttoWage, BigDecimal nettoWage, BigDecimal sourceTax, BigDecimal socialSecurityTax, BigDecimal vacationExtra) {
    return DSL.using(connection, SQLDialect.DERBY)
        .executeInsert(
            mapToRecord(new PayslipAccountingRecord(), payslipAccountingId, partnerId, taxGroupId, documentId, name, startDate, endDate, statementDate, workingHours, bruttoWage, nettoWage, sourceTax, socialSecurityTax, vacationExtra));
  }

  protected PayslipAccountingFormData toFormData(PayslipAccountingRecord rec) {
    if (rec == null) {
      return null;
    }
    PayslipAccountingFormData fd = new PayslipAccountingFormData();
    fd.getPayslipCalculationBox().getBruttoWage().setValue(rec.getBruttoWage());
    fd.setDocumentId(rec.getDocumentNr());
    fd.getFrom().setValue(rec.getStartDate());
    fd.getTitle().setValue(rec.getName());
    fd.getPayslipCalculationBox().getNettoWage().setValue(rec.getNettoWage());
    fd.getPartner().setValue(rec.getPartnerNr());
    fd.setPayslipAccountingId(rec.getPayslipAccountingNr());
    fd.getPayslipCalculationBox().getSocialSecurityTax().setValue(rec.getSocialSecurityTax());
    fd.getPayslipCalculationBox().getSourceTax().setValue(rec.getSourceTax());
    fd.getDate().setValue(rec.getStatementDate());
    fd.getTaxGroup().setValue(rec.getTaxGroupNr());
    fd.getTo().setValue(rec.getEndDate());
    fd.getPayslipCalculationBox().getVacationExtra().setValue(rec.getVacationExtra());
    fd.getPayslipCalculationBox().getWorkingHours().setValue(rec.getWorkingHours());
    return fd;

  }

  protected PayslipAccountingRecord mapToRecord(PayslipAccountingRecord rec, PayslipAccountingFormData fd) {
    if (fd == null) {
      return null;
    }
    return mapToRecord(rec, fd.getPayslipAccountingId(), fd.getPartner().getValue(), fd.getTaxGroup().getValue(), fd.getDocumentId(), fd.getTitle().getValue(),
        fd.getFrom().getValue(), fd.getTo().getValue(), fd.getDate().getValue(), fd.getPayslipCalculationBox().getWorkingHours().getValue(), fd.getPayslipCalculationBox().getBruttoWage().getValue(),
        fd.getPayslipCalculationBox().getNettoWage().getValue(), fd.getPayslipCalculationBox().getSourceTax().getValue(), fd.getPayslipCalculationBox().getSocialSecurityTax().getValue(),
        fd.getPayslipCalculationBox().getVacationExtra().getValue());

  }

  protected PayslipAccountingRecord mapToRecord(PayslipAccountingRecord rec, BigDecimal payslipAccountingId, BigDecimal partnerId, BigDecimal taxGroupId, BigDecimal documentId, String name,
      Date startDate, Date endDate, Date statementDate, BigDecimal workingHours, BigDecimal bruttoWage, BigDecimal nettoWage, BigDecimal sourceTax, BigDecimal socialSecurityTax, BigDecimal vacationExtra) {
    PayslipAccounting pg = PayslipAccounting.PAYSLIP_ACCOUNTING;
    return rec
        .with(pg.BRUTTO_WAGE, bruttoWage)
        .with(pg.DOCUMENT_NR, documentId)
        .with(pg.END_DATE, endDate)
        .with(pg.NAME, name)
        .with(pg.NETTO_WAGE, nettoWage)
        .with(pg.PARTNER_NR, partnerId)
        .with(pg.PAYSLIP_ACCOUNTING_NR, payslipAccountingId)
        .with(pg.SOCIAL_SECURITY_TAX, socialSecurityTax)
        .with(pg.SOURCE_TAX, sourceTax)
        .with(pg.START_DATE, startDate)
        .with(pg.STATEMENT_DATE, startDate)
        .with(pg.TAX_GROUP_NR, taxGroupId)
        .with(pg.VACATION_EXTRA, vacationExtra)
        .with(pg.WORKING_HOURS, workingHours);
  }

}

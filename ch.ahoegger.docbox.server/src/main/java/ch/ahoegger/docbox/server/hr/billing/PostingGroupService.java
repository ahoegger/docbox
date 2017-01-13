package ch.ahoegger.docbox.server.hr.billing;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.ch.ahoegger.docbox.jasper.PayslipProperties;
import org.ch.ahoegger.docbox.jasper.WageReportService;
import org.ch.ahoegger.docbox.jasper.bean.Expense;
import org.ch.ahoegger.docbox.jasper.bean.WageCalculation;
import org.ch.ahoegger.docbox.jasper.bean.Work;
import org.ch.ahoegger.docbox.server.or.app.tables.PostingGroup;
import org.ch.ahoegger.docbox.server.or.app.tables.records.PostingGroupRecord;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.config.CONFIG;
import org.eclipse.scout.rt.platform.resource.BinaryResource;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.server.jdbc.SQL;
import org.eclipse.scout.rt.shared.TEXTS;
import org.jooq.Condition;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.or.definition.table.ISequenceTable;
import ch.ahoegger.docbox.server.document.DocumentService;
import ch.ahoegger.docbox.server.hr.employee.EmployeeService;
import ch.ahoegger.docbox.server.hr.entity.EntityService;
import ch.ahoegger.docbox.shared.backup.IBackupService;
import ch.ahoegger.docbox.shared.document.DocumentFormData;
import ch.ahoegger.docbox.shared.document.DocumentFormData.Partners.PartnersRowData;
import ch.ahoegger.docbox.shared.hr.billing.IPostingGroupService;
import ch.ahoegger.docbox.shared.hr.billing.PostingCalculationBoxData;
import ch.ahoegger.docbox.shared.hr.billing.PostingCalculationBoxData.Entities.EntitiesRowData;
import ch.ahoegger.docbox.shared.hr.billing.PostingGroupCodeType.UnbilledCode;
import ch.ahoegger.docbox.shared.hr.billing.PostingGroupFormData;
import ch.ahoegger.docbox.shared.hr.billing.PostingGroupSearchFormData;
import ch.ahoegger.docbox.shared.hr.billing.PostingGroupTableData;
import ch.ahoegger.docbox.shared.hr.billing.PostingGroupTableData.PostingGroupTableRowData;
import ch.ahoegger.docbox.shared.hr.employee.EmployeeFormData;
import ch.ahoegger.docbox.shared.hr.employee.IEmployeeService;
import ch.ahoegger.docbox.shared.hr.entity.EntitySearchFormData;
import ch.ahoegger.docbox.shared.hr.entity.EntityTablePageData;
import ch.ahoegger.docbox.shared.hr.entity.EntityTablePageData.EntityTableRowData;
import ch.ahoegger.docbox.shared.hr.entity.EntityTypeCodeType.ExpenseCode;
import ch.ahoegger.docbox.shared.hr.entity.EntityTypeCodeType.WorkCode;
import ch.ahoegger.docbox.shared.hr.entity.IEntityService;
import ch.ahoegger.docbox.shared.util.LocalDateUtility;

/**
 * <h3>{@link PostingGroupService}</h3>
 *
 * @author Andreas Hoegger
 */
public class PostingGroupService implements IPostingGroupService {
  private static final Logger LOG = LoggerFactory.getLogger(PostingGroupService.class);

  @Override
  public PostingGroupTableData getTableData(PostingGroupSearchFormData formData) {
    Condition condition = DSL.trueCondition();
    PostingGroup pg = PostingGroup.POSTING_GROUP.as("PG");

    if (formData.getPartnerId() != null) {
      condition = pg.PARTNER_NR.eq(formData.getPartnerId());
    }

    List<PostingGroupTableRowData> rows = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .select(pg.POSTING_GROUP_NR, pg.PARTNER_NR, pg.DOCUMENT_NR, pg.NAME, pg.STATEMENT_DATE, pg.BRUTTO_WAGE, pg.NETTO_WAGE, pg.SOURCE_TAX, pg.SOCIAL_SECURITY_TAX, pg.VACATION_EXTRA)
        .from(pg)
        .where(condition)
        .fetch()
        .stream()
        .map(rec -> {
          PostingGroupTableRowData rd = new PostingGroupTableRowData();
          rd.setSortGroup(BigDecimal.valueOf(1));
          rd.setPartnerId(rec.get(pg.PARTNER_NR));
          rd.setDocumentId(rec.get(pg.DOCUMENT_NR));
          rd.setName(rec.get(pg.NAME));
          rd.setDate(rec.get(pg.STATEMENT_DATE));
          rd.setBruttoWage(rec.get(pg.BRUTTO_WAGE));
          rd.setNettoWage(rec.get(pg.NETTO_WAGE));
          rd.setSourceTax(rec.get(pg.SOURCE_TAX));
          rd.setSocialSecurityTax(rec.get(pg.SOCIAL_SECURITY_TAX));
          rd.setVacationExtra(rec.get(pg.VACATION_EXTRA));
          return rd;
        })
        .collect(Collectors.toList());

    // unbilled row
    PostingGroupTableRowData unbilledRow = new PostingGroupTableRowData();
    unbilledRow.setId(UnbilledCode.ID);
    unbilledRow.setSortGroup(BigDecimal.valueOf(2));
    unbilledRow.setPartnerId(formData.getPartnerId());
    unbilledRow.setName(TEXTS.get("Unbilled"));
    rows.add(unbilledRow);

    PostingGroupTableData tableData = new PostingGroupTableData();
    tableData.setRows(rows.toArray(new PostingGroupTableRowData[0]));
    return tableData;
  }

  @Override
  public PostingGroupFormData prepareCreate(PostingGroupFormData formData) {
    LocalDate today = LocalDate.now();
    formData.getDate().setValue(LocalDateUtility.toDate(today));

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
    return formData;
  }

  @Override
  public PostingGroupFormData create(PostingGroupFormData formData) {

    // create document
    EmployeeFormData employeeData = new EmployeeFormData();
    employeeData.setPartnerId(formData.getPartner().getValue());
    employeeData = BEANS.get(EmployeeService.class).load(employeeData);

    List<EntityTableRowData> unbilledEntities = getUnbilledEntities(formData.getFrom().getValue(), formData.getTo().getValue(), formData.getPartner().getValue());

    WageCalculation wageData = calculateWage(unbilledEntities, employeeData.getEmployeeBox().getHourlyWage().getValue());
    byte[] docContent = BEANS.get(WageReportService.class).createMonthlyReport(formData.getTitle().getValue(), employeeData.getEmployeeBox().getFirstName().getValue() + " " + employeeData.getEmployeeBox().getLastName().getValue(),
        employeeData.getEmployeeBox().getAddressLine1().getValue(), employeeData.getEmployeeBox().getAddressLine2().getValue(), formData.getDate().getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
        employeeData.getEmployeeBox().getAccountNumber().getValue(), employeeData.getEmployeeBox().getHourlyWage().getValue(), wageData,
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

    // create posting group
    BigDecimal postingGroupId = BigDecimal.valueOf(SQL.getSequenceNextval(ISequenceTable.TABLE_NAME));

    formData.setPostingGroupId(postingGroupId);

    PostingGroup pg = PostingGroup.POSTING_GROUP;
    int rowCouont = DSL.using(SQL.getConnection(), SQLDialect.DERBY).newRecord(pg)
        .with(pg.BRUTTO_WAGE, wageData.getBruttoWage())
        .with(pg.DOCUMENT_NR, formData.getDocumentId())
        .with(pg.NAME, formData.getTitle().getValue())
        .with(pg.NETTO_WAGE, wageData.getNettoWage())
        .with(pg.PARTNER_NR, formData.getPartner().getValue())
        .with(pg.POSTING_GROUP_NR, formData.getPostingGroupId())
        .with(pg.SOCIAL_SECURITY_TAX, wageData.getSocialSecuityTax())
        .with(pg.SOURCE_TAX, wageData.getSourceTax())
        .with(pg.STATEMENT_DATE, formData.getDate().getValue())
        .with(pg.VACATION_EXTRA, wageData.getVacationExtra())
        .with(pg.WORKING_HOURS, wageData.getHoursTotal())
        .insert();

    if (rowCouont == 1) {

      // update entities
      BEANS.get(EntityService.class).updateGroupId(unbilledEntities.stream().map(e -> e.getEnityId()).collect(Collectors.toSet()), postingGroupId);

      // notify backup needed
      BEANS.get(IBackupService.class).notifyModification();
      return formData;
    }
    return null;
  }

  @Override
  public PostingCalculationBoxData calculateWage(PostingGroupFormData formData) {
    EmployeeFormData employeeData = new EmployeeFormData();
    employeeData.setPartnerId(formData.getPartner().getValue());
    employeeData = BEANS.get(IEmployeeService.class).load(employeeData);

    List<EntityTableRowData> unbilledEntities = getUnbilledEntities(formData.getFrom().getValue(), formData.getTo().getValue(), formData.getPartner().getValue());
    WageCalculation wageCalc = calculateWage(unbilledEntities, employeeData.getEmployeeBox().getHourlyWage().getValue());

    PostingCalculationBoxData result = new PostingCalculationBoxData();
    List<EntitiesRowData> entityRows = unbilledEntities.stream().map(row -> {
      EntitiesRowData rd = new EntitiesRowData();
      rd.setAmount(row.getAmount());
      rd.setPostingGroupId(row.getPostingGroupId());
      rd.setDate(row.getDate());
      rd.setEnityId(row.getEnityId());
      rd.setEntityType(row.getEntityType());
      rd.setHours(row.getHours());
      rd.setPartnerId(row.getPartnerId());
      return rd;
    }).collect(Collectors.toList());

    result.getEntities().setRows(entityRows.toArray(new EntitiesRowData[0]));
    result.getBruttoWage().setValue(wageCalc.getBruttoWage());
    result.getNettoWage().setValue(wageCalc.getNettoWage());
    result.getWorkingHours().setValue(wageCalc.getHoursTotal());
    result.getSocialSecurityTax().setValue(wageCalc.getSocialSecuityTax());
    result.getSourceTax().setValue(wageCalc.getSourceTax());
    result.getVacationExtra().setValue(wageCalc.getVacationExtra());

    return result;
  }

  @Override
  public PostingGroupFormData load(PostingGroupFormData formData) {
    PostingGroup pg = PostingGroup.POSTING_GROUP;
    return toFormData(DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .fetchOne(pg, pg.POSTING_GROUP_NR.eq(formData.getPostingGroupId())));
  }

  @Override
  public boolean delete(BigDecimal postingGroupId) {

    PostingGroup pg = PostingGroup.POSTING_GROUP;

    PostingGroupRecord rec = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .fetchOne(pg, pg.POSTING_GROUP_NR.eq(postingGroupId));
    if (rec == null) {
      LOG.warn("Try to delete not existing record with id '{}'!", postingGroupId);
      return false;
    }
    if (rec.delete() != 1) {
      LOG.error("Could not delete record with id '{}'!", postingGroupId);
      return false;
    }

    BEANS.get(DocumentService.class).delete(rec.getDocumentNr());

    // update entities
    EntitySearchFormData entitySearchData = new EntitySearchFormData();
    entitySearchData.getPartnerId().setValue(rec.getPartnerNr());
    entitySearchData.setPostingGroupId(postingGroupId);
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
    entitySearchData.setPostingGroupId(UnbilledCode.ID);
    EntityTablePageData entityTableData = BEANS.get(IEntityService.class).getEntityTableData(entitySearchData);
    return CollectionUtility.arrayList(entityTableData.getRows());
  }

  protected WageCalculation calculateWage(List<EntityTableRowData> rows, BigDecimal hourlyWage) {
    List<Work> workItems = rows.stream().filter(row -> WorkCode.isEqual(row.getEntityType()))
        .map(row -> new Work().widthHours(row.getHours()).withDate(LocalDateUtility.toLocalDate(row.getDate())).withText(row.getText()))
        .collect(Collectors.toList());
    List<Expense> expenses = rows.stream().filter(row -> ExpenseCode.isEqual(row.getEntityType()))
        .map(row -> new Expense().withAmount(row.getAmount()).withDate(LocalDateUtility.toLocalDate(row.getDate())).withText(row.getText()))
        .collect(Collectors.toList());

    BigDecimal hoursWorked = workItems.stream()
        .map(workItem -> workItem.getHours())
        .reduce((h1, h2) -> h1.add(h2))
        .orElse(BigDecimal.ZERO)
        .setScale(2, RoundingMode.HALF_UP);

    BigDecimal expensesTotal = expenses.stream()
        .map(expense -> expense.getAmount())
        .reduce((h1, h2) -> h1.add(h2))
        .orElse(BigDecimal.ZERO)
        .setScale(2, RoundingMode.HALF_UP);

    BigDecimal bruttoWage = hoursWorked.multiply(hourlyWage).setScale(2, RoundingMode.HALF_UP);

    BigDecimal socialSecurityInsuranceRelative = CONFIG.getPropertyValue(PayslipProperties.SocialInsurancePercentageProperty.class).divide(BigDecimal.valueOf(-100.0));
    BigDecimal socialSecurityTax = socialSecurityInsuranceRelative.multiply(bruttoWage).setScale(2, RoundingMode.HALF_UP);
    BigDecimal sourceTaxRelative = CONFIG.getPropertyValue(PayslipProperties.SourceTaxPercentageProperty.class).divide(BigDecimal.valueOf(-100.0));
    BigDecimal sourceTax = sourceTaxRelative.multiply(bruttoWage).setScale(2, RoundingMode.HALF_UP);
    BigDecimal vacationExtraRelative = CONFIG.getPropertyValue(PayslipProperties.VacationExtraPercentageProperty.class).divide(BigDecimal.valueOf(100.0));
    BigDecimal vacationExtra = vacationExtraRelative.multiply(bruttoWage).setScale(2, RoundingMode.HALF_UP);
    BigDecimal nettoWage = bruttoWage.add(expensesTotal).add(socialSecurityTax).add(sourceTax).add(vacationExtra);

    WageCalculation result = new WageCalculation();
    result.setWorkItems(workItems);
    result.setExpenses(expenses);
    result.setBruttoWage(bruttoWage);
    result.setNettoWage(nettoWage);
    result.setExpencesTotal(expensesTotal);
    result.setHoursTotal(hoursWorked);
    result.setSocialSecuityTaxRelative(socialSecurityInsuranceRelative);
    result.setSocialSecuityTax(socialSecurityTax);
    result.setSourceTaxRelative(sourceTaxRelative);
    result.setSourceTax(sourceTax);
    result.setVacationExtraRelative(vacationExtraRelative);
    result.setVacationExtra(vacationExtra);
    return result;

  }

  protected PostingGroupFormData toFormData(PostingGroupRecord rec) {
    if (rec == null) {
      return null;
    }
    PostingGroupFormData fd = new PostingGroupFormData();
//    BRUTTO_WAGE : TableField<PostingGroupRecord, BigDecimal>
    fd.setDocumentId(rec.getDocumentNr());
    fd.getTitle().setValue(rec.getName());
//    NETTO_WAGE : TableField<PostingGroupRecord, BigDecimal>
    fd.getPartner().setValue(rec.getPartnerNr());
    fd.setPostingGroupId(rec.getPostingGroupNr());
//    SOCIAL_SECURITY_TAX : TableField<PostingGroupRecord, BigDecimal>
//    SOURCE_TAX : TableField<PostingGroupRecord, BigDecimal>
    fd.getDate().setValue(rec.getStatementDate());
//    VACATION_EXTRA : TableField<PostingGroupRecord, BigDecimal>
//    WORKING_HOURS : TableField<PostingGroupRecord, BigDecimal>
    return fd;

  }

}

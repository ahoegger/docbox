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
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.config.CONFIG;
import org.eclipse.scout.rt.platform.holders.NVPair;
import org.eclipse.scout.rt.platform.resource.BinaryResource;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.server.jdbc.SQL;
import org.eclipse.scout.rt.shared.TEXTS;

import ch.ahoegger.docbox.server.document.DocumentService;
import ch.ahoegger.docbox.server.hr.employee.EmployeeService;
import ch.ahoegger.docbox.server.hr.entity.EntityService;
import ch.ahoegger.docbox.shared.ISequenceTable;
import ch.ahoegger.docbox.shared.backup.IBackupService;
import ch.ahoegger.docbox.shared.document.DocumentFormData;
import ch.ahoegger.docbox.shared.document.DocumentFormData.Partners.PartnersRowData;
import ch.ahoegger.docbox.shared.hr.billing.IPostingGroupService;
import ch.ahoegger.docbox.shared.hr.billing.IPostingGroupTable;
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
import ch.ahoegger.docbox.shared.util.SqlFramentBuilder;

/**
 * <h3>{@link PostingGroupService}</h3>
 *
 * @author Andreas Hoegger
 */
public class PostingGroupService implements IPostingGroupService, IPostingGroupTable {

  @Override
  public PostingGroupTableData getTableData(PostingGroupSearchFormData formData) {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("SELECT 1, ").append(SqlFramentBuilder.columnsAliased(TABLE_ALIAS, POSTING_GROUP_NR, PARTNER_NR, DOCUMENT_NR, NAME, STATEMENT_DATE, BRUTTO_WAGE, NETTO_WAGE, SOURCE_TAX, SOCIAL_SECURITY_TAX, VACATION_EXTRA))
        .append(" FROM ").append(TABLE_NAME).append(" AS ").append(TABLE_ALIAS).append(" ")
        .append(SqlFramentBuilder.WHERE_DEFAULT);

    // search personId
    if (formData.getPartnerId() != null) {
      statementBuilder.append(" AND ").append(SqlFramentBuilder.where(TABLE_ALIAS, PARTNER_NR, formData.getPartnerId()));
    }
    // search criteria firstname
//    if (formData.getPartnerId().getValue() != null) {
//      statementBuilder.append(" AND ").append(SqlFramentBuilder.where(PARTNER_NR, formData.getPartnerId().getValue()));
//    }
//
//    if (formData.getBilledBox().getValue() != null) {
//      switch (formData.getBilledBox().getValue()) {
//        case TRUE:
//          statementBuilder.append(" AND (").append(SqlFramentBuilder.columnsAliased(TABLE_ALIAS, BILLED)).append(" <= ").append("CURRENT_DATE")
//              .append(" OR ").append(SqlFramentBuilder.columnsAliased(TABLE_ALIAS, BILLED)).append(" IS NULL)");
//          break;
//        case FALSE:
//          statementBuilder.append(" AND ").append(SqlFramentBuilder.columnsAliased(TABLE_ALIAS, BILLED)).append(" IS NULL ");
//          break;
//      }
//    }

    statementBuilder.append(" INTO ")
        .append(":{td.").append(PostingGroupTableRowData.sortGroup).append("}, ")
        .append(":{td.").append(PostingGroupTableRowData.id).append("}, ")
        .append(":{td.").append(PostingGroupTableRowData.partnerId).append("}, ")
        .append(":{td.").append(PostingGroupTableRowData.documentId).append("}, ")
        .append(":{td.").append(PostingGroupTableRowData.name).append("}, ")
        .append(":{td.").append(PostingGroupTableRowData.date).append("}, ")
        .append(":{td.").append(PostingGroupTableRowData.bruttoWage).append("}, ")
        .append(":{td.").append(PostingGroupTableRowData.nettoWage).append("}, ")
        .append(":{td.").append(PostingGroupTableRowData.sourceTax).append("}, ")
        .append(":{td.").append(PostingGroupTableRowData.socialSecurityTax).append("}, ")
        .append(":{td.").append(PostingGroupTableRowData.vacationExtra).append("} ");

    PostingGroupTableData tableData = new PostingGroupTableData();
    SQL.selectInto(statementBuilder.toString(),
        new NVPair("td", tableData),
        formData);
    PostingGroupTableRowData unbilledRow = tableData.addRow();
    unbilledRow.setId(UnbilledCode.ID);
    unbilledRow.setSortGroup(BigDecimal.valueOf(2));
    unbilledRow.setPartnerId(formData.getPartnerId());
    unbilledRow.setName(TEXTS.get("Unbilled"));
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
    documentService.prepareCreate(documentData);
    documentData.getDocumentDate().setValue(LocalDateUtility.today());
    documentData.getDocument().setValue(new BinaryResource("payslip.pdf", docContent));
    PartnersRowData partnerRow = documentData.getPartners().addRow();
    partnerRow.setPartner(formData.getPartner().getValue());
    documentData.getAbstract().setValue(formData.getDocumentAbstract().getValue());
    documentData = documentService.create(documentData);
    formData.setDocumentId(documentData.getDocumentId());

    // create posting group
    BigDecimal postingGroupId = BigDecimal.valueOf(SQL.getSequenceNextval(ISequenceTable.TABLE_NAME));
    formData.setPostingGroupId(postingGroupId);
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("INSERT INTO ").append(TABLE_NAME);
    statementBuilder.append(" (").append(SqlFramentBuilder.columns(POSTING_GROUP_NR, PARTNER_NR, DOCUMENT_NR, NAME, STATEMENT_DATE, WORKING_HOURS, BRUTTO_WAGE, NETTO_WAGE, SOURCE_TAX, SOCIAL_SECURITY_TAX, VACATION_EXTRA)).append(")");
    statementBuilder.append(" VALUES (:postingGroupId, :partner, :documentId, :title, :date, :hoursTotal, :bruttoWage, :nettoWage, :sourceTax, :socialSecuityTax, :vacationExtra)");
    SQL.insert(statementBuilder.toString(),
//        new NVPair("postingGroupId", postingGroupId),
        formData, wageData);

    // update entities
    BEANS.get(EntityService.class).updateGroupId(unbilledEntities.stream().map(e -> e.getEnityId()).collect(Collectors.toSet()), postingGroupId);

    // notify backup needed
    BEANS.get(IBackupService.class).notifyModification();
    return formData;
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
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("SELECT ").append(SqlFramentBuilder.columns(POSTING_GROUP_NR, PARTNER_NR, DOCUMENT_NR, NAME, STATEMENT_DATE));
    statementBuilder.append(" FROM ").append(TABLE_NAME);
    statementBuilder.append(" WHERE ").append(POSTING_GROUP_NR).append(" = :postingGroupId");
    statementBuilder.append(" INTO :postingGroupId, :partner, :documentId, :title, :date");
    SQL.selectInto(statementBuilder.toString(), formData);
    return formData;

  }

  @Override
  public void delete(BigDecimal postingGroupId) {
    PostingGroupFormData postingGroupData = new PostingGroupFormData();
    postingGroupData.setPostingGroupId(postingGroupId);
    postingGroupData = load(postingGroupData);
    BEANS.get(DocumentService.class).delete(postingGroupData.getDocumentId());

    // update entities
    EntitySearchFormData entitySearchData = new EntitySearchFormData();
    entitySearchData.getPartnerId().setValue(postingGroupData.getPartner().getValue());
    entitySearchData.setPostingGroupId(postingGroupId);
    EntityTablePageData entityTableData = BEANS.get(IEntityService.class).getEntityTableData(entitySearchData);

    BEANS.get(EntityService.class).updateGroupId(CollectionUtility.arrayList(entityTableData.getRows()).stream().map(e -> e.getEnityId()).collect(Collectors.toSet()), UnbilledCode.ID);

    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("DELETE FROM ").append(TABLE_NAME).append(" WHERE ").append(POSTING_GROUP_NR).append(" = :postingGroupId");
    SQL.delete(statementBuilder.toString(), new NVPair("postingGroupId", postingGroupId));

    // notify backup needed
    BEANS.get(IBackupService.class).notifyModification();

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

}

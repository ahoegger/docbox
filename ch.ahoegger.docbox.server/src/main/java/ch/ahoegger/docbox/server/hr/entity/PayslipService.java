package ch.ahoegger.docbox.server.hr.entity;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

import org.ch.ahoegger.docbox.jasper.Entity;
import org.ch.ahoegger.docbox.jasper.WageReportService;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.resource.BinaryResource;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;

import ch.ahoegger.docbox.server.document.DocumentService;
import ch.ahoegger.docbox.server.hr.employee.EmployeeService;
import ch.ahoegger.docbox.shared.document.DocumentFormData;
import ch.ahoegger.docbox.shared.document.DocumentFormData.Partners.PartnersRowData;
import ch.ahoegger.docbox.shared.hr.employee.EmployeeFormData;
import ch.ahoegger.docbox.shared.hr.entity.CreatePayslipPermission;
import ch.ahoegger.docbox.shared.hr.entity.EntitySearchFormData;
import ch.ahoegger.docbox.shared.hr.entity.EntityTablePageData;
import ch.ahoegger.docbox.shared.hr.entity.IPayslipService;
import ch.ahoegger.docbox.shared.hr.entity.PayslipFormData;
import ch.ahoegger.docbox.shared.util.LocalDateUtility;

public class PayslipService implements IPayslipService {

  @Override
  public DocumentFormData create(PayslipFormData formData) {
    if (!ACCESS.check(new CreatePayslipPermission())) {
      throw new VetoException(TEXTS.get("AuthorizationFailed"));
    }
    EmployeeFormData employeeData = new EmployeeFormData();
    employeeData.setPartnerId(formData.getPartnerId());
    employeeData = BEANS.get(EmployeeService.class).load(employeeData);

//    String title, String addressLine1, String addressLine2, String addressLine3, LocalDate date, List<Entity> entities, double hourWage, String iban) {

    byte[] documentContent = BEANS.get(WageReportService.class).createReport(formData.getTitle().getValue(), employeeData.getFirstName().getValue() + " " + employeeData.getLastName().getValue(),
        employeeData.getAddressLine1().getValue(), employeeData.getAddressLine2().getValue(), formData.getDate().getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
        transformEntities(formData.getEntityIds()), employeeData.getHourlyWage().getValue(), employeeData.getAccountNumber().getValue());
    // TODO [aho] add business logic here.

    DocumentFormData documentData = new DocumentFormData();

    DocumentService documentService = BEANS.get(DocumentService.class);
    documentService.prepareCreate(documentData);
    documentData.getDocumentDate().setValue(LocalDateUtility.today());
    documentData.getDocument().setValue(new BinaryResource("payslip.pdf", documentContent));
    PartnersRowData partnerRow = documentData.getPartners().addRow();
    partnerRow.setPartner(formData.getPartnerId());
    documentData.getAbstract().setValue("Reinigungsarbeiten: " + formData.getTitle().getValue());
    documentData = documentService.create(documentData);

    return documentData;
  }

  private List<Entity> transformEntities(List<BigDecimal> entityIds) {
    EntitySearchFormData searchFormData = new EntitySearchFormData();
    searchFormData.setEntityIds(entityIds);
    EntityTablePageData entityTableData = BEANS.get(EntityService.class).getEntityTableData(searchFormData);
    return CollectionUtility.arrayList(entityTableData.getRows()).stream()
        .map(row -> new Entity().withDate(row.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
            .withHoursWorked(row.getHours()))
        .collect(Collectors.toList());
  }
}

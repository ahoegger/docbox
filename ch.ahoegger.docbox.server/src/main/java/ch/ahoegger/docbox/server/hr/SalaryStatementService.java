package ch.ahoegger.docbox.server.hr;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDCheckBox;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.ch.ahoegger.docbox.server.or.app.tables.Address;
import org.ch.ahoegger.docbox.server.or.app.tables.Employer;
import org.ch.ahoegger.docbox.server.or.app.tables.EmployerTaxGroup;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.ProcessingStatus;
import org.eclipse.scout.rt.platform.resource.BinaryResource;
import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.platform.status.IStatus;
import org.eclipse.scout.rt.platform.util.IOUtility;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.server.jdbc.SQL;
import org.eclipse.scout.rt.shared.servicetunnel.RemoteServiceAccessDenied;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.server.hr.employee.EmployeeService;
import ch.ahoegger.docbox.server.hr.statement.StatementBean;
import ch.ahoegger.docbox.server.or.generator.table.EmployeeTableStatement;
import ch.ahoegger.docbox.shared.hr.employee.EmployeeFormData;
import ch.ahoegger.docbox.shared.hr.employee.EmployeeTaxGroupFormData;
import ch.ahoegger.docbox.shared.hr.tax.TaxCodeType.SourceTax;
import ch.ahoegger.docbox.shared.util.LocalDateUtility;

/**
 * <h3>{@link SalaryStatementService}</h3>
 *
 * @author aho
 */
public class SalaryStatementService implements IService {
  private static final Logger LOG = LoggerFactory.getLogger(EmployeeTableStatement.class);

  private NumberFormat m_integerFormatter;

  @PostConstruct
  protected void init() {
    m_integerFormatter = NumberFormat.getIntegerInstance(new Locale("de", "CH"));
    m_integerFormatter.setGroupingUsed(false);
  }

  public static void main(String[] args) {
    new SalaryStatementService().doIt();
  }

  /**
   * used to make the pdf writable.
   */
  public void doIt() {
    try {
      BinaryResource resouce = getResourceFromClassLoader(Paths.get("documents", "11LohnA-rechts-dfe.pdf"));
      PDDocument document = PDDocument.load(resouce.getContent());
      document.setAllSecurityToBeRemoved(true);
      // get the document catalog
      PDAcroForm acroForm = document.getDocumentCatalog().getAcroForm();

      // as there might not be an AcroForm entry a null check is necessary
      if (acroForm != null) {
        PDField f8 = acroForm.getField("8");
        f8.getWidgets().get(0).setActions(null);
        f8.setFieldFlags(0);
        PDField f11 = acroForm.getField("11");
        f11.setFieldFlags(0);
        f11.getWidgets().get(0).setActions(null);
        f11.setFieldFlags(0);

        document.save("D:\\temp\\max24h\\11LohnA-rechts-dfe.pdf");
        document.close();
      }
      else {
        System.out.println("acroForm NULL");
      }

    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  private BinaryResource getResourceFromClassLoader(Path path) throws IOException {
    InputStream in = null;
    try {
      URL resource = SalaryStatementService.class.getClassLoader().getResource(path.toString());
      in = resource.openStream();
      return new BinaryResource(path.getFileName().toString(), IOUtility.readBytes(in));
    }
    finally {
      try {
        if (in != null) {
          in.close();
        }
      }
      catch (IOException e) {
        // void
      }
    }
  }

  /**
   * @param formData
   * @param statement
   */
  @RemoteServiceAccessDenied
  public BinaryResource createSalaryStatement(EmployeeTaxGroupFormData formData, StatementBean statement) {
    EmployeeFormData employeeData = new EmployeeFormData();
    employeeData.setPartnerId(formData.getEmployeeId());
    employeeData = BEANS.get(EmployeeService.class).load(employeeData);

    PDDocument document = null;
    try {
      BinaryResource resouce = getResourceFromClassLoader(Paths.get("documents", "11LohnA-rechts-dfe.pdf"));
      document = PDDocument.load(resouce.getContent());
      document.setAllSecurityToBeRemoved(true);
      // get the document catalog
      PDAcroForm acroForm = document.getDocumentCatalog().getAcroForm();

      // as there might not be an AcroForm entry a null check is necessary
      if (acroForm != null) {
        removeFieldActions(acroForm);

        ((PDCheckBox) acroForm.getField("A")).check();
        acroForm.getField("C2").setValue(employeeData.getEmployeeBox().getAhvNumber().getValue());
        acroForm.getField("D").setValue(LocalDateUtility.format(formData.getPeriodBox().getFrom().getValue(), LocalDateUtility.DATE_FORMATTER_yyyy));
        acroForm.getField("E-von").setValue(LocalDateUtility.format(formData.getPeriodBox().getFrom().getValue(), LocalDateUtility.DATE_FORMATTER_ddMMyyyy));
        acroForm.getField("E-bis").setValue(LocalDateUtility.format(formData.getPeriodBox().getTo().getValue(), LocalDateUtility.DATE_FORMATTER_ddMMyyyy));
        ((PDCheckBox) acroForm.getField("G")).check(); // todo kantinenverpflegung
        acroForm.getField("HName").setValue(employeeData.getEmployeeBox().getFirstName().getValue() + " " + employeeData.getEmployeeBox().getLastName().getValue());
        acroForm.getField("HAdresse").setValue(employeeData.getEmployeeBox().getAddressBox().getLine1().getValue());
        acroForm.getField("HWohnort").setValue(employeeData.getEmployeeBox().getAddressBox().getPlz().getValue() + " " + employeeData.getEmployeeBox().getAddressBox().getCity().getValue());
        BigDecimal brutto = statement.getBruttoWage().setScale(0, RoundingMode.HALF_UP);
        acroForm.getField("1").setValue(m_integerFormatter.format(brutto));

        acroForm.getField("8").setValue(m_integerFormatter.format(brutto));
        BigDecimal socialInsuranceTax = statement.getSocialInsuranceTax().setScale(0, RoundingMode.HALF_UP);
        acroForm.getField("9").setValue(m_integerFormatter.format(socialInsuranceTax));

        if (formData.getWageBox().getPensionsFund().getValue() != null && !BigDecimal.ZERO.equals(formData.getWageBox().getPensionsFund().getValue())) {
          acroForm.getField("10-1").setValue(m_integerFormatter.format(formData.getWageBox().getPensionsFund().getValue()));
        }

        BigDecimal netto = brutto
            .subtract(socialInsuranceTax)
            .subtract(formData.getWageBox().getPensionsFund().getValue())
            .setScale(0, RoundingMode.HALF_UP);
        acroForm.getField("11").setValue(m_integerFormatter.format(netto));

        if (SourceTax.ID.equals(employeeData.getEmploymentBox().getTaxType().getValue())) {
          BigDecimal sourceTax = statement.getSourceTax().setScale(0, RoundingMode.HALF_UP);
          acroForm.getField("12").setValue(m_integerFormatter.format(sourceTax));
          acroForm.getField("15-1").setValue("Der Lohn ist quellenbesteuert und somit vom Arbeitnehmer nicht zu versteuern.");
        }

        fillEmployerData(formData.getEmployerTaxGroupId(), acroForm);

        acroForm.refreshAppearances();
        // save
        acroForm.flatten();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        document.save(out);

        BinaryResource result = new BinaryResource("salaryStatement.pdf", out.toByteArray());
        return result;
      }
      else {
        throw new ProcessingException("acronForm is null!");
      }
    }
    catch (IOException e) {
      throw new ProcessingException(new ProcessingStatus("Could not write salary statement!", e, 0, IStatus.ERROR));
    }
    finally {
      if (document != null) {
        try {
          document.close();
        }
        catch (IOException e) {
          LOG.warn("Could not close PDDcoument!", e);
        }
      }
    }
  }

  private void fillEmployerData(BigDecimal employerTaxGroupId, PDAcroForm acroForm) throws IllegalArgumentException, IOException {
    EmployerTaxGroup erTaxGroup = EmployerTaxGroup.EMPLOYER_TAX_GROUP;
    Employer e = Employer.EMPLOYER;
    Address a = Address.ADDRESS;
    Record rec = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .select(e.NAME, e.PHONE, a.LINE_1, a.LINE_2, a.PLZ, a.CITY)
        .from(erTaxGroup)
        .innerJoin(e).on(erTaxGroup.EMPLOYER_NR.eq(e.EMPLOYER_NR))
        .leftOuterJoin(a).on(e.ADDRESS_NR.eq(a.ADDRESS_NR))
        .where(erTaxGroup.EMPLOYER_TAX_GROUP_NR.eq(employerTaxGroupId))
        .fetchOne();

    acroForm.getField("OrtDatum").setValue(rec.get(a.CITY) + ", " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
    // Address lines
    List<String> addList = new ArrayList<>();
    addList.add(rec.get(e.NAME));
    addList.add(rec.get(a.LINE_1));
    if (StringUtility.hasText(rec.get(a.LINE_2))) {
      addList.add(rec.get(a.LINE_2));
    }
    addList.add(rec.get(a.PLZ) + " " + rec.get(a.CITY));
    String[] keyList = new String[]{"Unterschrift1.1",
        "Unterschrift1.2",
        "Unterschrift1.3",
        "Unterschrift1.4"};
    addList.add(rec.get(e.PHONE));
    if (addList.size() == 5) {
      // "Unterschrift1",
      keyList = new String[]{
          "Unterschrift1.0",
          "Unterschrift1.1",
          "Unterschrift1.2",
          "Unterschrift1.3",
          "Unterschrift1.4"};
    }
    for (int i = 0; i < keyList.length; i++) {
      acroForm.getField(keyList[i]).setValue(addList.get(i));
    }
  }

  protected void removeFieldActions(PDAcroForm acroForm) {
    PDField f8 = acroForm.getField("8");
    f8.getWidgets().get(0).setActions(null);
    f8.setFieldFlags(0);
    PDField f11 = acroForm.getField("11");
    f11.setFieldFlags(0);
    f11.getWidgets().get(0).setActions(null);
    f11.setFieldFlags(0);
  }

}

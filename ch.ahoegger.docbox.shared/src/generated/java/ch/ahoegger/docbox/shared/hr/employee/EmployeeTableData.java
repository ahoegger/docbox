package ch.ahoegger.docbox.shared.hr.employee;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Generated;

import org.eclipse.scout.rt.shared.data.basic.table.AbstractTableRowData;
import org.eclipse.scout.rt.shared.data.page.AbstractTablePageData;

/**
 * <b>NOTE:</b><br>
 * This class is auto generated by the Scout SDK. No manual modifications recommended.
 */
@Generated(value = "ch.ahoegger.docbox.client.hr.employee.EmployeeTablePage", comments = "This class is auto generated by the Scout SDK. No manual modifications recommended.")
public class EmployeeTableData extends AbstractTablePageData {

  private static final long serialVersionUID = 1L;

  @Override
  public EmployeeTableRowData addRow() {
    return (EmployeeTableRowData) super.addRow();
  }

  @Override
  public EmployeeTableRowData addRow(int rowState) {
    return (EmployeeTableRowData) super.addRow(rowState);
  }

  @Override
  public EmployeeTableRowData createRow() {
    return new EmployeeTableRowData();
  }

  @Override
  public Class<? extends AbstractTableRowData> getRowType() {
    return EmployeeTableRowData.class;
  }

  @Override
  public EmployeeTableRowData[] getRows() {
    return (EmployeeTableRowData[]) super.getRows();
  }

  @Override
  public EmployeeTableRowData rowAt(int index) {
    return (EmployeeTableRowData) super.rowAt(index);
  }

  public void setRows(EmployeeTableRowData[] rows) {
    super.setRows(rows);
  }

  public static class EmployeeTableRowData extends AbstractTableRowData {

    private static final long serialVersionUID = 1L;
    public static final String partnerId = "partnerId";
    public static final String displayName = "displayName";
    public static final String firstName = "firstName";
    public static final String lastName = "lastName";
    public static final String addressLine1 = "addressLine1";
    public static final String addressLine2 = "addressLine2";
    public static final String plz = "plz";
    public static final String city = "city";
    public static final String aHVNumber = "aHVNumber";
    public static final String accountNumber = "accountNumber";
    public static final String taxType = "taxType";
    public static final String birthday = "birthday";
    public static final String hourlyWage = "hourlyWage";
    public static final String startDate = "startDate";
    public static final String endDate = "endDate";
    private BigDecimal m_partnerId;
    private String m_displayName;
    private String m_firstName;
    private String m_lastName;
    private String m_addressLine1;
    private String m_addressLine2;
    private String m_plz;
    private String m_city;
    private String m_aHVNumber;
    private String m_accountNumber;
    private BigDecimal m_taxType;
    private Date m_birthday;
    private BigDecimal m_hourlyWage;
    private Date m_startDate;
    private Date m_endDate;

    public BigDecimal getPartnerId() {
      return m_partnerId;
    }

    public void setPartnerId(BigDecimal newPartnerId) {
      m_partnerId = newPartnerId;
    }

    public String getDisplayName() {
      return m_displayName;
    }

    public void setDisplayName(String newDisplayName) {
      m_displayName = newDisplayName;
    }

    public String getFirstName() {
      return m_firstName;
    }

    public void setFirstName(String newFirstName) {
      m_firstName = newFirstName;
    }

    public String getLastName() {
      return m_lastName;
    }

    public void setLastName(String newLastName) {
      m_lastName = newLastName;
    }

    public String getAddressLine1() {
      return m_addressLine1;
    }

    public void setAddressLine1(String newAddressLine1) {
      m_addressLine1 = newAddressLine1;
    }

    public String getAddressLine2() {
      return m_addressLine2;
    }

    public void setAddressLine2(String newAddressLine2) {
      m_addressLine2 = newAddressLine2;
    }

    public String getPlz() {
      return m_plz;
    }

    public void setPlz(String newPlz) {
      m_plz = newPlz;
    }

    public String getCity() {
      return m_city;
    }

    public void setCity(String newCity) {
      m_city = newCity;
    }

    public String getAHVNumber() {
      return m_aHVNumber;
    }

    public void setAHVNumber(String newAHVNumber) {
      m_aHVNumber = newAHVNumber;
    }

    public String getAccountNumber() {
      return m_accountNumber;
    }

    public void setAccountNumber(String newAccountNumber) {
      m_accountNumber = newAccountNumber;
    }

    public BigDecimal getTaxType() {
      return m_taxType;
    }

    public void setTaxType(BigDecimal newTaxType) {
      m_taxType = newTaxType;
    }

    public Date getBirthday() {
      return m_birthday;
    }

    public void setBirthday(Date newBirthday) {
      m_birthday = newBirthday;
    }

    public BigDecimal getHourlyWage() {
      return m_hourlyWage;
    }

    public void setHourlyWage(BigDecimal newHourlyWage) {
      m_hourlyWage = newHourlyWage;
    }

    public Date getStartDate() {
      return m_startDate;
    }

    public void setStartDate(Date newStartDate) {
      m_startDate = newStartDate;
    }

    public Date getEndDate() {
      return m_endDate;
    }

    public void setEndDate(Date newEndDate) {
      m_endDate = newEndDate;
    }
  }
}

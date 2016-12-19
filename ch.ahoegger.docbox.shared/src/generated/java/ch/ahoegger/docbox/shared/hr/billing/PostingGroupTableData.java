package ch.ahoegger.docbox.shared.hr.billing;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Generated;

import org.eclipse.scout.rt.shared.data.basic.table.AbstractTableRowData;
import org.eclipse.scout.rt.shared.data.page.AbstractTablePageData;

/**
 * <b>NOTE:</b><br>
 * This class is auto generated by the Scout SDK. No manual modifications recommended.
 */
@Generated(value = "ch.ahoegger.docbox.client.hr.billing.PostingGroupTablePage", comments = "This class is auto generated by the Scout SDK. No manual modifications recommended.")
public class PostingGroupTableData extends AbstractTablePageData {

  private static final long serialVersionUID = 1L;

  @Override
  public PostingGroupTableRowData addRow() {
    return (PostingGroupTableRowData) super.addRow();
  }

  @Override
  public PostingGroupTableRowData addRow(int rowState) {
    return (PostingGroupTableRowData) super.addRow(rowState);
  }

  @Override
  public PostingGroupTableRowData createRow() {
    return new PostingGroupTableRowData();
  }

  @Override
  public Class<? extends AbstractTableRowData> getRowType() {
    return PostingGroupTableRowData.class;
  }

  @Override
  public PostingGroupTableRowData[] getRows() {
    return (PostingGroupTableRowData[]) super.getRows();
  }

  @Override
  public PostingGroupTableRowData rowAt(int index) {
    return (PostingGroupTableRowData) super.rowAt(index);
  }

  public void setRows(PostingGroupTableRowData[] rows) {
    super.setRows(rows);
  }

  public static class PostingGroupTableRowData extends AbstractTableRowData {

    private static final long serialVersionUID = 1L;
    public static final String sortGroup = "sortGroup";
    public static final String id = "id";
    public static final String partnerId = "partnerId";
    public static final String documentId = "documentId";
    public static final String name = "name";
    public static final String date = "date";
    public static final String bruttoWage = "bruttoWage";
    public static final String nettoWage = "nettoWage";
    public static final String sourceTax = "sourceTax";
    public static final String socialSecurityTax = "socialSecurityTax";
    public static final String vacationExtra = "vacationExtra";
    private BigDecimal m_sortGroup;
    private BigDecimal m_id;
    private BigDecimal m_partnerId;
    private BigDecimal m_documentId;
    private String m_name;
    private Date m_date;
    private BigDecimal m_bruttoWage;
    private BigDecimal m_nettoWage;
    private BigDecimal m_sourceTax;
    private BigDecimal m_socialSecurityTax;
    private BigDecimal m_vacationExtra;

    public BigDecimal getSortGroup() {
      return m_sortGroup;
    }

    public void setSortGroup(BigDecimal newSortGroup) {
      m_sortGroup = newSortGroup;
    }

    public BigDecimal getId() {
      return m_id;
    }

    public void setId(BigDecimal newId) {
      m_id = newId;
    }

    public BigDecimal getPartnerId() {
      return m_partnerId;
    }

    public void setPartnerId(BigDecimal newPartnerId) {
      m_partnerId = newPartnerId;
    }

    public BigDecimal getDocumentId() {
      return m_documentId;
    }

    public void setDocumentId(BigDecimal newDocumentId) {
      m_documentId = newDocumentId;
    }

    public String getName() {
      return m_name;
    }

    public void setName(String newName) {
      m_name = newName;
    }

    public Date getDate() {
      return m_date;
    }

    public void setDate(Date newDate) {
      m_date = newDate;
    }

    public BigDecimal getBruttoWage() {
      return m_bruttoWage;
    }

    public void setBruttoWage(BigDecimal newBruttoWage) {
      m_bruttoWage = newBruttoWage;
    }

    public BigDecimal getNettoWage() {
      return m_nettoWage;
    }

    public void setNettoWage(BigDecimal newNettoWage) {
      m_nettoWage = newNettoWage;
    }

    public BigDecimal getSourceTax() {
      return m_sourceTax;
    }

    public void setSourceTax(BigDecimal newSourceTax) {
      m_sourceTax = newSourceTax;
    }

    public BigDecimal getSocialSecurityTax() {
      return m_socialSecurityTax;
    }

    public void setSocialSecurityTax(BigDecimal newSocialSecurityTax) {
      m_socialSecurityTax = newSocialSecurityTax;
    }

    public BigDecimal getVacationExtra() {
      return m_vacationExtra;
    }

    public void setVacationExtra(BigDecimal newVacationExtra) {
      m_vacationExtra = newVacationExtra;
    }
  }
}

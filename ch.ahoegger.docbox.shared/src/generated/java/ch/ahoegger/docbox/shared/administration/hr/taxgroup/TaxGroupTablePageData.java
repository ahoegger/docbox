package ch.ahoegger.docbox.shared.administration.hr.taxgroup;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Generated;

import org.eclipse.scout.rt.shared.data.basic.table.AbstractTableRowData;
import org.eclipse.scout.rt.shared.data.page.AbstractTablePageData;

/**
 * <b>NOTE:</b><br>
 * This class is auto generated by the Scout SDK. No manual modifications recommended.
 */
@Generated(value = "ch.ahoegger.docbox.client.administration.hr.taxgroup.TaxGroupTablePage", comments = "This class is auto generated by the Scout SDK. No manual modifications recommended.")
public class TaxGroupTablePageData extends AbstractTablePageData {

  private static final long serialVersionUID = 1L;

  @Override
  public TaxGroupTableRowData addRow() {
    return (TaxGroupTableRowData) super.addRow();
  }

  @Override
  public TaxGroupTableRowData addRow(int rowState) {
    return (TaxGroupTableRowData) super.addRow(rowState);
  }

  @Override
  public TaxGroupTableRowData createRow() {
    return new TaxGroupTableRowData();
  }

  @Override
  public Class<? extends AbstractTableRowData> getRowType() {
    return TaxGroupTableRowData.class;
  }

  @Override
  public TaxGroupTableRowData[] getRows() {
    return (TaxGroupTableRowData[]) super.getRows();
  }

  @Override
  public TaxGroupTableRowData rowAt(int index) {
    return (TaxGroupTableRowData) super.rowAt(index);
  }

  public void setRows(TaxGroupTableRowData[] rows) {
    super.setRows(rows);
  }

  public static class TaxGroupTableRowData extends AbstractTableRowData {

    private static final long serialVersionUID = 1L;
    public static final String taxGroupId = "taxGroupId";
    public static final String name = "name";
    public static final String startDate = "startDate";
    public static final String endDate = "endDate";
    private BigDecimal m_taxGroupId;
    private String m_name;
    private Date m_startDate;
    private Date m_endDate;

    public BigDecimal getTaxGroupId() {
      return m_taxGroupId;
    }

    public void setTaxGroupId(BigDecimal newTaxGroupId) {
      m_taxGroupId = newTaxGroupId;
    }

    public String getName() {
      return m_name;
    }

    public void setName(String newName) {
      m_name = newName;
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

package ch.ahoegger.docbox.shared.conversation;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Generated;

import org.eclipse.scout.rt.shared.data.basic.table.AbstractTableRowData;
import org.eclipse.scout.rt.shared.data.page.AbstractTablePageData;

/**
 * <b>NOTE:</b><br>
 * This class is auto generated by the Scout SDK. No manual modifications recommended.
 */
@Generated(value = "ch.ahoegger.docbox.client.category.CategoryTablePage", comments = "This class is auto generated by the Scout SDK. No manual modifications recommended.")
public class ConversationTableData extends AbstractTablePageData {

  private static final long serialVersionUID = 1L;

  @Override
  public ConversationTableRowData addRow() {
    return (ConversationTableRowData) super.addRow();
  }

  @Override
  public ConversationTableRowData addRow(int rowState) {
    return (ConversationTableRowData) super.addRow(rowState);
  }

  @Override
  public ConversationTableRowData createRow() {
    return new ConversationTableRowData();
  }

  @Override
  public Class<? extends AbstractTableRowData> getRowType() {
    return ConversationTableRowData.class;
  }

  @Override
  public ConversationTableRowData[] getRows() {
    return (ConversationTableRowData[]) super.getRows();
  }

  @Override
  public ConversationTableRowData rowAt(int index) {
    return (ConversationTableRowData) super.rowAt(index);
  }

  public void setRows(ConversationTableRowData[] rows) {
    super.setRows(rows);
  }

  public static class ConversationTableRowData extends AbstractTableRowData {

    private static final long serialVersionUID = 1L;
    public static final String conversationId = "conversationId";
    public static final String name = "name";
    public static final String startDate = "startDate";
    public static final String endDate = "endDate";
    private BigDecimal m_conversationId;
    private String m_name;
    private Date m_startDate;
    private Date m_endDate;

    public BigDecimal getConversationId() {
      return m_conversationId;
    }

    public void setConversationId(BigDecimal newConversationId) {
      m_conversationId = newConversationId;
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

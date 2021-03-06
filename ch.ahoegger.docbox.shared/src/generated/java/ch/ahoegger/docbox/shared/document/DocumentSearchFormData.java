package ch.ahoegger.docbox.shared.document;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import javax.annotation.Generated;

import org.eclipse.scout.rt.platform.util.TriState;
import org.eclipse.scout.rt.shared.data.basic.table.AbstractTableRowData;
import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;
import org.eclipse.scout.rt.shared.data.form.fields.tablefield.AbstractTableFieldBeanData;

/**
 * <b>NOTE:</b><br>
 * This class is auto generated by the Scout SDK. No manual modifications recommended.
 */
@Generated(value = "ch.ahoegger.docbox.client.document.DocumentSearchForm", comments = "This class is auto generated by the Scout SDK. No manual modifications recommended.")
public class DocumentSearchFormData extends AbstractFormData {

  private static final long serialVersionUID = 1L;

  public Abstract getAbstract() {
    return getFieldByClass(Abstract.class);
  }

  public ActiveBox getActiveBox() {
    return getFieldByClass(ActiveBox.class);
  }

  public CategoriesBox getCategoriesBox() {
    return getFieldByClass(CategoriesBox.class);
  }

  public Conversation getConversation() {
    return getFieldByClass(Conversation.class);
  }

  public DocumentDateFrom getDocumentDateFrom() {
    return getFieldByClass(DocumentDateFrom.class);
  }

  public DocumentDateTo getDocumentDateTo() {
    return getFieldByClass(DocumentDateTo.class);
  }

  public OcrSearchTable getOcrSearchTable() {
    return getFieldByClass(OcrSearchTable.class);
  }

  public Owner getOwner() {
    return getFieldByClass(Owner.class);
  }

  public ParseFailure getParseFailure() {
    return getFieldByClass(ParseFailure.class);
  }

  public ParsedContentBox getParsedContentBox() {
    return getFieldByClass(ParsedContentBox.class);
  }

  public Partner getPartner() {
    return getFieldByClass(Partner.class);
  }

  public static class Abstract extends AbstractValueFieldData<String> {

    private static final long serialVersionUID = 1L;
  }

  public static class ActiveBox extends AbstractValueFieldData<TriState> {

    private static final long serialVersionUID = 1L;
  }

  public static class CategoriesBox extends AbstractValueFieldData<Set<BigDecimal>> {

    private static final long serialVersionUID = 1L;
  }

  public static class Conversation extends AbstractValueFieldData<BigDecimal> {

    private static final long serialVersionUID = 1L;
  }

  public static class DocumentDateFrom extends AbstractValueFieldData<Date> {

    private static final long serialVersionUID = 1L;
  }

  public static class DocumentDateTo extends AbstractValueFieldData<Date> {

    private static final long serialVersionUID = 1L;
  }

  public static class OcrSearchTable extends AbstractTableFieldBeanData {

    private static final long serialVersionUID = 1L;

    @Override
    public OcrSearchTableRowData addRow() {
      return (OcrSearchTableRowData) super.addRow();
    }

    @Override
    public OcrSearchTableRowData addRow(int rowState) {
      return (OcrSearchTableRowData) super.addRow(rowState);
    }

    @Override
    public OcrSearchTableRowData createRow() {
      return new OcrSearchTableRowData();
    }

    @Override
    public Class<? extends AbstractTableRowData> getRowType() {
      return OcrSearchTableRowData.class;
    }

    @Override
    public OcrSearchTableRowData[] getRows() {
      return (OcrSearchTableRowData[]) super.getRows();
    }

    @Override
    public OcrSearchTableRowData rowAt(int index) {
      return (OcrSearchTableRowData) super.rowAt(index);
    }

    public void setRows(OcrSearchTableRowData[] rows) {
      super.setRows(rows);
    }

    public static class OcrSearchTableRowData extends AbstractTableRowData {

      private static final long serialVersionUID = 1L;
      public static final String searchText = "searchText";
      private String m_searchText;

      public String getSearchText() {
        return m_searchText;
      }

      public void setSearchText(String newSearchText) {
        m_searchText = newSearchText;
      }
    }
  }

  public static class Owner extends AbstractValueFieldData<String> {

    private static final long serialVersionUID = 1L;
  }

  public static class ParseFailure extends AbstractValueFieldData<Boolean> {

    private static final long serialVersionUID = 1L;
  }

  public static class ParsedContentBox extends AbstractValueFieldData<TriState> {

    private static final long serialVersionUID = 1L;
  }

  public static class Partner extends AbstractValueFieldData<BigDecimal> {

    private static final long serialVersionUID = 1L;
  }
}
